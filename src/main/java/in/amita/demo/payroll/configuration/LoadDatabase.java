package in.amita.demo.payroll.configuration;

import in.amita.demo.payroll.dto.Employee;
import in.amita.demo.payroll.dto.Order;
import in.amita.demo.payroll.dto.Status;
import in.amita.demo.payroll.repository.EmployeeRepository;
import in.amita.demo.payroll.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository employeeRepository, OrderRepository orderRepository){
        return args -> {
          log.info("Preloading " + employeeRepository.save(new Employee("Bilbo Baggins", "burglar")));
          log.info("Preloading " + employeeRepository.save(new Employee("Frodo Baggins", "thief")));

//          log.info("Preloading " + orderRepository.save(new Order("MacBookPro", 100, Status.IN_PROGRESS)));
//          log.info("Preloading " + orderRepository.save(new Order("iPhone", 2, Status.COMPLETED)));
        };
    }
}
