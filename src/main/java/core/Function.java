package core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Function {
	static WebDriver driver;
	static String browser;
	static Properties p = new Properties();
	static Writer report;
	
	static void open(String browser, String url) {
		Logger.getLogger("").setLevel(Level.OFF);
		String driverPath = "";
		
		if (browser.equalsIgnoreCase("firefox")) {
			if (System.getProperty("os.name").toUpperCase().contains("MAC"))
				driverPath = "./resources/webdrivers/mac/geckodriver.sh";
			else if (System.getProperty("os.name").toUpperCase().contains("WINDOWS"))
				 driverPath = "./resources/webdrivers/pc/geckodriver.exe";
			else throw new IllegalArgumentException("Unknown OS");
			
			System.setProperty("webdriver.gecko.driver", driverPath);
			driver = new FirefoxDriver();
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		}
		
		else if (browser.equalsIgnoreCase("chrome")) {
			if (System.getProperty("os.name").toUpperCase().contains("MAC"))
				driverPath = "./resources/webdrivers/mac/chromedriver";
			else if (System.getProperty("os.name").toUpperCase().contains("WINDOWS"))
				 driverPath = "./resources/webdrivers/pc/chromedriver.exe";
			else throw new IllegalArgumentException("Unknown OS");
			
			System.setProperty("webdriver.chrome.driver", driverPath);
			System.setProperty("webdriver.chrome.silentOutput", "true");
			ChromeOptions option = new ChromeOptions();
			option.addArguments("disable-infobars");
			option.addArguments("--disable-notifications");
			if (System.getProperty("os.name").toUpperCase().contains("MAC"))
				option.addArguments("-start-fullscreen");
            else if (System.getProperty("os.name").toUpperCase().contains("WINDOWS"))
            	option.addArguments("--start-maximized");
            else throw new IllegalArgumentException("Unknown OS");
			driver = new ChromeDriver(option);
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);	
		}
		else if (browser.equalsIgnoreCase("safari")) {
			if (!System.getProperty("os.name").toUpperCase().contains("MAC")) {
				throw new IllegalArgumentException("Safari is available only on Mac");
			}
			driver = new SafariDriver();
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		}
		else if (browser.equalsIgnoreCase("edge")) {
			if (!System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
				throw new IllegalArgumentException("MS Edge is available only on Windows");
			}
			System.setProperty("webdriver.edge.driver", "./resources/webdrivers/pc/MicrosoftWebDriver.exe");
			driver = new EdgeDriver();
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		}
		else {
			throw new WebDriverException("Unknown WebDriver");
		}
		driver.get(url);
	}
	
	static boolean isElementPresent(By by) {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		if (!driver.findElements(by).isEmpty())
			return true;
		else
			return false;
	}
	
	static String getSize(By by) {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		if (!driver.findElements(by).isEmpty() && driver.findElement(by).isDisplayed())
			return driver.findElement(by).getRect().getDimension().toString().replace(", ", "x");
			//return driver.findElement(by).getSize().toString().replace(", ", "x");
		else
			return "null";
	}
	
	static String getLocation(By by) {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//		if (((RemoteWebDriver) driver).getCapabilities().getBrowserName().equals("Safari")) 
//			return "(0x0)";
//		else {
			if (!driver.findElements(by).isEmpty() && driver.findElement(by).isDisplayed())
				return driver.findElement(by).getRect().getPoint().toString().replace(", ", "x");
			else
				return "null";
//		}
	}
	
	static void setValue(By by, String value) {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		if (!driver.findElements(by).isEmpty() && driver.findElement(by).isDisplayed()) {
			driver.findElement(by).sendKeys(value);
		}
		
	}
	
	static String getValue(By by) {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		if (!driver.findElements(by).isEmpty() && 
				driver.findElement(by).isDisplayed() && 
				driver.findElement(by).getTagName().equalsIgnoreCase("input"))
			return driver.findElement(by).getAttribute("value").toString().trim();
		else if (!driver.findElements(by).isEmpty() && 
				driver.findElement(by).isDisplayed() && 
				driver.findElement(by).getTagName().equalsIgnoreCase("span"))
			return driver.findElement(by).getText().trim();
		else
			return "null";
	}
	
	static void submit(By by) {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		if (!driver.findElements(by).isEmpty() && driver.findElement(by).isDisplayed()) {
			driver.findElement(by).submit();
		}
	}
	
	static String getOS() {
		return ((RemoteWebDriver) driver).getCapabilities().getPlatform().toString().trim();
	}
	
	static String getBrowser() {
		String browser = ((RemoteWebDriver) driver).getCapabilities().getBrowserName().toString().trim();
		return browser.replaceFirst(String.valueOf(browser.charAt(0)), String.valueOf(browser.charAt(0)).toUpperCase());
		//return browser.substring(0, 1).toUpperCase() + browser.substring(1).toLowerCase();
	}
	
	static String getFileName() {
		String file = driver.getCurrentUrl().toString().trim();
		return file.substring(file.lastIndexOf('/') + 1);
	}
	
	static void waitTitlePage(String title) {
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.titleIs(title));
	}
	
	static void writeReportHeader(Writer report) throws IOException {
		report.write("#," + getBrowser() + ",Page,Field,isPresent,Value,Size,Location" + "\n");
		System.out.print("#," + getBrowser() + ",Page,Field,isPresent,Value,Size,Location" + "\n");
	}
	
	static void writeReportLine(String index, String fieldName, By by, Writer report) throws IOException {
		report.write(
				index + "," +
				getBrowser() + "," +
				getFileName() + "," +
				fieldName + "," +
				isElementPresent(by) + "," +
				getValue(by) + "," +
				getSize(by) + "," +
				getLocation(by) + "\n");
		System.out.print(
				index + "," +
				getBrowser() + "," +
				getFileName() + "," +
				fieldName + "," +
				isElementPresent(by) + "," +
				getValue(by) + "," +
				getSize(by) + "," +
				getLocation(by) + "\n");
	}
	
	static void quit() {
		driver.quit();
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		browser = System.getProperty("browser"); // -Dbrowser="chrome"
		if (browser == null) {
			System.out.println("Please provide browser: -Dbrowser=chrome");
			System.exit(0);
		}
		else if (!browser.equalsIgnoreCase("chrome") && 
				!browser.equalsIgnoreCase("safari") && 
				!browser.equalsIgnoreCase("firefox") && 
				!browser.equalsIgnoreCase("edge")) {
			System.out.println("Framework supports only Chrome, Firefox, Safari or Edge");
			System.exit(0);
		}
		p.load(new FileInputStream("./input.properties"));
		//OPEN URL in a browser
		open(browser, p.getProperty("url"));
		
		//REPORT
		report = new FileWriter("./report_" + getBrowser().toString().toLowerCase() + "_function" + ".csv", false);
		writeReportHeader(report);
		
		//SignUp Page
		//01 :: First Name
		setValue(By.id(p.getProperty("fname_id")), p.getProperty("fname_value"));
		writeReportLine("01", "First Name", By.id(p.getProperty("fname_id")), report);
		//02 :: Last Name
		setValue(By.id(p.getProperty("lname_id")), p.getProperty("lname_value"));
		writeReportLine("02", "Last Name", By.id(p.getProperty("lname_id")), report);
		//03 :: Email
		setValue(By.id(p.getProperty("email_id")), p.getProperty("email_value"));
		writeReportLine("03", "Email", By.id(p.getProperty("email_id")), report);
		//04 :: Phone
		setValue(By.id(p.getProperty("phone_id")), p.getProperty("phone_value"));
		writeReportLine("04", "Phone", By.id(p.getProperty("phone_id")), report);
		//SUBMIT
		submit(By.id(p.getProperty("submit_id")));
		waitTitlePage("Confirmation");
		
		//Confirmation page
		//05 :: First Name
		writeReportLine("05", "First Name", By.id(p.getProperty("fname_id")), report);
		//06 :: Last Name
		writeReportLine("06", "Last Name", By.id(p.getProperty("lname_id")), report);
		//07 :: Email
		writeReportLine("07", "Email", By.id(p.getProperty("email_id")), report);
		//08 :: Phone
		writeReportLine("08", "Phone", By.id(p.getProperty("phone_id")), report);
		
		report.flush();
		report.close();
		
		//QUIT browser
		quit();	
	}

}
