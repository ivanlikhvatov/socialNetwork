package com.example.socialNetwork.component;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.*;
import java.io.IOException;

@Component("myAuthenticationSuccessHandler")
public class MySimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws IOException {
//        HttpSession session = request.getSession(false);
//        if (session != null) {
//            LoggedUser user = new LoggedUser(authentication.getName(), activeUserStore);
//            session.setAttribute("user", user);
//        }

        System.out.println("ddddddsjkdflsjdsfjljsdlkjflksdjf");
    }
}
