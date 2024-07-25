package ru.job4j.service;

import ru.job4j.model.Ticket;
import ru.job4j.repository.TicketRepository;

import java.util.Collection;
import java.util.Optional;

public class SimpleTicketService implements TicketService {
    private final TicketRepository ticketRepository;

    public SimpleTicketService(TicketRepository sql2oTicketRepository) {
        this.ticketRepository = sql2oTicketRepository;
    }

    @Override
    public Optional<Ticket> save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public boolean deleteById(int id) {
        return ticketRepository.deleteById(id);
    }

    @Override
    public boolean update(Ticket ticket) {
        return ticketRepository.update(ticket);
    }

    @Override
    public Optional<Ticket> findById(int id) {
        return ticketRepository.findById(id);
    }

    @Override
    public Collection<Ticket> findAll() {
        return ticketRepository.findAll();
    }
}
