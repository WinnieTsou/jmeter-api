package winnie.jmeter_api;

import java.util.HashMap;
import java.util.Map;


public class Driver {

	public static void main(String[] args) throws Exception {
		Map<String, String> settings = new HashMap<String, String>();
		settings.put("URL", "http://localhost:5000/home?a=b");
		settings.put("METHOD", "get");
		settings.put("ENCODING", "utf-8");
		settings.put("COUNT", "1");
		settings.put("DURATION", "1");
		JMeterTestElement t1 = new JMeterTestElement(settings);
		t1.addCookie("name", "value", "localhost", "/", true, 1000000000);
		t1.addRequestHeader("Connection", "keep-alive");
		JMeterTestJob job1 = new JMeterTestJob();
		job1.addJMeterTestElement(t1);
		job1.run();
		
//		System.out.println(job1.getResult().get(0));
		
	}

}

