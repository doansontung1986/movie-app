package vn.techmaster.movie.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.techmaster.movie.entity.Episode;
import vn.techmaster.movie.entity.Movie;
import vn.techmaster.movie.entity.Video;
import vn.techmaster.movie.exception.ResourceNotFoundException;
import vn.techmaster.movie.model.enums.MovieType;
import vn.techmaster.movie.model.request.UpsertEpisodeRequest;
import vn.techmaster.movie.repository.EpisodeRepository;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EpisodeService {
    private final EpisodeRepository episodeRepository;
    private final VideoService videoService;
    private final MovieService movieService;

    // Lấy danh sách tập phim của movie sắp xếp theo displayOrder tăng dần
    public List<Episode> getEpisodeListOfMovie(Integer movieId) {
        return episodeRepository.findByMovie_IdOrderByDisplayOrderAsc(movieId);
    }

    public List<Episode> getEpisodeListOfMovie(Integer movieId, Boolean status) {
        return episodeRepository.findByMovie_IdAndStatusOrderByDisplayOrderAsc(movieId, status);
    }

    public Episode getEpisode(Integer movieId, String tap, boolean episodeStatus) {
        if (tap.equals("full")) {
            return episodeRepository.findByMovie_IdAndDisplayOrderAndStatus(movieId, 1, episodeStatus).orElse(null);
        } else {
            return episodeRepository.findByMovie_IdAndDisplayOrderAndStatus(movieId, Integer.parseInt(tap), episodeStatus).orElse(null);
        }
    }

    public void uploadVideo(Integer id, MultipartFile file) {
        log.info("Uploading video for episode with id = {}", id);
        log.info("File name: {}", file.getOriginalFilename());

        // Kiểm tra tập phim có tồn tại không
        Episode episode = episodeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tập phim có id = " + id));

        // Upload video
        Video video = videoService.uploadVideo(file);

        episode.setVideoUrl(video.getUrl());
        episode.setDuration(video.getDuration());
        episodeRepository.save(episode);
    }

    public void deleteEpisode(Integer id) {
        // Kiểm tra tập phim có tồn tại không
        Episode episode = episodeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tập phim có id = " + id));
        episodeRepository.deleteById(episode.getId());
    }

    public void updateEpisode(Integer id, UpsertEpisodeRequest request) {
        // Kiểm tra tập phim có tồn tại không
        Episode episode = episodeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tập phim có id = " + id));

        episode.setTitle(request.getTitle());
        episode.setDisplayOrder(request.getDisplayOrder());
        episode.setStatus(request.getStatus());
        episode.setCreatedAt(request.getCreatedAt());
        episode.setUpdatedAt(request.getUpdatedAt());
        episode.setPublishedAt(request.getPublishedAt());
        episode.setMovie(request.getMovie());
    }

    public void createEpisode(UpsertEpisodeRequest request, Integer id) {
        Random random = new Random();
        Movie movie = movieService.findMovieById(id);
        Episode episode = null;

        if (movie.getType().equals(MovieType.PHIM_BO)) {
            // Sử dụng vòng lặp để tạo ra 5 -> 10 tập phim
            for (int i = 0; i < random.nextInt(5) + 5; i++) {
                episode = Episode.builder()
                        .title("Tập " + (i + 1))
                        .displayOrder(i + 1)
                        .status(true)
                        .createdAt(new Date())
                        .updatedAt(new Date())
                        .publishedAt(new Date())
                        .movie(movie)
                        .build();
            }
        } else {
            // Trường hợp phim lẻ, phim chiếu rạp thì chỉ có 1 tập phim
            episode = Episode.builder()
                    .title("Tập full")
                    .displayOrder(1)
                    .status(true)
                    .createdAt(new Date())
                    .updatedAt(new Date())
                    .publishedAt(new Date())
                    .movie(movie)
                    .build();
        }

        episodeRepository.save(episode);
    }
}
