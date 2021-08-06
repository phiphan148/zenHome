package com.zenhomedemo.controller;

import com.zenhomedemo.exception.ResourceNotFoundException;
import com.zenhomedemo.model.Village;
import com.zenhomedemo.repository.VillageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/")
public class VillageRestController {
    @Autowired
    private VillageRepository villageRepository;

    @GetMapping("villages")
    public List<Village> getAllVillages() {
        return this.villageRepository.findAll();
    }

    @GetMapping("/villages/{id}")
    public ResponseEntity<Village> getVillageById(@PathVariable(value = "id") Long villageId) throws ResourceNotFoundException {
        Village village = villageRepository.findById(villageId).orElseThrow(() -> new ResourceNotFoundException("Village not found for this id " + villageId));
        return ResponseEntity.ok().body(village);
    }

    @PostMapping("villages")
    public Village createVillage(@RequestBody Village village) {
        return this.villageRepository.save(village);
    }

    @PutMapping("/villages/{id}")
    public ResponseEntity<Village> updateVillage(@PathVariable(value = "id") Long villageId, @Valid @RequestBody Village villageDetails) throws ResourceNotFoundException {
        Village village = villageRepository.findById(villageId).orElseThrow(() -> new ResourceNotFoundException("Village not found for this id " +
                "" + villageId));
        village.setName(villageDetails.getName());

        return ResponseEntity.ok(this.villageRepository.save(village));
    }

    @DeleteMapping("villages/{id}")
    public Map<String, Boolean> deleteVillage(@PathVariable(value = "id") Long villageId) throws ResourceNotFoundException {
        Village village = villageRepository.findById(villageId).orElseThrow(() -> new ResourceNotFoundException("Village not found for this id " + villageId));
        this.villageRepository.delete(village);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
