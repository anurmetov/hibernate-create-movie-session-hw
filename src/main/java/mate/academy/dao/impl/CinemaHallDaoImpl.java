package mate.academy.dao.impl;

import jakarta.persistence.Query;
import java.util.List;
import java.util.Optional;
import mate.academy.dao.CinemaHallDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.CinemaHall;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Dao
public class CinemaHallDaoImpl implements CinemaHallDao {
    @Override
    public CinemaHall add(CinemaHall cinemaHall) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            session.persist(cinemaHall);
            transaction.commit();
            return cinemaHall;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can not add CinemaHall to a DB", e);
        }
    }

    @Override
    public Optional<CinemaHall> get(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            CinemaHall foundedCinemaHall = session.get(CinemaHall.class, id);
            transaction.commit();
            return Optional.ofNullable(foundedCinemaHall);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can not add CinemaHall to a DB", e);
        }
    }

    @Override
    public List<CinemaHall> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query getAllMoviesQuery = session.createQuery("from CinemaHall ", CinemaHall.class);
            List<CinemaHall> resultList = getAllMoviesQuery.getResultList();
            return resultList;
        } catch (Exception e) {
            throw new DataProcessingException("Can't get all Cinema Halls. ", e);

        }
    }
}
