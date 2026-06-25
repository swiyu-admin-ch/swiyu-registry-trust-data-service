/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */
package ch.admin.bj.swiyu.registry.trust.data.common.features;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Data
@Validated
@ConfigurationProperties(prefix = "features")
public final class FeaturesProperties {

    @NotNull
    private Boolean eidartfe754VcSchema;

    @NotNull
    private Boolean eidartfe1129NonCompliantActors;

    @PostConstruct
    public void logFeatureFlags() {
        log.info(
            """
            Following features are configured:
              EIDARTFE_754_VC_SCHEMA:{}
              EIDARTFE_1129_NON_COMPLIANT_ACTORS:{}
            """,
            eidartfe754VcSchema,
            eidartfe1129NonCompliantActors
        );
    }
}
