package testBlog.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import testBlog.entity.Article;
import testBlog.entity.Category;
import testBlog.repository.ArticleRepository;
import testBlog.repository.CategoryRepository;

import java.util.List;
import java.util.Set;

@Controller
public class HomeController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/")
    public String index() {
        return "redirect:/profile";
    }

    @RequestMapping("/error/403")
    public String accessDenied(Model model) {
        model.addAttribute("view","error/403");
        return "base-layout";
    }

    @GetMapping("category/{id}")
    public String listArticles(Model model, @PathVariable Integer id) {
        if(!this.categoryRepository.exists(id)) {
            return "redirect:/";
        }
        Category category = this.categoryRepository.findOne(id);
        Set<Article> articles = category.getArticles();
        model.addAttribute("articles",articles);
        model.addAttribute("category",category);
        model.addAttribute("view","home/list-articles");
        return "base-layout";
    }
}
