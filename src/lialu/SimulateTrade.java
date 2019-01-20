package lialu;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntPredicate;


public class SimulateTrade {
	String[]testStock;
	double startFund;
	
	public SimulateTrade(String[]testStock) {
		this.testStock = testStock;
		this.startFund = 100_000;
	}
	
	public Map<String, Double[]> getMA9And20Result() throws Exception{
		Map<String, Double[]> map = new HashMap<String, Double[]>();
		MovingAverageAnalysis mAnalysis = new MovingAverageAnalysis();
		DataSourceQuery query = new DataSourceQuery();
		
		for (String stock : testStock) {
			StockData[]datas = query.requestStockDatasInTimeRange(stock, DataSourceQuery.oneYear);
			List<MAObservation> actionList = mAnalysis.analysisBasedOn9DaysAnd20Days(datas);
			Map<Date, Double> dateToPriceMap = new HashMap();
			for (StockData stockData : datas) {
				dateToPriceMap.put(stockData.date, stockData.close);
			}
			
			double money = startFund;
			double share = 0.0;
			
			for (MAObservation maBasedAction : actionList) {
				if (maBasedAction.action == MABasedAction.buy) {
					if (money > 0) {
					share = money/dateToPriceMap.get(maBasedAction.date);
					money = 0;
					}
				}
				else if (maBasedAction.action == MABasedAction.sell) {
					if (share > 0) {
						money = dateToPriceMap.get(maBasedAction.date) * share;
						share = 0;
					}
				}
			}
			
			money += share * dateToPriceMap.get(datas[datas.length - 1].date);
			
			map.put(stock, new Double[] {money/this.startFund, datas[datas.length - 1].close/datas[0].close});
		}
		
		return map;
	}
}
