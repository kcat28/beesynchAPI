package com.beesynch.app.rest.controller;

import com.beesynch.app.rest.Controller.HiveController;
import com.beesynch.app.rest.Models.Hive;
import com.beesynch.app.rest.Repo.HiveRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class HiveControllerTest {

    @Mock
    private HiveRepo hiveRepo;

    @InjectMocks
    private HiveController hiveController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(hiveController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testCreateHive_Success() throws Exception {
        Hive hive = new Hive();
        hive.setHiveName("Hive 1");

        when(hiveRepo.existsByHiveName(hive.getHiveName())).thenReturn(false);
        when(hiveRepo.save(any(Hive.class))).thenReturn(hive);

        String json = objectMapper.writeValueAsString(hive);

        // Format the creation date to match the expected response format
        String expectedCreationDate = new java.sql.Date(System.currentTimeMillis()).toString();

        mockMvc.perform(post("/hive/createHive")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().string("Hive created with name: Hive 1 and creation date: " + expectedCreationDate));

        verify(hiveRepo, times(1)).save(any(Hive.class));
    }


    @Test
    public void testCreateHive_Failure_HiveExists() throws Exception {
        Hive hive = new Hive();
        hive.setHiveName("Hive 1");

        // Mocking behavior of hiveRepo.existsByHiveName to return true (indicating hive already exists)
        when(hiveRepo.existsByHiveName(hive.getHiveName())).thenReturn(true);

        String json = objectMapper.writeValueAsString(hive);

        mockMvc.perform(post("/hive/createHive")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Hive with name: Hive 1 already exists"));

        verify(hiveRepo, times(0)).save(any(Hive.class)); // Verify save was NOT called
    }

    @Test
    public void testDeleteHive_Success() throws Exception {
        Long hiveId = 1L;

        // Mocking the deleteById method of hiveRepo
        doNothing().when(hiveRepo).deleteById(hiveId);

        mockMvc.perform(delete("/hive")
                        .param("hiveId", hiveId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("Hive deleted with ID: " + hiveId));

        verify(hiveRepo, times(1)).deleteById(hiveId);
    }
}
