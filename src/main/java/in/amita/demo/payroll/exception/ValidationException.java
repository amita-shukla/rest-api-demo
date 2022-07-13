package in.amita.demo.payroll.exception;

public class ValidationException extends RuntimeException{
    public ValidationException(String message){
        super(message);
    }
}
