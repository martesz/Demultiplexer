package org.demultiplexer.service;

import org.demultiplexer.model.Config;
import org.demultiplexer.model.ConfigGroup;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.demultiplexer.model.Config.ConfigType;
import static org.demultiplexer.model.Config.ConfigType.BEST_ALIGNMENT;
import static org.demultiplexer.model.Config.ConfigType.ENDS_ALIGNMENT;
import static org.demultiplexer.model.Config.ConfigType.MID_ALIGNMENT;
import static org.demultiplexer.model.ConfigGroup.AlignmentType.INFIX;
import static org.demultiplexer.model.ConfigGroup.AlignmentType.POSTFIX;
import static org.demultiplexer.model.ConfigGroup.AlignmentType.PREFIX;

public class JsonConfigParser implements ConfigParser {

    @Override
    public Config parseConfig(String configPath) {
        JSONParser parser = new JSONParser();
        Config config = new Config();
        try {
            JSONObject jsonConfig = (JSONObject) parser.parse(new FileReader(configPath));

            parseEndsAlignment(jsonConfig)
                    .forEach(configGroup -> config.addGroup(ENDS_ALIGNMENT, configGroup));

            parseInfixAlignment(MID_ALIGNMENT, jsonConfig)
                    .forEach(configGroup -> config.addGroup(MID_ALIGNMENT, configGroup));

            parseInfixAlignment(BEST_ALIGNMENT, jsonConfig)
                    .forEach(configGroup -> config.addGroup(BEST_ALIGNMENT, configGroup));
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        return config;
    }

    private static List<ConfigGroup> parseInfixAlignment(ConfigType configType, JSONObject jsonConfig) {
        List<ConfigGroup> infixGroups = new ArrayList<>();
        JSONObject alignment = (JSONObject) jsonConfig.get(configType.toString());
        for (Object groupName : alignment.keySet()) {
            JSONObject group = (JSONObject) alignment.get(groupName);
            String infix = (String) group.get(INFIX.toString());
            ConfigGroup configGroup = new ConfigGroup((String) groupName);
            configGroup.addFix(INFIX, infix);
            infixGroups.add(configGroup);
        }
        return infixGroups;
    }

    private List<ConfigGroup> parseEndsAlignment(JSONObject jsonConfig) {
        List<ConfigGroup> endsAlignmentGroups = new ArrayList<>();
        JSONObject endsAlignment = (JSONObject) jsonConfig.get(ENDS_ALIGNMENT.toString());
        for (Object groupName : endsAlignment.keySet()) {
            JSONObject group = (JSONObject) endsAlignment.get(groupName);
            String prefix = (String) group.get(PREFIX.toString());
            String postfix = (String) group.get(POSTFIX.toString());
            ConfigGroup configGroup = new ConfigGroup((String) groupName);
            configGroup.addFix(PREFIX, prefix);
            configGroup.addFix(POSTFIX, postfix);
            endsAlignmentGroups.add(configGroup);
        }
        return endsAlignmentGroups;
    }
}
