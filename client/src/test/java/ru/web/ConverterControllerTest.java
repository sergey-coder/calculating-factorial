package ru.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.model.CalculatingRequest;
import ru.services.ConverterHttpGrpcService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ConverterController.class)
class ConverterControllerTest {

    @MockBean
    private ConverterHttpGrpcService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Проверка успешного запроса на метод requestCalculating
     */
    @Test
    void requestCalculating() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/converter")
                .content(objectMapper.writeValueAsString(new CalculatingRequest()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }

    /**
     * Проверка успешного запроса на метод stopCalculatingId
     */
    @Test
    void stopCalculatingId() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/converter/stop")
                .content(objectMapper.writeValueAsString(new CalculatingRequest()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }

    /**
     * Проверка успешного запроса на метод getCalculatingStatus
     */
    @Test
    void getCalculatingStatus() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/converter/status")
                .content(objectMapper.writeValueAsString(new CalculatingRequest()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }
}
