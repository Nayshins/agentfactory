# CLAUDE.md Guide for AgentFactory Library

## Build Commands
- Build: `./gradlew build`
- Clean and build: `./gradlew clean build`
- Build and publish locally: `./gradlew publishToMavenLocal`
- Generate javadoc: `./gradlew javadoc`

## Example Usage
- Run example: `export OPENAI_API_KEY=your_api_key_here && ./gradlew run`

## Test Commands
- Run all tests: `./gradlew test`
- Run single test: `./gradlew test --tests "com.agentfactory.TestName"`
- Run tests for specific package: `./gradlew test --tests "com.agentfactory.package.*"`

## Lint Commands
- Format code: `./gradlew spotlessApply`
- Check style: `./gradlew checkstyleMain checkstyleTest`

## Code Style Guidelines
- **Formatting**: Palantir Java Style Guide with 120 character line limit
- **Naming**: camelCase for methods/variables, PascalCase for classes
- **Imports**: No wildcards; static imports only for constants
- **Types**: Prefer immutable objects; use Optional for nullable returns
- **Error handling**: SafeArg/UnsafeArg pattern; checked exceptions for recoverable errors
- **Comments**: JavaDoc for public APIs; self-documenting code preferred
- **Testing**: JUnit Jupiter; descriptive test method names; use assertj