package com.shortify.controller;

import com.shortify.dto.UserDto;
import com.shortify.entity.ShortUrl;
import com.shortify.entity.User;
import com.shortify.service.UrlService;
import com.shortify.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final UserService userService;
    private final UrlService urlService;

    @GetMapping("/")
    public String index(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/dashboard";
        }
        model.addAttribute("userDto", new UserDto());
        return "index";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userDto") UserDto userDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "index";
        }
        try {
            userService.registerUser(userDto);
            return "redirect:/?success";
        } catch (IllegalArgumentException e) {
            model.addAttribute("registrationError", e.getMessage());
            return "index";
        }
    }

    private User getAuthenticatedUser(Authentication authentication) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        return userService.findByEmail(email).orElseThrow(() -> new SecurityException("User not found"));
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        model.addAttribute("urls", urlService.getUserUrls(user));
        model.addAttribute("totalUrls", urlService.getTotalUrls(user));
        model.addAttribute("totalClicks", urlService.getTotalClicks(user));
        return "dashboard";
    }

    @GetMapping("/analytics/{id}")
    public String analytics(@PathVariable Long id, Model model, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        ShortUrl url = urlService.getById(id).orElseThrow(() -> new IllegalArgumentException("URL not found"));
        if (!url.getUser().getId().equals(user.getId())) {
            return "redirect:/dashboard?error=unauthorized";
        }
        model.addAttribute("url", url);
        model.addAttribute("clicks", urlService.getClickHistory(url));
        return "analytics";
    }
}
