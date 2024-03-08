package com.lekkss.springsecurityjwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lekkss.springsecurityjwt.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}
