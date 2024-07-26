package ru.job4j.service;

import ru.job4j.dto.FilmSessionDto;
import ru.job4j.dto.FilmSessionPageDto;

import java.util.Collection;
import java.util.Optional;

public interface FilmSessionService {
    Optional<FilmSessionDto> findById(int id);

    Optional<FilmSessionPageDto> getFilmSessionPageById(int id);

    Collection<FilmSessionDto> findAllFilmSessionDto();
}
