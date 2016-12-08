package parser.parsing.selenium;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parser.utils.TimeUtil;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;

@Service
class SeleniumJsController {
	private static final Logger log = LoggerFactory.getLogger(SeleniumJsController.class);
	
	@Autowired
	private Driver driver;
	
	private final static String AJAX_COUNTER_SCRIPT = "ajaxCounter.js";
	private String ajaxCounterScript;
	private boolean ajaxCounterScriptExecuted = false;
	
	@PostConstruct
	public void init() {
		URL scriptResource = getClass().getClassLoader().getResource(AJAX_COUNTER_SCRIPT);
		if (scriptResource == null) {
			log.warn("Unable to load AJAX counter script from classpath:{}", AJAX_COUNTER_SCRIPT);
			return;
		}
		try {
			ajaxCounterScript = Resources.toString(scriptResource, Charsets.UTF_8);
		}
		catch (IOException e) {
			log.warn("Error while reading AJAX counter script from classpath:{}", AJAX_COUNTER_SCRIPT, e);
		}
	}
	
	void executeAjaxCounterScript() {
		if (ajaxCounterScript != null) {
			log.info("Executing AJAX counter script");
			driver.executeJs(ajaxCounterScript);
			ajaxCounterScriptExecuted = true;
		}
	}
	
	void waitForAjaxToFinish() {
		waitForAjaxToFinish(200);
	}
	void waitForAjaxToFinish(long millisToWait) {
		if (ajaxCounterScriptExecuted) {
			log.trace("Waiting for AJAX to finish ...");
			long started = System.currentTimeMillis();
			Long currentAjaxCount;
			do {
				TimeUtil.sleep(millisToWait);
				currentAjaxCount = (Long) driver.executeJs("return window.openHTTPs == null ? 0 : window.openHTTPs;");
			} while (currentAjaxCount != null && currentAjaxCount > 0);
			log.trace("AJAX finished after {} ms", System.currentTimeMillis() - started);
		}
	}
	
}
