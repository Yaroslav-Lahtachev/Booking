package com.pingvin.autoservice.controller;

import com.pingvin.autoservice.config.Consts;
import com.pingvin.autoservice.dao.MasterDAO;
import com.pingvin.autoservice.dao.OfferDAO;
import com.pingvin.autoservice.dao.OrderDAO;
import com.pingvin.autoservice.dao.UserDAO;
import com.pingvin.autoservice.entity.Master;
import com.pingvin.autoservice.entity.Offer;
import com.pingvin.autoservice.entity.Order;
import com.pingvin.autoservice.entity.User;
import com.pingvin.autoservice.form.SignUpForm;
import com.pingvin.autoservice.form.UsersForm;
import com.pingvin.autoservice.form.UtilForm;
import com.pingvin.autoservice.model.OffersInfo;
import com.pingvin.autoservice.model.OrderInfo;
import com.pingvin.autoservice.model.UsersInfo;
import com.pingvin.autoservice.pagination.PaginationResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.pingvin.autoservice.config.Consts.TIME_FOR_DELIVER_KIT;

public class UserCase {
    final static int MAX_RESULT = 3;
    final static int MAX_NAVIGATION_PAGE = 10;

    //todo registration & removing user
    public static String registerUser(Model model) {
        UsersForm form = new UsersForm();
        model.addAttribute("usersForm", form);
        return "registerPage";
    }

