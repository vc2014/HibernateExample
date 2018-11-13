package com.mkyong;

import java.util.Date;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.mkyong.stock.Stock;
import com.mkyong.stock.StockDailyRecord;
import com.mkyong.util.HibernateUtil;

public class App {
	public static void main(String[] args) {
		System.out.println("Hibernate one to many (Annotation)");
		Session session = HibernateUtil.getSessionFactory().openSession();

		session.beginTransaction();

		Stock stock = new Stock();
        stock.setStockCode("7052");
        stock.setStockName("PADINI");
        session.save(stock);
        
        StockDailyRecord stockDailyRecords = new StockDailyRecord();
        stockDailyRecords.setPriceOpen(new Float("1.2"));
        stockDailyRecords.setPriceClose(new Float("1.1"));
        stockDailyRecords.setPriceChange(new Float("10.0"));
        stockDailyRecords.setVolume(3000000L);
        stockDailyRecords.setDate(new Date());
        
        stockDailyRecords.setStock(stock);        
        stock.getStockDailyRecords().add(stockDailyRecords);

        session.save(stockDailyRecords);

		session.getTransaction().commit();
		System.out.println("Done");
		
		int id = stock.getStockId();
		 
		
		session.beginTransaction();
		
		try {
			session.beginTransaction();

			Stock dbStock = (Stock) session.get(Stock.class, id);

			System.out.println(dbStock.getStockId() + " - " + dbStock.getStockName() + "__"+ dbStock.getStockDailyRecords() );
			
			Set<StockDailyRecord> stockDailyRecord1 = dbStock.getStockDailyRecords();
			
			for( StockDailyRecord stockDailyRecord : stockDailyRecord1) {
				System.out.println(stockDailyRecord);
			}
			
			session.getTransaction().commit();

		}

		catch (HibernateException e) {

			e.printStackTrace();

			session.getTransaction().rollback();

		}

	}
}
