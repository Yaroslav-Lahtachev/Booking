package com.pingvin.autoservice.controller;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;
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
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.pingvin.autoservice.model.OrderInfo.timeCut;

@Controller
public class MainController {

    @Autowired
    private UsersDAO usersDAO;
    @Autowired
    private SellerDAO sellerDAO;
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

    @Autowired
    private OfferValidator offerValidator;

    @Autowired
    private ReserveValidator reserveValidator;

    @InitBinder
    protected void initBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }

        if (target.getClass() == UsersForm.class) {
            dataBinder.setValidator(userValidator);
        }

        if (target.getClass() == OfferForm.class) {
            dataBinder.setValidator(offerValidator);
        }

        if (target.getClass() == SignUpForm.class) {
            dataBinder.setValidator(reserveValidator);
        }
    }

    @RequestMapping("/")
    public String home() {
        return "index";
    }

    //
    //      LOGIN
    //
    @RequestMapping(value = {"/admin/login"}, method = RequestMethod.GET)
    public String login(Model model) {
        return "login.html";
    }


    //
    //      User
    //
    @RequestMapping(value = {"/admin/userInfo"}, method = RequestMethod.GET)
    public String userInfo(Model model) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UsersInfo userInfo = new UsersInfo(usersDAO.findByLogin(userDetails.getUsername()));

        model.addAttribute("userDetails", userDetails);
        model.addAttribute("userInfo", userInfo);
        return "userInfo";
    }

    //
    //      Offer
    //
    //@RequestMapping(value = "/newOffer", method = RequestMethod.GET)                  //todo добавление новых услуг?
    //public String newOffer(Model model, @RequestParam(value = "id", defaultValue = "-1") int idOffer, @RequestParam(value = "idUser", defaultValue = "-1") int idUser) {
    //    OffersInfo offersInfo = null;
    //    OrderForm offerForm = null;
    //    if (idOffer != -1) {
    //        Offer offer = offerDAO.findByIdOffer(idOffer);
    //        if (offer != null) {
    //            offersInfo = new OffersInfo(offer);
    //            offerForm = new OrderForm(offersInfo);
    //        }
    //    }
    //    if (offersInfo == null) {
    //        offersInfo = new OffersInfo();
    //        offerForm = new OrderForm();
    //        offersInfo.setNewOffer(true);
    //    } else {
    //        offersInfo.setNewOffer(false);
    //        offerForm.setNewOffer(false);
    //    }
    //    offersInfo.setIdSeller(idUser);
    //    offerForm.setIdSeller(idUser);
    //    model.addAttribute("offerForm", offerForm);
