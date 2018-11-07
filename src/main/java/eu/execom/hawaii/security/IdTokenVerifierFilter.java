package eu.execom.hawaii.security;

import eu.execom.hawaii.model.User;
import eu.execom.hawaii.service.TokenIdentityVerifier;
import eu.execom.hawaii.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

public class IdTokenVerifierFilter extends OncePerRequestFilter {
    public static final String ID_TOKEN_HEADER = "X-ID-TOKEN";

    private final TokenIdentityVerifier tokenIdentityVerifier;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public IdTokenVerifierFilter(TokenIdentityVerifier tokenIdentityVerifier, UserService userService,
            AuthenticationManager authenticationManager) {

        this.tokenIdentityVerifier = tokenIdentityVerifier;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @Override protected void doFilterInternal(HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String idToken = httpServletRequest.getHeader(ID_TOKEN_HEADER);

        Optional<String> userIdentity = tryToGetUserIdentityFromToken(idToken);

        if(!userIdentity.isPresent()) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        User user = userService.findByEmail(userIdentity.get());

        if (user == null) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (!user.isActive()) {
            httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        Authentication authentication = authenticationManager.authenticate(
                new PreAuthenticatedAuthenticationToken(user, null,
                        Collections.singletonList(new SimpleGrantedAuthority(user.getUserRole().toString()))));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private Optional<String> tryToGetUserIdentityFromToken(String token) {
        return token == null ? Optional.empty() : tokenIdentityVerifier.tryToGetIdentityOf(token);
    }
}
