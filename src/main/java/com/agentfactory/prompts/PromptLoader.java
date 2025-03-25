package com.agentfactory.prompts;

import java.io.InputStream;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

public class PromptLoader {
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
