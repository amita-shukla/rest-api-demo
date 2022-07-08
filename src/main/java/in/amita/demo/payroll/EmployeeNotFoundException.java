package in.amita.demo.payroll;

public class EmployeeNotFoundException extends RuntimeException{
    EmployeeNotFoundException(Long id){
        super("Couldn't find employee " + id);
    }
}
