package com.pingvin.autoservice.dao;

import com.pingvin.autoservice.entity.Offer;
//import com.pingvin.autoservice.entity.OrderHistory;
import com.pingvin.autoservice.entity.Parts;
import com.pingvin.autoservice.entity.User;
import com.pingvin.autoservice.model.OffersInfo;
import com.pingvin.autoservice.pagination.PaginationResult;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class OfferDAO {
    @Autowired
    private SessionFactory sessionFactory;

    public OfferDAO() {
    }

    public Offer findByIdOffer(int idOffer) {
        Session session = this.sessionFactory.getCurrentSession();
        return session.get(Offer.class, idOffer);
    }

    public PaginationResult<OffersInfo> findOffersInfo(OffersInfo request, int page, int maxResult, int maxNavPage) {
        String sql = "Select new " + OffersInfo.class.getName()
                + " (o.id, o.name, o.prof, o.price, o.time, p.name ) "
                + " from " + Offer.class.getName() + " as o, " + Parts.class.getName() + " as p " +
                " where p.id =o.kit order by o.id ";
        Session session = this.sessionFactory.getCurrentSession();
        Query<OffersInfo> query = session.createQuery(sql, OffersInfo.class);

        return new PaginationResult<OffersInfo>(query, page, maxResult, maxNavPage);
    }

}
