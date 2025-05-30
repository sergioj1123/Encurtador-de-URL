package com.encurtador.url.controller;


import com.encurtador.url.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/url")
public class UrlController {

    @Autowired
    private UrlService urlService;

    @PostMapping("/shorten")
    @Operation(summary = "Encurta uma URL original e retorna a URL encurtada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "shortUrl : http://localhost:8080/url/{shortUrl}"),
            @ApiResponse(responseCode = "400", description = "Erro ao encurtar a URL, verifique os dados informados"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado ao encurtar a URL")
    })
    public ResponseEntity<Map<String, String>> shortenUrl(@RequestBody Map<String, String> request) {
        String originalUrl = request.get("originalUrl");
        if (originalUrl == null || originalUrl.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Original URL is required"));
        }
        try {
            String shortUrl = urlService.createShortUrl(originalUrl);
            return ResponseEntity.ok(Map.of("shortUrl", "http://localhost:8080/url/" + shortUrl));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Erro inesperado: " + e.getMessage()));
        }
    }

    @GetMapping("/{shortUrl}")
    @Operation(summary = "Redireciona para a URL original a partir da URL encurtada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Redireciona para a URL original"),
            @ApiResponse(responseCode = "400", description = "Erro ao redirecionar, verifique a URL encurtada informada"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado ao redirecionar")
    })
    public ResponseEntity<Map<String, String>> redirectToOriginalUrl(@PathVariable String shortUrl) {
        try {
            String originalUrl = urlService.getOriginalUrl(shortUrl);
            if (originalUrl == null) {
                throw new RuntimeException("URL não encontrada");
            }
            return ResponseEntity.status(302).location(java.net.URI.create(originalUrl)).build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Erro inesperado: " + e.getMessage()));
        }
    }


}
