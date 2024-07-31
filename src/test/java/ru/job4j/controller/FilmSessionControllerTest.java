package ru.job4j.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.dto.FilmDto;
import ru.job4j.dto.FilmSessionDto;
import ru.job4j.dto.FilmSessionPageDto;
import ru.job4j.service.FilmSessionService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FilmSessionControllerTest {

    private FilmSessionController filmSessionController;
    private FilmSessionService filmSessionService;

    @BeforeEach
    public void init() {
        filmSessionService = mock(FilmSessionService.class);
        filmSessionController = new FilmSessionController(filmSessionService);
    }

    @Test
    public void whenGetAllInEmptyListThenGetFilmSessionEmptyListPage() {
        Collection<FilmSessionDto> expectedFilmSessionDtoList = new ArrayList<>();
        when(filmSessionService.findAllFilmSessionDto()).thenReturn(expectedFilmSessionDtoList);
        var model = new ConcurrentModel();
        var view = filmSessionController.getAll(model);
        var actualFilmSessionDtoList = model.getAttribute("filmSessionsDto");

        assertThat(view).isEqualTo("filmSessions/list");
        assertThat(actualFilmSessionDtoList).isEqualTo(expectedFilmSessionDtoList);
    }

    @Test
    public void whenGetAllInNotEmptyListThenGetFilmSessionListPageWithDto() {
        Collection<FilmSessionDto> expectedFilmSessionDtoList = List.of(
                new FilmSessionDto(1, "RED",
                        new FilmDto(1, "Film", "Film", 2000, 16, 180,
                                "genre", 1),
                        LocalDateTime.now(), LocalDateTime.now(), 400),
                new FilmSessionDto(2, "BLUE",
                        new FilmDto(2, "Film2", "Film2", 2002, 18, 210,
                                "genre2", 2),
                        LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3), 900)
        );
        when(filmSessionService.findAllFilmSessionDto()).thenReturn(expectedFilmSessionDtoList);
        var model = new ConcurrentModel();
        var view = filmSessionController.getAll(model);
        var actualFilmSessionDtoList = model.getAttribute("filmSessionsDto");

        assertThat(view).isEqualTo("filmSessions/list");
        assertThat(actualFilmSessionDtoList).usingRecursiveComparison().isEqualTo(expectedFilmSessionDtoList);
    }

    @Test
    public void whenGetByIdNotExistSessionThenGetErrorPageAndMessage() {
        when(filmSessionService.getFilmSessionPageById(1)).thenReturn(Optional.empty());
        var model = new ConcurrentModel();
        var view = filmSessionController.getById(model, 1);
        var message = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(message).isEqualTo("Сеанс или фильм с указанным идентификатором не найден");
    }

    @Test
    public void whenGetByIdExistSessionThenGetPageWithDto() {
        var expectedFilmSessionPageDto = new FilmSessionPageDto(
                1,
                new FilmSessionDto(1, "RED",
                        new FilmDto(1, "Film", "Film", 2000, 16, 180,
                                "genre", 1),
                        LocalDateTime.now(), LocalDateTime.now(), 400),
                10,
                12
        );

        when(filmSessionService.getFilmSessionPageById(1)).thenReturn(Optional.of(expectedFilmSessionPageDto));
        var model = new ConcurrentModel();
        var view = filmSessionController.getById(model, 1);
        var actualFilmSessionPageDto = model.getAttribute("filmSessionPageDto");

        assertThat(view).isEqualTo("filmSessions/one");
        assertThat(actualFilmSessionPageDto).isEqualTo(expectedFilmSessionPageDto);
    }
}