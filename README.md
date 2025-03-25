# AgentFactory

The Enterprise Ready™ AI Agent Library

## Overview

AgentFactory is a Java library for building AI agents with text, tool calling, and memory capabilities. The library provides:

- Model abstraction for working with AI APIs
- Memory system for storing agent interactions
- Tool calling mechanism for agent actions
- Prompt management for agent instructions

## Getting Started

### Prerequisites

- Java 11 or higher
- Gradle (for building from source)
- OpenAI API key (for model access)

### Adding to Your Project

#### Gradle

```gradle
repositories {
    mavenCentral()
    // If using a local build:
    mavenLocal() 
}

dependencies {
    implementation 'com.agentfactory:agentfactory:1.0-SNAPSHOT'
}
```

#### Maven

```xml
<dependency>
    <groupId>com.agentfactory</groupId>
    <artifactId>agentfactory</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### Building from Source

```bash
./gradlew build
./gradlew publishToMavenLocal  # For local development
```

### Basic Usage

```java
import com.agentfactory.agents.ToolCallingAgent;
import com.agentfactory.models.AIModel;
import com.agentfactory.models.OpenAIModel;
import com.agentfactory.tools.FinalAnswerTool;
import com.agentfactory.tools.SearchTool;
import com.agentfactory.tools.Tool;
import java.util.Arrays;
import java.util.List;

// Initialize the model with your API key
AIModel model = new OpenAIModel(apiKey, "gpt-4");

// Create tools for the agent
List<Tool> tools = Arrays.asList(new SearchTool(), new FinalAnswerTool());

// Create agent
ToolCallingAgent agent = new ToolCallingAgent(model, tools);

// Run a task
String result = agent.run("Search for 'Java programming' and summarize the results.");
```

## Features

- **Tool Calling Agent**: Execute tasks using a sequence of tool calls
- **Memory System**: Track agent actions and observations
- **Model Abstraction**: Interface with different AI models

## Development

### Running Tests

```bash
./gradlew test
```

### Code Style

The project follows the Palantir Java Style Guide. You can format the code using:

```bash
./gradlew spotlessApply
```

## License

See the [LICENSE](LICENSE) file for details.
