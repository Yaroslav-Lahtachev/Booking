package com.pingvin.autoservice.dao;

import com.pingvin.autoservice.entity.Offer;
import com.pingvin.autoservice.entity.OrderHistory;
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

    private static boolean wasFirst;

    public Offer findByIdOffer(int idOffer) {
        Session session = this.sessionFactory.getCurrentSession();
        return session.get(Offer.class, idOffer);
    }

    public PaginationResult<OffersInfo> listOffersInfo(String status, int page, int maxResult, int maxNavPage) {
        String sql = "Select new " + OffersInfo.class.getName()
                + " (e.idOffer.idOffer, e.city, e.address, e.maxPeopleCount, e.price, e.parking,e.wifi, e.animal, e.smoking) "
                + " from " + OrderHistory.class.getName() + " as o," + Offer.class.getName() + " as e "
                + " where o.status =: status and o.idOffer = e.idOffer order by e.price ";
        Session session = this.sessionFactory.getCurrentSession();
        Query<OffersInfo> query = session.createQuery(sql, OffersInfo.class);
        query.setParameter("status", status);
        return new PaginationResult<OffersInfo>(query, page, maxResult, maxNavPage);
    }

    public PaginationResult<OffersInfo> findOffersInfo(OffersInfo request, String status, int page, int maxResult, int maxNavPage) {
        wasFirst = false;
        String sql = "Select new " + OffersInfo.class.getName()
                + " (e.idOffer.idOffer, e.city, e.address, e.maxPeopleCount, e.price, e.parking,e.wifi, e.animal, e.smoking) "
                + " from " + OrderHistory.class.getName() + " as o," + Offer.class.getName() + " as e ";
        if (request.getCity().length() != 0) {
            sql += isFirst();
            sql += " e.city like :city ";
        }
        if (request.getAddress().length() != 0) {
            sql += isFirst();
            sql += " e.address like :address";
        }
        if (request.getMaxPeopleCount() != 0) {
            sql += isFirst();
            sql += " e.maxPeopleCount<=:maxPeopleCount";
        }
        if (request.getPrice() != 0) {
            sql += isFirst();
            sql += " e.price<=:price";
        }
        if (request.isParking()) {
            sql += isFirst();
            sql += " e.parking=:parking";
        }
        if (request.isWifi()) {
            sql += isFirst();
            sql += " e.wifi=:wifi";
        }
        if (request.isAnimal()) {
            sql += isFirst();
            sql += " e.animal=:animal";
        }
        if (request.isSmoking()) {
            sql += isFirst();
            sql += " e.smoking=:smoking";
        }
        sql += isFirst();
        sql += " o.status =: status and o.idOffer = e.idOffer order by e.price ";
        Session session = this.sessionFactory.getCurrentSession();
        Query<OffersInfo> query = session.createQuery(sql, OffersInfo.class);

        if (request.getCity().length() != 0)
            query.setParameter("city", "%" + request.getCity() + "%");
        if (request.getAddress().length() != 0)
            query.setParameter("address", "%" + request.getAddress() + "%");
        if (request.getMaxPeopleCount() != 0)
            query.setParameter("maxPeopleCount", request.getMaxPeopleCount());
        if (request.getPrice() != 0)
            query.setParameter("price", request.getPrice());
        if (request.isParking())
            query.setParameter("parking", request.isParking());
        if (request.isWifi())
            query.setParameter("wifi", request.isWifi());
        if (request.isAnimal())
            query.setParameter("animal", request.isAnimal());
        if (request.isSmoking())
            query.setParameter("smoking", request.isSmoking());
        query.setParameter("status", status);
        return new PaginationResult<OffersInfo>(query, page, maxResult, maxNavPage);
    }

    public static String isFirst() {
        if (wasFirst != true) {
            wasFirst = true;
            return " where ";
        } else
            return " and ";
    }

    public PaginationResult<OffersInfo> listOffersByIdUser(User user, int page, int maxResult, int maxNavPage) {
        String sql = "Select new " + OffersInfo.class.getName()
                + " (e.idOffer.idOffer, e.city, e.address, e.maxPeopleCount, e.price, e.parking,e.wifi, e.animal, e.smoking) "
                + " from " + Offer.class.getName() + " e "
                + " where e.idOffer.idSeller.idUser = :idUser ";
        Session session = this.sessionFactory.getCurrentSession();
        Query<OffersInfo> query = session.createQuery(sql, OffersInfo.class);
        query.setParameter("idUser", user.getIdUser());
        return new PaginationResult<OffersInfo>(query, page, maxResult, maxNavPage);
    }

    public void createNewOffer(Parts seller, OffersInfo offersInfo) {
        Session session = this.sessionFactory.getCurrentSession();
        Offer offer = null;
        if (offersInfo.getIdOffer() != 0) {
            offer = findByIdOffer(offersInfo.getIdOffer());
            offer.setCity(offersInfo.getCity());
            offer.setAdress(offersInfo.getAddress());
            offer.setMaxPeopleCount(offersInfo.getMaxPeopleCount());
            offer.setPrice(offersInfo.getPrice());
            offer.setParking(offersInfo.isParking());
            offer.setWifi(offersInfo.isWifi());
            offer.setAnimal(offersInfo.isAnimal());
            offer.setSmoking(offersInfo.isSmoking());
        } else {
            offer = new Offer();
            offer.setIdOffer(seller);
            offer.setCity(offersInfo.getCity());
            offer.setAdress(offersInfo.getAddress());
            offer.setMaxPeopleCount(offersInfo.getMaxPeopleCount());
            offer.setPrice(offersInfo.getPrice());
            offer.setParking(offersInfo.isParking());
            offer.setWifi(offersInfo.isWifi());
            offer.setAnimal(offersInfo.isAnimal());
            offer.setSmoking(offersInfo.isSmoking());
        }
        session.persist(offer);
        session.flush();
    }
}
