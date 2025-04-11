package org.springframework.samples.petclinic.vets.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SpecialtyTests {

    @Test
    public void shouldSetAndGetSpecialtyProperties() {
        Specialty specialty = new Specialty();
        
        specialty.setName("radiology");

        assertEquals("radiology", specialty.getName());
    }
}
