package vn.techmaster.movie.model.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.techmaster.movie.entity.Movie;
import vn.techmaster.movie.model.enums.MovieType;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpsertEpisodeRequest {
    String title;
    Integer displayOrder;
    Boolean status;
    Date createdAt;
    Date updatedAt;
    Date publishedAt;
    Movie movie;
}
