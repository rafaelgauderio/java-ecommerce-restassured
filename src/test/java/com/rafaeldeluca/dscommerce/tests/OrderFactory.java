package com.rafaeldeluca.dscommerce.tests;

import java.time.Instant;

import com.rafaeldeluca.dscommerce.entities.Order;
import com.rafaeldeluca.dscommerce.entities.OrderItem;
import com.rafaeldeluca.dscommerce.entities.OrderStatus;
import com.rafaeldeluca.dscommerce.entities.Payment;
import com.rafaeldeluca.dscommerce.entities.Product;
import com.rafaeldeluca.dscommerce.entities.User;

public class OrderFactory {

	public static Order createOrder(User client) {
		
		Order order = new Order(1L, Instant.now(), OrderStatus.WAITING_PAYMENT, client, new Payment());
		
		Product product = ProductFactory.createProduct();
		OrderItem orderItem = new OrderItem(order, product, 2, 10.0);
		order.getItems().add(orderItem);
		
		return order;
	}
}
