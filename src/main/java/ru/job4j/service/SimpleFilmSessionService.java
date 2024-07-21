package ru.job4j.service;

import org.springframework.stereotype.Service;
import ru.job4j.dto.FilmSessionDto;
import ru.job4j.model.Film;
import ru.job4j.model.FilmSession;
import ru.job4j.model.Hall;
import ru.job4j.repository.FilmRepository;
import ru.job4j.repository.FilmSessionRepository;
import ru.job4j.repository.HallRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class SimpleFilmSessionService implements FilmSessionService {
    private final FilmSessionRepository filmSessionRepository;
    private final FilmRepository filmRepository;
    private final HallRepository hallRepository;

    public SimpleFilmSessionService(FilmSessionRepository sql2oFilmSessionRepository,
                                    FilmRepository sql2oFilmRepository, HallRepository sql2oHallRepository) {
        this.filmSessionRepository = sql2oFilmSessionRepository;
        this.filmRepository = sql2oFilmRepository;
        this.hallRepository = sql2oHallRepository;
    }

    @Override
    public Optional<FilmSessionDto> findById(int id) {
        Optional<FilmSession> optionalFilmSession = filmSessionRepository.findById(id);
        if (optionalFilmSession.isEmpty()) {
            return Optional.empty();
        }
        FilmSession filmSession = optionalFilmSession.get();
        String hallName = getHallNameById(filmSession.getHallsId());
        String filmName = getFilmNameById(filmSession.getFilmId());
        FilmSessionDto filmSessionDto = new FilmSessionDto(filmSession, hallName, filmName);
        return Optional.of(filmSessionDto);
    }

    private String getHallNameById(int id) {
        Optional<Hall> optionalHall = hallRepository.findById(id);
        return optionalHall.isPresent() ? optionalHall.get().getName() : "Неизвестный зал";
    }

    private String getFilmNameById(int id) {
        Optional<Film> optionalFilm = filmRepository.findById(id);
        return optionalFilm.isPresent() ? optionalFilm.get().getName() : "Неизвестный фильм";
    }

    @Override
    public Collection<FilmSessionDto> findAll() {
        Collection<FilmSessionDto> filmSessionDtoCollection = new ArrayList<>();
        filmSessionRepository.findAll().forEach(filmSession -> {
            String hallName = getHallNameById(filmSession.getHallsId());
            String filmName = getFilmNameById(filmSession.getFilmId());
            filmSessionDtoCollection.add(new FilmSessionDto(filmSession, hallName, filmName));
        });
        return filmSessionDtoCollection;
    }
}
