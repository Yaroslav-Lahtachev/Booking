package com.pingvin.autoservice.dao;

import com.pingvin.autoservice.config.Consts;
import com.pingvin.autoservice.entity.*;
//import com.pingvin.autoservice.entity.OrderHistory;
//import com.pingvin.autoservice.model.OrderHistoryInfo;
import com.pingvin.autoservice.model.OffersInfo;
import com.pingvin.autoservice.model.OrderInfo;
import com.pingvin.autoservice.pagination.PaginationResult;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
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
                + " where f.id=o.offer and o.customer.id =: idUser and m.id = o.master and o.status !=: text_status";
        Query<OrderInfo> query = session.createQuery(sql, OrderInfo.class);
        query.setParameter("text_status", Consts.WAITING_FOR_RESULTION_STATUS);
        query.setParameter("idUser", user.getIdUser());
        return new PaginationResult<OrderInfo>(query, page, maxResult, maxNavPage);
    }

    public PaginationResult<OffersInfo> findOfferByCustomerByStatus(User user, String status, int page, int maxResult, int maxNavPage) {
        Session session = this.sessionFactory.getCurrentSession();
        String sql = "Select new " + OffersInfo.class.getName()
                + " (f.id, f.name, f.prof, f.price, f.time, p.name ) "
                + " from "  + Order.class.getName() + " as o, " + Offer.class.getName() + " as f, " + Parts.class.getName() + " as p "
                + " where p.id =f.kit and o.customer.id =: idUser and o.status =: text_status and o.offer = f.id order by f.id ";
        Query<OffersInfo> query = session.createQuery(sql, OffersInfo.class);
        query.setParameter("text_status", status);
        query.setParameter("idUser", user.getIdUser());
        return new PaginationResult<OffersInfo>(query, page, maxResult, maxNavPage);
    }

    public void removeOrder(int OrderId) {
        Session session = this.sessionFactory.getCurrentSession();
        Order order = session.get(Order.class, OrderId);
        session.remove(order);
    }

    public void removeOrders(List<Order> orders) {
        Session session = this.sessionFactory.getCurrentSession();
        for (int i = 0; i < orders.size(); i++) {
            session.remove(orders.get(i));
        }
    }

    public void removeOrdersByStatus(String status, int userId) {
       removeOrders(this.findOrderByStatus(status, userId));
    }

    public void reserve(User customer, Master master, Offer offer, int needParts, Date dateStart, Date dateFinish, String status) {
        Session session = this.sessionFactory.getCurrentSession();
        Order order = new Order();
        order.setCustomer(customer);
        order.setOffer(offer);
        order.setMaster(master);
        order.setNeedKit(needParts);
        order.setDateStart(dateStart);
        order.setDateFinish(dateFinish);
        order.setStatus(status);
        session.persist(order);
        session.flush();
    }

    public List<Order> findOrderByCustomer(int idUser) {
        Session session = this.sessionFactory.getCurrentSession();
        String sql = "from Order where customer.id =: idUser";
        Query<Order> query = session.createQuery(sql, Order.class);
        query.setParameter("idUser", idUser);
        return query.list();
    }

    public List<Order> findOrderByStatus(String status, int idUser) {
        Session session = this.sessionFactory.getCurrentSession();
        String sql = "from Order where customer.id =: idUser and status =: status";
        Query<Order> query = session.createQuery(sql, Order.class);
        query.setParameter("idUser", idUser);
        query.setParameter("status", status);
        return query.list();
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
