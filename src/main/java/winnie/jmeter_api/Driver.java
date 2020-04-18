package winnie.jmeter_api;

import java.util.HashMap;
import java.util.Map;


public class Driver {

	public static void main(String[] args) throws Exception {
		Map<String, String> settings = new HashMap<String, String>();
		settings.put("URL", "http://localhost:5000/home?dfds=222222&k=00&h=99");
		settings.put("METHOD", "get");
		settings.put("ENCODING", "utf-8");
		settings.put("COUNT", "1");
		settings.put("DURATION", "1");
		JMeterTestElement t1 = new JMeterTestElement(settings);
		t1.addRequestHeader("Connection", "keep-alive");
		JMeterTestJob job1 = new JMeterTestJob();
		job1.addJMeterTestElement(t1);
		job1.run();
		System.out.println(job1.getResult().get(0));
		
	}

}

