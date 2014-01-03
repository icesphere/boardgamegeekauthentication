package org.smartreaction.boardgamegeek.auth;

import com.sun.appserv.security.AppservRealm;
import com.sun.enterprise.security.auth.realm.InvalidOperationException;
import com.sun.enterprise.security.auth.realm.NoSuchUserException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class CustomRealm extends AppservRealm
{
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
