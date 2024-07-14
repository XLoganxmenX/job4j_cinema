package ru.job4j.repository;

import java.util.Optional;
import ru.job4j.model.Hall;

public interface HallRepository {
    Optional<Hall> findById(int id);
}
