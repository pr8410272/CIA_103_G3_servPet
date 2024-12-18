package com.servPet;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 可選的初始化代碼
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 獲取當前請求的URL
        String requestURI = httpRequest.getRequestURI();

        // 只對需要身份驗證的路徑進行檢查
        if (requestURI.startsWith("/back_end/admin") && !requestURI.equals("/back_end/toAdminLogin") && !requestURI.equals("/back_end/adminLogin")) {
            // 檢查 session 中是否有用戶對象
            Object adminVO = httpRequest.getSession().getAttribute("adminVO");
            Object psVO = httpRequest.getSession().getAttribute("psVO");

            // 如果沒有登入，重定向到登入頁面
            if (adminVO == null && psVO == null) {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/back_end/toAdminLogin");
                return;
            }
        }

        // 如果已經登入，放行請求
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // 可選的銷毀代碼
    }
}
