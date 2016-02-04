package view.nishat.net.Servlet;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import java.sql.Blob;
import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import javax.servlet.*;
import javax.servlet.http.*;

import view.nishat.net.Helper.CommonUtil;

public class ImageParser extends HttpServlet {
    private static final String CONTENT_TYPE = "image/png; charset=UTF-8";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException,
                                                           IOException {
        response.setContentType(CONTENT_TYPE);
        String personID = request.getParameter("person_id");
        OutputStream os = response.getOutputStream();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        //response.getWriter().print("Hello");
        try {
            conn = getConnection();
            ps =
                conn.prepareStatement("select image from PER_IMAGES where parent_id = " +
                                      personID);
            rs  = ps.executeQuery();
            if (rs.next()) {
                Blob image = rs.getBlob("IMAGE");
                BufferedInputStream bis =
                    new BufferedInputStream(image.getBinaryStream());
                byte[] buffer = new byte[10240];
                int b;
                while ((b = bis.read(buffer, 0, 10240)) != -1) {
                    os.write(buffer, 0, b);
                }
                os.close();
                
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                CommonUtil.log("Connection Closed: ImageParse.java");
                rs.close();
                ps.close();
                conn.close();
                conn = null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private Connection getConnection() throws Exception {
        loadOracleDrivers();
        
//        return DriverManager.getConnection("jdbc:oracle:thin:@10.152.13.34:1522:prod",
//                                           "apps", "mskiz145");
        return DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.31:1522:prod",
                                           "apps", "mskiz145");
    }

    private static void loadOracleDrivers() throws ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
    }

}
