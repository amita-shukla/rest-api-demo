package in.amita.demo.payroll.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) {
        super("Couldn't find Order with id: " + id);
    }
}
