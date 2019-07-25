
package com.ashsoft.dao;

import org.springframework.jdbc.core.JdbcTemplate;
public class TransactionDaoImpl  implements TransactionDao{
    
    
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override        //Only Business Logic exists,but not service logic(Transactions)
    public String transferFunds(String fromAccount, int amount, String toAccount) {
        String status="";     
        int debitRowCount=debit(fromAccount,amount);
        //float f=20/0; Exception[problem] is caused, rollback happens automatically
        int creditRowCount=credit(toAccount,amount);
        
        if(creditRowCount==1 && debitRowCount==1)
        {
                   
            status="SUCCESS";   //Ensures commit operation, for successful debit and credit operations
        }
       else
        {
           status="FAILURE";
           throw new RuntimeException("Incorrect Account Details"); //Ensures rollback,for wrong account details
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
