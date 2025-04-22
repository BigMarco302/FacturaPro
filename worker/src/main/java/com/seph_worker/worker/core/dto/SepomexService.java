package com.seph_worker.worker.core.dto;


import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class SepomexService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String TOKEN = "db5f738e-bc7b-4c01-9ead-700abbc50b10";
    private static final String BASE_URL = "https://api.copomex.com/query/info_cp/{cp}?type=simplified&token={token}";

    public String consultarCodigoPostal(String cp) {
        ResponseEntity<String> response = restTemplate.getForEntity(BASE_URL, String.class, cp, TOKEN);
        return response.getBody();
    }
}
