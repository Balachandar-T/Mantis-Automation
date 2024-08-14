package first;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Primary {

	static WebDriver driver;
	public static String excelloc    = ".\\Data\\Mantis_Data.xls";
	public static String xpathloc    = ".\\Data\\Mantis_xpath.xls";
	public static String logloc      = ".\\log4j.properties";
	public static Logger log         = Logger.getLogger(Primary.class);
	public static String parentMT    = "";
	static ArrayList<String> URLs    = new ArrayList<String>();
	public static String cardnumaber = "";
	public static String project     = "";

	/*
	 * This method is used to Call main functions
	 * @param args
	 * @return void
	 * @author tbalachandar
	 * @version 1.0
	 * @throws Exception
	 * @DateChanged 2024-04-12
	 */

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		SimpleDateFormat dateFormate = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
		System.setProperty("current.date.time", dateFormate.format(new Date()));

		PropertyConfigurator.configure(logloc);

		//driver = new ChromeDriver();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("window-size=1400,800");
		options.addArguments("headless");

		/*
		 * options.addArguments("headless");
		 * options.addArguments("--no-default-browser-check");
		 * options.addArguments("--silent"); options.addArguments("--start-maximized");
		 * options.addArguments("--disable-dev-shm-usage"); // overcome limited resource
		 * problems options.addArguments("--no-sandbox"); // Bypass OS security model
		 */

		driver = new ChromeDriver(options);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		String URL = dataReader.getData(excelloc, "URL", "Login", 1);
		String UN  = dataReader.getData(excelloc, "UN",  "Login", 1);
		String PWD = dataReader.getData(excelloc, "PWD", "Login", 1);
		login(URL, UN, PWD);
	}

	public static void login(String url, String Uname, String Pwd) throws Exception {

		String username_xpath   = dataReader.getData(xpathloc, "uname_txt",     "Xpath", 1);
		String unamelogin_xpath = dataReader.getData(xpathloc, "ulogin_btn",    "Xpath", 1);
		String password_xpath   = dataReader.getData(xpathloc, "pwd_txt",       "Xpath", 1);
		String pwdlogin_xpath   = dataReader.getData(xpathloc, "pwd_login_btn", "Xpath", 1);
		String send_mail        = dataReader.getData(excelloc, "Send Mail", "Email", 1);

		driver.get(url);
		log.info("URL Is Opned Successfully");

		driver.findElement(By.xpath(username_xpath)).sendKeys(Uname);
		log.info("Username Has Been Entered");

		driver.findElement(By.xpath(unamelogin_xpath)).click();
		log.info("Username Page Login Button Is Clicked");

		driver.findElement(By.xpath(password_xpath)).sendKeys(Pwd);
		log.info("Password Has Been Entered Successfully");

		driver.findElement(By.xpath(pwdlogin_xpath)).click();
		log.info("Password Page Login Button Is Clicked");

		for (int i = 0; i < dataReader.getColumnCount(excelloc, "Details") - 1; i++) {

			String Type      = dataReader.getData(excelloc, "Project Type",    "Details", i + 1);
			project          = Type;
			String Gory      = dataReader.getData(excelloc, "Category",        "Details", i + 1);
			String Reproduce = dataReader.getData(excelloc, "Reproducibility", "Details", i + 1);
			String Severity  = dataReader.getData(excelloc, "Severity",        "Details", i + 1);
			String Priority  = dataReader.getData(excelloc, "Priority",        "Details", i + 1);
			String Assign    = dataReader.getData(excelloc, "Assign To",       "Details", i + 1);
			report(Type, Gory, Reproduce, Severity, Priority, Assign, i + 1);
		}

		if (send_mail.equalsIgnoreCase("Yes")) {
			String subject = "";
			if (cardnumaber.isEmpty()) {
				subject = project;
			} else {
				subject = project + " [" + cardnumaber + "]"; 
			}
			Mail.sendMail(excelloc, URLs, subject);
			log.info("Mail Sent Successfully......... ");
		}
		log.info("The Function Has Been End");
	}

	public static void report(String Project, String Category, String Reproducibility, String Severity, String Priority,
			String Assign, int indexNo) throws Exception {

		String project_xpath = dataReader.getData(xpathloc, "project_btn",      "Xpath", 1);
		String option_xpath  = dataReader.getData(xpathloc, "project_type_drp", "Xpath", 1);

		driver.findElement(By.xpath(project_xpath)).click();
		log.info("Project Type Field Is Clicked");

		List<WebElement> ptype = driver.findElements(By.xpath(option_xpath));
		for (WebElement First : ptype) {
			if (First.getText().equalsIgnoreCase(Project)) {
				log.info("Project Type Has Been Selected " + Project);
				First.click();
				break;
			}
		}
		String report_issue_xpath    = dataReader.getData(xpathloc, "issue_btn",         "Xpath", 1);
		String category_type_xpath   = dataReader.getData(xpathloc, "category_btn",      "Xpath", 1);
		String category_option_xpath = dataReader.getData(xpathloc, "category_type_drp", "Xpath", 1);

		/*
		 * WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		 * Boolean until =
		 * wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(
		 * report_issue))); until.booleanValue();
		 */

		driver.findElement(By.xpath(report_issue_xpath)).click();
		// Actions act = new Actions(driver);
		// act.scrollToElement(findElement).click();
		log.info("Report Issue Button Is Clicked");
		Thread.sleep(1000);
		driver.findElement(By.xpath(category_type_xpath)).click();
		log.info("Category Field Is Clicked");

		List<WebElement> category = driver.findElements(By.xpath(category_option_xpath));
		for (WebElement Second : category) {
			if (Second.getText().equalsIgnoreCase(Category)) {
				Second.click();
				log.info("The Category Type Has Been Selected " + Category);
				break;
			}
		}
		String reproduce_xpath = dataReader.getData(xpathloc, "reproduce_btn",             "Xpath", 1);
		String reproduce_Option_xpath = dataReader.getData(xpathloc, "reproduce_type_drp", "Xpath", 1);
		driver.findElement(By.xpath(reproduce_xpath)).click();
		log.info("Reproducibility Field Is Clicked");
		List<WebElement> reProduce = driver.findElements(By.xpath(reproduce_Option_xpath));
		for (WebElement Third : reProduce) {
			if (Third.getText().equalsIgnoreCase(Reproducibility)) {
				Third.click();
				log.info("The Reproducibility Type Has Been Selected " + Reproducibility);
				break;
			}
		}
		String severity_type_xpath   = dataReader.getData(xpathloc, "severity_btn",      "Xpath", 1);
		String severity_option_xpath = dataReader.getData(xpathloc, "severity_type_drp", "Xpath", 1);
		driver.findElement(By.xpath(severity_type_xpath)).click();
		log.info("Severity Field Is Clicked");
		List<WebElement> severity    = driver.findElements(By.xpath(severity_option_xpath));
		for (WebElement Four : severity) {
			if (Four.getText().equalsIgnoreCase(Severity)) {
				Four.click();
				log.info("The Severity Type Has Been Selected " + Severity);
				break;
			}
		}
		String priority_type_xpath   = dataReader.getData(xpathloc, "priority_btn",      "Xpath", 1);
		String priority_option_xpath = dataReader.getData(xpathloc, "priority_type_drp", "Xpath", 1);
		driver.findElement(By.xpath(priority_type_xpath)).click();
		log.info("Priority Field Is Clicked");
		List<WebElement> priority = driver.findElements(By.xpath(priority_option_xpath));
		for (WebElement Five : priority) {
			if (Five.getText().equalsIgnoreCase(Priority)) {
				Five.click();
				log.info("The Priority Type Has Been Selected " + Priority);
				break;
			}
		}
		String assign_type_xpath   = dataReader.getData(xpathloc, "assign_btn",      "Xpath", 1);
		String assign_option_xpath = dataReader.getData(xpathloc, "assign_type_drp", "Xpath", 1);
		driver.findElement(By.xpath(assign_type_xpath)).click();
		log.info("Assign To Field Is Clicked");
		List<WebElement> assign = driver.findElements(By.xpath(assign_option_xpath));
		for (WebElement Six : assign) {
			if (Six.getText().equalsIgnoreCase(Assign)) {
				Six.click();
				log.info("The Assign To Has Been Selected " + Assign);
				break;
			}
		}
		String Summary     = dataReader.getData(excelloc, "Summary",            "Details", indexNo);
		String Description = dataReader.getData(excelloc, "Description",        "Details", indexNo);
		String Reproduce   = dataReader.getData(excelloc, "Steps To Reproduce", "Details", indexNo);
		String EstHrs      = dataReader.getData(excelloc, "EstHrs",             "Details", indexNo);
		details(Summary, Description, Reproduce, EstHrs, indexNo);
	}

	public static void details(String Summary, String Description, String Reproduce, String EstHrs, int indexNo)
			throws Exception {

		String summary_field_xpath     = dataReader.getData(xpathloc, "summary_txt",         "Xpath", 1);
		String desctiption_field_xpath = dataReader.getData(xpathloc, "description_txt",     "Xpath", 1);
		String reproduce_field_xpath   = dataReader.getData(xpathloc, "steps_reproduce_txt", "Xpath", 1);
		String est_field_xpath         = dataReader.getData(xpathloc, "est_txt",             "Xpath", 1);

		driver.findElement(By.xpath(summary_field_xpath)).sendKeys(Summary);
		log.info("The Summary Has Been Entered");

		driver.findElement(By.xpath(desctiption_field_xpath)).sendKeys(Description);
		log.info("The Description Has Been Entered");

		driver.findElement(By.xpath(reproduce_field_xpath)).sendKeys(Reproduce);
		log.info("Steps To Reproduce Has Been Entered");

		driver.findElement(By.xpath(est_field_xpath)).sendKeys(EstHrs);
		log.info("The Estimation Has Been Entered");

		String ca_number = dataReader.getData(excelloc, "Card Number",    "Details", indexNo);
		cardnumaber      = ca_number;
		String de_number = dataReader.getData(excelloc, "Defect Number",  "Details", indexNo);
		String category  = dataReader.getData(excelloc, "Issue-Category", "Details", indexNo);
		String lob       = dataReader.getData(excelloc, "LOB",            "Details", indexNo);
		String sprint    = dataReader.getData(excelloc, "Sprint",         "Details", indexNo);
		String type      = dataReader.getData(excelloc, "Type",           "Details", indexNo);
		String project   = dataReader.getData(excelloc, "Project", "Login", 1);

		if (project.equalsIgnoreCase("WRG")) {

			wrg(ca_number, de_number, category, lob, sprint, type, indexNo);

		} else {
			upload(indexNo);
		}
	}

	public static void wrg(String Cardnumber, String Defectnumber, String Issue, String Business, String spt,
			String Option, int indexNo) throws Exception {

		String card_xpath    = dataReader.getData(xpathloc, "card_txt",     "Xpath", 1);
		String defect_xpath  = dataReader.getData(xpathloc, "defect_txt",   "Xpath", 1);
		String is_cat_xpath  = dataReader.getData(xpathloc, "issuecat_drp", "Xpath", 1);
		String line_xpath    = dataReader.getData(xpathloc, "lob_drp",      "Xpath", 1);
		String sprint_xpath  = dataReader.getData(xpathloc, "sprint_txt",   "Xpath", 1);
		String ca_type_xpath = dataReader.getData(xpathloc, "type_drp",     "Xpath", 1);

		driver.findElement(By.xpath(card_xpath)).sendKeys(Cardnumber);
		log.info("Card Number Has Been Entered");

		driver.findElement(By.xpath(defect_xpath)).sendKeys(Defectnumber);
		log.info("Defect Number Has Been Entered");
		List<WebElement> Category = driver.findElements(By.xpath(is_cat_xpath));
		for (WebElement Seven : Category) {
			if (Seven.getText().equalsIgnoreCase(Issue)) {
				Seven.click();
				log.info("Issue Category Has Been Selected");
				break;
			}
		}
		List<WebElement> LOB = driver.findElements(By.xpath(line_xpath));
		for (WebElement Eight : LOB) {
			if (Eight.getText().equalsIgnoreCase(Business)) {
				Eight.click();
				log.info("LOB Has Been Selected");
				break;
			}
		}

		driver.findElement(By.xpath(sprint_xpath)).sendKeys(spt);
		log.info("The Sprint Has Been Entered");
		List<WebElement> Card_Type = driver.findElements(By.xpath(ca_type_xpath));
		for (WebElement Nine : Card_Type) {
			if (Nine.getAccessibleName().equalsIgnoreCase(Option)) {
				Nine.click();
				log.info("Card Type Has Been Selected");
				break;
			}
		}

		upload(indexNo);
	}

	public static void upload(int indexNo) throws Exception {
		String upload_field_xpath = dataReader.getData(xpathloc, "upload_btn", "Xpath", 1);
		String submit_xpath       = dataReader.getData(xpathloc, "submit_btn", "Xpath", 1);
		String id_xpath           = dataReader.getData(xpathloc, "mid_lbe",    "Xpath", 1);
		String document           = dataReader.getData(excelloc, "File Upload", "Details", indexNo);

		if (!document.isEmpty()) {
			driver.findElement(By.xpath(upload_field_xpath)).click();
			Thread.sleep(3000);
			StringSelection selection = new StringSelection(document);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
			Robot robo = new Robot();
			robo.keyPress(KeyEvent.VK_CONTROL);
			robo.keyPress(KeyEvent.VK_V);
			robo.keyRelease(KeyEvent.VK_V);
			robo.keyRelease(KeyEvent.VK_CONTROL);
			robo.keyPress(KeyEvent.VK_ENTER);
			robo.keyRelease(KeyEvent.VK_ENTER);

			Thread.sleep(1000);
			log.info("The Document Is Uploaded Successfully");
			driver.findElement(By.xpath(submit_xpath)).click();

		} else {
			log.info("The Document Is Empty");
			driver.findElement(By.xpath(submit_xpath)).click();
		}

		Thread.sleep(1000);

		String text       = driver.findElement(By.xpath(id_xpath)).getText();
		String currentUrl = driver.getCurrentUrl();
		String URL        = "<a href = \"" + currentUrl + "\">" + text + "</a>";
		URLs.add(URL);
		log.info("The Mantis Id Is " + text);

		String relation   = dataReader.getData(excelloc, "Relationships", "Details", indexNo);

		if (relation.equalsIgnoreCase("Child")) {

			String parent = dataReader.getData(excelloc, "Parent of", "Details", indexNo);
			if (!parent.isEmpty()) {
				tagMantis(indexNo, parent);
			} else {
				tagMantis(indexNo, parentMT);
			}

		} else {
			parentMT = text;
			log.info("Parent Mantis Is Stored");
		}
	}

	public static void tagMantis(int indexNo, String parent) throws IOException, Exception {

		String relationship = dataReader.getData(xpathloc, "relations_lbe", "Xpath", 1);
		String add          = dataReader.getData(xpathloc, "add_btn",       "Xpath", 1);
		Thread.sleep(3000);

		if (!parent.isEmpty()) {

			driver.findElement(By.xpath(relationship)).sendKeys(parent);
			driver.findElement(By.xpath(add)).click();
			log.info("Parent Mantis Is Added Successfully.....");
		} else {

			log.info("Parent Mantis Is Not Added.....");
		}

	}

}
