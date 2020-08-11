package nl.maastrichtuniversity.ids.eureka.etl.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

@Configuration
@ConfigurationProperties(prefix = "bitstream")
public class BitStreamConfig {
    private String url;
    private Path assetFolder;
    private Path metadataFolder;
    private Path dspaceMetadataFolder;
    private Path dspaceAssetFolder;

    public Path getAssetFolder() {
        return assetFolder;
    }

    public void setAssetFolder(Path assetFolder) {
        this.assetFolder = assetFolder;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Path getMetadataFolder() {
        return metadataFolder;
    }

    public void setMetadataFolder(Path metadataFolder) {
        this.metadataFolder = metadataFolder;
    }

    public Path getDspaceMetadataFolder() {
        return dspaceMetadataFolder;
    }

    public void setDspaceMetadataFolder(Path dspaceMetadataFolder) {
        this.dspaceMetadataFolder = dspaceMetadataFolder;
    }

    public Path getDspaceAssetFolder() {
        return dspaceAssetFolder;
    }

    public void setDspaceAssetFolder(Path dspaceAssetFolder) {
        this.dspaceAssetFolder = dspaceAssetFolder;
    }
}
