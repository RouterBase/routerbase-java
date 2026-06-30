package com.routerbase;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

public interface Transport {
    TransportResponse request(String method, URI uri, Map<String, String> headers, String body)
            throws IOException, InterruptedException;
}
