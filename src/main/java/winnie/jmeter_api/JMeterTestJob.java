package winnie.jmeter_api;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.control.gui.TestPlanGui;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;

public class JMeterTestJob {
	private static boolean isSet = false;
	private StandardJMeterEngine jmeter;
	private TestPlan testPlan;
	private HashTree testPlanTree;
	private ResultData result;
	
	public JMeterTestJob() {
		jmeter = new StandardJMeterEngine();
		testPlan = new TestPlan();
		testPlanTree = new HashTree();

		if (!isSet) {
			File jmeterPropertyFile = new File("jmeter.properties");
			if ( !jmeterPropertyFile.exists() ) {
				try {
					jmeterPropertyFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			JMeterUtils.loadJMeterProperties("jmeter.properties");
			JMeterUtils.setProperty("language", "en");
			JMeterUtils.initLocale();
			isSet = true;
		}

		testPlan.setProperty(TestElement.TEST_CLASS, TestPlan.class.getName());
		testPlan.setProperty(TestElement.GUI_CLASS, TestPlanGui.class.getName());
		testPlan.setUserDefinedVariables((Arguments) new ArgumentsPanel().createTestElement());

		testPlanTree.add(testPlan);
	}

	public void addJMeterTestElement(JMeterTestElement element) {

		HashTree threadGroupHashTree = testPlanTree.add(testPlan, element.getTestThreadGroup());
		threadGroupHashTree.add(element.getTestHashTree());

	}
	
	public void run() {
		result = new ResultData();
		testPlanTree.add(testPlanTree.getArray()[0], result);
		jmeter.configure(testPlanTree);
		jmeter.run();
		
	}
	
	public List<Map<String, Object>> getResult() {
		return result.getResult();
	}
}
