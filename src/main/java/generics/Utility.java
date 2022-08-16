package generics;

import org.testng.Assert;
import org.testng.Reporter;

import java.io.FileInputStream;
import java.util.Properties;

public class Utility {

    static String getPropertyValue(String filePath, String key) {
        String value = "";
        Properties ppt = new Properties();
        try {
            ppt.load(new FileInputStream(filePath));
            value = ppt.getProperty(key);
        } catch (Exception e) {
            Reporter.log(e.getLocalizedMessage(), true);
            Assert.fail();
        }
        return value;
    }

}
