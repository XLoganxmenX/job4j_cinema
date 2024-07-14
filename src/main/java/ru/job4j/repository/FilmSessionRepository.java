package ru.job4j.repository;

import ru.job4j.model.FilmSession;

import java.util.Collection;
import java.util.Optional;

public interface FilmSessionRepository {
    Optional<FilmSession> findById(int id);

    Collection<FilmSession> findAll();
}
