package ru.practicum.statsservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.statscommon.dto.EndpointHit;
import ru.practicum.statscommon.dto.ViewStats;
import ru.practicum.statsservice.controller.StatsController;
import ru.practicum.statsservice.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatsController.class)
public class StatsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatsService statsService;

    @Test
    void hit_ShouldReturnCreatedStatus() throws Exception {
        EndpointHit dto = new EndpointHit("app", "uri", "ip", LocalDateTime.now());

        mockMvc.perform(post("/hit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void getStats_ShouldReturnViewStats() throws Exception {
        ViewStats viewStats = new ViewStats("app", "/test", 5L);
        when(statsService.getStats(any(LocalDateTime.class), any(LocalDateTime.class), any(), anyBoolean())).thenReturn(List.of(viewStats));

        mockMvc.perform(get("/stats")
                        .param("start", "2025-04-05 00:00:00")
                        .param("end", "2025-04-05 23:59:59"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].app").value("app"))
                .andExpect(jsonPath("$[0].hits").value(5));
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(obj);
    }
}