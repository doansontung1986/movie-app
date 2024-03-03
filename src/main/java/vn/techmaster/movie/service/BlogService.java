package vn.techmaster.movie.service;

import com.github.slugify.Slugify;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.techmaster.movie.entity.Blog;
import vn.techmaster.movie.entity.User;
import vn.techmaster.movie.exception.ResourceNotFoundException;
import vn.techmaster.movie.model.request.UpsertBlogRequest;
import vn.techmaster.movie.model.request.UpsertReviewRequest;
import vn.techmaster.movie.repository.BlogRepository;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;
    private final HttpSession session;

    // Lấy danh sách tất cả blog
    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    // Lấy blog theo Id
    public Blog getBlogById(Integer id) {
        return blogRepository.findById(id).orElse(null);
    }

    // Lấy danh sách tất cả blog của user
    public List<Blog> getAllOwnBlogs() {
        User user = (User) session.getAttribute("currentUser");
        return blogRepository.findByUser_IdOrderByCreatedAtDesc(user.getId());

    }

    // Lấy danh sách tất cả blog
    public Page<Blog> getAllBlogByStatus(Boolean status, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by("publishedAt").descending());
        return blogRepository.findByStatus(status, pageRequest);
    }

    // Lấy danh sách blog theo id
    public Blog getBlog(Integer id, String slug, Boolean status) {
        return blogRepository.findByIdAndSlugAndStatus(id, slug, status).orElse(null);
    }

    public Blog createBlog(UpsertBlogRequest upsertBlogRequest) {
        User user = (User) session.getAttribute("currentUser");

        Slugify slugify = Slugify.builder().build();

        Date publishedAt = null;

        Boolean status = upsertBlogRequest.getStatus();

        if (status) {
            publishedAt = new Date();
        }

        Blog blog = Blog.builder()
                .title(upsertBlogRequest.getTitle())
                .description(upsertBlogRequest.getDescription())
                .slug(slugify.slugify(upsertBlogRequest.getTitle()))
                .content(upsertBlogRequest.getContent())
                .status(upsertBlogRequest.getStatus())
                .createdAt(new Date())
                .updatedAt(new Date())
                .publishedAt(publishedAt)
                .user(user)
                .build();

        return blogRepository.save(blog);
    }

    public Blog updateBlog(Integer id, UpsertBlogRequest upsertBlogRequest) {
        // Kiểm tra blog có tồn tại không
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find blog by Id: " + id));

        Slugify slugify = Slugify.builder().build();

        Date publishedAt = null;

        Boolean status = upsertBlogRequest.getStatus();

        if (status) {
            publishedAt = new Date();
        }

        blog.setContent(upsertBlogRequest.getContent());
        blog.setDescription(upsertBlogRequest.getDescription());
        blog.setStatus(upsertBlogRequest.getStatus());
        blog.setTitle(upsertBlogRequest.getTitle());
        blog.setUpdatedAt(new Date());
        blog.setPublishedAt(publishedAt);
        blog.setSlug(slugify.slugify(upsertBlogRequest.getTitle()));

        return blogRepository.save(blog);
    }

    public void deleteBlog(Integer id) {
        // Kiểm tra blog có tồn tại không
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find blog by Id: " + id));
        blogRepository.delete(blog);
    }
}
