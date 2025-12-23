
# Junie Project Guidelines

## File header

When creating a new Kotlin or Java class,
HTML or CSS file, add a header after imports
with the actual file creation date and time.
Replace $ACTIVE_MODEL with the currently active LLM model Junie is using.
If a file is being modified, add or replace the model name.
Use the example below:

/**
 * Created: Sunday 11/02/2025, 10:51 AM Eastern Time
 *
 * @author Sergey Chuykov & Junie ($ACTIVE_MODEL)
 */

### Code
- Use Kotlin by default.
- Include and follow the rules in [Entity-Control-Boundary-Factory-Stereotypes](Entity-Control-Boundary-Factory-Stereotypes.md)
- Use 'pure' functions whenever possible. (Pure functions are functions that don't have any side effects)

### Comments
- Add detailed comments to the code fragments that use Kotlin coroutines
- Add detailed comments to the code fragments related to concurrency and locking
- Add detailed comments to the code fragments with Spring WebFlux RouterFunctions
- Add detailed comments to all HTMX elements in the HTML files.
- Add detailed comments to all CSS elements in the HTML and CSS files.

## Test
 - Use JUnit 5 by default;
 - Group JUnit 5 tests for each method using @Nested inner class;
 - When changing the code in packages com.kaizensundays.socketlab.reactor
   and com.kaizensundays.socketlab.reactor.jvm
   run all tests in these packages in the module 'socketlab-reactor-jvm';

## Front-end
 - use Spring WebFlux RouterFunctions to handle requests from the browser
 - use Thymeleaf for HTML templating
 - use HTML, HTMX, CSS, Thymeleaf
 - don't use JavaScript
 - don't use any CSS frameworks

## Etc
 - Don't open files in the 'docs' directory if not asked.
