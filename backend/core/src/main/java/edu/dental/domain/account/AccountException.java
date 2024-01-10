package edu.dental.domain.account;

import java.util.logging.Logger;

public class AccountException extends Exception{

    private static final Logger logger;

    static {
        logger = Logger.getLogger(AccountService.class.getName());
    }

    public AccountException() {
    }

    public AccountException(Throwable e) {
        super(e);
    }
}
