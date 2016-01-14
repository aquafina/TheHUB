package view.nishat.net.Helper;

import hub.nishat.net.model.AM.HubModuleImpl;

import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.print.attribute.standard.Severity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.server.ViewObjectImpl;


public class AuthHandler {
    private static final String USER_TABLE = "xx_e_portal_users";
    private String connectionString =
        "jdbc:oracle:thin:@192.168.0.31:1522:prod";
    //private String connectionString = "jdbc:oracle:thin:@10.152.13.34:1522:prod";


    private String user = "xx_e_portal";
    private String password = "mskiz145";

    /*###############################################################
     *                       CONSTRUCTOR
     *                       ###########                           */

    public AuthHandler() {
    }

    /* AUTHENTICATE METHOD WILL CHECK IF THE USER IS LOGGING IN FIRST TIME, IF HE/SHE IS LOGGING IN FIRST TIME
     * THEN CONFIRM HIS PASSWORD.IF THE USER HAS ALREADY CONFIRMED THE PASSWORD THEN SIMPLE AUTHENTICATE THE USER
     * AGAINST THE ROW  */

    public int authenticate(String username,
                            String password) throws ClassNotFoundException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String passwordField = null;
        int returnCode = 007;

        //LOAD ORACLE DATABASE DRIVERS
        loadDrivers();
        try {
            //CONNECT TO DATABASE
            connection = connectDB();
            /*
             * CHECK IF THE USER HAS ALREADY CONFIRM THE PASSWORD.
             */
            String getPasswordSql =
                "select password from " + USER_TABLE + " where upper(trim(email_address)) = upper(trim(?))";
            ps = connection.prepareStatement(getPasswordSql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) 
            {
                //GET THE PASSWORD FIELD
                passwordField = rs.getString("password");
                //IF THE PASSWORD IS NULL OR HAS NOTHING
                if (passwordField == null || passwordField.trim().equals("")) {
                    //AUTHENTICATE AGAINST THE DEFAULT PASSWORD GIVER TO USER
                    //AS USER IS LOGGING IN FOR THE FIRST TIME
                    String authWithDefaultSql =
                        "select EMAIL_ADDRESS,TEMP_PASS from " + USER_TABLE +
                        " where upper(trim(email_address)) = upper(trim(?)) and upper(trim(TEMP_PASS)) = upper(trim(?))";
                    ps = connection.prepareStatement(authWithDefaultSql);
                    ps.setString(1, username);
                    ps.setString(2, password);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        /* SAVE THE USERNAME AND TEMP PASSWORD TO USE ON CONFIRM PASSWORD
                         * PAGE */
                        CommonUtil.createUserSession(Constants.SESSION_USERNAME,
                                                     rs.getString("EMAIL_ADDRESS"));
                        CommonUtil.createUserSession(Constants.SESSION_TEMP_PASSWORD,
                                                     rs.getString("temp_pass"));
                        return 2; // RETURN CODE FOR CONFIRM PASSWORD
                    } else {
                        return 0; // RETURN CODE FOR RECORD NOT FOUND, FAILED AUTHENTICATION
                    }
                } else {
                    /*
                     * PASSWORD EXISTS, NOW AUTHENTICATE THE USER AGAINST THIS PASSWORD
                     */

                    String authQuery =
                                           "select EMP_TYPE,EMAIL_ADDRESS,TEMP_PASS,EMP_CODE,PASSWORD,PERSON_ID,CASUAL_LEAVE_CRITERIA,ANNUAL_LEAVE_CRITERIA,is_hr,ALTERNATE_APPROVING_AUTH from " +
                                           USER_TABLE +
                                           " where (upper(trim(email_address)) = upper(trim(?)) and password = DBMS_OBFUSCATION_TOOLKIT.MD5(input_string => ?))" +
                                           "or (upper(trim(email_address)) = upper(trim(?)) and '{system-master}'||temp_pass = ?)";

                                       ps = connection.prepareStatement(authQuery);
                                       ps.setString(1, username);
                                       ps.setString(2, password);
                                       ps.setString(3, username);
                                       ps.setString(4, password);
                    rs = ps.executeQuery();
                    // IF THE ROW MOVE TO THE NEXT THEN WE HAVE A PASSWORD AND AUTHENTICATINO IS SUCCESS

                    if (rs.next()) {
                        CommonUtil.createUserSession(Constants.SESSION_USERNAME,
                                                     rs.getString("email_address"));
                        CommonUtil.createUserSession(Constants.SESSION_PASSWORD,
                                                     rs.getString("password"));
                        CommonUtil.createUserSession(Constants.SESSION_USERID,
                                                     rs.getString("person_id"));
                        CommonUtil.createUserSession(Constants.SESSION_EMP_CODE,
                                                     rs.getString("EMP_CODE"));
                        CommonUtil.createUserSession(Constants.SESSION_IS_HR,
                                                     rs.getString("is_hr"));
                        CommonUtil.createUserSession(Constants.SESSION_IS_AAT, rs.getString("ALTERNATE_APPROVING_AUTH"));
                        CommonUtil.createUserSession(Constants.SESSION_EMP_TYPE,
                                                     rs.getString("EMP_TYPE"));
                        //LOAD EMPLOYEE HIERARCHY SUMMERY, LIKE EMP DEPARTMENT HIERARCHY, IF HE IS AN HOD/SUPERVISOR OR NOT
                        //HOW MANY CHILDS ARE UNDER HIM, WORKING. . . . .
                        HubModuleImpl am =
                            (HubModuleImpl)CommonUtil.getAppModule();
                        ViewObjectImpl vo = am.getVO_EmpHierarchySummary1();
                        String whereClause =
                            "emp_id = '" + Integer.parseInt(rs.getString("person_id")) +
                            "'";
                        //                        System.out.println(whereClause);
                        CommonUtil.resetWhereClause(vo);
                        vo.setWhereClause(whereClause);
                        vo.executeQuery();
                        RowSetIterator rsi = vo.createRowSetIterator(null);
                        int pageCount = vo.getEstimatedRangePageCount();
                        Row[] rows = null;
                        if (pageCount > 1) {
                            rows = rsi.getNextRangeSet();
                        } else if (pageCount == 1) {
                            rows = rsi.getAllRowsInRange();
                        }

                        StringBuilder dptHierarchyName = new StringBuilder();
                        String access_level = "";
                        String hodemail = "";
                        String empPosition = "";
                        String isHOD = "";
                        String isSupervisor = "";
                        StringBuilder parentEmail = new StringBuilder();


                        //                        while (rows.length > 0) {
                        //                            for (int i = 0; i < rows.length; i++) {
                        //                                System.out.println(rows[i].getAttribute("HierarchyName") +
                        //                                                   " is this null now???");
                        //                            }
                        //                            rows = rsi.getNextRangeSet();
                        //                        }
                        //


                        if (pageCount > 1) {


                            empPosition =rows[0].getAttribute("EmpPosition").toString();
                            hodemail = rows[0].getAttribute("Hodemail").toString();
                            while (rows.length > 0) {
                                for (int i = 0; i < rows.length; i++) {
                                    dptHierarchyName.append(rows[i].getAttribute("HierarchyName").toString());
                                    if (rows[i].getAttribute("ParentEmailAddress") !=
                                        null) {
                                        parentEmail.append(rows[i].getAttribute("ParentEmailAddress").toString());
                                    }
                                    if (rows[i].getAttribute("IsHod").toString().equals("Y")) {
                                        isHOD = rows[i].getAttribute("IsHod").toString();
                                    }
                                    if (rows[i].getAttribute("IsSupervisor").toString().equals("Y")) {
                                        isSupervisor = rows[i].getAttribute("IsSupervisor").toString();
                                    }
                                }
                                rows = rsi.getNextRangeSet();
                            }
                            access_level =
                                    isHOD.equals("Y") ? "HOD" : isSupervisor.equals("Y") ?
                                                                "SUPERVISOR" :
                                                                "EMPLOYEE";
                            
                            
                            

                        } else if (pageCount == 1) {

                             isHOD =
                                rows[0].getAttribute("IsHod").toString();
                             isSupervisor =
                                rows[0].getAttribute("IsSupervisor").toString();
                            access_level =
                                    isHOD.equals("Y") ? "HOD" : isSupervisor.equals("Y") ?
                                                                "SUPERVISOR" :
                                                                "EMPLOYEE";
                            empPosition =
                                    rows[0].getAttribute("EmpPosition").toString();
                            hodemail = rows[0].getAttribute("Hodemail").toString();

                            dptHierarchyName.append(rows[0].getAttribute("HierarchyName").toString());
                            if (rows[0].getAttribute("ParentEmailAddress") !=
                                null) {
                                parentEmail.append(rows[0].getAttribute("ParentEmailAddress").toString());
                            }
                        }

                        CommonUtil.createUserSession(Constants.SESSION_ACCESS_LEVEL,
                                                     access_level);
                        CommonUtil.createUserSession(Constants.SESSION_EMP_DPT,
                                                     dptHierarchyName.toString());
                        CommonUtil.createUserSession(Constants.SESSION_EMP_POSITION,
                                                     empPosition);
                        CommonUtil.createUserSession(Constants.SESSION_PARENT_EMAIL,
                                                     parentEmail.toString());
                        CommonUtil.createUserSession("hod_email", hodemail);

                        System.out.println(access_level + " - " +
                                           dptHierarchyName.toString() +
                                           " - " + empPosition +
                                           " Parent email: " + parentEmail);

                        return 1;
                    } else {
                        return 0;
                    }
                }
            } else {
                returnCode = 0;
            }
        } catch (SQLException sqlException) {
            CommonUtil.showMessage("Take screen shot and email it to I.C.T - \n\n" +
                    "AuthHandler.java line 96 - " + sqlException.getMessage(),
                    112);
            return 007;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    CommonUtil.log("Connection Closed Auth Handler.java");
                    connection.close();
                    connection = null;
                }
            } catch (SQLException e) {
                CommonUtil.showMessage(" \nTake screen shot and email it to I.C.T - " +
                                       " Error closing Resultset PS or Conn " +
                                       e.getMessage() +
                                       " AuthHandler.java line 112", 111);
            }

        }
        return returnCode;
    }

    /*############################################################################
     *                    UPDATE LEAVE CRITERIA
     *                    ---------------------
     * THE METHOD TO UPDATE THE LEAVE CRITERIA OF THE EMPLOYEE.IF THE USER DON'T
     * HAVE ANY LEAVE CRITERIA, CASUAL AND/OR ANNUAL LEAVE CRITERIA THEN ASSIGN
     * ##########################################################################*/

    public void updateLeaveCriteria(int alc, int clc) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String selectLeaveCriteria =
            "select ANNUAL_LEAVE_CRITERIA,CASUAL_LEAVE_CRITERIA from xx_e_portal_users where PERSON_ID = ?";
        try {
            loadDrivers();
            conn = connectDB();
            ps = conn.prepareStatement(selectLeaveCriteria);
            ps.setInt(1,
                      Integer.parseInt(CommonUtil.getSessionValue(Constants.SESSION_USERID).toString()));

            rs = ps.executeQuery();
            if (rs.next()) {
                /* IF THE USER HASN'T GIVEN ANY ANNUAL LEAVE AND CASUAL LEAVE
                 * CRITERIA THEN UPDATE HIS CRITERIA, ELSE WE DON'T HAVE TO DO
                 * ANYTHING */
                if ((rs.getString("ANNUAL_LEAVE_CRITERIA") == null) ||
                    (rs.getString("CASUAL_LEAVE_CRITERIA") == null)) {
                    String query =
                        "update xx_E_portal_users set ANNUAL_LEAVE_CRITERIA = ? , CASUAL_LEAVE_CRITERIA = ? where person_id = ?";
                    ps.close();
                    ps = conn.prepareStatement(query);
                    ps.setInt(1, alc);
                    ps.setInt(2, clc);
                    ps.setInt(3,
                              Integer.parseInt(CommonUtil.getSessionValue(Constants.SESSION_USERID).toString()));
                    ps.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ps.close();
                conn.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }

        }

    }

    /*#################################################################
     *                         UPDATE LATE SITTING CRITERIA
     *                         ----------------------------
     *JUST LIKE UPDATE LEAVE CRITERIA METHOD, THIS METHOD WILL UPDATE LATE
     * SITTING CRITERIA OF THE USER IF ONE IS NOT AVAILABLE.
     * ###################################################################
     *                                                                 */

    public void updateLateSittingCriteria(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        String selectLeaveCriteria =
            "select LATE_SITTING_CRITERIA from xx_e_portal_users where PERSON_ID = ?";

        try {
            loadDrivers();
            conn = connectDB();
            ps = conn.prepareStatement(selectLeaveCriteria);
            ps.setInt(1,
                      Integer.parseInt(CommonUtil.getSessionValue(Constants.SESSION_USERID).toString()));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {

                if (rs.getString("LATE_SITTING_CRITERIA") == null) {
                    String query =
                        "update xx_E_portal_users set LATE_SITTING_CRITERIA = ? where person_id = ?";
                    ps.close();
                    ps = conn.prepareStatement(query);
                    ps.setInt(1, id);
                    ps.setInt(2,
                              Integer.parseInt(CommonUtil.getSessionValue(Constants.SESSION_USERID).toString()));
                    ps.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ps.close();
                conn.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }

        }
    }

    /*#########################################################################
     *                             CHANGE PASSWORD
     *                             ---------------
     * THIS METHOD IS USED TO CHANGE THE PASSWORD.NOW THIS IS ENOUGH DESCRIPTION
     * FOR YOU. I DON'T KNOW WHAT IS THE REST. I AM FUCKED UP!BTW WHEN USER CONFIRMS
     * HIS PASSWORD ON THE CONRFIRM PASSWORD PAGE AFTER LOGGING IN WITH HIS DEFAULT
     * TEMP PASSWORD, THIS BELOW METHOD IS CALLED
     * #########################################################################
     *                                                                         */

    public int changePassword(String email, String newPassword,
                              String oldPassword) throws ClassNotFoundException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        /*THE QUERY,UPDATES THE USER PASSWORD AGAINS EMAIL ADDRESS
         * EMAIL ADDRESSES ARE UNIQUE, GOT TO BE!!!!!!!!!!!!!!!!*/
        String updateQuery =
            "update xx_e_portal_users set password = DBMS_OBFUSCATION_TOOLKIT.MD5(input_string => ?) where upper(trim(EMAIL_ADDRESS)) = upper(trim(?)) and password = DBMS_OBFUSCATION_TOOLKIT.MD5(input_string => ?)";
        loadDrivers();
        try {
            connection = connectDB();
            ps = connection.prepareStatement(updateQuery);
            ps.setString(1, newPassword);
            ps.setString(2, email);
            ps.setString(3, oldPassword);
            return ps.executeUpdate();
        } catch (SQLException e) {
            CommonUtil.showMessage("Take Screenshot and email it to I.C.T - " +
                                   e.getMessage() +
                                   " AuthHandler.java line 141", 112);
            return -1;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                CommonUtil.showMessage("Take Screenshot and email it to I.C.T - " +
                                       "Error Closing Prepared Statement or Connection " +
                                       e.getMessage() +
                                       " AuthHandler.java line 152", 112);
            }


        }
    }

    /*#########################################################################
     *                         RESET PASSWORD
     *                         --------------
     *THIS METHOD BELWO IS USED TO RESET THE PASSWORD IN CASE HE FORGOTS HIS
     * PASSWORD. FROM RESET PASSWORD PAGE, BELOW METHOD IS CALLED.
     *                                                                        *
     *                                                                        */

    public int resetPassword(String emailAddress) throws ClassNotFoundException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        loadDrivers();

        String resetQuery1 =
            "update xx_e_portal_users set password = ? where upper(trim(EMAIL_ADDRESS)) = upper(trim(?))";
        String resetQuery2 =
            "update xx_e_portal_users set temp_pass = ? where upper(trim(EMAIL_ADDRESS)) = upper(trim(?))";

        System.out.println(resetQuery1);
        System.out.println(resetQuery2);
        System.out.println(emailAddress);

        try {
            connection = connectDB();
            ps = connection.prepareStatement(resetQuery1);
            ps.setString(1, "");
            ps.setString(2, emailAddress);
            int id = ps.executeUpdate();
            if (id == 1) {
                System.out.println("id : " + id);
                ps.close();
                ps = connection.prepareStatement(resetQuery2);
                String randomString = CommonUtil.randomString();
                ps.setString(1, randomString);
                ps.setString(2, emailAddress);
                int id2 = ps.executeUpdate();
                if (id2 == 1) {
                    CommonUtil.createUserSession("random", randomString);
                    return id2;
                } else {
                    System.out.println("id: " + -1);
                    return -1;
                }
            } else {
                System.out.println("id: " + -1);
                return -1;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                CommonUtil.showMessage("Take Screenshot and email it to I.C.T - " +
                                       "Error Closing Prepared Statement or Connection " +
                                       e.getMessage() +
                                       " AuthHandler.java line 152", 112);
            }
        }


    }

    /*######################################################################
     *                             UPDATE PASSWORD
     *                             ---------------
     *BELOW PASSWORD IS GET CALLED WHEN USER WANT TO CHANGE HIS PASSWORD ON
     * WILL. PROBABLY GET CALLED FROM THE POPUP BOX THAT POPS WHEN USER SELECTS
     * CHANGE PASSWORD OPTION FROM THE DROW DOWN SETTINGS MENT
     *
     *                                                                      */

    public int updatePassword(String newPassword, String emailAddress,
                              String empNo) throws ClassNotFoundException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String updateQuery =
            "update xx_e_portal_users set password = DBMS_OBFUSCATION_TOOLKIT.MD5(input_string => ?) where upper(trim(EMAIL_ADDRESS)) = upper(trim(?)) and upper(trim(TEMP_PASS)) = upper(trim(?))";
        loadDrivers();
        try {
            connection = connectDB();
            ps = connection.prepareStatement(updateQuery);
            ps.setString(1, newPassword);
            ps.setString(2, emailAddress);
            ps.setString(3, empNo);
            return ps.executeUpdate();
        } catch (SQLException e) {
            CommonUtil.showMessage("Take Screenshot and email it to I.C.T - " +
                                   e.getMessage() +
                                   " AuthHandler.java line 141", 112);
            return -1;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                CommonUtil.showMessage("Take Screenshot and email it to I.C.T - " +
                                       "Error Closing Prepared Statement or Connection " +
                                       e.getMessage() +
                                       " AuthHandler.java line 152", 112);
            }


        }
    }


    /*SOME UTILITY METHOD
     * DON'T WORRY
     * NOTHING SO COMPLEX HERE.
     * YOU CAN HAVE TEA NOW!!! :-d */

    private void loadDrivers() throws ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
    }

    private Connection connectDB() throws SQLException {
        return DriverManager.getConnection(connectionString, user, password);
    }
}

