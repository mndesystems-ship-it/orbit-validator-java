# Orbit Protocol v1.0 — Reference Validator (Java)

Authoritative reference validator for **Orbit Protocol v1.0**.

Orbit v1.0 is a minimal protocol for describing intent.  
A valid Orbit v1.0 message expresses **exactly one thing**:

> open this URL

This library answers **one question only**:

> Is this JSON message structurally valid according to the Orbit v1.0 specification?

It does **not** execute actions.

---

## Status

Orbit Protocol v1.0 is **final and frozen**.

This repository contains the authoritative reference validator for Orbit v1.0.
The validator is:

- Deterministic and stateless
- Fail-closed (invalid messages are rejected)
- Validation-only (no execution logic)
- Covered by unit tests
- Buildable on a clean machine (`mvn test`)

The public API of the validator is considered **stable for v1.0**.

---

## Scope

This library performs **validation only**.

It does **not**:

- Execute actions
- Interpret intent beyond specification-defined constraints
- Apply policy (domains, HTTPS-only, allowlists)
- Perform I/O or side effects

Invalid messages are rejected.  
Valid messages are accepted.  
Nothing else happens.

---

## Requirements

- **Java:** 17+
- **Build tool:** Maven 3.8+
- **JSON parsing:** Jackson (dependency)

---

## Build

```bash
mvn clean test
mvn clean package
```

---

## Usage

### Validate and get an error message (recommended)

```java
import org.mnde.orbit.OrbitValidator;
import org.mnde.orbit.ValidationResult;

String json = """
{
  "orbit": "1.0",
  "id": "intent-a7f3e2",
  "action": "open",
  "payload": { "url": "https://example.com" }
}
""";

ValidationResult result = OrbitValidator.validate(json);

if (result.isValid()) {
    // Valid Orbit v1.0 intent
} else {
    System.err.println(result.getError());
}
```

### Boolean-only convenience

```java
boolean ok = OrbitValidator.isValid(json);
```

---

## What this validator checks

- ✅ JSON parses and root is an object
- ✅ Exactly four top-level fields: `orbit`, `id`, `action`, `payload`
- ✅ `orbit` is exactly `"1.0"`
- ✅ `id` is a non-empty string
- ✅ `action` is exactly `"open"`
- ✅ `payload` is an object with exactly one field: `url`
- ✅ `payload.url` is a non-empty string
- ✅ `payload.url` is a syntactically valid URI (RFC 3986)

---

## What this validator does NOT check

- ❌ URL reachability
- ❌ HTTPS enforcement
- ❌ Domain allowlists / denylists
- ❌ Permissions or authentication
- ❌ Executor support
- ❌ Any execution behavior

If you need different rules, that is **not Orbit v1.0**.

---

## Documentation

- **ORBIT_PHILOSOPHY.md** — plain-English rationale and non-goals
- **ORBIT_v1.0_SPECIFICATION.md** — formal, frozen v1.0 specification

---

## License

MIT License. See `LICENSE`.
