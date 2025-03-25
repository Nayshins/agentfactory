package com.agentfactory.tools;

import java.util.Map;

public interface Tool {
    String getName();

    Object execute(Map<String, Object> args);
}
