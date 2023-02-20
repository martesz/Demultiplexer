package org.demultiplexer;

import org.demultiplexer.model.Config;
import org.demultiplexer.model.Config.ConfigType;
import org.demultiplexer.service.ConfigParser;
import org.demultiplexer.service.Demultiplexer;
import org.demultiplexer.service.FileMatchWriter;
import org.demultiplexer.service.JsonConfigParser;
import org.demultiplexer.service.MatchWriter;
import org.demultiplexer.service.SimpleDemultiplexer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 4) {
            System.err.println("Usage: java Demultiplexer <seq_file> <conf_file> <out_prefix> <alignment>");
            return;
        }

        String seqFile = args[0];
        String confFile = args[1];
        String outPrefix = args[2];
        String alignment = args[3];

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String configPath = Objects.requireNonNull(classloader.getResource(confFile)).getPath();
        String seqPath = Objects.requireNonNull(classloader.getResource(seqFile)).getPath();
        ConfigType configType = ConfigType.fromString(alignment);

        ConfigParser configParser = new JsonConfigParser();
        Config config = configParser.parseConfig(configPath);

        Demultiplexer demultiplexer = new SimpleDemultiplexer();
        List<String> sequences = Files.readAllLines(Path.of(seqPath));
        Map<String, List<String>> matches = demultiplexer.demultiplex(configType, config.getConfigType(configType), sequences);

        MatchWriter matchWriter = new FileMatchWriter(outPrefix);
        matchWriter.writeMatches(matches);
    }
}