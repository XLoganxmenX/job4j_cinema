package ru.job4j.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.model.Ticket;
import ru.job4j.model.User;
import ru.job4j.service.TicketService;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/buy")
    public String buyTicket(@RequestParam("sessionId") int sessionId,
                            @RequestParam("selectedRow") int selectedRow,
                            @RequestParam("selectedPlace") int selectedPlace,
                            HttpServletRequest request,
                            Model model) {
        User user = (User) request.getAttribute("user");
        var optionalTicket = ticketService.save(new Ticket(0, sessionId, selectedRow, selectedPlace, user.getId()));
        if (optionalTicket.isEmpty()) {
            model.addAttribute("message", """
                    Данный билет недоступен к покупке.  Вероятно оно уже занято.
                    Перейдите на страницу бронирования билетов и попробуйте снова.
                    """
            );
            return "/tickets/invalid";
        }
        model.addAttribute("message", "Покупка билета произведена успешно!");
        model.addAttribute("ticket",
                String.format("Ваш билет: %d ряд, %d место.", selectedRow, selectedPlace)
        );
        return "/tickets/success";
    }
}
