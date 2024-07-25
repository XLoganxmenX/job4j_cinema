package ru.job4j.service;

import ru.job4j.model.Hall;

import java.util.Optional;

public interface HallService {
    Optional<Hall> findById(int id);
}
