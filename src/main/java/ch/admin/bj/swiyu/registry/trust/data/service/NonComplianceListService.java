package ch.admin.bj.swiyu.registry.trust.data.service;

import ch.admin.bj.swiyu.registry.trust.data.api.NonCompliantActorsDto;
import ch.admin.bj.swiyu.registry.trust.data.domain.NonComplianceListRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Service
@AllArgsConstructor
public class NonComplianceListService {

    private final NonComplianceListRepository nonComplianceListRepository;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public NonCompliantActorsDto getNonCompliantActors() {
        return nonComplianceListRepository
            .findTopByOrderByPublishedAtDesc()
            .map(nonComplianceList -> {
                try {
                    var json = nonComplianceList.getPayload();
                    log.debug("parsing non-compliance-list json: {}", json);
                    return objectMapper.readValue(json, NonCompliantActorsDto.class);
                } catch (JacksonException e) {
                    throw new IllegalStateException("Error while trying to deserialize NonComplianceList payload", e);
                }
            })
            .orElseGet(() -> new NonCompliantActorsDto(List.of()));
    }
}
