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
import com.pingvin.autoservice.form.UtilForm;
import com.pingvin.autoservice.model.OffersInfo;
import com.pingvin.autoservice.model.OrderInfo;
import com.pingvin.autoservice.model.UsersInfo;
import com.pingvin.autoservice.pagination.PaginationResult;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.pingvin.autoservice.model.OrderInfo.timeCut;

public class ManagerCase {
    final static int MAX_RESULT = 3;
    final static int MAX_NAVIGATION_PAGE = 10;

    public static String getUsersList(UserDAO usersDAO, String pageStr, Model model) {
        int page = 1;
        try {
            page = Integer.parseInt(pageStr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        PaginationResult<UsersInfo> paginationResult = usersDAO.listUsersInfo(page, MAX_RESULT, MAX_NAVIGATION_PAGE);

        model.addAttribute("paginationResult", paginationResult);
        return "usersList";
    }

    public static String getUsersOrdersList(OrderDAO orderDAO, String pageStr, Model model) {
        int page = 1;
        try {
            page = Integer.parseInt(pageStr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        PaginationResult<OrderInfo> paginationResult = orderDAO.findAllOrders(page, MAX_RESULT, MAX_NAVIGATION_PAGE);
        model.addAttribute("paginationResult", paginationResult);
        return "viewAllUsersOrdersForAdmin";
    }

    public static String getConcreteUserOrdersList(UserDAO usersDAO, OrderDAO orderDAO, String pageStr, int id, Model model) {
        int page = 1;
        try {
            page = Integer.parseInt(pageStr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (id != 0) {
            User buyer = usersDAO.findByIdUser(id);

            PaginationResult<OrderInfo> paginationResult = orderDAO.findOrderByCustomer(buyer, page, MAX_RESULT, MAX_NAVIGATION_PAGE);

            model.addAttribute("paginationResult", paginationResult);
            model.addAttribute("id", id);
        } else return "redirect:/usersList";
        return "viewUserOrdersForAdmin";
    }

    public static String cancelOrder(int idOrder, Model model) {
        UtilForm utilForm = new UtilForm();
        utilForm.setIntField(idOrder);
        model.addAttribute("utilForm", utilForm);
        return "cancelOrder";
    }

    public static String cancelOrderPost(MasterDAO masterDAO, OrderDAO orderDAO, UtilForm utilForm) {
        Order order = orderDAO.findOrderByIdOrder(utilForm.getIntField());
        if (utilForm.getTextField().equals("ПОДТВЕРЖДАЮ")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(order != null){
                masterDAO.checkIfMasterIsFree(order.getIdOrder(), order.getMaster().getMaster());
                orderDAO.removeOrder(order);
            }
        }
        return "redirect:/admin/viewUserOrdersForAdmin?id=" + order.getCustomer().getIdUser();
    }

    public static String checkupForAdmin(OfferDAO offerDAO, OrderDAO orderDAO, String id, int page, Model model) {
        int idOrder = 0;
        try {
            idOrder = Integer.parseInt(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Order order = null;
        if (idOrder != 0)
            order = orderDAO.findOrderByIdOrder(idOrder);
        OrderInfo orderInfo = null;
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
        if (order != null) {
            orderInfo = new OrderInfo(order);
            model.addAttribute("orderInfo", orderInfo);
            model.addAttribute("error", new Boolean(false));
        } else return "redirect:/admin/usersList";
        return "checkupForAdmin";
    }

    public static String checkupForAdminPost(UserDAO usersDAO, OfferDAO offerDAO, MasterDAO masterDAO, OrderDAO orderDAO, JavaMailSender emailSender, OffersInfo searchOffer) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User buyer = usersDAO.findByLogin(authentication.getName());
        List<Integer> offers = new ArrayList();
        try {
            for (int i = 0; i < searchOffer.getOffer().size(); i++) {
                offers.add(Integer.parseInt(searchOffer.getOffer().get(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sendTo = buyer.getEmail();
        if (offers.isEmpty()) {
            sendSimpleMessage(emailSender, String.format(Consts.MESSAGE_ABOUT_RESULT_OF_CHECKUP_LUCKY, buyer.getIdUser()), sendTo, "yo, dude");
        } else {
            for (int i = 0; i < offers.size(); i++) {
                Offer offer = offerDAO.findByIdOffer(offers.get(i));
                Master master = masterDAO.findByIdMaster(1);

                int isNeedParts = 0;
                orderDAO.reserve(buyer, master, offer, isNeedParts, new Date(), new Date(), Consts.WAITING_FOR_RESULTION_STATUS);
            }
            sendSimpleMessage(emailSender, String.format(Consts.MESSAGE_ABOUT_RESULT_OF_CHECKUP, buyer.getIdUser()), sendTo, "yo, dude");
        }
        return "redirect:/admin/usersList";
    }

    public static String changeOrderStatus(OrderDAO orderDAO, String id, Model model) {
        int idOrder = 0;
        try {
            idOrder = Integer.parseInt(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Order order = null;
        if (idOrder != 0)
            order = orderDAO.findOrderByIdOrder(idOrder);
        OrderInfo orderInfo = null;
        SignUpForm signUpForm = null;
        if (order != null) {
            orderInfo = new OrderInfo(order);
            signUpForm = new SignUpForm();
            signUpForm.setStatus(order.getStatus());
            model.addAttribute("orderInfo", orderInfo);
            model.addAttribute("signUpForm", signUpForm);
            model.addAttribute("error", new Boolean(false));
        } else return "redirect:/admin/usersList";
        return "changeOrderStatus";
    }

    public static String changeOrderStatusPost(MasterDAO masterDAO, OrderDAO orderDAO, OrderInfo orderInfo, SignUpForm signUpForm) {
        Order order = orderDAO.findOrderByIdOrder(orderInfo.getId());
        if(signUpForm.getStatus() !=  null){
            orderDAO.changeOrderStatus(order.getIdOrder(), signUpForm.getStatus());
            if (signUpForm.getStatus() == "DONE") {
                masterDAO.checkIfMasterIsFree(order.getIdOrder(), order.getMaster().getMaster());
            }
        }
        return "redirect:/admin/viewUserOrdersForAdmin?id=" + order.getCustomer().getIdUser();
    }

    public static String changeOrderTime(OrderDAO orderDAO, String id, Model model) {
        int idOrder = 0;
        try {
            idOrder = Integer.parseInt(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Order order = null;
        if (idOrder != 0)
            order = orderDAO.findOrderByIdOrder(idOrder);
        OrderInfo orderInfo = null;
        SignUpForm signUpForm = null;
        if (order != null) {
            orderInfo = new OrderInfo(order);
            signUpForm = new SignUpForm();
            signUpForm.setDateStart(new Date());
            signUpForm.setDateFinish(new Date());
            model.addAttribute("orderInfo", orderInfo);
            model.addAttribute("signUpForm", signUpForm);
            model.addAttribute("error", new Boolean(false));
        } else return "redirect:/admin/viewUserOrdersForAdmin?id=" + order.getCustomer().getIdUser();
        return "changeOrderTime";
    }

    public static String changeOrderTimePost(UserDAO usersDAO, JavaMailSender emailSender, OrderInfo orderInfo, SignUpForm signUpForm) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date newdate = timeCut(signUpForm.getDateFinish());
        String reason = signUpForm.getReason();
        String sendTo = usersDAO.findByIdUser(orderInfo.getCustomer()).getEmail();
        sendSimpleMessage(emailSender, String.format(Consts.MESSAGE_ABOUT_CHANGING_TIME, reason, simpleDateFormat.format(newdate), orderInfo.getId()), sendTo, "yo, dude");
        return "redirect:/admin/usersList";
    }

    private static void sendSimpleMessage(JavaMailSender emailSender,
                                          String text, String to, String subject) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}
