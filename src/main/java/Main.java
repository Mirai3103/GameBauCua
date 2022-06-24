import com.mysql.cj.xdevapi.Client;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("require args");
            return;
        }
        if ("server".equalsIgnoreCase(args[0])) {
            System.out.println("server");
            server.Server.run();
        }else{
            System.out.println("client");
            client.client.run();
        }
    }
}
