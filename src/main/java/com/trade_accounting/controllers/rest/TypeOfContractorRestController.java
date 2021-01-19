package com.trade_accounting.controllers.rest;


import com.trade_accounting.models.dto.TypeOfContractorDto;
import com.trade_accounting.services.interfaces.TypeOfContractorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@Tag(name = "Type Of Contractor Rest Controller", description = "CRUD операции со типами контрагентов")
@Api(tags = "Type Of Contractor Rest Controller")
@RequestMapping("/api/typeofcontractor")
public class TypeOfContractorRestController {
    private final TypeOfContractorService typeOfContractorService;

    public TypeOfContractorRestController(TypeOfContractorService typeOfContractorService) {
        this.typeOfContractorService = typeOfContractorService;
    }

    @ApiOperation(value = "getAll", notes = "Возвращает список всех типов контрагентов")
    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное получение списка всех типов контрагентов"),
            @ApiResponse(code = 401, message = "Нет доступа к данной операции"),
            @ApiResponse(code = 403, message = "Операция запрещена"),
            @ApiResponse(code = 404, message = "Данный контроллер не найден")
    })
    public ResponseEntity<List<TypeOfContractorDto>> getAll() {
        List<TypeOfContractorDto> typeOfContractorDtos = typeOfContractorService.getAll();
        log.info("Запрошен список TypeOfContractorDto");
        return ResponseEntity.ok(typeOfContractorDtos);
    }

    @ApiOperation(value = "getById", notes = "Возвращает определенный тип контрагента по Id")
    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Тип контрагента найден"),
            @ApiResponse(code = 401, message = "Нет доступа к данной операции"),
            @ApiResponse(code = 403, message = "Операция запрещена"),
            @ApiResponse(code = 404, message = "Данный контроллер не найден")
    })
    public ResponseEntity<TypeOfContractorDto> getById(@PathVariable(name = "id") Long id) {
        TypeOfContractorDto typeOfContractorDto = typeOfContractorService.getById(id);
        log.info("Запрошен экземпляр TypeOfContractorDto с id= {}", id);
        return ResponseEntity.ok(typeOfContractorDto);
    }

    @ApiOperation(value = "create", notes = "Создает тип контрагента на основе переданных данных")
    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Тип контрагента успешно создан"),
            @ApiResponse(code = 201, message = "Запрос принят и данные созданы"),
            @ApiResponse(code = 401, message = "Нет доступа к данной операции"),
            @ApiResponse(code = 403, message = "Операция запрещена"),
            @ApiResponse(code = 404, message = "Данный контроллер не найден")
    })
    public ResponseEntity<?> create(@RequestBody TypeOfContractorDto typeOfContractorDto) {
        typeOfContractorService.create(typeOfContractorDto);
        log.info("Записан новый экземпляр TypeOfContractorDto");
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "update", notes = "Обновляет тип контрагента на основе переданных данных")
    @PutMapping
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Тип контрагента успешно обновлен"),
            @ApiResponse(code = 201, message = "Запрос принят и данные обновлены"),
            @ApiResponse(code = 401, message = "Нет доступа к данной операции"),
            @ApiResponse(code = 403, message = "Операция запрещена"),
            @ApiResponse(code = 404, message = "Данный контроллер не найден")
    })
    public ResponseEntity<?> update(@RequestBody TypeOfContractorDto typeOfContractorDto) {
        typeOfContractorService.update(typeOfContractorDto);
        log.info("Обновлен экземпляр TypeOfContractorDto");
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "deleteById", notes = "Удаляет тип контрагента на основе переданного ID")
    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Тип контрагента успешно удален"),
            @ApiResponse(code = 204, message = "Запрос получен и обработан, данных для возврата нет"),
            @ApiResponse(code = 401, message = "Нет доступа к данной операции"),
            @ApiResponse(code = 403, message = "Операция запрещена"),
            @ApiResponse(code = 404, message = "Данный контроллер не найден")
    })
    public ResponseEntity<?> deleteById(@PathVariable(name = "id") Long id) {
        typeOfContractorService.deleteById(id);
        log.info("Удален экземпляр TypeOfContractorDto с id= {}", id);
        return ResponseEntity.ok().build();
    }
}
