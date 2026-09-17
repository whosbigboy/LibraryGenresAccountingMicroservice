package com.example.library_genres_registration;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class GenreService {

    private final GenreRepository repository;

    public GenreService(GenreRepository repository) {
        this.repository = repository;
    }

    public List<Genre> getAllGenres() {
        return repository.findAll();
    }

    public Optional<Genre> getGenreById(Long id) {
        return repository.findById(id);
    }

    public Genre create(Genre genre) {
        genre.setId(null);
        return repository.save(genre);
    }

    public Optional<Genre> update(Long id, Genre genreDetails) {
        return repository.findById(id).map(existingGenre -> {
            existingGenre.setName(genreDetails.getName());
            existingGenre.setDescription(genreDetails.getDescription());
            return repository.save(existingGenre);
        });
    }

    public boolean delete(Long id) {
        if (!repository.existsById(id)) return false;
        repository.deleteById(id);
        return true;
    }
}
