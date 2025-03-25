package com.agentfactory.tools;

import java.util.Map;

/**
 * A tool used by agents to submit their final answer to a query.
 * When this tool is called, it signals that the agent has completed its task.
 */
public class FinalAnswerTool implements Tool {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "final_answer";
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if the "answer" argument is missing
     */
    @Override
    public Object execute(Map<String, Object> args) {
        String answer = (String) args.get("answer");
        if (answer == null) {
            throw new IllegalArgumentException("FinalAnswer tool requires an 'answer' argument.");
        }
        return answer;
    }
}
