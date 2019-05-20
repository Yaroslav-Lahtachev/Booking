package com.pingvin.autoservice.dao;

import com.pingvin.autoservice.entity.*;
//import com.pingvin.autoservice.entity.OrderHistory;
//import com.pingvin.autoservice.model.OrderHistoryInfo;
import com.pingvin.autoservice.model.OrderInfo;
import com.pingvin.autoservice.pagination.PaginationResult;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
                + " (o.id, o.offer.id, f.name, f.price, m.name, o.dateStart, o.dateFinish) "
                + " From " + Order.class.getName() + " as o, " + Offer.class.getName() + " as f, " + Master.class.getName() + " as m "
                + " where f.id=o.offer and o.customer.id =: idUser ";
        Query<OrderInfo> query = session.createQuery(sql, OrderInfo.class);
        query.setParameter("idUser", user.getIdUser());
        return new PaginationResult<OrderInfo>(query, page, maxResult, maxNavPage);
    }


//   public PaginationResult<OrderHistoryInfo> findOrderBySeller(List<Parts> listOffers, int page, int maxResult, int maxNavPage) {
//       if (listOffers != null) {
//           Session session = this.sessionFactory.getCurrentSession();
//           String sql = "Select new " + OrderHistoryInfo.class.getName()
//                   + " (e.idOrder, e.idUser.idUser, e.idOffer.idOffer, e.dateStart, e.dateFinish, e.status) "
//                   + " From " + OrderHistory.class.getName() + " e "
//                   + " where e.idOffer.idOffer =:idOffer1 ";
//           for (int i = 0; i < listOffers.size(); i++) {
//               String str = new String("idOffer");
//               str += (i + 1);
//               sql += " OR e.idOffer.idOffer = :" + str;
//           }
//           Query<OrderHistoryInfo> query = session.createQuery(sql, OrderHistoryInfo.class);
//           for (int i = 0; i < listOffers.size(); i++) {
//               String str = new String("idOffer");
//               str += (i + 1);
//               query.setParameter(str, listOffers.get(i).getIdOffer());
//           }
//           return new PaginationResult<OrderHistoryInfo>(query, page, maxResult, maxNavPage);
//       }
//       return null;
//   }

//   public List<OrderHistoryInfo> getInfoConcretOrder(int idOffer, String status) {
//       Session session = this.sessionFactory.getCurrentSession();
//       String sql = " select new " + OrderHistoryInfo.class.getName()
//               + " (o.dateStart,o.dateFinish) "
//               + " From " + OrderHistory.class.getName() + " o "
//               + " where o.status =: status and o.idOffer.idOffer =: idOffer ";
//       Query<OrderHistoryInfo> query = session.createQuery(sql, OrderHistoryInfo.class);
//       query.setParameter("idOffer", idOffer);
//       query.setParameter("status", status);
//       return query.list();
//   }

//   public PaginationResult<OrderHistoryInfo> findOrderHistoryBySeller(User user, String status, int page, int maxResult, int maxNavPage) {

//       Session session = this.sessionFactory.getCurrentSession();
//       Query<OrderHistoryInfo> query = null;
//       if (status.equals("offer")) {
//           String sql = "SELECT new " + OrderHistoryInfo.class.getName() +
//                   " (o.idOrder, e.idOffer.idOffer, e.city, e.address, e.maxPeopleCount, e.price, e.parking,e.wifi, e.animal, e.smoking,  o.status) " +
//                   " FROM " + OrderHistory.class.getName() + " as o," + Offer.class.getName() + " as e " +
//                   " where o.idOffer=e.idOffer and o.idOffer.idSeller.idUser=:idUser and o.status=:status ";
//           query = session.createQuery(sql, OrderHistoryInfo.class);
//           query.setParameter("idUser", user.getIdUser());
//           query.setParameter("status", status);

//       } else {
//           String sql = "SELECT new " + OrderHistoryInfo.class.getName() +
//                   " (o.idOrder, e.idOffer.idOffer, o.idUser.idUser ,o.idUser.login, e.address, o.dateStart, o.dateFinish, o.status) " +
//                   " FROM " + OrderHistory.class.getName() + " as o," + Offer.class.getName() + " as e " +
//                   " where o.idOffer=e.idOffer and o.idOffer.idSeller.idUser=:idUser and o.status=:status ";
//           query = session.createQuery(sql, OrderHistoryInfo.class);
//           query.setParameter("idUser", user.getIdUser());
//           query.setParameter("status", status);
//       }
//       return new PaginationResult<OrderHistoryInfo>(query, page, maxResult, maxNavPage);

