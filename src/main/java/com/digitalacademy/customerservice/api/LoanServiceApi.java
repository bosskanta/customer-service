package com.digitalacademy.customerservice.api;

import com.digitalacademy.customerservice.model.response.GetLoanInfoResponse;

import com.digitalacademy.customerservice.util.Util;
import org.apache.http.impl.bootstrap.HttpServer;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
public class LoanServiceApi {
    private RestTemplate restTemplate;

    @Value("${spring.loanService.host}")
    private String host;

    @Value("${spring.loanService.endpoint.getInfo}")
    private String getInfo;

    public LoanServiceApi(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public GetLoanInfoResponse getLoanInfo(Long id) throws Exception {
        String data = "{}";
        try {
            RequestEntity requestEntity =
                    RequestEntity.get(URI.create(host + "/" + getInfo + "/" + id)).build();
            System.out.println("Request: " +
                    requestEntity.getMethod() + " Url: " + requestEntity.getUrl());

            ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

            if (response.getStatusCode().value() == 200) {
                JSONObject resp = new JSONObject(response.getBody());
                JSONObject status = new JSONObject(resp.getString("status"));
                if (status.getString("code").equals("0")) {
                    data = resp.get("data").toString();
                }
            }
        }
        catch (final HttpClientErrorException httpClientErrorException) {
            System.out.println("httpClientErrorException " + httpClientErrorException);
            throw new Exception("httpClientErrorException");
        }
        catch (HttpServerErrorException httpServerErrorException) {
            System.out.println("httpServerErrorException " + httpServerErrorException);
            throw new Exception("httpServerErrorException");
        }
        catch (Exception e) {
            throw e;
        }

        return Util.readValue(data, GetLoanInfoResponse.class);
    }
}
