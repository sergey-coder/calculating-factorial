package client.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import client.model.CalculatingRequest;
import client.services.http.ConverterHttpService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ConverterController.class)
class ConverterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConverterHttpService converterHttpService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Проверка успешного запроса на метод getEventCalculating
     */
    @Test
    void requestStartCalculating() throws Exception {
        mockMvc.perform(post("/api/converter/factorial")
                .content(objectMapper.writeValueAsString(new CalculatingRequest()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }

}
