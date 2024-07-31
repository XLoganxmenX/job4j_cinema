package ru.job4j.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.model.Hall;
import ru.job4j.repository.HallRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleHallServiceTest {
    private HallService hallService;
    private HallRepository hallRepository;

    @BeforeEach
    void init() {
        hallRepository = mock(HallRepository.class);
        hallService = new SimpleHallService(hallRepository);
    }

    @Test
    void whenFindByIdExistHallThenGetIt() {
        int hallId = 1;
        var expectedHall = new Hall(1, "RED", 10, 15, "RED");
        when(hallRepository.findById(hallId)).thenReturn(Optional.of(expectedHall));

        var actualHall = hallService.findById(hallId).get();
        assertThat(actualHall).usingRecursiveComparison().isEqualTo(expectedHall);
    }

    @Test
    void whenFindByIdNotExistHallThenGetItEmptyOptional() {
        int hallId = 1;
        when(hallRepository.findById(hallId)).thenReturn(Optional.empty());

        var actualHall = hallService.findById(hallId);
        assertThat(actualHall).isEmpty();
    }
}