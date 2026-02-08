package com.yash.minicas.controller;

import com.yash.minicas.entity.LoanApplication;
import com.yash.minicas.entity.User;
import com.yash.minicas.service.CASService;
import com.yash.minicas.util.ServletFilterUtilities;
import com.yash.minicas.util.factory.ServiceFactory;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@WebServlet(urlPatterns = {"/maker/loan/applications", "/checker/dashboard"})
public class LoanApplicationsServlet extends HttpServlet {
    private static final String     FLASH_MESSAGE = "flashMessage";
    private static final String     FLASH_TYPE    = "flashType";
    private static final CASService casService    = ServiceFactory.getCasService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        User        user    = (User) session.getAttribute("user");

        List<LoanApplication> loanApplicationList = casService.fetchLoansForUser(user);
        req.setAttribute("loanApplications", loanApplicationList);

        String flashMessage = (String) session.getAttribute(FLASH_MESSAGE);
        String flashType    = (String) session.getAttribute(FLASH_TYPE);
        if (flashMessage != null) {
            req.setAttribute(FLASH_MESSAGE, flashMessage);
            req.setAttribute(FLASH_TYPE, flashType);
            session.removeAttribute(FLASH_MESSAGE);
            session.removeAttribute(FLASH_TYPE);
        }

        ServletFilterUtilities.forwardToUrl("/WEB-INF/views/shared/loan-applications.jsp", req, resp);
    }
}