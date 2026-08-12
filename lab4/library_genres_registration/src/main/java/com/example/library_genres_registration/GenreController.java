package com.example.library_genres_registration;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;     
import java.util.Arrays;

@RestController
public class GenreController {
    
    @GetMapping("/genres")
    public List<String> getGenres() {
        return Arrays.asList("Fiction", "Non-Fiction", "Science Fiction", "Fantasy", "Mystery", "Romance");
    }
}
 