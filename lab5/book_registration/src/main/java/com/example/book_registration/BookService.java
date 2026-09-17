package com.example.book_registration;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository repository;
    private final GenreClient genres;
    public BookService(BookRepository repository, GenreClient genres) { this.repository=repository; this.genres=genres; }
    public List<Book> all(){return repository.findAll();}
    public Optional<Book> find(Long id){return repository.findById(id);}
    public Book create(BookRequest r){ validate(r); return repository.save(toBook(r)); }
    public Optional<Book> update(Long id, BookRequest r) {
        validate(r);
        return repository.findById(id).map(b -> { b.setTitle(r.getTitle()); b.setDescription(r.getDescription()); b.setCost(r.getCost()); b.setGenreId(r.getGenreId()); return repository.save(b); });
    }
    public boolean delete(Long id){if(!repository.existsById(id)) return false; repository.deleteById(id); return true;}
    private void validate(BookRequest r){ if(r.getTitle()==null || r.getTitle().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"title is required"); if(!genres.exists(r.getGenreId())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"genre does not exist"); }
    private Book toBook(BookRequest r){return new Book(null,r.getTitle(),r.getDescription(),r.getCost(),r.getGenreId());}
}
