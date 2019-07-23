
package com.ashsoft.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class TransactionDaoImpl  implements TransactionDao{
    
    private DataSourceTransactionManager txManager;
    private JdbcTemplate jdbcTemplate;

    public void setTxManager(DataSourceTransactionManager txManager) {
        this.txManager = txManager;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public String transferFunds(String fromAccount, int amount, String toAccount) {
        String status="";
        
        TransactionDefinition txDefinition= new DefaultTransactionDefinition();
        TransactionStatus tx_Status=txManager.getTransaction(txDefinition);
        
        try {
             int debitRowCount=debit(fromAccount,amount);
             int creditRowCount=credit(toAccount,amount);
             if(creditRowCount==1 && debitRowCount==1)
             {
                 txManager.commit(tx_Status);   //Committing the Transaction on SUCCESSFUL  credit and debit operations.
                 status="SUCCESS";
             }
             else
             {
                 txManager.rollback(tx_Status); //RollBacking the  changes in Transaction if any Failure Occurs
                 status="FAILURE";
             }
         
        } catch (Exception e) {
            txManager.rollback(tx_Status);       //Rollbacking the changes if any Exception occurs
            status="FAILURE";
            e.printStackTrace();
        }
        return status;
    }
    public int debit(String fromAccount,int transferAmount)
    {
        int rowCount=jdbcTemplate.update("update account set ACCBAL= ACCBAL- "+transferAmount+" where ACCNO='"+fromAccount+"'");
        System.out.println(transferAmount+" debited from Account "+fromAccount);
        
        return rowCount;
    }
    public int credit(String toAccount,int transferAmount)
    {
        int rowCount=jdbcTemplate.update("update account set ACCBAL= ACCBAL+ "+transferAmount+" where ACCNO='"+toAccount+"'");
        System.out.println(transferAmount+" credited to Account "+toAccount);
        
        return rowCount;
    }
}
