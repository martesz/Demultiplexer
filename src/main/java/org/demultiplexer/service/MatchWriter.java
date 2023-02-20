package org.demultiplexer.service;

import java.util.List;
import java.util.Map;

public interface MatchWriter {
    void writeMatches(Map<String, List<String>> matches);
}
