package com.ncookie.imad.domain.tmdb.openfeign;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "tmdb.api")
public class TmdbApiProperties {
    private String apiKey;
    private String apiUrl;
}
