package ru.job4j.repository;

import ru.job4j.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmRepository {
    Optional<Film> findById(int id);

    Collection<Film> findAll();
}
