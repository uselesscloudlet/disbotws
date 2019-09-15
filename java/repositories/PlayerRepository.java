package repositories;

import config.HibernateUtil;
import entities.Player;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class PlayerRepository {
    private Transaction transaction = null;
    private Session session = null;
    private Player player = null;

    //Get All Players
    public List<Player> getAllPlayers(){
        List<Player> list = new ArrayList<>();
        try {

            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Player> cq = cb.createQuery(Player.class);
            Root<Player> rootEntry = cq.from(Player.class);
            CriteriaQuery<Player> all = cq.select(rootEntry);

            TypedQuery<Player> allQuery = session.createQuery(all);
            list = allQuery.getResultList();
            session.clear();
            session.close();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();

        }

        return list;
    }


    //Get Single Player
    public Player getPlayerByDiscordId(long discord_id){
        try {
            session = HibernateUtil.getSessionFactory().openSession();

            // start a transaction
            transaction = session.beginTransaction();

            Query query = session.createQuery("from Player l where l.discord_id = :discord_id");
            query.setParameter("discord_id", discord_id);
            player = (Player) query.uniqueResult();

            session.clear();
            session.close();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

        return player;
    }

    //Save Single Player
    public void savePlayer(Player player) {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            session.saveOrUpdate(player);

            transaction.commit();

            session.clear();
            session.close();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public boolean checkIfPlayerExist (long discord_id) {
        try {
            session = HibernateUtil.getSessionFactory().openSession();

            // start a transaction
            transaction = session.beginTransaction();

            Query query = session.createQuery("from Player player where player.discord_id = :discord_id");
            query.setParameter("discord_id", discord_id);
            player = (Player) query.uniqueResult();

            session.clear();
            session.close();

            return (player != null);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

}
