package Utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;


public class ExtentReportManager {
    private static final Logger log = LoggerFactory.getLogger(ExtentReportManager.class);
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private ExtentTest logger;

    public static ExtentReports getExtentReports() {
        if (extent == null) {
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter("extent-report.html");
            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
        }
        return extent;
    }

    public static void createTest(String testName, String description) {
        ExtentTest test = getExtentReports().createTest(testName, description);
        extentTest.set(test);
    }

    public static ExtentTest getTest() {
        return extentTest.get();
    }

    public void info(final Object message)
    {
        if (message == null)
            logger.info("Report message get with null value");
        else
            logger.info(message.toString());
    }

    public void info(final StringWriter request, final StringWriter response)
    {
        logger.info(MarkupHelper.createCodeBlock(request.toString(),response.toString()));
    }

    public void setScreenshot(final WebDriver driver)
    {
        log.trace("taking screenshot.");
        try {

        }
        catch (Exception ex)
        {
            log.error("could not take a screenshot.");
            logger.warning("could not take a screenshot"+ex.getMessage());
        }
    }

    public static void flushReports() {
        if (extent != null) {
            extent.flush();
        }
    }
}
