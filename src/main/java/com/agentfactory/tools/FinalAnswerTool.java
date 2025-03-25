package com.agentfactory.tools;

import java.util.Map;

public class FinalAnswerTool implements Tool {
    @Override
    public String getName() {
        return "final_answer";
    }

    @Override
    public Object execute(Map<String, Object> args) {
        String answer = (String) args.get("answer");
        if (answer == null) {
            throw new IllegalArgumentException("FinalAnswer tool requires an 'answer' argument.");
        }
        return answer;
    }
}
