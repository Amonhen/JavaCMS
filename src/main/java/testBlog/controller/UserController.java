package testBlog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import testBlog.bindingModel.ResetPasswordBindingModel;
import testBlog.bindingModel.UserBindingModel;
import testBlog.config.communication.BlogMailSEnder;
import testBlog.controller.admin.SiteConfigController;
import testBlog.entity.Role;
import testBlog.entity.User;
import testBlog.repository.RoleRepository;
import testBlog.repository.UserRepository;
import org.springframework.ui.Model;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Controller
public class UserController {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("view","user/register");
        return "base-layout";
    }

    @PostMapping("/register")
    public String registerProcess(UserBindingModel userBindingModel) {
        if (!userBindingModel.getPassword().equals(userBindingModel.getConfirmPassword())) {
            return "redirect:/register";
        }
        System.out.println("User count: " + userRepository.count());
//        if (userRepository.count() > 1 ) {
//            if (userBindingModel.getUserName().equals(this.userRepository.findByUserName(userBindingModel.getUserName()).getUserName())) {
//                System.out.println("Username taken!");
//                return "redirect:/register";
//            }
//        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String ProfilePictureBase64 = "data:image/jpeg;base64,";
        try {
            ProfilePictureBase64 += Base64.getEncoder().encodeToString(userBindingModel.getProfilePictureBase64().getBytes());
        } catch (IOException exception) {
            System.out.println("Error: " + exception);
        }

        User user = new User(
                userBindingModel.getEmail(),
                userBindingModel.getFullName(),
                bCryptPasswordEncoder.encode(userBindingModel.getPassword()),
                userBindingModel.getUserName(),
                ProfilePictureBase64,
                userBindingModel.getUserInfo()
        );

        Role userRole = this.roleRepository.findByName("ROLE_USER");

        user.addRole(userRole);

        this.userRepository.saveAndFlush(user);


        return "redirect:/login";


    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("view","user/login");
        return "base-layout";
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null) {
            new SecurityContextLogoutHandler().logout(request,response,auth);
        }
        return "redirect:/login?logout";
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profilePage(Model model) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        User user  = this.userRepository.findByEmail(principal.getUsername());
        model.addAttribute("user",user);
        model.addAttribute("view","user/profile");
        return "base-layout";
    }

    @GetMapping("/recover")
    public String recover(Model model) {
        model.addAttribute("view","user/recover");
        return "base-layout";
    }

    @PostMapping("/recover")
    public String recover(ResetPasswordBindingModel resetPasswordBindingModel) {
        User user_new_pass = this.userRepository.findByEmail(resetPasswordBindingModel.getUserEmail());
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String newPass = bCryptPasswordEncoder.encode(UUID.randomUUID().toString());
        user_new_pass.setPassword(newPass);
        this.userRepository.saveAndFlush(user_new_pass);
        System.out.println("Changed password for user " + user_new_pass.getFullName());
        BlogMailSEnder blogMailSEnder  = new BlogMailSEnder();
        blogMailSEnder.sendMailToAddress(resetPasswordBindingModel.getUserEmail(),newPass);
        return "redirect:/login";
    }

    @GetMapping("/user-edit")
    public String userEdit(Model model) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        User user  = this.userRepository.findByEmail(principal.getUsername());
        model.addAttribute("user",user);
        model.addAttribute("view","user/user-edit");
        return "base-layout";
    }

    @PostMapping("/user-edit")
    public String userEditProcess(UserBindingModel userBindingModel) {
          User user = this.userRepository.findById(userBindingModel.getId());
          if (!user.getUserInfo().equals(userBindingModel.getUserInfo())) {
              user.setUserInfo(userBindingModel.getUserInfo());
          }
          if (!user.getFullName().equals(userBindingModel.getFullName())) {
              user.setFullName(userBindingModel.getFullName());
          }
          if (!user.getUserName().equals(userBindingModel.getUserName())) {
              user.setUserName(userBindingModel.getUserName());
          }
          if (!user.getEmail().equals(userBindingModel.getEmail())) {
              user.setUserInfo(userBindingModel.getUserInfo());
          }
          if (!userBindingModel.getPassword().isEmpty() && userBindingModel.getPassword().equals(userBindingModel.getConfirmPassword())) {
              BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
              user.setPassword(bCryptPasswordEncoder.encode(userBindingModel.getPassword()));
          }
          if (!userBindingModel.getProfilePictureBase64().isEmpty()) {
              try {
                  user.setProfilePictureBase64String("data:image/jpeg;base64,"+Base64.getEncoder().encodeToString(userBindingModel.getProfilePictureBase64().getBytes()));
              } catch (IOException exception) {
                  System.out.println(exception);
              }
          }
          this.userRepository.saveAndFlush(user);
          return "redirect:/user-edit";
    }

}
