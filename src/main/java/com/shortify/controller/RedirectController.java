package com.shortify.controller;

import com.shortify.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class RedirectController {

    private final UrlService urlService;

    @GetMapping("/{shortCode:[a-zA-Z0-9]+}")
    public String redirect(@PathVariable String shortCode, HttpServletRequest request, Model model) {
        try {
            String referrer = request.getHeader("referer");
            String userAgent = request.getHeader("User-Agent");
            String originalUrl = urlService.resolveAndLogClick(shortCode, referrer, userAgent);
            return "redirect:" + originalUrl;
        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
}
