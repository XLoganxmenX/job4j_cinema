package ru.job4j.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.service.FileService;
import ru.job4j.service.FilmService;
import ru.job4j.service.FilmSessionService;

@Controller
@RequestMapping("/filmSessions")
public class FilmSessionController {
    private final FilmSessionService filmSessionService;
    private final FilmService filmService;

    public FilmSessionController(FilmSessionService filmSessionService, FilmService filmService,
                                 FileService fileService) {
        this.filmSessionService = filmSessionService;
        this.filmService = filmService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("filmSessionsDto", filmSessionService.findAll());
        return "filmSessions/list";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        var filmSessionOptional = filmSessionService.findById(id);
        if (filmSessionOptional.isEmpty()) {
            model.addAttribute("message", "Сеанс или фильм с указанным идентификатором не найден");
            return "errors/404";
        }
        model.addAttribute("filmSessionDto", filmSessionOptional.get());
        var filmDto = filmService.findById(filmSessionOptional.get().getFilmId()).get();
        model.addAttribute("filmDto", filmDto);
        return "filmSessions/one";
    }
}
