package in.amita.demo.payroll.modelAssesmbler;

import in.amita.demo.payroll.dto.Employee;
import in.amita.demo.payroll.controller.EmployeeController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EmployeeModelAssembler implements RepresentationModelAssembler<Employee, EntityModel<Employee>> {
    @Override
    public EntityModel<Employee> toModel(Employee employee) {
        return EntityModel.of(employee,
            linkTo(methodOn(EmployeeController.class).getEmployee(employee.getId())).withSelfRel(),
            linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
    }

    public CollectionModel<EntityModel<Employee>> toCollectionModel(List<Employee> employees) {
        List<EntityModel<Employee>> employeeEntityModels = employees.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(employeeEntityModels, linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
    }
}
