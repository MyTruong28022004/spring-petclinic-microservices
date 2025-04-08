package org.springframework.samples.petclinic.visits.web;
import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.visits.model.Visit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
public class VisitModelTest {
    @Test
    public void testCreateVisitWithBuilder() {
        Date visitDate = new Date();

        Visit visit = Visit.VisitBuilder.aVisit()
            .id(1)
            .description("Test visit description")
            .petId(5)
            .date(visitDate)
            .build();

        assertEquals(Integer.valueOf(1), visit.getId());
        assertEquals("Test visit description", visit.getDescription());
        assertEquals(5, visit.getPetId());
        assertEquals(visitDate, visit.getDate());
    }
    @Test
    public void testCreateVisitDirectly() {
        Date visitDate = new Date();

        Visit visit = new Visit();
        visit.setId(2);
        visit.setDescription("Another test visit");
        visit.setPetId(3);
        visit.setDate(visitDate);

        assertEquals(Integer.valueOf(2), visit.getId());
        assertEquals("Another test visit", visit.getDescription());
        assertEquals(3, visit.getPetId());
        assertEquals(visitDate, visit.getDate());
    }
    @Test
    public void testDefaultDate() {
        Visit visit = new Visit();
        assertNotNull(visit.getDate(), "Default date should not be null");
    }
}
