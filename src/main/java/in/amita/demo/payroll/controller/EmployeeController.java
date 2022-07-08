package in.amita.demo.payroll.controller;

import in.amita.demo.payroll.dto.Employee;
import in.amita.demo.payroll.modelAssesmbler.EmployeeModelAssembler;
import in.amita.demo.payroll.exception.EmployeeNotFoundException;
import in.amita.demo.payroll.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
public class EmployeeController {

    @Autowired
    EmployeeRepository repository;

    @Autowired
    EmployeeModelAssembler assembler;

    @GetMapping("/employees")
    public ResponseEntity<CollectionModel<EntityModel<Employee>>> all() {
        List<Employee> employees = repository.findAll();
        return ResponseEntity.ok(assembler.toCollectionModel(employees));
    }

    @PostMapping("/employees")
    public ResponseEntity<EntityModel<Employee>> newEmployee(@RequestBody Employee newEmployee) {
        EntityModel<Employee> entityModel = assembler.toModel(repository.save(newEmployee));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<EntityModel<Employee>> getEmployee(@PathVariable Long id) {
        Employee emp = repository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, new EmployeeNotFoundException(id).getMessage()));

        return ResponseEntity.ok(assembler.toModel(emp));
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<EntityModel<Employee>> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
        Employee employee = repository.findById(id)
                .map(e -> {
                    e.setName(newEmployee.getName());
                    e.setRole(newEmployee.getRole());
                    return repository.save(e);
                }).orElseGet(() -> {
                    newEmployee.setId(id);
                    return repository.save(newEmployee);
                });
        EntityModel<Employee> entityModel = assembler.toModel(employee);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Object> deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
