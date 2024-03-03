package vn.techmaster.movie.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.movie.entity.Blog;
import vn.techmaster.movie.entity.User;

import java.util.List;
import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, Integer> {
    Page<Blog> findByStatus(Boolean status, Pageable pageable);

    Optional<Blog> findByIdAndSlugAndStatus(Integer id, String slug, Boolean status);

    List<Blog> findByUser_IdOrderByCreatedAtDesc(Integer id);
}
