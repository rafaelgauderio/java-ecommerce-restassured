package com.rafaeldeluca.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafaeldeluca.dscommerce.entities.OrderItem;
import com.rafaeldeluca.dscommerce.entities.OrderItemPK;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {

}
