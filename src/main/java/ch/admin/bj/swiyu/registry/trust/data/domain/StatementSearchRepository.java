/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bj.swiyu.registry.trust.data.domain;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StatementSearchRepository {
    Optional<String> findByFilter(TrustStatementV2Filter filter);

    Page<String> findAllByFilter(TrustStatementV2Filter filter, Pageable pageable);
}
