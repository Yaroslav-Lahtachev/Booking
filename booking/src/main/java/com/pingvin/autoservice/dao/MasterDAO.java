package com.pingvin.autoservice.dao;

import com.pingvin.autoservice.entity.Master;
import com.pingvin.autoservice.entity.Offer;
import com.pingvin.autoservice.entity.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
@Transactional
public class MasterDAO {
    @Autowired
    private SessionFactory sessionFactory;

    public MasterDAO() {
    }

    public Master findByIdMaster(int id) {
        Session session = this.sessionFactory.getCurrentSession();
        return session.get(Master.class, id);
    }

    public Master getFreeMaster(Offer offer, Date dateStart, Date dateFinish) {
        Session session = this.sessionFactory.getCurrentSession();
        String prof = offer.getProf();
        String sql = "from Master where prof =: prof and occupied=0";
        Query<Master> query = session.createQuery(sql, Master.class);
        query.setParameter("prof", prof);
        if (!query.list().isEmpty()) {
            return query.list().get(0);
        } else {

            sql = "Select count(m.id) from Master as m where m.prof =: prof and m.occupied=1";

            Query<Long> query1 = session.createQuery(sql, Long.class);
            query1.setParameter("prof", prof);
            Long value = query1.getSingleResult();
            if (value != null && value != 0) {
                sql = "Select distinct m.id from " + Master.class.getName() +" as m, "+ Order.class.getName() +
                        " as o where m.prof =: prof and m.occupied=1 and o.master.id=m.id and o.dateFinish <=: dateFinish and o.dateStart >=: dateStart";
                Query<Integer> query2 = session.createQuery(sql, Integer.class);
                query2.setParameter("prof", prof);
                query2.setParameter("dateStart", dateStart);
                query2.setParameter("dateFinish", dateFinish);
                List<Integer> busyInThatTime = query2.list();
                sql = "Select m.id from Master as m where m.prof =: prof and m.occupied=1";
                query2 = session.createQuery(sql, Integer.class);
                query2.setParameter("prof", prof);
                List<Integer> allAvailableMasters = query2.list();
                allAvailableMasters.removeAll(busyInThatTime);
                if (!allAvailableMasters.isEmpty()) {
                    return findByIdMaster(allAvailableMasters.get(0));
                }
            }
            return findByIdMaster(1);
        }
    }

    public void checkIfMasterIsFree(int orderID, int masterId) {
        Session session = this.sessionFactory.getCurrentSession();
        String sql = "Select count(o.id) from Order as o, Master as m where o.master=m.id and m.id =: masterID and o.id != : orderID and o.status != 'DONE'";
        Query<Long> query = session.createQuery(sql, Long.class);
        query.setParameter("masterID", masterId);
        query.setParameter("orderID", orderID);
        Master master = findByIdMaster(masterId);
        Long value = query.getSingleResult();
        if (value == null || value == 0) {
            master.setOccupied(0);
        }
    }
}