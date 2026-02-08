package com.yash.minicas.util;

import com.yash.minicas.entity.Role;
import com.yash.minicas.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class ServletFilterUtilities {
    private static final Logger logger = LoggerUtility.getLogger(ServletFilterUtilities.class);

    private static final Set<String>             STATIC_EXTENSIONS   = Set.of(".png", ".jpg", ".jpeg", ".gif", ".svg", ".ico");
    private static final Set<String>             STATIC_PREFIXES     = Set.of("/script", "/css", "/images");
    private static final Set<Pattern>            PUBLIC_URL_PATTERNS = compilePatterns("/root", "/login", "/logout", "/access-denied");
    private static final Map<Role, Set<Pattern>> ROLE_URL_PATTERNS   = new EnumMap<>(Role.class);
    private static final Set<Pattern>            SHARED_URL_PATTERNS = compilePatterns("/shared/loanApplications(/.*)?");
    private static final int                     COOKIE_MAX_AGE      = 7 * 24 * 60 * 60; // 7 days

    private ServletFilterUtilities() { }

    static {
        ROLE_URL_PATTERNS.put(Role.MAKER, compilePatterns("/maker(/.*)?"));
        ROLE_URL_PATTERNS.put(Role.CHECKER, compilePatterns("/checker(/.*)?"));
    }

    private static Set<Pattern> compilePatterns(String... urls) {
        Set<Pattern> patterns = new HashSet<>();
        for (String url : urls) {
            patterns.add(Pattern.compile("^" + url + "$"));
        }
        return patterns;
    }

    public static boolean matches(Set<Pattern> patterns, String uri) {
        for (Pattern pattern : patterns) {
            if (pattern.matcher(uri).matches()) { return true; }
        }
        return false;
    }

    public static boolean isPublicPage(String uri) {
        return matches(PUBLIC_URL_PATTERNS, uri);
    }

    public static boolean isStaticResource(String uri) {
        int qIdx = uri.indexOf("?");
        if (qIdx != -1) {
            uri = uri.substring(0, qIdx);
        }
        boolean isStaticExtension = STATIC_EXTENSIONS.stream().anyMatch(uri :: endsWith);
        boolean isStaticPrefix    = STATIC_PREFIXES.stream().anyMatch(uri :: startsWith);

        return isStaticPrefix || isStaticExtension;
    }

    public static boolean isAuthorized(Role role, String uri) {
        if (matches(SHARED_URL_PATTERNS, uri)) { return true; }

        Set<Pattern> allowed = ROLE_URL_PATTERNS.get(role);
        return allowed != null && matches(allowed, uri);
    }

    public static String getCookieValue(HttpServletRequest req, String name) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) { return null; }

        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }

    public static void redirectUserToDashboardForFirstLogin(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);

        User user = (User) session.getAttribute("user");

        try {
            String redirectPath;
            switch (user.getRole()) {
                case Role.MAKER -> redirectPath = req.getContextPath() + "/maker/dashboard";
                case Role.CHECKER -> redirectPath = req.getContextPath() + "/checker/dashboard";
                default -> {
                    String msg = URLEncoder.encode("Invalid session, Please log in again.", StandardCharsets.UTF_8);
                    resp.sendRedirect(req.getContextPath() + "/root?msg=" + msg);
                    return;
                }
            }

            session.setAttribute("flashMessage", "Welcome " + user.getRole() + ": " + user.getUsername());
            resp.sendRedirect(redirectPath);
        } catch (IOException e) {
            logger.error("Error while redirecting user", e);
        }
    }

    public static void redirectUserToDashboardForSubsequentLogins(Role role, HttpServletRequest req, HttpServletResponse res) {
        try {
            switch (role) {
                case Role.MAKER -> res.sendRedirect(req.getContextPath() + "/maker/dashboard");
                case Role.CHECKER -> res.sendRedirect(req.getContextPath() + "/checker/dashboard");
                default -> {
                    req.getSession().invalidate();
                    res.sendRedirect(req.getContextPath() + "/root");
                }
            }
        } catch (IOException e) {
            logger.error("{}", e.getMessage());
        }
    }

    public static void setRememberMeCookies(HttpServletResponse resp, User user) {
        Cookie usernameCookie = new Cookie("username", user.getUsername());
        Cookie roleCookie     = new Cookie("userRole", user.getRole().name());

        usernameCookie.setMaxAge(COOKIE_MAX_AGE);
        roleCookie.setMaxAge(COOKIE_MAX_AGE);

        usernameCookie.setPath("/");
        roleCookie.setPath("/");

        usernameCookie.setHttpOnly(true); // security best practice
        roleCookie.setHttpOnly(true);

        usernameCookie.setSecure(true);
        roleCookie.setSecure(true);

        resp.addCookie(usernameCookie);
        resp.addCookie(roleCookie);
    }

    public static void deleteCookiesByName(HttpServletRequest req, HttpServletResponse resp, String... cookieNames) {
        for (String name : cookieNames) {
            Cookie cookie = new Cookie(name, "");
            cookie.setMaxAge(0);              // delete cookie
            cookie.setPath("/");              // root path
            cookie.setHttpOnly(true);         // match original if applicable
            cookie.setSecure(req.isSecure()); // match original if applicable
            resp.addCookie(cookie);
        }
    }

    public static void forwardToUrl(String url, HttpServletRequest req, HttpServletResponse resp) {
        String forwardURL = url.endsWith(".jsp") ? url : req.getContextPath() + url;
        try {
            req.getRequestDispatcher(forwardURL).forward(req, resp);
        } catch (ServletException | IOException e) {
            logger.error("{}", e.getMessage());
        }
    }

    public static void redirectToUrl(String url, HttpServletRequest req, HttpServletResponse resp) {
        String redirectURL = url.endsWith(".jsp") ? url : req.getContextPath() + url;
        try {
            resp.sendRedirect(redirectURL);
        } catch (IOException e) {
            logger.error("{}", e.getMessage());
        }
    }
}
