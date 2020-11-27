package com.trade_accounting.controllers.rest;

import com.trade_accounting.models.dto.UnitDto;
import com.trade_accounting.services.interfaces.UnitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/unit")
public class UnitRestController {

    private final UnitService unitService;

    public UnitRestController(UnitService unitService) {
        this.unitService = unitService;
    }

    @GetMapping
    public ResponseEntity<List<UnitDto>> getAll() {
        List<UnitDto> unitDtos = unitService.getAll();
        return new ResponseEntity<>(unitDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnitDto> getById(@PathVariable Long id) {
        UnitDto unitDto = unitService.getById(id);
        return new ResponseEntity<>(unitDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody UnitDto unitDto) {
        unitService.create(unitDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody UnitDto unitDto) {
        unitService.update(unitDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        unitService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
