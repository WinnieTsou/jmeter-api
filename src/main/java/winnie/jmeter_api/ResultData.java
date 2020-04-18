package winnie.jmeter_api;

import java.util.*;

import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;

public class ResultData extends ResultCollector {
	private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	
	protected List<Map<String, String>> getResult(){
		return new ArrayList<Map<String, String>>(list);
	}
	
	
	@Override
	public synchronized void sampleOccurred(SampleEvent e) {
		super.sampleOccurred(e);
		SampleResult record = e.getResult();
		list.add(result2Map(record));
	}
	
	private Map<String, String> result2Map(SampleResult record) {
		Map<String, String> map = new HashMap<String, String>();

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

		return map;
	}
}
