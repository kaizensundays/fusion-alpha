
# Junie Project Guidelines

## File header

When creating a new Kotlin or Java class,
HTML or CSS file, add a header after imports
with the actual file creation date and time.
Replace $AGENT_NAME with the currently active agent name.
Replace $MODEL_NAME with the currently active LLM model the agent is using.
If a file is being modified, add or replace the model name.
Use the example below:

/**
 * Created: Sunday 11/02/2025, 10:51 AM Eastern Time
 *
 * @author Sergey Chuykov & $AGENT_NAME ($MODEL_NAME)
 */

## Code: General
- Use Kotlin by default.
- Use 'pure' functions whenever possible. (Pure functions are functions that don't have any side effects)

## Code: Entity-Control-Boundary-Factory Stereotypes
### Entity
- Represents domain objects with long-lived, persistent data.
- It does not contain any domain logic
- It does not contain any code that interacts with the outside world.
### Control
- Implements use-case or scenario-specific domain logic.
- Coordinates interactions between entities and boundaries, orchestrating workflows without direct actor access.
- It does not contain any code that interacts with the outside world.
### Boundary
- Handles all external communication interfaces (e.g., UI, APIs, Databases, File Systems).
- It does not contain any domain logic
### Factory
- Creates classes and objects of Entity, Control, Boundary
- Promotes loose coupling by hiding complex object construction logic and enabling dependency injection.
### Rules
- When creating a new or updating existing class, classify it according to the definition above.
- Mark each class with '<<stereotype>>' (e.g., '<<entity>>', '<<control>>', '<<boundary>>', '<<factory>>').

## Comments
- Add detailed comments to the code fragments that use Kotlin coroutines
- Add detailed comments to the code fragments related to concurrency and locking
- Add detailed comments to the code fragments with Spring WebFlux RouterFunctions
- Add detailed comments to all HTMX elements in the HTML files.
- Add detailed comments to all CSS elements in the HTML and CSS files.

**## Test
 - Use JUnit 5 by default;
 - Write a unit test for each <<control>> and <<boundary>> class that has any logic;
 - Group JUnit 5 tests for each method using @Nested inner class;
 - When changing the code in a package, run all tests in this package;
 - Run tests sequentially;
 - Don't run *RemoteTest and *ContainerTest;

## Front-end
 - use Spring WebFlux RouterFunctions to handle requests from the browser
 - use Thymeleaf for HTML templating
 - use HTML, HTMX, CSS, Thymeleaf
 - don't use JavaScript
 - don't use any CSS frameworks

## Etc
 - Don't open files in the 'docs' directory if not asked.
