package com.example.library_genres_registration;

import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class GenreService {

    private final Map<Long, Genre> genres = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @PostConstruct
    public void init() {
        // Инициализация некоторыми данными для тестирования
        create(new Genre(null, "Fiction", "Художественная литература"));
        create(new Genre(null, "Non-Fiction", "Документальная литература"));
        create(new Genre(null, "Science Fiction", "Научная фантастика"));
        create(new Genre(null, "Fantasy", "Фэнтези"));
        create(new Genre(null, "Mystery", "Детективы"));
        create(new Genre(null, "Romance", "Романы"));
    }

    public List<Genre> getAllGenres() {
        return new ArrayList<>(genres.values());
    }

    public Optional<Genre> getGenreById(Long id) {
        return Optional.ofNullable(genres.get(id));
    }

    public Genre create(Genre genre) {
        Long id = idCounter.getAndIncrement();
        genre.setId(id);
        genres.put(id, genre);
        return genre;
    }

    public Optional<Genre> update(Long id, Genre genreDetails) {
        return Optional.ofNullable(genres.get(id)).map(existingGenre -> {
            existingGenre.setName(genreDetails.getName());
            existingGenre.setDescription(genreDetails.getDescription());
            genres.put(id, existingGenre);
            return existingGenre;
        });
    }

    public boolean delete(Long id) {
        return genres.remove(id) != null;
    }
}
