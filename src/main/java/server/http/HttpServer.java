package server.http;

import express.Express;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HttpServer {
    private Express express;
    public  HttpServer(Router router) {
        express = new Express();
        express.bind(router);
    }
    public HttpServer() {
        this.express = new Express();

    }
    public void start() {
        this.express.listen();
    }
    public void start(int port) {
        this.express.listen(port);
    }
}
