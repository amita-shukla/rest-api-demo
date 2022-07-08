package in.amita.demo.payroll.repository;

import in.amita.demo.payroll.dto.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
