package com.hdbank.inventoryservice.controller;

import com.hdbank.inventoryservice.dto.InventoryResponse;
import com.hdbank.inventoryservice.service.InventoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public List<InventoryResponse> checkStock(@RequestParam List<String> skuCode) {
        return inventoryService.checkStock(skuCode);
    }
}
