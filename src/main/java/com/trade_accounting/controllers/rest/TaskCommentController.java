package com.trade_accounting.controllers.rest;

import com.trade_accounting.models.TaskComment;
import com.trade_accounting.models.dto.TaskCommentDTO;
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
@RequestMapping("api/taskcomment")
public class TaskCommentController {

    @Qualifier("TaskCommentService")
    private final SearchableService<TaskCommentDTO, TaskComment> commentService;

    @ApiOperation(value = "getAll", notes = "Получение списка всех комментариев")
    @GetMapping
    public ResponseEntity<Iterable<TaskCommentDTO>> getAll() {
        return ResponseEntity.ok(commentService.getAll());
    }

    @ApiOperation(value = "search", notes = "Получение списка комментариев по заданному фильтру")
    @GetMapping(value = "search")
    public ResponseEntity<Iterable<TaskCommentDTO>> search(
            @And({
                    @Spec(path = "id", params = "comment_id", spec = Equal.class),
                    @Spec(path = "commentContent", params = "comment", spec = LikeIgnoreCase.class),
                    @Spec(path = "publisher.id", params = "publisher_id", spec = Equal.class),
                    @Spec(path = "creationDateTime", params = {"lower_date", "upper_date"}, spec = Between.class),
            }) Specification<TaskComment> spec) {
        return ResponseEntity.ok(commentService.search(spec));
    }

    @ApiOperation(value = "getById", notes = "Получение комментария по его id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное получение комментария"),
            @ApiResponse(code = 404, message = "Данный контролер не найден"),
            @ApiResponse(code = 403, message = "Операция запрещена"),
            @ApiResponse(code = 401, message = "Нет доступа к данной операции")}
    )
    @GetMapping("/{id}")
    public ResponseEntity<TaskCommentDTO> getById(@PathVariable("id") long id) {
        return commentService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @ApiOperation(value = "create", notes = "Создание нового комментария")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Комментарий создан"),
            @ApiResponse(code = 404, message = "Данный контролер не найден"),
            @ApiResponse(code = 403, message = "Операция запрещена"),
            @ApiResponse(code = 401, message = "Нет доступа к данной операции")}
    )
    @PostMapping(consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Void> create(@RequestBody TaskCommentDTO dto) {
        commentService.create(dto);

        URI userURI = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dto.getId())
                .normalize()
                .toUri();

        return ResponseEntity.created(userURI).build();
    }

    @ApiOperation(value = "update", notes = "Обновление комментария")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Комментарий обновлен"),
            @ApiResponse(code = 404, message = "Данный контролер не найден"),
            @ApiResponse(code = 403, message = "Операция запрещена"),
            @ApiResponse(code = 401, message = "Нет доступа к данной операции")}
    )
    @PutMapping
    public ResponseEntity<Void> update(@RequestBody TaskCommentDTO dto) {
        commentService.update(dto);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "deleteById", notes = "Удаление комментария по его id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Комментарий удален"),
            @ApiResponse(code = 404, message = "Данный контролер не найден"),
            @ApiResponse(code = 403, message = "Операция запрещена"),
            @ApiResponse(code = 401, message = "Нет доступа к данной операции")}
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        commentService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
