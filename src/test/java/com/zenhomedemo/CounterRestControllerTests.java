package com.zenhomedemo;

import com.zenhomedemo.controller.CounterRestController;
import com.zenhomedemo.controller.ResultTemplate;
import com.zenhomedemo.exception.ResourceNotFoundException;
import com.zenhomedemo.model.Counter;
import com.zenhomedemo.model.Village;
import com.zenhomedemo.repository.CounterRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class CounterRestControllerTests {
    @Mock
    private CounterRepository counterRepository;

    private Counter mockedCounter;

    @InjectMocks
    private CounterRestController underTest;

    @Before
    public void init() {
        mockedCounter = Counter.builder().build();
        mockedCounter.setId(12L);
        mockedCounter.setAmount(1000.123);

        LocalDateTime now = LocalDateTime.now();
        mockedCounter.setDateTime(now);

        Village village = new Village();
        village.setName("Mitte");
        village.setId(1L);
        mockedCounter.setVillage(village);
    }

    @Test
    public void testGetCounters() {
        final List<Counter> mockCounters = new ArrayList<>();
        when(counterRepository.findAll()).thenReturn(mockCounters);

        assertThat(underTest.getAllCounters().equals(Collections.emptyList()));
    }

    @Test
    public void testGetCounterById() throws ResourceNotFoundException {
        when(counterRepository.findById(mockedCounter.getId())).thenReturn(java.util.Optional.ofNullable(mockedCounter));

        underTest.getCounterById(mockedCounter.getId());

        ResponseEntity<Counter> result = ResponseEntity.ok().body(mockedCounter);

        assertThat(underTest.getCounterById(mockedCounter.getId())).isEqualTo(result);
    }

    @Test
    public void testGetCounterByHours() throws ResourceNotFoundException {
        List<Counter> counters = new ArrayList<>();
        counters.add(mockedCounter);

        ResultTemplate counterResultTemplate = new ResultTemplate();
        counterResultTemplate.setVillageName(mockedCounter.getVillage().getName());
        counterResultTemplate.setAssumption(mockedCounter.getAmount());

        List<ResultTemplate> counterResults = new ArrayList<>();
        counterResults.add(counterResultTemplate);

        when(counterRepository.findAll()).thenReturn(counters);

        underTest.getCounterByHours(1L);

        ResponseEntity<List<ResultTemplate>> result = ResponseEntity.ok().body(counterResults);

        assertThat(underTest.getCounterByHours(1L)).isEqualTo(result);
    }

    @Test
    public void testCreateCounter() {
        when(counterRepository.save(mockedCounter)).thenReturn(mockedCounter);

        try {
            underTest.createCounter(mockedCounter);
            assertThat(underTest.createCounter(mockedCounter)).isEqualTo(mockedCounter);
        } catch (Exception e) {
            fail();
        }
    }

}
