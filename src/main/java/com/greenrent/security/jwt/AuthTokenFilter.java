package com.greenrent.security.jwt;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import com.greenrent.repository.UserRepository;
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String jwt=parseJwt(request);

            if(jwt!=null && jwtUtils.validateJwtToken(jwt)) {
                Long id= jwtUtils.getIdFromJwtToken(jwt);

                //userRepository.fi

                //request.setAttribute("id", id);


            }

        }catch(Exception e) {

        }


    }
    private String parseJwt(HttpServletRequest request) {
        String headerAuth=request.getHeader("Authorization");

        if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // TODO Auto-generated method stub
        return super.shouldNotFilter(request);
    }


}