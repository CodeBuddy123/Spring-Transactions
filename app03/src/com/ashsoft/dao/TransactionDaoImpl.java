
package com.ashsoft.dao;

import com.ashsoft.beans.Account;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
public class TransactionDaoImpl  implements TransactionDao{
    
    
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Transactional   //this Annotation can be used to replace AOP namespace tags in XML
    @Override        
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
    
    @Transactional
    @Override
    public String deposit(String accNo, int dp_Amount) {
        String status="";
        
        int depositRowCount=credit(accNo, dp_Amount);
        if(depositRowCount==1)
        {
            status="SUCCESS";
        }
        else
        {
            status="FAILURE";
            throw new RuntimeException("Incorrect Account information");
        }
        return status;
    }
    
    @Transactional
    @Override
    public String withDraw(String accNo, int wd_Amount) {
        
        String status="";
        List<Object> list=jdbcTemplate.query("select * from account where ACCNO='"+accNo+"'",(rs,index)->{
            Account acc= new Account();
            acc.setAccno(rs.getString("ACCNO")); //getting the data into Acc bean from RS
            acc.setAccbal(rs.getInt("ACCBAL"));
            return acc;
        });
           Account acc1=(Account)list.get(0);   //getting the data into acc1 bean from acc bean of list
           int balance=acc1.getAccbal();
           //System.out.println(balance);
           
           if(wd_Amount>=balance)
           {
               status="FAILURE";
               throw new RuntimeException("Insufficient Funds in your Account");
           }
           else
           {
               int withDrawRowCount=debit(accNo, wd_Amount);
               if(withDrawRowCount==1)
               {
                   status="SUCCESS";
               }
               else
               {
                   status="FAILURE";
                   throw new RuntimeException("Incorrect Account Details");
               }
           }
           return status;
    }     
}
