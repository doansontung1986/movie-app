package vn.techmaster.movie.service;

import com.github.slugify.Slugify;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.techmaster.movie.entity.Actor;
import vn.techmaster.movie.entity.Director;
import vn.techmaster.movie.entity.Genre;
import vn.techmaster.movie.entity.Movie;
import vn.techmaster.movie.exception.ResourceNotFoundException;
import vn.techmaster.movie.model.enums.MovieType;
import vn.techmaster.movie.model.request.UpsertMovieRequest;
import vn.techmaster.movie.repository.ActorRepository;
import vn.techmaster.movie.repository.DirectorRepository;
import vn.techmaster.movie.repository.GenreRepository;
import vn.techmaster.movie.repository.MovieRepository;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final DirectorRepository directorRepository;
    private final ActorRepository actorRepository;
    private final GenreRepository genreRepository;

    public Page<Movie> getHotMovies(Boolean status, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by("view").descending());
        return movieRepository.findByStatus(status, pageRequest);
    }

    public List<Movie> getMoviesByType(MovieType movieType, Boolean status, Sort sort) {
        return movieRepository.findByTypeAndStatus(movieType, status, sort);
    }

    public Page<Movie> getMoviesByType(MovieType movieType, Boolean status, Pageable pageable) {
        return movieRepository.findByTypeAndStatus(movieType, status, pageable);
    }

    public Page<Movie> getMoviesByType(MovieType movieType, Boolean status, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by("publishedAt").descending());
        return movieRepository.findByTypeAndStatus(movieType, status, pageRequest);
    }

    public Movie getMovie(Integer id, String slug, Boolean status) {
        return movieRepository.findByIdAndSlugAndStatus(id, slug, status).orElse(null);
    }

    public Movie getMovie(Integer id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phim với id = " + id));
    }

    public List<Movie> getRelatedMovies(Integer id, MovieType movieType, Boolean status, Integer size) {
        return movieRepository.findByTypeAndStatusAndRatingGreaterThanEqualAndIdNotOrderByRatingDescViewDescPublishedAtDesc(movieType, status, 5.0, id).stream().limit(size).toList();
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll(Sort.by("createdAt").descending());
    }

    public Movie createMovie(UpsertMovieRequest request) {
        // Lấy danh sách đạo diễn, diễn viên, thể loại từ request
        List<Director> directors = directorRepository.findAllById(request.getDirectorIds());
        List<Actor> actors = actorRepository.findAllById(request.getActorIds());
        List<Genre> genres = genreRepository.findAllById(request.getGenreIds());

        // Bổ sung các thông tin khác cho movie từ request
        Movie movie = Movie.builder().title(request.getTitle()).directors(directors).actors(actors).genres(genres).build();

        movieRepository.save(movie);
        return movie;
    }

    public Movie updateMovie(Integer id, UpsertMovieRequest upsertMovieRequest) {
        // tìm kiếm phim theo id
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find Movie by Id : " + id));
        Slugify slugify = Slugify.builder().build();
        Boolean status = upsertMovieRequest.getStatus();
        Date publishedAt = null;
        if (status) {
            publishedAt = new Date();
        }
        List<Actor> actorList = actorRepository.findAllById(upsertMovieRequest.getActorIds());
        List<Director> directorList = directorRepository.findAllById(upsertMovieRequest.getDirectorIds());
        List<Genre> genreList = genreRepository.findAllById(upsertMovieRequest.getGenreIds());

        movie.setTitle(upsertMovieRequest.getTitle());
        movie.setDescription(upsertMovieRequest.getDescription());
        movie.setSlug(slugify.slugify(upsertMovieRequest.getTitle()));
        movie.setStatus(status);
        movie.setType(upsertMovieRequest.getType());
        movie.setReleaseYear(upsertMovieRequest.getReleaseYear());
        movie.setPoster(upsertMovieRequest.getPoster());
        movie.setPublishedAt(publishedAt);
        movie.setDirectors(directorList);
        movie.setActors(actorList);
        movie.setGenres(genreList);

        return movieRepository.save(movie);
    }

    public void deleteMovie(Integer id) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cannot find by id :" + id));
        movieRepository.delete(movie);
    }

    public Movie findMovieById(Integer id) {
        return movieRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cannot find by id :" + id));
    }
}
