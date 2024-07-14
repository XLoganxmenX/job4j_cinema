package ru.job4j.repository;

import ru.job4j.model.Ticket;

import java.util.Collection;
import java.util.Optional;

public interface TicketRepository {
    Optional<Ticket> save(Ticket ticket);

    boolean deleteById(int id);

    boolean update(Ticket ticket);

    Optional<Ticket> findById(int id);

    Collection<Ticket> findAll();
}
