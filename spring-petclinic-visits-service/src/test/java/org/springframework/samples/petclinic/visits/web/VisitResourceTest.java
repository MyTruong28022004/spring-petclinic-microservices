package org.springframework.samples.petclinic.visits.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.visits.model.Visit;
import org.springframework.samples.petclinic.visits.model.VisitRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Collections;

import static java.util.Arrays.asList;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(VisitResource.class)
@ActiveProfiles("test")
class VisitResourceTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    VisitRepository visitRepository;
    private Visit request;
    private Visit response;

    @BeforeEach
    void initial_data(){
        request = Visit.VisitBuilder.aVisit()
            .id(123)
            .petId(123)
            .build();
        response = Visit.VisitBuilder.aVisit()
            .id(123)
            .petId(123)
            .build();
    }
    @Test
    void shouldFetchVisits() throws Exception {
        given(visitRepository.findByPetIdIn(asList(111, 222)))
            .willReturn(
                asList(
                    Visit.VisitBuilder.aVisit()
                        .id(1)
                        .petId(111)
                        .build(),
                    Visit.VisitBuilder.aVisit()
                        .id(2)
                        .petId(222)
                        .build(),
                    Visit.VisitBuilder.aVisit()
                        .id(3)
                        .petId(222)
                        .build()
                )
            );

        mvc.perform(get("/pets/visits?petId=111,222"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.items[0].id").value(1))
            .andExpect(jsonPath("$.items[1].id").value(2))
            .andExpect(jsonPath("$.items[2].id").value(3))
            .andExpect(jsonPath("$.items[0].petId").value(111))
            .andExpect(jsonPath("$.items[1].petId").value(222))
            .andExpect(jsonPath("$.items[2].petId").value(222));
    }
    @Test
    void shouldReturnBadRequestWhenPetIdMissing() throws Exception {
        mvc.perform(get("/pets/visits"))
            .andExpect(status().isBadRequest());
    }
    @Test
    void shouldReturnVisitsForSinglePetId() throws Exception {
        given(visitRepository.findByPetIdIn(asList(111)))
            .willReturn(
                asList(
                    Visit.VisitBuilder.aVisit().id(1).petId(111).build()
                )
            );

        mvc.perform(get("/pets/visits?petId=111"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.items[0].id").value(1))
            .andExpect(jsonPath("$.items[0].petId").value(111));
    }
    @Test
    void shouldReturnEmptyListWhenNoVisitsFound() throws Exception {
        given(visitRepository.findByPetIdIn(asList(999, 888)))
            .willReturn(Collections.emptyList());

        mvc.perform(get("/pets/visits?petId=999,888"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.items").isArray())
            .andExpect(jsonPath("$.items").isEmpty());
    }
    @Test
    void shouldReturnBadRequestWhenPetIdInvalid() throws Exception {
        mvc.perform(get("/pets/visits?petId=abc,123"))
            .andExpect(status().isBadRequest());
    }
//    @Test
//    void CreateVisit() throws Exception{
//        ObjectMapper objectMapper = new ObjectMapper();
//        String content = objectMapper.writeValueAsString(request);
//        given(visitRepository.save(request)).willReturn(response);
//        mvc.perform(post("/pet/visit/")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(content))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("id").value(123))
//            .andExpect(jsonPath("petId").value(123));
//
//    }

}
