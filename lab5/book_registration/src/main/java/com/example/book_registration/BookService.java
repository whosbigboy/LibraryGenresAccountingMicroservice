package com.example.book_registration;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import java.math.BigDecimal;
import java.util.Map;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository repository;
    private final GenreClient genres;

    public BookService(BookRepository repository, GenreClient genres) {
        this.repository = repository;
        this.genres = genres;
    }

    public List<Book> all() {
        return repository.findAll();
    }

    public Optional<Book> find(Long id) {
        return repository.findById(id);
    }

    public Book create(BookRequest r) {
        validate(r);
        return repository.save(toBook(r));
    }

    public Optional<Book> update(Long id, BookRequest r) {
        validate(r);
        return repository.findById(id).map(b -> {
            b.setTitle(r.getTitle());
            b.setDescription(r.getDescription());
            b.setCost(r.getCost());
            b.setGenreId(r.getGenreId());
            return repository.save(b);
        });
    }

    public boolean delete(Long id) {
        if (!repository.existsById(id))
            return false;
        repository.deleteById(id);
        return true;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void seedDefaults() {
        if (repository.count() > 0) {
            return;
        }

        for (int attempt = 1; attempt <= 10; attempt++) {
            try {
                Map<String, Long> genreIds = genres.all().stream()
                        .filter(g -> g.get("id") instanceof Number && g.get("name") != null)
                        .collect(java.util.stream.Collectors.toMap(
                                g -> String.valueOf(g.get("name")),
                                g -> ((Number) g.get("id")).longValue(),
                                (first, ignored) -> first));

                if (genreIds.isEmpty()) {
                    Thread.sleep(2000);
                    continue;
                }

                createDefault("Путешествие к звездам", "История космической экспедиции.",
                        null, genreIds.get("Фантастика"));
                createDefault("Тайна старого дома", "Расследование загадочного исчезновения.",
                        BigDecimal.valueOf(12.50), genreIds.get("Детектив"));
                createDefault("Волшебный лес", "Приключения в мире магии.",
                        BigDecimal.ZERO, genreIds.get("Фэнтези"));
                createDefault("История древнего мира", "Научно-популярный обзор цивилизаций.",
                        BigDecimal.valueOf(8.99), genreIds.get("История"));
                createDefault("Основы робототехники", "Введение в современные технологии.",
                        null, genreIds.get("Научно-популярная литература"));
                createDefault("Летний роман", "История встречи двух людей.",
                        BigDecimal.valueOf(5), genreIds.get("Роман"));
                return;
            } catch (RuntimeException ex) {
                if (attempt == 10) {
                    throw ex;
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException interrupted) {
                    Thread.currentThread().interrupt();
                    return;
                }
            } catch (InterruptedException interrupted) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private void createDefault(String title, String description, BigDecimal cost, Long genreId) {
        if (genreId != null) {
            repository.save(new Book(null, title, description, cost, genreId));
        }
    }

    private void validate(BookRequest r) {
        if (r.getTitle() == null || r.getTitle().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "title is required");
        if (!genres.exists(r.getGenreId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "genre does not exist");
    }

    private Book toBook(BookRequest r) {
        return new Book(null, r.getTitle(), r.getDescription(), r.getCost(), r.getGenreId());
    }
}
