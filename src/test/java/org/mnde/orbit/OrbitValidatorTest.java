package org.mnde.orbit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrbitValidatorTest {

    @Test
    void validMessage() {
        String json = """
        {
          "orbit": "1.0",
          "id": "intent-a7f3e2",
          "action": "open",
          "payload": { "url": "https://example.com/page" }
        }
        """;
        ValidationResult r = OrbitValidator.validate(json);
        assertTrue(r.isValid(), r.getError());
    }

    @Test
    void rejectsExtraTopLevelFields() {
        String json = """
        {
          "orbit": "1.0",
          "id": "x",
          "action": "open",
          "payload": { "url": "https://example.com" },
          "extra": 1
        }
        """;
        assertFalse(OrbitValidator.validate(json).isValid());
    }

    @Test
    void rejectsWrongVersion() {
        String json = """
        {
          "orbit": "2.0",
          "id": "x",
          "action": "open",
          "payload": { "url": "https://example.com" }
        }
        """;
        assertFalse(OrbitValidator.validate(json).isValid());
    }

    @Test
    void rejectsUnsupportedAction() {
        String json = """
        {
          "orbit": "1.0",
          "id": "x",
          "action": "launch",
          "payload": { "url": "https://example.com" }
        }
        """;
        assertFalse(OrbitValidator.validate(json).isValid());
    }

    @Test
    void rejectsEmptyId() {
        String json = """
        {
          "orbit": "1.0",
          "id": "",
          "action": "open",
          "payload": { "url": "https://example.com" }
        }
        """;
        assertFalse(OrbitValidator.validate(json).isValid());
    }

    @Test
    void rejectsEmptyUrl() {
        String json = """
        {
          "orbit": "1.0",
          "id": "x",
          "action": "open",
          "payload": { "url": "" }
        }
        """;
        assertFalse(OrbitValidator.validate(json).isValid());
    }

    @Test
    void rejectsInvalidUriSyntax() {
        String json = """
        {
          "orbit": "1.0",
          "id": "x",
          "action": "open",
          "payload": { "url": "https://exa mple.com" }
        }
        """;
        assertFalse(OrbitValidator.validate(json).isValid());
    }

    @Test
    void rejectsExtraPayloadFields() {
        String json = """
        {
          "orbit": "1.0",
          "id": "x",
          "action": "open",
          "payload": { "url": "https://example.com", "mode": "newtab" }
        }
        """;
        assertFalse(OrbitValidator.validate(json).isValid());
    }
}
