/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bj.swiyu.registry.trust.data.infrastructure.web.controller;

import ch.admin.bj.swiyu.registry.trust.data.service.StatementService;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/")
@AllArgsConstructor
@Tag(name = "Statements 2.0", description = "Returns statements of the Trust Protocol 2.0 from the datastore.")
public class StatementV2Controller {

    private final StatementService statementService;

    @Timed
    @GetMapping(value = "identity-trust-statement/{identifier}")
    @Operation(
        summary = "IF-007.007 - Get the Identity Trust Statement (idTS) for a given identifier.",
        description = "Get the Identity Trust Statement (idTS) for a given identifier."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Returns an encoded trust statement.",
                useReturnTypeSchema = true
            ),
        }
    )
    public String getIdTS(
        @PathVariable(name = "identifier") String identifier // swagger is handling the path encoding
    ) {
        return this.statementService.getIdentityV2TrustStatementForIdentifier(identifier);
    }

    @Timed
    @GetMapping(value = "identity-trust-statement/")
    @Operation(
        summary = "IF-007.008 - List the Identity Trust Statement (idTS).",
        description = "List the Identity Trust Statement (idTS)."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Returns a list of encoded trust statements",
                useReturnTypeSchema = true
            ),
        }
    )
    @PageableAsQueryParam
    public PagedModel<String> listIdTS(
        @RequestParam(name = "sub", required = false) String sub,
        @RequestParam(name = "filterActive", required = false, defaultValue = "true") Boolean filterActive,
        @SortDefault(sort = "iat", direction = Sort.Direction.DESC) @Parameter(hidden = true) final Pageable pageable
    ) {
        return new PagedModel<>(this.statementService.getIdentityV2TrustStatements(sub, filterActive, pageable));
    }

    @Timed
    @GetMapping(value = "verification-query-public-statement/{jti}")
    @Operation(
        summary = "IF-007.009 - Get the Verification Query Public Statement (vqPS) for a given identifier.",
        description = "Get the Verification Query Public Statement (vqPS) for a given identifier."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Returns an encoded trust statement.",
                useReturnTypeSchema = true
            ),
        }
    )
    public String getVqPS(
        @PathVariable(name = "jti") UUID jti // swagger is handling the path encoding
    ) {
        return this.statementService.getVerificationQueryV2PublicStatementForJti(jti);
    }

    @Timed
    @GetMapping(value = "verification-query-public-statement/")
    @Operation(
        summary = "IF-007.010 - List the Verification Query Public Statement (vqPS).",
        description = "List the Verification Query Public Statement (vqPS)."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Returns a list of encoded trust statements",
                useReturnTypeSchema = true
            ),
        }
    )
    @PageableAsQueryParam
    public PagedModel<String> listVqPS(
        @RequestParam(name = "sub", required = false) String sub,
        @RequestParam(name = "filterActive", required = false, defaultValue = "true") Boolean filterActive,
        @SortDefault(sort = "iat", direction = Sort.Direction.DESC) @Parameter(hidden = true) final Pageable pageable
    ) {
        return new PagedModel<>(
            this.statementService.getVerificationQueryV2PublicStatements(sub, filterActive, pageable)
        );
    }

    @Timed
    @GetMapping(value = "protected-verification-authorization-trust-statement/{jti}")
    @Operation(
        summary = "IF-007.011 - Get the Protected Verification Authorization Trust Statement (pvaTS) for a given identifier.",
        description = "Get the Protected Verification Authorization Trust Statement (pvaTS) for a given identifier."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Returns an encoded trust statement.",
                useReturnTypeSchema = true
            ),
        }
    )
    public String getPvaTS(
        @PathVariable(name = "jti") UUID jti // swagger is handling the path encoding
    ) {
        return this.statementService.getProtectedVerificationAuthorizationTrustStatementForJti(jti);
    }

    @Timed
    @GetMapping(value = "protected-verification-authorization-trust-statement/")
    @Operation(
        summary = "IF-007.012 - List the Protected Verification Authorization Trust Statement (pvaTS).",
        description = "List the Protected Verification Authorization Trust Statement (pvaTS)."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Returns a list of encoded trust statements",
                useReturnTypeSchema = true
            ),
        }
    )
    @PageableAsQueryParam
    public PagedModel<String> listPvaTS(
        @RequestParam(name = "sub", required = false) String sub,
        @RequestParam(name = "filterActive", required = false, defaultValue = "true") Boolean filterActive,
        @SortDefault(sort = "iat", direction = Sort.Direction.DESC) @Parameter(hidden = true) final Pageable pageable
    ) {
        return new PagedModel<>(
            this.statementService.getProtectedVerificationAuthorizationTrustStatements(sub, filterActive, pageable)
        );
    }

    @Timed
    @GetMapping(value = "protected-issuance-authorization-trust-statement/{jti}")
    @Operation(
        summary = "IF-007.013 - Get the Protected Issuance Authorization Trust Statement (piaTS) for a given identifier.",
        description = "Get the Protected Issuance Authorization Trust Statement (piaTS) for a given identifier."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Returns an encoded trust statement.",
                useReturnTypeSchema = true
            ),
        }
    )
    public String getPiaTS(
        @PathVariable(name = "jti") UUID jti // swagger is handling the path encoding
    ) {
        return this.statementService.getProtectedIssuanceAuthorizationTrustStatementForJti(jti);
    }

    @Timed
    @GetMapping(value = "protected-issuance-authorization-trust-statement/")
    @Operation(
        summary = "IF-007.014 - List the Protected Issuance Authorization Trust Statement (piaTS).",
        description = "List the Protected Issuance Authorization Trust Statement (piaTS)."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Returns a list of encoded trust statements",
                useReturnTypeSchema = true
            ),
        }
    )
    @PageableAsQueryParam
    public PagedModel<String> listPiaTS(
        @RequestParam(name = "sub", required = false) String sub,
        @RequestParam(name = "filterActive", required = false, defaultValue = "true") Boolean filterActive,
        @SortDefault(sort = "iat", direction = Sort.Direction.DESC) @Parameter(hidden = true) final Pageable pageable
    ) {
        return new PagedModel<>(
            this.statementService.getProtectedIssuanceAuthorizationTrustStatements(sub, filterActive, pageable)
        );
    }

    @Timed
    @GetMapping(value = "protected-issuance-trust-list-statement/{jti}")
    @Operation(
        summary = "IF-007.015 - Get the Protected Issuance Trust List Statement (piTLS) for a given identifier.",
        description = "Get the Protected Issuance Trust List Statement (piTLS) for a given identifier."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Returns an encoded trust statement.",
                useReturnTypeSchema = true
            ),
        }
    )
    public String getPiTLS(
        @PathVariable(name = "jti") UUID jti // swagger is handling the path encoding
    ) {
        return this.statementService.getProtectedIssuanceTrustListStatementForJti(jti);
    }

    @Timed
    @GetMapping(value = "protected-issuance-trust-list-statement/")
    @Operation(
        summary = "IF-007.016 - List the Protected Issuance Trust List Statement (piTLS).",
        description = "List the Protected Issuance Trust List Statement (piTLS)."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Returns a list of encoded trust statements",
                useReturnTypeSchema = true
            ),
        }
    )
    @PageableAsQueryParam
    public PagedModel<String> listPiTLS(
        @RequestParam(name = "sub", required = false) String sub,
        @RequestParam(name = "filterActive", required = false, defaultValue = "true") Boolean filterActive,
        @SortDefault(sort = "iat", direction = Sort.Direction.DESC) @Parameter(hidden = true) final Pageable pageable
    ) {
        return new PagedModel<>(
            this.statementService.getProtectedIssuanceTrustListStatements(sub, filterActive, pageable)
        );
    }

    @Timed
    @GetMapping(value = "protected-issuance-trust-list")
    @Operation(
        summary = "IF-007.018 - Get the currently active Protected Issuance Trust List Statement (piTLS) for the ecosystem.",
        description = "Get the currently active Protected Issuance Trust List Statement (piTLS) for the ecosystem."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Returns an encoded trust statement.",
                useReturnTypeSchema = true
            ),
        }
    )
    public String getActivePiTLS() {
        return this.statementService.getActiveProtectedIssuanceTrustListStatement();
    }

    @Timed
    @GetMapping(value = "non-compliance-trust-list")
    @Operation(
        summary = "IF-007.017 - Get the currently active Non-Compliance Trust List Statement (ncTLS) for the ecosystem.",
        description = "Get the currently active Non-Compliance Trust List Statement (ncTLS) for the ecosystem."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Returns an encoded trust statement.",
                useReturnTypeSchema = true
            ),
        }
    )
    public String getActiveNcTLS() {
        return this.statementService.getActiveNonComplianceTrustListStatement();
    }
}
