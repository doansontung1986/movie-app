package vn.techmaster.movie.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import vn.techmaster.movie.entity.Movie;
import vn.techmaster.movie.entity.Review;
import vn.techmaster.movie.model.enums.MovieType;
import vn.techmaster.movie.service.MovieService;
import vn.techmaster.movie.service.ReviewService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebController {
    private final MovieService movieService;
    private final ReviewService reviewService;

    @GetMapping("/")
    public String getHome(Model model) {
        Page<Movie> pageDataHotMovie = movieService.getHotMovies(true, 1, 8);
        Page<Movie> pageDataSingleMovie = movieService.getMoviesByType(MovieType.PHIM_LE, true, 1, 20);
        Page<Movie> pageDataSeriesMovie = movieService.getMoviesByType(MovieType.PHIM_BO, true, 1, 20);
        Page<Movie> pageDataCinemaMovie = movieService.getMoviesByType(MovieType.PHIM_CHIEU_RAP, true, 1, 20);

        model.addAttribute("hotMovieList", pageDataHotMovie.getContent());
        model.addAttribute("singleMovieList", pageDataSingleMovie.getContent());
        model.addAttribute("seriesMovieList", pageDataSeriesMovie.getContent());
        model.addAttribute("cinemaMovieList", pageDataCinemaMovie.getContent());
        return "web/index";
    }

    // Danh sách phim chiếu rạp
    // /phim-chieu-rap?page=1&size=12 -> page = 1, size = 12
    // /phim-chieu-rap -> page = 1, size = 12
    @GetMapping("/phim-le")
    public String getSingleMovies(Model model,
                                  @RequestParam(required = false, defaultValue = "1") Integer page,
                                  @RequestParam(required = false, defaultValue = "20") Integer size) {
        Page<Movie> pageData = movieService.getMoviesByType(MovieType.PHIM_LE, true, page, size);

        model.addAttribute("singleMovieList", pageData.getContent());
        model.addAttribute("pageData", pageData);
        model.addAttribute("currentPage", page);
        return "web/phim-le";
    }

    @GetMapping("/phim-bo")
    public String getSeriesMovies(Model model,
                                  @RequestParam(required = false, defaultValue = "1") Integer page,
                                  @RequestParam(required = false, defaultValue = "20") Integer size) {
        Page<Movie> pageData = movieService.getMoviesByType(MovieType.PHIM_BO, true, page, size);

        model.addAttribute("seriesMovieList", pageData.getContent());
        model.addAttribute("pageData", pageData);
        model.addAttribute("currentPage", page);
        return "web/phim-bo";
    }

    @GetMapping("/phim-chieu-rap")
    public String getCinemaMovies(Model model,
                                  @RequestParam(required = false, defaultValue = "1") Integer page,
                                  @RequestParam(required = false, defaultValue = "20") Integer size) {
        Page<Movie> pageData = movieService.getMoviesByType(MovieType.PHIM_CHIEU_RAP, true, page, size);

        model.addAttribute("cinemaMovieList", pageData.getContent());
        model.addAttribute("pageData", pageData);
        model.addAttribute("currentPage", page);
        return "web//phim-chieu-rap";
    }

    @GetMapping("/phim/{id}/{slug}")
    public String getMovieById(@PathVariable Integer id, @PathVariable String slug, Model model) {
        Movie movie = movieService.getMovie(id, slug, true);
        List<Movie> relatedMovieList = movieService.getRelatedMovies(id, movie.getType(), true, 6);
        List<Review> reviewList = reviewService.getReviewsByMovie(id);

        model.addAttribute("movie", movie);
        model.addAttribute("relatedMovieList", relatedMovieList);
        model.addAttribute("reviewList", reviewList);
        return "web/chi-tiet-phim";
    }

    @GetMapping("/dang-nhap")
    public String getLoginPage() {
        return "web/auth/login";
    }

    @GetMapping("/dang-ky")
    public String getRegisterPage() {
        return "web/auth/register";
    }
}
