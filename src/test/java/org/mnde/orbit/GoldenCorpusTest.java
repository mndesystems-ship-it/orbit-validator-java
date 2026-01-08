package org.mnde.orbit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Golden corpus validation tests.
 *
 * These tests enforce fail-closed behavior when the
 * golden corpus is present.
 *
 * If the corpus directories are missing, the tests
 * are skipped rather than failing the build.
 */
public class GoldenCorpusTest {

    private static final Path VALID_DIR =
            Path.of("src/test/resources/golden/valid");

    private static final Path INVALID_DIR =
            Path.of("src/test/resources/golden/invalid");

    @Test
    void allValidExamplesPass() throws IOException {
        if (!Files.exists(VALID_DIR)) {
            System.out.println("Golden corpus (valid) not present — skipping test.");
            return;
        }

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
        if (!Files.exists(INVALID_DIR)) {
            System.out.println("Golden corpus (invalid) not present — skipping test.");
            return;
        }

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
