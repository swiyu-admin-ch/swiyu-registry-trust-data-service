package ch.admin.bj.swiyu.registry.trust.data.infrastructure.web.controller;

import ch.admin.bj.swiyu.registry.trust.data.api.NonCompliantActorsDto;
import ch.admin.bj.swiyu.registry.trust.data.service.NonComplianceListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ConditionalOnProperty(prefix = "features", value = "EIDARTFE_1129_NON_COMPLIANT_ACTORS", havingValue = "true")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/non-compliant-actors")
@Tag(name = "NonCompliantActor", description = "Returns most recent list of non compliant actors.")
public class NonCompliantActorController {

    private final NonComplianceListService nonComplianceListService;

    @GetMapping
    @Operation(
        summary = "IF-007.006 - Get registered non-compliant actors.",
        description = "Get the DIDs of all non-compliant actors and their non-compliant reason."
    )
    public NonCompliantActorsDto getNonCompliantActors() {
        return this.nonComplianceListService.getNonCompliantActors();
    }
}
