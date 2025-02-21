package edu.eci.arep;

import com.google.gson.JsonObject;


import java.lang.reflect.Method;
import java.net.*;
import java.io.*;
import java.util.Arrays;

public class ReflexCalculator {

    public static void main(String[] args) throws IOException {
        try{
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(36000);
            } catch (IOException e) {
                System.err.println("Could not listen on port: 35000.");
                System.exit(1);
            }

            Socket clientSocket = null;
            boolean running = true;
            while (running){
                try {
                    System.out.println("Listo para recibir ...");
                    clientSocket = serverSocket.accept();
                } catch (IOException e) {
                    System.err.println("Accept failed.");
                    System.exit(1);
                }
                PrintWriter out = new PrintWriter(
                        clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                String inputLine, outputLine;
                boolean firstLine = true;
                String requestStringURI = "";
                while ((inputLine = in.readLine()) != null) {
                    if(firstLine){
                        System.out.println("Recibí: " + inputLine);
                        requestStringURI = inputLine.split("")[1];
                        firstLine = false;
                        System.out.println("URI: " + requestStringURI);
                        continue;
                    }
                    if (!in.ready()) {break; }
                }
                URI requestURI = new URI(requestStringURI);
                System.out.println(requestURI.toString());
                JsonObject response = makeReflection(requestURI.getQuery().split("=")[1]);
                outputLine = "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: application/json\r\n" +
                        "\r\n" +
                        response.toString();

                out.println(outputLine);
                out.close();
                in.close();
                clientSocket.close();
            }
            serverSocket.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private static JsonObject makeReflection(String query) throws Exception {
        String reflectiveMethod = query.split("\\(")[0];
        String params = query.split("\\(")[1].split("\\)")[0];

        JsonObject response = new JsonObject();
        String className = "java.lang.Math";
        Class<?> classInvoke = Class.forName(className);

        if(reflectiveMethod.equals("bbl")){
            response.addProperty("response", "En construcción");
        }
        else{
            try{
                Double number = Double.parseDouble(params);
                Class<?>[] offeredTypesArray = {};
                Method method = classInvoke.getMethod(reflectiveMethod, offeredTypesArray);
                response = invokingMethod(method, number);
            } catch (Exception e){
                e.printStackTrace();
            }

        }

        return response;
    }
    private static JsonObject invokingMethod(Method method,Double number) throws Exception {
        JsonObject response = new JsonObject();
        Object result = method.invoke(number);
        response.addProperty("result", result.toString());
        return response;
    }

    private static JsonObject getJsonBbl(int[] toSort){
        JsonObject response = new JsonObject();
        int[] sorted = bubbleSort(toSort);
        response.addProperty("response", Arrays.toString(sorted));
        return response;
    }

    private static int[] bubbleSort(int[] toSort){
        boolean changedArray = true;
        while (changedArray){
            for( int i= 0; i< toSort.length; i++){
                if (toSort[i] >toSort[i+1]){
                    int placeholder = toSort[i];
                    toSort[i] = toSort[i+1];
                }
                else{
                    changedArray = false;
                }
            }
        }
        return toSort;
    }
}
