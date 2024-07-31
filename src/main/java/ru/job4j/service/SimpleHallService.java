package ru.job4j.service;

import org.springframework.stereotype.Service;
import ru.job4j.model.Hall;
import ru.job4j.repository.HallRepository;

import java.util.Optional;

@Service
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
