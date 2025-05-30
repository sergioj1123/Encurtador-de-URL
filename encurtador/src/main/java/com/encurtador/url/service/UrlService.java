package com.encurtador.url.service;

import com.encurtador.url.model.Url;
import com.encurtador.url.repository.UrlRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;


    public String createShortUrl(String originalUrl) {
        Url url = new Url();
        if (alreadyHaveOneUrlReduced(originalUrl)) {
            Url existingUrl = urlRepository.findByOriginalUrl(originalUrl)
                    .orElseThrow(() -> new RuntimeException("URL not found"));
            if (isExpired(existingUrl)) {
                urlRepository.delete(existingUrl);
            } else {
                return existingUrl.getShortUrl();
            }
        }
        url.setOriginalUrl(originalUrl);
        url.setShortUrl(newRandomUrl(originalUrl));
        url.setExpirationDate(LocalDateTime.now().plusMinutes(10)); // Set expiration date
        urlRepository.save(url);
        return url.getShortUrl();
    }

    private boolean alreadyHaveOneUrlReduced(String originalUrl) {
        return urlRepository.findByOriginalUrl(originalUrl).isPresent();
    }

    public String newRandomUrl(String originalUrl) {
        return RandomStringUtils.randomAlphabetic(5, 10);
    }

    public String getOriginalUrl(String shortUrl) {

        Url url = urlRepository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new RuntimeException("URL not found"));
        if (isExpired(url)) {
            urlRepository.delete(url);
            throw new RuntimeException("URL has expired");
        }
        return url.getOriginalUrl();

    }

    private static boolean isExpired(Url url) {
        return url.getExpirationDate().isBefore(LocalDateTime.now());
    }
}
