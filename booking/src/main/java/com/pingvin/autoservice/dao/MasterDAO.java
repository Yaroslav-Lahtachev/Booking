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
            sql = "from Master as m, Order as o where m.prof =: prof and m.occupied=1 and o.master=m.id and o.finishDate <=: dateFinish and o.dateStart >=: dateStart";
            query = session.createQuery(sql, Master.class);
            query.setParameter("prof", prof);
            query.setParameter("dateStart", dateStart);
            query.setParameter("dateFinish", dateFinish);
            List<Master> busyInThatTime = query.list();
            sql = "from Master as m, Order as o where m.prof =: prof and m.occupied=1";
            query = session.createQuery(sql, Master.class);
            query.setParameter("prof", prof);
            List<Master> allAvailableMasters = query.list();
            allAvailableMasters.removeAll(busyInThatTime);
            if (!allAvailableMasters.isEmpty()) {
                return allAvailableMasters.get(0);
            }
            return findByIdMaster(1);
        }
    }

    public void checkIfMasterIsFree(int orderID, Master master) {
        Session session = this.sessionFactory.getCurrentSession();
        String sql = "from Order as o where o.master=m.id and m.id =: masterID and o.id != : orderID";
        Query<Order> query = session.createQuery(sql, Order.class);
        query.setParameter("masterID", master.getMaster());
        query.setParameter("orderID", orderID);
        if (query.list().isEmpty()) {
            master.setOccupied(0);
        }
    }
}