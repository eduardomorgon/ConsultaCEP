package com.github.eduardomorgon.consultacep;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 * @author Eduardo Morgon <eduardo.morgon+github at gmail.com>
 */
public class Programa {
    
    private static final String CEP = "01311000";
    
    public static void main(String[] args) throws IOException {
        
        URL url = new URL("http://www.buscacep.correios.com.br/sistemas/buscacep/resultadoBuscaCepEndereco.cfm");
        
        String parametros = "relaxation="+CEP+"&tipoCEP=ALL&semelhante=N";

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
            wr.write(parametros.getBytes());
        }
        connection.connect();
        
        InputStream inputStream = connection.getInputStream();
        
        String html = new BufferedReader(new InputStreamReader(inputStream, "ISO-8859-1"))
                .lines()
                .map(linha -> linha)
                .collect(Collectors.joining());
        
        Pattern pattern = Pattern.compile("(?:<td.*?>)(.*?)(?:</td>)");
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            String tr = matcher.group(1); 
            System.out.println(tr.replace("&nbsp", ""));
        }
    }
    
}
