/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bj.swiyu.registry.trust.data.common.exception;

public class InvalidPageException extends RuntimeException {

    public InvalidPageException(Throwable cause) {
        super("Page details are out of bounds.", cause);
    }
}
