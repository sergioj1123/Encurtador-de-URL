package com.encurtador.url.repository;

import com.encurtador.url.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByShortUrl(String shortUrl);

    Optional<Url> findByOriginalUrl(String originalUrl);
}
