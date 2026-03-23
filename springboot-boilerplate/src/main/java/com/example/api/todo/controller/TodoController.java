package com.example.api.todo.controller;

import com.example.api.common.response.BaseResponse;
import com.example.api.common.response.PageResponse;
import com.example.api.todo.dto.TodoDto;
import com.example.api.todo.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 할 일 컨트롤러
 */
@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
public class TodoController implements TodoControllerDocs {

  private final TodoService todoService;

  @Override
  @GetMapping
  public BaseResponse<PageResponse<TodoDto.TodoResponse>> getTodoList(
      Authentication authentication,
      @ModelAttribute TodoDto.TodoListRequest request) {
    return BaseResponse.ok(todoService.getTodoList(authentication.getName(), request));
  }

  @Override
  @PostMapping
  public BaseResponse<TodoDto.TodoResponse> insertTodo(
      Authentication authentication,
      @RequestBody @Valid TodoDto.CreateRequest request) {
    return BaseResponse.ok(todoService.insertTodo(authentication.getName(), request));
  }

  @Override
  @PatchMapping("/{id}/complete")
  public BaseResponse<TodoDto.TodoResponse> updateTodoComplete(
      Authentication authentication,
      @PathVariable Long id) {
    return BaseResponse.ok(todoService.updateTodoComplete(authentication.getName(), id));
  }

  @Override
  @PatchMapping("/{id}/image")
  public BaseResponse<TodoDto.TodoResponse> updateTodoImage(
      Authentication authentication,
      @PathVariable Long id,
      @RequestBody @Valid TodoDto.UpdateImageRequest request) {
    return BaseResponse.ok(todoService.updateTodoImage(authentication.getName(), id, request));
  }

  @Override
  @DeleteMapping("/{id}")
  public BaseResponse<Void> deleteTodo(
      Authentication authentication,
      @PathVariable Long id) {
    todoService.deleteTodo(authentication.getName(), id);
    return BaseResponse.ok();
  }
}
