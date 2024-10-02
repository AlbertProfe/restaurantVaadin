package dev.example.restaurant.repository;

import dev.example.restaurant.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {

    }

