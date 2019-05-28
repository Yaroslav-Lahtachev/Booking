package com.pingvin.autoservice.controller;

import org.hibernate.Session;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import com.pingvin.autoservice.config.Consts;
import com.pingvin.autoservice.dao.*;
import com.pingvin.autoservice.entity.*;
import com.pingvin.autoservice.form.*;
import com.pingvin.autoservice.model.OffersInfo;
//import com.pingvin.autoservice.model.OrderHistoryInfo;
import com.pingvin.autoservice.model.OrderInfo;
import com.pingvin.autoservice.model.UsersInfo;
import com.pingvin.autoservice.pagination.PaginationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.pingvin.autoservice.config.Consts.TIME_FOR_DELIVER_KIT;
import static com.pingvin.autoservice.model.OrderInfo.timeCut;

@Controller
public class MainController {

    @Autowired
    private UserDAO usersDAO;
    @Autowired
    private OfferDAO offerDAO;
    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private PartsDAO partsDAO;
    @Autowired
    private MasterDAO masterDAO;
    @Autowired
    public JavaMailSender emailSender;

    final int MAX_RESULT = 3;
    final int MAX_NAVIGATION_PAGE = 10;

    @Autowired
    private UserValidator userValidator;

    @InitBinder
    protected void initBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        if (target.getClass() == UsersForm.class) {
            dataBinder.setValidator(userValidator);
        }
    }

    @RequestMapping("/")
    public String home() {
        return "index";
    }

    @RequestMapping(value = {"/admin/login"}, method = RequestMethod.GET)
    public String login(Model model) {
        return "login.html";
    }

    @RequestMapping(value = {"/admin/userInfo"}, method = RequestMethod.GET)
    public String userInfo(Model model) {
        return UserCase.getUserInfo(usersDAO, model);
    }

    @RequestMapping(value = "/admin/usersList", method = RequestMethod.GET)
    public String usersList(Model model, @RequestParam(value = "page", defaultValue = "1") String pageStr) {
        return ManagerCase.getUsersList(usersDAO, pageStr, model);
    }

    @RequestMapping(value = "/buyerOrders", method = RequestMethod.GET)
    public String findActiveOrderByBuyer(Model model,
                                         @RequestParam(value = "page", defaultValue = "1") String pageStr) {
        return UserCase.getOrders(usersDAO, orderDAO, pageStr, model);
    }

    @RequestMapping(value = "/cancelOrder", method = RequestMethod.GET)
    public String cancelOrder(Model model, @RequestParam(value = "id", defaultValue = "0") int idOrder) {
        return ManagerCase.cancelOrder(idOrder, model);
    }

    @RequestMapping(value = "/cancelOrder", method = RequestMethod.POST)
    public String cancelConfirm(Model model, @ModelAttribute("UtilForm") UtilForm utilForm) {
        return ManagerCase.cancelOrderPost(masterDAO, orderDAO, utilForm);
    }

    @RequestMapping(value = "/prepareResult", method = RequestMethod.GET)
    public String prepareResult(Model model,
                                @RequestParam(value = "page", defaultValue = "1") int page,
                                @RequestParam(value = "id", defaultValue = "0") String id) {
        return ManagerCase.checkupForAdmin(offerDAO, orderDAO, id, page, model);
    }

    @RequestMapping(value = "/prepareResult", method = RequestMethod.POST)
    public String prepareResult(Model model,
                                @ModelAttribute("searchOffer")
                                @Validated OffersInfo searchOffer,
                                BindingResult result,
                                final RedirectAttributes redirectAttributes,
                                @ModelAttribute("offersInfo") OffersInfo offersInfo,
                                @ModelAttribute("orderInfo") OrderInfo orderInfo) {
        return ManagerCase.checkupForAdminPost(usersDAO, offerDAO, masterDAO, orderDAO, emailSender, searchOffer, orderInfo);
    }

    @RequestMapping(value = "/removeUser", method = RequestMethod.GET)
    public String removeUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User activeUser = usersDAO.findByLogin(authentication.getName());
        return UserCase.removeUser(activeUser.getIdUser(), model);
    }

    @RequestMapping(value = "/removeUser", method = RequestMethod.POST)
    public String removeUser(Model model, @ModelAttribute("UtilForm") UtilForm utilForm) {
        return UserCase.removeUserPost(usersDAO, orderDAO, utilForm);
    }

    @RequestMapping(value = "/changeOrderStatus", method = RequestMethod.GET)
    public String changeOrderStatus(Model model,
                                    @RequestParam(value = "id", defaultValue = "0") String id) {
        return ManagerCase.changeOrderStatus(orderDAO, id, model);
    }

    @RequestMapping(value = "/changeOrderStatus", method = RequestMethod.POST)
    public String changeOrderStatus(Model model,
                                    @ModelAttribute("signUpForm")
                                    @Validated SignUpForm signUpForm,
                                    BindingResult result,
                                    final RedirectAttributes redirectAttributes,
                                    @ModelAttribute("orderInfo") OrderInfo orderInfo) {

        return ManagerCase.changeOrderStatusPost(masterDAO, orderDAO, orderInfo, signUpForm);

    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String viewRegister(Model model) {
        return UserCase.registerUser(model);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String saveRegister(Model model,
                               @ModelAttribute("usersForm")
                               @Validated UsersForm usersForm,
                               BindingResult result,
                               final RedirectAttributes redirectAttributes) {
        return UserCase.registerUserPost(usersDAO, usersForm, result, model);
    }

    @RequestMapping(value = "/checkupForUser", method = RequestMethod.GET)
    public String offersPageResultOfCheckupForUser(Model model,
                                                   @RequestParam(value = "page", defaultValue = "1") int page) {
        return UserCase.checkupForUser(usersDAO, orderDAO, page, model);
    }

    @RequestMapping(value = "/offersPage", method = RequestMethod.GET)
    public String showOffers(Model model,
                             @RequestParam(value = "page", defaultValue = "1") int page) {
        return UserCase.getOffers(offerDAO, page, model);
    }

    //@RequestMapping(value = "/signUp", method = RequestMethod.GET)
    //public String reserve(Model model,
    //                      @RequestParam(value = "id", defaultValue = "-1") String id) {
    //    int idOffer = -1;
    //    try {
    //        idOffer = Integer.parseInt(id);
    //    } catch (Exception ex) {
    //        ex.printStackTrace();
    //    }
    //    Offer offer = null;
    //    if (idOffer != -1) {
    //        offer = offerDAO.findByIdOffer(idOffer);
    //    }
    //    OffersInfo offersInfo = null;
    //    SignUpForm signUpForm = null;
    //    if (offer != null) {
    //        offersInfo = new OffersInfo(offer);
    //        signUpForm = new SignUpForm();
    //        signUpForm.setIdOffer(idOffer);
    //        signUpForm.setDateStart(new Date());
    //        signUpForm.setDateFinish(new Date());
    //        model.addAttribute("offersInfo", offersInfo);
    //        model.addAttribute("signUpForm", signUpForm);
    //        model.addAttribute("error", new Boolean(false));
    //    } else return "redirect:/offersPage";
    //    return "signup";
    //}


    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public String reserveAddInfo(Model model,
                                 @ModelAttribute("searchOffer")
                                 @Validated OffersInfo searchOffer,
                                 BindingResult result,
                                 final RedirectAttributes redirectAttributes,
                                 @ModelAttribute("offersInfo") OffersInfo offersInfo,
                                 @ModelAttribute("signUpForm") SignUpForm signUpForm) {

        return UserCase.addOrder(usersDAO, offerDAO, masterDAO, orderDAO, searchOffer, signUpForm);
    }

    @RequestMapping(value = "/submitResult", method = RequestMethod.POST)
    public String submitResult(Model model,
                               @ModelAttribute("searchOffer")
                               @Validated OffersInfo searchOffer,
                               BindingResult result,
                               final RedirectAttributes redirectAttributes,
                               @ModelAttribute("offerInfo") OffersInfo offersInfo,
                               @ModelAttribute("signUpForm") SignUpForm signUpForm) {

        return UserCase.submitResultСheckup(usersDAO, offerDAO, masterDAO, orderDAO, searchOffer, signUpForm);
    }

    @RequestMapping(value = "/changeOrderTime", method = RequestMethod.GET)
    public String changeOrderTime(Model model,
                                  @RequestParam(value = "id", defaultValue = "0") String id) {
        return ManagerCase.changeOrderTime(orderDAO, id, model);
    }

    @RequestMapping(value = "/changeOrderTime", method = RequestMethod.POST)
    public String changeOrderTime(Model model,
                                  @ModelAttribute("signUpForm")
                                  @Validated SignUpForm signUpForm,
                                  BindingResult result,
                                  final RedirectAttributes redirectAttributes,
                                  @ModelAttribute("orderInfo") OrderInfo orderInfo) {
        return ManagerCase.changeOrderTimePost(usersDAO, emailSender, orderInfo, signUpForm);
    }

    @RequestMapping(value = "/acceptChangeTime", method = RequestMethod.GET)
    public String acceptChangeTime(Model model,
                                   @RequestParam(value = "order", defaultValue = "-1") String id,
                                   @RequestParam(value = "date", defaultValue = "null") String time,
                                   @RequestParam(value = "userId", defaultValue = "-1") String userId) {
        return UserCase.changeOrderResponse(usersDAO, orderDAO, id, time, userId, model);
    }


    @RequestMapping(value = "/acceptChangeTime", method = RequestMethod.POST)
    public String acceptChangeTime(Model model,
                                   @ModelAttribute("utilForm") UtilForm utilForm,
                                   BindingResult result,
                                   final RedirectAttributes redirectAttributes,
                                   @ModelAttribute("orderInfo") OrderInfo orderInfo) {

        return UserCase.changeOrderResponsePost(masterDAO, orderDAO, utilForm, orderInfo);
    }


    @RequestMapping(value = "/admin/viewAllUsersOrdersForAdmin", method = RequestMethod.GET)
    public String userOffersForAdmin(Model model,
                                     @RequestParam(value = "page", defaultValue = "1") String pageStr) {
        return ManagerCase.getUsersOrdersList(orderDAO, pageStr, model);
    }

    @RequestMapping(value = "/admin/viewUserOrdersForAdmin", method = RequestMethod.GET)
    public String userOrdersForAdmin(Model model,
                                     @RequestParam(value = "page", defaultValue = "1") String pageStr,
                                     @RequestParam(value = "id", defaultValue = "0") int id) {
        return ManagerCase.getConcreteUserOrdersList(usersDAO, orderDAO, pageStr, id, model);
    }


}