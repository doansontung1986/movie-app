package vn.techmaster.movie.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "images")
public class Image {
    @Id
    String id;

    String name;
    String type;
    Double size;
    String url;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    Date createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = new Date();
    }
}
