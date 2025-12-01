package it.zerob.learn.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet("/doLogin")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doLogin(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doLogin(req, resp);
    }

    protected void doLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/login.html");
            return;
        }

        Connection connessione = null;
        try {
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            
            connessione = DriverManager.getConnection(
                "jdbc:oracle:thin:@franellucci-zb:1521:XE",
                "ZEROBSPORTS", 
                "zrbpwdzerobsports"
            );

            String loginQuery = "SELECT * FROM ZS_UTENTI WHERE USERNAME = ? AND PASSWORD = ?";
            PreparedStatement login = connessione.prepareStatement(loginQuery);
            login.setString(1, username);
            login.setString(2, password);

            ResultSet loginResult = login.executeQuery();

            if (loginResult.next()) {
                request.getSession().setAttribute("utenteLoggato", loginResult.getString("USERNAME"));
                response.sendRedirect(request.getContextPath() + "/secure/giocatori.html");
            } else {
                response.sendRedirect(request.getContextPath() + "/login.html?error=1");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            // Redirect to an error page or show an error message
            response.sendRedirect(request.getContextPath() + "/login.html?error=2");
        } finally {
            try {
                if (connessione != null && !connessione.isClosed()) {
                    connessione.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}