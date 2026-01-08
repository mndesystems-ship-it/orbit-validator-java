package org.mnde.orbit;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class GoldenCorpusTest {

    private static final Path VALID_DIR =
            Path.of("src/test/resources/golden/v1.0/valid");

    private static final Path INVALID_DIR =
            Path.of("src/test/resources/golden/v1.0/invalid");

    @Test
    void validMessagesAreAccepted() throws IOException {
        OrbitValidator validator = new OrbitValidator();

        Files.list(VALID_DIR).forEach(path -> {
            try {
                String json = Files.readString(path);
                ValidationResult result = validator.validate(json);
                assertTrue(result.isValid(), "Expected VALID for " + path.getFileName());
            } catch (Exception e) {
                fail("Exception validating " + path.getFileName() + ": " + e.getMessage());
            }
        });
    }

    @Test
    void invalidMessagesAreRejected() throws IOException {
        OrbitValidator validator = new OrbitValidator();

        Files.list(INVALID_DIR).forEach(path -> {
            try {
                String json = Files.readString(path);
                ValidationResult result = validator.validate(json);
                assertFalse(result.isValid(), "Expected INVALID for " + path.getFileName());
            } catch (Exception e) {
                // Parse errors are acceptable for invalid corpus
            }
        });
    }
}
