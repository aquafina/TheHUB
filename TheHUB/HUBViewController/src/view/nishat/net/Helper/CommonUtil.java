package view.nishat.net.Helper;

import java.io.IOException;

import java.math.BigInteger;

import java.security.SecureRandom;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCDataControl;
import oracle.adf.model.binding.DCIteratorBinding;

import oracle.jbo.server.ViewObjectImpl;

public class CommonUtil {

    public static final String CURR_MONTH_NAME = "month_name";
    public static final String CURR_MONTH_DAYS = "month_days";
    public static final String PREVIOUS_MONTH_NAME = "p_month_name";
    private static String connectionString =
        "jdbc:oracle:thin:@192.168.0.2:1521:oranctex";
    private static String user = "cyber_ncl";
    private static String password = "panther";
    
    private static String connectionString_EBS =
        "jdbc:oracle:thin:@192.168.0.31:1522:prod";
    private static String user_EBS = "xx_e_portal";
    private static String password_EBS = "mskiz145";
    
    

    public static String limitSubtractTime(String time1, String time2,
                                           String totalHours) {
        String difference = "";
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
            java.util.Date d1 = formatter.parse(time1);
            java.util.Date d2 = formatter.parse(time2);
            long timeDiff = d2.getTime() - d1.getTime();
            long diffMinutes = timeDiff / (60 * 1000) % 60;
            long diffHours = timeDiff / (60 * 60 * 1000) % 24;
            long totalMin = (diffHours * 60) + diffMinutes;


            if (totalMin <= 0) {
                diffHours = 0;
                diffMinutes = 0;
                difference =
                        "0" + "#" + (diffMinutes <= 9 ? ("0" + diffMinutes) :
                                     (diffMinutes + ""));
            }
            if (totalMin >= 480) { //480 for 6 hours
                //diffHours = 8;
                diffMinutes = 0;
                difference =
                        totalHours + "#" + (diffMinutes <= 9 ? ("0" + diffMinutes) :
                                            (diffMinutes + ""));
            } else {
                difference =
                        diffHours + "#" + (diffMinutes <= 9 ? ("0" + diffMinutes) :
                                           (diffMinutes + ""));
            }


        } catch (Exception e) {
            difference = "";
            e.printStackTrace();
        }
        return difference;
    }

    public static void log(String chars) {
        System.out.println("###>>>>    " + chars + "    <<<<###");
    }

    public static Date convertJBODateToJavaDate(oracle.jbo.domain.Date jboDate) {
        Calendar c = Calendar.getInstance();
        java.sql.Date sd = jboDate.dateValue();
        Date jd = new Date(sd.getTime());
        return jd;
    }

    public static String getMonthNumber(String month) {
        if (month.toUpperCase().trim().equals("JAN")) {
            return "01";
        } else if (month.toUpperCase().trim().equals("FEB")) {
            return "02";
        } else if (month.toUpperCase().trim().equals("MAR")) {
            return "03";
        } else if (month.toUpperCase().trim().equals("APR")) {
            return "04";
        } else if (month.toUpperCase().trim().equals("MAY")) {
            return "05";
        } else if (month.toUpperCase().trim().equals("JUN")) {
            return "06";
        } else if (month.toUpperCase().trim().equals("JUL")) {
            return "07";
        } else if (month.toUpperCase().trim().equals("AUG")) {
            return "08";
        } else if (month.toUpperCase().trim().equals("SEP")) {
            return "09";
        } else if (month.toUpperCase().trim().equals("OCT")) {
            return "10";
        } else if (month.toUpperCase().trim().equals("NOV")) {
            return "11";
        } else if (month.toUpperCase().trim().equals("DEC")) {
            return "12";
        }else{
            return "f";
        }
    }

    public static String addHourToAmPMTime(String time, String hours) {
        Date date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm");
        try {

            date = formatter.parse(time);

        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR, Integer.parseInt(hours.split(":")[0]));
        c.add(Calendar.MINUTE, Integer.parseInt(hours.split(":")[1]));
        return c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE);

    }

    public static String getMonDD(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.get(Calendar.DAY_OF_MONTH);
        String d =
            DateFormatSymbols.getInstance().getShortMonths()[c.get(Calendar.MONTH)].toUpperCase() +
            (c.get(Calendar.DAY_OF_MONTH) <= 9 ? "0" : "") +
            c.get(Calendar.DAY_OF_MONTH);
        return d;
    }

    public static String ddmonyyyy(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH) + "-" +
            new DateFormatSymbols().getShortMonths()[c.get(Calendar.MONTH)] +
            "-" + c.get(Calendar.YEAR);
    }

    public static void doNothing() {
    }

    public static void doSomething(String whatToDo) {
        log(whatToDo);
    }

    public static String getValueFrmExpression(String data) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Application app = fc.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = fc.getELContext();
        ValueExpression valueExp =
            elFactory.createValueExpression(elContext, data, Object.class);
        String Message = null;
        Object obj = valueExp.getValue(elContext);
        if (obj != null) {
            Message = obj.toString();
        }
        return Message;
    }

    public static void resetWhereClause(ViewObjectImpl vo) {
        vo.setWhereClause(null);
        vo.executeQuery();
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

    public static void setvalueToExpression(String el, Object val) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory expressionFactory =
            facesContext.getApplication().getExpressionFactory();
        ValueExpression exp =
            expressionFactory.createValueExpression(elContext, el,
                                                    Object.class);
        exp.setValue(elContext, val);
    }

    public static java.sql.Date convertFromJAVADateToSQLDate(java.util.Date javaDate) {
        java.sql.Date sqlDate = null;
        if (javaDate != null) {
            sqlDate = new java.sql.Date(javaDate.getTime());
        }
        return sqlDate;
    }

    public static String getYYYYMON(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR) +
            new DateFormatSymbols().getShortMonths()[c.get(Calendar.MONTH)].toUpperCase();
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

    public static void redirect(String pageName) {
        FacesContext fctx = FacesContext.getCurrentInstance();
        ExternalContext ec = fctx.getExternalContext();
        HttpServletRequest request = (HttpServletRequest)ec.getRequest();
        HttpServletResponse response = (HttpServletResponse)ec.getResponse();

        try {
            response.sendRedirect(pageName);
        } catch (IOException e) {
            showMessage("Send the screen shot to I.C.T - " + e.getMessage(),
                        112);
        } finally {
            fctx.responseComplete();
        }
    }

    public static void showMessage(String message, int code) {

        FacesMessage.Severity s = null;
        if (code == 112) {
            s = FacesMessage.SEVERITY_ERROR;
        } else if (code == 111) {
            s = FacesMessage.SEVERITY_INFO;
        }

        FacesMessage msg = new FacesMessage(s, message, "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public static String randomString() {
        String random = new BigInteger(130, new SecureRandom()).toString(32);
        return (random.length() > 6 ? random.substring(0, 6) : random);
    }

    public static void createUserSession(String key, String value) {
        FacesContext fctx = FacesContext.getCurrentInstance();
        ExternalContext ec = fctx.getExternalContext();
        HttpSession userSession = (HttpSession)ec.getSession(true);
        userSession.setAttribute(key, value);
    }

    public static Object getSessionValue(String key) {
        FacesContext fctx = FacesContext.getCurrentInstance();
        ExternalContext ec = fctx.getExternalContext();
        HttpSession userSession = (HttpSession)ec.getSession(true);
        return userSession.getAttribute(key);
    }

    public static void destroySession() {
        FacesContext fctx = FacesContext.getCurrentInstance();
        ExternalContext ec = fctx.getExternalContext();
        HttpSession userSession = (HttpSession)ec.getSession(true);
        userSession.invalidate();
    }

    public static FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    private static BindingContext getBindingContext() {
        return BindingContext.getCurrent();
    }

    private static DCBindingContainer getBindingContainer() {
        return (DCBindingContainer)getBindingContext().getCurrent().getCurrentBindingsEntry();
    }

    private static DCDataControl getDataControl(String dataControl) {
        return getBindingContext().findDataControl(dataControl);
    }

    public static Object getAppModule() {
        return getDataControl("HubModuleDataControl").getDataProvider();
    }
    
    public static Object getCustomDataControl(String dc){
        return getDataControl(dc).getDataProvider();
    }

    public static Object getCEOModule() {
        return getDataControl("CEOAppModuleDataControl").getDataProvider();
    }

    public static void refreshVO(String voName) {
        DCBindingContainer dcBindings =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding iterBind = (DCIteratorBinding)dcBindings.get(voName);
        iterBind.executeQuery();
        iterBind.refresh(DCIteratorBinding.RANGESIZE_UNLIMITED);
//        iterBind.refresh(31);
    }

    public static DCIteratorBinding getIterator(String iteratorName) {
        return (DCIteratorBinding)getBindingContainer().get(iteratorName);
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
        } else if (key.equals(PREVIOUS_MONTH_NAME)) {
            DateFormatSymbols dfs = new DateFormatSymbols();
            String[] months = dfs.getShortMonths();
            result = months[Calendar.getInstance().get(Calendar.MONTH) - 1];
        }
        return result;
    }

    private static void loadOracleDrivers() throws ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
    }

    public static Connection connectHRDB() throws Exception {
        loadOracleDrivers();
        return DriverManager.getConnection(connectionString, user, password);
    }

    public static Connection connectEBSDB() throws Exception {
        loadOracleDrivers();
        return DriverManager.getConnection(connectionString_EBS, user_EBS, password_EBS);
    }
    public static String getMONYYYY(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return new DateFormatSymbols().getShortMonths()[c.get(Calendar.MONTH)].toUpperCase() +
            c.get(Calendar.YEAR);
    }

    public static String getMM_YYYY(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return ((c.get(Calendar.MONTH) + 1) <= 9 ?
                "0" + (c.get(Calendar.MONTH) + 1) :
                (c.get(Calendar.MONTH) + 1)) + "-" + c.get(Calendar.YEAR);
    }

    public static int getTotalDaysOfPreviousMonth() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        int days = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        return days;
    }

    public static int getTotalDaysOfCurrentMonth() {
        Calendar c = Calendar.getInstance();
        int days = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        return days;
    }

    /**
     * @param value
     * @param places
     * @return
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

}
