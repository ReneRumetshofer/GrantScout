package org.example.parsing;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ParsedCall {
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Themenbereich")
    private String category;
    @JsonProperty("Kurzbeschreibung")
    private String shortDescription;
    @JsonProperty("Langbeschreibung")
    private String longDescription;
    @JsonProperty("Organisation")
    private String organisation;
    @JsonProperty("Förderbetrag")
    private double fundingAmount;
    @JsonProperty("Bewerbungsfrist")
    private Date deadline;
    @JsonProperty("Regionen")
    private List<String> region;
    @JsonProperty("Zielgruppe")
    private List<String> audience;
    @JsonProperty("Webseite")
    private String url;
    @JsonProperty("Kontakt")
    private List<Contact> contacts;
    @JsonProperty("Optional")
    private Map<String, String> optional;

    private static class Contact {
        @JsonProperty("Ansprechpartner")
        private String name;
        @JsonProperty("E-Mail")
        private String email;
        @JsonProperty("Telefon")
        private String phone;
    }
}
