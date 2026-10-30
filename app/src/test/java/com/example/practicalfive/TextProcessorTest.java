package com.example.practicalfive;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TextProcessorTest {
    private final TextProcessor processor = new TextProcessor();

    @Test public void acceptsAllFourMunicipalities() {
        assertEquals("Beijing", processor.processMunicipality("Beijing"));
        assertEquals("Shanghai", processor.processMunicipality("Shanghai"));
        assertEquals("Chongqing", processor.processMunicipality("Chongqing"));
        assertEquals("Tianjin", processor.processMunicipality("Tianjin"));
    }

    @Test public void normalisesCaseWithoutChangingCanonicalSpelling() {
        assertEquals("Beijing", processor.processMunicipality("beijing"));
        assertEquals("Beijing", processor.processMunicipality("BEIJING"));
        assertEquals("Shanghai", processor.processMunicipality("SHANGHAI"));
        assertEquals("Tianjin", processor.processMunicipality("tianjin"));
        assertEquals("Chongqing", processor.processMunicipality("ChOnGqInG"));
    }

    @Test public void trimsSurroundingWhitespace() {
        assertEquals("Beijing", processor.processMunicipality("  Beijing  "));
        assertEquals("Shanghai", processor.processMunicipality("\tShanghai\n"));
    }

    @Test public void rejectsNull() {
        assertNull(processor.processMunicipality(null));
    }

    @Test public void rejectsEmptyString() {
        assertNull(processor.processMunicipality(""));
    }

    @Test public void rejectsWhitespaceOnly() {
        assertNull(processor.processMunicipality("   "));
        assertNull(processor.processMunicipality("\t\n"));
        assertNull(processor.processMunicipality("\u2003"));
    }

    @Test public void rejectsUnsupportedNamesAndNumbers() {
        assertNull(processor.processMunicipality("Guangzhou"));
        assertNull(processor.processMunicipality("123"));
        assertNull(processor.processMunicipality("Bei jing"));
    }

    @Test public void rejectsMultipleMunicipalities() {
        assertNull(processor.processMunicipality("Beijing Shanghai"));
        assertNull(processor.processMunicipality("Tianjin,Chongqing"));
    }
}
