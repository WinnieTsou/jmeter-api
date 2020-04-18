package winnie.jmeter_api;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.jmeter.assertions.DurationAssertion;
import org.apache.jmeter.assertions.ResponseAssertion;
import org.apache.jmeter.assertions.SizeAssertion;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.gui.LoopControlPanel;
import org.apache.jmeter.protocol.http.control.Cookie;
import org.apache.jmeter.protocol.http.control.CookieManager;
import org.apache.jmeter.protocol.http.control.Header;
import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.protocol.http.control.gui.HttpTestSampleGui;
import org.apache.jmeter.protocol.http.gui.CookiePanel;
import org.apache.jmeter.protocol.http.gui.HeaderPanel;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.protocol.http.util.HTTPFileArg;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.threads.gui.ThreadGroupGui;
import org.apache.jorphan.collections.HashTree;
import org.apache.jmeter.threads.ThreadGroup;

public class JMeterTestElement {
	public static final int RESPONSEASSERTION_RESPONSECODE = 5;
	public static final int RESPONSEASSERTION_RESPONSEDATA = 6;
	public static final int RESPONSEASSERTION_RESPONSEHEADER = 7;
	public static final int RESPONSEASSERTION_RESPONSEMESSAGE = 8;
	public static final int RESPONSEASSERTION_URL = 9;
	public static final int SIZEASSERTION_NETWORKSIZE = 1;
	public static final int SIZEASSERTION_RESPONSEBODY = 2;
	public static final int SIZEASSERTION_RESPONSECODE = 3;
	public static final int SIZEASSERTION_RESPONSEHEADER = 4;
	public static final int SIZEASSERTION_RESPONSEMESSAGE = 5;
	public static final int EQUAL = 1;
	public static final int NOTEQUAL = 2;
	public static final int GREATERTHAN = 3;
	public static final int LESSTHAN = 4;
	public static final int GREATERTHANEQUAL = 5;
	public static final int LESSTHANEQUAL = 6;
	public static final int CONTAIN = 7;
	public static final int MATCHE = 8;
	public static final int SUBSTRING = 9;
	private ThreadGroup threadGroup;
	private HashTree threadGroupHashTree;
	private HTTPSamplerProxy httpSampler;
	private LoopController loopController; 
	private HeaderManager headerManager;
	private CookieManager cookieManager;
	private String responseEncoding;
	
	public JMeterTestElement() {
		this.init();
	}
	
	public JMeterTestElement(Map<String, String> m) throws Exception {
		this.init();
		this.setURL(m.get("URL"));
		this.setNumThread(Integer.parseInt(m.get("COUNT")));
		this.setRampUp(Integer.parseInt(m.get("DURATION")));
		this.setResponseEncoding(m.get("ENCODING"));
		httpSampler.setMethod(m.get("METHOD"));
	}
	
	private void init() {
		threadGroupHashTree = new HashTree();
		threadGroup = new ThreadGroup();
		httpSampler = new HTTPSamplerProxy();
		loopController = new LoopController();
		headerManager = new HeaderManager();
		cookieManager = new CookieManager();
		
		
		httpSampler.setProperty(TestElement.TEST_CLASS, HTTPSamplerProxy.class.getName());
		httpSampler.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());
		loopController.setProperty(TestElement.TEST_CLASS, LoopController.class.getName());
		loopController.setProperty(TestElement.GUI_CLASS, LoopControlPanel.class.getName());
		threadGroup.setProperty(TestElement.TEST_CLASS, ThreadGroup.class.getName());
		threadGroup.setProperty(TestElement.GUI_CLASS, ThreadGroupGui.class.getName());
		cookieManager.setProperty(TestElement.TEST_CLASS, CookieManager.class.getName());
		cookieManager.setProperty(TestElement.GUI_CLASS, CookiePanel.class.getName());
		headerManager.setProperty(TestElement.TEST_CLASS, HeaderManager.class.getName());
		headerManager.setProperty(TestElement.GUI_CLASS, HeaderPanel.class.getName());
		
