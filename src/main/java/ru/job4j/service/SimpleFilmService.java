package ru.job4j.service;

import ru.job4j.dto.FilmDto;
import ru.job4j.model.Film;
import ru.job4j.model.Genre;
import ru.job4j.repository.FilmRepository;
import ru.job4j.repository.GenreRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class SimpleFilmService implements FilmService {
    private final FilmRepository filmRepository;
    private final GenreRepository genreRepository;

    public SimpleFilmService(FilmRepository filmRepository, GenreRepository genreRepository) {
        this.filmRepository = filmRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public Optional<FilmDto> findById(int id) {
        Optional<Film> optionalFilm = filmRepository.findById(id);
        if (optionalFilm.isEmpty()) {
            return Optional.empty();
        }
        Film film = optionalFilm.get();
        String genreName = getGenreNameById(film.getGenreId());
        FilmDto filmDto = new FilmDto(film, genreName);
        return Optional.of(filmDto);
    }

    private String getGenreNameById(int id) {
        Optional<Genre> optionalGenre = genreRepository.findById(id);
        return optionalGenre.isPresent() ? optionalGenre.get().getName() : "Неизвестный жанр";
    }

    @Override
    public Collection<FilmDto> findAll() {
        Collection<FilmDto> filmDtoCollection = new ArrayList<>();
        filmRepository.findAll().forEach(film -> {
            String genreName = getGenreNameById(film.getGenreId());
            filmDtoCollection.add(new FilmDto(film, genreName));
        });
        return filmDtoCollection;
    }
}
