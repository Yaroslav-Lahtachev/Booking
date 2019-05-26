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
    private SessionFactory sessionFactory;

    @Test
    @Transactional
    @Rollback(true)
    public void findByIdMasterWhenIdExists() {
            Session session = this.sessionFactory.getCurrentSession();
            Master masterExpected = new Master(9999,"test","test",0);
            session.persist(masterExpected);
            session.flush();
            Master masterActual = new MasterDAO().findByIdMaster(9999);
            Assert.assertEquals(masterExpected, masterActual);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findByIdMasterWhenIdNotExists() {
        Master masterActual = new MasterDAO().findByIdMaster(9999);
        Assert.assertNull(masterActual);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void getFreeMasterWhenNoFreeMasters() {
        Session session = this.sessionFactory.getCurrentSession();
        Master masterExpected = new Master(1,"test","test",1);
        session.persist(masterExpected);
        Master masterExpected2 = new Master(2,"test","test",1);
        session.persist(masterExpected2);

        Parts partsActual = new Parts(9999,"test","test",9999);
        session.persist(partsActual);
        Offer offerActual = new Offer(9999,"test",9999,"rightProf",9999,partsActual);
        session.persist(offerActual);
        Date startTime = new Date(1970,01,01);
        Date finishedTime = new Date(1970,01,02);
        session.flush();
        Master masterActual = new MasterDAO().getFreeMaster(offerActual,startTime,finishedTime);
        Assert.assertEquals(masterExpected, masterActual);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void getFreeMasterWhenNoFreeMastersWithThatSpec() {
        Session session = this.sessionFactory.getCurrentSession();
        Master masterExpected = new Master(1,"test","test",0);
        session.persist(masterExpected);
        Master masterExpected2 = new Master(2,"test","test",0);
        session.persist(masterExpected2);

        Parts partsActual = new Parts(9999,"test","test",9999);
        session.persist(partsActual);
        Offer offerActual = new Offer(9999,"test",9999,"rightProf",9999,partsActual);
        session.persist(offerActual);
        Date startTime = new Date(1970,01,01);
        Date finishedTime = new Date(1970,01,02);
        session.flush();
        Master masterActual = new MasterDAO().getFreeMaster(offerActual,startTime,finishedTime);
        Assert.assertEquals(masterExpected, masterActual);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void getFreeMasterWhenMasterWithThatSpecIsFree() {
        Session session = this.sessionFactory.getCurrentSession();
        Master masterExpected = new Master(9999,"rightProf","test",0);
        session.persist(masterExpected);

        Parts partsActual = new Parts(9999,"test","test",9999);
        session.persist(partsActual);
        Offer offerActual = new Offer(9999,"test",9999,"rightProf",9999,partsActual);
        session.persist(offerActual);
        Date startTime = new Date(1970,01,01);
        Date finishedTime = new Date(1970,01,02);
        session.flush();
        Master masterActual = new MasterDAO().getFreeMaster(offerActual,startTime,finishedTime);
        Assert.assertEquals(masterExpected, masterActual);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void checkIfMasterIsFreeWhenHeIsBusy() {
        Session session = this.sessionFactory.getCurrentSession();
        Master masterExpected = new Master(1,"test","test",1);
        session.persist(masterExpected);
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
                masterExpected, 1, "test"
        );
        session.persist(order1);
        session.flush();
        new MasterDAO().checkIfMasterIsFree(9999, masterExpected.getMaster());
        Assert.assertEquals(masterExpected.getOccupied(), 1);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void checkIfMasterIsFreeWhenHeIsFree() {
        Session session = this.sessionFactory.getCurrentSession();
        Master masterExpected = new Master(1,"test","test",1);
        session.persist(masterExpected);
        session.flush();
        new MasterDAO().checkIfMasterIsFree(9999, masterExpected.getMaster());
        Assert.assertEquals(masterExpected.getOccupied(), 0);
    }
}
