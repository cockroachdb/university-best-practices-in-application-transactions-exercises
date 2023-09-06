package com.cockroachlabs.university.javatransactions.service;

import java.sql.SQLException;
import java.util.UUID;

public interface ItemInventoryOrchestratorService {

    void handleItemInventoryUpdate(UUID itemId, int quantity) throws SQLException, InterruptedException ;
    
}
