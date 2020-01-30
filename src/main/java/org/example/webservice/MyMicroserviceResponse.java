package org.example.webservice;

/**
 *  Bean to wrap each web service response.  Renders as a JSON Object via Spring Web.
 */
public class MyMicroserviceResponse {

    private String status;
    private String description;

    public MyMicroserviceResponse(String status, String description) {
        this.status = status;
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

}