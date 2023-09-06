package com.cockroachlabs.university.javatransactions.service;

import java.sql.SQLException;

@FunctionalInterface
public interface RetryableTransaction {

    void execute() throws SQLException, InterruptedException;
    
}
