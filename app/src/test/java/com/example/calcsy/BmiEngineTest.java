package com.example.calcsy;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BmiEngineTest {

    @Test
    public void testCalculateBMI_Normal() {
        double weight = 70.0;
        double height = 175.0; // 1.75 m
        double expected = 70.0 / (1.75 * 1.75); // ~22.857
        double actual = BmiEngine.calculateBMI(weight, height);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testCalculateBMI_InvalidInput() {
        assertEquals(0.0, BmiEngine.calculateBMI(0, 175), 0.001);
        assertEquals(0.0, BmiEngine.calculateBMI(70, 0), 0.001);
        assertEquals(0.0, BmiEngine.calculateBMI(-1, -1), 0.001);
    }

    @Test
    public void testCategories() {
        assertEquals("KURANG BERAT BADAN", BmiEngine.getCategory(17.5));
        assertEquals("BERAT BADAN IDEAL", BmiEngine.getCategory(22.0));
        assertEquals("KELEBIHAN BERAT BADAN", BmiEngine.getCategory(27.5));
        assertEquals("OBESITAS", BmiEngine.getCategory(32.0));
    }
}
