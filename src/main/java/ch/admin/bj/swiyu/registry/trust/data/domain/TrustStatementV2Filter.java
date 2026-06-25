/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */
package ch.admin.bj.swiyu.registry.trust.data.domain;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TrustStatementV2Filter {

    private String typ;
    private UUID jti;
    private String sub;
    private Instant maxNotBefore;
    private Instant minExpiration;
    private Boolean activeInStatuslist;

    public static TrustStatementV2Filter.TrustStatementV2FilterBuilder builderWithDefaults(boolean filterActive) {
        var builder = builder(); // NOSONAR False positive:Remove this useless assignment to local variable
        if (filterActive) {
            var now = Instant.now(); // NOSONAR False positive:Remove this useless assignment to local variable
            builder.maxNotBefore(now);
            builder.minExpiration(now);
            builder.activeInStatuslist(true);
        }
        return builder;
    }
}
