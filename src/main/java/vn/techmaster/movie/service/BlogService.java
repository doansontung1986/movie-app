package vn.techmaster.movie.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.techmaster.movie.entity.Blog;
import vn.techmaster.movie.repository.BlogRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;

    // Lấy danh sách tất cả blog
    public Page<Blog> getAllBlogs(Boolean status, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by("publishedAt").descending());
        return blogRepository.findByStatus(status, pageRequest);
    }

    // Lấy danh sách blog theo id
    public List<Blog> getBlogsById(Integer id) {
        return blogRepository.findByIdOrderByPublishedAtDesc(id);
    }
}
