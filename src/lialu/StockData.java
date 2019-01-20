package lialu;

import java.lang.reflect.Field;
import java.util.Date;

public class StockData {
    public Date date;
    public double close;
    public double high;
    public double low;
    public double open;
    public int volume;
    public String symbol;
    
    public double getPriceByField(String fieldName) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
    	Field filedField = StockData.class.getField(fieldName);
    	return filedField.getDouble(this);
    }
}

