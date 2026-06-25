/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bj.swiyu.registry.trust.data.service;

import ch.admin.bj.swiyu.registry.trust.data.domain.TrustStatementV1Filter;
import ch.admin.bj.swiyu.registry.trust.data.domain.VcEntity;
import ch.admin.bj.swiyu.registry.trust.data.domain.VcEntityRepository;
import ch.admin.bj.swiyu.registry.trust.data.domain.datastore.DatastoreStatus;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class VcEntityService {

    private final VcEntityRepository vcEntityRepository;

    @Transactional(readOnly = true)
    public List<String> getIdentityTrustStatementsForDid(String did, Boolean filterActive) {
        var restrictedVct = List.of("TrustStatementIdentityV1");
        var filter = toFilter(filterActive, TrustStatementV1Filter.builder().did(did).vct(restrictedVct));
        return vcEntityRepository.search(filter.build()).stream().map(VcEntity::getRawVc).toList();
    }

    @Transactional(readOnly = true)
    public List<String> getTrustStatementsForIssuanceVcSchema(String vcSchemaIdentifier, Boolean activeOnly) {
        var filter = toFilter(activeOnly, TrustStatementV1Filter.builder().canIssue(vcSchemaIdentifier));
        return vcEntityRepository.search(filter.build()).stream().map(VcEntity::getRawVc).toList();
    }

    @Transactional(readOnly = true)
    public List<String> getTrustStatementsForVerificationVcSchema(String vcSchemaIdentifier, Boolean activeOnly) {
        var filter = toFilter(activeOnly, TrustStatementV1Filter.builder().canVerify(vcSchemaIdentifier));
        return vcEntityRepository.search(filter.build()).stream().map(VcEntity::getRawVc).toList();
    }

    private static TrustStatementV1Filter.TrustStatementV1FilterBuilder toFilter(
        Boolean activeOnly,
        TrustStatementV1Filter.TrustStatementV1FilterBuilder filter
    ) {
        if (Boolean.TRUE.equals(activeOnly)) {
            filter.maxNotBefore(Instant.now()).minExpiration(Instant.now()).status(DatastoreStatus.ACTIVE);
        }
        return filter;
    }
}
