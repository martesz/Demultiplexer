package org.demultiplexer.service;

import org.demultiplexer.model.Config.ConfigType;
import org.demultiplexer.model.ConfigGroup;

import java.util.List;
import java.util.Map;

public interface Demultiplexer {
    Map<String, List<String>> demultiplex(ConfigType alignment, List<ConfigGroup> config, List<String> sequences);
}
