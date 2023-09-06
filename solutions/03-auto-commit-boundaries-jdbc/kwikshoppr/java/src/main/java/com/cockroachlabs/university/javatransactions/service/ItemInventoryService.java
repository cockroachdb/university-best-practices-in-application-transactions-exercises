package com.cockroachlabs.university.javatransactions.service;

import java.sql.SQLException;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

public interface ItemInventoryService {

    @Transactional
    public void updateItemInventory(UUID itemId, int quantity) throws SQLException, InterruptedException ;

}
