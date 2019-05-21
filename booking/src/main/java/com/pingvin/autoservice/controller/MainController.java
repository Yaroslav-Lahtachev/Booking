package com.pingvin.autoservice.controller;

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

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UsersInfo userInfo = new UsersInfo(usersDAO.findByLogin(userDetails.getUsername()));

        model.addAttribute("userDetails", userDetails);
        model.addAttribute("userInfo", userInfo);
        return "userInfo";
    }

    @RequestMapping(value = "/buyerOrders", method = RequestMethod.GET)
    public String findActiveOrderByBuyer(Model model,
                                         @RequestParam(value = "page", defaultValue = "1") String pageStr) {
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

    @RequestMapping(value = "/cancelOrder", method = RequestMethod.GET)
    public String cancelOrder(Model model, @RequestParam(value = "id", defaultValue = "0") int idOrder) {
        UtilForm utilForm = new UtilForm();
        utilForm.setIntField(idOrder);
        model.addAttribute("utilForm", utilForm);
        return "cancelOrder";
    }

    @RequestMapping(value = "/cancelOrder", method = RequestMethod.POST)
    public String cancelConfirm(Model model, @ModelAttribute("UtilForm") UtilForm utilForm) {
        if (utilForm.getTextField().equals("CONFIRM")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            orderDAO.removeOrder(utilForm.getIntField());
        }
        return "index";
    }

    @RequestMapping(value = "/removeUser", method = RequestMethod.GET)
    public String removeUser(Model model, @RequestParam(value = "id", defaultValue = "0") int userId) {
        UtilForm utilForm = new UtilForm();
        utilForm.setIntField(userId);
        model.addAttribute("utilForm", utilForm);
        return "removeUser";
    }

    @RequestMapping(value = "/removeUser", method = RequestMethod.POST)
    public String removeUser(Model model, @ModelAttribute("UtilForm") UtilForm utilForm) {
        if (utilForm.getTextField().equals("CONFIRM")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            int idUser = utilForm.getIntField();
            List allOrdersByUser = orderDAO.findOrderByCustomer(idUser);
            if (allOrdersByUser.size() > 0) {
                orderDAO.removeOrders(allOrdersByUser);
            }
            usersDAO.removeUser(utilForm.getIntField());
        }
        return "redirect:/admin/logout";
    }

    @RequestMapping(value = "/changeOrderStatus", method = RequestMethod.GET)
    public String changeOrderStatus(Model model,
                                    @RequestParam(value = "id", defaultValue = "0") String id) {
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

    @RequestMapping(value = "/changeOrderStatus", method = RequestMethod.POST)
    public String changeOrderStatus(Model model,
                                    @ModelAttribute("signUpForm")
                                    @Validated SignUpForm signUpForm,
                                    BindingResult result,
                                    final RedirectAttributes redirectAttributes,
                                    @ModelAttribute("orderInfo") OrderInfo orderInfo) {

        orderDAO.changeOrderStatus(orderInfo.getId(), signUpForm.getStatus());
        return "redirect:/admin/usersList";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String viewRegister(Model model) {
        UsersForm form = new UsersForm();
        model.addAttribute("usersForm", form);
        return "registerPage";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String saveRegister(Model model,
                               @ModelAttribute("usersForm")
                               @Validated UsersForm usersForm,
                               BindingResult result,
                               final RedirectAttributes redirectAttributes) {
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


    @RequestMapping(value = "/offersPage", method = RequestMethod.GET)
    public String showOffers(Model model,
                             @RequestParam(value = "page", defaultValue = "1") int page
    ) {
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

    @RequestMapping(value = "/signUp", method = RequestMethod.GET)
    public String reserve(Model model,
                          @RequestParam(value = "id", defaultValue = "0") String id) {
        int idOffer = 0;
        try {
            idOffer = Integer.parseInt(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Offer offer = null;
        if (idOffer != 0)
            offer = offerDAO.findByIdOffer(idOffer);
        OffersInfo offersInfo = null;
        SignUpForm signUpForm = null;
        if (offer != null) {
            offersInfo = new OffersInfo(offer);
            signUpForm = new SignUpForm();
            signUpForm.setIdOffer(idOffer);
            signUpForm.setDateStart(new Date());
            signUpForm.setDateFinish(new Date());
            model.addAttribute("offersInfo", offersInfo);
            model.addAttribute("signUpForm", signUpForm);
            model.addAttribute("error", new Boolean(false));
        } else return "redirect:/offersPage";
        return "signup";
    }


    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public String reserveAddInfo(Model model,
                                 @ModelAttribute("searchOffer")
                                 @Validated OffersInfo searchOffer,
                                 BindingResult result,
                                 final RedirectAttributes redirectAttributes,
                                 @ModelAttribute("offersInfo") OffersInfo offersInfo,
                                 @ModelAttribute("signUpForm") SignUpForm signUpForm) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User buyer = usersDAO.findByLogin(authentication.getName());
        List<Integer> offers = new ArrayList();
        List<Integer> needKit = new ArrayList();
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
                Master master = masterDAO.findByIdMaster(masterDAO.getFreeMaster(offer.getIdOffer()));
                Date dateFinish = new Date(orderInfo.getDateStart().getTime() + TimeUnit.SECONDS.toMillis(offer.getTime()));
                int isNeedParts = 0;
                for (int j = 0; j < needKit.size(); j++)
                    if (offers.get(i) == needKit.get(j)) {
                        isNeedParts = 1;
                        break;
                    }
                orderDAO.reserve(buyer, master, offer, isNeedParts, orderInfo.getDateStart(), dateFinish);
            }
        }
        return "redirect:/buyerOrders";
    }

    @RequestMapping(value = "/changeOrderTime", method = RequestMethod.GET)
    public String changeOrderTime(Model model,
                                  @RequestParam(value = "id", defaultValue = "0") String id) {
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
        } else return "redirect:/admin/usersList";
        return "changeOrderTime";
    }

    @RequestMapping(value = "/changeOrderTime", method = RequestMethod.POST)
    public String changeOrderTime(Model model,
                                  @ModelAttribute("signUpForm")
                                  @Validated SignUpForm signUpForm,
                                  BindingResult result,
                                  final RedirectAttributes redirectAttributes,
                                  @ModelAttribute("orderInfo") OrderInfo orderInfo) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date newdate = timeCut(signUpForm.getDateFinish());
        String reason = signUpForm.getReason();
        String sendTo = usersDAO.findByIdUser(orderInfo.getCustomer()).getEmail();
        sendSimpleMessage(String.format(Consts.MESSAGE_ABOUT_CHANGING_TIME, reason, simpleDateFormat.format(newdate), orderInfo.getId()), sendTo, "yo, dude");
        return "redirect:/admin/usersList";
    }

    @RequestMapping(value = "/acceptChangeTime", method = RequestMethod.GET)
    public String acceptChangeTime(Model model,
                                   @RequestParam(value = "order", defaultValue = "-1") String id,
                                   @RequestParam(value = "date", defaultValue = "null") String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        int idOrder = 0;
        Date date = null;
        try {
            idOrder = Integer.parseInt(id);
            date = format.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (date != null && idOrder != -1) {
            Order order = orderDAO.findOrderByIdOrder(idOrder);
            if (order != null) {
                UtilForm utilForm = new UtilForm();
                OrderInfo orderInfo = new OrderInfo(order);
                orderInfo.setDateFinish(date);
                model.addAttribute("orderInfo", orderInfo);
                model.addAttribute("utilForm", utilForm);
                model.addAttribute("error", new Boolean(false));
            } else return "redirect:/admin/usersList";
        }
        return "acceptChangeTime";
    }


    @RequestMapping(value = "/acceptChangeTime", method = RequestMethod.POST)
    public String acceptChangeTime(Model model,
                                   @ModelAttribute("utilForm") UtilForm utilForm,
                                   BindingResult result,
                                   final RedirectAttributes redirectAttributes,
                                   @ModelAttribute("orderInfo") OrderInfo orderInfo) {
        if (utilForm.getTextField().equals("Agree")) {
            orderDAO.changeOrderDate(orderInfo.getId(), orderInfo.getDateFinish());
        } else {
            System.out.println("customer is gay, lets delete his order");
            orderDAO.removeOrder(orderInfo.getId());
        }
        return "redirect:/buyerOrders";
    }

    @RequestMapping(value = "/admin/usersList", method = RequestMethod.GET)
    public String usersList(Model model, @RequestParam(value = "page", defaultValue = "1") String pageStr) {
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

    @RequestMapping(value = "/admin/viewAllUsersOrdersForAdmin", method = RequestMethod.GET)
    public String userOffersForAdmin(Model model,
                                     @RequestParam(value = "page", defaultValue = "1") String pageStr) {
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

    @RequestMapping(value = "/admin/viewUserOrdersForAdmin", method = RequestMethod.GET)          //todo
    public String userOrdersForAdmin(Model model,
                                     @RequestParam(value = "page", defaultValue = "1") String pageStr,
                                     @RequestParam(value = "id", defaultValue = "0") int id) {
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

    private void sendSimpleMessage(
            String text, String to, String subject) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

}