		loopController.setLoops(1);
		loopController.initialize();
		threadGroup.setSamplerController(loopController);
		threadGroup.setNumThreads(1); /// here
		threadGroup.setRampUp(1); ///here
	}
	
	protected HashTree getTestHashTree() {

		if (headerManager.size() > 0) {
			httpSampler.setHeaderManager(headerManager);
			threadGroupHashTree.add(httpSampler, headerManager);
		}
		if (cookieManager.getCookieCount() > 0) {
			httpSampler.setCookieManager(cookieManager);
			threadGroupHashTree.add(httpSampler, cookieManager);
		}
		
		threadGroupHashTree.add(httpSampler);
		
		return threadGroupHashTree;
	}
	
	protected ThreadGroup getTestThreadGroup() {
		return threadGroup;
	}

	public void addArgument(Map<String, String> args) {
		for (Map.Entry<String, String> entry : args.entrySet()) {
			addArgument(entry.getKey(), entry.getValue());
		}
	}

	public void addArgument(String key, String value) {
		httpSampler.addArgument(key, value);
	}

	public void addCookie(String name, String value, String domain, String path, boolean secure, long expires) {
		Cookie cookie = new Cookie(name, value, domain, path, secure, expires);
		cookieManager.add(cookie);
	}

	public void addRequestHeader(Map<String, String> headers) {
		for (Map.Entry<String, String> entry : headers.entrySet()) {
			this.addRequestHeader(entry.getKey(), entry.getValue());
		}
	}
	
	public void addRequestHeader(String name, String value) {
		Header header = new Header(name, value);
		headerManager.add(header);
	}

	public void sendDataJSON(String jsonData) {
		this.addRequestHeader("Content-Type", "application/json");
		httpSampler.setPostBodyRaw(true);
		httpSampler.addNonEncodedArgument("", jsonData, null);
	}

	public void sendDataRaw(String data) {
		this.addRequestHeader("Content-Type", "text/plain");
		httpSampler.setPostBodyRaw(true);
		httpSampler.addNonEncodedArgument("", data, null);
	}

	public void sendDataXML(String xmlData) {
		this.addRequestHeader("Content-Type", "application/xml");
		httpSampler.setPostBodyRaw(true);
		httpSampler.addNonEncodedArgument("", xmlData, null);
	}
	
	public void setAutoRedirects(boolean b) {
		httpSampler.setAutoRedirects(b);
	}

	private void setURL(String urlString) throws Exception {
		List<NameValuePair> params = null;
		URL url = new URL(urlString);

		params = URLEncodedUtils.parse(new URI(urlString), "UTF-8");

		httpSampler.setPort(url.getPort());
		httpSampler.setProtocol(url.getProtocol());
		httpSampler.setPath(url.getPath());
		httpSampler.setDomain(url.getHost());

		for (NameValuePair param : params) {
			this.addArgument(param.getName(), param.getValue());
		}
	}
	
	public void setConnectTimeout(String value) {
		httpSampler.setConnectTimeout(value);
	}
	
	public void setDelay(long delay) {
		threadGroup.setScheduler(true);
		threadGroup.setDelay(delay);
	}

	public void setDurationAssertion(String duration) {
		DurationAssertion durationAssertion = new DurationAssertion();
		durationAssertion.setProperty("DurationAssertion.duration", duration);
		threadGroupHashTree.add(httpSampler, durationAssertion);
	}

	public void setFollowRedirects(boolean b) {
		httpSampler.setFollowRedirects(b);
	}
	

	public void setHTTPFiles(List<Map<String, String>> filesMap) {
		HTTPFileArg[] file = new HTTPFileArg[filesMap.size()];

		for (int i = 0; i < filesMap.size(); i++) {
			file[i] = new HTTPFileArg((String) filesMap.get(i).get("PATH"), (String) filesMap.get(i).get("PARAMNAME"),
					(String) filesMap.get(i).get("MIME"));
		}
		httpSampler.setDoMultipart(true);
		httpSampler.setHTTPFiles(file);
	}
	
	public void setNumThread(int numThreads) {
		threadGroup.setNumThreads(numThreads);
	}

	public void setRampUp(int rampUp) {
		threadGroup.setRampUp(rampUp);
	}
	
	public void addResponseAssertion(int testField, int type, boolean b, String testString) {
		ResponseAssertion responseAssertion = new ResponseAssertion();
		switch (testField) {
		case RESPONSEASSERTION_RESPONSECODE: {
			responseAssertion.setTestFieldResponseCode();
			break;
		}
		case RESPONSEASSERTION_RESPONSEDATA: {
			responseAssertion.setTestFieldResponseData();
			break;
		}
		case RESPONSEASSERTION_RESPONSEHEADER: {
			responseAssertion.setTestFieldResponseHeaders();
			break;
		}
		case RESPONSEASSERTION_RESPONSEMESSAGE: {
			responseAssertion.setTestFieldResponseMessage();
			break;
		}
		case RESPONSEASSERTION_URL: {
			responseAssertion.setTestFieldURL();
			break;
		}
		}
		switch (type) {
		case CONTAIN: {
			responseAssertion.setToContainsType();
			break;
		}
		case EQUAL: {
			responseAssertion.setToEqualsType();
			break;
		}
		case MATCHE: {
			responseAssertion.setToMatchType();
			break;
		}
		case SUBSTRING: {
			responseAssertion.setToSubstringType();
			break;
		}
		}

		if (b) {
			responseAssertion.setToNotType();
		} else {
			responseAssertion.unsetNotType();
		}

		responseAssertion.addTestString(testString);
		threadGroupHashTree.add(httpSampler, responseAssertion);
	}
	
	public void setResponseEncoding(String encoding) {
		this.responseEncoding = encoding;
	}

	public void setResponseTimeout(String value) {
		httpSampler.setResponseTimeout(value);
	}
	
	public void addSizeAssertion(String size, int testField, int compareOperator) {
		SizeAssertion sizeAssertion = new SizeAssertion();
		sizeAssertion.setAllowedSize(size);

		switch (testField) {
		case SIZEASSERTION_NETWORKSIZE: {
			sizeAssertion.setTestFieldNetworkSize();
			break;
		}
		case SIZEASSERTION_RESPONSEBODY: {
			sizeAssertion.setTestFieldResponseBody();
			break;
		}
		case SIZEASSERTION_RESPONSECODE: {
			sizeAssertion.setTestFieldResponseCode();
			break;
		}
		case SIZEASSERTION_RESPONSEHEADER: {
			sizeAssertion.setTestFieldResponseHeaders();
			break;
		}
		case SIZEASSERTION_RESPONSEMESSAGE: {
			sizeAssertion.setTestFieldResponseMessage();
			break;
		}
		}
		sizeAssertion.setCompOper(compareOperator);
		threadGroupHashTree.add(httpSampler, sizeAssertion);
	}
}

