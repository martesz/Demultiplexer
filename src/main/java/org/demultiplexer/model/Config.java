package org.demultiplexer.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {
    public enum ConfigType {
        ENDS_ALIGNMENT("endsAlignment"),
        MID_ALIGNMENT("midAlignment"),
        BEST_ALIGNMENT("bestAlignment");

        private final String value;

        ConfigType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }

        public static ConfigType fromString(String s) {
            return Arrays.stream(ConfigType.values())
                    .filter(configType -> configType.value.equals(s))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid alignment type: " + s));
        }
    }

    private Map<ConfigType, List<ConfigGroup>> groups;

    public Config() {
        this.groups = new HashMap<>();
    }

    public List<ConfigGroup> getConfigType(ConfigType type) {
        return groups.get(type);
    }

    public void addGroup(ConfigType type, ConfigGroup group) {
        List<ConfigGroup> configGroupsWithType = groups.computeIfAbsent(type, l -> new ArrayList<>());
        configGroupsWithType.add(group);
    }
}
