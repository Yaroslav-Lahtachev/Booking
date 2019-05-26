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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OfferDAOTest {
    @Autowired
    private SessionFactory sessionFactory;

    @Test
    @Transactional
    @Rollback(true)
    public void findByIdOfferWhenIdExists() {
        Session session = this.sessionFactory.getCurrentSession();
        Parts partsExpected = new Parts(9999,"test","test",9999);
        session.persist(partsExpected);
        Offer offerExpected = new Offer(9999,"test",9999,"rightProf",9999,partsExpected);
        session.persist(offerExpected);
        session.flush();
        Offer offerActual = new OfferDAO().findByIdOffer(9999);
        Assert.assertEquals(offerExpected, offerActual);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findByIdOfferWhenIdNotExists() {
        Offer offerActual = new OfferDAO().findByIdOffer(9999);
        Assert.assertNull(offerActual);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findOffersInfoWhenOffersNotExists() {
        PaginationResult<OffersInfo> pagination = new OfferDAO().findOffersInfo(new OffersInfo(),2,1,10);
        Assert.assertNull(pagination);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findOffersInfoWhenOffersExists() {
        Session session = this.sessionFactory.getCurrentSession();
        Parts parts9997 = new Parts(9997,"test","test",9999);
        session.persist(parts9997);
        Offer offer9997 = new Offer(9997,"test",9999,"rightProf",9999,parts9997);
        session.persist(offer9997);
        Parts parts9998 = new Parts(9998,"test","test",9999);
        session.persist(parts9998);
        Offer offer9998 = new Offer(9998,"test",9999,"rightProf",9999,parts9998);
        session.persist(offer9998);
        OffersInfo offersInfo9998 = new OffersInfo(offer9998);
        Parts parts9999 = new Parts(9999,"test","test",9999);
        session.persist(parts9999);
        Offer offer9999 = new Offer(9999,"test",9999,"rightProf",9999,parts9999);
        session.persist(offer9999);

        PaginationResult<OffersInfo> pagination = new OfferDAO().findOffersInfo(new OffersInfo(),2,1,10);
        Assert.assertEquals(offersInfo9998, pagination.getList().get(0));
    }
}