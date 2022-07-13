package in.amita.demo.payroll.controller;

import in.amita.demo.payroll.dto.Order;
import in.amita.demo.payroll.dto.OrderCreationResponse;
import in.amita.demo.payroll.dto.OrderStatus;
import in.amita.demo.payroll.dto.Status;
import in.amita.demo.payroll.exception.OrderNotFoundException;
import in.amita.demo.payroll.modelAssesmbler.OrderModelAssembler;
import in.amita.demo.payroll.repository.OrderRepository;
import in.amita.demo.payroll.service.OrderCreationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class OrderController {
    @Autowired
    OrderRepository repository;

    @Autowired
    OrderModelAssembler assembler;

    @Autowired
    OrderCreationService orderCreationService;

    @GetMapping("/orders")
    public ResponseEntity<CollectionModel<EntityModel<Order>>> all(){
        return ResponseEntity.ok(assembler.toCollectionModel(repository.findAll()));
    }

    @PostMapping("/orders")
    public ResponseEntity<EntityModel<OrderCreationResponse>> newOrder(@RequestBody Order order){
        order.setStatus(Status.IN_PROGRESS);
        order.setId(orderCreationService.generateOrderId());
        repository.save(order);

        log.info("Triggering creation async job...");
        orderCreationService.createOrder(order);
        return ResponseEntity.accepted().body(assembler.toModel(new OrderCreationResponse(order.getId())));
    }

    @GetMapping("/orders/{id}/status")
    public ResponseEntity<EntityModel<OrderStatus>> getOrderStatus(@PathVariable Long id){
        Order order = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        Status status = order.getStatus();
        return ResponseEntity.ok(assembler.toModel(new OrderStatus(id, status)));
    }

    @DeleteMapping("/orders/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id){
        Order order = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        if(!order.getStatus().equals(Status.IN_PROGRESS)){
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        }
        order.setStatus(Status.CANCELLED);
        repository.save(order);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/orders/{id}/result")
    public ResponseEntity<EntityModel<Order>> getOrderResult(@PathVariable Long id){
        Order order = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        return ResponseEntity.ok(assembler.toModel(order));
    }
}
