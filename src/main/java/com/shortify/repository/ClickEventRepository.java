package com.shortify.repository;

import com.shortify.entity.ClickEvent;
import com.shortify.entity.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClickEventRepository extends JpaRepository<ClickEvent, Long> {
    List<ClickEvent> findByShortUrlOrderByClickedAtDesc(ShortUrl shortUrl);
    long countByShortUrl(ShortUrl shortUrl);
    
    // For analytics over time (e.g., last 30 days)
    List<ClickEvent> findByShortUrlAndClickedAtAfterOrderByClickedAtAsc(ShortUrl shortUrl, LocalDateTime clickedAt);
}
