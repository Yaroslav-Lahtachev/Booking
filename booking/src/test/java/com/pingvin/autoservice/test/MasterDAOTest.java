package com.pingvin.autoservice.test;

import com.pingvin.autoservice.dao.MasterDAO;
import com.pingvin.autoservice.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MasterDAOTest {
    @Autowired
    private MasterDAO masterDAO;
    @Autowired
    private SessionFactory sessionFactory;

    @Test
    @Transactional
    @Rollback(true)
    public void findByIdMasterWhenIdExists() {
            Session session = this.sessionFactory.getCurrentSession();
            Master masterExpected = new Master("test","test",0);
            session.persist(masterExpected);
            session.flush();
            Master masterActual = masterDAO.findByIdMaster(masterExpected.getMaster());
            Assert.assertEquals(masterExpected, masterActual);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findByIdMasterWhenIdNotExists() {
        Master masterActual = masterDAO.findByIdMaster(1);
        Assert.assertNull(masterActual);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void getFreeMasterWhenNoFreeMasters() {
        Session session = this.sessionFactory.getCurrentSession();
        Master masterExpected = new Master("test","test",1);
        session.persist(masterExpected);
        Master masterExpected2 = new Master("test","test",1);
        session.persist(masterExpected2);

        Parts partsActual = new Parts("test","test",9999);
        session.persist(partsActual);
        Offer offerActual = new Offer("test",9999,"rightProf",9999,partsActual);
        session.persist(offerActual);
        Date startTime = new Date(1970,01,01);
        Date finishedTime = new Date(1970,01,02);
        session.flush();
        Master masterActual = masterDAO.getFreeMaster(offerActual,startTime,finishedTime);
        Assert.assertNull(masterActual);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void getFreeMasterWhenNoFreeMastersWithThatSpec() {
        Session session = this.sessionFactory.getCurrentSession();
        Master masterExpected = new Master("test","test",0);
        session.persist(masterExpected);
        Master masterExpected2 = new Master("test","test",0);
        session.persist(masterExpected2);

        Parts partsActual = new Parts("test","test",9999);
        session.persist(partsActual);
        Offer offerActual = new Offer("test",9999,"rightProf",9999,partsActual);
        session.persist(offerActual);
        Date startTime = new Date(1970,01,01);
        Date finishedTime = new Date(1970,01,02);
        session.flush();
        Master masterActual = masterDAO.getFreeMaster(offerActual,startTime,finishedTime);
        Assert.assertNull(masterActual);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void getFreeMasterWhenMasterWithThatSpecIsFree() {
        Session session = this.sessionFactory.getCurrentSession();
        Master masterExpected = new Master("rightProf","test",0);
        session.persist(masterExpected);

        Parts partsActual = new Parts("test","test",9999);
        session.persist(partsActual);
        Offer offerActual = new Offer("test",9999,"rightProf",9999,partsActual);
        session.persist(offerActual);
        Date startTime = new Date(1970,01,01);
        Date finishedTime = new Date(1970,01,02);
        session.flush();
        Master masterActual = masterDAO.getFreeMaster(offerActual,startTime,finishedTime);
        Assert.assertEquals(masterExpected, masterActual);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void checkIfMasterIsFreeWhenHeHasOrders() {
        Session session = this.sessionFactory.getCurrentSession();
        Master masterExpected = new Master("test","test",1);
        session.persist(masterExpected);
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
                masterExpected, 1, "test"
        );
        session.persist(order1);
        session.flush();
        masterDAO.checkIfMasterIsFree(order1.getIdOrder(), masterExpected.getMaster());
        Assert.assertEquals(0, masterExpected.getOccupied());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void checkIfMasterIsFreeWhenHeHasNotOrders() {
        Session session = this.sessionFactory.getCurrentSession();
        Master masterExpected = new Master("test","test",1);
        session.persist(masterExpected);
        session.flush();
        masterDAO.checkIfMasterIsFree(1, masterExpected.getMaster());
        Assert.assertEquals(masterExpected.getOccupied(), 0);
    }
}
