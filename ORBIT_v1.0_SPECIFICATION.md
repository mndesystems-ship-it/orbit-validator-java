# Orbit Protocol v1.0 Specification

## Status

**FROZEN.** This specification is immutable and will never change.

Future versions (v2.0+) are separate protocols with separate validators.

---

## Overview

Orbit Protocol v1.0 defines a minimal JSON object that describes exactly one intent:

> **open** a resource identified by a URL.

Orbit v1.0 describes intent only. It does not execute actions.

---

## Message Structure

A valid Orbit v1.0 message is a JSON object containing **exactly** these four top-level fields:

- `orbit`
- `id`
- `action`
- `payload`

No additional top-level fields are permitted.

---

## Required Fields

### `orbit` (string)

- MUST be exactly `"1.0"`
- Any other value is invalid

### `id` (string)

- MUST be a non-empty string
- Opaque identifier for tracking/logging/de-duplication
- Global uniqueness is recommended but not required

### `action` (string)

- MUST be exactly `"open"`
- No other action value is valid in v1.0

### `payload` (object)

- MUST be a JSON object
- MUST contain exactly one field: `url`

#### `payload.url` (string)

- MUST be a non-empty string
- MUST be a syntactically valid URI (RFC 3986)
- Semantics are not validated (e.g., scheme, domain, policy)

---

## Validation Rules

A message is valid if and only if:

1. The input parses as JSON
2. The root value is a JSON object
3. The root contains exactly the four required top-level fields
4. Each required field is present and has the correct type
5. Field values meet all constraints above

Validators MUST be fail-closed: invalid messages are rejected, not interpreted.

---

## Example Valid Message

```json
{
  "orbit": "1.0",
  "id": "intent-a7f3e2",
  "action": "open",
  "payload": {
    "url": "https://example.com/page"
  }
}
```

---

## Invalid Examples (Non-Exhaustive)

- Missing any required field
- Any extra top-level field
- Wrong type for any field
- Empty string for `id`, `action`, or `payload.url`
- `orbit` not exactly `"1.0"`
- `action` not exactly `"open"`
- `payload` contains fields other than `url`
- `payload.url` is not syntactically a valid URI

---

## Non-Goals

This specification does NOT define:
- authentication / authorization
- security policy
- executor behavior or support guarantees
- logging / auditing requirements
- transport (NFC, QR, file, API, etc.)
- retries, scheduling, or workflows
