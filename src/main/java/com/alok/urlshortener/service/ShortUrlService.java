package com.alok.urlshortener.service;

import com.alok.urlshortener.model.ShortUrl;
import com.alok.urlshortener.repository.ShortUrlRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ShortUrlService {
    private static final String ALPHABET="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final SecureRandom random=new SecureRandom();
    private final ShortUrlRepository repository;
    private final StringRedisTemplate redis;
    public ShortUrlService(ShortUrlRepository repository,StringRedisTemplate redis){this.repository=repository;this.redis=redis;}

    @Transactional
    public ShortUrl create(String originalUrl){
        String code;
        do { code=randomCode(7); } while(repository.existsByShortCode(code));
        ShortUrl saved=repository.save(new ShortUrl(code,originalUrl));
        redis.opsForValue().set("url:"+code, originalUrl, 24, TimeUnit.HOURS);
        return saved;
    }

    @Transactional
    public String resolve(String code){
        String cached=redis.opsForValue().get("url:"+code);
        if(cached!=null){ incrementClick(code); return cached; }
        ShortUrl url=repository.findByShortCode(code).orElseThrow(()->new IllegalArgumentException("Short URL not found"));
        redis.opsForValue().set("url:"+code,url.getOriginalUrl(),24,TimeUnit.HOURS);
        url.incrementClicks(); repository.save(url);
        return url.getOriginalUrl();
    }

    private void incrementClick(String code){ repository.findByShortCode(code).ifPresent(u->{u.incrementClicks(); repository.save(u);}); }
    public List<ShortUrl> recent(){ return repository.findTop10ByOrderByCreatedAtDesc(); }
    public Map<String,Object> analytics(String code){
        ShortUrl u=repository.findByShortCode(code).orElseThrow(()->new IllegalArgumentException("Short URL not found"));
        return Map.of("shortCode",u.getShortCode(),"originalUrl",u.getOriginalUrl(),"clicks",u.getClickCount(),"createdAt",u.getCreatedAt());
    }
    private String randomCode(int n){StringBuilder b=new StringBuilder(n);for(int i=0;i<n;i++)b.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));return b.toString();}
}