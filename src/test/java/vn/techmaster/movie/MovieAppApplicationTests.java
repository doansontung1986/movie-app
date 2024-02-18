package vn.techmaster.movie;

import com.github.javafaker.Faker;
import com.github.slugify.Slugify;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import vn.techmaster.movie.entity.Movie;
import vn.techmaster.movie.model.enums.MovieType;
import vn.techmaster.movie.repository.MovieRepository;
import vn.techmaster.movie.service.MovieService;

import java.util.Date;
import java.util.List;
import java.util.Random;

@SpringBootTest
class MovieAppApplicationTests {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieService movieService;

    @Test
    void saveMovies() {
        Faker faker = new Faker();
        Slugify slugify = Slugify.builder().build();
        Random random = new Random();

        for (int i = 0; i < 100; i++) {
            String title = faker.book().title();
            boolean status = faker.bool().bool();
            Date createAt = faker.date().birthday();
            Date publishedAt = null;
            if (status) {
                publishedAt = createAt;
            }

            Movie movie = Movie.builder()
                    .title(title)
                    .slug(slugify.slugify(title))
                    .description(faker.lorem().paragraph())
                    .poster(faker.company().logo())
                    .releaseYear(faker.number().numberBetween(2021, 2024))
                    .view(faker.number().numberBetween(1000, 10000))
                    .rating(faker.number().randomDouble(1, 6, 10))
                    .type(MovieType.values()[random.nextInt(MovieType.values().length)])
                    .status(status)
                    .createdAt(createAt)
                    .updatedAt(createAt)
                    .publishedAt(publishedAt)
                    .build();

            movieRepository.save(movie);
        }
    }

    @Test
    void loadBannerMovies() {
        Page<Movie> pageDataBannerMovie = movieService.getHotMovies(true, 1, 2);
        List<Movie> bannerMovies = pageDataBannerMovie.getContent();
        for (int i = 0; i < bannerMovies.size(); i++) {
            String path = "/images/hero-" + (i + 1) + ".jpg";
            bannerMovies.get(i).setPoster(path);
            movieRepository.save(bannerMovies.get(i));
        }
    }

    @Test
    void loadHotMovies() {
        Page<Movie> pageDataHotMovie = movieService.getMoviesByType(MovieType.PHIM_LE, true, 1, 8);
        List<Movie> hotMovies = pageDataHotMovie.getContent();
        for (int i = 0; i < hotMovies.size(); i++) {
            String path = "/images/movie-" + (i + 1) + ".jpg";
            hotMovies.get(i).setPoster(path);
            movieRepository.save(hotMovies.get(i));
        }
    }

    @Test
    void loadSingleMovies() {
        Page<Movie> pageDataSingleMovie = movieService.getMoviesByType(MovieType.PHIM_LE, true, 1, 8);
        List<Movie> singleMovies = pageDataSingleMovie.getContent();
        for (int i = 0; i < singleMovies.size(); i++) {
            String path = "/images/movie-" + (i + 1) + ".jpg";
            singleMovies.get(i).setPoster(path);
            movieRepository.save(singleMovies.get(i));
        }
    }

    @Test
    void loadSeriesMovies() {
        Page<Movie> pageDataSeriesMovie = movieService.getMoviesByType(MovieType.PHIM_BO, true, 1, 8);
        List<Movie> seriesMovies = pageDataSeriesMovie.getContent();
        for (int i = 0; i < seriesMovies.size(); i++) {
            String path = "/images/movie-" + (i + 1) + ".jpg";
            seriesMovies.get(i).setPoster(path);
            movieRepository.save(seriesMovies.get(i));
        }
    }

    @Test
    void loadCinemaMovies() {
        Page<Movie> pageDataCinemaMovie = movieService.getMoviesByType(MovieType.PHIM_CHIEU_RAP, true, 1, 8);
        List<Movie> cinemaMovies = pageDataCinemaMovie.getContent();
        for (int i = 0; i < cinemaMovies.size(); i++) {
            String path = "/images/movie-" + (i + 1) + ".jpg";
            cinemaMovies.get(i).setPoster(path);
            movieRepository.save(cinemaMovies.get(i));
        }
    }

    @Test
    void testMovieRepo() {
//        List<Movie> movies = movieRepository.findAll();
//        System.out.println(movies.size());
//
//        Movie movie = movieRepository.findById(1).orElse(null);
//        System.out.println(movie);
//
//        movie.setTitle("Avatar 2");
//        movieRepository.save(movie);
//
//        movieRepository.delete(movie);
//
//        Movie movie2 = movieRepository.findById(2).orElse(null);
//        System.out.println(movie2);
//        movieRepository.delete(movie2);

//        List<Movie> movies = movieRepository.findAll(Sort.by("view").descending());
//        movies.forEach(movie -> System.out.println(movie.getView()));

        Pageable pageable = PageRequest.of(0, 6, Sort.by("publishedAt").descending());
        Page<Movie> pageData = movieRepository.findByTypeAndStatus(MovieType.PHIM_LE, true, pageable);
        System.out.println("Content: " + pageData.getContent());
        System.out.println("Total pages: " + pageData.getTotalPages());
        System.out.println("Total elements: " + pageData.getTotalElements());
        pageData.getContent().forEach(System.out::println);
    }
}
