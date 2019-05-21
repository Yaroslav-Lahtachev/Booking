package com.pingvin.autoservice.dao;

import com.pingvin.autoservice.entity.*;
//import com.pingvin.autoservice.entity.OrderHistory;
//import com.pingvin.autoservice.model.OrderHistoryInfo;
import com.pingvin.autoservice.model.OrderInfo;
import com.pingvin.autoservice.pagination.PaginationResult;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Repository
@Transactional
public class OrderDAO {
    @Autowired
    private SessionFactory sessionFactory;

    public Order findOrderByIdOrder(int idOrder) {
        Session session = this.sessionFactory.getCurrentSession();
        return session.get(Order.class, idOrder);
    }

    public PaginationResult<OrderInfo> findAllOrders(int page, int maxResult, int maxNavPage) {
        Session session = this.sessionFactory.getCurrentSession();
        String sql = "Select new " + OrderInfo.class.getName()
                + " (e.id, e.customer.id, e.offer.id, e.master.id, e.needKit, e.dateStart, e.dateFinish) "
                + " From " + Order.class.getName() + " e ";
        Query<OrderInfo> query = session.createQuery(sql, OrderInfo.class);
        return new PaginationResult<OrderInfo>(query, page, maxResult, maxNavPage);
    }

    public PaginationResult<OrderInfo> findOrderByCustomer(User user, int page, int maxResult, int maxNavPage) {
        Session session = this.sessionFactory.getCurrentSession();
        String sql = "Select new " + OrderInfo.class.getName()
                + " (o.id, o.offer.id, f.name, f.price, m.name, o.dateStart, o.dateFinish, o.status, o.needKit) "
                + " From " + Order.class.getName() + " as o, " + Offer.class.getName() + " as f, " + Master.class.getName() + " as m "
                + " where f.id=o.offer and o.customer.id =: idUser ";
        Query<OrderInfo> query = session.createQuery(sql, OrderInfo.class);
        query.setParameter("idUser", user.getIdUser());
        return new PaginationResult<OrderInfo>(query, page, maxResult, maxNavPage);
    }

    public void removeOrder(int OrderId) {
        Session session = this.sessionFactory.getCurrentSession();
        Order order = session.get(Order.class, OrderId);
        session.remove(order);
    }

    public void reserve(User customer, Master master, Offer offer, int needParts, Date dateStart, Date dateFinish) {
        Session session = this.sessionFactory.getCurrentSession();
        Order order = new Order();
        order.setCustomer(customer);
        order.setOffer(offer);
        order.setMaster(master);
        order.setNeedKit(needParts);
        order.setDateStart(dateStart);
        order.setDateFinish(dateFinish);
        order.setStatus("CREATED");
        session.persist(order);
        session.flush();
    }

    public void changeOrderStatus(int idOrder, String status) {
        Order order = findOrderByIdOrder(idOrder);
        order.setStatus(status);
    }

    public void changeOrderDate(int idOrder, Date date) {
        Order order = findOrderByIdOrder(idOrder);
        order.setDateFinish(date);
    }
}
