
package com.ashsoft.test;

import com.ashsoft.dao.TransactionDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {
    public static void main(String[] args) {
        ApplicationContext context= new ClassPathXmlApplicationContext("/com/ashsoft/resources/applicationContext.xml");
        TransactionDao transactionDao=(TransactionDao) context.getBean("transactionDao");
        String status=transactionDao.transferFunds("abc123", 5000,"xyzp123");
        System.out.println(status);                
    }  
}
