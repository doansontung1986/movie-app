package vn.techmaster.movie.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.techmaster.movie.entity.Blog;
import vn.techmaster.movie.service.BlogService;

import java.util.List;

@Controller
@RequestMapping("admin/blogs")
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;

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
    public String viewDetailPage(@PathVariable Integer id, Model model) {
        Blog blog = blogService.getBlogById(id);
        model.addAttribute("blogAdminDetail", blog);
        return "admin/blog/detail";
    }
}
