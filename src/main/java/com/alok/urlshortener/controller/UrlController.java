package com.alok.urlshortener.controller;

import com.alok.urlshortener.model.ShortUrl;
import com.alok.urlshortener.service.ShortUrlService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import java.util.List;
import java.util.Map;

@RestController
public class UrlController {
    private final ShortUrlService service;
    public UrlController(ShortUrlService service){this.service=service;}
    public record CreateRequest(@NotBlank @Pattern(regexp="https?://.+", message="URL must start with http:// or https://") String url) {}
    public record CreateResponse(String shortCode,String shortUrl,String originalUrl) {}
    @PostMapping("/api/urls") public ResponseEntity<CreateResponse> create(@Valid @RequestBody CreateRequest req){
        ShortUrl u=service.create(req.url());
        return ResponseEntity.ok(new CreateResponse(u.getShortCode(),"/"+u.getShortCode(),u.getOriginalUrl()));
    }
    @GetMapping("/api/urls/recent") public List<ShortUrl> recent(){return service.recent();}
    @GetMapping("/api/urls/{code}/analytics") public Map<String,Object> analytics(@PathVariable String code){return service.analytics(code);}
    @GetMapping("/{code}") public RedirectView redirect(@PathVariable String code){
        RedirectView v=new RedirectView(service.resolve(code)); v.setStatusCode(org.springframework.http.HttpStatus.FOUND); return v;
    }
}