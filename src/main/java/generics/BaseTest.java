package generics;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import io.github.bonigarcia.wdm.managers.ChromeDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class BaseTest {

    static WebDriver driver=null;
    private static ExtentReports report=null;
     static ExtentTest reporter=null;
     String filePath="src/main/resources/Automationconstants.properties";
     int ITO=30;

    @BeforeSuite
    public void configReport(){
        report = new ExtentReports("Reports/myreports.html",true);
        report.addSystemInfo("Browser", "Chrome")
                .addSystemInfo("URL", Utility.getPropertyValue(filePath,"url"))
                .addSystemInfo("User Name", System.getProperty("user.name"));
       // report.loadConfig(new File(System.getProperty("user.dir") + Utility.getPropertyValue(AutomationConstants.AUTOMATION_CONSTANTS,"reportsConfig")));
    }

    @BeforeTest
    public void launchBrowser(){
        ChromeDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(ITO, TimeUnit.SECONDS);
        driver.get(Utility.getPropertyValue(filePath,"url"));
    }

    @BeforeMethod
    public void startTest(Method method){
        String className = method.getName();
        reporter = report.startTest(className);
    }

    @AfterMethod
    public void endTest(Method method, ITestResult result){
        String className = method.getName();
        if (result.getStatus() == ITestResult.SUCCESS) {
            reporter.log(LogStatus.PASS, className + " : Has Executed Successfully");
        } else if (result.getStatus() == ITestResult.FAILURE) {
            reporter.log(LogStatus.FAIL, className + " : Has Failed to Execute");
        } else if (result.getStatus() == ITestResult.SKIP) {
            reporter.log(LogStatus.SKIP, className + " : Has got Skipped from Execution");
        }
        report.endTest(reporter);
    }
    @AfterTest
    public void closeBrowser(){
        driver.close();
    }

    @AfterSuite
    public void flushReport(){
        report.flush();
    }
}
