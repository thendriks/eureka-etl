package nl.maastrichtuniversity.ids.eureka.etl.service;

import lombok.extern.slf4j.Slf4j;
import nl.maastrichtuniversity.ids.eureka.etl.config.BitStreamConfig;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.*;
import java.nio.file.Path;

@Slf4j
@Service
public class ContentExtractionService {
    private BitStreamConfig bitStreamConfig;

    public ContentExtractionService(BitStreamConfig bitStreamConfig) {
        this.bitStreamConfig = bitStreamConfig;
    }

    public void uploadLanguageToDspace(){

    }

    public void detectLanguage(){

    }

    public String detectDocTypeUsingFacade()
            throws IOException {
        Path assetFolder = bitStreamConfig.getAssetFolder();
        File[] pdfFiles = assetFolder.toFile().listFiles();
//        Stream<Path> files = Files.walk(assetFolder);
//        File pdfFile = files.findFirst().get().toFile();
        File pdfFile = pdfFiles[0];
        log.info(pdfFile.getAbsolutePath());
        InputStream inputStream = new BufferedInputStream(new FileInputStream(pdfFile));
        Tika tika = new Tika();
        return tika.detect(inputStream);
    }

    public String extractContentUsingFacade()
            throws IOException, TikaException {
        InputStream inputStream = new BufferedInputStream(new FileInputStream("annual-report.pdf"));
        Tika tika = new Tika();
        return tika.parseToString(inputStream);
    }

    public Metadata extractMetadatatUsingParser()
            throws IOException, SAXException, TikaException {
        InputStream inputStream = new BufferedInputStream(new FileInputStream("annual-report.pdf"));
        Parser parser = new AutoDetectParser();
        ContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();
        parser.parse(inputStream, handler, metadata, context);
        return metadata;
    }
}
