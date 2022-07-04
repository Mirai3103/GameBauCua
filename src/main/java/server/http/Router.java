package server.http;
import com.google.gson.Gson;
import express.DynExpress;
import express.http.RequestMethod;
import express.http.request.Request;
import express.http.response.Response;
import server.model.User;

import java.io.*;
import java.util.stream.Collectors;

public class Router {
    private final Gson gson = new Gson();
     @DynExpress(method = RequestMethod.POST, context = "/login")
        public void login(Request request, Response response) {
            response.send("login");
        }
    @DynExpress(method = RequestMethod.POST, context = "/signup")
        public void signup(Request request, Response response) throws IOException {
        InputStream inputStream = request.getBody();

        System.out.println(getJson(inputStream));
        User user = new User();
        user.setUsername("test");
        user.setPassword("test");
        user.setFullName("test");
        String json = gson.toJson(user);
            response.send(json);

        }
        private String getJson(InputStream inputStream){
            return  new BufferedReader(new InputStreamReader(inputStream))
                    .lines().collect(Collectors.joining("\n"));
        }
}
