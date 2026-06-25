/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */
package ch.admin.bj.swiyu.registry.trust.data.service;

import ch.admin.bj.swiyu.registry.trust.data.common.exception.ResourceDeactivatedException;
import ch.admin.bj.swiyu.registry.trust.data.common.exception.ResourceNotFoundException;
import ch.admin.bj.swiyu.registry.trust.data.domain.VcSchemaEntityRepository;
import ch.admin.bj.swiyu.registry.trust.data.domain.VcSchemaStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class VcSchemaService {

    private final VcSchemaEntityRepository vcSchemaEntityRepository;

    public String getVcSchema(String vcSchemaPath) throws ResourceNotFoundException, ResourceDeactivatedException {
        var vcSchema = vcSchemaEntityRepository
            .findByPath(vcSchemaPath)
            .orElseThrow(() -> new ResourceNotFoundException(vcSchemaPath, "VC_SCHEMA"));
        if (vcSchema.getStatus().compareTo(VcSchemaStatus.DEACTIVATED) == 0) {
            throw new ResourceDeactivatedException(vcSchemaPath, "VC_SCHEMA");
        }

        return vcSchema.getFile();
    }
}
