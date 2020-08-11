package nl.maastrichtuniversity.ids.eureka.etl.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

@Configuration
@ConfigurationProperties("crawler")
public class CrawlerConfig {
    private Path metaDataCsv;

    public Path getMetaDataCsv() {
        return metaDataCsv;
    }

    public void setMetaDataCsv(Path metaDataCsv) {
        this.metaDataCsv = metaDataCsv;
    }
}
