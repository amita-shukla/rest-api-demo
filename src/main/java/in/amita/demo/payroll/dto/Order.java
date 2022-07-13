package in.amita.demo.payroll.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "customer_order") // 'order' is not a valid table name
@Getter @Setter @NoArgsConstructor
public class Order {
    private @Id Long id;
    private String description;

    private Integer price;
    private Integer quantity;
    private Integer amount;
    private Status status;

    public Order(Long id, String description, Integer price, Integer quantity, Integer amount, Status status) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.amount = amount;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(description, order.description) && Objects.equals(price, order.price) && Objects.equals(quantity, order.quantity) && Objects.equals(amount, order.amount) && status == order.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, price, quantity, amount, status);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", amount=" + amount +
                ", status=" + status +
                '}';
    }
}
