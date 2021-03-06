package testBlog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import testBlog.bindingModel.ArticleBindingModel;
import testBlog.entity.Article;
import testBlog.entity.Category;
import testBlog.entity.Tag;
import testBlog.entity.User;
import testBlog.repository.ArticleRepository;
import testBlog.repository.CategoryRepository;
import testBlog.repository.TagRepository;
import testBlog.repository.UserRepository;
import org.springframework.ui.Model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TagRepository tagRepository;

    @GetMapping("/article/options")
    @PreAuthorize("isAuthenticated()")
    public String options( Model model ) {
        List<Category> categories = this.categoryRepository.findAll();
        model.addAttribute("view","article/options");
        model.addAttribute("categories",categories);
        return "base-layout";
    }

    @GetMapping("/article/create")
    @PreAuthorize("isAuthenticated()")
    public String create( Model model ) {
        List<Category> categories = this.categoryRepository.findAll();
        model.addAttribute("categories",categories);
        model.addAttribute("view","article/create");
        return "base-layout";
    }
    @PostMapping("/article/create")
    @PreAuthorize("isAuthenticated()")
    public String createProcess(ArticleBindingModel articleBindingModel) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        User userEntity =this.userRepository.findByEmail(user.getUsername());
        Category category = this.categoryRepository.findOne(articleBindingModel.getCategoryId());
        HashSet<Tag> tags = this.findTagsFromString(articleBindingModel.getTagString());
        List<String> arrayOfbase64StringsOfMultiPartfiles = new ArrayList<>();
        String stringOfarrayOfbase64StringsOfMultiPartfiles;
        for (int i = 0; i < articleBindingModel.getImagestoBeInBase64().length; i++) {
            try {
              arrayOfbase64StringsOfMultiPartfiles.add(Base64.getEncoder().encodeToString(articleBindingModel.getImagestoBeInBase64()[i].getBytes()));
            } catch (IOException exception) {
                System.out.println(exception);
            }
        }
        ByteArrayOutputStream serialize = new ByteArrayOutputStream();
        try {
            new ObjectOutputStream(serialize).writeObject(arrayOfbase64StringsOfMultiPartfiles.toArray());
        } catch (IOException exception) {
            System.out.println(exception);
        }
        stringOfarrayOfbase64StringsOfMultiPartfiles = Base64.getEncoder().encodeToString(serialize.toByteArray());
        Article articleEntity = new Article(articleBindingModel.getTitle(),articleBindingModel.getContent(),userEntity,category,tags,stringOfarrayOfbase64StringsOfMultiPartfiles);
        this.articleRepository.saveAndFlush(articleEntity);
        return "redirect:/";
    }

    @GetMapping("article/{id}")
    public String details(Model model, @PathVariable Integer id) {
        if (!this.articleRepository.exists(id)) {
            return "redirect:/";
        }
        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();
            User entityUser = this.userRepository.findByEmail(principal.getUsername());
            model.addAttribute("user",entityUser);
        }
        Article article = this.articleRepository.findOne(id);
        ByteArrayInputStream unserialize = new ByteArrayInputStream(Base64.getDecoder().decode(article.getImagesInBase64StringsArray()));
        String[] articleImages = new String[unserialize.read()];
        System.out.println(articleImages);
        model.addAttribute("article",article);
        model.addAttribute("view","article/details");
        return  "base-layout";
    }

    @GetMapping("article/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String edit(@PathVariable Integer id, Model model) {
        if (!this.articleRepository.exists(id)) {
            return "redirect:/";
        }
        Article article = this.articleRepository.findOne(id);

        if (!isUserAuthorOrAdmin(article)) {
            return "redirect:/article" + id;
        }

        List<Category> categories = this.categoryRepository.findAll();

        String tagString = article.getTags().stream().map(Tag::getName).collect(Collectors.joining(", "));

        model.addAttribute("view","article/edit");
        model.addAttribute("article",article);
        model.addAttribute("categories",categories);
        model.addAttribute("tags",tagString);
        return "base-layout";
    }

    @PostMapping("article/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editProcess(@PathVariable Integer id, ArticleBindingModel articleBindingModel) {
        if (!this.articleRepository.exists(id)) {
            return "redirect:/";
        }

        Article article = this.articleRepository.findOne(id);
        Category category = this.categoryRepository.findOne(articleBindingModel.getCategoryId());
        HashSet<Tag> tags = this.findTagsFromString(articleBindingModel.getTagString());

        if (!isUserAuthorOrAdmin(article)) {
            return "redirect:/article" + id;
        }

        article.setCategory(category);
        article.setContent(articleBindingModel.getContent());
        article.setTitle(articleBindingModel.getTitle());
        article.setTags(tags);
        this.articleRepository.saveAndFlush(article);
        return "redirect:/article/" + article.getId();
    }

    @GetMapping("article/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(@PathVariable Integer id, Model model) {
        if (!this.articleRepository.exists(id)) {
            return "redirect:/";
        }
        Article article = this.articleRepository.findOne(id);

        if (!isUserAuthorOrAdmin(article)) {
            return "redirect:/article" + id;
        }

        model.addAttribute("view","article/delete");
        model.addAttribute("article",article);
        return "base-layout";
    }

    private boolean isUserAuthorOrAdmin(Article article) {
        UserDetails user  = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        User userEntity = this.userRepository.findByEmail(user.getUsername());

        return userEntity.isAdmin() || userEntity.isAuthor(article);

    }

    @PostMapping("article/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteProcess(@PathVariable Integer id) {
        if (!this.articleRepository.exists(id)) {
            return "redirect:/";
        }

        Article article = this.articleRepository.findOne(id);

        if (!isUserAuthorOrAdmin(article)) {
            return "redirect:/article" + id;
        }

        this.articleRepository.delete(article);
        return "redirect:/";
    }

    private HashSet<Tag> findTagsFromString(String tagString) {
        HashSet<Tag> tags = new HashSet<>();
        String[] tagNames = tagString.split(",\\s*");
        for (String tagName : tagNames){
            Tag currentTag = this.tagRepository.findByName(tagName);
            if (currentTag == null) {
                currentTag = new Tag(tagName);
                this.tagRepository.saveAndFlush(currentTag);
            }
            tags.add(currentTag);
        }
        return tags;
    }

}
