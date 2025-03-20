package org.example.parsing;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ParsedCall {
    private String name;
    private String category;
    private String shortDescription;
    private String longDescription;
    private double fundingAmount;
    private Date deadline;
    private List<String> region;
    private List<String> audience;
    private String url;
    private List<Contact> contacts;
    private Map<String, String> optional;

    private static class Contact {
        private String name;
        private String phone;
        private String email;
    }
}
