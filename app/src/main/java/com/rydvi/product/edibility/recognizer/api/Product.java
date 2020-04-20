package com.rydvi.product.edibility.recognizer.api;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public
class Product {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("name_local")
    private String nameLocal;

    @JsonProperty("consulting")
    private String consulting;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNameLocal(String nameLocal) {
        this.nameLocal = nameLocal;
    }

    public void setConsulting(String consulting) {
        this.consulting = consulting;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNameLocal() {
        return nameLocal;
    }

    public String getConsulting() {
        return consulting;
    }
}