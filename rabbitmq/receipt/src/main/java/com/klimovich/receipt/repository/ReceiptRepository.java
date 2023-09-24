package com.klimovich.receipt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klimovich.receipt.model.Receipt;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
}
