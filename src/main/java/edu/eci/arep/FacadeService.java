package edu.eci.arep;

import java.io.*;
import java.net.*;

import com.google.gson.JsonObject;

public class FacadeService {
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String GET_URL = "http://localhost:45000/compreflex?method=";

    private static FacadeService instance;

    public static FacadeService getInstance(){
        if(instance == null){
            instance = new FacadeService();
        }
        return instance;
    }

    public static JsonObject getReflectiveCommand(String query) throws IOException {

        URL obj = new URL(GET_URL + query);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        JsonObject responseJson = new JsonObject();
        //The following invocation perform the connection implicitly before getting the code
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
            responseJson.addProperty("response", response.toString());
        } else {
            System.out.println("GET request not worked");
        }
        System.out.println("GET DONE");
        return responseJson;
    }
}
