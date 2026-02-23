package full_k8s_project.todo.Repository;

import full_k8s_project.todo.Model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {}
