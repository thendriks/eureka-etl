
package nl.maastrichtuniversity.ids.eureka.etl.web.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "dc.description",
    "dc.title"
})
public class Metadata {

    @JsonProperty("dc.description")
    private List<DcDescription> dcDescription = null;
    @JsonProperty("dc.title")
    private List<DcTitle> dcTitle = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("dc.description")
    public List<DcDescription> getDcDescription() {
        return dcDescription;
    }

    @JsonProperty("dc.description")
    public void setDcDescription(List<DcDescription> dcDescription) {
        this.dcDescription = dcDescription;
    }

    @JsonProperty("dc.title")
    public List<DcTitle> getDcTitle() {
        return dcTitle;
    }

    @JsonProperty("dc.title")
    public void setDcTitle(List<DcTitle> dcTitle) {
        this.dcTitle = dcTitle;
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
