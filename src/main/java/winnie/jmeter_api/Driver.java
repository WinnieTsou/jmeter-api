package winnie.jmeter_api;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;



public class Driver {

	public static void main(String[] args) throws Exception {
		Map<String, String> settings = new HashMap<String, String>();
		settings.put("URL", "http://localhost:5000/home?dfds=222222&k=00&h=99");
		settings.put("METHOD", "post");
		settings.put("ENCODING", "utf-8");
		settings.put("COUNT", "3");
		settings.put("DURATION", "1");
		JMeterTestElement t1 = new JMeterTestElement(settings);
		Map<String, String> filemap = new HashMap<String, String>();
		filemap.put("PATH", "/Users/winnie/eclipse-workspace/jmeter-api/jmeter.properties");
		filemap.put("PARAMNAME", "jmeter");
		filemap.put("MIME", "plain/text");
		List<Map<String, String>> filemaps = new ArrayList<Map<String,String>>();
		filemaps.add(filemap);
		t1.setHTTPFiles(filemaps);
		t1.addRequestHeader("Connection", "keep-alive");
		t1.addCookie("ddd", "dddfg", "localhost", "/", false, 100000000);
		t1.sendDataRaw("{ \"data\": \"hi there\"}");
		t1.addResponseAssertion(JMeterTestElement.RESPONSEASSERTION_RESPONSECODE, 
				JMeterTestElement.EQUAL, false, "200");
		t1.addSizeAssertion("10", JMeterTestElement.SIZEASSERTION_RESPONSEHEADER, JMeterTestElement.LESSTHAN);
		JMeterTestJob job1 = new JMeterTestJob();
		job1.addJMeterTestElement(t1);
		job1.run();
//		for (Map<String, Object> rsMap : job1.getResultAsList()) {
//			for (Map.Entry<String, Object> entry : rsMap.entrySet()) {
//				System.out.print(entry.getKey() + ": " + entry.getValue() + " ");
//				System.out.println();
//			}
//		}
		System.out.println(job1.getResultAsJSON());
		
//		Map<String, String> family = new HashMap<String, String>();
//		family.put("Mom", "LFC");
//		family.put("Bro", "TCY");
//		
//
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("Name", "Winnie");
//		map.put("Family", family);
//		
//		
//		Gson gson = new Gson();
//        Type gsonType = new TypeToken<HashMap>(){}.getType();
//        String gsonString = gson.toJson(map,gsonType);
//        System.out.println(gsonString);
		
	}

}

