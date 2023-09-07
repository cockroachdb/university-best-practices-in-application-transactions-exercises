package com.cockroachlabs.university.javatransactions;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.sql.SQLException;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.cockroachlabs.university.javatransactions.dao.ItemDao;
import com.cockroachlabs.university.javatransactions.domain.Item;
import com.cockroachlabs.university.javatransactions.service.ItemInventoryService;

import lombok.extern.slf4j.Slf4j;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {SpringBootJdbiApplication.class, JdbiConfiguration.class})
@Slf4j
public class SpringBootJdbiApplicationIntegrationTest {
    
    @Autowired
    private ItemDao itemDao;

    @Autowired
    private ItemInventoryService itemInventoryService;
 
    @Test
    public void givenNewItem_whenInsertNewItem_thenSuccess() {

        assertNotNull(itemDao);
        
        Item item = Item.builder().name("foo")
        .description("fang")
        .quantity(200)
        .build();
         
        UUID generatedId = itemDao.insertItem(item);
        log.info("[RUNNING TEST - givenNewItem_whenInsertNewItem_thenSuccess] generatedId = %s", generatedId);
        assertNotNull(generatedId);
    }

    @Test
    public void givenExistingItem_WhenInventoryUpdated_thenSuccess() throws SQLException, InterruptedException {
        assertNotNull(itemDao);
        
        Item item = Item.builder().name("foo")
        .description("fang")
        .quantity(200)
        .build();

        UUID generatedId = itemDao.insertItem(item);

        assertNotNull(generatedId);
        
        itemInventoryService.updateItemInventory(generatedId, 3);

        int updatedQuantity = itemDao.findItemById(generatedId).getQuantity();

        assertTrue(197 == updatedQuantity, "New quantity should reflect 200 - 3");

    }      

}
