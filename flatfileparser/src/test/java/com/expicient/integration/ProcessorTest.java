/**
 *
 */
package com.expicient.integration;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProcessorTest {

	@Test
    public void isDelimiterValidLengthTest() {
        boolean value = Processor.isDelimiterValid("shhs");
        assertEquals( value, false );
    }

    @Test
    public void isDelimiterValidCharTest() {
        boolean value = Processor.isDelimiterValid("a");
        assertEquals( value, false );
    }

}
