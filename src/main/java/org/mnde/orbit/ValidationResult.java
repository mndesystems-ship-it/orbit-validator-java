package org.mnde.orbit;

/**
 * Immutable validation result for Orbit messages.
 *
 * Intentionally minimal: valid/invalid plus a single human-readable error string.
 * No partial parsing, no auto-corrections, no execution hints.
 */
public final class ValidationResult {

    private static final ValidationResult OK = new ValidationResult(true, null);

    private final boolean valid;
    private final String error;

    private ValidationResult(boolean valid, String error) {
        this.valid = valid;
        this.error = error;
    }

    public static ValidationResult ok() {
        return OK;
    }

    public static ValidationResult invalid(String error) {
        if (error == null || error.isBlank()) {
            return new ValidationResult(false, "Invalid Orbit message");
        }
        return new ValidationResult(false, error);
    }

    public boolean isValid() {
        return valid;
    }

    /**
     * @return null when valid; otherwise a single human-readable reason.
     */
    public String getError() {
        return error;
    }
}
