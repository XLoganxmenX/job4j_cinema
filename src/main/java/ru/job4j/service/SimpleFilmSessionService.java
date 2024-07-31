package ru.job4j.service;

import org.springframework.stereotype.Service;
import ru.job4j.dto.FilmDto;
import ru.job4j.dto.FilmSessionDto;
import ru.job4j.dto.FilmSessionPageDto;
import ru.job4j.model.FilmSession;
import ru.job4j.model.Hall;
import ru.job4j.repository.FilmSessionRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class SimpleFilmSessionService implements FilmSessionService {
    private final FilmSessionRepository filmSessionRepository;
    private final FilmService filmService;
    private final HallService hallService;

    public SimpleFilmSessionService(FilmSessionRepository sql2oFilmSessionRepository, FilmService filmService,
                                    HallService hallService) {
        this.filmSessionRepository = sql2oFilmSessionRepository;
        this.filmService = filmService;
        this.hallService = hallService;
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
        Optional<Hall> optionalHall = hallService.findById(id);
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
        Hall hall = hallService.findById(optionalFilmSession.get().getHallsId()).get();
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
