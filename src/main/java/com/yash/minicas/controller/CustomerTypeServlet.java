package com.yash.minicas.controller;

import com.yash.minicas.exception.ApplicationNumberIncorrectException;
import com.yash.minicas.entity.LoanApplication;
import com.yash.minicas.service.CASService;
import com.yash.minicas.util.LoggerUtility;
import com.yash.minicas.util.ServletFilterUtilities;
import com.yash.minicas.util.factory.ServiceFactory;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/maker/loan/new", "/maker/loan/search"})
public class CustomerTypeServlet extends HttpServlet {
    private static final Logger     logger     = LoggerUtility.getLogger(CustomerTypeServlet.class);
    private final        CASService casService = ServiceFactory.getCasService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        ServletFilterUtilities.forwardToUrl("/WEB-INF/views/maker/customer-type.jsp", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String applicationNumber = req.getParameter("applicationNumber").trim();

        LoanApplication loanApplication = null;
        try {
            loanApplication = casService.searchLoanApplication(applicationNumber);
        } catch (ApplicationNumberIncorrectException e) {
            logger.error("{}", e.getMessage());
        }

        HttpSession session = req.getSession();

        if (loanApplication == null) {
            session.removeAttribute("loanApplication");
            session.setAttribute("flashMessage",
                                 "Loan application with number " + applicationNumber + " does not exist.");
            session.setAttribute("flashType", "error");
        } else {
            session.setAttribute("loanApplication", loanApplication);
            session.setAttribute("flashMessage", "Loan application found successfully.");
            session.setAttribute("flashType", "success");
        }

        ServletFilterUtilities.redirectToUrl("/maker/loan/new", req, resp);
    }
}
