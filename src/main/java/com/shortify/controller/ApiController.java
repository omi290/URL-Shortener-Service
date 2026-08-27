package com.shortify.controller;

import com.shortify.dto.UrlRequestDto;
import com.shortify.entity.ShortUrl;
import com.shortify.entity.User;
import com.shortify.service.UrlService;
import com.shortify.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/urls")
@RequiredArgsConstructor
public class ApiController {

    private final UrlService urlService;
    private final UserService userService;

    private User getAuthenticatedUser(Authentication authentication) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        return userService.findByEmail(email).orElseThrow(() -> new SecurityException("User not found"));
    }

    @PostMapping
    public ResponseEntity<ShortUrl> createUrl(@Valid @RequestBody UrlRequestDto request, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        ShortUrl shortUrl = urlService.shortenUrl(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(shortUrl);
    }

    @GetMapping
    public ResponseEntity<List<ShortUrl>> getUserUrls(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return ResponseEntity.ok(urlService.getUserUrls(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShortUrl> getUrl(@PathVariable Long id, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        ShortUrl url = urlService.getById(id).orElseThrow(() -> new IllegalArgumentException("URL not found"));
        if (!url.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Not authorized");
        }
        return ResponseEntity.ok(url);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUrl(@PathVariable Long id, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        urlService.deleteUrl(id, user);
        return ResponseEntity.ok(Map.of("message", "URL deleted successfully"));
    }

    @PutMapping("/{id}/toggle")
    public ResponseEntity<?> toggleUrl(@PathVariable Long id, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        urlService.toggleUrlStatus(id, user);
        return ResponseEntity.ok(Map.of("message", "URL status toggled"));
    }

    @GetMapping("/{id}/analytics")
    public ResponseEntity<?> getAnalytics(@PathVariable Long id, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        ShortUrl url = urlService.getById(id).orElseThrow(() -> new IllegalArgumentException("URL not found"));
        if (!url.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Not authorized");
        }
        return ResponseEntity.ok(urlService.getClickHistory(url));
    }
}
