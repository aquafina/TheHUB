package view.nishat.net;

import java.util.ArrayList;

public class LeaveDeductionRulesEngine {

    private int relaxationTime = 5; //In Mins
    private int fourHourClockedInMins = 4 * 60;
    private ArrayList<String> deduction;

    public LeaveDeductionRulesEngine() {
        deduction = new ArrayList<String>();
    }

    public ArrayList getLeaveDeducted7(int missingMinutes7,
                                      String leaveType,
                                      int expectedWorkHours) 
    {
        String leaveTypeCA = leaveType==null ? "CASUAL" : leaveType;
            if (missingMinutes7 > 5 && missingMinutes7 <= 30)
            {
                deduction.add("30_MINS_LATE");
            }
            else if (missingMinutes7 > 30 && missingMinutes7 <= 90) 
            {
                deduction.add("SHORT");   
            }
            else if (missingMinutes7 > 90 && missingMinutes7 <= 180) 
            {
                if (leaveTypeCA.equals("CASUAL"))
                {
                    deduction.add("HALF_CASUAL");
                }
                else if (leaveTypeCA.equals("ANNUAL"))
                {
                    deduction.add("HALF_ANNUAL");
                }
            }
            else if (missingMinutes7 > 180)
            {
                if (leaveTypeCA.equals("CASUAL"))
                {
                    deduction.add("CASUAL");
                }
                else if (leaveTypeCA.equals("ANNUAL"))
                {
                    deduction.add("ANNUAL");
                }
            }
        return deduction;
    }
    
