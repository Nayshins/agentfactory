package com.agentfactory.prompts;

import java.io.InputStream;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

/**
 * Utility class for loading prompts from YAML files.
 * Prompts are stored in YAML format with a "system_prompt" key.
 */
public class PromptLoader {

    /**
     * Loads a prompt from a YAML file.
     *
     * @param fileName the name of the YAML file containing the prompt
     * @return the system prompt string
     * @throws RuntimeException if the file is not found or cannot be loaded
     */
    public static String loadPrompt(String fileName) {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = PromptLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new RuntimeException("Prompt file not found: " + fileName);
            }
            Map<String, Object> data = yaml.load(inputStream);
            return (String) data.get("system_prompt");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load prompt: " + e.getMessage(), e);
        }
    }
}
