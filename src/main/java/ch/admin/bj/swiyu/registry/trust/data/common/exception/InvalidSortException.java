/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bj.swiyu.registry.trust.data.common.exception;

import jakarta.validation.constraints.NotEmpty;
import java.text.MessageFormat;
import java.util.List;

public class InvalidSortException extends RuntimeException {

    private static final MessageFormat ERR_MESSAGE = new MessageFormat(
        "Resource cannot be sorted by ''{0}''. Allowed sorts are: {1}"
    );

    public InvalidSortException(String offendingSort, @NotEmpty List<String> allowedSorts) {
        super(
            ERR_MESSAGE.format(
                new String[] {
                    offendingSort,
                    allowedSorts.stream().map(a -> "'" + a + "'").reduce((a, b) -> a + ", " + b).orElse(""),
                }
            )
        );
    }
}
