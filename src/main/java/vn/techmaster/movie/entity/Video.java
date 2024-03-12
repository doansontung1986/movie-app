package vn.techmaster.movie.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "videos")
public class Video {
    @Id
    String id;

    // Những thông tin cần thiết cho video thì bổ sung thêm
    String name;
    String type;
    Double size;
    Integer duration;
    String url;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    Date createAt;

    @PrePersist
    public void prePersist() {
        createAt = new Date();
    }
}
