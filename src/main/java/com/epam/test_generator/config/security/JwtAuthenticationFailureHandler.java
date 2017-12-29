package com.epam.test_generator.config.security;

import com.epam.test_generator.dto.ErrorDTO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * This simple override removes the default behavior of a unsuccessful authentication. And let us to
 * intercept the Exceptions and set the type of our response to JSON which content our Exception
 * like {@link ErrorDTO} with 403 status.
 */
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception)
        throws IOException, ServletException {
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new ErrorDTO(exception)));
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

    }
}
