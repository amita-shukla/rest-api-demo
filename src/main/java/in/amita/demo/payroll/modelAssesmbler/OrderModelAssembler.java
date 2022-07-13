package in.amita.demo.payroll.modelAssesmbler;

import in.amita.demo.payroll.dto.Order;
import in.amita.demo.payroll.controller.OrderController;
import in.amita.demo.payroll.dto.OrderCreationResponse;
import in.amita.demo.payroll.dto.OrderStatus;
import in.amita.demo.payroll.dto.Status;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {
    @Override
    public EntityModel<Order> toModel(Order entity) {
        EntityModel<Order> entityModel = EntityModel.of(entity);
        getDynamicLinks(entity.getStatus(), entity.getId()).forEach(entityModel::add);
        return entityModel;
    }

    private List<Link> getDynamicLinks(Status status, Long id){
        List<Link> links = new ArrayList<>();
        if(status.equals(Status.IN_PROGRESS)) {
            links.add(linkTo(methodOn(OrderController.class).getOrderStatus(id)).withRel("status"));
            links.add(linkTo(methodOn(OrderController.class).cancelOrder(id)).withRel("cancel"));
        } else if(status.equals(Status.COMPLETED)){
            links.add(linkTo(methodOn(OrderController.class).getOrderResult(id)).withRel("result"));
        } else {
            links.add(linkTo(methodOn(OrderController.class).getOrderStatus(id)).withRel("status"));
        }
        return links;
    }
    public EntityModel<OrderCreationResponse> toModel(OrderCreationResponse response){
        EntityModel<OrderCreationResponse> entityModel = EntityModel.of(response);
        getDynamicLinks(Status.IN_PROGRESS,response.getId()).forEach(entityModel::add);
        return entityModel;
    }

    public EntityModel<OrderStatus> toModel(OrderStatus orderStatus) {
        EntityModel<OrderStatus> entityModel = EntityModel.of(orderStatus);
        getDynamicLinks(orderStatus.getStatus(), orderStatus.getId()).forEach(entityModel::add);
        return entityModel;
    }

    public CollectionModel<EntityModel<Order>> toCollectionModel(List<Order> entities) {
        return CollectionModel.of(entities.stream().map(this::toModel).collect(Collectors.toList()),
                linkTo(methodOn(OrderController.class).all()).withSelfRel());
    }
}
