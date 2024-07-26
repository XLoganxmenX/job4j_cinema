package ru.job4j.service;

import org.springframework.stereotype.Service;
import ru.job4j.dto.FilmDto;
import ru.job4j.dto.FilmSessionDto;
import ru.job4j.dto.FilmSessionPageDto;
import ru.job4j.model.Film;
import ru.job4j.model.FilmSession;
import ru.job4j.model.Hall;
import ru.job4j.repository.FilmSessionRepository;
import ru.job4j.repository.HallRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class SimpleFilmSessionService implements FilmSessionService {
    private final FilmSessionRepository filmSessionRepository;
    private final FilmService filmService;
    private final HallRepository hallRepository;

    public SimpleFilmSessionService(FilmSessionRepository sql2oFilmSessionRepository, FilmService filmService,
                                    HallRepository sql2oHallRepository) {
        this.filmSessionRepository = sql2oFilmSessionRepository;
        this.filmService = filmService;
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
        Optional<FilmDto> optionalFilmDto = filmService.findById(filmSession.getFilmId());
        if (optionalFilmDto.isEmpty()) {
            return Optional.empty();
        }
        FilmSessionDto filmSessionDto = new FilmSessionDto(filmSession, hallName, optionalFilmDto.get());
        return Optional.of(filmSessionDto);
    }

    private String getHallNameById(int id) {
        Optional<Hall> optionalHall = hallRepository.findById(id);
        return optionalHall.isPresent() ? optionalHall.get().getName() : "Неизвестный зал";
    }

    @Override
    public Optional<FilmSessionPageDto> getFilmSessionPageById(int id) {
        Optional<FilmSessionDto> optionalFilmSessionDto = findById(id);
        if (optionalFilmSessionDto.isEmpty()) {
            return Optional.empty();
        }
        FilmSessionDto filmSessionDto = optionalFilmSessionDto.get();
        Optional<FilmSession> optionalFilmSession = filmSessionRepository.findById(id);
        Hall hall = hallRepository.findById(optionalFilmSession.get().getHallsId()).get();
        FilmSessionPageDto filmSessionPageDto = new FilmSessionPageDto(
                filmSessionDto.getId(), filmSessionDto,
                hall.getRowCount(), hall.getPlaceCount()
        );
        return Optional.of(filmSessionPageDto);
    }

    @Override
    public Collection<FilmSessionDto> findAllFilmSessionDto() {
        Collection<FilmSessionDto> filmSessionDtoCollection = new ArrayList<>();
        filmSessionRepository.findAll().forEach(filmSession -> {
            String hallName = getHallNameById(filmSession.getHallsId());
            Optional<FilmDto> optionalFilmDto = filmService.findById(filmSession.getFilmId());
            optionalFilmDto.ifPresent(filmDto ->
                    filmSessionDtoCollection.add(new FilmSessionDto(filmSession, hallName, filmDto)));
        });
        return filmSessionDtoCollection;
    }
}
