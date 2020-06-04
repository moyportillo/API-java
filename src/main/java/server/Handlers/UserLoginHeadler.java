package server.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;
import server.ServerHttp;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserLoginHeadler implements HttpHandler {
    public void handle(HttpExchange Exchange) throws IOException {
        //esto son cursores de lectura y escritura de streams que es una salida de datos binarios
        InputStream inputStream = Exchange.getRequestBody();
        OutputStream outputStream = Exchange.getResponseBody();
        //Donde se va a guardar el contenido en modo texto, contructores de streams
        StringBuilder bodyBuilder = new StringBuilder();
        StringBuilder contentBuilder = new StringBuilder();
        //*
        InputStreamReader reader = new InputStreamReader(inputStream, "utf-8"); //aqui va bit por bit leyendo
        BufferedReader br = new BufferedReader(reader); // aqui lo va guardando todo el recorido

        int bc;
        while((bc = br.read()) != -1) {
            bodyBuilder.append((char) bc); //aqui lo va metiendo en el builder
        }
        // hasta aqui es el metodo de lectura en como se van a ller los datos del stream
        System.out.println(bodyBuilder.toString());
        JSONObject jreq = new JSONObject(bodyBuilder.toString());
        System.out.println(jreq.getString("UsrUsr"));
        String UsrUsr = jreq.getString("UsrUsr");
        String UsrPwd = jreq.getString("UsrPwd");
        try {
            String qry = "SELECT * FROM usuarios where UsrUsr = ? and UsrPwd = ?;";
            PreparedStatement st =  ServerHttp.conn.prepareStatement(qry);
            st.setString(1, UsrUsr);
            st.setString(2, UsrPwd);
            ResultSet rs = st.executeQuery();

            JSONObject jres = new JSONObject();

            if(rs.next()){
                System.out.println("Usuario Valido");
                jres.put("Status", "Activo");
                jres.put("payload", "Usuario Valido");
            }
            else{
                jres.put("Status", "Error");
                jres.put("payload", "Usuario No Valido");
                System.out.println("Usuario no Encontrado");
            }
            contentBuilder.append(jres.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Exchange.getResponseHeaders().add("Content-type", "application/json"); //este es para que me envie el resultado como json
        Exchange.sendResponseHeaders(200, contentBuilder.length());
        outputStream.write(contentBuilder.toString().getBytes()); // escribimos en la salida
        inputStream.close(); //Siempre que usemos un reader, buffer, outputstream hay que cerrarlos
        br.close();
        inputStream.close();
        outputStream.flush();
        outputStream.close();
    }
}
