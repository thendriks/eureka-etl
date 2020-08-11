package nl.maastrichtuniversity.ids.eureka.etl.web.controller;

import lombok.extern.slf4j.Slf4j;
import nl.maastrichtuniversity.ids.eureka.etl.service.ContentExtractionService;
import org.apache.tika.exception.TikaException;
import org.apache.tika.langdetect.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageDetector;
import org.apache.tika.language.detect.LanguageResult;
import org.apache.tika.metadata.Metadata;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RequestMapping("/api")
@RestController
public class ContentExtractionController {
    private ContentExtractionService contentExtractionService;

    public ContentExtractionController(ContentExtractionService contentExtractionService) {
        this.contentExtractionService = contentExtractionService;
    }

    @PostMapping("/extract-metadata")
    public ResponseEntity extractData() throws IOException, TikaException, SAXException {
        String docType = contentExtractionService.detectDocTypeUsingFacade();
        log.info(docType);
        String content = contentExtractionService.extractContentUsingFacade();
        log.info(content);
        LanguageDetector languageDetector = new OptimaizeLangDetector().loadModels();
        LanguageResult languageResult = languageDetector.detect(content);
        Metadata metadata = contentExtractionService.extractMetadatatUsingParser();
        log.info(metadata.toString());
        log.info(languageResult.getLanguage());

        return ResponseEntity.ok().build();
    }
}
