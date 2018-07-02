package com.epam.admin_client.service;

import com.epam.admin_client.service.exception.UserException;
import com.epam.admin_client.service.exception.UserRoleExeption;
import com.epam.admin_client.service.util.HeadersBuilder;
import com.epam.test_generator.controllers.admin.request.UserRoleUpdateDTO;
import com.epam.test_generator.controllers.user.response.UserDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {

    private final String uriUsers;
    private final String uriRole;
    private final String uriBlock;

    private RestTemplate restTemplate;
    private HeadersBuilder headersBuilder;

    public UserService(Environment environment, RestTemplate restTemplate,
                       HeadersBuilder headersBuilder) {
        this.restTemplate = restTemplate;
        this.headersBuilder = headersBuilder;
        this.uriUsers = environment.getRequiredProperty("users.api.url");
        this.uriRole = environment.getRequiredProperty("role.api.url");
        this.uriBlock = environment.getRequiredProperty("block.api.url");
    }

    public UserDTO[] getAllUsers(String token) throws IOException {
        HttpEntity<String> entity = new HttpEntity<>(headersBuilder.tokenToHeaders(token));
        ResponseEntity<UserDTO[]> result;
        try {
            result = restTemplate
                .exchange(uriUsers, HttpMethod.GET, entity, UserDTO[].class);
        } catch (HttpServerErrorException e) {
            throw new UserException(e);
        }
        return result.getBody();
    }

    public void setUserRole(UserRoleUpdateDTO user, String token)
        throws JsonProcessingException {
        if (user != null) {
            HttpEntity<UserRoleUpdateDTO> entity;
            try {
                entity = new HttpEntity<>(user,
                    headersBuilder.tokenToHeaders(token));
                restTemplate.exchange(uriRole, HttpMethod.PUT, entity, String.class);
            } catch (HttpClientErrorException e) {
                throw new UserRoleExeption();
            }
        }
    }

    public void blockUser(String id, String token) {
        HttpEntity<String> entity = new HttpEntity<>(headersBuilder.tokenToHeaders(token));
        try {
            restTemplate
                .exchange(uriBlock + "/" + id + "/block", HttpMethod.PUT, entity, String.class);
        } catch (HttpStatusCodeException e) {
            throw new UserException(e);
        }
    }

    public void unblockUser(String id, String token) {
        HttpEntity<String> entity = new HttpEntity<>(headersBuilder.tokenToHeaders(token));
        try {
            restTemplate
                .exchange(uriBlock + "/" + id + "/unblock", HttpMethod.PUT, entity, String.class);
        } catch (HttpStatusCodeException e) {
            throw new UserException(e);
        }
    }
}
