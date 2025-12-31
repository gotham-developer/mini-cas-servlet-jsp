package com.yash.minicas.filter;

import com.yash.minicas.entity.Role;
import com.yash.minicas.entity.User;
import com.yash.minicas.service.AuthService;
import com.yash.minicas.util.ServletFilterUtilities;
import com.yash.minicas.util.factory.ServiceFactory;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class RememberMeFilter implements Filter {
    private static final AuthService authService = ServiceFactory.getAuthService();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest  request  = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String              uri      = request.getRequestURI().substring(request.getContextPath().length());

        if (ServletFilterUtilities.isPublicPage(uri) || ServletFilterUtilities.isStaticResource(uri)) {
            filterChain.doFilter(request, response);
            return;
        }

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = ServletFilterUtilities.getCookieValue(request, "username");
        String roleStr  = ServletFilterUtilities.getCookieValue(request, "userRole");
        if (username == null || roleStr == null) {
            filterChain.doFilter(request, response);
            return;
        }

        Role role = Role.valueOf(roleStr);
        User user = authService.fetchUserByUsername(username);
        if (user == null) {
            filterChain.doFilter(request, response);
            return;
        }

        session = request.getSession(true);
        session.setAttribute("user", user);

        if ("/login".equals(uri) || "/root".equals(uri)) {
            session.setAttribute("flashMessage", "Welcome " + role + ": " + username);
            ServletFilterUtilities.redirectUserToDashboardForSubsequentLogins(user.getRole(), request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

}
