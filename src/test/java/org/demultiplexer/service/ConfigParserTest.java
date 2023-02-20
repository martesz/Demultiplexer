package org.demultiplexer.service;

import org.demultiplexer.model.Config;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.demultiplexer.model.Config.ConfigType.BEST_ALIGNMENT;
import static org.demultiplexer.model.Config.ConfigType.ENDS_ALIGNMENT;
import static org.demultiplexer.model.Config.ConfigType.MID_ALIGNMENT;

public class ConfigParserTest {
    @Test
    public void parseConfigWhenValidJsonFileProvidedShouldReturnConfigObject() {
        String configPath = "src/test/resources/sample.conf";
        ConfigParser parser = new JsonConfigParser();
        Config config = parser.parseConfig(configPath);

        Assertions.assertNotNull(config);
        Assertions.assertEquals(2, config.getConfigType(ENDS_ALIGNMENT).size());
        Assertions.assertEquals(2, config.getConfigType(MID_ALIGNMENT).size());
        Assertions.assertEquals(1, config.getConfigType(BEST_ALIGNMENT).size());
    }

    @Test
    public void parseConfigWhenInvalidJsonFileProvidedShouldThrowRuntimeException() {
        String configPath = "src/test/resources/invalid.conf";
        ConfigParser parser = new JsonConfigParser();

        Assertions.assertThrows(RuntimeException.class, () -> parser.parseConfig(configPath));
    }

    @Test
    public void parseEmptyConfigShouldThrowRuntimeException() {
        String configPath = "src/test/resources/empty.conf";
        ConfigParser parser = new JsonConfigParser();

        Assertions.assertThrows(RuntimeException.class, () -> parser.parseConfig(configPath));
    }
}
