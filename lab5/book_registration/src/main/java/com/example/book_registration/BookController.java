package com.example.book_registration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService service;
    private final GenreClient genres;
    public BookController(BookService service, GenreClient genres){this.service=service; this.genres=genres;}
    @GetMapping public List<Book> all(){return service.all();}
    @GetMapping("/{id}") public ResponseEntity<Book> one(@PathVariable Long id){return service.find(id).map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());}
    @PostMapping public ResponseEntity<Book> create(@RequestBody BookRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(service.create(r));}
    @PutMapping("/{id}") public ResponseEntity<Book> update(@PathVariable Long id,@RequestBody BookRequest r){return service.update(id,r).map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());}
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id){return service.delete(id)?ResponseEntity.noContent().build():ResponseEntity.notFound().build();}
    @GetMapping("/{id}/genre") public ResponseEntity<Object> genre(@PathVariable Long id){return service.find(id).map(b->ResponseEntity.ok(genres.get(b.getGenreId()))).orElseGet(()->ResponseEntity.notFound().build());}
}
