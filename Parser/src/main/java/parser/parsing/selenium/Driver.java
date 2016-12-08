package parser.parsing.selenium;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parser.parsing.configuration.ParseBrowser;
import parser.parsing.configuration.SystemSettings;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

@Service
class Driver {
	private static final Logger log = LoggerFactory.getLogger(Driver.class);
	
	private WebDriver driver;
	
	@Autowired
	private SystemSettings systemSettings;
	@Autowired
	private SeleniumJsController seleniumJsController;
	
	@PostConstruct
	private void init() {
		log.debug("Initializing web driver: {}", systemSettings.getChosenBrowser());
		
		switch (systemSettings.getChosenBrowser()) {
			case FirefoxPortable:
				notForExecutable(ParseBrowser.FirefoxPortable);
				//portable firefox zawarty w projekcie - widac co sie otwiera - do testow
				File pathToBinary = new File(getProjectPath() + "/lib/FirefoxPortable/App/Firefox/firefox.exe");
				FirefoxBinary ffBinary = new FirefoxBinary(pathToBinary);
				FirefoxProfile firefoxProfile = new FirefoxProfile();
				driver = new FirefoxDriver(ffBinary, firefoxProfile);
				break;

			case PhantomJS:
				DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
				if (!systemSettings.isShowLogs()) {
					String[] phantomArgs = new String[]{
						"--webdriver-loglevel=NONE"
					};
					desiredCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, phantomArgs);
					java.util.logging.Logger.getLogger(PhantomJSDriverService.class.getName()).setLevel(Level.OFF);
				}
				String phantomJsPath;
				if (systemSettings.isExecutable()) {
					phantomJsPath = getProjectPath() + "/lib/phantomjs/bin/phantomjs.exe";
				}
				else {
					phantomJsPath = getProjectPath() + "/lib/phantomjs-2.1.1-windows/bin/phantomjs.exe";
				}
				desiredCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, phantomJsPath);
				
				driver = new PhantomJSDriver(desiredCapabilities);
				break;
			
			case FirefoxProxy:
				notForExecutable(ParseBrowser.FirefoxProxy);
				Proxy proxy = new Proxy();
				proxy.setHttpProxy("proxy.otago.pl:8080");
				DesiredCapabilities cap = new DesiredCapabilities();
				cap.setCapability(CapabilityType.PROXY, proxy);
				driver = new FirefoxDriver(cap);
				break;
			
			case HtmlUnitDriver:
				notForExecutable(ParseBrowser.HtmlUnitDriver);
				driver = new HtmlUnitDriver();
				break;
		}
		
		setDriverOptions();
		seleniumJsController.executeAjaxCounterScript();
	}
	
	@PreDestroy
	private void destroy() {
		if (systemSettings.isCloseBrowserOnFinish() && driver != null) {
			log.debug("Destroying driver");
			driver.quit();
		}
	}
	
	void disableImplicitWait() {
		assertDriverNotNull();
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
	}
	
	void enableImplicitWait() {
		assertDriverNotNull();
		driver.manage().timeouts().implicitlyWait(systemSettings.getDriverImplicitWait(), TimeUnit.SECONDS);
	}
	
	private void setDriverOptions() {
		assertDriverNotNull();
		driver.manage().timeouts().implicitlyWait(systemSettings.getDriverImplicitWait(), TimeUnit.SECONDS);
	}
	
	Object executeJs(String script, Object... args) {
		if (driver instanceof JavascriptExecutor) {
			JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
			return jsExecutor.executeScript(script, args);
		}
		else {
			log.warn("Driver '{}' is not capable of executing JS code!", driver.getClass());
			return null;
		}
	}
	
	WebDriver get() {
		assertDriverNotNull();
		return driver;
	}
	
	private void notForExecutable(ParseBrowser browser) {
		if (systemSettings.isExecutable()) {
			throw new UnsupportedOperationException(browser.name() + " is not permitted in executable jar");
		}
	}
	
	private void assertDriverNotNull() {
		if (driver == null) {
			throw new IllegalStateException("web driver is null!");
		}
	}
	
	private String getProjectPath() {
		File tmp = new File(".");
		String path = tmp.getAbsolutePath();
		if (path.contains("Parser")) {
			return path.substring(0, path.length() - 8);
		}
		else {
			return path.substring(0, path.length() - 1);
		}
	}
}
