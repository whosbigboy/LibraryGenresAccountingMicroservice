package com.example.book_registration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.List;

@Component
public class GenreClient {
    private final RestClient client;

    public GenreClient(@Value("${genres.service.url}") String url) {
        client = RestClient.builder().baseUrl(url).build();
    }

    public boolean exists(Long id) {
        if (id == null)
            return false;
        try {
            client.get().uri("/genres/{id}", id).retrieve().toBodilessEntity();
            return true;
        } catch (RuntimeException ex) {
            return false;
        }
    }

    public Object get(Long id) {
        return client.get().uri("/genres/{id}", id).retrieve().body(Object.class);
    }

    @SuppressWarnings("unchecked")
    public List<java.util.Map<String, Object>> all() {
        List<java.util.Map<String, Object>> result = client.get()
                .uri("/genres")
                .retrieve()
                .body(List.class);
        return result == null ? List.of() : result;
    }
}
