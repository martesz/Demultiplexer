package org.demultiplexer.service;

import org.demultiplexer.model.Config.ConfigType;
import org.demultiplexer.model.ConfigGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.demultiplexer.model.ConfigGroup.AlignmentType.INFIX;
import static org.demultiplexer.model.ConfigGroup.AlignmentType.POSTFIX;
import static org.demultiplexer.model.ConfigGroup.AlignmentType.PREFIX;

public class WildcardDemultiplexer implements Demultiplexer {

    public static final String UNMATCHED = "unmatched";

    @Override
    public Map<String, List<String>> demultiplex(ConfigType alignment, List<ConfigGroup> config, List<String> sequences) {
        switch (alignment) {
            case ENDS_ALIGNMENT -> {
                return demuxEndsAlignment(sequences, config);
            }
            case MID_ALIGNMENT -> {
                return demuxMidAlignment(sequences, config);
            }
            case BEST_ALIGNMENT -> {
                return demuxBestAlignment(sequences, config);
            }
            default -> {
                return Collections.emptyMap();
            }
        }
    }

    private Map<String, List<String>> demuxBestAlignment(List<String> sequences, List<ConfigGroup> groups) {
        Map<String, List<String>> matchesByGroups = new HashMap<>();
        List<String> unmatched = new ArrayList<>(sequences);
        for (ConfigGroup group : groups) {
            String bestMatch = null;
            int maxMatch = -1;
            for (String sequence : sequences) {
                String infix = group.getFix(INFIX);
                int currentMaxMatches = -1;
                for (int i = 0; i <= sequence.length() - infix.length(); i++) {
                    String substring = sequence.substring(i, i + infix.length());
                    int matches = 0;
                    for (int j = 0; j < infix.length(); j++) {
                        if (infix.charAt(j) == substring.charAt(j) || infix.charAt(j) == '?') {
                            matches++;
                        }
                    }
                    if (matches > currentMaxMatches) {
                        currentMaxMatches = matches;
                    }
                }
                if (currentMaxMatches > maxMatch) {
                    maxMatch = currentMaxMatches;
                    if (bestMatch != null) {
                        unmatched.add(bestMatch);
                    }
                    bestMatch = sequence;
                    unmatched.remove(bestMatch);
                }
            }
            matchesByGroups.put(group.getName(), List.of(bestMatch));
        }
        matchesByGroups.put(UNMATCHED, unmatched);
        return matchesByGroups;
    }

    private Map<String, List<String>> demuxMidAlignment(List<String> sequences, List<ConfigGroup> groups) {
        Map<String, List<String>> matchesBygroups = new HashMap<>();
        List<String> unmatched = new ArrayList<>(sequences);
        for (ConfigGroup group : groups) {
            String infix = group.getFix(INFIX).replace("?", ".");
            Pattern pattern = Pattern.compile("^" + "(.*)" + infix + "(.*)" + "$");
            List<String> matched = sequences.stream()
                    .filter(seq -> pattern.matcher(seq).matches())
                    .collect(Collectors.toList());
            unmatched.removeAll(matched);
            matchesBygroups.put(group.getName(), matched);
        }
        if (!unmatched.isEmpty()) {
            matchesBygroups.put(UNMATCHED, unmatched);
        }
        return matchesBygroups;
    }

    private Map<String, List<String>> demuxEndsAlignment(List<String> sequences, List<ConfigGroup> groups) {
        Map<String, List<String>> matchesBygroups = new HashMap<>();
        List<String> unmatched = new ArrayList<>(sequences);
        for (ConfigGroup group : groups) {
            String prefix = group.getFix(PREFIX).replace("?", ".");
            String postfix = group.getFix(POSTFIX).replace("?", ".");
            Pattern pattern = Pattern.compile("^" + prefix + "(.*)" + postfix + "$");
            List<String> matched = sequences.stream()
                    .filter(seq -> pattern.matcher(seq).matches())
                    .collect(Collectors.toList());
            unmatched.removeAll(matched);
            matchesBygroups.put(group.getName(), matched);
        }
        if (!unmatched.isEmpty()) {
            matchesBygroups.put(UNMATCHED, unmatched);
        }
        return matchesBygroups;
    }
}
