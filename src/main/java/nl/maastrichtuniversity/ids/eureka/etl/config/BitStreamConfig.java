package nl.maastrichtuniversity.ids.eureka.etl.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

@Data
@Configuration
@ConfigurationProperties(prefix = "bitstream")
public class BitStreamConfig {
    private String dspaceUrl;
    private String registrationPath;
    private Path assetFolder;
    private Path metadataFolder;
    private Path dspaceMetadataFolder;
    private Path dspaceAssetFolder;
    private String collectionId;
}
