package com.example.library_genres_registration;

public class GenreRequest {
    private String name;
    private String description;

    public GenreRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Genre toGenre() {
        return new Genre(null, name, description);
    }
}
