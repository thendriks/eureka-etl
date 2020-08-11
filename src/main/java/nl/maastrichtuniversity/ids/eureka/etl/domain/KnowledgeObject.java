package nl.maastrichtuniversity.ids.eureka.etl.domain;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class KnowledgeObject {
    private String fileName;
    @CsvBindByName(column = "knowledgeobject.url")
    private String url;
    private final Map<String, String> optionalMetaData = new HashMap<>();

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Map<String, String> getOptionalMetaData() {
        return optionalMetaData;
    }

    public void setOptionalMetaData(Map<String, String> optionalMetaData) {
        this.optionalMetaData.clear();
        this.optionalMetaData.putAll(optionalMetaData);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
