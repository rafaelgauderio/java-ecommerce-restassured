package com.rafaeldeluca.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafaeldeluca.dscommerce.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
