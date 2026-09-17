package com.example.book_registration;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class Book {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(length = 4000) private String description;
    private BigDecimal cost;
    private Long genreId;

    public Book() {}
    public Book(Long id, String title, String description, BigDecimal cost, Long genreId) {
        this.id=id; this.title=title; this.description=description; this.cost=cost; this.genreId=genreId;
    }
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getTitle(){return title;} public void setTitle(String v){title=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public BigDecimal getCost(){return cost;} public void setCost(BigDecimal v){cost=v;}
    public Long getGenreId(){return genreId;} public void setGenreId(Long v){genreId=v;}
}
