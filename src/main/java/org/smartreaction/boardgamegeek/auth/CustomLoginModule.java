package org.smartreaction.boardgamegeek.auth;

import com.sun.appserv.security.AppservPasswordLoginModule;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.apache.commons.lang.StringUtils;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.security.auth.login.LoginException;
import javax.security.jacc.PolicyContext;
import javax.security.jacc.PolicyContextException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;

@SuppressWarnings("UnusedDeclaration")
public class CustomLoginModule extends AppservPasswordLoginModule
{
    @Override
    protected void authenticateUser() throws LoginException
    {
        String password = new String(_passwd);

        if (!StringUtils.isEmpty(password)) {
            Client client = BoardGameGeekClient.getClient();

            WebResource webResource = client.resource("http://boardgamegeek.com/login");

            MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
            queryParams.add("username", _username);
            queryParams.add("password", password);

            ClientResponse response = webResource.queryParams(queryParams).post(ClientResponse.class);

            boolean loginSuccessful = false;

            int status = response.getStatus();
            if (status == 200) {
                for (NewCookie newCookie : response.getCookies()) {
                    if(newCookie.getName().equalsIgnoreCase("bggpassword")) {
                        loginSuccessful = true;

                        try {
                            HttpServletRequest request = (HttpServletRequest) PolicyContext.getContext(HttpServletRequest.class.getName());
                            request.getSession().setAttribute("bggPasswordCookie", newCookie.getValue());
                        } catch (PolicyContextException e) {
                            throw new LoginException("Error getting http request");
                        }
                    }
                }
            }

            if (!loginSuccessful) {
                throw new LoginException("Username or password is not valid");
            }
        }

        String[] groups = {"USER"};
        commitUserAuthentication(groups);
    }
}
