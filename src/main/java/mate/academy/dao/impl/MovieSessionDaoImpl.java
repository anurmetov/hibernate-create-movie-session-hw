package mate.academy.dao.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import mate.academy.dao.MovieSessionDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.MovieSession;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

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
    public Optional<MovieSession> get(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            MovieSession foundedMovieSession = session.get(MovieSession.class, id);
            transaction.commit();
            return Optional.ofNullable(foundedMovieSession);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can not get MovieSession from a DB by id: " + id, e);
        }
    }

    @Override
    public List<MovieSession> findAvailableSessions(Long movieId, LocalDate date) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Query<MovieSession> getAllMoviesQuery = session.createQuery(
                    "from MovieSession ms "
                            + "where ms.movie.id = :movieId "
                            + "and ms.showTime between :start and :end",
                    MovieSession.class);

            getAllMoviesQuery.setParameter("movieId", movieId);
            getAllMoviesQuery.setParameter("start", date.atStartOfDay());
            getAllMoviesQuery.setParameter("end", date.atTime(23, 59, 59));

            return getAllMoviesQuery.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get all available movies. ", e);

        }
    }
}