//
    //    return "newOffer";
    //}

    //@RequestMapping(value = "/newOffer", method = RequestMethod.POST)
    //public String saveUserOffer(Model model,
    //                            @ModelAttribute("offerForm")
    //                            @Validated OrderForm offerForm,
    //                            BindingResult result,
    //                            final RedirectAttributes redirectAttributes) {
    //    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //    if (result.hasErrors()) {
    //        return "newOffer";
    //    }
    //    OffersInfo offersInfo = new OffersInfo(offerForm);
    //    try {
    //        Parts seller = null;
    //        if (offersInfo.getIdSeller() == -1) {
    //            seller = sellerDAO.addNewOffer(usersDAO.findByLogin(authentication.getName()));
    //        } else {
    //            seller = sellerDAO.addNewOffer(usersDAO.findByIdUser(offersInfo.getIdSeller()));
    //        }
    //        if (seller != null) {
    //            offerDAO.createNewOffer(seller, offersInfo);
    //            if (offersInfo.isNewOffer()) {
    //                System.out.println(offersInfo.isNewOffer());
    //                orderHistoryDAO.saveNewOfferInOrder(seller, null, null);
    //            }
    //        } else
    //            return "accessesDenied";
    //    } catch (Exception e) {
    //        String message = e.getMessage();
    //        model.addAttribute("message", message);
    //        return "newOffer";
    //    }
    //    return "redirect:/sellerOrders";
    //}


    //
    //
    //


    @RequestMapping(value = "/buyerOrders", method = RequestMethod.GET)       //todo
    public String findActiveOrderByBuyer(Model model,
                                         @RequestParam(value = "page", defaultValue = "1") String pageStr
            /*@RequestParam(value = "type", defaultValue = "reserved") String type*/) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //String status = null;
        //boolean reserved = false;
        int page = 1;
        try {
            page = Integer.parseInt(pageStr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //switch (type) {
        //    case "reserved":
        //        status = "reserve";
        //        reserved = true;
        //        break;
        //    case "performed":
        //        status = "performed";
        //        break;
        //}
        User user = usersDAO.findByLogin(authentication.getName());
        PaginationResult<OrderInfo> paginationResult =
                orderDAO.findOrderByCustomer(user, page, MAX_RESULT, MAX_NAVIGATION_PAGE);
        //for (int i=0;i<paginationResult.getList().size();i++){
        //    paginationResult.getList().get(i).setStatus(calculateStatus(paginationResult.getList().get(i)));
        //}
        model.addAttribute("paginationResult", paginationResult);
        //model.addAttribute("Reserved", reserved);
        //model.addAttribute("type", type);
        return "buyerOrders";
    }


    //@RequestMapping(value = "/sellerOrders", method = RequestMethod.GET)
    //public String findOrderBySeller(Model model,
    //                                @RequestParam(value = "page", defaultValue = "1") String pageStr,
    //                                @RequestParam(value = "type", defaultValue = "active") String type) {
    //    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //    int page = 1;
    //    String status = null;
    //    boolean reserved = false;
    //    boolean performed = false;
    //    try {
    //        page = Integer.parseInt(pageStr);
    //    } catch (Exception ex) {
    //        ex.printStackTrace();
    //    }
    //    switch (type) {
    //        case "active":
    //            status = "offer";
    //            break;
    //        case "reserved":
    //            reserved = true;
    //            status = "reserve";
    //            break;
    //        case "performed":
    //            reserved = true;
    //            status = "performed";
    //            performed = true;
    //            break;
    //    }
    //    User user = usersDAO.findByLogin(authentication.getName());
//
    //    PaginationResult<OrderHistoryInfo> paginationResult = orderHistoryDAO.findOrderHistoryBySeller(user, status, page, MAX_RESULT, MAX_NAVIGATION_PAGE);
//
    //    model.addAttribute("paginationResult", paginationResult);
    //    model.addAttribute("Reserved", reserved);
    //    model.addAttribute("Performed", performed);
    //    model.addAttribute("type", type);
    //    return "sellerOrders";
    //}


    @RequestMapping(value = "/cancelOrder", method = RequestMethod.GET)         //todo
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
            String status = new String("canceled");
            //orderDAO.changeOrderStatus(utilForm.getIntField(), status);
        }
        return "index";
    }

    @RequestMapping(value = "/changeOrderStatus", method = RequestMethod.GET)               //todo
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

    @RequestMapping(value = "/register", method = RequestMethod.POST)               //todo add email
    public String saveRegister(Model model,
                               @ModelAttribute("usersForm")
                               @Validated UsersForm usersForm,
                               BindingResult result,
                               final RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "registerPage";
        }
        try {
            usersDAO.addNewUser(usersForm);                                         //todo here
        }
        // Other error!!
        catch (Exception e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "registerPage";
        }
        return "redirect:/admin/login";
    }


    @RequestMapping(value = "/offersPage", method = RequestMethod.GET)            //todo услуги будут тут?
    public String showOffers(Model model,
                             @RequestParam(value = "page", defaultValue = "1") int page/*,
                             @RequestParam(value = "search", defaultValue = "false") boolean search,
                             @RequestParam(value = "city", defaultValue = "") String city,
                             @RequestParam(value = "address", defaultValue = "") String address,
                             @RequestParam(value = "mpc", defaultValue = "0") int maxPeopleCount,
                             @RequestParam(value = "price", defaultValue = "0.0") double price,
                             @RequestParam(value = "parking", defaultValue = "false") boolean parking,
                             @RequestParam(value = "wifi", defaultValue = "false") boolean wifi,
                             @RequestParam(value = "animal", defaultValue = "false") boolean animal,
                             @RequestParam(value = "smoking", defaultValue = "false") boolean smoking*/) {
        //String status = new String("offer");
        OffersInfo searchOffer = new OffersInfo();
        PaginationResult<OffersInfo> paginationResult = offerDAO.findOffersInfo(searchOffer, page, MAX_RESULT, MAX_NAVIGATION_PAGE);
        model.addAttribute("paginationResult", paginationResult);

        model.addAttribute("searchOffer", searchOffer);
        return "offersPage";
    }

    //@RequestMapping(value = "/offersPage", method = RequestMethod.POST)
    //public String searchOffer(Model model, @ModelAttribute("searchOffer") @Validated OffersInfo searchOffer, BindingResult result) {
    //    String status = new String("offer");
    //    if (result.hasErrors()) {
    //        return "offersPage";
    //    }
    //    searchOffer.setNewOffer(true);
    //    PaginationResult<OffersInfo> paginationResult = offerDAO.findOffersInfo(searchOffer, status, 1, MAX_RESULT, MAX_NAVIGATION_PAGE);
    //    model.addAttribute("paginationResult", paginationResult);
    //    model.addAttribute("searchOffer", searchOffer);
    //    return "offersPage";
    //}

    @RequestMapping(value = "/signUp", method = RequestMethod.GET)               //todo
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
//
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
                                 @ModelAttribute("signUpForm")
                                 @Validated SignUpForm signUpForm,
                                 BindingResult result,
                                 final RedirectAttributes redirectAttributes,
                                 @ModelAttribute("offersInfo") OffersInfo offersInfo) {
        //if (result.hasErrors()) {
        //    model.addAttribute("offersInfo", offersInfo);
        //    List<OrderInfo> calendar = orderDAO.getInfoConcretOrder(offersInfo.getIdOffer(), "signUp");
        //    model.addAttribute("calendar", calendar);
        //    model.addAttribute("error", new Boolean(true));
        //    return "signUp";
        //}
        OrderInfo orderInfo = new OrderInfo(signUpForm.getIdOffer(), signUpForm.getDateStart(), signUpForm.getDateFinish());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //if (orderDAO.CheckReserveOfferByIdAndDate(orderHistoryInfo.getIdOffer(), orderHistoryInfo.getDateStart(), orderHistoryInfo.getDateFinish())) {
        User buyer = usersDAO.findByLogin(authentication.getName());
        Offer offer = offerDAO.findByIdOffer(orderInfo.getOffer());
        Master master = masterDAO.findByIdMaster(masterDAO.getFreeMaster(offer.getIdOffer()));
        Date dateFinish = new Date(orderInfo.getDateStart().getTime() + TimeUnit.SECONDS.toMillis(offer.getTime()));
        int isNeedParts = signUpForm.getNeedKit() ? 1 : 0;
        //if (orderDAO.checkOnOwnership(buyer, seller)) {
        orderDAO.reserve(buyer, master, offer, isNeedParts, orderInfo.getDateStart(), dateFinish); //нафиг нам мнение клиента лол
        return "redirect:/buyerOrders";
        //} else return "itsYourOwnOfferDude";
        //} else return "index";

    }

    @RequestMapping(value = "/changeOrderTime", method = RequestMethod.GET)               //todo
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
        //@DateTimeFormat(pattern = "yyyy-MM-dd")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date nd = timeCut(signUpForm.getDateStart());

        String sendTo = usersDAO.findByIdUser(orderInfo.getCustomer()).getEmail();
        sendSimpleMessage(Consts.MESSAGE_ABOUT_CHANGING_TIME + simpleDateFormat.format(nd), sendTo, "yo, dude");
        return "redirect:/admin/usersList";
    }

    @RequestMapping(value = "/acceptChangeTime", method = RequestMethod.GET)
    public String acceptChangeTime(Model model,
                                   @RequestParam(value = "order", defaultValue = "3") String id,
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

        if (date != null && idOrder != 0) {
            Order order = orderDAO.findOrderByIdOrder(idOrder);
            if (order != null) {
                UtilForm utilForm = new UtilForm();
                OrderInfo orderInfo = new OrderInfo(order);
                orderInfo.setDateStart(date);
                model.addAttribute("orderInfo", orderInfo);
                model.addAttribute("utilForm", utilForm);
                //model.addAttribute("date", date);
                model.addAttribute("error", new Boolean(false));
            } else return "redirect:/admin/usersList";
        }
        return "redirect:/acceptChangeTime";
    }


    @RequestMapping(value = "/acceptChangeTime", method = RequestMethod.POST)
    public String acceptChangeTime(Model model,
                                  @ModelAttribute("utilForm") UtilForm utilForm,
                                  BindingResult result,
                                  final RedirectAttributes redirectAttributes,
                                  @ModelAttribute("orderInfo") OrderInfo orderInfo) {
        if (utilForm.getTextField().equals("Agree")) {
            orderDAO.changeOrderDate(orderInfo.getId(), orderInfo.getDateStart());
        }
        else System.out.println("customer is gay, lets delete his order");
        return "redirect:/buyerOrders";
    }

    //
    //admin controller
    //
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

    //@RequestMapping(value = "/admin/blockPage")               //todo ban hammer?
    //public String blockPage(Model model,
    //                        @RequestParam(value = "id", defaultValue = "0") int id,
    //                        @RequestParam(value = "status", defaultValue = "0") int status) {
    //    String setStatus = new String();
    //    switch (status) {
    //        case -1:
    //            setStatus = "BLOCKED";
    //            orderHistoryDAO.blockUnblockOffer(id, "blocked");
    //            break;
    //        case 1:
    //            setStatus = "USER";
    //            break;
    //    }
    //    usersDAO.changeUserRole(id, setStatus);
    //    return "redirect:/admin/usersList";
    //}

}