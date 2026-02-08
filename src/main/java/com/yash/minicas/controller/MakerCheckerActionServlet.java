package com.yash.minicas.controller;

import com.yash.minicas.entity.Role;
import com.yash.minicas.entity.User;
import com.yash.minicas.service.checker.CheckerService;
import com.yash.minicas.service.maker.MakerService;
import com.yash.minicas.util.ServletFilterUtilities;
import com.yash.minicas.util.factory.ServiceFactory;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/maker/loan/applications/action", "/checker/dashboard/action"})
public class MakerCheckerActionServlet extends HttpServlet {
    private static final String         ERROR   = "error";
    private static final MakerService   MAKER   = ServiceFactory.getCasService();
    private static final CheckerService CHECKER = ServiceFactory.getCasService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        User        user    = (User) session.getAttribute("user");

        if (user == null) {
            ServletFilterUtilities.forwardToUrl("/access-denied", req, resp);
            return;
        }

        String   applicationNumber = req.getParameter("applicationNumber");
        String   action            = req.getParameter("action");
        String[] result;

        switch (user.getRole()) {
            case Role.MAKER -> result = handleMakerAction(applicationNumber, action);
            case Role.CHECKER -> result = handleCheckerAction(applicationNumber, action);
            default -> result = new String[] {"Unauthorized Action", ERROR};
        }

        session.setAttribute("flashMessage", result[0]);
        session.setAttribute("flashType", result[1]);

        String url = Role.MAKER == user.getRole() ? "/maker/loan/applications" : "/checker/dashboard";
        ServletFilterUtilities.redirectToUrl(url, req, resp);
    }

    private String[] handleMakerAction(String applicationNumber, String action) {
        String result = switch (action.toUpperCase()) {
            case "MODIFY" -> MAKER.modifyLoanApplication(applicationNumber);
            case "DELETE" -> MAKER.deleteLoanApplication(applicationNumber);
            default -> "";
        };
        if (result.isEmpty()) {
            return new String[] {"Something went wrong. Please try again later.", ERROR};
        }

        return new String[] {result, "success"};
    }

    private String[] handleCheckerAction(String applicationNumber, String action) {
        String result = switch (action.toUpperCase()) {
            case "APPROVE" -> CHECKER.approveLoan(applicationNumber);
            case "REJECT" -> CHECKER.rejectLoan(applicationNumber);
            default -> "";
        };
        if (result.isEmpty()) {
            return new String[] {"Something went wrong. Please try again later.", ERROR};
        }

        return new String[] {result, "success"};
    }
}
