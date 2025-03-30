package ododock.webserver.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.time.temporal.ChronoUnit;

@Component
@ConfigurationProperties(prefix = "app.jwt")
@Data
public class JwtProperties {

    private String issuer;
    private String tokenExpiryTimeUnit = ChronoUnit.MINUTES.name();
    private Long accessTokenExpiry;
    private Long refreshTokenExpiry;
    @Nullable
    private Long refreshTokenEarlyRenewalPeriod;

}
