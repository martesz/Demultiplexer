package org.demultiplexer.model;

import java.util.HashMap;
import java.util.Map;

public class ConfigGroup {

    public enum AlignmentType {
        PREFIX("prefix"),
        POSTFIX("postfix"),
        INFIX("infix");

        private final String value;

        AlignmentType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    private String name;
    private Map<AlignmentType, String> alignments;

    public ConfigGroup(String name) {
        this.name = name;
        this.alignments = new HashMap<>();
    }

    public void addFix(AlignmentType type, String fix) {
        alignments.put(type, fix);
    }

    public String getFix(AlignmentType type) {
        return alignments.get(type);
    }

    public String getName() {
        return name;
    }
}
