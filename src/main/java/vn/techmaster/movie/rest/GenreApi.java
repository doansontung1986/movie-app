package vn.techmaster.movie.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.techmaster.movie.entity.Genre;
import vn.techmaster.movie.model.request.UpsertGenreRequest;
import vn.techmaster.movie.service.GenreService;

@RestController
@RequestMapping("/api/admin/genre")
@RequiredArgsConstructor
public class GenreApi {
    private final GenreService genreService;

    @PostMapping
    public ResponseEntity<?> createBlog(@RequestBody UpsertGenreRequest upsertGenreRequest) {
        Genre genre = genreService.createNewGenre(upsertGenreRequest);
        return new ResponseEntity<>(genre, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBlog(@PathVariable Integer id, @RequestBody UpsertGenreRequest upsertGenreRequest) {
        Genre genre = genreService.updateGenre(id, upsertGenreRequest);
        return ResponseEntity.ok(genre);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBlog(@PathVariable Integer id) {
        genreService.deleteGenre(id);
        return ResponseEntity.noContent().build();
    }
}
