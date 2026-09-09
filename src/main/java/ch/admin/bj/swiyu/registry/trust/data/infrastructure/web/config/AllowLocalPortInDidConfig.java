package ch.admin.bj.swiyu.registry.trust.data.infrastructure.web.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.firewall.StrictHttpFirewall;

/**
 * Localhost test DIDs contain the URL-encoded port value (e.g. localhost%3A8190)
 * and are URL-encoded again when sent as a path parameter, resulting
 * in encoded percent signs (%25). Spring Security's StrictHttpFirewall
 * rejects such requests by default as a protection against double-
 * encoding attacks. This relaxation is enabled only for the local
 * profile to support local development and testing.
 */
@Slf4j
@Profile("local")
@Configuration
public class AllowLocalPortInDidConfig {

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        log.error("Enable url encoded parameters. This should only happen in the loccal environment");
        var firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedPercent(true);
        return web -> web.httpFirewall(firewall);
    }
}
