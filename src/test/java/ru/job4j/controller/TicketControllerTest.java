package ru.job4j.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.model.Ticket;
import ru.job4j.model.User;
import ru.job4j.service.TicketService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TicketControllerTest {

    private TicketController ticketController;
    private TicketService ticketService;

    @BeforeEach
    public void init() {
        ticketService = mock(TicketService.class);
        ticketController = new TicketController(ticketService);
    }

    @Test
    public void whenBuyTicketIsSuccessThenGetSuccessPageWithPlace() {
        var request = new MockHttpServletRequest();
        request.setAttribute("user", new User(1, "user", "user@mail.com", "123"));
        var ticket = new Ticket(1, 1, 10, 15, 1);
        when(ticketService.save(any())).thenReturn(Optional.of(ticket));

        var model = new ConcurrentModel();
        var view = ticketController.buyTicket(ticket.getSessionId(), ticket.getRowNumber(),
                ticket.getPlaceNumber(), request, model);
        var actualMessage = model.getAttribute("message");
        var actualTicketMessage = model.getAttribute("ticket");

        assertThat(actualMessage).isEqualTo("Покупка билета произведена успешно!");
        assertThat(actualTicketMessage).isEqualTo(
                String.format("Ваш билет: %d ряд, %d место.", ticket.getRowNumber(), ticket.getPlaceNumber())
        );
        assertThat(view).isEqualTo("/tickets/success");
    }

    @Test
    public void whenBuyTicketIsInvalidThenGetInvalidPageAndMessage() {
        var request = new MockHttpServletRequest();
        request.setAttribute("user", new User(1, "user", "user@mail.com", "123"));
        var ticket = new Ticket(1, 1, 10, 15, 1);
        when(ticketService.save(any())).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = ticketController.buyTicket(ticket.getSessionId(), ticket.getRowNumber(),
                ticket.getPlaceNumber(), request, model);
        var actualMessage = model.getAttribute("message");

        assertThat(actualMessage).isEqualTo("""
                    Данный билет недоступен к покупке.  Вероятно оно уже занято.
                    Перейдите на страницу бронирования билетов и попробуйте снова.
                    """);
        assertThat(view).isEqualTo("/tickets/invalid");
    }
}