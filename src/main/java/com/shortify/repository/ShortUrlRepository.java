package com.shortify.repository;

import com.shortify.entity.ShortUrl;
import com.shortify.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    Optional<ShortUrl> findByShortCode(String shortCode);
    List<ShortUrl> findByUserOrderByCreatedAtDesc(User user);
    long countByUser(User user);
}
