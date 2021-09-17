package ru.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.model.CalculatingRequest;
import ru.model.CalculatingRespons;
import ru.services.grpc.GrpcService;
import ru.services.grpc.SendRequestToGrpcServer;
import ru.services.grpc.impl.GrpcServiceEventImpl;
import ru.services.http.ConverterHttpGrpcService;
import ru.services.http.impl.ConverterHttpGrpcServiceImpl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ConverterController.class)
class ConverterControllerTest {

    @MockBean
    private ConverterHttpGrpcService service;

    @MockBean
    private GrpcServiceEventImpl grpcServiceEvent;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    ConverterController testClass;

    /**
     * Вызывается ли метод stopCalculatingId().
     */
    @Test
    void getStopCalculatingRespons() throws Exception {
        CalculatingRequest calculatingRequest = new CalculatingRequest();
        calculatingRequest.setUid("testUid");

        SendRequestToGrpcServer sendRequestToGrpcServer = new SendRequestToGrpcServer();
        GrpcService grpcService = new GrpcServiceEventImpl(sendRequestToGrpcServer);
        ConverterHttpGrpcService service = new ConverterHttpGrpcServiceImpl(grpcService);
        ConverterController converterController = new ConverterController(service);


        CalculatingRequest calculatingRequestStart = new CalculatingRequest();
        calculatingRequestStart.setTreads(10);
        calculatingRequestStart.setNumber(20);

        //converterController.requestStartCalculating(calculatingRequestStart);
        CalculatingRespons calculatingRespons = converterController.stopCalculatingId(calculatingRequest);


//        CalculatingRespons calculatingRespons = new CalculatingRespons();
//        calculatingRespons.setUid("testUid");
//        calculatingRespons.setMessage("запрос на остановку вычеслений выполнен");
//
//        CalculatingRequest calculatingRequest = new CalculatingRequest();
//        calculatingRequest.setUid("testUid");
//
//        testClass.stopCalculatingId(calculatingRequest);
//        //Mockito.when(service.stopCalculat(calculatingRequest)).thenReturn(calculatingRespons);
//
//
//
//        ResultActions resultActions = mockMvc.perform(post("/api/converter/stop")
//                .content(objectMapper.writeValueAsString(calculatingRequest))
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().is(200))
//                .andExpect(jsonPath("$.uid").value(calculatingRespons.getUid()))
//                .andExpect(jsonPath("$.message").value(calculatingRespons.getMessage()));
    }

//    void department() throws Exception {
//        PersonInfo personInfo = new PersonInfo().setId(45).setFullName("Full name");
//        DepartmentResponse departmentResponse = new DepartmentResponse().setId(78).setName("IT").setClosed(true).setPersons(List.of(personInfo));
//        Mockito.when(departmentService.getDepartment(Mockito.anyInt())).thenReturn(departmentResponse);
//
//        mockMvc.perform(get("/api/v1/departments/78")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().is(200))
//                .andExpect(jsonPath("$.id").value(departmentResponse.getId()))
//                .andExpect(jsonPath("$.name").value(departmentResponse.getName()))
//                .andExpect(jsonPath("$.closed").value(departmentResponse.isClosed()));
//    }

    /**
     * Проверка успешного запроса на метод requestStartCalculating
     */
    @Test
    void requestStartCalculating() throws Exception {
        CalculatingRespons respons = new CalculatingRespons();
        respons.setUid("someUid");
        CalculatingRequest request = new CalculatingRequest();

        //Mockito.when(service.startCalculat(Mockito.mock(CalculatingRequest.class))).thenReturn(respons);
        Mockito.doReturn(respons).when(service).startCalculat(request);
        CalculatingRespons calculatingRespons = testClass.requestStartCalculating(request);

        ResultActions resultActions = mockMvc.perform(post("/api/converter/start")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("uid").value(respons.getUid()));


//        mockMvc.perform(post("/form"))
//                .andExpect(matchAll(
//                        status().isOk(),
//                        redirectedUrl("/person/1"),
//                        model().size(1),
//                        model().attributeExists("person"),
//                        flash().attributeCount(1),
//                        flash().attribute("message", "success!"))
//                );
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

        CalculatingRequest request = new CalculatingRequest();
        request.setTreads(10);
        request.setNumber(15);
        System.out.println(objectMapper.writeValueAsString(request));
    }

    /**
     * Вызывается ли метод stopCalculatingId().
     */
    @Test
    void getStopCalculatingId() throws Exception {

        CalculatingRespons calculatingRespons = new CalculatingRespons();
        calculatingRespons.setUid("testUid");
        calculatingRespons.setMessage("запрос на остановку вычеслений выполнен");

        CalculatingRequest calculatingRequest = new CalculatingRequest();
        calculatingRequest.setUid("testUid");

        testClass.stopCalculatingId(calculatingRequest);
        Mockito.verify(service, Mockito.times(1)).stopCalculat(calculatingRequest);
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

    /**
     * Проверка успешного запроса на метод getCalculatingStatus
     */
    @Test
    void recommenceCalculating() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/converter/recommence")
                .content(objectMapper.writeValueAsString(new CalculatingRequest()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }
}
