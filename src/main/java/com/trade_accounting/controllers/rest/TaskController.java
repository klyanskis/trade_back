package com.trade_accounting.controllers.rest;

import com.trade_accounting.models.Task;
import com.trade_accounting.models.dto.TaskDTO;
import com.trade_accounting.services.interfaces.SearchableService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.kaczmarzyk.spring.data.jpa.domain.Between;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("api/task")
public class TaskController {

    @Qualifier("TaskService")
    private final SearchableService<TaskDTO, Task> taskService;

    @ApiOperation(value = "getById", notes = "Получение списка всех задач")
    @GetMapping
    public ResponseEntity<Iterable<TaskDTO>> getAll() {
        return ResponseEntity.ok(taskService.getAll());
    }

    @ApiOperation(value = "search", notes = "Получение списка задач по заданному фильтру")
    @GetMapping(value = "search")
    public ResponseEntity<Iterable<TaskDTO>> search(@And({
            @Spec(path = "description", params = "description", spec = LikeIgnoreCase.class),
            @Spec(path = "taskEmployee.id", params = "employeeId", spec = Equal.class),
            @Spec(path = "taskAuthor.id", params = "taskAuthorId", spec = Equal.class),
            @Spec(path = "creationDateTime", params = {"lowerDate", "upperDate"}, spec = Between.class),
            @Spec(path = "completed", params = "completed", spec = Equal.class),
    }) Specification<Task> spec) {
        return ResponseEntity.ok(taskService.search(spec));
    }

    @ApiOperation(value = "getById", notes = "Получение задачи по её id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное получение списка задач"),
            @ApiResponse(code = 404, message = "Данный контролер не найден"),
            @ApiResponse(code = 403, message = "Операция запрещена"),
            @ApiResponse(code = 401, message = "Нет доступа к данной операции")}
    )
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getById(@PathVariable("id") long id) {
        return taskService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @ApiOperation(value = "create", notes = "Создание новой задачи")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Задача создана"),
            @ApiResponse(code = 404, message = "Данный контролер не найден"),
            @ApiResponse(code = 403, message = "Операция запрещена"),
            @ApiResponse(code = 401, message = "Нет доступа к данной операции")}
    )
    @PostMapping(consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Void> create(@RequestBody TaskDTO dto) {
        taskService.create(dto);

        URI userURI = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dto.getId())
                .normalize()
                .toUri();

        return ResponseEntity.created(userURI).build();
    }

    @ApiOperation(value = "update", notes = "Обновление задачи")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Задача обновлена"),
            @ApiResponse(code = 404, message = "Данный контролер не найден"),
            @ApiResponse(code = 403, message = "Операция запрещена"),
            @ApiResponse(code = 401, message = "Нет доступа к данной операции")}
    )
    @PutMapping
    public ResponseEntity<Void> update(@RequestBody TaskDTO dto) {
        taskService.update(dto);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "deleteById", notes = "Удаление задачи по её id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Задача удалена"),
            @ApiResponse(code = 404, message = "Данный контролер не найден"),
            @ApiResponse(code = 403, message = "Операция запрещена"),
            @ApiResponse(code = 401, message = "Нет доступа к данной операции")}
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        taskService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
