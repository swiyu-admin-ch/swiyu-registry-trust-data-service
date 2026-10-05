/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bj.swiyu.registry.trust.data.infrastructure.web.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/devops/")
@AllArgsConstructor
public class DevOpsController {

    @GetMapping(value = "alert-test/{identifier}")
    public String getIdTS(@PathVariable(name = "identifier") String identifier) {
        throw new IllegalStateException("This is a test alert." + identifier);
    }
}
