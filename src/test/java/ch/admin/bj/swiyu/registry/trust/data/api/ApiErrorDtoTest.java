/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bj.swiyu.registry.trust.data.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ApiErrorDtoTest {

    @Test
    void escapesHtmlInErrorMessages() {
        var error = new ApiErrorDto(HttpStatus.BAD_REQUEST, "<script>alert(1)</script>");

        assertThat(error.message()).isEqualTo("&lt;script&gt;alert(1)&lt;/script&gt;");
    }
}
