package vn.techmaster.movie.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.techmaster.movie.entity.Genre;
import vn.techmaster.movie.service.GenreService;

import java.util.List;

@Controller
@RequestMapping("admin/genre")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/")
    public String viewHomePage(Model model) {
        List<Genre> genres = genreService.getAllGenres();
        model.addAttribute("genres", genres);
        return "admin/genre/index";
    }

    @GetMapping("/create-genre")
    public String viewCreatePage() {
        return "admin/genre/create";
    }
}
