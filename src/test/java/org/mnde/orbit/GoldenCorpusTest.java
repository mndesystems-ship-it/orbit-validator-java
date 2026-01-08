package org.mnde.orbit;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Golden corpus validation tests.
 *
 * Ensures:
 *  - All valid examples are accepted
 *  - All invalid examples are rejected
 *
 * This test enforces fail-closed behavior.
 */
public class GoldenCorpusTest {

    private static final Path VALID_DIR =
            Path.of("src/test/resources/golden/valid");

    private static final Path INVALID_DIR =
            Path.of("src/test/resources/golden/invalid");

    @Test
    void allValidExamplesPass() throws IOException {
        Files.list(VALID_DIR).forEach(path -> {
            try {
                String json = Files.readString(path);
                ValidationResult result = OrbitValidator.validate(json);

                assertTrue(
                        result.isValid(),
                        "Expected VALID but was INVALID for file: " + path.getFileName()
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void allInvalidExamplesFail() throws IOException {
        Files.list(INVALID_DIR).forEach(path -> {
            try {
                String json = Files.readString(path);
                ValidationResult result = OrbitValidator.validate(json);

                assertFalse(
                        result.isValid(),
                        "Expected INVALID but was VALID for file: " + path.getFileName()
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
