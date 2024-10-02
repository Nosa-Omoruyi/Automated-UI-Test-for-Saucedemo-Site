package saucedemosampletest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SauceDemoUITest {
    WebDriver driver;

    @BeforeMethod
    public void setUp() {
        // Set up the ChromeDriver path
        System.setProperty("webdriver.chrome.driver", "C:\\SeleniumWebDriverJAVA\\ChromeDriver\\chromedriver-win64\\chromedriver.exe");
        
        // Initialize WebDriver
        driver = new ChromeDriver();
        
        // Open the SauceDemo website
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");
        // Log in to the site
        login("standard_user", "secret_sauce");
     // Wait for the next page to load by checking for a specific element
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("app_logo")));     
    }

    
    @Test(priority = 1)
    public void verifySortingByNameA_Z() {
        // Verify items sorted by Name (A -> Z)
    	verifyItemsSortedAscendingByDefault();
    }
    
    
    
    @Test(priority = 2)
    public void verifySortingByNameZ_A() {
        // Verify items sorted by Name (Z -> A)
    	verifyItemsSortedDescending();
    }

    @AfterMethod
    public void tearDown() {
    	
        // Close the browser
        if (driver != null) {
            driver.quit();
        }
    }
    
    
    public void login(String username, String password) {
    	// Use parameters for login
    	driver.findElement(By.name("user-name")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.xpath("//input[@type='submit']")).click();
        
    }
    
    
    
    public void verifyItemsSortedAscendingByDefault() {
        // Retrieve item names when the default "Name (A to Z)" is active
        List<String> itemNames = driver.findElements(By.className("inventory_item_name"))
                .stream().map(WebElement::getText).toList();

        // Create a sorted version of the list to compare without modifying the original list
        List<String> sortedItemNames = new ArrayList<>(itemNames);
        Collections.sort(sortedItemNames);

        // Verify the items are sorted in ascending order by default
        Assert.assertEquals(sortedItemNames, itemNames);
    }

    public void verifyItemsSortedDescending() {
        // Before selecting "Name (Z to A)"
        List<String> itemNamesBeforeSort = driver.findElements(By.className("inventory_item_name"))
                .stream().map(WebElement::getText).toList();

        // Selecting "Name (Z to A)"
        new Select(driver.findElement(By.className("product_sort_container")))
                .selectByVisibleText("Name (Z to A)");

        // After selecting "Name (Z to A)"
        List<String> itemNamesAfterSort = driver.findElements(By.className("inventory_item_name"))
                .stream().map(WebElement::getText).toList();

        // Create a sorted version of the list in reverse order to compare
        List<String> expectedItemNames = new ArrayList<>(itemNamesBeforeSort);
        expectedItemNames.sort(Collections.reverseOrder());

        // Verify the items are sorted in descending order
        Assert.assertEquals(expectedItemNames, itemNamesAfterSort);
    }

       
    
    
    
    
    
}




	
