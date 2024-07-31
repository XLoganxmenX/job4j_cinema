package ru.job4j.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.dto.FilmDto;
import ru.job4j.dto.FilmSessionDto;
import ru.job4j.dto.FilmSessionPageDto;
import ru.job4j.model.FilmSession;
import ru.job4j.model.Hall;
import ru.job4j.repository.FilmSessionRepository;

import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleFilmSessionServiceTest {

    private FilmSessionService filmSessionService;
    private FilmSessionRepository filmSessionRepository;
    private HallService hallService;
    private FilmService filmService;

    @BeforeEach
    public void init() {
        filmSessionRepository = mock(FilmSessionRepository.class);
        hallService = mock(HallService.class);
        filmService = mock(FilmService.class);
        filmSessionService = new SimpleFilmSessionService(filmSessionRepository, filmService, hallService);
    }

    @Test
    public void whenFindByIdThenReturnFilmSessionDto() {
        var time = now();
        var filmSession = new FilmSession(1, 1, 1, time, time.plusHours(3), 400);
        var filmDto = new FilmDto(1, "Film", "Film", 2000, 16, 180,
                "genre", 1);
        var expectedFilmSessionDto =
                new FilmSessionDto(1, "RED", filmDto, time, time.plusHours(3), 400);
        var hall = new Hall(1, "RED", 10, 15, "RED");

        when(filmSessionRepository.findById(filmSession.getId())).thenReturn(Optional.of(filmSession));
        when(filmService.findById(filmSession.getFilmId())).thenReturn(Optional.of(filmDto));
        when(hallService.findById(filmSession.getHallsId())).thenReturn(Optional.of(hall));

        var actualFilmSessionDto = filmSessionService.findById(filmSession.getId());
        assertThat(actualFilmSessionDto).usingRecursiveComparison().isEqualTo(Optional.of(expectedFilmSessionDto));
    }

    @Test
    public void whenFindByIdNotExistFilmSessionThenReturnEmptyOptional() {
        int filmSessionId = 1;
        when(filmSessionRepository.findById(filmSessionId)).thenReturn(Optional.empty());

        var actualFilmSessionDto = filmSessionService.findById(filmSessionId);
        assertThat(actualFilmSessionDto).isEmpty();
    }

    @Test
    public void whenGetFilmSessionPageById() {
        var time = now();
        var filmSession = new FilmSession(1, 1, 1, time, time.plusHours(3), 400);
        var filmDto = new FilmDto(1, "Film", "Film", 2000, 16, 180,
                "genre", 1);
        var hall = new Hall(1, "RED", 10, 15, "RED");

        when(filmSessionRepository.findById(filmSession.getId())).thenReturn(Optional.of(filmSession));
        when(filmService.findById(filmSession.getFilmId())).thenReturn(Optional.of(filmDto));
        when(hallService.findById(filmSession.getHallsId())).thenReturn(Optional.of(hall));

        var filmSessionDto = filmSessionService.findById(filmSession.getId()).get();
        var expectedFilmSessionPageDto =
                new FilmSessionPageDto(filmSessionDto.getId(), filmSessionDto, hall.getRowCount(), hall.getPlaceCount());
        var actualFilmSessionPageDto = filmSessionService.getFilmSessionPageById(filmSession.getId());
        assertThat(actualFilmSessionPageDto)
                .usingRecursiveComparison().isEqualTo(Optional.of(expectedFilmSessionPageDto));
    }

    @Test
    public void whenGetNotExistFilmSessionPageByIdThenGetEmptyOptional() {
        var mockFilmSessionService = mock(FilmSessionService.class);
        when(mockFilmSessionService.findById(0)).thenReturn(Optional.empty());

        var actualFilmSessionPageDto = filmSessionService.getFilmSessionPageById(0);

        assertThat(actualFilmSessionPageDto).isEmpty();
    }

    @Test
    public void whenFindAll() {
        var time = now();
        var filmSession1 = new FilmSession(1, 1, 1, time, time.plusHours(3), 400);
        var filmSession2 = new FilmSession(2, 1, 1, time.plusHours(3), time.plusHours(6), 900);
        var filmSessions = List.of(filmSession1, filmSession2);
        var filmDto = new FilmDto(1, "Film", "Film", 2000, 16, 180,
                "genre", 1);
        var hall = new Hall(1, "RED", 10, 15, "RED");
        when(filmSessionRepository.findAll()).thenReturn(filmSessions);
        when(filmService.findById(filmDto.getId())).thenReturn(Optional.of(filmDto));
        when(hallService.findById(hall.getId())).thenReturn(Optional.of(hall));


        var expectedFilmSessionsDto = List.of(
                new FilmSessionDto(filmSession1, hall.getName(), filmDto),
                new FilmSessionDto(filmSession2, hall.getName(), filmDto)
        );
        var actualFilmSessionsDto = filmSessionService.findAllFilmSessionDto();
        assertThat(actualFilmSessionsDto).usingRecursiveComparison().isEqualTo(expectedFilmSessionsDto);
    }

}