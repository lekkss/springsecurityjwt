package com.lekkss.springsecurityjwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.lekkss.springsecurityjwt.dto.ReqRes;
import com.lekkss.springsecurityjwt.entity.Product;
import com.lekkss.springsecurityjwt.repository.ProductRepository;

@RestController
public class AdminUsers {
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/public/product")
    public ResponseEntity<Object> geAlltProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }

    @PostMapping("/admin/saveproduct")
    public ResponseEntity<Object> signup(@RequestBody ReqRes product) {
        Product productToSave = new Product();
        productToSave.setName(product.getName());
        return ResponseEntity.ok(productRepository.save(productToSave));
    }

    @GetMapping("/user/alone")
    public ResponseEntity<Object> userAlone() {
        return ResponseEntity.ok("Users Alone can access this API");
    }

    @GetMapping("/adminuser/both")
    public ResponseEntity<Object> bothAdminAndUSer() {
        return ResponseEntity.ok("Both Users and Admin can access this API");
    }
}
