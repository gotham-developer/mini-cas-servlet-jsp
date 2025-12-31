package com.yash.minicas.controller;

import com.yash.minicas.entity.Customer;
import com.yash.minicas.service.maker.MakerService;
import com.yash.minicas.util.DataValidationUtilities;
import com.yash.minicas.util.ServletFilterUtilities;
import com.yash.minicas.util.factory.ServiceFactory;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/maker/loan/new/application", "/maker/loan/new/application/submit"})
public class LoanApplicationFormServlet extends HttpServlet {
    private static final MakerService makerService = ServiceFactory.getCasService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        ServletFilterUtilities.forwardToUrl("/WEB-INF/views/maker/loan-application-form.jsp", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session  = req.getSession();
        Customer    customer = (Customer) session.getAttribute("customer");
        session.removeAttribute("customer");

        String loanAmountStr = req.getParameter("loanAmount");
        String rateStr       = req.getParameter("rate");
        String tenureStr     = req.getParameter("tenure");
        String tenureIn      = req.getParameter("tenureIn");
        String productType   = req.getParameter("productType");
        String product       = req.getParameter("product");
        String scheme        = req.getParameter("scheme");

        if (tenureIn == null || tenureIn.isEmpty()) {
            tenureIn = "Months";
        }

        double loanAmount = DataValidationUtilities.parseDouble(loanAmountStr, 0);
        double rate       = DataValidationUtilities.parseDouble(rateStr, 0);
        int    tenure     = DataValidationUtilities.parseInt(tenureStr, 0);

        String applicationNumber =
                makerService.addLoanApplication(loanAmount, rate, tenure, productType, product, scheme, tenureIn,
                                                customer);

        if (applicationNumber == null || applicationNumber.isEmpty()) {
            session.setAttribute("flashMessage", "Loan was not applied");
            session.setAttribute("flashType", "error");
        } else {
            session.setAttribute("flashMessage", "Loan Applied Successfully, Application Number: " + applicationNumber);
            session.setAttribute("flashType", "success");
        }

        ServletFilterUtilities.redirectToUrl("/maker/loan/applications", req, resp);
    }
}
