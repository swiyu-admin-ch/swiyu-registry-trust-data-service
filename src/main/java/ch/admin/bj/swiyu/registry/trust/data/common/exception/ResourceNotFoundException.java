/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bj.swiyu.registry.trust.data.common.exception;

public class ResourceNotFoundException extends RuntimeException {

    private static final String DEFAULT_MSG_ID_AND_TYPE = "Resource with id '%s' of type '%s' not found";
    private static final String DEFAULT_MSG_TYPE = "Resource of type '%s' not found";

    public ResourceNotFoundException(String id, String type) {
        super(DEFAULT_MSG_ID_AND_TYPE.formatted(id, type));
    }

    public ResourceNotFoundException(String type) {
        super(DEFAULT_MSG_TYPE.formatted(type));
    }
}
