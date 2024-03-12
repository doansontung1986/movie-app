package vn.techmaster.movie.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.techmaster.movie.model.request.UpsertEpisodeRequest;
import vn.techmaster.movie.service.EpisodeService;

@RestController
@RequestMapping("/api/admin/episodes")
@RequiredArgsConstructor
public class EpisodeResource {
    private final EpisodeService episodeService;

    @PostMapping("/{id}/upload-video")
    public ResponseEntity<?> uploadVideo(@RequestParam("file") MultipartFile file, @PathVariable Integer id) {
        episodeService.uploadVideo(id, file);
        return ResponseEntity.ok().build(); // status code 200
    }

    @PostMapping
    public ResponseEntity<?> createEpisode(@RequestBody UpsertEpisodeRequest request, @PathVariable Integer id) {
        episodeService.createEpisode(request, id);
        return ResponseEntity.ok().build(); // status code 200
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateEpisode(@PathVariable Integer id, @RequestBody UpsertEpisodeRequest request) {
        episodeService.updateEpisode(id, request);
        return ResponseEntity.ok().build(); // status code 200
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEpisode(@PathVariable Integer id) {
        episodeService.deleteEpisode(id);
        return ResponseEntity.noContent().build(); // status code 204
    }
}
