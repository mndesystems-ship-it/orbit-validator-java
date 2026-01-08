package org.mnde.orbit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Set;

/**
 * Orbit Protocol v1.0 Reference Validator.
 *
 * Scope: validation only. No execution. No policy.
 *
 * Orbit v1.0 is frozen and supports exactly one action: "open".
 */
public final class OrbitValidator {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Set<String> ROOT_FIELDS =
            Set.of("orbit", "id", "action", "payload");

    private static final Set<String> PAYLOAD_FIELDS =
            Set.of("url");

    // No instances
    private OrbitValidator() {}

    /**
     * Validates a JSON string as an Orbit Protocol v1.0 message.
     *
     * @param json input JSON
     * @return ValidationResult.ok() if valid; otherwise ValidationResult.invalid(reason)
     */
    public static ValidationResult validate(String json) {
        if (json == null) {
            return ValidationResult.invalid("Input is null");
        }

        final JsonNode root;
        try {
            root = MAPPER.readTree(json);
        } catch (JsonProcessingException e) {
            return ValidationResult.invalid("Input is not valid JSON");
        }

        if (root == null || !root.isObject()) {
            return ValidationResult.invalid("Root must be a JSON object");
        }

        // Top-level fields: exact match
        Iterator<String> fieldNames = root.fieldNames();
        int count = 0;
        while (fieldNames.hasNext()) {
            String f = fieldNames.next();
            count++;
            if (!ROOT_FIELDS.contains(f)) {
                return ValidationResult.invalid(
                        "Unexpected top-level field: '" + f + "'"
                );
            }
        }
        if (count != ROOT_FIELDS.size()) {
            return ValidationResult.invalid("Missing required top-level fields");
        }

        // orbit
        JsonNode orbit = root.get("orbit");
        if (orbit == null || !orbit.isTextual()) {
            return ValidationResult.invalid("'orbit' must be a string");
        }
        if (!"1.0".equals(orbit.asText())) {
            return ValidationResult.invalid(
                    "Unsupported orbit version: '" + orbit.asText() + "'"
            );
        }

        // id
        JsonNode id = root.get("id");
        if (id == null || !id.isTextual()) {
            return ValidationResult.invalid("'id' must be a string");
        }
        if (id.asText().isEmpty()) {
            return ValidationResult.invalid("'id' must be non-empty");
        }

        // action
        JsonNode action = root.get("action");
        if (action == null || !action.isTextual()) {
            return ValidationResult.invalid("'action' must be a string");
        }
        if (!"open".equals(action.asText())) {
            return ValidationResult.invalid(
                    "Unsupported action: '" + action.asText() + "'"
            );
        }

        // payload
        JsonNode payload = root.get("payload");
        if (payload == null || !payload.isObject()) {
            return ValidationResult.invalid("'payload' must be an object");
        }

        Iterator<String> payloadFields = payload.fieldNames();
        int payloadCount = 0;
        while (payloadFields.hasNext()) {
            String f = payloadFields.next();
            payloadCount++;
            if (!PAYLOAD_FIELDS.contains(f)) {
                return ValidationResult.invalid(
                        "Unexpected payload field: '" + f + "'"
                );
            }
        }
        if (payloadCount != PAYLOAD_FIELDS.size()) {
            return ValidationResult.invalid(
                    "Payload must contain exactly one field: 'url'"
            );
        }

        JsonNode url = payload.get("url");
        if (url == null || !url.isTextual()) {
            return ValidationResult.invalid("'payload.url' must be a string");
        }
        if (url.asText().isEmpty()) {
            return ValidationResult.invalid("'payload.url' must be non-empty");
        }

        // Syntactic URI validation only
        try {
            new URI(url.asText());
        } catch (URISyntaxException e) {
            return ValidationResult.invalid(
                    "'payload.url' is not a syntactically valid URI"
            );
        }

        return ValidationResult.ok();
    }

    /**
     * Convenience boolean-only check.
     */
    public static boolean isValid(String json) {
        return validate(json).isValid();
    }
}
