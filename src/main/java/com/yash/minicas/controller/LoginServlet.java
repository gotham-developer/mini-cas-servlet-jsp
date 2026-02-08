package com.yash.minicas.controller;

import com.yash.minicas.entity.Role;
import com.yash.minicas.entity.User;
import com.yash.minicas.service.AuthService;
import com.yash.minicas.util.ServletFilterUtilities;
import com.yash.minicas.util.factory.ServiceFactory;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    private static final AuthService authService = ServiceFactory.getAuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        ServletFilterUtilities.forwardToUrl("/WEB-INF/views/login.jsp", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String username   = req.getParameter("username");
        String password   = req.getParameter("password");
        String rememberMe = req.getParameter("rememberMe");
        String userRole   = req.getParameter("userRole");

        HttpSession session = req.getSession(true);
        Role        role    = Role.valueOf(userRole.toUpperCase());

        User user = authService.validateUser(username, password, role);

        if (user == null) {
            session.setAttribute("flashMessage", "Invalid Credentials, Try Again!");
            session.setAttribute("flashType", "error");
            ServletFilterUtilities.redirectToUrl("/root", req, resp);
            return;
        }

        session.setAttribute("user", user);

        if ("on".equalsIgnoreCase(rememberMe)) {
            ServletFilterUtilities.setRememberMeCookies(resp, user);
        }

        ServletFilterUtilities.redirectUserToDashboardForFirstLogin(req, resp);
    }

}
