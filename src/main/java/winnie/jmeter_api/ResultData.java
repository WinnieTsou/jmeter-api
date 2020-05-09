package winnie.jmeter_api;

import java.util.*;
import org.apache.jmeter.assertions.Assertion;
import org.apache.jmeter.assertions.AssertionResult;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class ResultData extends ResultCollector {
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	
	protected List<Map<String, Object>> getResult(){
		return new ArrayList<Map<String, Object>>(list);
	}
	
	protected String getResultAsJSON() {
		GsonBuilder builder = new GsonBuilder();
		builder.disableHtmlEscaping();
		Gson gson = builder.create();
        Type gsonType = new TypeToken<List>(){}.getType();
        String gsonString = gson.toJson(list ,gsonType);
        return gsonString;
	}
	
	@Override
	public synchronized void sampleOccurred(SampleEvent e) {
		super.sampleOccurred(e);
		SampleResult record = e.getResult();
		list.add(result2Map(record));
	}
	
	private Map<String, Object> result2Map(SampleResult record) {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("SAMPLE TIME", Long.toString(record.getTime()));
		map.put("THREAD NUMBER", record.getThreadName());
		map.put("URL", record.getUrlAsString());
		map.put("REQUEST HEADER", record.getRequestHeaders());
		map.put("RESPONSE CODE", record.getResponseCode());
		map.put("RESPONSE MESSAGE", record.getResponseMessage());
		map.put("RESPONSE DATA", record.getResponseDataAsString());
		map.put("RESPONSE HEADER", record.getResponseHeaders());
		map.put("HEADER SIZE", Integer.toString(record.getHeadersSize()));
		map.put("BYTES", Long.toString(record.getBytes()));
		map.put("CONTENT TYPE", record.getContentType());
		map.put("MEDIA TYPE", record.getMediaType());
		map.put("END TIME", Long.toString(record.getEndTime()));
		map.put("LATENCY", Long.toString(record.getLatency()));
		map.put("IDLE TIME", Long.toString(record.getIdleTime()));
		map.put("CONNECT TIME", Long.toString(record.getConnectTime()));
		map.put("START TIME", Long.toString(record.getStartTime()));
		map.put("BODY SIZE", Integer.toString(record.getBodySize()));
		map.put("SAMPLE LABEL", record.getSampleLabel());
		map.put("SAMPLE COUNT", Integer.toString(record.getSampleCount()));
		map.put("ERROR COUNT", Integer.toString(record.getErrorCount()));
		
		int count = 1;
		Map<String, String> subResult;
		for (AssertionResult rs: record.getAssertionResults()) {
			subResult = new HashMap<String, String>();
			subResult.put("ASSERTION MSG", rs.getFailureMessage());
			subResult.put("ASSERTION FAIL", Boolean.toString(rs.isFailure()));
			map.put("ASSERTION " + count, subResult);
			count ++;
		}
		

		return map;
	}
}
