package vn.techmaster.movie;

import com.github.javafaker.Faker;
import com.github.slugify.Slugify;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.techmaster.movie.entity.*;
import vn.techmaster.movie.model.enums.MovieType;
import vn.techmaster.movie.model.enums.UserRole;
import vn.techmaster.movie.repository.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest
public class InitDataTests {
    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void saveGenres() {
        Faker faker = new Faker();
        Slugify slugify = Slugify.builder().build();
        for (int i = 0; i < 30; i++) {
            String name = faker.funnyName().name();
            Genre genre = Genre.builder().name(name).slug(slugify.slugify(name)).build();
            genreRepository.save(genre);
        }
    }

    @Test
    void saveActors() {
        Faker faker = new Faker();
        for (int i = 0; i < 100; i++) {
            String name = faker.name().fullName();
            Actor actor = Actor.builder()
                    .name(name)
                    .description(faker.lorem().paragraph())
                    .avatar(generateLinkImage(name))
                    .birthday(faker.date().birthday())
                    .createdAt(new Date())
                    .updatedAt(new Date())
                    .build();

            actorRepository.save(actor);
        }
    }

    @Test
    void saveDirectors() {
        Faker faker = new Faker();
        for (int i = 0; i < 30; i++) {
            String name = faker.name().fullName();
            Director director = Director.builder()
                    .name(name)
                    .description(faker.lorem().paragraph())
                    .avatar(generateLinkImage(name))
                    .birthday(faker.date().birthday())
                    .createdAt(new Date())
                    .updatedAt(new Date())
                    .build();

            directorRepository.save(director);
        }
    }

    @Test
    void saveUsers() {
        Faker faker = new Faker();
        for (int i = 0; i < 20; i++) {
            String name = faker.name().fullName();
            User user = User.builder()
                    .name(name)
                    .password("123")
                    .email(faker.internet().emailAddress())
                    .avatar(generateLinkImage(name))
                    .role(i == 0 || i == 1 ? UserRole.ADMIN : UserRole.USER)
                    .createdAt(new Date())
                    .updatedAt(new Date())
                    .build();

            userRepository.save(user);
        }
    }

    @Test
    void saveBlogs() {
        Slugify slugify = Slugify.builder().build();
        Faker faker = new Faker();
        Random rd = new Random();

        List<User> userList = userRepository.findByRole(UserRole.ADMIN);

        Date start = new Calendar.Builder().setDate(2023, 8, 1).build().getTime();
        Date end = new Date();

        for (int i = 0; i < 20; i++) {
            String title = faker.book().title();
            boolean status = faker.bool().bool();
            Date createdAt = randomDateBetweenTwoDates(start, end);
            Date publishedAt = null;
            if (status) {
                publishedAt = createdAt;
            }

            Blog blog = Blog.builder()
                    .title(title)
                    .slug(slugify.slugify(title))
                    .description(faker.lorem().paragraph())
                    .content(faker.lorem().paragraph(100))
                    .status(rd.nextInt(2) == 0)
                    .user(userList.get(rd.nextInt(userList.size())))
                    .thumbnail(generateLinkImage(title))
                    .createdAt(createdAt)
                    .updatedAt(createdAt)
                    .publishedAt(publishedAt)
                    .build();

            blogRepository.save(blog);
        }
    }

    @Test
    void saveComments() {
        Faker faker = new Faker();
        Random rd = new Random();

        List<User> userList = userRepository.findAll();
        List<Blog> blogList = blogRepository.findAll();

        for (Blog blog : blogList) {
            // Mỗi blog sẽ có 1 số lượng comment ngẫu nhiên từ 5 -> 10 comment
            int numberOfComment = rd.nextInt(5) + 5;
            for (int i = 0; i < numberOfComment; i++) {
                Comment comment = Comment.builder()
                        .content(faker.lorem().paragraph())
                        .user(userList.get(rd.nextInt(userList.size())))
                        .blog(blog)
                        .createdAt(new Date())
                        .updatedAt(new Date())
                        .build();

                commentRepository.save(comment);
            }
        }
    }