    public static String registerUserPost(UserDAO usersDAO, UsersForm usersForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "registerPage";
        }
        try {
            usersDAO.addNewUser(usersForm);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "registerPage";
        }
        return "redirect:/admin/login";
    }

    public static String removeUser(int userId, Model model) {
        UtilForm utilForm = new UtilForm();
        utilForm.setIntField(userId);
        model.addAttribute("utilForm", utilForm);
        return "removeUser";
    }

    public static String removeUserPost(UserDAO usersDAO, OrderDAO orderDAO, UtilForm utilForm) {
        if (utilForm.getTextField().equals("ПОДТВЕРЖДАЮ")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            int idUser = utilForm.getIntField();
            List allOrdersByUser = orderDAO.findOrderByCustomer(idUser);
            if (allOrdersByUser.size() > 0) {
                orderDAO.removeOrders(allOrdersByUser);
            }
            usersDAO.removeUser(utilForm.getIntField());
            return "redirect:/admin/logout";
        }
        return "redirect:/admin/userInfo";
    }

    public static String getUserInfo(UserDAO usersDAO, Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UsersInfo userInfo = new UsersInfo(usersDAO.findByLogin(userDetails.getUsername()));

        model.addAttribute("userDetails", userDetails);
        model.addAttribute("userInfo", userInfo);
        return "userInfo";
    }

    //todo Orders manipulations
    public static String addOrder(UserDAO usersDAO, OfferDAO offerDAO, MasterDAO masterDAO, OrderDAO orderDAO, OffersInfo searchOffer, SignUpForm signUpForm) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User buyer = usersDAO.findByLogin(authentication.getName());
        List<Integer> offers = new ArrayList();
        List<Integer> needKit = new ArrayList();
        reserveOrder(offerDAO, masterDAO, orderDAO, offers, needKit, searchOffer, signUpForm, buyer);
        return "redirect:/buyerOrders";
    }

    public static String getOrders(UserDAO usersDAO, OrderDAO orderDAO, String pageStr, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int page = 1;
        try {
            page = Integer.parseInt(pageStr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        User user = usersDAO.findByLogin(authentication.getName());
        PaginationResult<OrderInfo> paginationResult =
                orderDAO.findOrderByCustomer(user, page, MAX_RESULT, MAX_NAVIGATION_PAGE);
        model.addAttribute("paginationResult", paginationResult);
        return "buyerOrders";
    }

    public static String submitResultСheckup(UserDAO usersDAO, OfferDAO offerDAO, MasterDAO masterDAO, OrderDAO orderDAO, OffersInfo searchOffer, SignUpForm signUpForm) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User buyer = usersDAO.findByLogin(authentication.getName());
        List<Integer> offers = new ArrayList();
        List<Integer> needKit = new ArrayList();
        orderDAO.removeOrdersByStatus(Consts.WAITING_FOR_RESULTION_STATUS, buyer.getIdUser());
        reserveOrder(offerDAO, masterDAO, orderDAO, offers, needKit, searchOffer, signUpForm, buyer);
        return "redirect:/buyerOrders";
    }

    public static String changeOrderResponse(UserDAO usersDAO, OrderDAO orderDAO, String id, String time, Model model) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        int idOrder = 0;
        Date date = null;
        try {
            idOrder = Integer.parseInt(id);
            date = format.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User buyer = usersDAO.findByLogin(authentication.getName());
        if (date != null && idOrder != -1) {
            Order order = orderDAO.findOrderByIdOrder(idOrder);
            if (order != null && order.getCustomer().getIdUser() == buyer.getIdUser()) {
                UtilForm utilForm = new UtilForm();
                OrderInfo orderInfo = new OrderInfo(order);
                orderInfo.setDateFinish(date);
                model.addAttribute("orderInfo", orderInfo);
                model.addAttribute("utilForm", utilForm);
                model.addAttribute("error", new Boolean(false));
            } else return "index";
        }
        return "acceptChangeTime";
    }

    public static String changeOrderResponsePost(MasterDAO masterDAO, OrderDAO orderDAO, UtilForm utilForm, OrderInfo orderInfo) {
        if (utilForm.getTextField() == null) {
            return "redirect:/buyerOrders";
        }
        if (utilForm.getTextField().equals("Agree")) {
            orderDAO.changeOrderDate(orderInfo.getId(), orderInfo.getDateFinish());
        } else {
            System.out.println("No way, dude, customer is gay, lets delete his order");
            Order order = orderDAO.findOrderByIdOrder(utilForm.getIntField());
            masterDAO.checkIfMasterIsFree(order.getIdOrder(), order.getMaster().getMaster());
            orderDAO.removeOrder(order);
        }
        return "redirect:/buyerOrders";
    }

    public static String checkupForUser(UserDAO usersDAO, OrderDAO orderDAO, int page, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User buyer = usersDAO.findByLogin(authentication.getName());
        OffersInfo searchOffer = new OffersInfo();
        PaginationResult<OffersInfo> paginationResult = orderDAO.findOfferByCustomerByStatus(buyer, Consts.WAITING_FOR_RESULTION_STATUS, page, MAX_RESULT, MAX_NAVIGATION_PAGE);
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setDateStart(new Date());
        signUpForm.setDateFinish(new Date());
        model.addAttribute("paginationResult", paginationResult);
        model.addAttribute("signUpForm", signUpForm);
        model.addAttribute("searchOffer", searchOffer);
        if (paginationResult.getTotalRecords() != 0) {
            return "checkupForUser";
        }
        return "index";
    }


    //todo Offers
    public static String getOffers(OfferDAO offerDAO, int page, Model model) {
        OffersInfo searchOffer = new OffersInfo();
        //UtilForm utilForm = new UtilForm();
        //ArrayList arrayList = new ArrayList();
        PaginationResult<OffersInfo> paginationResult = offerDAO.findOffersInfo(searchOffer, page, MAX_RESULT, MAX_NAVIGATION_PAGE);
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setDateStart(new Date());
        signUpForm.setDateFinish(new Date());
        model.addAttribute("paginationResult", paginationResult);
        //model.addAttribute("utilForm", utilForm);
        model.addAttribute("signUpForm", signUpForm);
        //model.addAttribute("arrayList", arrayList);
        model.addAttribute("searchOffer", searchOffer);
        return "offersPage";
    }


    //todo internal methods
    private static void reserveOrder(OfferDAO offerDAO, MasterDAO masterDAO, OrderDAO orderDAO, List<Integer> offers, List<Integer> needKit, OffersInfo searchOffer, SignUpForm signUpForm, User buyer) {
        try {
            for (int i = 0; i < searchOffer.getOffer().size(); i++) {
                offers.add(Integer.parseInt(searchOffer.getOffer().get(i)));
            }
            for (int i = 0; i < searchOffer.getNeedKit().size(); i++) {
                needKit.add(Integer.parseInt(searchOffer.getNeedKit().get(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!offers.isEmpty()) {
            for (int i = 0; i < offers.size(); i++) {
                OrderInfo orderInfo = new OrderInfo(offers.get(i), signUpForm.getDateStart(), signUpForm.getDateFinish());
                Offer offer = offerDAO.findByIdOffer(offers.get(i));

                int isNeedParts = 0;
                for (int j = 0; j < needKit.size(); j++) {
                    if (offer.getIdOffer() == needKit.get(j)) {
                        isNeedParts = 1;
                        break;
                    }
                }
                long timeFinish = orderInfo.getDateStart().getTime() + TimeUnit.SECONDS.toMillis(offer.getTime());
                if (isNeedParts == 1) {
                    timeFinish += TIME_FOR_DELIVER_KIT;
                }
                long timeStart = orderInfo.getDateStart().getTime();
                if (isNeedParts == 1) {
                    timeStart += TIME_FOR_DELIVER_KIT;
                }
                Date dateFinish = new Date(timeFinish);
                Date dateStart = new Date(timeStart);
                Master master = masterDAO.getFreeMaster(offer, dateStart, dateFinish);
                orderDAO.reserve(buyer, master, offer, isNeedParts, orderInfo.getDateStart(), dateFinish, Consts.CREATED_STATUS);
            }
        }
    }
}
