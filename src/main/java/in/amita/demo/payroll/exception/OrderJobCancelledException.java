package in.amita.demo.payroll.exception;

public class OrderJobCancelledException extends Exception{
    public OrderJobCancelledException(Long id){
        super("Job cancelled for id:" + id);
    }
}
