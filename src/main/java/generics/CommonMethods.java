package generics;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static generics.BaseTest.driver;

public class CommonMethods {
    static ExtentTest reporter=null;

    private static String getFormattedDateTime() {
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
        return simpleDate.format(new Date());
    }

    private static String getScreenShot() {
        String encodedBase64 = null;
        File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try (FileInputStream fileInputStreamReader = new FileInputStream (sourceFile)){
            byte[] bytes = new byte[(int) sourceFile.length()];
            fileInputStreamReader.read(bytes);
            encodedBase64 = new String(Base64.encodeBase64(bytes));
        } catch (Exception e) {
            reporter.log(LogStatus.FAIL, "An Error occurred while taking ScreenShot Because of : " + e.getMessage().split("\n")[0].trim());
            Assert.fail();
        }
        return "data:image/png;base64," + encodedBase64;
    }


    private static String getErrorMessage(Exception e) {
        String error;
        String[] message = e.getMessage().split(":");
        String screenshotPath = getScreenShot();
        error = message[0].trim() + " : " + message[1].trim() + " - Element info : " + message[message.length - 1].trim()
                + reporter.addScreenCapture(screenshotPath);
        return error;
    }

    public void verifyTitle(String eTitle) {
        String aTitle = driver.getTitle();
        try {
            Assert.assertEquals(aTitle, eTitle);
            reporter.log(LogStatus.PASS, " ActualTitle : " + aTitle + " is matching with the ExpectedTitle: " + eTitle);
        } catch (Exception e) {
            reporter.log(LogStatus.FAIL, " ActualTitle : " + aTitle + " is not matching with the ExpectedTitle : "
                    + eTitle + " and the ERROR is : " + getErrorMessage(e));
            Assert.fail();
        }
    }

    public void verifyTitleContain(String eTitle) {
        try {
            new WebDriverWait(driver, 30).until(ExpectedConditions.titleContains(eTitle));
            String aTitle = driver.getTitle();
            Assert.assertEquals(aTitle, eTitle);
            reporter.log(LogStatus.PASS, " ActualTitle : " + aTitle + " is matching with the ExpectedTitle: " + eTitle);
        } catch (Exception e) {
            String aTitle = driver.getTitle();
            reporter.log(LogStatus.FAIL, " ActualTitle : " + aTitle + " is not matching with the ExpectedTitle : "
                    + eTitle + " and the ERROR is : " + getErrorMessage(e));
            Assert.fail();
        }
    }
}
