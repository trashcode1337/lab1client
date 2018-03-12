package com.drrufus.lab1client;

import com.drrufus.lab1client.soap.Utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


public class Application {
    
    private final static String ENDPOINT_URL = "http://localhost:8080/ws";
    private final static String SOAP_ACTION = "someTestHeader";
    private final static String[] PARAMS = { "id", "login", "name", "email", "pass"};
    
    public static void main(String[] args) throws IOException {
    
        Map<String, String> paramsMap = new HashMap();
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        for (String param : PARAMS) {
            System.out.print(param + ": ");
            String value = br.readLine();
            System.out.println();
            if (!value.isEmpty())
                paramsMap.put(param, value);
        }

        Utils.callSoapWebService(ENDPOINT_URL, SOAP_ACTION, paramsMap);
    }
}
