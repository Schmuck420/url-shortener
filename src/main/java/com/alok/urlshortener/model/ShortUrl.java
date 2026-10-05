package com.alok.urlshortener.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name="short_urls", indexes=@Index(name="idx_short_code", columnList="shortCode", unique=true))
public class ShortUrl {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, unique=true, length=12) private String shortCode;
    @Column(nullable=false, length=2048) private String originalUrl;
    @Column(nullable=false) private Instant createdAt;
    @Column(nullable=false) private long clickCount;

    protected ShortUrl() {}
    public ShortUrl(String shortCode, String originalUrl) { this.shortCode=shortCode; this.originalUrl=originalUrl; this.createdAt=Instant.now(); }
    public Long getId(){return id;} public String getShortCode(){return shortCode;} public String getOriginalUrl(){return originalUrl;}
    public Instant getCreatedAt(){return createdAt;} public long getClickCount(){return clickCount;} public void incrementClicks(){clickCount++;}
}