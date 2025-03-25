# CLAUDE.md Guide for AgentFactory Library

## Build Commands
- Build: `./gradlew build`
- Clean and build: `./gradlew clean build`
- Build and publish locally: `./gradlew publishToMavenLocal`
- Generate javadoc: `./gradlew javadoc`

## Example Usage
- Run example: `export OPENAI_API_KEY=your_api_key_here && export SERPER_API_KEY=your_serper_key_here && ./gradlew run`
- **Important**: Both API keys are required - OPENAI_API_KEY for the AI model and SERPER_API_KEY for the search tool

## API Key Security Best Practices
- Never commit API keys to version control
- Use environment variables for local development
- Consider using a secrets manager for production deployments
- Rotate API keys regularly
- Set appropriate permissions and rate limits on API keys
- For CI/CD, use secure environment variables or secret management services

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
- **Documentation**:
  - JavaDoc required for ALL public APIs, classes, methods, and interfaces
  - Avoid inline comments unless critically necessary to explain complex logic
  - Use self-documenting code with descriptive variable and method names
- **Security**:
  - NEVER include hardcoded API keys or credentials in code
  - Always use environment variables or secure configuration for sensitive data
- **Testing**:
  - Every class must have corresponding unit tests
  - Tests should cover both success and failure paths
  - Use JUnit Jupiter for test framework
  - Use AssertJ for fluent assertions
  - Use Mockito for mocking dependencies
  - Write descriptive test method names using pattern: methodName_condition_expectedOutcome()
  - NEVER use inline comments in test code
  - Test method names should be self-documenting
  - Use proper test setup and helper methods to improve readability

## IntelliJ MCP Commands
- Open file: Use `.navigate` to explore project files
- View implementations: Use `.goto implementation` at cursor position
- Debug: Set breakpoints with `.toggle breakpoint` and use `.debug`
- Find usages: `.find usages` at cursor position
- Refactor: `.rename`, `.extract method`, etc.
