package ru.job4j.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.service.FilmSessionService;

@Controller
@RequestMapping("/filmSessions")
public class FilmSessionController {
    private final FilmSessionService filmSessionService;

    public FilmSessionController(FilmSessionService filmSessionService) {
        this.filmSessionService = filmSessionService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("filmSessionsDto", filmSessionService.findAllFilmSessionDto());
        return "filmSessions/list";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        var filmSessionOptional = filmSessionService.getFilmSessionPageById(id);
        if (filmSessionOptional.isEmpty()) {
            model.addAttribute("message", "Сеанс или фильм с указанным идентификатором не найден");
            return "errors/404";
        }
        model.addAttribute("filmSessionPageDto", filmSessionOptional.get());
        return "filmSessions/one";
    }
}
