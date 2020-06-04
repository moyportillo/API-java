package server.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;

public class RootHttpHandler implements HttpHandler {
    public void handle(HttpExchange Exchange) throws IOException {

        OutputStream outputStream = Exchange.getResponseBody();
        StringBuilder contentBuilder = new StringBuilder();

        JSONObject jres = new JSONObject();
        jres.put("description", "Webserver API");
        jres.put("version", "0.0.1");

        contentBuilder.append(jres.toString());

        Exchange.getResponseHeaders().add("Content-type", "application");
        Exchange.sendResponseHeaders(200, contentBuilder.length());
        outputStream.write(contentBuilder.toString().getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
