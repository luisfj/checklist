package dev.luisjohann.checklist.infra.todo.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import dev.luisjohann.checklist.ChecklistApplicationBaseTest;
import dev.luisjohann.checklist.domain.project.IProjectRepository;
import dev.luisjohann.checklist.domain.project.Project;
import dev.luisjohann.checklist.domain.project.Worker;
import dev.luisjohann.checklist.domain.todo.ITodoRepository;
import dev.luisjohann.checklist.domain.todo.Todo;
import dev.luisjohann.checklist.domain.todo.exception.TodoNotFoundForTheProjectException;
import dev.luisjohann.checklist.helper.MockProjectHelper;
import dev.luisjohann.checklist.helper.MockTodoHelper;
import dev.luisjohann.checklist.infra.todo.controller.response.ListTodoResponse;

public class ListTodoControllerTest extends ChecklistApplicationBaseTest {
    private static final String BASE_URI = "/todo/";

    @Autowired
    private IProjectRepository projectRepository;
    @Autowired
    private ITodoRepository todoRepository;

    private List<ListTodoResponse> responseExpected = new ArrayList<>();

    private Project projectWithTodos;
    private Project projectWithoutTodos;
    private Project projectInvalid;

    @BeforeAll
    void start() {
        projectWithTodos = MockProjectHelper.createBean(projectRepository);
        projectWithoutTodos = MockProjectHelper.createOtherBean(projectRepository);
        projectInvalid = MockProjectHelper.createNotPersistedBean();

        Todo todo = MockTodoHelper.createBean(todoRepository, projectWithTodos, null);
        Todo otherTodo = MockTodoHelper.createBean(todoRepository, projectWithTodos, null);

        responseExpected.add(buildTodoResponse(todo));
        responseExpected.add(buildTodoResponse(otherTodo));
    }

    ListTodoResponse buildTodoResponse(Todo todo) {
        BiFunction<Worker, Function<Worker, String>, String> workerCheck = (worker, getMethod) -> worker == null
                ? null
                : getMethod.apply(worker);

        return new ListTodoResponse(todo.getId(), todo.getTitle(), todo.getDescription(),
                workerCheck.apply(todo.getAssignedTo(), Worker::getSlug),
                workerCheck.apply(todo.getAssignedTo(), Worker::getName),
                todo.getCreatedAt(), todo.getUpdatedAt(), todo.getCheckedAt(),
                workerCheck.apply(todo.getCheckedWorker(), Worker::getSlug),
                workerCheck.apply(todo.getCheckedWorker(), Worker::getName));
    }

    @Test
    void testWithExistingProjectSlug_thenReturnTodosFromProject() {
        webTestClient
                .get()
                .uri(BASE_URI + projectWithTodos.getSlug())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ListTodoResponse[].class).value(list -> {
                    assertTrue(responseExpected.size() > 0 && Objects.nonNull(list)
                            && Arrays.asList(list).containsAll(responseExpected));
                });
    }

    @Test
    void testWithExistingProjectSlugWhereNotHaveTodos_thenReturnTodoNotFoundForTheProjectException() {
        webTestClient
                .get()
                .uri(BASE_URI + projectWithoutTodos.getSlug())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(TodoNotFoundForTheProjectException.class);
    }

    @Test
    void testWithNotExistingProjectSlug_thenReturnTodoNotFoundForTheProjectException() {
        webTestClient
                .get()
                .uri(BASE_URI + projectInvalid.getSlug())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(TodoNotFoundForTheProjectException.class);
    }

}