//   }

//   public void saveNewOfferInOrder(Parts seller, Date dateStart, Date dateFinish) {
//       Session session = this.sessionFactory.getCurrentSession();
//       OrderHistory order = new OrderHistory();
//       order.setIdUser(null);
//       order.setIdOffer(seller);
//       order.setDateStart(dateStart);
//       order.setDateFinish(dateFinish);
//       order.setStatus("offer");
//       session.persist(order);
//       session.flush();
//   }

//   public boolean CheckReserveOfferByIdAndDate(int idOffer, Date dateStart, Date dateFinish) {
//       String status = "reserve";
//       List<Date> dates = checkDates(dateStart, dateFinish);
//       if (!dates.isEmpty()) {
//           Session session = this.sessionFactory.getCurrentSession();
//           String sql = " Select new " + OrderHistoryInfo.class.getName() + " (o.idOrder) " +
//                   " FROM " + OrderHistory.class.getName() + " as o " +
//                   "where ((o.dateStart between :dateS and :dateF) " +
//                   " or (o.dateFinish between :dateS and :dateF)) " +
//                   " and (o.idOffer.idOffer =:id) and o.status =: status ";
//           Query query = session.createQuery(sql, OrderHistoryInfo.class);
//           query.setParameter("dateS", dates.get(0));
//           query.setParameter("dateF", dates.get(1));
//           query.setParameter("id", idOffer);
//           query.setParameter("status", status);
//           if (query.list().isEmpty())
//               return true;
//           else
//               return false;
//       } else return false;
//   }

    public void reserve(User customer, Master master, Offer offer, boolean needParts, Date dateStart, Date dateFinish) {
        Session session = this.sessionFactory.getCurrentSession();
        Order order = new Order();
        order.setCustomer(customer);
        order.setOffer(offer);
        order.setMaster(master);
        order.setNeedKit(needParts);
        order.setDateStart(dateStart);
        order.setDateFinish(dateFinish);
        session.persist(order);
        session.flush();
    }

//   public void changeOrderStatus(int idOrder, String status) {
//       OrderHistory orderHistory = findOrderByIdOrder(idOrder);
//       orderHistory.setStatus(status);
//   }

//   public boolean checkOnOwnership(User buyer, Parts seller) {
//       if (buyer.getIdUser() == seller.getIdUser().getIdUser()) {
//           return false;
//       } else {
//           return true;
//       }
//   }

//   public void blockUnblockOffer(int idSeller, String status) {
//       Session session = this.sessionFactory.getCurrentSession();
//       String sql = "Select new " + OrderHistoryInfo.class.getName()
//               + " (o.idOrder) "
//               + " From " + OrderHistory.class.getName() + " as o "
//               + " where o.idOffer.idSeller.idUser =: idSeller ";

//       Query<OrderHistoryInfo> query = session.createQuery(sql, OrderHistoryInfo.class);
//       query.setParameter("idSeller", idSeller);
//       OrderHistory orderHistory = null;
//       for (int i = 0; i < query.list().size(); i++) {

//           orderHistory = findOrderByIdOrder(query.list().get(i).getIdOrder());
//           if (status.equals("blocked"))
//               if (orderHistory.getStatus().equals("offer") || orderHistory.getStatus().equals("reserve"))
//                   orderHistory.setStatus(orderHistory.getStatus() + " " + status);
//           if (status.equals("unblock")) {
//               if (orderHistory.getStatus().contains("offer"))
//                   orderHistory.setStatus("offer");
//               if (orderHistory.getStatus().contains("reserve"))
//                   orderHistory.setStatus("reserve");
//           }
//           session.persist(orderHistory);
//       }
//       session.flush();
//   }

//   public List<Date> checkDates(Date dateStart, Date dateFinish) {
//       List<Date> dates = new ArrayList<>();
//       Date today = new Date();
//       today = OrderHistoryInfo.timeCut(today);
//       Date dateCurrEdit = new Date(today.getTime() - TimeUnit.SECONDS.toMillis(2));
//       Date dateStartEdit = new Date(dateStart.getTime() - TimeUnit.SECONDS.toMillis(1));
//       Date dateFinishEdit = new Date(dateFinish.getTime() + TimeUnit.SECONDS.toMillis(1));
//       if (dateCurrEdit.before(dateStartEdit) && dateStartEdit.before(dateFinishEdit)) {
//           dates.add(dateStartEdit);
//           dates.add(dateFinishEdit);
//       }
//       return dates;
//   }
}
