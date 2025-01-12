package org.ablonewolf.externalService;

import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.LoopResources;

public abstract class AbstractHttpClient {

    protected final HttpClient httpClient;

    public AbstractHttpClient() {
        var loopResources = LoopResources.create("worker_thread", 1, true);
        this.httpClient = HttpClient.create().runOn(loopResources);
    }
}
