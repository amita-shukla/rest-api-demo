package in.amita.demo.payroll.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class OrderStatus {
    private Long id;
    private Status status;
}
