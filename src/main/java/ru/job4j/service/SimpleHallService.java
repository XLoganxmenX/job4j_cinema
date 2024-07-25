package ru.job4j.service;

import ru.job4j.model.Hall;
import ru.job4j.repository.HallRepository;

import java.util.Optional;

public class SimpleHallService implements HallService {
    private final HallRepository hallRepository;

    public SimpleHallService(HallRepository sql2oHallRepository) {
        this.hallRepository = sql2oHallRepository;
    }

    @Override
    public Optional<Hall> findById(int id) {
        return hallRepository.findById(id);
    }
}
