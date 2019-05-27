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
    @Autowired
    private OrderDAO orderDAO;

    @Test
    @Transactional
    @Rollback(true)
    public void findOrderByIdWhenIdNotExists() {
        Assert.assertNull(orderDAO.findOrderByIdOrder(9999));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findOrderByIdWhenIdExists() {
        Session session = this.sessionFactory.getCurrentSession();
        Master master = new Master("test","test",1);
        session.persist(master);
        User user = new User("test","test","test","test");
        session.persist(user);
        Parts parts = new Parts("test","test",9999);
        session.persist(parts);
        Offer offer = new Offer("test",9999,"test",9999, parts);
        session.persist(offer);
        Order order1 = new Order(
                user, offer,
                new Date(1970,01,01),
                new Date(1970,01,02),
                master, 1, "test"
        );
        session.persist(order1);
        session.flush();

        Assert.assertEquals(order1, orderDAO.findOrderByIdOrder(order1.getIdOrder()));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findAllOrdersWhenOrdersNotExists() {
        Assert.assertEquals(0, orderDAO.findAllOrders(1,1,10).getList().size());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findAllOrdersWhenOrdersExists() {
        Session session = this.sessionFactory.getCurrentSession();
        Master master = new Master("test","test",1);
        session.persist(master);
        User user = new User("test","test","test","test");
        session.persist(user);
        Parts parts = new Parts("test","test",9999);
        session.persist(parts);
        Offer offer = new Offer("test",9999,"test",9999, parts);
        session.persist(offer);
        Order order1 = new Order(
                user, offer,
                new Date(1970,01,01),
                new Date(1970,01,02),
                master, 1, "test"
        );
        session.persist(order1);
        Order order2 = new Order(
                user, offer,
                new Date(1970,01,03),
                new Date(1970,01,04),
                master, 1, "test"
        );
        session.persist(order2);
        session.flush();

        Assert.assertEquals(2, orderDAO.findAllOrders(1,2,10).getList().size());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findOrderByCustomerWhenCustomerNotHaveOrders() {
        Session session = this.sessionFactory.getCurrentSession();
        User user = new User("test","test","test","test");
        session.persist(user);
        Assert.assertEquals(0, orderDAO.findOrderByCustomer(user,1,1,10).getList().size());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findOrderByCustomerWhenCustomerHaveOrders() {
        Session session = this.sessionFactory.getCurrentSession();
        Master master = new Master("test","test",1);
        session.persist(master);
        User user = new User("test","test","test","test");
        session.persist(user);
        Parts parts = new Parts("test","test",9999);
        session.persist(parts);
        Offer offer = new Offer("test",9999,"test",9999, parts);
        session.persist(offer);
        Order order1 = new Order(
                user, offer,
                new Date(1970,01,01),
                new Date(1970,01,02),
                master, 1, "test"
        );
        session.persist(order1);
        Order order2 = new Order(
                user, offer,
                new Date(1970,01,03),
                new Date(1970,01,04),
                master, 1, "test"
        );
        session.persist(order2);
        session.flush();

        Assert.assertEquals(1, orderDAO.findOrderByCustomer(user,2,1,10).getList().size());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findOfferByCustomerByStatusWhenNoSuchStatus() {
        Session session = this.sessionFactory.getCurrentSession();
        Master master = new Master("test","test",1);
        session.persist(master);
        User user = new User("test","test","test","test");
        session.persist(user);
        Parts parts = new Parts("test","test",9999);
        session.persist(parts);
        Offer offer = new Offer("test",9999,"test",9999, parts);
        session.persist(offer);
        Order order1 = new Order(
                user, offer,
                new Date(1970,01,01),
                new Date(1970,01,02),
                master, 1, "test"
        );
        session.persist(order1);
        Order order2 = new Order(
                user, offer,
                new Date(1970,01,03),
                new Date(1970,01,04),
                master, 1, "test"
        );
        session.persist(order2);
        session.flush();

        Assert.assertEquals(0, orderDAO.findOfferByCustomerByStatus(user,"test2",1,1,10).getList().size());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findOfferByCustomerByStatusWhenOrderWithSuchStatusExists() {
        Session session = this.sessionFactory.getCurrentSession();
        Master master = new Master("test","test",1);
        session.persist(master);
        User user = new User("test","test","test","test");
        session.persist(user);
        Parts parts = new Parts("test","test",9999);
        session.persist(parts);
        Offer offer = new Offer("test",9999,"test",9999, parts);
        session.persist(offer);
        Order order1 = new Order(
                user, offer,
                new Date(1970,01,01),
                new Date(1970,01,02),
                master, 1, "test"
        );
        session.persist(order1);
        Order order2 = new Order(
                user, offer,
                new Date(1970,01,03),
                new Date(1970,01,04),
                master, 1, "test2"
        );
        session.persist(order2);
        session.flush();

        OffersInfo offersInfo = new OffersInfo(offer);
        Assert.assertEquals(offersInfo, orderDAO.findOfferByCustomerByStatus(user,"test2",1,1,10).getList().get(0));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void removeOrderWhenOrderExists() {
        Session session = this.sessionFactory.getCurrentSession();
        Master master = new Master("test","test",1);
        session.persist(master);
        User user = new User("test","test","test","test");
        session.persist(user);
        Parts parts = new Parts("test","test",9999);
        session.persist(parts);
        Offer offer = new Offer("test",9999,"test",9999, parts);
        session.persist(offer);
        Order order1 = new Order(
                user, offer,
                new Date(1970,01,01),
                new Date(1970,01,02),
                master, 1, "test"
        );
        session.persist(order1);
        session.flush();

        orderDAO.removeOrder(order1);
        Assert.assertNull(session.get(Order.class, order1.getIdOrder()));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void removeOrders() {
        Session session = this.sessionFactory.getCurrentSession();
        Master master = new Master("test","test",1);
        session.persist(master);
        User user = new User("test","test","test","test");
        session.persist(user);
        Parts parts = new Parts("test","test",9999);
        session.persist(parts);
        Offer offer = new Offer("test",9999,"test",9999, parts);
        session.persist(offer);
        List<Order> list = new ArrayList<>();
        Order order1 = new Order(
                user, offer,
                new Date(1970,01,01),
                new Date(1970,01,02),
                master, 1, "test"
        );
        list.add(order1);
        session.persist(order1);
        Order order2 = new Order(
                user, offer,
                new Date(1970,01,03),
                new Date(1970,01,04),
                master, 1, "test2"
        );
        list.add(order2);
        session.persist(order2);
        session.flush();

        orderDAO.removeOrders(list);
        Assert.assertNull(session.get(Order.class, order1.getIdOrder()));
        Assert.assertNull(session.get(Order.class, order2.getIdOrder()));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void removeOrdersByStatus() {
        Session session = this.sessionFactory.getCurrentSession();
        Master master = new Master("test","test",1);
        session.persist(master);
        User user = new User("test","test","test","test");
        session.persist(user);
        Parts parts = new Parts("test","test",9999);
        session.persist(parts);
        Offer offer = new Offer("test",9999,"test",9999, parts);
        session.persist(offer);
        List<Order> list = new ArrayList<>();
        Order order1 = new Order(
                user, offer,
                new Date(1970,01,01),
                new Date(1970,01,02),
                master, 1, "test"
        );
        list.add(order1);
        session.persist(order1);
        session.flush();

        orderDAO.removeOrdersByStatus("test",user.getIdUser());
        Assert.assertNull(session.get(Order.class, order1.getIdOrder()));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void reserve() {
        Session session = this.sessionFactory.getCurrentSession();
        Master master = new Master("test","test",0);
        session.persist(master);
        User user = new User("test","test","test","test");
        session.persist(user);
        Parts parts = new Parts("test","test",9999);
        session.persist(parts);
        Offer offer = new Offer("test",9999,"test",9999, parts);
        session.persist(offer);
        Date startDate = new Date(1970,01,01);
        Date finishDate = new Date(1970,01,02);

        orderDAO.reserve(user,master,offer,1,startDate,finishDate,"test");
        Assert.assertEquals(1, master.getOccupied());
        Assert.assertEquals(1, orderDAO.findOrderByStatus("test", user.getIdUser()).size());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findOrdersAsListByCustomerWhenNoOrders() {
        Assert.assertEquals(0, orderDAO.findOrderByCustomer(1).size());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findOrdersAsListByCustomerWhenOrderExists() {
        Session session = this.sessionFactory.getCurrentSession();
        Master master = new Master("test","test",1);
        session.persist(master);
        User user = new User("test","test","test","test");
        session.persist(user);
        Parts parts = new Parts("test","test",9999);
        session.persist(parts);
        Offer offer = new Offer("test",9999,"test",9999, parts);
        session.persist(offer);
        List<Order> list = new ArrayList<>();
        Order order1 = new Order(
                user, offer,
                new Date(1970,01,01),
                new Date(1970,01,02),
                master, 1, "test"
        );
        list.add(order1);
        session.persist(order1);
        Order order2 = new Order(
                user, offer,
                new Date(1970,01,03),
                new Date(1970,01,04),
                master, 1, "test2"
        );
        list.add(order2);
        session.persist(order2);
        session.flush();

        Assert.assertEquals(list, orderDAO.findOrderByCustomer(user.getIdUser()));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findOrderByStatusWhenNoSuchStatus() {
        Session session = this.sessionFactory.getCurrentSession();
        Master master = new Master("test","test",1);
        session.persist(master);
        User user = new User("test","test","test","test");
        session.persist(user);
        Parts parts = new Parts("test","test",9999);
        session.persist(parts);
        Offer offer = new Offer("test",9999,"test",9999, parts);
        session.persist(offer);
        List<Order> list = new ArrayList<>();
        Order order1 = new Order(
                user, offer,
                new Date(1970,01,01),
                new Date(1970,01,02),
                master, 1, "test"
        );
        list.add(order1);
        session.persist(order1);
        Order order2 = new Order(
                user, offer,
                new Date(1970,01,03),
                new Date(1970,01,04),
                master, 1, "test2"
        );
        list.add(order2);
        session.persist(order2);
        session.flush();

        Assert.assertEquals(0, orderDAO.findOrderByStatus("test3",user.getIdUser()).size());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findOrderByStatusWhenOrderWithSuchStatusExists() {
        Session session = this.sessionFactory.getCurrentSession();
        Master master = new Master("test","test",1);
        session.persist(master);
        User user = new User("test","test","test","test");
        session.persist(user);
        Parts parts = new Parts("test","test",9999);
        session.persist(parts);
        Offer offer = new Offer("test",9999,"test",9999, parts);
        session.persist(offer);
        List<Order> list = new ArrayList<>();
        Order order1 = new Order(
                user, offer,
                new Date(1970,01,01),
                new Date(1970,01,02),
                master, 1, "test"
        );
        list.add(order1);
        session.persist(order1);
        Order order2 = new Order(
                user, offer,
                new Date(1970,01,03),
                new Date(1970,01,04),
                master, 1, "test"
        );
        list.add(order2);
        session.persist(order2);
        session.flush();

        Assert.assertEquals(list, orderDAO.findOrderByStatus("test",user.getIdUser()));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void changeOrderStatusWhenOrderExists() {
        Session session = this.sessionFactory.getCurrentSession();
        Master master = new Master("test","test",1);
        session.persist(master);
        User user = new User("test","test","test","test");
        session.persist(user);
        Parts parts = new Parts("test","test",9999);
        session.persist(parts);
        Offer offer = new Offer("test",9999,"test",9999, parts);
        session.persist(offer);
        Order order1 = new Order(
                user, offer,
                new Date(1970,01,01),
                new Date(1970,01,02),
                master, 1, "test"
        );
        session.persist(order1);
        session.flush();

        orderDAO.changeOrderStatus(order1.getIdOrder(),"test1");
        Assert.assertEquals("test1", session.get(Order.class, order1.getIdOrder()).getStatus());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void changeOrderDate() {
        Session session = this.sessionFactory.getCurrentSession();
        Master master = new Master("test","test",1);
        session.persist(master);
        User user = new User("test","test","test","test");
        session.persist(user);
        Parts parts = new Parts("test","test",9999);
        session.persist(parts);
        Offer offer = new Offer("test",9999,"test",9999, parts);
        session.persist(offer);
        Order order1 = new Order(
                user, offer,
                new Date(1970,01,01),
                new Date(1970,01,02),
                master, 1, "test"
        );
        session.persist(order1);
        session.flush();

        Date newDate = new Date(1999,10,1);
        orderDAO.changeOrderDate(order1.getIdOrder(), newDate);
        Assert.assertEquals(newDate, session.get(Order.class, order1.getIdOrder()).getDateFinish());
    }
}