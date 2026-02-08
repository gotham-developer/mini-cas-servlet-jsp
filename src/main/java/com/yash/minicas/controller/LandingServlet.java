package com.yash.minicas.controller;

import com.yash.minicas.entity.User;
import com.yash.minicas.util.ServletFilterUtilities;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/root"}, loadOnStartup = 1)
public class LandingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        User        user    = (session != null) ? (User) session.getAttribute("user") : null;

        if (user != null) {
            ServletFilterUtilities.redirectUserToDashboardForSubsequentLogins(user.getRole(), req, resp);
            return;
        }

        ServletFilterUtilities.redirectToUrl("/login", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        doGet(req, resp);
    }
}

