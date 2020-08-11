
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
    "value",
    "language",
    "authority",
    "confidence",
    "place"
})
public class DcDescription {

    @JsonProperty("value")
    private String value;
    @JsonProperty("language")
    private Object language;
    @JsonProperty("authority")
    private Object authority;
    @JsonProperty("confidence")
    private Integer confidence;
    @JsonProperty("place")
    private Integer place;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(String value) {
        this.value = value;
    }

    @JsonProperty("language")
    public Object getLanguage() {
        return language;
    }

    @JsonProperty("language")
    public void setLanguage(Object language) {
        this.language = language;
    }

    @JsonProperty("authority")
    public Object getAuthority() {
        return authority;
    }

    @JsonProperty("authority")
    public void setAuthority(Object authority) {
        this.authority = authority;
    }

    @JsonProperty("confidence")
    public Integer getConfidence() {
        return confidence;
    }

    @JsonProperty("confidence")
    public void setConfidence(Integer confidence) {
        this.confidence = confidence;
    }

    @JsonProperty("place")
    public Integer getPlace() {
        return place;
    }

    @JsonProperty("place")
    public void setPlace(Integer place) {
        this.place = place;
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
