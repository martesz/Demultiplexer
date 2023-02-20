package org.demultiplexer.service;

import org.demultiplexer.model.ConfigGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.demultiplexer.model.Config.ConfigType.BEST_ALIGNMENT;
import static org.demultiplexer.model.Config.ConfigType.ENDS_ALIGNMENT;
import static org.demultiplexer.model.Config.ConfigType.MID_ALIGNMENT;

public class DemultiplexerTest {

    private static List<String> testSequences;

    @BeforeAll
    public static void setup() {
        testSequences = List.of("ACTCACGACCACTAACTAGCAATACGATCG",
                "CAGTAAGCGATCAGACAGTACAGACGTACA",
                "ACTCACGACCACTAACTGGCAATACGATCG",
                "AGACAACATCAGATCGCAAGACGACAGATA");
    }

    @Test
    public void testSimpleEndsAlignment() {
        Demultiplexer demultiplexer = new SimpleDemultiplexer();
        ConfigGroup group1 = new ConfigGroup("group1");
        group1.addFix(ConfigGroup.AlignmentType.PREFIX, "ACTCACG");
        group1.addFix(ConfigGroup.AlignmentType.POSTFIX, "ACGATCG");
        List<ConfigGroup> config = List.of(group1);

        Map<String, List<String>> result = demultiplexer.demultiplex(ENDS_ALIGNMENT, config, testSequences);
        Assertions.assertEquals(List.of("ACTCACGACCACTAACTAGCAATACGATCG", "ACTCACGACCACTAACTGGCAATACGATCG"), result.get("group1"));
        Assertions.assertEquals(List.of("CAGTAAGCGATCAGACAGTACAGACGTACA", "AGACAACATCAGATCGCAAGACGACAGATA"), result.get("unmatched"));
    }

    @Test
    public void testWildcardEndsAlignment() {
        Demultiplexer demultiplexer = new WildcardDemultiplexer();
        ConfigGroup group1 = new ConfigGroup("group1");
        group1.addFix(ConfigGroup.AlignmentType.PREFIX, "ACTCACG?");
        group1.addFix(ConfigGroup.AlignmentType.POSTFIX, "ACGAT?G");
        List<ConfigGroup> config = List.of(group1);

        Map<String, List<String>> result = demultiplexer.demultiplex(ENDS_ALIGNMENT, config, testSequences);
        Assertions.assertEquals(List.of("ACTCACGACCACTAACTAGCAATACGATCG", "ACTCACGACCACTAACTGGCAATACGATCG"), result.get("group1"));
        Assertions.assertEquals(List.of("CAGTAAGCGATCAGACAGTACAGACGTACA", "AGACAACATCAGATCGCAAGACGACAGATA"), result.get("unmatched"));
    }

    @Test
    public void testSimpleMidAlignment() {
        Demultiplexer demultiplexer = new SimpleDemultiplexer();
        ConfigGroup group1 = new ConfigGroup("group1");
        group1.addFix(ConfigGroup.AlignmentType.INFIX, "CACTAACT");
        List<ConfigGroup> config = List.of(group1);

        Map<String, List<String>> result = demultiplexer.demultiplex(MID_ALIGNMENT, config, testSequences);
        Assertions.assertEquals(List.of("ACTCACGACCACTAACTAGCAATACGATCG", "ACTCACGACCACTAACTGGCAATACGATCG"), result.get("group1"));
        Assertions.assertEquals(List.of("CAGTAAGCGATCAGACAGTACAGACGTACA", "AGACAACATCAGATCGCAAGACGACAGATA"), result.get("unmatched"));
    }

    @Test
    public void testWildcardMidAlignment() {
        Demultiplexer demultiplexer = new WildcardDemultiplexer();
        ConfigGroup group1 = new ConfigGroup("group1");
        group1.addFix(ConfigGroup.AlignmentType.INFIX, "CACT??CT");
        List<ConfigGroup> config = List.of(group1);

        Map<String, List<String>> result = demultiplexer.demultiplex(MID_ALIGNMENT, config, testSequences);
        Assertions.assertEquals(List.of("ACTCACGACCACTAACTAGCAATACGATCG", "ACTCACGACCACTAACTGGCAATACGATCG"), result.get("group1"));
        Assertions.assertEquals(List.of("CAGTAAGCGATCAGACAGTACAGACGTACA", "AGACAACATCAGATCGCAAGACGACAGATA"), result.get("unmatched"));
    }

    @Test
    public void testSimpleBestAlignment() {
        Demultiplexer demultiplexer = new SimpleDemultiplexer();
        ConfigGroup group1 = new ConfigGroup("group1");
        group1.addFix(ConfigGroup.AlignmentType.INFIX, "CTATCTAGCAAT");
        List<ConfigGroup> config = List.of(group1);

        Map<String, List<String>> result = demultiplexer.demultiplex(BEST_ALIGNMENT, config, testSequences);
        Assertions.assertEquals(List.of("ACTCACGACCACTAACTAGCAATACGATCG"), result.get("group1"));
        Assertions.assertEquals(List.of("CAGTAAGCGATCAGACAGTACAGACGTACA", "ACTCACGACCACTAACTGGCAATACGATCG", "AGACAACATCAGATCGCAAGACGACAGATA"), result.get("unmatched"));
    }

    @Test
    public void testWildcardBestAlignment() {
        Demultiplexer demultiplexer = new WildcardDemultiplexer();
        ConfigGroup group1 = new ConfigGroup("group1");
        group1.addFix(ConfigGroup.AlignmentType.INFIX, "CTATCT?GCAAT?");
        List<ConfigGroup> config = List.of(group1);

        Map<String, List<String>> result = demultiplexer.demultiplex(BEST_ALIGNMENT, config, testSequences);
        Assertions.assertEquals(List.of("ACTCACGACCACTAACTAGCAATACGATCG"), result.get("group1"));
        Assertions.assertEquals(List.of("CAGTAAGCGATCAGACAGTACAGACGTACA", "ACTCACGACCACTAACTGGCAATACGATCG", "AGACAACATCAGATCGCAAGACGACAGATA"), result.get("unmatched"));
    }

}
