package dao; /**
 * Created by ekky on 12/30/15.
 */

import java.util.List;
import model.Meja;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;


public class GenericDao {
    private static SessionFactory factory;
    public static void main(String[] args) {
        GenericDao dao = new GenericDao();
        dao.getAllData("from Meja");
        
    }
    public Integer save(Object m){
        try{
            factory = new Configuration().configure().buildSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        Session session = factory.openSession();
        Transaction tx = null;
        Integer no = null;
        try {
            tx=session.beginTransaction();
            no = (Integer) session.save(m);
            tx.commit();
        }catch (HibernateException e){
            if(tx!=null){
                tx.rollback();
            }
            e.printStackTrace();
        }finally {
            session.close();
        }
        return no;
    }

    public boolean update(Object m){
        try{
            factory = new Configuration().configure().buildSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        Session session = factory.openSession();
        Transaction tx=null;
        try {
            tx=session.beginTransaction();
            session.update(m);
            tx.commit();
            return true;
        }catch (HibernateException e){
            if(tx!=null){
                tx.rollback();
            }
            e.printStackTrace();
        }finally {
            session.close();
        }
        return false;
    }
   
    public boolean delete(Object m){
        try{
            factory = new Configuration().configure().buildSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        Session session = factory.openSession();
        Transaction tx=null;
        try {
            tx=session.beginTransaction();
            session.delete(m);
            tx.commit();
            return true;
        }catch (HibernateException e){
            if(tx!=null){
                tx.rollback();
            }
            e.printStackTrace();
        }finally {
            session.close();
        }
        return false;
    }
    
    public List getAllData(String query){
        try{
            factory = new Configuration().configure().buildSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx= session.beginTransaction();
            List data =  session.createQuery(query).list();
            tx.commit();
            return data;
        }catch (HibernateException e){
            if (tx!=null){tx.rollback();}
            System.out.println("error");
            e.printStackTrace();
        }finally {
            session.close();
        }
        return null;
    }
    
    public Object getData(String query){
        try{
            factory = new Configuration().configure().buildSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx= session.beginTransaction();
            Object data = (Meja) session.createQuery(query).uniqueResult();
            tx.commit();
            return data;
        }catch (HibernateException e){
            if (tx!=null){tx.rollback();}
            e.printStackTrace();
        }finally {
            session.close();
        }
        return null;
    }
    
    
    
}
