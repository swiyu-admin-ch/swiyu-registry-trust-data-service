/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bj.swiyu.registry.trust.data.service;

import ch.admin.bj.swiyu.registry.trust.data.common.exception.ResourceNotFoundException;
import ch.admin.bj.swiyu.registry.trust.data.domain.Statement;
import ch.admin.bj.swiyu.registry.trust.data.domain.StatementRepository;
import ch.admin.bj.swiyu.registry.trust.data.domain.StatementType;
import ch.admin.bj.swiyu.registry.trust.data.domain.TrustStatementV2Filter;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class StatementService {

    private final StatementRepository statementRepository;

    @Transactional(readOnly = true)
    public String getIdentityV2TrustStatementForIdentifier(String sub) {
        var filter = TrustStatementV2Filter.builderWithDefaults(true)
            .sub(sub)
            .typ(StatementType.IDENTITY_TRUST_STATEMENT_V2.getJwtTyp());

        return statementRepository
            .findByFilter(filter.build())
            .orElseThrow(() -> new ResourceNotFoundException(sub, "idTS"))
            .getSerialized();
    }

    @Transactional(readOnly = true)
    public Page<String> getIdentityV2TrustStatements(String sub, Boolean filterActive, Pageable pageable) {
        var filter = TrustStatementV2Filter.builderWithDefaults(filterActive)
            .sub(sub)
            .typ(StatementType.IDENTITY_TRUST_STATEMENT_V2.getJwtTyp());

        return statementRepository.findAllByFilter(filter.build(), pageable).map(Statement::getSerialized);
    }

    @Transactional(readOnly = true)
    public String getVerificationQueryV2PublicStatementForJti(UUID jti) {
        var filter = TrustStatementV2Filter.builderWithDefaults(true)
            .jti(jti)
            .typ(StatementType.VERIFICATION_QUERY_PUBLIC_STATEMENT_V2.getJwtTyp());

        return statementRepository
            .findByFilter(filter.build())
            .orElseThrow(() -> new ResourceNotFoundException(jti.toString(), "vqPS"))
            .getSerialized();
    }

    @Transactional(readOnly = true)
    public Page<String> getVerificationQueryV2PublicStatements(String sub, Boolean filterActive, Pageable pageable) {
        var filter = TrustStatementV2Filter.builderWithDefaults(filterActive)
            .sub(sub)
            .typ(StatementType.VERIFICATION_QUERY_PUBLIC_STATEMENT_V2.getJwtTyp());

        return statementRepository.findAllByFilter(filter.build(), pageable).map(Statement::getSerialized);
    }

    @Transactional(readOnly = true)
    public String getProtectedVerificationAuthorizationTrustStatementForJti(UUID jti) {
        var filter = TrustStatementV2Filter.builderWithDefaults(true)
            .jti(jti)
            .typ(StatementType.PROTECTED_VERIFICATION_AUTHORIZATION_TRUST_STATEMENT_V2.getJwtTyp());

        return statementRepository
            .findByFilter(filter.build())
            .orElseThrow(() -> new ResourceNotFoundException(jti.toString(), "pvaTS"))
            .getSerialized();
    }

    @Transactional(readOnly = true)
    public Page<String> getProtectedVerificationAuthorizationTrustStatements(
        String sub,
        Boolean filterActive,
        Pageable pageable
    ) {
        var filter = TrustStatementV2Filter.builderWithDefaults(filterActive)
            .sub(sub)
            .typ(StatementType.PROTECTED_VERIFICATION_AUTHORIZATION_TRUST_STATEMENT_V2.getJwtTyp());

        return statementRepository.findAllByFilter(filter.build(), pageable).map(Statement::getSerialized);
    }

    @Transactional(readOnly = true)
    public String getProtectedIssuanceAuthorizationTrustStatementForJti(UUID jti) {
        var filter = TrustStatementV2Filter.builderWithDefaults(true)
            .jti(jti)
            .typ(StatementType.PROTECTED_ISSUANCE_AUTHORIZATION_TRUST_STATEMENT_V2.getJwtTyp());

        return statementRepository
            .findByFilter(filter.build())
            .orElseThrow(() -> new ResourceNotFoundException(jti.toString(), "piaTS"))
            .getSerialized();
    }

    @Transactional(readOnly = true)
    public Page<String> getProtectedIssuanceAuthorizationTrustStatements(
        String sub,
        Boolean filterActive,
        Pageable pageable
    ) {
        var filter = TrustStatementV2Filter.builderWithDefaults(filterActive)
            .sub(sub)
            .typ(StatementType.PROTECTED_ISSUANCE_AUTHORIZATION_TRUST_STATEMENT_V2.getJwtTyp());

        return statementRepository.findAllByFilter(filter.build(), pageable).map(Statement::getSerialized);
    }

    @Transactional(readOnly = true)
    public String getProtectedIssuanceTrustListStatementForJti(UUID jti) {
        var filter = TrustStatementV2Filter.builderWithDefaults(true)
            .jti(jti)
            .typ(StatementType.PROTECTED_ISSUANCE_TRUST_LIST_STATEMENT_V2.getJwtTyp());
        return statementRepository
            .findByFilter(filter.build())
            .orElseThrow(() -> new ResourceNotFoundException(jti.toString(), "piTLS"))
            .getSerialized();
    }

    @Transactional(readOnly = true)
    public Page<String> getProtectedIssuanceTrustListStatements(String sub, Boolean filterActive, Pageable pageable) {
        var filter = TrustStatementV2Filter.builderWithDefaults(filterActive)
            .sub(sub)
            .typ(StatementType.PROTECTED_ISSUANCE_TRUST_LIST_STATEMENT_V2.getJwtTyp());

        return statementRepository.findAllByFilter(filter.build(), pageable).map(Statement::getSerialized);
    }

    @Transactional(readOnly = true)
    public String getActiveProtectedIssuanceTrustListStatement() {
        var filter = TrustStatementV2Filter.builderWithDefaults(true).typ(
            StatementType.PROTECTED_ISSUANCE_TRUST_LIST_STATEMENT_V2.getJwtTyp()
        );

        return statementRepository
            .findByFilter(filter.build())
            .orElseThrow(() -> new ResourceNotFoundException("piTLS"))
            .getSerialized();
    }

    @Transactional(readOnly = true)
    public String getActiveNonComplianceTrustListStatement() {
        var filter = TrustStatementV2Filter.builderWithDefaults(true).typ(
            StatementType.NON_COMPLIANCE_TRUST_LIST_STATEMENT_V2.getJwtTyp()
        );

        return statementRepository
            .findByFilter(filter.build())
            .orElseThrow(() -> new ResourceNotFoundException("ncTLS"))
            .getSerialized();
    }
}
