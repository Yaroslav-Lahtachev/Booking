package com.pingvin.autoservice.controller;


import com.pingvin.autoservice.dao.OfferDAO;
import com.pingvin.autoservice.dao.OrderHistoryDAO;
import com.pingvin.autoservice.dao.SellerDAO;
import com.pingvin.autoservice.dao.UsersDAO;
import com.pingvin.autoservice.entity.Offer;
import com.pingvin.autoservice.entity.Parts;
import com.pingvin.autoservice.entity.User;
import com.lahtachev.booking.form.*;
import com.pingvin.autoservice.form.*;
import com.pingvin.autoservice.model.OffersInfo;
import com.pingvin.autoservice.model.OrderHistoryInfo;
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

import java.util.Date;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private UsersDAO usersDAO;
    @Autowired
    private SellerDAO sellerDAO;
    @Autowired
    private OfferDAO offerDAO;
    @Autowired
    private OrderHistoryDAO orderHistoryDAO;

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

        if (target.getClass() == ReserveForm.class) {
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
    @RequestMapping(value = "/newOffer", method = RequestMethod.GET)
    public String newOffer(Model model, @RequestParam(value = "id", defaultValue = "-1") int idOffer, @RequestParam(value = "idUser", defaultValue = "-1") int idUser) {
        OffersInfo offersInfo = null;
        OfferForm offerForm = null;
        if (idOffer != -1) {
            Offer offer = offerDAO.findByIdOffer(idOffer);
            if (offer != null) {
                offersInfo = new OffersInfo(offer);
                offerForm = new OfferForm(offersInfo);
            }
        }
        if (offersInfo == null) {
            offersInfo = new OffersInfo();
            offerForm = new OfferForm();
            offersInfo.setNewOffer(true);
        } else {
            offersInfo.setNewOffer(false);
            offerForm.setNewOffer(false);
        }
        offersInfo.setIdSeller(idUser);
        offerForm.setIdSeller(idUser);
        model.addAttribute("offerForm", offerForm);

        return "newOffer";
    }

    @RequestMapping(value = "/newOffer", method = RequestMethod.POST)
    public String saveUserOffer(Model model,
                                @ModelAttribute("offerForm")
                                @Validated OfferForm offerForm,
                                BindingResult result,
                                final RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (result.hasErrors()) {
            return "newOffer";
        }
        OffersInfo offersInfo = new OffersInfo(offerForm);
        try {
            Parts seller = null;
            if (offersInfo.getIdSeller() == -1) {
                seller = sellerDAO.addNewOffer(usersDAO.findByLogin(authentication.getName()));
            } else {
                seller = sellerDAO.addNewOffer(usersDAO.findByIdUser(offersInfo.getIdSeller()));
            }
            if (seller != null) {
                offerDAO.createNewOffer(seller, offersInfo);
                if (offersInfo.isNewOffer()) {
                    System.out.println(offersInfo.isNewOffer());
                    orderHistoryDAO.saveNewOfferInOrder(seller, null, null);
                }
            } else
                return "accessesDenied";
        } catch (Exception e) {
            String message = e.getMessage();
            model.addAttribute("message", message);
            return "newOffer";
        }
        return "redirect:/sellerOrders";
    }


    //
    //
    //


    @RequestMapping(value = "/buyerOrders", method = RequestMethod.GET)
    public String findActiveOrderByBuyer(Model model,
                                         @RequestParam(value = "page", defaultValue = "1") String pageStr,
                                         @RequestParam(value = "type", defaultValue = "reserved") String type) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String status = null;
        boolean reserved = false;
        int page = 1;
        try {
            page = Integer.parseInt(pageStr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        switch (type) {
            case "reserved":
                status = "reserve";
                reserved = true;
                break;
            case "performed":
                status = "performed";
                break;
        }
        User user = usersDAO.findByLogin(authentication.getName());
        PaginationResult<OrderHistoryInfo> paginationResult =
                orderHistoryDAO.findOrderByBuyer(user, status, page, MAX_RESULT, MAX_NAVIGATION_PAGE);
        model.addAttribute("paginationResult", paginationResult);
        model.addAttribute("Reserved", reserved);
        model.addAttribute("type", type);
        return "buyerOrders";
    }


    @RequestMapping(value = "/sellerOrders", method = RequestMethod.GET)
    public String findOrderBySeller(Model model,
                                    @RequestParam(value = "page", defaultValue = "1") String pageStr,
                                    @RequestParam(value = "type", defaultValue = "active") String type) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int page = 1;
        String status = null;
        boolean reserved = false;
        boolean performed = false;
        try {
            page = Integer.parseInt(pageStr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        switch (type) {
            case "active":
                status = "offer";
                break;
            case "reserved":
                reserved = true;
                status = "reserve";
                break;
            case "performed":
                reserved = true;
                status = "performed";
                performed = true;
                break;
        }
        User user = usersDAO.findByLogin(authentication.getName());

        PaginationResult<OrderHistoryInfo> paginationResult = orderHistoryDAO.findOrderHistoryBySeller(user, status, page, MAX_RESULT, MAX_NAVIGATION_PAGE);

        model.addAttribute("paginationResult", paginationResult);
        model.addAttribute("Reserved", reserved);
        model.addAttribute("Performed", performed);
        model.addAttribute("type", type);
        return "sellerOrders";
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
            String status = new String("canceled");
            orderHistoryDAO.changeOrderStatus(utilForm.getIntField(), status);
        }
        return "index";
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
        }
        // Other error!!
        catch (Exception e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "registerPage";
        }
        return "redirect:/admin/login";
    }


    @RequestMapping(value = "/offersPage", method = RequestMethod.GET)
    public String showOffers(Model model,
                             @RequestParam(value = "page", defaultValue = "1") int page,
                             @RequestParam(value = "search", defaultValue = "false") boolean search,
                             @RequestParam(value = "city", defaultValue = "") String city,
                             @RequestParam(value = "address", defaultValue = "") String address,
                             @RequestParam(value = "mpc", defaultValue = "0") int maxPeopleCount,
                             @RequestParam(value = "price", defaultValue = "0.0") double price,
                             @RequestParam(value = "parking", defaultValue = "false") boolean parking,
                             @RequestParam(value = "wifi", defaultValue = "false") boolean wifi,
                             @RequestParam(value = "animal", defaultValue = "false") boolean animal,
                             @RequestParam(value = "smoking", defaultValue = "false") boolean smoking) {
        String status = new String("offer");
        OffersInfo searchOffer = new OffersInfo(city, address, maxPeopleCount, price, parking, wifi, animal, smoking, search);
        if (!searchOffer.isNewOffer()) {
            PaginationResult<OffersInfo> paginationResult = offerDAO.listOffersInfo(status, page, MAX_RESULT,
                    MAX_NAVIGATION_PAGE);
            model.addAttribute("paginationResult", paginationResult);
        } else {
            PaginationResult<OffersInfo> paginationResult = offerDAO.findOffersInfo(searchOffer, status, page, MAX_RESULT, MAX_NAVIGATION_PAGE);
            model.addAttribute("paginationResult", paginationResult);
        }
        model.addAttribute("searchOffer", searchOffer);
        return "offersPage";
    }

    @RequestMapping(value = "/offersPage", method = RequestMethod.POST)
    public String searchOffer(Model model, @ModelAttribute("searchOffer") @Validated OffersInfo searchOffer, BindingResult result) {
        String status = new String("offer");
        if (result.hasErrors()) {
            return "offersPage";
        }
        searchOffer.setNewOffer(true);
        PaginationResult<OffersInfo> paginationResult = offerDAO.findOffersInfo(searchOffer, status, 1, MAX_RESULT, MAX_NAVIGATION_PAGE);
        model.addAttribute("paginationResult", paginationResult);
        model.addAttribute("searchOffer", searchOffer);
        return "offersPage";
    }

    @RequestMapping(value = "/reserve", method = RequestMethod.GET)
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

        ReserveForm reserveForm = null;
        if (offer != null) {
            offersInfo = new OffersInfo(offer);
            reserveForm = new ReserveForm();
            reserveForm.setIdOffer(idOffer);
            reserveForm.setDateStart(new Date());
            reserveForm.setDateFinish(new Date());
            model.addAttribute("offersInfo", offersInfo);
            model.addAttribute("reserveForm", reserveForm);
            model.addAttribute("error", new Boolean(false));
        } else return "redirect:/offersPage";
        return "reserve";
    }


    @RequestMapping(value = "/reserve", method = RequestMethod.POST)
    public String reserveAddInfo(Model model,
                                 @ModelAttribute("reserveForm")
                                 @Validated ReserveForm reserveForm,
                                 BindingResult result,
                                 final RedirectAttributes redirectAttributes,
                                 @ModelAttribute("offersInfo") OffersInfo offersInfo) {
        if (result.hasErrors()) {
            model.addAttribute("offersInfo", offersInfo);
            List<OrderHistoryInfo> calendar = orderHistoryDAO.getInfoConcretOrder(offersInfo.getIdOffer(), "reserve");
            model.addAttribute("calendar", calendar);
            model.addAttribute("error", new Boolean(true));
            return "reserve";
        }
        OrderHistoryInfo orderHistoryInfo = new OrderHistoryInfo(reserveForm);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (orderHistoryDAO.CheckReserveOfferByIdAndDate(orderHistoryInfo.getIdOffer(), orderHistoryInfo.getDateStart(), orderHistoryInfo.getDateFinish())) {
            User buyer = usersDAO.findByLogin(authentication.getName());
            Parts seller = sellerDAO.findSellerByOffer(orderHistoryInfo.getIdOffer());
            if (orderHistoryDAO.checkOnOwnership(buyer, seller)) {
                orderHistoryDAO.reserve(buyer, seller, orderHistoryInfo.getDateStart(), orderHistoryInfo.getDateFinish());
                return "redirect:/buyerOrders";
            } else return "itsYourOwnOfferDude";
        } else return "index";

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

    @RequestMapping(value = "/admin/viewUserOffersForAdmin", method = RequestMethod.GET)
    public String userOffersForAdmin(Model model,
                                     @RequestParam(value = "page", defaultValue = "1") String pageStr,
                                     @RequestParam(value = "id", defaultValue = "0") int id) {
        int page = 1;
        try {
            page = Integer.parseInt(pageStr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        User seller = usersDAO.findByIdUser(id);
        PaginationResult<OffersInfo> paginationResult = offerDAO.listOffersByIdUser(seller, page, MAX_RESULT, MAX_NAVIGATION_PAGE);
        model.addAttribute("paginationResult", paginationResult);
        model.addAttribute("idUser", id);

        return "viewUserOffersForAdmin";
    }

    @RequestMapping(value = "/admin/viewUserOrdersForAdmin", method = RequestMethod.GET)
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
            PaginationResult<OrderHistoryInfo> paginationResult = orderHistoryDAO.findOrderByBuyer(buyer, "reserve", page, MAX_RESULT, MAX_NAVIGATION_PAGE);
            model.addAttribute("paginationResult", paginationResult);
            model.addAttribute("id", id);
        } else return "redirect:/usersList";
        return "viewUserOrdersForAdmin";
    }

    @RequestMapping(value = "/admin/blockPage")
    public String blockPage(Model model,
                            @RequestParam(value = "id", defaultValue = "0") int id,
                            @RequestParam(value = "status", defaultValue = "0") int status) {
        String setStatus = new String();
        switch (status) {
            case -1:
                setStatus = "BLOCKED";
                orderHistoryDAO.blockUnblockOffer(id, "blocked");
                break;
            case 1:
                setStatus = "USER";
                break;
        }
        usersDAO.changeUserRole(id, setStatus);
        return "redirect:/admin/usersList";
    }

}