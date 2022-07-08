package in.amita.demo.payroll.exception;

public class EmployeeNotFoundException extends RuntimeException{
    public EmployeeNotFoundException(Long id){
        super("Couldn't find employee " + id);
    }
}
