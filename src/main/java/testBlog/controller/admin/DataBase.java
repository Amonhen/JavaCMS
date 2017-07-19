package testBlog.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/database")
public class DataBase {
    @GetMapping("")
    public String getView(Model model) {
        model.addAttribute("view","admin/database/database");
        return "base-layout";
    }
}
