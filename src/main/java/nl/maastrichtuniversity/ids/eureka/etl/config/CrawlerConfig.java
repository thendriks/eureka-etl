package nl.maastrichtuniversity.ids.eureka.etl.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

@Data
@Configuration
@ConfigurationProperties("crawler")
public class CrawlerConfig {
    private Path metaDataCsv;
}
