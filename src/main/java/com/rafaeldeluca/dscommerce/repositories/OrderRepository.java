package com.rafaeldeluca.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafaeldeluca.dscommerce.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
