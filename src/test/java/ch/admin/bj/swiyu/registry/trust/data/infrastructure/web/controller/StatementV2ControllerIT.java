/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bj.swiyu.registry.trust.data.infrastructure.web.controller;

import static org.assertj.core.api.Assertions.assertThat;

import ch.admin.bj.swiyu.registry.trust.data.domain.Statement;
import ch.admin.bj.swiyu.registry.trust.data.domain.StatementRepository;
import ch.admin.bj.swiyu.registry.trust.data.domain.StatementType;
import ch.admin.bj.swiyu.registry.trust.data.test.PostgreSQLContainerInitializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestClient;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
@ContextConfiguration(initializers = PostgreSQLContainerInitializer.class)
class StatementV2ControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private StatementRepository publishedStatementRepository;

    private RestClient restClient;

    @Autowired
    private ObjectMapper mapper;

    private Statement createTestData(StatementType type) {
        return createTestData(type, 0);
    }

    private Statement createTestData(StatementType type, int timeModifier) {
        var id = UUID.randomUUID();
        var node = mapper.createObjectNode();
        node.put("typ", type.getJwtTyp());
        node.put("iat", Instant.now().minusSeconds(50 + timeModifier).getEpochSecond());
        node.put("exp", Instant.now().plusSeconds(50 + timeModifier).getEpochSecond());
        node.put("nbf", Instant.now().minusSeconds(0).getEpochSecond());
        node.put("jti", id.toString());
        node.put("sub", "did:test:" + id);

        return new Statement(id, false, true, type.toString() + id, node, type);
    }

    @BeforeEach
    void setUp() {
        publishedStatementRepository.deleteAllInBatch();
        restClient = RestClient.create("http://localhost:" + port);
    }

    @Test
    void getIdentityTrustStatementByIdentifier() {
        var idTs = publishedStatementRepository.save(createTestData(StatementType.IDENTITY_TRUST_STATEMENT_V2));

        var sub = idTs.getData().get("sub").asText();
        var uri = URI.create("/api/v2/identity-trust-statement/" + sub);

        var result = restClient.get().uri(uri).retrieve().toEntity(String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(idTs.getSerialized());
    }

    @Test
    void listIdentityTrustStatements() {
        var statement = publishedStatementRepository.save(createTestData(StatementType.IDENTITY_TRUST_STATEMENT_V2));
        publishedStatementRepository.save(createTestData(StatementType.IDENTITY_TRUST_STATEMENT_V2, 1));

        var uri = URI.create("/api/v2/identity-trust-statement/");

        var result = restClient.get().uri(uri).retrieve().toEntity(JsonNode.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        var json = result.getBody();
        assertThat(json).isNotNull();
        assertThat(json.get("content")).hasSize(2);
        assertThat(json.get("content").get(0).asText()).isEqualTo(statement.getSerialized());
    }

    @Test
    void getVerificationQueryPublicStatementByJti() {
        var statement = publishedStatementRepository.save(
            createTestData(StatementType.VERIFICATION_QUERY_PUBLIC_STATEMENT_V2)
        );
        var jti = UUID.fromString(statement.getData().get("jti").asText());
        var uri = URI.create("/api/v2/verification-query-public-statement/" + jti);

        var result = restClient.get().uri(uri).retrieve().toEntity(String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(statement.getSerialized());
    }

    @Test
    void listVerificationQueryPublicStatementByJti() {
        var statement = publishedStatementRepository.save(
            createTestData(StatementType.VERIFICATION_QUERY_PUBLIC_STATEMENT_V2)
        );
        publishedStatementRepository.save(createTestData(StatementType.VERIFICATION_QUERY_PUBLIC_STATEMENT_V2, 1));

        var uri = URI.create("/api/v2/verification-query-public-statement/");

        var result = restClient.get().uri(uri).retrieve().toEntity(JsonNode.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        var json = result.getBody();
        assertThat(json).isNotNull();
        assertThat(json.get("content")).hasSize(2);
        assertThat(json.get("content").get(0).asText()).isEqualTo(statement.getSerialized());
    }

    @Test
    void getProtectedIssuanceAuthorizationTrustStatementByJti() {
        var statement = publishedStatementRepository.save(
            createTestData(StatementType.PROTECTED_ISSUANCE_AUTHORIZATION_TRUST_STATEMENT_V2)
        );
        var jti = UUID.fromString(statement.getData().get("jti").asText());
        var uri = URI.create("/api/v2/protected-issuance-authorization-trust-statement/" + jti);

        var result = restClient.get().uri(uri).retrieve().toEntity(String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(statement.getSerialized());
    }

    @Test
    void listProtectedIssuanceAuthorizationTrustStatements() {
        var statement = publishedStatementRepository.save(
            createTestData(StatementType.PROTECTED_ISSUANCE_AUTHORIZATION_TRUST_STATEMENT_V2)
        );
        publishedStatementRepository.save(
            createTestData(StatementType.PROTECTED_ISSUANCE_AUTHORIZATION_TRUST_STATEMENT_V2, 1)
        );

        var uri = URI.create("/api/v2/protected-issuance-authorization-trust-statement/");

        var result = restClient.get().uri(uri).retrieve().toEntity(JsonNode.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        var json = result.getBody();
        assertThat(json).isNotNull();
        assertThat(json.get("content")).hasSize(2);
        assertThat(json.get("content").get(0).asText()).isEqualTo(statement.getSerialized());
    }

    @Test
    void getProtectedIssuanceTrustListStatementByJti() {
        var statement = publishedStatementRepository.save(
            createTestData(StatementType.PROTECTED_ISSUANCE_TRUST_LIST_STATEMENT_V2)
        );
        var jti = UUID.fromString(statement.getData().get("jti").asText());
        var uri = URI.create("/api/v2/protected-issuance-trust-list-statement/" + jti);

        var result = restClient.get().uri(uri).retrieve().toEntity(String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(statement.getSerialized());
    }

    @Test
    void listProtectedIssuanceTrustListStatements() {
        var statement = publishedStatementRepository.save(
            createTestData(StatementType.PROTECTED_ISSUANCE_TRUST_LIST_STATEMENT_V2)
        );
        publishedStatementRepository.save(createTestData(StatementType.PROTECTED_ISSUANCE_TRUST_LIST_STATEMENT_V2, 1));

        var uri = URI.create("/api/v2/protected-issuance-trust-list-statement/");

        var result = restClient.get().uri(uri).retrieve().toEntity(JsonNode.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        var json = result.getBody();
        assertThat(json).isNotNull();
        assertThat(json.get("content")).hasSize(2);
        assertThat(json.get("content").get(0).asText()).isEqualTo(statement.getSerialized());
    }

    @Test
    void getProtectedVerificationAuthorizationTrustStatementByJti() {
        var statement = publishedStatementRepository.save(
            createTestData(StatementType.PROTECTED_VERIFICATION_AUTHORIZATION_TRUST_STATEMENT_V2)
        );
        var jti = UUID.fromString(statement.getData().get("jti").asText());
        var uri = URI.create("/api/v2/protected-verification-authorization-trust-statement/" + jti);

        var result = restClient.get().uri(uri).retrieve().toEntity(String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(statement.getSerialized());
    }

    @Test
    void listProtectedVerificationAuthorizationTrustStatements() {
        var statement = publishedStatementRepository.save(
            createTestData(StatementType.PROTECTED_VERIFICATION_AUTHORIZATION_TRUST_STATEMENT_V2)
        );
        publishedStatementRepository.save(
            createTestData(StatementType.PROTECTED_VERIFICATION_AUTHORIZATION_TRUST_STATEMENT_V2, 1)
        );

        var uri = URI.create("/api/v2/protected-verification-authorization-trust-statement/");

        var result = restClient.get().uri(uri).retrieve().toEntity(JsonNode.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        var json = result.getBody();
        assertThat(json).isNotNull();
        assertThat(json.get("content")).hasSize(2);
        assertThat(json.get("content").get(0).asText()).isEqualTo(statement.getSerialized());
    }

    @Test
    void getActiveNonComplianceTrustListStatement() {
        var statement = publishedStatementRepository.save(
            createTestData(StatementType.NON_COMPLIANCE_TRUST_LIST_STATEMENT_V2)
        );
        var uri = URI.create("/api/v2/non-compliance-trust-list");

        var result = restClient.get().uri(uri).retrieve().toEntity(String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(statement.getSerialized());
    }
}
