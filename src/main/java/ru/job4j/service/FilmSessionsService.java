package ru.job4j.service;

import ru.job4j.dto.FilmSessionDto;

import java.util.Collection;
import java.util.Optional;

public interface FilmSessionsService {
    Optional<FilmSessionDto> findById(int id);

    Collection<FilmSessionDto> findAll();
}