    public ArrayList getLeaveDeducted(int lateInMins, int earlyOutmin,
                                      String leaveType,
                                      int expectedWorkedHours) {
        /*
         *
         * It's late in
         *
         */
        String lt = leaveType==null ? "CASUAL" : leaveType;
        
        if (isLateIn(lateInMins)) {
            //System.out.println("Employee is late in the morning.");
            if (lateInMins > 5 && lateInMins <= 30) {
                //System.out.println("Employee is " + lateInMins + " minutes late in the morning.");
                if (isEarlyOut(earlyOutmin)) {
                    //Now Determine how much early??
                    if (earlyOutmin > 0 && earlyOutmin <= 30) {
                        //      System.out.println("Employee left between 0-30 minutes range");
                        //    System.out.println("Deduct 30 Min late in | Deduct Short leave.");
                        deduction.add("30_MINS_LATE");
                        deduction.add("SHORT");
                    } else if (earlyOutmin > 30 && earlyOutmin <= 90) {
                        //  System.out.println("Employee left between 30-90 minutes range");
                        // System.out.println("Deduct 30 Min late in | Deduct Short leave.");
                        deduction.add("30_MINS_LATE");
                        deduction.add("SHORT");
                    } else if (earlyOutmin > 90) {
                        int getTotalMins =
                            expectedWorkedHours * 60 - getTotalMins(lateInMins,
                                                                    earlyOutmin);
                        //System.out.println(getTotalMins + " ");
                        if (getTotalMins >= fourHourClockedInMins) {
                            //  System.out.println("Clocked in hours are less then 4, Deduct Half casual leave");
                            if (lt.equals("CASUAL")) {
                                deduction.add("HALF_CASUAL");
                            } else if (lt.equals("ANNUAL")) {
                                deduction.add("HALF_ANNUAL");
                            }

                        } else if (getTotalMins < fourHourClockedInMins) {
                            //System.out.println("Clocked in hours are greater then 4, Deduct Full casual leave");
                            if (lt.equals("CASUAL")) {
                                deduction.add("CASUAL");
                            } else if (lt.equals("ANNUAL")) {
                                deduction.add("ANNUAL");
                            }
                            
                            
                        }
                    }
                } else if (!isEarlyOut(earlyOutmin)) {
                    //System.out.println("Deduct 30 Mins late in.");
                    deduction.add("30_MINS_LATE");
                }
            } else if (lateInMins > 30 && lateInMins <= 90) {
                //System.out.println("Employee is late between 30-90 minutes range");
                if (isEarlyOut(earlyOutmin)) {
                    if (earlyOutmin > 0 && earlyOutmin <= 30) {
                        //      System.out.println("Employee left between 0-30 minutes range");
                        //    System.out.println("Deduct 30 Min Early Out | Deduct Short leave.");
                        deduction.add("30_MINS_LATE");
                        deduction.add("SHORT");
                    } else if (earlyOutmin > 30) {
                        int getTotalMins =
                            expectedWorkedHours * 60 - getTotalMins(lateInMins,
                                                                    earlyOutmin);
                        //  System.out.println(getTotalMins + " ");
                        if (getTotalMins >= fourHourClockedInMins) {
                            //    System.out.println("Clocked in hours are less then 4, Deduct Half casual leave");
                            if (lt.equals("CASUAL")) {
                                deduction.add("HALF_CASUAL");
                            } else if (lt.equals("ANNUAL")) {
                                deduction.add("HALF_ANNUAL");
                            }
                            
                            
                        } else if (getTotalMins < fourHourClockedInMins) {
                            //  System.out.println("Clocked in hours are greater then 4, Deduct Full casual leave");
                            if (lt.equals("CASUAL")) {
                                deduction.add("CASUAL");
                            } else if (lt.equals("ANNUAL")) {
                                deduction.add("ANNUAL");
                            }
                            
                            
                        }
                    }
                } else if (!isEarlyOut(earlyOutmin)) {
                    deduction.add("SHORT");
                }
            } else if (lateInMins > 90) {
                //System.out.println("Employee is late in more then 90 minutes");
                int getTotalMins =
                    expectedWorkedHours * 60 - getTotalMins(lateInMins,
                                                            earlyOutmin);
                //System.out.println(getTotalMins + " ");
                if (getTotalMins >= fourHourClockedInMins) {
                    //  System.out.println("Clocked in hours are less then 4, Deduct Half casual leave");
                    if (lt.equals("CASUAL")) {
                        deduction.add("HALF_CASUAL");
                    } else if (lt.equals("ANNUAL")) {
                        deduction.add("HALF_ANNUAL");
                    }
                    
                    
                } else if (getTotalMins < fourHourClockedInMins) {
                    //  System.out.println("Clocked in hours are greater then 4, Deduct Full casual leave");
                    if (lt.equals("CASUAL")) {
                        deduction.add("CASUAL");
                    } else if (lt.equals("ANNUAL")) {
                        deduction.add("ANNUAL");
                    }
                    
                    
                }
            }
            /**
             *
             *
             * It's not late in
             *
             */
        } else if (!isLateIn(lateInMins)) {
            //System.out.println("Employee is not late in the morning.");
            if (isEarlyOut(earlyOutmin)) {
                //System.out.println("Employee Left Early.");
                //Now Determine how much early??
                if (earlyOutmin > 0 && earlyOutmin <= 30) {
                    //System.out.println("30 Min Early Out");
                    deduction.add("30_MINS_LATE");
                } else if (earlyOutmin > 30 && earlyOutmin <= 90) {
                    //System.out.println("Short Leave");
                    deduction.add("SHORT");
                } else if (earlyOutmin > 90) {
                    int getTotalMins =
                        expectedWorkedHours * 60 - getTotalMins(lateInMins,
                                                                earlyOutmin);
                    if (getTotalMins >= fourHourClockedInMins) {
                        //  System.out.println("Clocked in hours are less then 4, Deduct Half casual leave");
                        if (lt.equals("CASUAL")) {
                            deduction.add("HALF_CASUAL");
                        } else if (leaveType.equals("ANNUAL")) {
                            deduction.add("HALF_ANNUAL");
                        }
                        
                        
                    } else if (getTotalMins < fourHourClockedInMins) {
                        //System.out.println("Clocked in hours are greater then 4, Deduct Full casual leave");
                        if (lt.equals("CASUAL")) {
                            deduction.add("CASUAL");
                        } else if (leaveType.equals("ANNUAL")) {
                            deduction.add("ANNUAL");
                        }
                        
                        
                    }
                }
            } else if (!isEarlyOut(earlyOutmin)) {
            }
        }
        return deduction;
    }

    private boolean isLateIn(int mins) {
        boolean isLate = false;
        if (mins > 5) {
            isLate = true;
        } else if (mins < 5) {
            isLate = false;
        }
        return isLate;
    }

    private boolean isEarlyOut(int mins) {
        boolean isEarly = false;
        if (mins > 0) {
            isEarly = true;
        } else {
            isEarly = false;
        }
        return isEarly;
    }

    private int getTotalMins(int lateIn, int earlyOut) {
        int totalMins = 0;
        if (lateIn > 0 && lateIn <= 5) {
            totalMins = 0 + earlyOut;
        } else {
            totalMins = lateIn + earlyOut;
        }
        return totalMins;
    }
}
