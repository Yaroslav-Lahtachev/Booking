package com.lahtachev.booking.dao;

import com.lahtachev.booking.entity.Seller;
import com.lahtachev.booking.entity.User;
import com.lahtachev.booking.model.SellerInfo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class SellerDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public SellerDAO(){}

    public List<Seller> findOffersByIdUser(User idSeller){
        Session session = this.sessionFactory.getCurrentSession();
        return session.createCriteria(Seller.class).add(Restrictions.eq("idSeller", idSeller)).list();
    }

    public Seller addNewOffer(User idSeller){
        Session session = this.sessionFactory.getCurrentSession();
        if (idSeller.getStatus()!= "ROLE_BLOCKED") {
            if (idSeller.getStatus() == "ROLE_USER")
                idSeller.setStatus("ROLE_SELLER");
            Seller seller = new Seller();
            seller.setIdUser(idSeller);
            session.persist(seller);
            session.flush();
            return seller;
        }
        return null;
    }

    public Seller findSellerByOffer(int idOffer){
        Session session = this.sessionFactory.getCurrentSession();
        return session.get(Seller.class, idOffer);
    }
}
