package org.springframework.samples.petclinic.vets.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VetTests {

    @Test
    void shouldAddSpecialtyToVet() {
        String specialtyName = "Dentistry";
        Vet vet = new Vet();
        Specialty specialty = new Specialty();
        specialty.setName(specialtyName);

        vet.addSpecialty(specialty);

        List<Specialty> specialties = vet.getSpecialties();
        assertEquals(1, specialties.size());
        assertEquals(specialtyName, specialties.get(0).getName());
    }

    @Test
    public void shouldReturnCorrectFullName() {
        String firstName = "John";
        String lastName = "Doe";
        int id = 1;
        Vet vet = new Vet();
        vet.setFirstName(firstName);
        vet.setLastName(lastName);
        vet.setId(id);

        assertEquals(firstName, vet.getFirstName());
        assertEquals(lastName, vet.getLastName());
        assertEquals(id, vet.getId());
    }

    @Test
    void shouldSortSpecialtiesByName() {
        String specialtyName1 = "Zoology";
        String specialtyName2 = "Dentistry";
        Vet vet = new Vet();

        Specialty s1 = new Specialty();
        s1.setName(specialtyName1);
        Specialty s2 = new Specialty();
        s2.setName(specialtyName2);

        vet.addSpecialty(s1);
        vet.addSpecialty(s2);

        List<Specialty> specialties = vet.getSpecialties();

        assertEquals(2, specialties.size());
        assertEquals(specialtyName2, specialties.get(0).getName());
        assertEquals(specialtyName1, specialties.get(1).getName());
    }
}
