package com.cockroachlabs.university.javatransactions.service;

import java.sql.SQLException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemInventoryOrchestratorServiceImpl implements ItemInventoryOrchestratorService{

    @Autowired
    private ItemInventoryService itemInventoryService;

    @Override
    public void handleItemInventoryUpdate(UUID itemId, int quantity) throws SQLException, InterruptedException {
        
        RetryUtil.executeWithRetry(() -> {
            itemInventoryService.updateItemInventory(itemId, quantity);
        });

    }
    
}
