package vn.techmaster.movie.model.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpsertReviewRequest {
    String comment;
    Integer rating;
    Integer movieId;
}
