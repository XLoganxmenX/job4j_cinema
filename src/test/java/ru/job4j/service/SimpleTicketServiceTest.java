package ru.job4j.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.model.Ticket;
import ru.job4j.repository.TicketRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleTicketServiceTest {

    private TicketService ticketService;
    private TicketRepository ticketRepository;

    @BeforeEach
    public void init() {
        ticketRepository = mock(TicketRepository.class);
        ticketService = new SimpleTicketService(ticketRepository);
    }

    @Test
    public void whenSaveNewTicketThenGetTicketOptional() {
        var expectedTicket = new Ticket(1, 1, 10, 15, 1);
        when(ticketRepository.save(expectedTicket)).thenReturn(Optional.of(expectedTicket));

        var actualTicket = ticketService.save(expectedTicket);
        assertThat(actualTicket).usingRecursiveComparison().isEqualTo(Optional.of(expectedTicket));
    }

    @Test
    public void whenSaveExistTicketThenGetEmptyOptional() {
        var expectedTicket = new Ticket(1, 1, 10, 15, 1);
        when(ticketRepository.save(expectedTicket)).thenReturn(Optional.empty());

        var actualTicket = ticketService.save(expectedTicket);
        assertThat(actualTicket).isEmpty();
    }
}