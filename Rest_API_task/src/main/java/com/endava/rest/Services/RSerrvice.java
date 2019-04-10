package com.endava.rest.Services;

import com.endava.rest.models.Issue;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

@Service
public class RSerrvice implements RServiceInterface {

    @Autowired
    RestTemplate restTemplate;
    @Value("${s.username}")
    private String username;

    @Value("${s.password}")
    private String password;

    @Value("${r.addres}")
    public String REST_SERVICE_URI;

    public HttpHeaders httpHeaders;
    public HttpEntity<?> httpEntity;


    public static HttpHeaders createHeaders(String username, String password) {

        return new HttpHeaders() {
            {
                String auth = username + ":" + password;
                byte[] encodedAuth = Base64.encodeBase64(
                        auth.getBytes(Charset.forName("US-ASCII")));
                String authHeader = "Basic " + new String(encodedAuth);
                set("Authorization", authHeader);
            }
        };

    }

    public HttpHeaders getToken() {

        String uri = REST_SERVICE_URI + "rest/api/2/issue/createmeta";

        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(username,password));
        restTemplate.getForObject(uri,String.class);

        return httpHeaders;
    }

    public void setHttpHeaders(HttpHeaders httpHeaders){
        this .httpHeaders=httpHeaders;
        System.err.println(this.httpHeaders);
    }

    public ResponseEntity<String> getAllTasksByUsername(String user) {
        String uri = REST_SERVICE_URI + "rest/api/2/search?jql=project=\"IIA\"AND assignee =" + "\"" + user + "\"";
        System.err.println(httpEntity.getBody());
        ResponseEntity<String> result = restTemplate.exchange(
                uri, HttpMethod.GET, httpEntity, String.class);
        return result;
    }


    public ResponseEntity<String> getTaskById(int id) {
        String uri = REST_SERVICE_URI + "rest/api/2/issue/" + id;
        ResponseEntity<String> result = restTemplate.exchange(
                uri, HttpMethod.GET, httpEntity, String.class);
        return result;
    }


    public void createIssue(Issue issue) {

        Issue issue1 = issue;

        String uri = REST_SERVICE_URI + "/rest/api/2/issue";
       httpEntity = new HttpEntity<>(issue, httpHeaders);
        ResponseEntity<String> result = restTemplate.postForEntity(uri, httpEntity, String.class);

        System.err.println(result);

    }


    public void update(int id, Issue issue) {
        String uri = REST_SERVICE_URI + "rest/api/2/issue/" + id;

        httpEntity = new HttpEntity<>(issue, httpHeaders);

        ResponseEntity<Issue> result = restTemplate.exchange(uri, HttpMethod.PUT, httpEntity, Issue.class);
        System.err.println(result);
    }


}
