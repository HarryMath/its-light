package com.its.light.tools;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CountryDefinerTest {

    private final CountryDefiner countryDefiner = new CountryDefiner();

    @BeforeEach
    void setUp() {
        System.out.println("test started: \n");
    }

    @AfterEach
    void tearDown() {
        System.out.println("\ntest ended \n\n");
    }

    @Test
    void getCountry() {
        try{
            String country = countryDefiner.getCountry("0.0.0.1");
            assertEquals("Australia", country);
            country = countryDefiner.getCountry("217.150.35.129");
            assertEquals("Russian Federation", country);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void isCountryExist() {
        try{
            assertFalse(countryDefiner.isCountryExists("russia"));
            assertTrue(countryDefiner.isCountryExists("ruSSian Federation"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}