package com.cockroachlabs.university.javatransactions.service;

import java.sql.SQLException;

import org.jdbi.v3.core.statement.UnableToExecuteStatementException;
import org.postgresql.util.PSQLException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RetryUtil {
    
    public static void executeWithRetry(RetryableTransaction transaction) throws  SQLException, InterruptedException  {
        // Initialize parameters
        int maxRetries = 3;  // Set maximum number of retries
        int retryCount = 0;  // Use this to track which retry we're on (start at 0)
        int retryDelay = 1000; // Delay parameter for exponential backoff (milliseconds)

        while (true) {

            try {

                // First, attempt the transaction
               transaction.execute();
                // The transaction worked! Time to exit!
                return;

            } catch (UnableToExecuteStatementException exception) {
                // Caught an UnableToExecuteStatementException

                // First, verify that this is actually a retry error:
                if (isRetryError(exception)) {

                    // Check to see if we've exceeded our allowable retries
                    // If you haven't modified anything, this will occur if even
                    // one retry error is caught
                    if (retryCount >= maxRetries) {
                        log.info("CURRENT RETRY COUNT {}", retryCount);

                        // Throw a RuntimeException 
                        throw new RuntimeException("Max retries exceeded", exception);

                    } else {  // Otherwise, increment the counter and try again

                        // UPDATED FOR EXERCISE 5 SOLUTION
                        // We don't want to keep retrying right away as that could overload the system
                        // Instead, use exponential backoff to set the retry delay interval
                        int delay = (int) (retryDelay * Math.pow(2, retryCount));

                        // Wait for the delay period before retrying
                        Thread.sleep(delay);

                        // Increment the retry counter
                        retryCount++;

                    }

                } else {  // Not a retry error! Just re-throw the caught exception

                    throw exception;

                }

            }  // End of catch block
                
        }  // End of while block
    }

    private static boolean isRetryError(UnableToExecuteStatementException exception) {

        // Helper function. 
        // Once we've caught the UnableToExecuteStatementException,
        // We'll check the cause to determine if it's a retry error
        Throwable cause = exception.getCause(); 
        log.error(String.format("ENCOUNTERED  %s", cause.toString()));

        // We are looking for a type of PSQLException, "TransactionRetryError"
        if (cause instanceof PSQLException) {
            PSQLException psqlException = (PSQLException) cause;

            /**
             * The SQL State code 40001 refers to RETRY_WRITE_TOO_OLD error.
             * This error occurs when a transaction A tries to write to a row R, 
             * but another transaction B that was supposed to be serialized after A 
             * (i.e., had been assigned a higher timestamp), has already written to 
             * that row R, and has already committed. 
             * 
             * This is a common error when you have too much contention in your workload.
             * 
             * Ensure the SQL State code is 40001
             */
            if ("40001".equals(psqlException.getSQLState())) {

                // Since we've encountered UnableToExecuteStatementException, with a cause
                // of PSQLException with an error code of 40001 (RETRY_WRITE_TOO_OLD), 
                // it can only be a retry error.
                return true;

            }

        }

        // If we reach this point, it wasn't a retry error.
        return false;

    }
}
