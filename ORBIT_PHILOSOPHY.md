# Orbit — Philosophy (Plain English)

## What Orbit Is

Orbit is a protocol for **describing intent**.

An Orbit message answers exactly one question:

> What single action is intended to happen?

Orbit does not execute actions. Orbit does not decide how actions happen.
Orbit only describes intent in a deterministic, machine-readable way.

---

## Why Orbit Exists

Automation systems often mix responsibilities together:
- message format
- execution logic
- policy and permissions
- UI assumptions

This causes ambiguity, fragmentation, and non-deterministic behavior.

Orbit exists to separate **intent (data)** from **execution (behavior)**.

---

## Core Constraints

Orbit is intentionally boring:

- One message represents **exactly one** action
- Fixed structure
- Fail-closed validation
- No conditional logic
- No side effects

If two independent implementations read the same Orbit message, they should reach the same conclusion about whether it is **structurally valid according to the specification**.

---

## What Orbit Is NOT

Orbit is not:
- an automation engine
- a workflow language
- a scripting system
- a permission system
- a scheduler

Orbit does not define:
- who is allowed to execute intents
- where or when execution occurs
- policy decisions (e.g., HTTPS-only)

Those belong to executors and policy layers.

---

## Mental Model

Orbit is like:
- a shipping label (not the delivery truck)
- a blueprint (not the building)
- a sentence (not the conversation)

It describes intent. It does not perform it.
