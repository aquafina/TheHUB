package ict.nishat.net.attendanceeventlogger;

import java.sql.Connection;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.SQLException;
import java.sql.Timestamp;

import java.text.DateFormatSymbols;

import java.util.Calendar;
import java.util.Date;

import weblogic.entitlement.expression.UserIdentifier;

public class EventLogger {
    private DatabaseHandler dbH;
    private Connection ebsConn;
    private PreparedStatement pst;
    private PreparedStatement pstActualWorkedHours;
    private String selectUsersSql =
        "select * from xx_e_portal_users where email_address is not null and emp_type is not null and person_id = '2331'";

    public EventLogger() {
        dbH = new DatabaseHandler();
    }

    public void logEvents(Calendar date) {
        Logger l = new Logger();
        try {
            /**LOAD ORACLE DATABASE DRIVERS AND CONNECT TO EBS DATABASE*/
            dbH.loadDrivers();
            ebsConn = dbH.connectDB();
            /**LOAD ALL EMPOYEES FROM EBS AND LOG EVENTS FOR EACH OF THEM,ONE BY ONE*/
            ResultSet rsUsers = getAllUsers();
            rsUsers.last();
            int total = rsUsers.getRow();
            rsUsers.beforeFirst();
            l.logInfo("Total rows: "+total);
            while (rsUsers.next()) {
                String userID = rsUsers.getString("PERSON_ID");
                int empType = rsUsers.getInt("EMP_TYPE");
                if (empType != 5) {       
                    System.out.println("Processing Irregularities of user: "+userID+" of date: "+date.getTime().toString());
                    l.logInfo("Processing Irregularities of user:"+userID+" of date: "+date.getTime().toString()+"\n");
                    
                    String query= "select v.*,LAG (Effective_WORKed_HOURS, 1, NULL)OVER (ORDER BY attendance_Date) previous_day_effective_hours,LAG (TO_CHAR (max_out_time, 'hh12:MI AM'), 1, NULL) OVER (ORDER BY attendance_date)"
                       +"previous_day_out_time from XX_E_PORTAL_EMP_ATD_V_ALL v where emp_id = ? AND TO_CHAR(ATTENDANCE_DATE,'MON-DD-YYYY') = ? " +"order by v.attendance_date asc";
                    
                    String actualWorkedHoursQuery = "select total from xx_e_portal_emp_atd_v atd_v where emp_id = ?  and TO_CHAR(atd_v.in_time,'MON-DD-YYYY') =?";
                    ResultSet rsAttendance = null;
                    //for (int i=1; i<= 31; i++)
//                    {
                    String getPreviousDayRec = "select (TO_CHAR(max_out_time, 'hh12:MI AM')) outtime from xx_e_portal_emp_atd where TO_CHAR (ATTENDANCE_DATE, 'MON-DD-YYYY') = ? and emp_id = ?";
                    
//                    String formattedDateParam =
//                        new DateFormatSymbols().getShortMonths()[date.get(Calendar.MONTH)].toUpperCase() +
//                        "-" +
//                        (date.get(Calendar.DAY_OF_MONTH) <= 9 ? "0" + date.get(Calendar.DAY_OF_MONTH) :
//                         date.get(Calendar.DAY_OF_MONTH)) + "-" +
//                        Calendar.getInstance().get(Calendar.YEAR);
//                    
//                    Calendar pc = Calendar.getInstance();
//                    pc.setTime(date.getTime());
//                    pc.add(Calendar.DAY_OF_MONTH, -1);
//                    String formattedPreviousDateParam =
//                        new DateFormatSymbols().getShortMonths()[pc.get(Calendar.MONTH)].toUpperCase() +
//                        "-" +
//                        ((pc.get(Calendar.DAY_OF_MONTH)) <= 9 ? "0" + (pc.get(Calendar.DAY_OF_MONTH)) :
//                         (pc.get(Calendar.DAY_OF_MONTH))) + "-" +
//                        pc.get(Calendar.YEAR);
                      int i = 17;
                    
                        String formattedDateParam =
                        "JAN" +
                        "-" +
                        (i <= 9 ? "0" + i :
                         i) + "-" +
                        "2016";
                    
                    Calendar pc = Calendar.getInstance();
                    pc.setTime(date.getTime());
                    //pc.add(Calendar.DAY_OF_MONTH, -1);
                    String formattedPreviousDateParam =
                        "JAN" +
                        "-" +
                        ((i-1) <= 9 ? "0" + (i-1) :
                         (i-1)) + "-" +
                        "2016";
                    
                    //GETTING PREVIOUS DAY OUT TIME
                    pst = ebsConn.prepareStatement(getPreviousDayRec);
                    pst.setString(1, formattedPreviousDateParam);
                    pst.setString(2, userID);
                    ResultSet rsPrevious = pst.executeQuery();
                    String previousDayWorking = null;
                    if (rsPrevious.next()) {
                        previousDayWorking = rsPrevious.getString("outtime");
                    }
                    
                    System.out.println("Previous day out time: "+previousDayWorking);
                    l.logInfo("Previous day out time: "+previousDayWorking);
                    
                    //GETTING CURRENT DAY DATA
                    pst = null;
                    pst = ebsConn.prepareStatement(query);
                    pst.setString(1, userID);
                    pst.setString(2, formattedDateParam);
                    rsAttendance = pst.executeQuery();
                    while (rsAttendance.next()) {
                        String expectedHours = rsAttendance.getString("EXPECTED_WORK_HOURS");
                        int expectedHoursMins = (Integer.parseInt(expectedHours.split(":")[0])*60)+Integer.parseInt(expectedHours.split(":")[1]);
                        String lateIn = null;
                        String earlyOut = null;
                        String missedMinutes7 = null;
                        String expectedWorkHours = rsAttendance.getString("EXPECTED_WORK_HOURS");
                        String effectiveWorkHours = rsAttendance.getString("EFFECTIVE_WORKED_HOURS") ==
                                          null ? "0:00" :
                                          rsAttendance.getString("EFFECTIVE_WORKED_HOURS");
                        Timestamp d =
                            rsAttendance.getTimestamp("Attendance_date");
                        String description =
                            rsAttendance.getString("DESCRIPTION");
                        String dayDescription = rsAttendance.getString("DAY");
                        System.out.println("Day description = "+dayDescription);
                        System.out.println("Description: "+description);
                        l.logInfo("Description: "+description);
                        
                        int empID = rsAttendance.getInt("emp_id");
                        String atd_id =
                            rsAttendance.getString("EMP_ATD_ID").toString();
                        String effectiveWorkedHours =
                            rsAttendance.getString("EFFECTIVE_WORKED_HOURS");
                        System.out.println("effective="+effectiveWorkHours);
                        System.out.println("expected="+expectedWorkHours);
                        if (empType==3)
                        {
                                missedMinutes7 = getMissedMinutes7(empType,expectedWorkHours,effectiveWorkHours);
                        }
                        else
                        {
                            lateIn = 
                                getLateIn(rsAttendance.getString("MAX_START_TIME"),
                                          rsAttendance.getTimestamp("MIN_IN_TIME"),
                                          previousDayWorking,
                                          String.valueOf(empType),
                                          rsAttendance.getString("EXPECTED_WORK_HOURS"),
                                          rsAttendance.getString("EFFECTIVE_WORKED_HOURS") ==
                                          null ? "0:00" :
                                          rsAttendance.getString("EFFECTIVE_WORKED_HOURS"));
                            earlyOut =
                                getEarlyOut(rsAttendance.getString("INTIME"),
                                            rsAttendance.getString("OUTTIME"),
                                            rsAttendance.getString("START_TIME"),
                                            rsAttendance.getString("END_TIME"),
                                            rsAttendance.getString("MAX_END_TIME"),
                                            rsAttendance.getString("MAX_START_TIME"),
                                            rsAttendance.getDate("MAX_OUT_TIME"),
                                            String.valueOf(empType),
                                            rsAttendance.getString("EXPECTED_WORK_HOURS"),
                                            rsAttendance.getString("EFFECTIVE_WORKED_HOURS") ==
                                            null ? "0:00" :
                                            rsAttendance.getString("EFFECTIVE_WORKED_HOURS"));
                        }
                        System.out.println("Late in: "+lateIn+" and Early out: "+earlyOut);
                        l.logInfo("Late in:"+lateIn+" and Early out: "+earlyOut);
                        
                        int missingMinLateIn = 0;
                        int missingMinEarlyOut = 0;
                        int missingMinMissedMinutes7 = 0;
                        Calendar c = Calendar.getInstance();
                        c.setTime(d);
                        String day =
                            new DateFormatSymbols().getShortWeekdays()[c.get(Calendar.DAY_OF_WEEK)];

                        /** LOG ABSENT IF EMPLOYEE HASN'T SWIPED HIS CARD */
                        if (description.equals("Independence Day") || description.equals("Eid Holiday") || description.equals("Public Holiday") || description.equals("SATURDAY_OFF") || description.equals("Ashura") || description.trim().equals("SUNDAY")) {
                            System.out.println("It's not a regular day so not logging anything.");
                            l.logInfo("It's not a regular day so not logging anything.");
                        }
                        else
                        {
                        if (effectiveWorkedHours == null) 
                        {
                                int id = insertIntoIrregularities(ebsConn,atd_id,d, expectedHoursMins, empID,"ABSENT");
                                //int id1 =  insertIntoMissingMinutes(ebsConn, atd_id, d, 60, empID, "ABSENT");
                                    l.logInfo("Absent logged");
                            } 
                        else 
                        {
                            if (lateIn != null) {
                                missingMinLateIn =
                                        (Integer.parseInt(lateIn.split(":")[0]) *
                                         60) +
                                        Integer.parseInt(lateIn.split(":")[1]);
                                if (missingMinLateIn > 5) {
                                    int id = insertIntoIrregularities(ebsConn,atd_id,d, missingMinLateIn, empID,"LATE_IN");
                                    System.out.println("Late in logged: "+missingMinLateIn);
                                    l.logInfo("Late in logged: "+missingMinLateIn);
                                }
                            }
                            if (earlyOut != null) {
                                missingMinEarlyOut =
                                        (Integer.parseInt(earlyOut.split(":")[0]) *
                                         60) +
                                        Integer.parseInt(earlyOut.split(":")[1]);
                                if (missingMinEarlyOut != 0) {
                                    int id = insertIntoIrregularities(ebsConn,atd_id,d, missingMinEarlyOut, empID,"EARLY_OUT");
                                }
                                System.out.println("Early out logged: "+missingMinEarlyOut);
                                l.logInfo("Early out logged: "+missingMinEarlyOut);
                            }
                            if (missedMinutes7 != null) {
                                //System.out.println("missedMinutesCheck");
                                missingMinMissedMinutes7 =
                                        (Integer.parseInt(missedMinutes7.split(":")[0]) *
                                         60) +
                                        Integer.parseInt(missedMinutes7.split(":")[1]);
                                //System.out.println("missingMinMissedMinutes7 = "+missingMinMissedMinutes7);
                                if (missingMinMissedMinutes7 != 0)
                                {
                                    int id = insertIntoIrregularities(ebsConn,atd_id,d, missingMinMissedMinutes7, empID,"MISSED_MINUTES_7");
                                }
                            }
                            if (empType == 3)
                            {
                                pstActualWorkedHours = ebsConn.prepareStatement(actualWorkedHoursQuery);
                                pstActualWorkedHours.setString(1, userID);
                                pstActualWorkedHours.setString(2, formattedDateParam);
                                ResultSet rsActualWorkedHours = pstActualWorkedHours.executeQuery();
                                rsActualWorkedHours.next();
                                int actualWorkedMinutes = CommonUtil.convertTimeToMinutes(rsActualWorkedHours.getString(1));
                                int missingMinutesOutOf8 = getMissingMinutesOutOf8(actualWorkedMinutes);
                                System.out.println("actualWorkedMinutes= "+actualWorkedMinutes);
                                if (missingMinutesOutOf8!=0)
                                {
                                    int id1 =  insertIntoMissingMinutes(ebsConn, atd_id, d, missingMinutesOutOf8, empID, "MISSED_MINUTES_7");
                                    System.out.println("MissedMinutesOutOf8 logged: " + missingMinutesOutOf8);
                                }
                            }
                        }
                        System.out.println("#####################################################################################################");
                        l.logInfo("####################################################################");
                    rsAttendance.close();
                    pst.close();
                    System.out.println("#####################################################################################################");
                    l.logInfo("####################################################################");
                }

            }
            rsUsers.close();
        }
    }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ResultSet getAllUsers() throws Exception {
        return ebsConn.prepareStatement(selectUsersSql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE,
         ResultSet.HOLD_CURSORS_OVER_COMMIT).executeQuery();
    }

    private String getEarlyOut(String intime, String outtime, String startTime,
                               String endTime, String maxEndtime,
                               String maxStartTime, Date maxOutTime,
                               String empType, String expectedWorkHours,
                               String effectiveWorkHours) {
        boolean fullyWorked =
            (Integer.parseInt(expectedWorkHours.split(":")[0]) * 60 +
             Integer.parseInt(expectedWorkHours.split(":")[1])) ==
            (Integer.parseInt(effectiveWorkHours.split(":")[0]) * 60 +
             Integer.parseInt(effectiveWorkHours.split(":")[1]));
//        CommonUtil.log("fully Worked: " + fullyWorked + " emptype: " +
//                       empType);
        if (Integer.parseInt(empType) != 1 && fullyWorked) {
            //CommonUtil.log("Person is a part timer");
            return null;
        } else {
            if (maxOutTime != null) {
                //                    String intime = "10:32 AM";
                //                    String startTime = "8:30 AM";
                //                    String maxStartTime = "10:00 AM";
                //                    String outtime = "10:32 AM";
                //                    String maxEndtime = "6:00 PM";
                //                    String endTime = "4:30 PM";

                //            String intime = getIntime();
                //            String startTime = getStartTime();
                //            String outtime = getOuttime();
                //            String maxEndtime = getMaxEndTime();
                //            String maxStartTime = getMaxStartTime();
                //            String endTime = getEndTime();

                String diff_intime_and_startTime =
                    CommonUtil.subtractTime(intime, startTime);
                String diff_intime_and_maxStartTime =
                    CommonUtil.subtractTime(intime, maxStartTime);
                String diff_intime_and_maxEndTime =
                    CommonUtil.subtractTime(intime, maxEndtime);

                int min_diff_intime_and_startTime =
                    Integer.parseInt(diff_intime_and_startTime.replace("#",
                                                                       ":").split(":")[0]) +
                    Integer.parseInt(diff_intime_and_startTime.replace("#",
                                                                       ":").split(":")[1]);
                int min_diff_intime_and_maxStartTime =
                    Integer.parseInt(diff_intime_and_maxStartTime.replace("#",
                                                                          ":").split(":")[0]) +
                    Integer.parseInt(diff_intime_and_maxStartTime.replace("#",
                                                                          ":").split(":")[1]);
                int min_diff_intime_and_maxEndTime =
                    Integer.parseInt(diff_intime_and_maxEndTime.replace("#",
                                                                        ":").split(":")[0]) +
                    Integer.parseInt(diff_intime_and_maxEndTime.replace("#",
                                                                        ":").split(":")[1]);


                String earlyOut = null;
                if (min_diff_intime_and_maxEndTime < 0) {
                    earlyOut = "0:00";
                } else {
                    if (min_diff_intime_and_startTime <= 0 &&
                        min_diff_intime_and_maxStartTime >= 0) {
//                        System.out.println("Case#1 " +
//                                           CommonUtil.addHourToAmPMTime(intime.split(" ")[0],
//                                                                        expectedWorkHours) +
//                                           " - " + outtime);
                        earlyOut =
                                CommonUtil.limitSubtractTime(outtime, CommonUtil.addHourToAmPMTime(intime.split(" ")[0],
                                                                                                   "8:00") +
                                                             " PM");
                    } else if (min_diff_intime_and_startTime > 0) {
                //        System.out.println("Case#2 "+endTime + " - " + outtime);
                        earlyOut =
                                CommonUtil.limitSubtractTime(outtime, endTime);
                    } else if (min_diff_intime_and_maxStartTime < 0) {
                  //      System.out.println("Case#3 "+maxEndtime + " - " + outtime);
                        earlyOut =
                                CommonUtil.limitSubtractTime(outtime, maxEndtime);
                    }
                }
                return earlyOut.replace("#", ":");
            } else {
                return null;
            }
        }


    }

    private String getLateIn(String pMaxStartTime, Date pMinInTime,
                             String previousDayOutTime, String empType,
                             String expectedWorkHours,
                             String effectiveWorkHours) {
        //System.out.println("Hhhhhhhhhhhhh: "+previousDayOutTime);
        String workAfterOfficeTimings = "";
        String previousDayTime =
            previousDayOutTime != null ? previousDayOutTime.toString() : null;
        boolean fullyWorked =
            (Integer.parseInt(expectedWorkHours.split(":")[0]) * 60 +
             Integer.parseInt(expectedWorkHours.split(":")[1])) ==
            (Integer.parseInt(effectiveWorkHours.split(":")[0]) * 60 +
             Integer.parseInt(effectiveWorkHours.split(":")[1]));
//        CommonUtil.log("fully Worked: " + fullyWorked + " emptype: " +
//                       empType);
        if (Integer.parseInt(empType) != 1 && fullyWorked) {
            //CommonUtil.log("Person is a part timer");
            return null;
        } else {
            if (pMinInTime != null) {
                //CommonUtil.log("Nadia: in block");
                String amPmString = pMinInTime.toLocaleString().split(" ")[4];
                String timeString = pMinInTime.toLocaleString().split(",")[1];
                //System.out.println("timeString: " + timeString);
                String timeStringWithAmPm =
                    (timeString.split(":")[0] + ":" + timeString.split(":")[1] +
                     " " + amPmString).trim();
                String minInTime =
                    timeStringWithAmPm.split(" ")[1] + " " + timeStringWithAmPm.split(" ")[2];
               // System.out.println("min in time: " + minInTime);
                String maxInFlexiC = "";
                if (previousDayOutTime != null) {
                    workAfterOfficeTimings =
                            CommonUtil.subtractTime("6:30 PM", previousDayTime).replace("#",
                                                                                        ":");
                    int lateSittingMins =
                        Integer.parseInt(workAfterOfficeTimings.split(":")[0]) *
                        60 +
                        Integer.parseInt(workAfterOfficeTimings.split(":")[1]);
                 //   CommonUtil.log("lateSittings: nadia " + lateSittingMins);
                    if (lateSittingMins >= 60 && lateSittingMins <= 149) {
                        maxInFlexiC = "10:30 AM";
                    } else if (lateSittingMins > 149 &&
                               lateSittingMins < 209) {
                        maxInFlexiC = "11:00 AM";
                    } else if (lateSittingMins > 209) {
                        maxInFlexiC = "11:30 AM";
                    } else 
                    {
                        maxInFlexiC = pMaxStartTime;
                    }
                } else {
                    maxInFlexiC = pMaxStartTime;
                }

                //CommonUtil.log("Nadia: " + maxInFlexiC + " and " + minInTime);
                String lateIn =
                    CommonUtil.limitSubtractTime(maxInFlexiC, minInTime); //minInTime - maxInFlexi+someFigure
                //CommonUtil.log("naida: " + lateIn);
                return lateIn.replace("#", ":");
            } else {
                return null;
            }
        }


    }
    
    public String getMissedMinutes7(int empType,
                             String expectedWorkHours,
                             String effectiveWorkHours) {
        int expectedEffectiveDiff =
            (Integer.parseInt(expectedWorkHours.split(":")[0]) * 60 +
             Integer.parseInt(expectedWorkHours.split(":")[1])) -
            (Integer.parseInt(effectiveWorkHours.split(":")[0]) * 60 +
             Integer.parseInt(effectiveWorkHours.split(":")[1]));
        //        CommonUtil.log("fully Worked: " + fullyWorked + " emptype: " +
        //                       empType);
        System.out.println("expectedEffectiveDiff = "+expectedEffectiveDiff);
        if (empType != 1 && expectedEffectiveDiff<=0) {
            //CommonUtil.log("Person is a part timer");
            return null;
        }
        else {
//           String expectedEffectiveDiffStr = CommonUtil.limitSubtractTime(String.valueOf(Integer.parseInt(expectedWorkHours.split(":")[0]) * 60 +
//             Integer.parseInt(expectedWorkHours.split(":")[1])),String.valueOf(Integer.parseInt(effectiveWorkHours.split(":")[0]) * 60 +
//             Integer.parseInt(effectiveWorkHours.split(":")[1])));
            System.out.println(expectedEffectiveDiff/60+":"+ expectedEffectiveDiff%60);
            return expectedEffectiveDiff/60+":"+ expectedEffectiveDiff%60;
        }
    }
    
    //HR leave calculation for 7 hour part time employees
    //It returns 60 if the actual worked hours are less than or equal to 7 and the amount of minutes less than 8 if actual worked hours greater than 7
    public int getMissingMinutesOutOf8(int actualWorkedMinutes) 
    {
        if (actualWorkedMinutes<330) 
        {
            return 0;    
        }
        else if (actualWorkedMinutes >=330 && actualWorkedMinutes < 420)
        {
            return 60;
        }
        else if (actualWorkedMinutes>=420 && actualWorkedMinutes<475)
        {
//            System.out.println("Actual Worked Minutes bw 420 and 480");
            return (480-actualWorkedMinutes);
        }
        else  
        {
            return 0;    
        }
    }
    
    public int insertIntoIrregularities(Connection ebsConn, String atd_id, Timestamp d, int missingMins, int empID, String irrType) 
    {
        PreparedStatement pstLogIrr1;
        int id = 0;
        try {
            pstLogIrr1 = ebsConn.prepareStatement("INSERT INTO xx_e_portal_emp_irregularities (IRREGULARITY_ID,EMP_ATD_ID,ATTENDANCE_DATE,TYPE,EXCEPTION_REMARKS,minutes_missing,emp_id) SELECT xx_e_portal_irr_seq.nextval,?,?,?,?,?,? FROM DUAL");
        pstLogIrr1.setString(1, atd_id);
        pstLogIrr1.setTimestamp(2, d);
        pstLogIrr1.setString(3, irrType);
        pstLogIrr1.setString(4, null);
        //pstLogIrr1.setInt(5, 8);
        pstLogIrr1.setInt(5, missingMins);
        pstLogIrr1.setInt(6, empID);
        id = pstLogIrr1.executeUpdate();
        } catch (SQLException e) {
        }
        return id;
    }
    public int insertIntoMissingMinutes(Connection ebsConn, String atd_id, Timestamp d, int missingMins, int empID, String irrType) 
    {
        int id = 0;
        try
        {
            PreparedStatement pstLogMissingMins=
                ebsConn.prepareStatement("INSERT INTO xx_e_portal_emp_missing_mins (MISSING_MINUTES_ID,EMP_ATD_ID,ATTENDANCE_DATE,TYPE,EXCEPTION_REMARKS,missing_minutes,emp_id,exception_approved) SELECT xx_e_portal_missing_mins_seq.nextval,?,?,?,?,?,?,? FROM DUAL");
                pstLogMissingMins.setString(1, atd_id);
                pstLogMissingMins.setTimestamp(2, d);
                pstLogMissingMins.setString(3, irrType);
                pstLogMissingMins.setString(4, null);
                pstLogMissingMins.setInt(5, missingMins);
                pstLogMissingMins.setInt(6, empID);
                pstLogMissingMins.setString(7, null);
                id = pstLogMissingMins.executeUpdate();
        }
        catch(SQLException e) 
        {
            
        }
        return id;
    }
}
