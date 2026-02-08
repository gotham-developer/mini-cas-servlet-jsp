package com.yash.minicas.controller;

import com.yash.minicas.util.ServletFilterUtilities;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/maker/dashboard"})
public class MakerDashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        ServletFilterUtilities.forwardToUrl("/WEB-INF/views/maker/dashboard.jsp", req, resp);
    }
}