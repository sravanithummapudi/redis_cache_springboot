package com.example.redisdemo.Repository;

import com.example.redisdemo.model.product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface productrepository extends JpaRepository<product, Long> {
}
