package learniverse.learniversemain.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CorsFilter extends OncePerRequestFilter {
    // 허용하는 Origin 목록
    private static final List<String> ALLOWED_ORIGINS = new ArrayList<>(
            Arrays.asList(
                    "https://learniverse-front-end.vercel.app",
                    "http://localhost:3000"
            )
    );

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String origin = request.getHeader("Origin");
        response.addHeader("Access-Control-Allow-Origin", origin);
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "*");
        response.setIntHeader("Access-Control-Max-Age", 3600);

        if(ALLOWED_ORIGINS.contains(origin))
            filterChain.doFilter(request, response);
    }
}
