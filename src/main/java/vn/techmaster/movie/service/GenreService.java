package vn.techmaster.movie.service;

import com.github.slugify.Slugify;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.techmaster.movie.entity.Genre;
import vn.techmaster.movie.exception.ResourceNotFoundException;
import vn.techmaster.movie.model.request.UpsertGenreRequest;
import vn.techmaster.movie.repository.GenreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    public Genre createNewGenre(UpsertGenreRequest request) {
        Genre existGenre = genreRepository.findAll().stream()
                .filter(genre -> genre.getName().equals(request.getName()))
                .findFirst().orElse(null);

        if (existGenre != null) {
            throw new RuntimeException(request.getName() + " is existed.");
        }

        Slugify slugify = Slugify.builder().build();

        Genre genre = Genre.builder()
                .name(request.getName())
                .slug(slugify.slugify(request.getName()))
                .build();

        return genreRepository.save(genre);
    }

    public Genre updateGenre(Integer id, UpsertGenreRequest request) {
        Genre genre = genreRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Cannot find genre by Id: " + id)
        );

        Genre existGenre = genreRepository.findAll().stream()
                .filter(genreEle -> genreEle.getName().equals(request.getName()))
                .findFirst().orElse(null);

        if (existGenre != null) {
            throw new RuntimeException(request.getName() + " is existed.");
        }

        genre.setName(request.getName());

        return genreRepository.save(genre);
    }

    public void deleteGenre(Integer id) {
        Genre genre = genreRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Cannot find genre by Id: " + id)
        );
        genreRepository.delete(genre);
    }
}
