package full_k8s_project.todo.Controller;

import full_k8s_project.todo.Model.Todo;
import full_k8s_project.todo.Repository.TodoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin
public class TodoController {
        private final TodoRepository repo;
        public TodoController(TodoRepository repo) {
            this.repo = repo;
        }
        @GetMapping
        public List<Todo> getAll() {
            return repo.findAll();
        }
        @PostMapping
        public Todo create(@RequestBody Todo todo) {
            return repo.save(todo);
        }

        @PutMapping("/{id}")
        public Todo update(@PathVariable Long id, @RequestBody Todo todo) {
            todo.setId(id);
            return repo.save(todo);

        }
        @DeleteMapping("/{id}")
        public void delete(@PathVariable Long id) {
            repo.deleteById(id);
        }
}

