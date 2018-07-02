package com.epam.admin_client.service.util;

import com.epam.test_generator.dto.TokenDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class HeadersBuilder {


    public HttpHeaders tokenToHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.set(TokenDTO.TOKEN_HEADER, "Bearer "+token);
        return headers;
    }

}
