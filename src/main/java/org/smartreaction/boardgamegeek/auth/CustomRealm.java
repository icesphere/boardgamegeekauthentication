package org.smartreaction.boardgamegeek.auth;

import com.sun.appserv.security.AppservRealm;
import com.sun.enterprise.security.auth.realm.BadRealmException;
import com.sun.enterprise.security.auth.realm.InvalidOperationException;
import com.sun.enterprise.security.auth.realm.NoSuchRealmException;
import com.sun.enterprise.security.auth.realm.NoSuchUserException;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomRealm extends AppservRealm
{
    public static final String PARAM_JAAS_CONTEXT = "jaas-context";

    @Override
    public void init(Properties properties) throws BadRealmException, NoSuchRealmException
    {
        String propJaasContext = properties.getProperty(PARAM_JAAS_CONTEXT);

        if (propJaasContext != null) setProperty(PARAM_JAAS_CONTEXT, propJaasContext);

        Logger logger = Logger.getLogger(this.getClass().getName());

        String realmName = this.getClass().getSimpleName();

        logger.log(Level.INFO, realmName + " started. ");
        logger.log(Level.INFO, realmName + ": " + getAuthType());
        logger.log(Level.INFO, realmName + " authentication uses jar file in $domain/lib folder ");

        for (Map.Entry<Object, Object> property:properties.entrySet()) logger.log(Level.INFO, property.getKey() + ": " + property.getValue());
    }

    @Override
    public String getAuthType()
    {
        return "Login to BGG";
    }

    @Override
    public Enumeration getGroupNames(String username) throws InvalidOperationException, NoSuchUserException
    {
        List<String> groups = new ArrayList<>();
        groups.add("USER");
        return Collections.enumeration(groups);
    }
}
