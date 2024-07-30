package ru.job4j.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.dto.FilmDto;
import ru.job4j.service.FilmService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FilmControllerTest {

    private FilmController filmController;
    private FilmService filmService;

    @BeforeEach
    public void init() {
        filmService = mock(FilmService.class);
        filmController = new FilmController(filmService);
    }

    @Test
    public void whenGetAllInEmptyListThenGetFilmEmptyListPage() {
        Collection<FilmDto> expectedFilmDtoList = new ArrayList<>();
        when(filmService.findAll()).thenReturn(expectedFilmDtoList);
        var model = new ConcurrentModel();
        var view = filmController.getAll(model);
        var actualFilmDtoList = model.getAttribute("filmsDto");

        assertThat(view).isEqualTo("films/list");
        assertThat(actualFilmDtoList).isEqualTo(expectedFilmDtoList);
    }


}