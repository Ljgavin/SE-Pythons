package v1;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

public class Requests {
    
	public static String apiCall(String req) {

        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(req);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            for(String line; (line = reader.readLine()) != null;) {
                result.append(line);
            }

            reader.close();
            return result.toString();
        }
        catch(Exception e) {
            e.printStackTrace();
            System.out.println("API_FAIL");
            return "";

        }
	}

	
}
