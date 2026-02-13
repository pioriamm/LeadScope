package br.com.pioriam.leadscope.modelos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OfficeResponse {

    private String updated;
    private String taxId;
    private String alias;
    private String founded;
    private Boolean head;

    private Company company;

    private String statusDate;
    private Status status;
    private Address address;
    private Activity mainActivity;

    private List<Phone> phones;
    private List<Email> emails;
    private List<Activity> sideActivities;
}