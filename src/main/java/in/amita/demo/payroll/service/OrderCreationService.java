package in.amita.demo.payroll.service;

import in.amita.demo.payroll.dto.Order;
import in.amita.demo.payroll.dto.Status;
import in.amita.demo.payroll.exception.OrderJobCancelledException;
import in.amita.demo.payroll.exception.OrderNotFoundException;
import in.amita.demo.payroll.exception.ValidationException;
import in.amita.demo.payroll.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderCreationService {
    @Autowired
    OrderRepository orderRepository;

    @Async
    public void createOrder(Order order) {
        Long id = order.getId();
        try {
            Thread.sleep(10000);

            cancelOrContinue(id, 1);
            if (order.getPrice() < 0) throw new ValidationException("Price must not be negative");
            if (order.getQuantity() < 0) throw new ValidationException("Quantity can not be negative");

            cancelOrContinue(id, 2);
            order.setAmount(order.getPrice() * order.getQuantity());
            order.setStatus(Status.COMPLETED);
            log.info("Async job completed");
        } catch (OrderJobCancelledException e){
            order.setStatus(Status.CANCELLED);
            log.info("Job cancelled");
        } catch (Exception e) {
            order.setStatus(Status.FAILED);
            log.error("Job failed");
            throw new RuntimeException(e);
        } finally {
            orderRepository.save(order);
        }
    }

    private void cancelOrContinue(Long id, int cancelPosition) throws OrderJobCancelledException {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        boolean isCancelled = order.getStatus().equals(Status.CANCELLED);
        if (isCancelled){
            log.info("cancelled captured at pos: " + cancelPosition);
            throw new OrderJobCancelledException(id);
        } else {
          log.info("cancel checked at pos: "+ cancelPosition);
        }
    }
    public Long generateOrderId(){
        return orderRepository.count()+ 1L;
    }
}
