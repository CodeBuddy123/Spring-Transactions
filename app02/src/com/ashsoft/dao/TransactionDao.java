
package com.ashsoft.dao;

public interface TransactionDao {
    public String transferFunds(String fromAccount,int amount,String toAccount);
}
