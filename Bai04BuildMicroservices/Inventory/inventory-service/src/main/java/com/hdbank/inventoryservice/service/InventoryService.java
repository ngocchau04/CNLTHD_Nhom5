package com.hdbank.inventoryservice.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hdbank.inventoryservice.dto.InventoryResponse;
import com.hdbank.inventoryservice.model.Inventory;
import com.hdbank.inventoryservice.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public List<InventoryResponse> checkStock(List<String> skuCodes) {
        if (skuCodes == null || skuCodes.isEmpty()) {
            return Collections.emptyList();
        }

        List<Inventory> inventories = inventoryRepository.findBySkuCodeIn(skuCodes);
        Map<String, Integer> quantityBySku = inventories.stream()
                .collect(Collectors.toMap(
                        Inventory::getSkuCode,
                        Inventory::getQuantity,
                        (left, right) -> right));

        return skuCodes.stream()
                .map(sku -> InventoryResponse.builder()
                        .skuCode(sku)
                        .inStock(isInStock(quantityBySku.get(sku)))
                        .build())
                .toList();
    }

    private boolean isInStock(Integer quantity) {
        return quantity != null && quantity > 0;
    }
}
