package vn.techmaster.movie.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.techmaster.movie.entity.Blog;
import vn.techmaster.movie.entity.Image;
import vn.techmaster.movie.service.BlogService;
import vn.techmaster.movie.service.ImageService;

import java.util.List;

@Controller
@RequestMapping("admin/blogs")
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;
    private final ImageService imageService;

    @GetMapping("/")
    public String viewHomePage(Model model) {
        List<Blog> blogList = blogService.getAllBlogs();
        model.addAttribute("blogList", blogList);
        return "admin/blog/index";
    }

    @GetMapping("/own-blogs")
    public String viewOwnBlogPage(Model model) {
        List<Blog> myBlogList = blogService.getAllOwnBlogs();
        model.addAttribute("myBlogList", myBlogList);
        return "admin/blog/own-blog";
    }

    @GetMapping("/create-blog")
    public String viewCreatePage() {
        return "admin/blog/create";
    }

    @GetMapping("/{id}/detail-blog")
    public String viewDetailPage(@PathVariable Integer id, Model model,
                                 @RequestParam(required = false, defaultValue = "1") Integer page,
                                 @RequestParam(required = false, defaultValue = "20") Integer size) {
        Blog blog = blogService.getBlogById(id);

        Page<Image> pageData = imageService.getAllImagesByCurrentUser(page, size);

        model.addAttribute("blogAdminDetail", blog);
        model.addAttribute("pageData", pageData);
        model.addAttribute("currentPage", page);
        model.addAttribute("images", pageData.getContent());
        return "admin/blog/detail";
    }
}
