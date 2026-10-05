package com.alok.urlshortener.repository;

import com.alok.urlshortener.model.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    Optional<ShortUrl> findByShortCode(String shortCode);
    boolean existsByShortCode(String shortCode);
    List<ShortUrl> findTop10ByOrderByCreatedAtDesc();
}