package vn.techmaster.movie.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/dashboard")
public class DashboardController {
    @GetMapping
    public String getDashboardPage(Model model) {
        return "admin/dashboard/index";
    }
}
