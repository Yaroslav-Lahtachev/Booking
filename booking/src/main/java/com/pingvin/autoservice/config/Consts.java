package com.pingvin.autoservice.config;

public class Consts {

    final public static String MESSAGE_ABOUT_CHANGING_TIME = "Hello, unfortunately, (%s). Your order on pingvin.com was delayed. We are happy to provide you new Finish Date. Good luck in your next try, dude. Use this link to accept or decline changes in order http://localhost:8080/acceptChangeTime?date=(%s)&order=(%s)";

    final public static String CREATED_STATUS = "CREATED";
    final public static String WAITING_FOR_KIT_STATUS = "WAITING_FOR_KIT";
    final public static String KIT_WAS_ORDERED_STATUS = "KIT_WAS_ORDERED";
    final public static String KIT_ON_ITS_WAY_STATUS = "KIT_ON_ITS_WAY";
    final public static String KIT_WAS_DELIVERED_STATUS = "KIT_WAS_DELIVERED";
    final public static String WAITING_FOR_MASTER_STATUS = "WAITING_FOR_MASTER";
    final public static String IN_PROGRESS_STATUS = "IN_PROGRESS";
    final public static String DONE_STATUS = "DONE";

    final public static int TIME_FOR_DELIVER_KIT = 120;

}
