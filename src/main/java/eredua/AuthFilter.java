package eredua;

import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class AuthFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession(false);

        Object user = (session != null) ? session.getAttribute("authBean") : null;
        boolean loggedIn = false;
        if (user != null) {
            try {
                Object userObj = user.getClass().getMethod("getUser").invoke(user);
                loggedIn = userObj != null;
            } catch (Exception e) {
                loggedIn = false;
            }
        }
        
        System.out.print(loggedIn);

        String requestURI = req.getRequestURI();
        String contextPath = req.getContextPath();

        boolean isPublicPage = requestURI.equals(contextPath + "/") ||
                              requestURI.equals(contextPath + "/Login.xhtml") ||
                              requestURI.equals(contextPath + "/Register.xhtml") ||
                              requestURI.equals(contextPath + "/Entry.xhtml");

        if (loggedIn || isPublicPage) {
            chain.doFilter(request, response);
        } else {
            ((HttpServletResponse) response).sendRedirect(contextPath + "/Entry.xhtml");
        }
    }
}
