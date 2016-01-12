package view.nishat.net.Servlet;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.ResultSetMetaData;

import java.sql.Timestamp;

import javax.servlet.*;
import javax.servlet.http.*;

public class TheServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
    static PrintWriter out;
    static int number;
    static Connection connection;
    static boolean processRunning;
    static String activeUsers;
    static int count;
    static Thread th;
    static String employee_name;
    static String month;
    static Runnable runnable = new Runnable() {

        @Override
        public void run() {
            //while (processRunning) {
            try {
                //READ ACTIVE EMPLOYEE
                activeUsers = "select * from XX_E_PORTAL_emp_empty_atd";

                connection = getConnection();
                PreparedStatement ps =
                    connection.prepareStatement(activeUsers, ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = ps.executeQuery();
                ResultSetMetaData rdms =  rs.getMetaData();
                for (int i = 1; i<= rdms.getColumnCount();i++) {
                    System.out.println(rdms.getColumnName(i));
                }
                while (rs.next()) {
                    String insertQuery =
                        "insert into xx_e_portal_emp_atd(emp_atd_id,emp_id,emp_name,expected_work_hours,max_start_time,max_end_time,end_time,start_time,attendance_date) select xx_e_portal_sequence.nextval,?,?,?,?,?,?,?,? from dual";
                    PreparedStatement insertPST =
                        connection.prepareStatement(insertQuery);
                    insertPST.setInt(1, rs.getInt("PERSON_ID"));
                    insertPST.setString(2, rs.getString("LAST_NAME"));
                    insertPST.setString(3,
                                        rs.getString("EXPECTED_WORK_HOURS"));
                    insertPST.setString(4, rs.getString("max_start_time"));
                    insertPST.setString(5, rs.getString("max_end_time"));
                    insertPST.setString(6, rs.getString("end_time"));
                    insertPST.setString(7, rs.getString("start_time"));
                    insertPST.setTimestamp(8, rs.getTimestamp("ATTENDANCE_DATE"));
                    int resultID = insertPST.executeUpdate();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
            }
        }

        //}
    };

    private static Connection getConnection() throws Exception {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        return DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.30:1523:ERPTEST2",
                                           "XX_E_PORTAL", "mskiz145");
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        th = new Thread(runnable);
        processRunning = true;
        if (!th.isAlive()) {

            th.start();
            System.out.println("Started.");
        }


    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException,
                                                           IOException {
        response.setContentType(CONTENT_TYPE);
        out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>TheServlet</title></head>");
        out.println("<body>");
        out.println("Total rows: " + count + " (" + activeUsers + ")");
        out.println("</body></html>");
        out.close();
    }

    @Override
    public void destroy() {
        super.destroy();
        System.out.println("destroy called.");
        th.stop();
        th = null;
    }
}
