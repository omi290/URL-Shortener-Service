package com.shortify.service;

import com.shortify.dto.UrlRequestDto;
import com.shortify.entity.ClickEvent;
import com.shortify.entity.ShortUrl;
import com.shortify.entity.User;
import com.shortify.repository.ClickEventRepository;
import com.shortify.repository.ShortUrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final ShortUrlRepository shortUrlRepository;
    private final ClickEventRepository clickEventRepository;

    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    @Transactional
    public ShortUrl shortenUrl(UrlRequestDto requestDto, User user) {
        String shortCode = requestDto.getCustomAlias();

        if (shortCode != null && !shortCode.trim().isEmpty()) {
            if (shortUrlRepository.findByShortCode(shortCode).isPresent()) {
                throw new IllegalArgumentException("Custom alias already in use");
            }
        } else {
            shortCode = generateShortCode();
        }

        ShortUrl shortUrl = ShortUrl.builder()
                .originalUrl(requestDto.getOriginalUrl())
                .shortCode(shortCode)
                .expiresAt(requestDto.getExpiresAt())
                .user(user)
                .active(true)
                .clickCount(0)
                .build();

        return shortUrlRepository.save(shortUrl);
    }

    private String generateShortCode() {
        String code;
        do {
            StringBuilder sb = new StringBuilder(6);
            for (int i = 0; i < 6; i++) {
                int index = (int) (Math.random() * BASE62.length());
                sb.append(BASE62.charAt(index));
            }
            code = sb.toString();
        } while (shortUrlRepository.findByShortCode(code).isPresent());
        return code;
    }

    public List<ShortUrl> getUserUrls(User user) {
        return shortUrlRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public Optional<ShortUrl> getByShortCode(String shortCode) {
        return shortUrlRepository.findByShortCode(shortCode);
    }

    public Optional<ShortUrl> getById(Long id) {
        return shortUrlRepository.findById(id);
    }

    @Transactional
    public void deleteUrl(Long id, User user) {
        ShortUrl url = shortUrlRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("URL not found"));
        
        if (!url.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Not authorized to delete this URL");
        }
        
        shortUrlRepository.delete(url);
    }
    
    @Transactional
    public void toggleUrlStatus(Long id, User user) {
        ShortUrl url = shortUrlRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("URL not found"));
                
        if (!url.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Not authorized to modify this URL");
        }
        
        url.setActive(!url.isActive());
        shortUrlRepository.save(url);
    }

    @Transactional
    public String resolveAndLogClick(String shortCode, String referrer, String userAgent) {
        ShortUrl url = shortUrlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new IllegalArgumentException("URL not found"));

        if (!url.isActive()) {
            throw new IllegalStateException("URL is inactive");
        }

        if (url.getExpiresAt() != null && url.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("URL has expired");
        }

        url.setClickCount(url.getClickCount() + 1);
        shortUrlRepository.save(url);

        ClickEvent clickEvent = ClickEvent.builder()
                .shortUrl(url)
                .referrer(referrer != null ? (referrer.length() > 255 ? referrer.substring(0, 255) : referrer) : "Unknown")
                .userAgent(userAgent != null ? (userAgent.length() > 255 ? userAgent.substring(0, 255) : userAgent) : "Unknown")
                .build();
        clickEventRepository.save(clickEvent);

        return url.getOriginalUrl();
    }
    
    public List<ClickEvent> getClickHistory(ShortUrl shortUrl) {
        return clickEventRepository.findByShortUrlOrderByClickedAtDesc(shortUrl);
    }
    
    public long getTotalUrls(User user) {
        return shortUrlRepository.countByUser(user);
    }
    
    public long getTotalClicks(User user) {
        return getUserUrls(user).stream().mapToLong(ShortUrl::getClickCount).sum();
    }
}
