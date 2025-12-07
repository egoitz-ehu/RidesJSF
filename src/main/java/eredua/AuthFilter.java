package eredua;

import java.util.Set;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import jakarta.enterprise.inject.spi.CDI;
import eredua.bean.AuthBean;

public class AuthFilter implements Filter {
    private static final Set<String> PRIVATE_PAGES = new HashSet<>();

    @Override
    public void init(FilterConfig filterConfig) {
        PRIVATE_PAGES.add("/CreateRide.xhtml");
        PRIVATE_PAGES.add("/ManageMoney.xhtml");
        PRIVATE_PAGES.add("/ManageReservationsDriver.xhtml");
        PRIVATE_PAGES.add("/Sarrera.xhtml");
        PRIVATE_PAGES.add("/SearchRide.xhtml");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        String requestURI = req.getRequestURI();
        String contextPath = req.getContextPath();
        
        if (requestURI.contains("/jakarta.faces.resource/")) {
            chain.doFilter(request, response);
            return;
        }
        
        String path = requestURI.substring(contextPath.length());
        
        if (!PRIVATE_PAGES.contains(path)) {
            chain.doFilter(request, response);
            return;
        }
        
        boolean loggedIn = false;
        try {
            AuthBean authBean = CDI.current().select(AuthBean.class).get();
            loggedIn = (authBean != null && authBean.getUser() != null);
        } catch (Exception e) {
            System.err.println("Error getting AuthBean from CDI: " + e.getMessage());
        }
        
        System.out.println("AuthFilter - Path: " + path + " | LoggedIn: " + loggedIn);
        
        if (loggedIn) {
            chain.doFilter(request, response);
        } else {
            res.sendRedirect(contextPath + "/Entry.xhtml");
        }
    }
}
