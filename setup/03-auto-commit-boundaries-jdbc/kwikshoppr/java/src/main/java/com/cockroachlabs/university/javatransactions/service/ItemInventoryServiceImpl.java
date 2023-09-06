package com.cockroachlabs.university.javatransactions.service;

import java.sql.SQLException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cockroachlabs.university.javatransactions.dao.ItemDao;

@Service
public class ItemInventoryServiceImpl implements ItemInventoryService {

    private ItemDao itemDao;

    public ItemInventoryServiceImpl(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    @Transactional
    @Override
    public void updateItemInventory(UUID itemId, int quantity) throws SQLException, InterruptedException {
        
        //Step 1: Use the ItemDao to find the item using the method parameter itemId.
        itemDao.findItemById(itemId);

        //Step 2: Use the ItemDao to update the item quantity using the method parameter quantity
        itemDao.updateItemQuantity(itemId, quantity);
    }

}
