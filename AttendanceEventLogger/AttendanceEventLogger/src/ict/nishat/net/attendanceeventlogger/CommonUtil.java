package ict.nishat.net.attendanceeventlogger;

import java.io.IOException;

import java.math.BigInteger;

import java.security.SecureRandom;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.util.List;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class CommonUtil {

    public static final String CURR_MONTH_NAME = "month_name";
    public static final String CURR_MONTH_DAYS = "month_days";

    public static String limitSubtractTime(String time1, String time2) {
        
        //CommonUtil.log(time1+" nadia "+ time2);

        String difference = "";

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
            java.util.Date d1 = formatter.parse(time1);
            java.util.Date d2 = formatter.parse(time2);
            long timeDiff = d2.getTime() - d1.getTime();
            //CommonUtil.log("nadia : "+timeDiff);
            long diffMinutes = timeDiff / (60 * 1000) % 60;
            //CommonUtil.log("nadia : "+diffMinutes);
            long diffHours = timeDiff / (60 * 60 * 1000) % 24;
            //CommonUtil.log("nadia : "+diffHours);
            long totalMin = (diffHours * 60) + diffMinutes;
            //CommonUtil.log("nadia total mis"+totalMin);
            if (totalMin <= 0) {
                diffHours = 0;
                diffMinutes = 0;
            }
            if (totalMin >= 480) {
                diffHours = 8;
                diffMinutes = 0;
            }


            difference = diffHours + "#" + diffMinutes;

        } catch (Exception e) {
            difference = "";
            e.printStackTrace();
        }
        return difference;
    }
    
    public static String addHourToAmPMTime(String time,String hours){
                Date date = null;
                SimpleDateFormat formatter = new SimpleDateFormat("h:mm");
            try {
                
            date = formatter.parse(time);
            
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
            Calendar c= Calendar.getInstance();    
            c.setTime(date);
            c.add(Calendar.HOUR, Integer.parseInt(hours.split(":")[0]));
            c.add(Calendar.MINUTE,  Integer.parseInt(hours.split(":")[1]));
            return c.get(Calendar.HOUR)+":"+c.get(Calendar.MINUTE);
            
    }

    public static void log(String chars) {
        System.out.println("###>>>>    " + chars + "    <<<<###");
    }

    public static List<Date> getDatesBetween(Date fromDate, Date toDate) {
        List<Date> dates = new ArrayList<Date>();
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(fromDate);
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(toDate);
        endDate.add(Calendar.DATE,
                    1); //INCREASING THE END DATE BY ONE TO GET FROM-DATE INCLUDED
        while (startDate.getTime().before(endDate.getTime())) {
            dates.add(startDate.getTime());
            startDate.add(Calendar.DATE, 1);
        }
        return dates;
    }

    public static String subtractTime(String time1, String time2) {

        String difference = "";

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
            java.util.Date d1 = formatter.parse(time1);
            java.util.Date d2 = formatter.parse(time2);
            long timeDiff = d2.getTime() - d1.getTime();
            long diffMinutes = timeDiff / (60 * 1000) % 60;
            long diffHours = timeDiff / (60 * 60 * 1000) % 24;
            long totalMin = (diffHours * 60) + diffMinutes;
            //            if (totalMin<=0) {
            //                diffHours = 0;
            //                diffMinutes = 0;
            //            }
            //            if (totalMin>=480) {
            //                diffHours = 8;
            //                diffMinutes = 0;
            //            }
            difference = diffHours + "#" + diffMinutes;

        } catch (Exception e) {
            difference = "";
            e.printStackTrace();
        }
        return difference;
    }


    public static String randomString() {
        String random = new BigInteger(130, new SecureRandom()).toString(32);
        return (random.length() > 6 ? random.substring(0, 6) : random);
    }


    public static int convertTimeStringToMin(String effectiveWorkedHours) {
        int min =
            (Integer.parseInt(effectiveWorkedHours.split(":")[0]) * 60) + (Integer.parseInt(effectiveWorkedHours.split(":")[1]));
        return min;
    }


    public static Object getCurrent(String key) {
        Object result = null;
        if (key.equals(CURR_MONTH_DAYS)) {
            result = Calendar.getInstance().getActualMaximum(Calendar.YEAR);
        }
        if (key.equals(CURR_MONTH_NAME)) {
            DateFormatSymbols dfs = new DateFormatSymbols();
            String[] months = dfs.getShortMonths();
            result = months[Calendar.getInstance().get(Calendar.MONTH)];
        }
        return result;
    }
    
    public static int convertTimeToMinutes(String time) {
        return (Integer.parseInt(time.split(":")[0]) *60) +
                Integer.parseInt(time.split(":")[1]);
    }
}
