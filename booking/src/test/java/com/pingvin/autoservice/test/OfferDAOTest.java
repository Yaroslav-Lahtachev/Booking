package com.pingvin.autoservice.test;

import com.pingvin.autoservice.dao.MasterDAO;
import com.pingvin.autoservice.dao.OfferDAO;
import com.pingvin.autoservice.entity.Master;
import com.pingvin.autoservice.entity.Offer;
import com.pingvin.autoservice.entity.Parts;
import com.pingvin.autoservice.model.OffersInfo;
import com.pingvin.autoservice.pagination.PaginationResult;
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

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OfferDAOTest {
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private OfferDAO offerDAO;

    @Test
    @Transactional
    @Rollback(true)
    public void findByIdOfferWhenIdExists() {
        Session session = this.sessionFactory.getCurrentSession();
        Parts partsExpected = new Parts("test","test",9999);
        session.persist(partsExpected);
        Offer offerExpected = new Offer("test",9999,"rightProf",9999,partsExpected);
        session.persist(offerExpected);
        session.flush();
        Offer offerActual = offerDAO.findByIdOffer(offerExpected.getIdOffer());
        Assert.assertEquals(offerExpected, offerActual);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findByIdOfferWhenIdNotExists() {
        Offer offerActual = offerDAO.findByIdOffer(1);
        Assert.assertNull(offerActual);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findOffersInfoWhenOffersNotExists() {
        PaginationResult<OffersInfo> pagination = offerDAO.findOffersInfo(new OffersInfo(),2,1,10);
        Assert.assertEquals(0,pagination.getList().size());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findOffersInfoWhenOffersExists() {
        Session session = this.sessionFactory.getCurrentSession();
        Parts parts1 = new Parts("test","test",9999);
        session.persist(parts1);
        Offer offer1 = new Offer("test",9999,"rightProf",9999,parts1);
        session.persist(offer1);
        Parts parts2 = new Parts("test","test",9999);
        session.persist(parts2);
        Offer offer2 = new Offer("test",9999,"rightProf",9999,parts2);
        session.persist(offer2);
        OffersInfo offersInfo2 = new OffersInfo(offer2);
        Parts parts3 = new Parts("test","test",9999);
        session.persist(parts3);
        Offer offer3 = new Offer("test",9999,"rightProf",9999,parts3);
        session.persist(offer3);

        PaginationResult<OffersInfo> pagination = offerDAO.findOffersInfo(new OffersInfo(),2,1,10);
        Assert.assertEquals(offersInfo2, pagination.getList().get(0));
    }
}