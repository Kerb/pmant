package eu.pmant.app.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class AuthenticationFilter implements Filter {

    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
            "/api/login",
            "/api/logout",
            "/api/register"
    );

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        String requestURI = httpRequest.getRequestURI();

        // Check if the path is excluded from authentication
        if (isExcludedPath(requestURI)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // For all other paths, check if user is authenticated
        HttpSession session = httpRequest.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            // User is authenticated
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            // User is not authenticated
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write("{\"success\": false, \"message\": \"Unauthorized\"}");
        }
    }

    private boolean isExcludedPath(String requestURI) {
        return EXCLUDED_PATHS.stream().anyMatch(requestURI::startsWith);
    }
}
