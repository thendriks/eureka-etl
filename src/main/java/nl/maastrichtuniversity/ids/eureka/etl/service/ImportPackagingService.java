package nl.maastrichtuniversity.ids.eureka.etl.service;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import lombok.extern.slf4j.Slf4j;
import nl.maastrichtuniversity.ids.eureka.etl.config.BitStreamConfig;
import nl.maastrichtuniversity.ids.eureka.etl.config.CrawlerConfig;
import nl.maastrichtuniversity.ids.eureka.etl.util.SafPackage;
import nl.maastrichtuniversity.ids.eureka.etl.domain.KnowledgeObject;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Slf4j
@Service
public class ImportPackagingService {
    public static final String COLLECTION_ID = "collection-id";
    public static final String SOURCE = "source";
    public static final String MAPFILE = "mapfile";
    private CrawlerConfig crawlerConfig;
    private BitStreamConfig bitStreamConfig;

    public ImportPackagingService(BitStreamConfig bitStreamConfig,
                                  CrawlerConfig crawlerConfig) {
        this.crawlerConfig = crawlerConfig;
        this.bitStreamConfig = bitStreamConfig;
    }

    @Scheduled(initialDelay=1000, fixedDelay=Long.MAX_VALUE) //May give issues when we're all dead
//    @Scheduled(cron = "0 * * * * ?")
    public void downloadBitStreams() throws IOException {
        log.info("downloading bitstreams");
        List<KnowledgeObject> knowledgeObjects = parseMetaDataCsv();
        log.info("Knowledge objects: {}", Arrays.toString(knowledgeObjects.toArray()));
        Path registrationFolderPath = createFolderPath(bitStreamConfig.getAssetFolder());
        Map<Boolean, List<KnowledgeObject>> objectsByHasBinary = knowledgeObjects.stream()
                .collect(Collectors.partitioningBy(object -> object.getFileName() != null));
        for(KnowledgeObject knowledgeObject : objectsByHasBinary.get(true)){
            try {
                downloadFile(knowledgeObject.getFileName(), knowledgeObject.getUrl(), registrationFolderPath);
            } catch (WebClientResponseException e){
                log.error("Unable to download file from location {}", e, knowledgeObject.getUrl());
            }
        }
        Path metadataFolderPath = createFolderPath(bitStreamConfig.getMetadataFolder());
        createMetaData(objectsByHasBinary.get(true), metadataFolderPath);
        requestUploadForToday(metadataFolderPath.getFileName());
    }

    private void createMetaData(List<KnowledgeObject> knowledgeObjects, Path metadataFolderPath) throws IOException {
        log.info("Creating metadata for path: {}", metadataFolderPath);
        SafPackage safPackage = new SafPackage(metadataFolderPath, Paths.get(getCurrentFormattedDate()));
        safPackage.setStoreNumber("1"); //TODO: parameterize
        safPackage.processMetaPack(knowledgeObjects);
    }

    private Path createFolderPath(Path parentFolder) throws IOException {
        String currentDateFormatted = getCurrentFormattedDate();
        final Path folderPath = FileSystems.getDefault().getPath(parentFolder.toString(), currentDateFormatted);
        return Files.createDirectories(folderPath,
                PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwxrwxrwx")));
    }

    @NotNull
    private String getCurrentFormattedDate() {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date currentDate = new Date();
        return simpleDateFormat.format(currentDate);
    }

    private void requestUploadForToday(Path fileName) {
        Path uploadFolderPath = bitStreamConfig.getDspaceMetadataFolder().resolve(fileName);
        log.info("Uploading knowledge objects for path: {}", uploadFolderPath);
        WebClient webClient = WebClient.create(bitStreamConfig.getDspaceUrl());
        WebClient.RequestBodySpec request = webClient.post()
                .uri(builder -> builder.path(bitStreamConfig.getRegistrationPath())
                        .queryParam(SOURCE, uploadFolderPath.toString())
                        .queryParam(COLLECTION_ID, bitStreamConfig.getCollectionId())
                        .queryParam(MAPFILE, MAPFILE)
                        .build());
        try {
            String response = request.exchange().block().bodyToMono(String.class).block();
            log.info("Upload response: {}.", response);
        } catch(WebClientException e){
            log.error("Error requesting knowledge objects upload.", e);
        }
    }

    private List<KnowledgeObject> parseMetaDataCsv() throws IOException {
        Path metaData = crawlerConfig.getMetaDataCsv();
        List<KnowledgeObject> knowledgeObjects;
        try (Reader beanReader = Files.newBufferedReader(metaData)) {
            CsvToBean<KnowledgeObject> csvToBean = new CsvToBeanBuilder<KnowledgeObject>(beanReader)
                    .withType(KnowledgeObject.class)
                    .withSeparator(';')
                    .withQuoteChar('"')
                    .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                    .build();
            knowledgeObjects = csvToBean.parse();
        }

        try (Reader optionalReader = Files.newBufferedReader(metaData)) {
            CSVParser parser = new CSVParserBuilder()
                    .withSeparator(';')
                    .withQuoteChar('"')
                    .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                    .build();
            CSVReaderHeaderAware mapReader = new CSVReaderHeaderAware(optionalReader, 0, parser, false,
                                                          true, 0, null);

            for(KnowledgeObject knowledgeObject : knowledgeObjects) {
                knowledgeObject.setOptionalMetaData(mapReader.readMap());
                String fileSize = knowledgeObject.getOptionalMetaData().get("knowledgeobject.filesize");
                if(fileSize != null && !fileSize.isBlank() && !"NA".equals(fileSize)){ //If filesize is blank assume there is no binary data.
                    String location = knowledgeObject.getUrl();
                    String fileName = location.substring(location.lastIndexOf("/") + 1);
                    knowledgeObject.setFileName(fileName);
                }
            }
        }
        return knowledgeObjects;
    }

    private void downloadFile(String fileName, String url, Path folderPath) {
        log.info("Downloading bitstreams from {}", url);
        final WebClient client = WebClient.create(url);
        final Flux<DataBuffer> dataBufferFlux = client.get()
                .accept(MediaType.TEXT_HTML)
                .retrieve()
                .bodyToFlux(DataBuffer.class);
        Path destination = Paths.get(folderPath.toString(), fileName);
        if(!destination.toFile().exists()){
            DataBufferUtils
                    .write(dataBufferFlux, destination, CREATE_NEW)
                    .block();
        } else {
            log.warn("File already exists at {}.", destination);
        }
    }
}