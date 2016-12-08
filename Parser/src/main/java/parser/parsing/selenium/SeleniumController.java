package parser.parsing.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parser.parsing.configuration.SystemSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class SeleniumController {
	private static final Logger log = LoggerFactory.getLogger(SeleniumController.class);
	
	@Autowired
	private Driver driver;
	@Autowired
	private SystemSettings systemSettings;
	@Autowired
	private SeleniumJsController seleniumJsController;
	
	public void openSite(String url) {
		log.info("Opening site {}", url);
		driver.get().get(url);
		seleniumJsController.executeAjaxCounterScript();
		seleniumJsController.waitForAjaxToFinish(1500);
	}
	
	public String getSiteUrl() {
		return driver.get().getCurrentUrl();
	}
	
	public String getTitle(){
		return driver.get().getTitle();
	}

	public String getSource(){
		return driver.get().getPageSource();
	}

	public List<WebElement> getByClassName(final String className) {
		return executeSafely(() -> driver.get().findElements(By.className(className)));
	}
	
	public List<WebElement> getByClassName(final WebElement parent, final String className) {
		return executeSafely(() -> parent.findElements(By.className(className)));
	}
	
	public List<WebElement> getByText(final String name) {
		return executeSafely(() -> driver.get().findElements(By.xpath("//*[contains(text(), '"+name+"')]")));
	}
	
	public List<WebElement> getByText(final String name, WebElement element) {
		return executeSafely(() -> element.findElements(By.xpath("//*[contains(text(), '"+name+"')]")));
	}
	
	public List<WebElement> getById(final String id) {
		return executeSafely(() -> driver.get().findElements(By.id(id)));
	}
	
	public List<WebElement> getByPattern(final Pattern pattern, WebElement element, String keyWord) {
		List<WebElement> tmp = getByText(keyWord,element);
		List<WebElement> out = new ArrayList<>();
		for (WebElement e:tmp) {
			String elementText = e.getText();
			if (elementText.matches(pattern.toString()))
				out.add(e);
		}
		return out;
	}
	
	public List<WebElement> getParent(WebElement element) {
		return executeSafely(() -> element.findElements(By.xpath("./..")));
	}
	
	public List<WebElement> getChild(WebElement element){
		return executeSafely(() -> element.findElements(By.xpath(".//*")));
	}

	public String getElementText(final WebElement element) {
		return executeSafely(() -> element.getText());
	}
	
	public void click(final WebElement element) {
		executeSafely(() -> {
			seleniumJsController.waitForAjaxToFinish();
			element.click();
			seleniumJsController.waitForAjaxToFinish();
			return null;
		});
	}
	
	public void waitForElement(String elementClass) {
		long started = System.currentTimeMillis();
		
		while (timeoutNotReached(started)) {
			try {
				driver.disableImplicitWait();
				List<WebElement> elements = getByClassName(elementClass);
				if (elements != null && elements.size() > 0) {
					if (elements.get(0).isDisplayed()) {
						break;
					}
				}
			}
			catch (NoSuchElementException e) {
				// do nothing
			}
			finally {
				driver.enableImplicitWait();
			}
		}
	}
	
	public void waitForElementName(String elementText) {
		long started = System.currentTimeMillis();

		while (timeoutNotReached(started)) {
			try {
				driver.disableImplicitWait();
				List<WebElement> elements = getByText(elementText);
				if (elements != null && elements.size() > 0) {
					if (elements.get(0).isDisplayed()) {
						break;
					}
				}
			}
			catch (NoSuchElementException e) {
				// do nothing
			}
			finally {
				driver.enableImplicitWait();
			}
		}
	}
	
	public boolean doesElementExistsByText(String text) {
		driver.disableImplicitWait();
		
		try {
			WebElement element = driver.get().findElement(By.xpath("//*[contains(text(), '" + text + "')]"));
			return element.isDisplayed();
		}
		catch (NoSuchElementException e) {
			return false;
		}
		finally {
			driver.enableImplicitWait();
		}
	}
	
	private <T> T executeSafely(SecuredOperation<T> securedOperation) {
		int tryNumber = 0;
		while (true) {
			try {
				return securedOperation.execute();
			}
			catch (StaleElementReferenceException e) {
				if (tryNumber < systemSettings.getDriverStaleElRefExcMaxTryNo()) {
					log.debug("StaleElementReferenceException while fetching element, trying again");
					tryNumber++;
				}
				else {
					throw e;
				}
			}
		}
	}
	
	private interface SecuredOperation<T> {
		T execute();
	}
	
	private boolean timeoutNotReached(long started) {
		return System.currentTimeMillis() - started <= systemSettings.getDriverImplicitWait()*1000;
	}
	
}
