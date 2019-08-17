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
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Linear {
	static Properties p = new Properties();
	static String browser;
	static WebDriver driver;
	static Writer report;

	public static void main(String[] args) throws FileNotFoundException, IOException {
		p.load(new FileInputStream("./input.properties"));
		browser = System.getProperty("browser");
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
		//Open browser
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
		//Open URL
		driver.get(p.getProperty("url"));
		//Create report
		report = new FileWriter("./report_" + browser.toLowerCase() + "_linear" + ".csv", false);
		//Getting browser name 
		browser = browser.replaceFirst(String.valueOf(browser.charAt(0)), String.valueOf(browser.charAt(0)).toUpperCase());
		//Getting file name
		String file = driver.getCurrentUrl().toString().trim();
		file = file.substring(file.lastIndexOf('/') + 1);
		//isElementPresent
		boolean isElementPresent = false;
		String value = "";
		String size = "";
		String location;
		
		//Header
		report.write("#," + browser + ",Page,Field,isPresent,Value,Size,Location" + "\n");
		System.out.print("#," + browser + ",Page,Field,isPresent,Value,Size,Location" + "\n");
		
		//01 :: First Name
		//Setting value
		if (!driver.findElements(By.id(p.getProperty("fname_id"))).isEmpty() && driver.findElement(By.id(p.getProperty("fname_id"))).isDisplayed()) {
			driver.findElement(By.id(p.getProperty("fname_id"))).sendKeys(p.getProperty("fname_value"));
		}
		
		//Checking if element present
		if (!driver.findElements(By.id(p.getProperty("fname_id"))).isEmpty())
			isElementPresent = true;
		else
			isElementPresent = false;
		//Getting the value
		if (!driver.findElements(By.id(p.getProperty("fname_id"))).isEmpty() && 
				driver.findElement(By.id(p.getProperty("fname_id"))).isDisplayed() && 
				driver.findElement(By.id(p.getProperty("fname_id"))).getTagName().equalsIgnoreCase("input"))
			value = driver.findElement(By.id(p.getProperty("fname_id"))).getAttribute("value").toString().trim();
		else
			value = "null";
		//Getting size
		if (!driver.findElements(By.id(p.getProperty("fname_id"))).isEmpty() && driver.findElement(By.id(p.getProperty("fname_id"))).isDisplayed())
			size = driver.findElement(By.id(p.getProperty("fname_id"))).getRect().getDimension().toString().replace(", ", "x");
		else
			size = "null";
		//Getting location 
		if (!driver.findElements(By.id(p.getProperty("fname_id"))).isEmpty() && driver.findElement(By.id(p.getProperty("fname_id"))).isDisplayed())
			location = driver.findElement(By.id(p.getProperty("fname_id"))).getRect().getPoint().toString().replace(", ", "x");
		else
			location = "null";
		//Printing report line
		report.write("01" + "," + browser + "," + file + "," + "First Name" + "," + 
				isElementPresent + "," + value + "," + size + "," + location + "\n");
		System.out.print("01" + "," + browser + "," + file + "," + "First Name" + "," +
				isElementPresent + "," + value + "," + size + "," + location + "\n");
		
		//02 :: Last Name
		//Setting value
		if (!driver.findElements(By.id(p.getProperty("lname_id"))).isEmpty() && driver.findElement(By.id(p.getProperty("lname_id"))).isDisplayed()) {
			driver.findElement(By.id(p.getProperty("lname_id"))).sendKeys(p.getProperty("lname_value"));
		}
		
		//Checking if element present
		if (!driver.findElements(By.id(p.getProperty("lname_id"))).isEmpty())
			isElementPresent = true;
		else
			isElementPresent = false;
		//Getting the value
		if (!driver.findElements(By.id(p.getProperty("lname_id"))).isEmpty() && 
				driver.findElement(By.id(p.getProperty("lname_id"))).isDisplayed() && 
				driver.findElement(By.id(p.getProperty("lname_id"))).getTagName().equalsIgnoreCase("input"))
			value = driver.findElement(By.id(p.getProperty("lname_id"))).getAttribute("value").toString().trim();
		else
			value = "null";
		//Getting size
		if (!driver.findElements(By.id(p.getProperty("lname_id"))).isEmpty() && driver.findElement(By.id(p.getProperty("lname_id"))).isDisplayed())
			size = driver.findElement(By.id(p.getProperty("lname_id"))).getRect().getDimension().toString().replace(", ", "x");
		else
			size = "null";
		//Getting location 
		if (!driver.findElements(By.id(p.getProperty("lname_id"))).isEmpty() && driver.findElement(By.id(p.getProperty("lname_id"))).isDisplayed())
			location = driver.findElement(By.id(p.getProperty("lname_id"))).getRect().getPoint().toString().replace(", ", "x");
		else
			location = "null";
		//Printing report line
		report.write("02" + "," + browser + "," + file + "," + "Last Name" + "," + 
				isElementPresent + "," + value + "," + size + "," + location + "\n");
		System.out.print("02" + "," + browser + "," + file + "," + "Last Name" + "," +
				isElementPresent + "," + value + "," + size + "," + location + "\n");
		
		//03 :: Email
		//Setting value
		if (!driver.findElements(By.id(p.getProperty("email_id"))).isEmpty() && driver.findElement(By.id(p.getProperty("email_id"))).isDisplayed()) {
			driver.findElement(By.id(p.getProperty("email_id"))).sendKeys(p.getProperty("email_value"));
		}
		
		//Checking if element present
		if (!driver.findElements(By.id(p.getProperty("email_id"))).isEmpty())
			isElementPresent = true;
		else
			isElementPresent = false;
		//Getting the value
		if (!driver.findElements(By.id(p.getProperty("email_id"))).isEmpty() && 
				driver.findElement(By.id(p.getProperty("email_id"))).isDisplayed() && 
				driver.findElement(By.id(p.getProperty("email_id"))).getTagName().equalsIgnoreCase("input"))
			value = driver.findElement(By.id(p.getProperty("email_id"))).getAttribute("value").toString().trim();
		else
			value = "null";
		//Getting size
		if (!driver.findElements(By.id(p.getProperty("email_id"))).isEmpty() && driver.findElement(By.id(p.getProperty("email_id"))).isDisplayed())
			size = driver.findElement(By.id(p.getProperty("email_id"))).getRect().getDimension().toString().replace(", ", "x");
		else
			size = "null";
		//Getting location 
		if (!driver.findElements(By.id(p.getProperty("email_id"))).isEmpty() && driver.findElement(By.id(p.getProperty("email_id"))).isDisplayed())
			location = driver.findElement(By.id(p.getProperty("email_id"))).getRect().getPoint().toString().replace(", ", "x");
		else
			location = "null";
		//Printing report line
		report.write("03" + "," + browser + "," + file + "," + "Email" + "," + 
				isElementPresent + "," + value + "," + size + "," + location + "\n");
		System.out.print("03" + "," + browser + "," + file + "," + "Email" + "," +
				isElementPresent + "," + value + "," + size + "," + location + "\n");
		
		//04 :: Phone
		//Setting value
		if (!driver.findElements(By.id(p.getProperty("phone_id"))).isEmpty() && driver.findElement(By.id(p.getProperty("phone_id"))).isDisplayed()) {
			driver.findElement(By.id(p.getProperty("phone_id"))).sendKeys(p.getProperty("phone_value"));
		}
		
		//Checking if element present
		if (!driver.findElements(By.id(p.getProperty("phone_id"))).isEmpty())
			isElementPresent = true;
		else
			isElementPresent = false;
		//Getting the value
		if (!driver.findElements(By.id(p.getProperty("phone_id"))).isEmpty() && 
				driver.findElement(By.id(p.getProperty("phone_id"))).isDisplayed() && 
				driver.findElement(By.id(p.getProperty("phone_id"))).getTagName().equalsIgnoreCase("input"))
			value = driver.findElement(By.id(p.getProperty("phone_id"))).getAttribute("value").toString().trim();
		else
			value = "null";
		//Getting size
		if (!driver.findElements(By.id(p.getProperty("phone_id"))).isEmpty() && driver.findElement(By.id(p.getProperty("phone_id"))).isDisplayed())
			size = driver.findElement(By.id(p.getProperty("phone_id"))).getRect().getDimension().toString().replace(", ", "x");
		else
			size = "null";
		//Getting location 
		if (!driver.findElements(By.id(p.getProperty("phone_id"))).isEmpty() && driver.findElement(By.id(p.getProperty("phone_id"))).isDisplayed())
			location = driver.findElement(By.id(p.getProperty("phone_id"))).getRect().getPoint().toString().replace(", ", "x");
		else
			location = "null";
		//Printing report line
		report.write("04" + "," + browser + "," + file + "," + "Phone" + "," + 
				isElementPresent + "," + value + "," + size + "," + location + "\n");
		System.out.print("04" + "," + browser + "," + file + "," + "Phone" + "," +
				isElementPresent + "," + value + "," + size + "," + location + "\n");
		
		//SUBMIT
		if (!driver.findElements(By.id(p.getProperty("submit_id"))).isEmpty() && driver.findElement(By.id(p.getProperty("submit_id"))).isDisplayed()) {
			driver.findElement(By.id(p.getProperty("submit_id"))).submit();
		}
		
		//Wait until the page is loaded
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.titleIs("Confirmation"));
		
		//05 :: First Name
		//Checking if element present
		if (!driver.findElements(By.id(p.getProperty("phone_id"))).isEmpty())
			isElementPresent = true;
		else
			isElementPresent = false;
		//Getting the value
		if (!driver.findElements(By.id(p.getProperty("fname_id"))).isEmpty() && 
				driver.findElement(By.id(p.getProperty("fname_id"))).isDisplayed() && 
				driver.findElement(By.id(p.getProperty("fname_id"))).getTagName().equalsIgnoreCase("span"))
			value = driver.findElement(By.id(p.getProperty("fname_id"))).getText().trim();
		else
			value = "null";
		//Getting size
		if (!driver.findElements(By.id(p.getProperty("fname_id"))).isEmpty() && driver.findElement(By.id(p.getProperty("fname_id"))).isDisplayed())
			size = driver.findElement(By.id(p.getProperty("fname_id"))).getRect().getDimension().toString().replace(", ", "x");
		else
			size = "null";
		//Getting location 
		if (!driver.findElements(By.id(p.getProperty("fname_id"))).isEmpty() && driver.findElement(By.id(p.getProperty("fname_id"))).isDisplayed())
			location = driver.findElement(By.id(p.getProperty("fname_id"))).getRect().getPoint().toString().replace(", ", "x");
		else
			location = "null";
		//Printing report line
		report.write("05" + "," + browser + "," + file + "," + "First Name" + "," + 
				isElementPresent + "," + value + "," + size + "," + location + "\n");
		System.out.print("05" + "," + browser + "," + file + "," + "First Name" + "," +
				isElementPresent + "," + value + "," + size + "," + location + "\n");
		
		//06 :: Last Name
		//Checking if element present
		if (!driver.findElements(By.id(p.getProperty("lname_id"))).isEmpty())
			isElementPresent = true;
		else
			isElementPresent = false;
		//Getting the value
		if (!driver.findElements(By.id(p.getProperty("lname_id"))).isEmpty() && 
				driver.findElement(By.id(p.getProperty("lname_id"))).isDisplayed() && 
				driver.findElement(By.id(p.getProperty("lname_id"))).getTagName().equalsIgnoreCase("span"))
			value = driver.findElement(By.id(p.getProperty("lname_id"))).getText().trim();
		else
			value = "null";
		//Getting size
		if (!driver.findElements(By.id(p.getProperty("lname_id"))).isEmpty() && driver.findElement(By.id(p.getProperty("lname_id"))).isDisplayed())
			size = driver.findElement(By.id(p.getProperty("lname_id"))).getRect().getDimension().toString().replace(", ", "x");
		else
			size = "null";
		//Getting location 
		if (!driver.findElements(By.id(p.getProperty("lname_id"))).isEmpty() && driver.findElement(By.id(p.getProperty("lname_id"))).isDisplayed())
			location = driver.findElement(By.id(p.getProperty("lname_id"))).getRect().getPoint().toString().replace(", ", "x");
		else
			location = "null";
		//Printing report line
		report.write("06" + "," + browser + "," + file + "," + "Last Name" + "," + 
				isElementPresent + "," + value + "," + size + "," + location + "\n");
		System.out.print("06" + "," + browser + "," + file + "," + "Last Name" + "," +
				isElementPresent + "," + value + "," + size + "," + location + "\n");
		
		//07 :: Email
		//Checking if element present
		if (!driver.findElements(By.id(p.getProperty("email_id"))).isEmpty())
			isElementPresent = true;
		else
			isElementPresent = false;
		//Getting the value
		if (!driver.findElements(By.id(p.getProperty("email_id"))).isEmpty() && 
				driver.findElement(By.id(p.getProperty("email_id"))).isDisplayed() && 
				driver.findElement(By.id(p.getProperty("email_id"))).getTagName().equalsIgnoreCase("span"))
			value = driver.findElement(By.id(p.getProperty("email_id"))).getText().trim();
		else
			value = "null";
		//Getting size
		if (!driver.findElements(By.id(p.getProperty("email_id"))).isEmpty() && driver.findElement(By.id(p.getProperty("email_id"))).isDisplayed())
			size = driver.findElement(By.id(p.getProperty("email_id"))).getRect().getDimension().toString().replace(", ", "x");
		else
			size = "null";
		//Getting location 
		if (!driver.findElements(By.id(p.getProperty("email_id"))).isEmpty() && driver.findElement(By.id(p.getProperty("email_id"))).isDisplayed())
			location = driver.findElement(By.id(p.getProperty("email_id"))).getRect().getPoint().toString().replace(", ", "x");
		else
			location = "null";
		//Printing report line
		report.write("07" + "," + browser + "," + file + "," + "Email" + "," + 
				isElementPresent + "," + value + "," + size + "," + location + "\n");
		System.out.print("07" + "," + browser + "," + file + "," + "Email" + "," +
				isElementPresent + "," + value + "," + size + "," + location + "\n");
		
		//08 :: Phone
		//Checking if element present
		if (!driver.findElements(By.id(p.getProperty("phone_id"))).isEmpty())
			isElementPresent = true;
		else
			isElementPresent = false;
		//Getting the value
		if (!driver.findElements(By.id(p.getProperty("phone_id"))).isEmpty() && 
				driver.findElement(By.id(p.getProperty("phone_id"))).isDisplayed() && 
				driver.findElement(By.id(p.getProperty("phone_id"))).getTagName().equalsIgnoreCase("span"))
			value = driver.findElement(By.id(p.getProperty("phone_id"))).getText().trim();
		else
			value = "null";
		//Getting size
		if (!driver.findElements(By.id(p.getProperty("phone_id"))).isEmpty() && driver.findElement(By.id(p.getProperty("phone_id"))).isDisplayed())
			size = driver.findElement(By.id(p.getProperty("phone_id"))).getRect().getDimension().toString().replace(", ", "x");
		else
			size = "null";
		//Getting location 
		if (!driver.findElements(By.id(p.getProperty("phone_id"))).isEmpty() && driver.findElement(By.id(p.getProperty("phone_id"))).isDisplayed())
			location = driver.findElement(By.id(p.getProperty("phone_id"))).getRect().getPoint().toString().replace(", ", "x");
		else
			location = "null";
		//Printing report line
		report.write("08" + "," + browser + "," + file + "," + "Phone" + "," + 
				isElementPresent + "," + value + "," + size + "," + location +"\n");
		System.out.print("08" + "," + browser + "," + file + "," + "Phone" + "," +
				isElementPresent + "," + value + "," + size + "," + location + "\n");
		
		report.flush();
		report.close();
		driver.quit();
	}

}
