package eu.execom.hawaii.security;

import eu.execom.hawaii.service.IdTokenVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class IdTokenVerifierFilter implements Filter {
    public static final String ID_TOKEN_HEADER = "X-ID-TOKEN";

    private final IdTokenVerifier idTokenVerifier;

    @Autowired
    public IdTokenVerifierFilter(IdTokenVerifier idTokenVerifier) {

        this.idTokenVerifier = idTokenVerifier;
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String idToken = ((HttpServletRequest) servletRequest).getHeader(ID_TOKEN_HEADER);

        if (idToken == null) {
            ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            boolean tokenIsValid = idTokenVerifier.verify(idToken);

            if (tokenIsValid) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
    }

    @Override
    public void destroy() {
    }
}
