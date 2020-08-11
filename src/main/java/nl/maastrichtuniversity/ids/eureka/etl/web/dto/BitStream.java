
package nl.maastrichtuniversity.ids.eureka.etl.web.dto;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "uuid",
    "name",
    "handle",
    "metadata",
    "bundleName",
    "sizeBytes",
    "checkSum",
    "sequenceId",
    "type"
})
public class BitStream {

    @JsonProperty("id")
    private String id;
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("name")
    private String name;
    @JsonProperty("handle")
    private Object handle;
    @JsonProperty("metadata")
    private Metadata metadata;
    @JsonProperty("bundleName")
    private String bundleName;
    @JsonProperty("sizeBytes")
    private Integer sizeBytes;
    @JsonProperty("checkSum")
    private CheckSum checkSum;
    @JsonProperty("sequenceId")
    private Integer sequenceId;
    @JsonProperty("type")
    private String type;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("uuid")
    public String getUuid() {
        return uuid;
    }

    @JsonProperty("uuid")
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("handle")
    public Object getHandle() {
        return handle;
    }

    @JsonProperty("handle")
    public void setHandle(Object handle) {
        this.handle = handle;
    }

    @JsonProperty("metadata")
    public Metadata getMetadata() {
        return metadata;
    }

    @JsonProperty("metadata")
    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    @JsonProperty("bundleName")
    public String getBundleName() {
        return bundleName;
    }

    @JsonProperty("bundleName")
    public void setBundleName(String bundleName) {
        this.bundleName = bundleName;
    }

    @JsonProperty("sizeBytes")
    public Integer getSizeBytes() {
        return sizeBytes;
    }

    @JsonProperty("sizeBytes")
    public void setSizeBytes(Integer sizeBytes) {
        this.sizeBytes = sizeBytes;
    }

    @JsonProperty("checkSum")
    public CheckSum getCheckSum() {
        return checkSum;
    }

    @JsonProperty("checkSum")
    public void setCheckSum(CheckSum checkSum) {
        this.checkSum = checkSum;
    }

    @JsonProperty("sequenceId")
    public Integer getSequenceId() {
        return sequenceId;
    }

    @JsonProperty("sequenceId")
    public void setSequenceId(Integer sequenceId) {
        this.sequenceId = sequenceId;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
