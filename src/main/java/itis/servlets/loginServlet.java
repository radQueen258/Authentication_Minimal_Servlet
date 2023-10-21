package itis.servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;

@WebServlet("/authentication")
public class loginServlet extends HttpServlet {
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "postgres";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/Accounts?useSSL=false";

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new ServletException("Database driver not found", e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accountEmail = request.getParameter("email");
        String accountPassword = request.getParameter("password");

        HttpSession session = request.getSession();
        RequestDispatcher dispatcher = null;

        try {
            Connection connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement("select * from accounts where email = ? and password = ?");
            preparedStatement.setString(1, accountEmail);
            preparedStatement.setString(2,accountPassword);

            ResultSet resultSet = preparedStatement.executeQuery();


            if (resultSet.next()) {
                session.setAttribute("username", resultSet.getString("username"));
                dispatcher = request.getRequestDispatcher("/html/final.html");
            } else {
                request.setAttribute("status", "Failed");
                dispatcher = request.getRequestDispatcher("/html/registration.html");
            }

            dispatcher.forward(request,response);
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

}
