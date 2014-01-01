package org.smartreaction.boardgamegeek.auth;

import com.sun.jersey.api.client.Client;

public class BoardGameGeekClient
{
    private static Client client = Client.create();

    public static Client getClient()
    {
        return client;
    }
}
