package com.zenhomedemo.controller;

import com.zenhomedemo.exception.ResourceNotFoundException;
import com.zenhomedemo.model.Counter;
import com.zenhomedemo.repository.CounterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/")
public class CounterRestController {
    @Autowired
    private CounterRepository counterRepository;

    @GetMapping("counters")
    public List<Counter> getAllCounters() {
        return this.counterRepository.findAll();
    }

    @GetMapping("/counters/{id}")
    public ResponseEntity<Counter> getCounterById(@PathVariable(value = "id") Long counterId) throws ResourceNotFoundException {
        Counter counter = counterRepository.findById(counterId).orElseThrow(() -> new ResourceNotFoundException("Counter not found for this id " + counterId));
        return ResponseEntity.ok().body(counter);
    }

    @GetMapping("/countersByHours/{hours}")
    public ResponseEntity<List<ResultTemplate>> getCounterByHours(@PathVariable(value = "hours") Long hours) throws ResourceNotFoundException {
        LocalDateTime timeToCheck = LocalDateTime.now().minusHours(hours);
        List<ResultTemplate> counters = counterRepository.findAll().stream().filter(counter -> counter.getDateTime().isAfter(timeToCheck))
                                                         .map(this::mapToResultTemplate).collect(Collectors.toList());

        if (counters.size() == 0) {
            throw new ResourceNotFoundException("There are no data for this get counter by " + hours + " hours");
        }

        return ResponseEntity.ok().body(counters);
    }

    public ResultTemplate mapToResultTemplate(Counter el){
        return ResultTemplate.builder().villageName(el.getVillage().getName()).assumption(el.getAmount()).build();
    }


    @PostMapping("counters")
    public Counter createCounter(@RequestBody Counter counter) {
        return this.counterRepository.save(counter);
    }

    @PutMapping("/counters/{id}")
    public ResponseEntity<Counter> updateCounter(@PathVariable(value = "id") Long counterId, @Valid @RequestBody Counter counterDetails) throws ResourceNotFoundException {
        Counter counter = counterRepository.findById(counterId).orElseThrow(() -> new ResourceNotFoundException("Counter not found for this id " + counterId));

        counter.setAmount(counterDetails.getAmount());
        counter.setDateTime(counterDetails.getDateTime());

        if (!Objects.isNull(counterDetails.getVillage())) {
            counter.setVillage(counterDetails.getVillage());
        }

        return ResponseEntity.ok(this.counterRepository.save(counter));
    }

    @DeleteMapping("counters/{id}")
    public Map<String, Boolean> deleteCounter(@PathVariable(value = "id") Long counterId) throws ResourceNotFoundException {
        Counter counter = counterRepository.findById(counterId).orElseThrow(() -> new ResourceNotFoundException("Counter not found for this id %s" + counterId));
        this.counterRepository.delete(counter);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
