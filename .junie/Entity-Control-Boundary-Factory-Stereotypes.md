# Entity-Control-Boundary-Factory Stereotypes

## Entity
- Represents domain objects with long-lived, persistent data.
- It does not contain any domain logic
- It does not contain any code that interacts with the outside world.

## Control
- Implements use-case or scenario-specific domain logic.
- Coordinates interactions between entities and boundaries, orchestrating workflows without direct actor access.
- It does not contain any code that interacts with the outside world.

## Boundary
- Handles all external communication interfaces (e.g., UI, APIs, Databases, File Systems).
- It does not contain any domain logic

## Factory
- Creates classes and objects of Entity, Control, Boundary 
- Promotes loose coupling by hiding complex object construction logic and enabling dependency injection.

## Rules
- When creating a new or updating existing class, classify it according to the definition above.
- Mark each class with '<<stereotype>>' (e.g., '<<entity>>', '<<control>>', '<<boundary>>', '<<factory>>').
