package dao;

import java.util.Iterator;
import java.util.List;
import model.DetailPesanan;
import model.Pesanan;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * Created by ekky on 12/30/15.
 */
public class DaoPesanan {
    private static SessionFactory factory;

    public Integer save(Pesanan pesanan, List<DetailPesanan> listDetailPesanan){
        try{
            factory = new Configuration().configure().buildSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        Session session = factory.openSession();
        Transaction tx = null;
        Integer noPesanan = null;
        Integer noDetailPesanan= null;
        try {
            tx=session.beginTransaction();
            noPesanan = (Integer) session.save(pesanan);
            for (DetailPesanan detailPesanan : listDetailPesanan) {
              detailPesanan.setPesanan(pesanan);
              session.save(detailPesanan);
            }
            
            tx.commit();
        }catch (HibernateException e){
            if(tx!=null){
                tx.rollback();
            }
            e.printStackTrace();
        }finally {
            session.close();
        }
        return noPesanan;
    }



    public boolean update(Pesanan pesanan, List<DetailPesanan> listDetailPesanan){
        try{
            factory = new Configuration().configure().buildSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        Session session = factory.openSession();
        Transaction tx = null;
        Integer noPesanan = null;
        Integer noDetailPesanan= null;
        try {
            tx=session.beginTransaction();
            System.out.println("hapus:" + pesanan.getIdPesanan());
            Iterator<DetailPesanan> detailPesananIterator = pesanan.getDetailPesanans().iterator();
            while(detailPesananIterator.hasNext()){
                DetailPesanan setDetailPesanan = detailPesananIterator.next();
                System.out.println(setDetailPesanan.getIdDetailPesanan());
                session.delete(setDetailPesanan);

            }
            for (DetailPesanan detailPesanan : listDetailPesanan) {
                session.save(detailPesanan);
            }

            tx.commit();
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

    public boolean delete(Integer idPesanan){
        try{
            factory = new Configuration().configure().buildSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        Session session = factory.openSession();
        Transaction tx = null;
        Integer noPesanan = null;
        Integer noDetailPesanan= null;
        try {
            tx=session.beginTransaction();
            Pesanan pesanan= new Pesanan();
            session.delete(pesanan);
            DetailPesanan detailPesanan = new DetailPesanan();
            noPesanan = (Integer) session.save(pesanan);
            detailPesanan.setPesanan(pesanan);
            session.delete(detailPesanan);
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
}
