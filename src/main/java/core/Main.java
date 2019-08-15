package core;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
	static String browser;

	public static void main(String[] args) throws FileNotFoundException, IOException {
		// browser = "chrome";
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
		SignUp.open(browser);
		SignUp.validate();
		Confirmation.validate();
		Common.quit();

	}

}
