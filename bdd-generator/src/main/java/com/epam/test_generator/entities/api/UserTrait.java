package com.epam.test_generator.entities.api;


public interface UserTrait {

    void setPassword(String password);

    void setLoginAttempts(int i);

    Integer getLoginAttempts();

    void setLocked(boolean b);

    default void updatePassword(String password) {
        setPassword(password);
        resetLoginAttempts();
    }

    default void resetLoginAttempts() {
        setLocked(false);
        setLoginAttempts(0);
    }

    default void updateFailureLoginAttempts(int maxAttempts){
        int attempts = getLoginAttempts() + 1;
        if (maxAttempts <= attempts) {
            setLocked(true);
        }
        setLoginAttempts(attempts);
    }

    default void lock() {
        setLocked(true);
    }

    default void unlock() {
        setLocked(false);
    }
}
