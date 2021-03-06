package main.java.com.dbyl.appiumCore.tests;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import main.java.com.dbyl.appiumServer.AppiumServerUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import java.util.concurrent.TimeUnit;

public class iOSWebBrowser {
	private IOSDriver<?> driver;
	private URL url;

	@BeforeClass
	public void beforeClasss() throws Exception {
		url = AppiumServerUtils.getInstance().startServer("127.0.0.1", 4723);
	}

	@BeforeMethod(alwaysRun = true)
	public void setUp() throws Exception {
		// set up appium

		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "safari");
		capabilities.setCapability("platformName", "iOS");
		capabilities.setCapability("platformVersion", "11.0");
		capabilities.setCapability("deviceName", "iPhone 6s");
		// capabilities.setCapability(MobileCapabilityType.UDID,
		// "b90269dd9954f6a9edd5c8499cf9d364572ccc72");
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITEST");
		// support Chinese
		capabilities.setCapability("unicodeKeyboard", "True");
		capabilities.setCapability("resetKeyboard", "True");
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS"); // newly
																				// added
		capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 10000);
		driver = new IOSDriver(url, capabilities);
	}

	@Test(groups = { "webView" })
	public void runWebBrowser() {
		driver.get("http://www.baidu.com");
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		System.out.println(driver.getTitle());
		snapshot((TakesScreenshot) driver, "before_search.png");
		driver.findElementByXPath("//textarea[@id='index-kw']").sendKeys("appium");
		driver.findElement(By.xpath("//button[@id='index-bn']")).click();
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		System.out.println(driver.getTitle());
		snapshot((TakesScreenshot) driver, "after_search.png");
		Assert.assertTrue(driver.getTitle().contains("appium"));

	}

	/**
	 * This Method create for take screenshot
	 * 
	 * @author Young
	 * @param drivername
	 * @param filename
	 */
	public static void snapshot(TakesScreenshot drivername, String filename) {
		// this method will take screen shot ,require two parameters ,one is
		// driver name, another is file name

		String currentPath = System.getProperty("user.dir"); // get current work
																// folder
		File scrFile = drivername.getScreenshotAs(OutputType.FILE);
		// Now you can do whatever you need to do with it, for example copy
		// somewhere
		try {
			System.out.println("save snapshot path is:" + currentPath + "/" + filename);
			FileUtils.copyFile(scrFile, new File(currentPath + "\\" + filename));
		} catch (IOException e) {
			System.out.println("Can't save screenshot");
			e.printStackTrace();
		} finally {
			System.out.println("screen shot finished, it's in " + currentPath + " folder");
		}
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {
		driver.quit();
		AppiumServerUtils.getInstance().stopServer();
	}

}