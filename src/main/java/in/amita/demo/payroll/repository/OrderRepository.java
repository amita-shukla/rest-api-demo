package in.amita.demo.payroll.repository;

import in.amita.demo.payroll.dto.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
