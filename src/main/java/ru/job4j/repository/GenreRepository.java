package ru.job4j.repository;

import ru.job4j.model.Genre;

import java.util.Optional;

public interface GenreRepository {
    Optional<Genre> findById(int id);
}
