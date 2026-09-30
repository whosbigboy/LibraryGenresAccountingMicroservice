package com.example.library_genres_registration;

import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.util.*;

@Service
public class GenreService {

    private final GenreRepository repository;

    public GenreService(GenreRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void seedDefaults() {
        createIfMissing("Фантастика", "Научная фантастика и космические приключения");
        createIfMissing("Фэнтези", "Волшебные миры и сказочные существа");
        createIfMissing("Детектив", "Расследования преступлений и загадочных событий");
        createIfMissing("Роман", "Истории о любви и отношениях");
        createIfMissing("История", "Исторические события и личности");
        createIfMissing("Научно-популярная литература", "Книги о науке и технологиях");
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

    private void createIfMissing(String name, String description) {
        if (!repository.existsByName(name)) {
            repository.save(new Genre(null, name, description));
        }
    }
}
