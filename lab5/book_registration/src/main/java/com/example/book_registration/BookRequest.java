package com.example.book_registration;

import java.math.BigDecimal;

public class BookRequest {
    private String title;
    private String description;
    private BigDecimal cost;
    private Long genreId;
    public String getTitle(){return title;} public void setTitle(String v){title=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public BigDecimal getCost(){return cost;} public void setCost(BigDecimal v){cost=v;}
    public Long getGenreId(){return genreId;} public void setGenreId(Long v){genreId=v;}
}
