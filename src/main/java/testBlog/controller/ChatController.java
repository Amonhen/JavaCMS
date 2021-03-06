package testBlog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chat-page")
public class ChatController {

    @GetMapping("")
    public String getMainChatPage(Model model) {
        model.addAttribute("view","chat/main");
        return "base-layout";
    }
}