    @Test
    void saveMovies() {
        Faker faker = new Faker();
        Slugify slugify = Slugify.builder().build();
        Random rd = new Random();

        Date start = new Calendar.Builder().setDate(2023, 8, 1).build().getTime();
        Date end = new Date();

        List<Genre> genreList = genreRepository.findAll();
        List<Actor> actorList = actorRepository.findAll();
        List<Director> directorList = directorRepository.findAll();

        for (int i = 0; i < 300; i++) {
            // Lấy ngẫu nhiên danh sách 1 -> 3 thể loại
            List<Genre> rdGenreList = new ArrayList<>();
            for (int j = 0; j < rd.nextInt(3) + 1; j++) {
                Genre genre = genreList.get(rd.nextInt(genreList.size()));
                if (!rdGenreList.contains(genre)) {
                    rdGenreList.add(genre);
                }
            }

            // Lấy ngẫu nhiên danh sách 5 -> 7 diễn viên
            List<Actor> rdActorList = new ArrayList<>();
            for (int j = 0; j < rd.nextInt(3) + 5; j++) {
                Actor actor = actorList.get(rd.nextInt(actorList.size()));
                if (!rdActorList.contains(actor)) {
                    actorList.add(actor);
                }
            }

            // Lấy ngẫu nhiên 1 -> 3 đạo diễn
            List<Director> rdDirectorList = new ArrayList<>();
            for (int j = 0; j < rd.nextInt(3) + 1; j++) {
                Director director = directorList.get(rd.nextInt(directorList.size()));
                if (!rdDirectorList.contains(director)) {
                    rdDirectorList.add(director);
                }
            }

            String title = faker.book().title();
            boolean status = faker.bool().bool();
            Date createdAt = randomDateBetweenTwoDates(start, end);
            Date publishedAt = null;
            if (status) {
                publishedAt = createdAt;
            }
            Movie movie = Movie.builder()
                    .title(title)
                    .slug(slugify.slugify(title))
                    .description(faker.lorem().paragraph())
                    .poster(generateLinkImage(title))
                    .releaseYear(faker.number().numberBetween(2021, 2024))
                    .view(faker.number().numberBetween(1000, 10000))
                    .rating(faker.number().randomDouble(1, 6, 10))
                    .type(MovieType.values()[rd.nextInt(MovieType.values().length)])
                    .status(status)
                    .createdAt(createdAt)
                    .updatedAt(createdAt)
                    .publishedAt(publishedAt)
                    .genres(rdGenreList)
                    .actors(rdActorList)
                    .directors(rdDirectorList)
                    .build();

            movieRepository.save(movie);
        }
    }

    @Test
    void saveReviews() {
        Faker faker = new Faker();
        Random rd = new Random();

        List<User> userList = userRepository.findAll();
        List<Movie> movieList = movieRepository.findAll();

        for (Movie movie: movieList) {
            // Mỗi phim sẽ có 1 số lượng review ngẫu nhiên từ 10 -> 20 review
            int numberOfReview = rd.nextInt(10) + 10;
            for (int i = 0; i < numberOfReview; i++) {
                Review review = Review.builder()
                        .comment(faker.lorem().paragraph())
                        .rating(faker.number().numberBetween(1, 10))
                        .user(userList.get(rd.nextInt(userList.size())))
                        .movie(movie)
                        .createdAt(new Date())
                        .updatedAt(new Date())
                        .build();

                reviewRepository.save(review);
            }
        }
    }

    // Write method to random date between 2 date
    private Date randomDateBetweenTwoDates(Date startInclusive, Date endExclusive) {
        long startMillis = startInclusive.getTime();
        long endMillis = endExclusive.getTime();
        long randomMillisSinceEpoch = ThreadLocalRandom.current().nextLong(startMillis, endMillis);
        return new Date(randomMillisSinceEpoch);
    }

    // Generate link author avatar follow struct : https://placehold.co/200x200?text=[...]
    private static String generateLinkImage(String str) {
        return "https://placehold.co/200x200?text=" + str.substring(0, 1).toUpperCase();
    }
}
