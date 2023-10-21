package itis.servlets;

import itis.models.User;
import itis.repository.AccountsRepository;
import itis.repository.AccountsRepositoryJdbcImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet("/save")
public class SaveServlet extends HttpServlet {

    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/Authentications";

    private AccountsRepository accountsRepository;

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            Connection connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            accountsRepository = new AccountsRepositoryJdbcImpl(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/html/save.html").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       String accountUsername = request.getParameter("username");
       String accountEmail = request.getParameter("email");
       String accountPassword = request.getParameter("password");

        User user = User.builder()
                .userName(accountUsername)
                .userEmail(accountEmail)
                .userPassword(accountPassword)
                .build();

        try{
            accountsRepository.save(user);
            response.sendRedirect("/registration");
        } catch (SQLException e) {
            response.sendRedirect("/save");
            throw new RuntimeException(e);
        }
    }
}
