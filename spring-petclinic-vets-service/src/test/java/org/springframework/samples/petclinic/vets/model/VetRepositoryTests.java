package org.springframework.samples.petclinic.vets.model;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class VetRepositoryTests {

    @Autowired
    private VetRepository vetRepository;

    @Test
    void shouldSaveAndRetrieveVet() {
        String firstName = "John";
        String lastName = "Doe";
        Vet vet = new Vet();
        vet.setFirstName(firstName);
        vet.setLastName(lastName);

        vetRepository.save(vet);

        List<Vet> vets = vetRepository.findAll();
        assertEquals(1, vets.size());

        Vet retrieved = vets.get(0);
        assertEquals(firstName, retrieved.getFirstName());
        assertEquals(lastName, retrieved.getLastName());
        assertNotNull(retrieved.getId());
    }
    
    @Test
    void shouldThrowConstraintViolationExceptionWhenFirstNameIsBlank() {
        String lastName = "Smith";
        Vet vet = new Vet();

        vet.setFirstName(""); // invalid
        vet.setLastName(lastName);

        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            vetRepository.saveAndFlush(vet); // use saveAndFlush to trigger validation immediately
        });
    }

    @Test
    void shouldThrowConstraintViolationExceptionWhenLastNameIsBlank() {
        String firstName = "Jane";
        Vet vet = new Vet();
        
        vet.setFirstName(firstName);
        vet.setLastName(""); // invalid

        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            vetRepository.saveAndFlush(vet);
        });
    }
}
