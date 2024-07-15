package ru.job4j.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.dto.FilmDto;
import ru.job4j.model.Film;
import ru.job4j.model.Genre;
import ru.job4j.repository.FileRepository;
import ru.job4j.repository.FilmRepository;
import ru.job4j.repository.GenreRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleFilmServiceTest {
    private FilmRepository filmRepository;
    private GenreRepository genreRepository;
    private FilmService filmService;

    @BeforeEach
    public void init() {
        filmRepository = mock(FilmRepository.class);
        genreRepository = mock(GenreRepository.class);
        filmService = new SimpleFilmService(filmRepository, genreRepository);
    }

    @Test
    public void whenFindByIdExistFilmThenGetFilmDtoWithGenre() {
        int filmId = 1;
        int genreId = 1;
        Film film = new Film(
                filmId, "film", "filmDesc", 2000, genreId, 18, 120, 1
        );
        Genre genre = new Genre(genreId, "Genre");
        FilmDto expectedFilmDto = new FilmDto(film, genre.getName());
        when(filmRepository.findById(filmId)).thenReturn(Optional.of(film));
        when(genreRepository.findById(genreId)).thenReturn(Optional.of(genre));

        FilmDto actualFilmDto = filmService.findById(filmId).get();

        assertThat(actualFilmDto).usingRecursiveComparison().isEqualTo(expectedFilmDto);
    }

    @Test
    public void whenFindByIdNotExistFilmThenGetEmptyOptional() {
        when(filmRepository.findById(0)).thenReturn(Optional.empty());
        Optional<FilmDto> actualFilmDto = filmService.findById(0);
        assertThat(actualFilmDto).isEmpty();
    }

    @Test
    public void whenFindByIdExistFilmAndNotExistGenre() {
        String expectedGenreName = "Неизвестный жанр";
        int filmId = 1;
        Film film = new Film(
                filmId, "film", "filmDesc", 2000, 0, 18, 120, 1
        );
        FilmDto expectedFilmDto = new FilmDto(film, expectedGenreName);
        when(filmRepository.findById(filmId)).thenReturn(Optional.of(film));
        when(genreRepository.findById(0)).thenReturn(Optional.empty());

        FilmDto actualFilmDto = filmService.findById(filmId).get();

        assertThat(actualFilmDto).usingRecursiveComparison().isEqualTo(expectedFilmDto);
    }

    @Test
    public void whenFindAll() {
        int genreId = 1;
        Film film1 = new Film(
                1, "film1", "filmDesc1", 2001, genreId, 10, 60, 1
        );
        Film film2 = new Film(
                2, "film2", "filmDesc2", 2002, genreId, 16, 120, 2
        );
        Film film3 = new Film(
                3, "film3", "filmDesc3", 2003, genreId, 18, 160, 3
        );
        Genre genre = new Genre(genreId, "Genre");
        String genreName = genre.getName();
        var expectedFilmDto = List.of(
                new FilmDto(film1, genreName),
                new FilmDto(film2, genreName),
                new FilmDto(film3, genreName)
        );
        when(genreRepository.findById(genreId)).thenReturn(Optional.of(genre));
        when(filmRepository.findAll()).thenReturn(List.of(film1, film2, film3));

        var actualFilmDto = filmService.findAll();

        assertThat(actualFilmDto).usingRecursiveComparison().isEqualTo(expectedFilmDto);
    }
}