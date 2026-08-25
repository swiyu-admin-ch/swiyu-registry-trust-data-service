/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */
package ch.admin.bj.swiyu.registry.trust.data.service;

import static ch.admin.bj.swiyu.registry.trust.data.domain.datastore.DatastoreStatus.*;
import static ch.admin.bj.swiyu.registry.trust.data.test.VcEntityTestData.datastoreEntity;
import static ch.admin.bj.swiyu.registry.trust.data.test.VcEntityTestData.vcIdentityV1;
import static ch.admin.bj.swiyu.registry.trust.data.test.VcEntityTestData.vcIssuanceV1;
import static ch.admin.bj.swiyu.registry.trust.data.test.VcEntityTestData.vcVerificationV1;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import ch.admin.bj.swiyu.registry.trust.data.domain.*;
import ch.admin.bj.swiyu.registry.trust.data.domain.datastore.*;
import ch.admin.bj.swiyu.registry.trust.data.test.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.data.jpa.test.autoconfigure.*;
import org.springframework.boot.jdbc.test.autoconfigure.*;
import org.springframework.boot.webmvc.test.autoconfigure.*;
import org.springframework.context.annotation.*;
import org.springframework.test.context.*;
import org.springframework.test.context.transaction.*;
import org.testcontainers.junit.jupiter.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@Testcontainers
@DataJpaTest
@Import({ VcEntityService.class })
@ContextConfiguration(initializers = PostgreSQLContainerInitializer.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VcEntityServiceIT {

    @Autowired
    private VcEntityRepository vcEntityRepository;

    @Autowired
    private DataStoreEntityRepository dataStoreEntityRepository;

    @Autowired
    private VcEntityService vcEntityService;

    private static void commit() {
        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    @BeforeEach
    void setUp() {
        vcEntityRepository.deleteAllInBatch();
        dataStoreEntityRepository.deleteAllInBatch();
    }

    @Test
    void getTrustStatementsForIssuanceVcSchema() throws Exception {
        // GIVEN (a couple of different trust statements)
        var datastore1 = dataStoreEntityRepository.save(datastoreEntity(ACTIVE)).getId();
        var datastore2 = dataStoreEntityRepository.save(datastoreEntity(ACTIVE)).getId();
        var datastore3 = dataStoreEntityRepository.save(datastoreEntity(ACTIVE)).getId();
        vcEntityRepository.save(vcIdentityV1(datastore1, "did1"));
        vcEntityRepository.save(vcVerificationV1(datastore3, "did1", "https://schema/dummy-example"));
        var vc = vcEntityRepository.save(vcIssuanceV1(datastore2, "did1", "https://schema/dummy-example"));
        commit();
        // WHEN (querying only for specific issuance statements)
        var result = vcEntityService.getTrustStatementsForIssuanceVcSchema("https://schema/dummy-example", true);
        // THEN
        assertEquals(1, result.size());
        assertThat(result.getFirst()).isEqualTo(vc.getRawVc());
    }

    @Test
    void getTrustStatementsForVerificationVcSchema() throws Exception {
        // GIVEN (a couple of different trust statements)
        var datastore1 = dataStoreEntityRepository.save(datastoreEntity(ACTIVE)).getId();
        var datastore2 = dataStoreEntityRepository.save(datastoreEntity(ACTIVE)).getId();
        var datastore3 = dataStoreEntityRepository.save(datastoreEntity(ACTIVE)).getId();
        vcEntityRepository.save(vcIdentityV1(datastore1, "did1"));
        vcEntityRepository.save(vcIssuanceV1(datastore2, "did1", "https://schema/dummy-example"));
        var vc = vcEntityRepository.save(vcVerificationV1(datastore3, "did1", "https://schema/dummy-example"));
        commit();
        // WHEN (querying only for specific verification statements)
        var result = vcEntityService.getTrustStatementsForVerificationVcSchema("https://schema/dummy-example", true);
        // THEN
        assertEquals(1, result.size());
        assertThat(result.getFirst()).isEqualTo(vc.getRawVc());
    }
}
