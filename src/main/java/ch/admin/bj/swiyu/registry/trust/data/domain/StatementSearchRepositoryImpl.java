/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bj.swiyu.registry.trust.data.domain;

import ch.admin.bj.swiyu.registry.trust.data.common.exception.InvalidPageException;
import ch.admin.bj.swiyu.registry.trust.data.common.exception.InvalidSortException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.*;
import org.springframework.util.StringUtils;

public class StatementSearchRepositoryImpl implements StatementSearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Statement> findByFilter(TrustStatementV2Filter filter) {
        return this.findAllByFilter(filter, null).stream().findFirst();
    }

    public Page<Statement> findAllByFilter(TrustStatementV2Filter filter, Pageable pageable) {
        if (pageable == null) {
            pageable = PageRequest.of(0, 1, Sort.by("iat").descending());
        }

        var sql = new StringBuilder("SELECT * FROM {h-schema}statement WHERE 1=1");
        var countSql = new StringBuilder("SELECT COUNT(id) FROM {h-schema}statement WHERE 1=1");

        addWhere(filter, sql, countSql);
        addOrderBy(pageable, sql);

        var countQuery = entityManager.createNativeQuery(countSql.toString());
        var query = entityManager.createNativeQuery(sql.toString(), Statement.class);

        setPage(pageable, query);
        addParameters(filter, query, countQuery);

        // Fetch results
        @SuppressWarnings("unchecked")
        List<Statement> resultList = query.getResultList();
        var total = ((Number) countQuery.getSingleResult()).longValue();
        // Return a Page
        return new PageImpl<>(resultList, pageable, total);
    }

    private static void setPage(Pageable pageable, Query query) {
        var pageSize = Math.min(pageable.getPageSize(), 50);
        query.setMaxResults(pageSize);
        try {
            query.setFirstResult(Math.multiplyExact(pageable.getPageNumber(), pageSize));
        } catch (ArithmeticException e) {
            throw new InvalidPageException(e);
        }
    }

    private static void addParameters(TrustStatementV2Filter filter, Query query, Query countQuery) {
        query.setParameter("is_soft_deleted", false);
        countQuery.setParameter("is_soft_deleted", false);

        if (filter.getMaxNotBefore() != null) {
            query.setParameter("maxNotBefore", filter.getMaxNotBefore().getEpochSecond());
            countQuery.setParameter("maxNotBefore", filter.getMaxNotBefore().getEpochSecond());
        }
        if (filter.getMinExpiration() != null) {
            query.setParameter("minExpiration", filter.getMinExpiration().getEpochSecond());
            countQuery.setParameter("minExpiration", filter.getMinExpiration().getEpochSecond());
        }
        if (filter.getActiveInStatuslist() != null) {
            query.setParameter("is_active_in_statuslist", filter.getActiveInStatuslist());
            countQuery.setParameter("is_active_in_statuslist", filter.getActiveInStatuslist());
        }
        if (StringUtils.hasText(filter.getSub())) {
            query.setParameter("sub", filter.getSub());
            countQuery.setParameter("sub", filter.getSub());
        }
        if (filter.getJti() != null) {
            query.setParameter("jti", filter.getJti());
            countQuery.setParameter("jti", filter.getJti());
        }
        if (StringUtils.hasText(filter.getTyp())) {
            query.setParameter("typ", filter.getTyp());
            countQuery.setParameter("typ", filter.getTyp());
        }
    }

    private static void addWhere(TrustStatementV2Filter filter, StringBuilder sql, StringBuilder countSql) {
        sql.append(" AND is_soft_deleted = :is_soft_deleted");
        countSql.append(" AND is_soft_deleted = :is_soft_deleted");

        if (filter.getMaxNotBefore() != null) {
            sql.append(" AND (data->>'nbf')::bigint <= :maxNotBefore");
            countSql.append(" AND (data->>'nbf')::bigint <= :maxNotBefore");
        }
        if (filter.getMinExpiration() != null) {
            sql.append(" AND (data->>'exp')::bigint >= :minExpiration");
            countSql.append(" AND (data->>'exp')::bigint >= :minExpiration");
        }
        if (StringUtils.hasText(filter.getSub())) {
            sql.append(" AND (data->>'sub') = :sub");
            countSql.append(" AND (data->>'sub') = :sub");
        }
        if (filter.getJti() != null) {
            sql.append(" AND (data->>'jti')::uuid = :jti");
            countSql.append(" AND (data->>'jti')::uuid = :jti");
        }
        if (StringUtils.hasText(filter.getTyp())) {
            sql.append(" AND (data->>'typ') = :typ");
            countSql.append(" AND (data->>'typ') = :typ");
        }
        if (filter.getActiveInStatuslist() != null) {
            sql.append(" AND is_active_in_statuslist = :is_active_in_statuslist");
            countSql.append(" AND is_active_in_statuslist = :is_active_in_statuslist");
        }
    }

    private static String sanitizeOrderProperty(String property) {
        return switch (property) {
            case "typ", "alg", "kid", "profile_version" -> "(data->>'" + property + "')";
            case "iat", "exp", "nbf" -> "(data->>'" + property + "')::bigint";
            default -> throw new InvalidSortException(
                property,
                List.of("typ", "alg", "kid", "profile_version", "iat", "exp", "nbf")
            );
        };
    }

    private void addOrderBy(Pageable pageable, StringBuilder sql) {
        if (pageable.getSort().isSorted()) {
            String orderBy = pageable
                .getSort()
                .stream()
                .map(order -> {
                    // Prevent SQL injection – sanitize allowed columns
                    String sanitizedProperty = sanitizeOrderProperty(order.getProperty());
                    return sanitizedProperty + " " + order.getDirection().name();
                })
                .reduce((a, b) -> a + " , " + b)
                .orElse("");

            if (!orderBy.isBlank()) {
                sql.append(" ORDER BY ").append(orderBy);
            }
        }
    }
}
