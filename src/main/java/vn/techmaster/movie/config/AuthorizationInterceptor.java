package vn.techmaster.movie.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import vn.techmaster.movie.entity.User;

import java.util.Objects;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Lấy thông tin user từ session
        User user = (User) request.getSession().getAttribute("currentUser");

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // Lấy ra role cuả user
        String role = user.getRole().getValue();
        if (Objects.equals(role, "ROLE_ADMIN")) {
            return true;
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
    }
}
