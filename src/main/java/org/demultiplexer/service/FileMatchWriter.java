package org.demultiplexer.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

public class FileMatchWriter implements MatchWriter {

    public static final String SEQ_ENDING = ".seq";
    private final String outPrefix;

    public FileMatchWriter(String outPrefix) {
        this.outPrefix = outPrefix;
    }

    @Override
    public void writeMatches(Map<String, List<String>> matches) {
        matches.forEach(this::writeGroup);
    }

    private void writeGroup(String groupName, List<String> sequences) {
        String content = String.join(" ", sequences);
        Path path = Path.of(outPrefix, groupName + SEQ_ENDING);
        try {
            Files.writeString(path, content, StandardOpenOption.CREATE);
        } catch (IOException e) {
            System.out.println("Unable to create file for group: " + groupName);
        }
    }


}
