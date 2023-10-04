package com.ecommerce.user.payloads;

public class TimeUtil {

    public static String miliTominuteConvter(long milSeconds){
        return String.valueOf(milSeconds/60000)+" Minutes"; // 1 minute = 60,000 milliseconds
    }

    public static String miliToHourConvter(long milSeconds){
        return String.valueOf(milSeconds/3600000)+" Hour"; // 1 hour = 3,600,000 milliseconds
    }
}
