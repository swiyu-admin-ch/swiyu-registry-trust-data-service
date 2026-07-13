/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bj.swiyu.registry.trust.data.api;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.HtmlUtils;

@Schema(name = "ApiError")
public record ApiErrorDto(HttpStatus status, String message) {
    public ApiErrorDto {
        message = message == null ? null : HtmlUtils.htmlEscape(message);
    }

    public ApiErrorDto(HttpStatus status) {
        this(status, status.getReasonPhrase());
    }
}
