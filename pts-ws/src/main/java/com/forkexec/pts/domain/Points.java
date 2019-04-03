package com.forkexec.pts.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.forkexec.pts.domain.Exceptions.EmailAlreadyExistsException;
import com.forkexec.pts.domain.Exceptions.NotEnoughBalanceException;

/**
 * Points
 * <p>
 * A points server.
 */
public class Points {

    /**
     * Constant representing the default initial balance for every new client
     */
    private static final int DEFAULT_INITIAL_BALANCE = 100;
    /**
     * Global with the current value for the initial balance of every new client
     */
    private final AtomicInteger initialBalance = new AtomicInteger(DEFAULT_INITIAL_BALANCE);

    // database for users and points
    private static Map<String,AtomicInteger> database = new HashMap<String,AtomicInteger>();

    // Singleton -------------------------------------------------------------

    /**
     * Private constructor prevents instantiation from other classes.
     */
    private Points() {   
    }

    /**
     * SingletonHolder is loaded on the first execution of Singleton.getInstance()
     * or the first access to SingletonHolder.INSTANCE, not before.
     */
    private static class SingletonHolder {
        private static final Points INSTANCE = new Points();
    }

    public static synchronized Points getInstance() {
        return SingletonHolder.INSTANCE;
    }

	public synchronized  void registerEmail(String userEmail) throws EmailAlreadyExistsException {
        String message = "Email already exists";
        if(database.containsKey(userEmail))
            throw new EmailAlreadyExistsException(message);

        database.put(userEmail, initialBalance);
	}

	public synchronized int getBalance(String userEmail) {
        AtomicInteger balance = database.get(userEmail);
		return balance.intValue();
	}

    public synchronized int deltaBalance(String userEmail,int deltaPoints) throws NotEnoughBalanceException{
        String message = "Not enough balance";
        if(database.get(userEmail).addAndGet(deltaPoints) < 0)
            throw new NotEnoughBalanceException(message);
        return database.get(userEmail).addAndGet(deltaPoints);
    }
    public synchronized void reset() {
        database.clear(); 
        initialBalance.set(DEFAULT_INITIAL_BALANCE);
   }

	public synchronized void init(int startPoints) {
        initialBalance.set(startPoints);
           
        }

}
    
