package org.springframework.samples.petclinic.visits.web;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.samples.petclinic.visits.model.Visit;
import org.springframework.samples.petclinic.visits.model.VisitRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@DataJpaTest
public class VisitRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private VisitRepository visitRepository;
    @Test
    public void testFindByPetId() {
        // Create and persist test visits
        Visit visit1 = new Visit();
        visit1.setDescription("Visit 1 pet 1");
        visit1.setPetId(1);
        visit1.setDate(new Date());
        entityManager.persist(visit1);

        Visit visit2 = new Visit();
        visit2.setDescription("Visit 2 pet 1");
        visit2.setPetId(1);
        visit2.setDate(new Date());
        entityManager.persist(visit2);

        Visit visit3 = new Visit();
        visit3.setDescription("Visit pet 2");
        visit3.setPetId(2);
        visit3.setDate(new Date());
        entityManager.persist(visit3);

        entityManager.flush();

        // Test findByPetId method
        List<Visit> foundVisits = visitRepository.findByPetId(1);

        assertEquals(2, foundVisits.size(), "Should find 2 visits pet ID 1");
        assertEquals("Visit 1 pet 1", foundVisits.get(0).getDescription());
        assertEquals("Visit 2 pet 1", foundVisits.get(1).getDescription());
    }
    @Test
    public void testFindByPetIdIn() {
        // Create and persist test visits
        Visit visit1 = new Visit();
        visit1.setDescription("Visit pet 1");
        visit1.setPetId(1);
        visit1.setDate(new Date());
        entityManager.persist(visit1);

        Visit visit2 = new Visit();
        visit2.setDescription("Visit pet 2");
        visit2.setPetId(2);
        visit2.setDate(new Date());
        entityManager.persist(visit2);

        Visit visit3 = new Visit();
        visit3.setDescription("Visit pet 3");
        visit3.setPetId(3);
        visit3.setDate(new Date());
        entityManager.persist(visit3);

        entityManager.flush();

        // Test findByPetIdIn method
        List<Visit> foundVisits = visitRepository.findByPetIdIn(Arrays.asList(1, 3));

        assertEquals(2, foundVisits.size(), "Should find 2 visits pet IDs 1 and 3");
        assertEquals(1, foundVisits.get(0).getPetId());
        assertEquals(3, foundVisits.get(1).getPetId());
    }
    @Test
    public void testSaveVisit() {
        Visit visit = new Visit();
        visit.setDescription("New visit");
        visit.setPetId(4);
        visit.setDate(new Date());

        Visit savedVisit = visitRepository.save(visit);

        assertNotNull(savedVisit.getId(), "Saved visit should have an ID");

        Visit fetchedVisit = entityManager.find(Visit.class, savedVisit.getId());
        assertEquals("New visit", fetchedVisit.getDescription());
        assertEquals(4, fetchedVisit.getPetId());
    }
}
