package com.yash.minicas.filter;

import com.yash.minicas.entity.User;
import com.yash.minicas.util.LoggerUtility;
import com.yash.minicas.util.ServletFilterUtilities;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class AuthFilter implements Filter {
    private static final Logger logger = LoggerUtility.getLogger(AuthFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest  request  = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String              uri      = request.getRequestURI().substring(request.getContextPath().length());

        if (uri.isEmpty() || "/".equals(uri)) {
            response.sendRedirect(request.getContextPath() + "/root");
            return;
        }

        if (ServletFilterUtilities.isPublicPage(uri) || ServletFilterUtilities.isStaticResource(uri)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = request.getSession(false);
        User        user    = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/root");
            return;
        }

        if (!ServletFilterUtilities.isAuthorized(user.getRole(), uri)) {
            logger.info("Not Authorized");
            response.sendRedirect(request.getContextPath() + "/access-denied");
            return;
        }

        chain.doFilter(request, response);
    }

}
