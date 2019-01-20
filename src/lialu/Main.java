package lialu;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Main {
	private static DecimalFormat df2 = new DecimalFormat(".##");
	
	public static void main(String[]args) throws Exception {
		new EmailNotification().sendEmail();
		
		SimulateTrade sTrade = new SimulateTrade(NasDaqStockList.StockList);
		Map<String, Double[]> res = sTrade.getMA9And20Result();
		List<Performance> ls = new ArrayList<Performance>();
		for (String stock: res.keySet()) {
			ls.add(new Performance(stock, res.get(stock)[1], res.get(stock)[0]));
		}
		
		Collections.sort(ls, new Comparator<Performance>() {

			@Override
			public int compare(Performance o1, Performance o2) {
				// TODO Auto-generated method stub
				if(o1.ratio < o2.ratio) return 1;
				else if (o1.ratio > o2.ratio) return -1;
				return 0;
			}
		});
		
		for (Performance pfPerformance : ls) {
			System.out.println(pfPerformance.symbol + "," + df2.format(pfPerformance.ratio)+ ","+df2.format(pfPerformance.algorithm)+","+df2.format(pfPerformance.benchMark));
		}
		
			
	}
	
	
}

class Performance{
	String symbol;
	double benchMark;
	double algorithm;
	double ratio;
	public Performance(String s, double ben, double algo) {
		this.symbol = s;
		this.benchMark = ben;
		this.algorithm = algo;
		this.ratio = this.algorithm/this.benchMark;
	}
}