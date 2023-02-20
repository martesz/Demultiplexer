package org.demultiplexer.service;

import org.demultiplexer.model.Config;

public interface ConfigParser {
    Config parseConfig(String configPath);
}
