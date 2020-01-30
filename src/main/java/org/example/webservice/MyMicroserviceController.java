package org.example.webservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@RestController
public class MyMicroserviceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyMicroserviceController.class);

    /**
     *  Say Hello.
     *
     *  @param username The user's name (i.e. the person to say hello to)
     *  @return response JSON Object.
     */
    @RequestMapping(value = "/sayHello", method = RequestMethod.GET)
    public MyMicroserviceResponse sayHello(@RequestParam("username") String username) {

        LOGGER.info("*** about to say hello...");
        try {
            return new MyMicroserviceResponse("SUCCESS", "Hello, " + username + "!  Welcome to your first Spring Boot service!");
        } catch (Exception e) {
            LOGGER.error("*** something went wrong...");
            return new MyMicroserviceResponse("ERROR", "Could not say hello for some reason..." + e.getMessage());
        }

    }

    @RequestMapping(value = "/handleInvoice", method = RequestMethod.POST)
    public MyMicroserviceResponse handleInvoice(@RequestBody String input) {

        LOGGER.info("*** just received invoice...");
        LOGGER.info(input);
        String payload = "";
        JSONObject inputJson = new JSONObject(input);
        payload = inputJson.toString();
        LOGGER.info(payload);

        // call camunda to kick off process
        JSONObject invoiceJson = new JSONObject();
        invoiceJson.put("type", "Json");
        invoiceJson.put("value", payload);
        JSONObject variablesJson = new JSONObject();
        variablesJson.put("invoice", invoiceJson);
        JSONObject outputJson = new JSONObject();
        outputJson.put("variables", variablesJson);
        outputJson.put("businessKey", "bk-" + System.currentTimeMillis());

        final String uri = "http://localhost:8080/rest/process-definition/key/process-invoice/start";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<String>(outputJson.toString(), headers);

        String result = restTemplate.postForObject(uri, request, String.class);

        System.out.println(result);

        // send response
        try {
            return new MyMicroserviceResponse("SUCCESS", "Invoice Received!");
        } catch (Exception e) {
            LOGGER.error("*** something went wrong...");
            return new MyMicroserviceResponse("ERROR", "Some error occurred..." + e.getMessage());
        }

    }


}
