package vn.techmaster.movie;

import com.github.javafaker.Faker;
import com.github.slugify.Slugify;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import vn.techmaster.movie.entity.Movie;
import vn.techmaster.movie.entity.User;
import vn.techmaster.movie.model.enums.MovieType;
import vn.techmaster.movie.repository.MovieRepository;
import vn.techmaster.movie.repository.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.Random;

@SpringBootTest
class MovieAppApplicationTests {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void saveMovies() {
        Faker faker = new Faker();
        Slugify slugify = Slugify.builder().build();
        Random random = new Random();

        for (int i = 0; i < 200; i++) {
            String title = faker.book().title();
            boolean status = faker.bool().bool();
            Date createAt = faker.date().birthday();
            Date publishedAt = null;
            if (status) {
                publishedAt = createAt;
            }

            Movie movie = Movie.builder().title(title).slug(slugify.slugify(title)).description(faker.lorem().paragraph()).poster(faker.company().logo()).releaseYear(faker.number().numberBetween(2021, 2024)).view(faker.number().numberBetween(1000, 10000)).rating(faker.number().randomDouble(1, 6, 10)).type(MovieType.values()[random.nextInt(MovieType.values().length)]).status(status).createdAt(createAt).updatedAt(createAt).publishedAt(publishedAt).build();

            movieRepository.save(movie);
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

    @Test
    void update_password() {
        List<User> userList = userRepository.findAll();
        userList.forEach(user -> {
            user.setPassword(passwordEncoder.encode("123"));
            userRepository.save(user);
        });
    }
}
