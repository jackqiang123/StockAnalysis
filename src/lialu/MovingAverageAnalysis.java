package lialu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MovingAverageAnalysis {	
	public List<MAObservation> analysisBasedOn9DaysAnd20Days(StockData[] stockDatas) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException{
		List<MovingAverageData> nineDayMovingAverageData = convertRawStockToMovingAverage(stockDatas, "close", 9);
		List<MovingAverageData> twentyDayMovingAverageData = convertRawStockToMovingAverage(stockDatas, "close", 20);
		
		List<MAObservation> observations = new ArrayList<MAObservation>();
		int len1 = nineDayMovingAverageData.size();
		int len2 = twentyDayMovingAverageData.size();
		int diff = len1 - len2;
		for (int i = 1; i < len2; i++) {
			int nineDayIndex = i + diff;
			if (nineDayMovingAverageData.get(nineDayIndex).date != twentyDayMovingAverageData.get(i).date) {
				throw new IllegalArgumentException("data is not clean");
			}
			
			if (nineDayMovingAverageData.get(nineDayIndex).value > twentyDayMovingAverageData.get(i).value) {
				if (nineDayMovingAverageData.get(nineDayIndex - 1).value < twentyDayMovingAverageData.get(i - 1).value) {
					observations.add(new MAObservation(MABasedAction.buy, nineDayMovingAverageData.get(nineDayIndex).date));
				}
			}		
			else {
				if (nineDayMovingAverageData.get(nineDayIndex - 1).value > twentyDayMovingAverageData.get(i - 1).value) {
					observations.add(new MAObservation(MABasedAction.sell, nineDayMovingAverageData.get(nineDayIndex).date));
				}
			}
		}
		
		Collections.sort(observations, new Comparator<MAObservation>() {

			@Override
			public int compare(MAObservation o1, MAObservation o2) {
				// TODO Auto-generated method stub
				return o1.date.compareTo(o2.date);
			}
		});
		
		return observations;
	}
	
	
	public List<MovingAverageData> convertRawStockToMovingAverage(
			StockData[] stockDatas, 
			String standard, 
			int averageDays) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		if (averageDays <= 0) 
			throw new IllegalArgumentException("moving average on zero days");
		
		List<MovingAverageData> resAverageDatas = new ArrayList<MovingAverageData>();
		
		if (stockDatas.length < averageDays) return resAverageDatas; // less than required date
		
		double totalprice = 0;
		for (int i = 0; i < averageDays; i++) {
			totalprice += stockDatas[i].getPriceByField(standard);
		}
		
		resAverageDatas.add(new MovingAverageData(stockDatas[0].symbol, stockDatas[averageDays - 1].date, totalprice/averageDays));
		
		for (int i = averageDays; i < stockDatas.length; i++) {
			totalprice -= stockDatas[i - averageDays].getPriceByField(standard);
			totalprice += stockDatas[i].getPriceByField(standard);
			resAverageDatas.add(new MovingAverageData(stockDatas[i].symbol, stockDatas[i].date, totalprice/averageDays));
		}
		
		return resAverageDatas;
	}
}

enum MABasedAction {
	hold,
	sell,
	buy
}

class MAObservation{
	MABasedAction action;
	double count = 0.0;
	Date date;
	
	public MAObservation(MABasedAction action, Date date) {
		this.action = action;
		this.date = date;
		
		if (action == MABasedAction.buy) {
			this.count = 0.1;// increase by 10%
		}
		else if (action == MABasedAction.sell) {
			this.count = 0.5; // sell by 50%
		}
	}
}
