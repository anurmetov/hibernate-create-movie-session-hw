package mate.academy.dao.impl;

import jakarta.persistence.Query;
import java.time.LocalDate;
import java.util.List;
import mate.academy.dao.MovieSessionDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;

import mate.academy.model.MovieSession;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Dao
public class MovieSessionDaoImpl implements MovieSessionDao {
    @Override
    public MovieSession add(MovieSession movieSession) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(movieSession);
            transaction.commit();
            return movieSession;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't insert movieSession " + movieSession, e);
        }
    }

    @Override
    public MovieSession get(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            MovieSession foundedMovieSession = session.get(MovieSession.class, id);
            transaction.commit();
            return foundedMovieSession;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can not get MovieSession from a DB", e);
        }
    }

    @Override
    public List<MovieSession> findAvailableSessions(Long movieId, LocalDate date) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query getAllMoviesQuery = session.createQuery(
                    "from MovieSession ms where ms.showTime <= :date", MovieSession.class);
            getAllMoviesQuery.setParameter("date", date);
            List<MovieSession> resultList = getAllMoviesQuery.getResultList();
            return resultList;
        } catch (Exception e) {
            throw new DataProcessingException("Can't get all available movies. ", e);

        }
    }
}
