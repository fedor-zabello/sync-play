# Getting Started

### Architecture

This project follows a layered architecture with the following layers:

- **web** — controllers and WebSocket handlers (entry points)
- **application** — application services orchestrating domain logic
- **domain** — core domain entities and business rules
- **repository** — persistence layer (JPA entities, mappers, repository implementations)

Layer dependency rules are enforced by ArchUnit via `ApplicationModulesTest.verifiesLayerDependencies`:

```
web → application → domain ← repository
```

- **web** may only access **application**
- **application** may only access **domain**
- **repository** may only access **domain**
- **domain** has no dependencies on other layers
