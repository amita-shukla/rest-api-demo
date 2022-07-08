package in.amita.demo.payroll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class EmployeeController {

    @Autowired
    EmployeeRepository repository;

    @Autowired
    EmployeeModelAssembler assembler;

    @GetMapping("/employees")
    CollectionModel<EntityModel<Employee>> all() {
        List<Employee> employees = repository.findAll();
        return assembler.toCollectionModel(employees);
    }

    @PostMapping("/employees")
    EntityModel<Employee> newEmployee(@RequestBody Employee newEmployee) {
        return assembler.toModel(repository.save(newEmployee));
    }

    @GetMapping("/employees/{id}")
    EntityModel<Employee> getEmployee(@PathVariable Long id) {
        Employee emp = repository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, new EmployeeNotFoundException(id).getMessage()));

        return assembler.toModel(emp);
    }

    @PutMapping("/employees/{id}")
    EntityModel<Employee> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
        Employee employee = repository.findById(id)
                .map(e -> {
                    e.setName(newEmployee.getName());
                    e.setRole(newEmployee.getRole());
                    return repository.save(e);
                }).orElseGet(() -> {
                    newEmployee.setId(id);
                    return repository.save(newEmployee);
                });
        return assembler.toModel(employee);
    }

    @DeleteMapping("/employees/{id}")
    void deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
