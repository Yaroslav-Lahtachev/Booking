package com.pingvin.autoservice.test;

import com.pingvin.autoservice.dao.OrderDAO;
import com.pingvin.autoservice.entity.*;
import com.pingvin.autoservice.model.OffersInfo;
import com.pingvin.autoservice.model.OrderInfo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDAOTest {
    @Autowired
    private SessionFactory sessionFactory;

    @Test
    @Transactional
    @Rollback(true)
    public void findOrderByIdWhenIdNotExists() {
        Order orderActual = new OrderDAO().findOrderByIdOrder(9999);
        Assert.assertNull(orderActual);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findOrderByIdWhenIdExists() {
        Session session = this.sessionFactory.getCurrentSession();
        Master master = new Master(1,"test","test",1);
        session.persist(master);
        User user = new User(1,"test","test","test","test");
        session.persist(user);
        Parts parts = new Parts(1,"test","test",9999);
        session.persist(parts);
        Offer offer = new Offer(1,"test",9999,"test",9999, parts);
        session.persist(offer);
        Order order1 = new Order(
                9998, user, offer,
                new Date(1970,01,01),
                new Date(1970,01,02),
                master, 1, "test"
        );
        session.persist(order1);
        session.flush();

        Assert.assertEquals(order1, new OrderDAO().findOrderByIdOrder(9998));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findAllOrdersWhenOrdersNotExists() {
        Assert.assertNull(new OrderDAO().findAllOrders(1,1,10));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findAllOrdersWhenOrdersExists() {
        Session session = this.sessionFactory.getCurrentSession();
        Master master = new Master(1,"test","test",1);
        session.persist(master);
        User user = new User(1,"test","test","test","test");
        session.persist(user);
        Parts parts = new Parts(1,"test","test",9999);
        session.persist(parts);
        Offer offer = new Offer(1,"test",9999,"test",9999, parts);
        session.persist(offer);
        Order order1 = new Order(
                9998, user, offer,
                new Date(1970,01,01),
                new Date(1970,01,02),
                master, 1, "test"
        );
        session.persist(order1);
        Order order2 = new Order(
                9999, user, offer,
                new Date(1970,01,03),
                new Date(1970,01,04),
                master, 1, "test"
        );
        session.persist(order2);
        session.flush();

        OrderInfo orderInfo = new OrderInfo(order2);
        Assert.assertEquals(orderInfo, new OrderDAO().findAllOrders(2,1,10).getList().get(0));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findOrderByCustomerWhenCustomerNotHaveOrders() {
        Session session = this.sessionFactory.getCurrentSession();
        User user = new User(1,"test","test","test","test");
        session.persist(user);
        Assert.assertNull(new OrderDAO().findOrderByCustomer(user,1,1,10));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findOrderByCustomerWhenCustomerHaveOrders() {
        Session session = this.sessionFactory.getCurrentSession();
        Master master = new Master(1,"test","test",1);
        session.persist(master);
        User user = new User(1,"test","test","test","test");
        session.persist(user);
        Parts parts = new Parts(1,"test","test",9999);
        session.persist(parts);
        Offer offer = new Offer(1,"test",9999,"test",9999, parts);
        session.persist(offer);
        Order order1 = new Order(
                9998, user, offer,
                new Date(1970,01,01),
                new Date(1970,01,02),
                master, 1, "test"
        );
        session.persist(order1);
        Order order2 = new Order(
                9999, user, offer,
                new Date(1970,01,03),
                new Date(1970,01,04),
                master, 1, "test"
        );
        session.persist(order2);
        session.flush();

        OrderInfo orderInfo = new OrderInfo(order2);
        Assert.assertEquals(orderInfo, new OrderDAO().findOrderByCustomer(user,2,1,10).getList().get(0));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findOfferByCustomerByStatusWhenNoSuchStatus() {
        Session session = this.sessionFactory.getCurrentSession();
        Master master = new Master(1,"test","test",1);
        session.persist(master);
        User user = new User(1,"test","test","test","test");
        session.persist(user);
        Parts parts = new Parts(1,"test","test",9999);
        session.persist(parts);
        Offer offer = new Offer(1,"test",9999,"test",9999, parts);
        session.persist(offer);
        Order order1 = new Order(
                9998, user, offer,
                new Date(1970,01,01),
                new Date(1970,01,02),
                master, 1, "test"
        );
        session.persist(order1);
        Order order2 = new Order(
                9999, user, offer,
                new Date(1970,01,03),
                new Date(1970,01,04),
                master, 1, "test"
        );
        session.persist(order2);
        session.flush();

        Assert.assertNull(new OrderDAO().findOfferByCustomerByStatus(user,"test2",1,1,10));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findOfferByCustomerByStatusWhenOrderWithSuchStatusExists() {
        Session session = this.sessionFactory.getCurrentSession();
        Master master = new Master(1,"test","test",1);
        session.persist(master);
        User user = new User(1,"test","test","test","test");
        session.persist(user);
        Parts parts = new Parts(1,"test","test",9999);
        session.persist(parts);
        Offer offer = new Offer(1,"test",9999,"test",9999, parts);
        session.persist(offer);
        Order order1 = new Order(
                9998, user, offer,
                new Date(1970,01,01),
                new Date(1970,01,02),
                master, 1, "test"
        );
        session.persist(order1);
        Order order2 = new Order(
                9999, user, offer,
                new Date(1970,01,03),
                new Date(1970,01,04),
                master, 1, "test2"
        );
        session.persist(order2);
        session.flush();

        OffersInfo offersInfo = new OffersInfo(offer);
        Assert.assertEquals(offersInfo, new OrderDAO().findOfferByCustomerByStatus(user,"test2",1,1,10).getList().get(0));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void removeOrderWhenOrderExists() {
        Session session = this.sessionFactory.getCurrentSession();
        Master master = new Master(1,"test","test",1);
        session.persist(master);
        User user = new User(1,"test","test","test","test");
        session.persist(user);
        Parts parts = new Parts(1,"test","test",9999);
        session.persist(parts);
        Offer offer = new Offer(1,"test",9999,"test",9999, parts);
        session.persist(offer);
        Order order1 = new Order(
                9998, user, offer,
                new Date(1970,01,01),
                new Date(1970,01,02),
                master, 1, "test"
        );
        session.persist(order1);
        session.flush();

        new OrderDAO().removeOrder(order1);
        Assert.assertNull(session.get(Order.class, 9998));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void removeOrders() {
        Session session = this.sessionFactory.getCurrentSession();
        Master master = new Master(1,"test","test",1);
        session.persist(master);
        User user = new User(1,"test","test","test","test");
        session.persist(user);
        Parts parts = new Parts(1,"test","test",9999);
        session.persist(parts);
        Offer offer = new Offer(1,"test",9999,"test",9999, parts);
        session.persist(offer);
        List<Order> list = new ArrayList<>();
        Order order1 = new Order(
                9998, user, offer,
                new Date(1970,01,01),
                new Date(1970,01,02),
                master, 1, "test"
        );
        list.add(order1);
        session.persist(order1);
        Order order2 = new Order(
                9999, user, offer,
                new Date(1970,01,03),
                new Date(1970,01,04),
                master, 1, "test2"
        );
        list.add(order2);
        session.persist(order2);
        session.flush();

        new OrderDAO().removeOrders(list);
        Assert.assertNull(session.get(Order.class, 9998));
        Assert.assertNull(session.get(Order.class, 9999));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void removeOrdersByStatus() {
        Session session = this.sessionFactory.getCurrentSession();
        Master master = new Master(1,"test","test",1);
        session.persist(master);
        User user = new User(1,"test","test","test","test");
        session.persist(user);
        Parts parts = new Parts(1,"test","test",9999);
        session.persist(parts);
        Offer offer = new Offer(1,"test",9999,"test",9999, parts);
        session.persist(offer);
        List<Order> list = new ArrayList<>();
        Order order1 = new Order(
                9998, user, offer,
                new Date(1970,01,01),
                new Date(1970,01,02),
                master, 1, "test"
        );
        list.add(order1);
        session.persist(order1);
        session.flush();

        new OrderDAO().removeOrdersByStatus("test",1);
        Assert.assertNull(session.get(Order.class, 9998));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void reserve() {
        Session session = this.sessionFactory.getCurrentSession();
        Master master = new Master(1,"test","test",1);
        session.persist(master);
        User user = new User(1,"test","test","test","test");
        session.persist(user);
        Parts parts = new Parts(1,"test","test",9999);
        session.persist(parts);
        Offer offer = new Offer(1,"test",9999,"test",9999, parts);
        session.persist(offer);
        Date startDate = new Date(1970,01,01);
        Date finishDate = new Date(1970,01,02);
        Order order1 = new Order(
                1, user, offer,
                startDate, finishDate,
                master, 1, "test"
        );

        new OrderDAO().reserve(user,master,offer,1,startDate,finishDate,"test");
        Assert.assertEquals(order1, session.get(Order.class, 1));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findOrdersAsListByCustomerWhenNoOrders() {
        Assert.assertNull(new OrderDAO().findOrderByCustomer(1));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findOrdersAsListByCustomerWhenOrderExists() {
        Session session = this.sessionFactory.getCurrentSession();
        Master master = new Master(1,"test","test",1);
        session.persist(master);
        User user = new User(1,"test","test","test","test");
        session.persist(user);
        Parts parts = new Parts(1,"test","test",9999);
        session.persist(parts);
        Offer offer = new Offer(1,"test",9999,"test",9999, parts);
        session.persist(offer);
        List<Order> list = new ArrayList<>();
        Order order1 = new Order(
                9998, user, offer,
                new Date(1970,01,01),
                new Date(1970,01,02),
                master, 1, "test"
        );
        list.add(order1);
        session.persist(order1);
        Order order2 = new Order(
                9999, user, offer,
                new Date(1970,01,03),
                new Date(1970,01,04),
                master, 1, "test2"
        );
        list.add(order2);
        session.persist(order2);
        session.flush();

        Assert.assertEquals(list, new OrderDAO().findOrderByCustomer(1));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findOrderByStatusWhenNoSuchStatus() {
        Session session = this.sessionFactory.getCurrentSession();
        Master master = new Master(1,"test","test",1);
        session.persist(master);
        User user = new User(1,"test","test","test","test");
        session.persist(user);
        Parts parts = new Parts(1,"test","test",9999);
        session.persist(parts);
        Offer offer = new Offer(1,"test",9999,"test",9999, parts);
        session.persist(offer);
        List<Order> list = new ArrayList<>();
        Order order1 = new Order(
                9998, user, offer,
                new Date(1970,01,01),
                new Date(1970,01,02),
                master, 1, "test"
        );
        list.add(order1);
        session.persist(order1);
        Order order2 = new Order(
                9999, user, offer,
                new Date(1970,01,03),
                new Date(1970,01,04),
                master, 1, "test2"
        );
        list.add(order2);
        session.persist(order2);
        session.flush();

        Assert.assertNull(new OrderDAO().findOrderByStatus("test3",1));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findOrderByStatusWhenOrderWithSuchStatusExists() {
        Session session = this.sessionFactory.getCurrentSession();
        Master master = new Master(1,"test","test",1);
        session.persist(master);
        User user = new User(1,"test","test","test","test");
        session.persist(user);
        Parts parts = new Parts(1,"test","test",9999);
        session.persist(parts);
        Offer offer = new Offer(1,"test",9999,"test",9999, parts);
        session.persist(offer);
        List<Order> list = new ArrayList<>();
        Order order1 = new Order(
                9998, user, offer,
                new Date(1970,01,01),
                new Date(1970,01,02),
                master, 1, "test"
        );
        list.add(order1);
        session.persist(order1);
        Order order2 = new Order(
                9999, user, offer,
                new Date(1970,01,03),
                new Date(1970,01,04),
                master, 1, "test"
        );
        list.add(order2);
        session.persist(order2);
        session.flush();

        Assert.assertEquals(list, new OrderDAO().findOrderByStatus("test",1));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void changeOrderStatusWhenOrderExists() {
        Session session = this.sessionFactory.getCurrentSession();
        Master master = new Master(1,"test","test",1);
        session.persist(master);
        User user = new User(1,"test","test","test","test");
        session.persist(user);
        Parts parts = new Parts(1,"test","test",9999);
        session.persist(parts);
        Offer offer = new Offer(1,"test",9999,"test",9999, parts);
        session.persist(offer);
        Order order1 = new Order(
                9998, user, offer,
                new Date(1970,01,01),
                new Date(1970,01,02),
                master, 1, "test"
        );
        session.persist(order1);
        session.flush();

        new OrderDAO().changeOrderStatus(9998,"test1");
        Assert.assertEquals("test1", session.get(Order.class, 9998).getStatus());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void changeOrderDate() {
        Session session = this.sessionFactory.getCurrentSession();
        Master master = new Master(1,"test","test",1);
        session.persist(master);
        User user = new User(1,"test","test","test","test");
        session.persist(user);
        Parts parts = new Parts(1,"test","test",9999);
        session.persist(parts);
        Offer offer = new Offer(1,"test",9999,"test",9999, parts);
        session.persist(offer);
        Order order1 = new Order(
                9998, user, offer,
                new Date(1970,01,01),
                new Date(1970,01,02),
                master, 1, "test"
        );
        session.persist(order1);
        session.flush();

        Date newDate = new Date(1999,10,1);
        new OrderDAO().changeOrderDate(9998, newDate);
        Assert.assertEquals(newDate, session.get(Order.class, 9998).getDateFinish());
    }
}