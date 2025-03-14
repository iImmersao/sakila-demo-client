package com.iimmersao.sakila_demo_client.controllers;

import com.iimmersao.sakila_demo_client.models.Actor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
public class ActorController {
    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/actors")
    public String getActors(Model model,
                            @RegisteredOAuth2AuthorizedClient("users-client-oidc") OAuth2AuthorizedClient authorizedClient) {

        System.out.println("Fetching actors...");

        String jwtAccessToken = authorizedClient.getAccessToken().getTokenValue();
        System.out.println("jwtAccessToken =  " + jwtAccessToken);

        String url = "http://127.0.0.1:8280/actor";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtAccessToken);

        System.out.println("Setting up call to resource server with headers");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<List<Actor>> responseEntity =
                restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Actor>>() {});

        System.out.println("Got status code: " + responseEntity.getStatusCode());

        List<Actor> actors = responseEntity.getBody();

        model.addAttribute("actors", actors);

        return "actors-page";
    }
}
