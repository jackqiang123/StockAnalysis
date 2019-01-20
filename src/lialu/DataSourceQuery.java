package lialu;

import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import com.google.gson.Gson;

public class DataSourceQuery {
	public static String fiveYear = "5y";
	public static String twoYear = "2y";
	public static String oneYear = "1y";
	public static String ytd = "ytd";
	public static String sixMonth = "6m";
	public static String threeMonth = "3m";
	public static String oneMonthString = "1m";
	
    private static Gson gson = new Gson();
    
	public StockData[] requestStockDatasInTimeRange(String symbol, String range) throws Exception{
        String url = "https://api.iextrading.com/1.0/stock/"+ symbol +"/chart/" + range;
        
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();
        if (responseCode != 200) {
        	return new StockData[0];
        }
        
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();


        StockData[] stocks = gson.fromJson(response.toString(), StockData[].class);
        
        return stocks;
	}
}

