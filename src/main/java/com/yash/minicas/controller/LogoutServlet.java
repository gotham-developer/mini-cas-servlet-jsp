package com.yash.minicas.controller;

import com.yash.minicas.util.ServletFilterUtilities;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        ServletFilterUtilities.deleteCookiesByName(req, resp, "username", "userRole");

        // Invalidate older session if it exists
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Create a new session just for flash message, also it will be used later on
        HttpSession newSession = req.getSession(true);
        newSession.setAttribute("flashMessage", "Logged out successfully");
        newSession.setAttribute("flashType", "success");

        ServletFilterUtilities.redirectToUrl("/root", req, resp);
    }
}