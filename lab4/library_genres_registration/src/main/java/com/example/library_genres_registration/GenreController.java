package com.example.library_genres_registration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public List<Genre> getAllGenres() {
        return genreService.getAllGenres();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genre> getGenreById(@PathVariable Long id) {
        Optional<Genre> genre = genreService.getGenreById(id);
        return genre.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Genre> createGenre(@RequestBody GenreRequest request) {
        Genre createdGenre = genreService.create(request.toGenre());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGenre);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Genre> updateGenre(@PathVariable Long id, @RequestBody GenreRequest request) {
        Optional<Genre> updatedGenre = genreService.update(id, request.toGenre());
        return updatedGenre.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        boolean deleted = genreService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

@RestController
@RequestMapping("/")
class HomeController {
    @GetMapping
    public String home() {
        return "Welcome!";
    }
}
