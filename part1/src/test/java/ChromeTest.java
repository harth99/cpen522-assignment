import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ChromeTest {

    private static WebDriver driver;
    private static final String EMAIL = "harthuang990517@gmail.com";
    private static final String PASSWORD = "Polly@981106";
    private static final String WRONG_PASSWORD = "WrongPassword";
    private static final String WEBSITE_LINK = "http://automationpractice.multiformis.com/index.php";

    @BeforeAll
    public static void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Log in positive")
    public void testSignInPos() {
        driver.get(WEBSITE_LINK);
        WebElement signInLink = driver.findElement(By.cssSelector("a[title='Log in to your customer account']"));
        signInLink.click();

        WebElement emailAddr = driver.findElement(By.id("email"));
        WebElement password = driver.findElement(By.id("passwd"));

        emailAddr.sendKeys(EMAIL);
        password.sendKeys(PASSWORD);

        password.sendKeys(Keys.RETURN);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }

        try {
            WebElement myAccountElement = driver.findElement(By.cssSelector("h1.page-heading"));
            Assertions.assertTrue(myAccountElement.isDisplayed(), "Logout was not successful. My Account element not found after logout.");
        } catch (NoSuchElementException e) {
            System.out.println("Logout successful. My account element not found after logout.");
        }

        WebElement signOutLink = driver.findElement(By.cssSelector("a.logout"));
        signOutLink.click();
    }

    @Test
    @DisplayName("Log in negative")
    public void testSignInNeg() {
        driver.get(WEBSITE_LINK);
        WebElement signInLink = driver.findElement(By.cssSelector("a[title='Log in to your customer account']"));
        signInLink.click();

        WebElement emailAddr = driver.findElement(By.id("email"));
        WebElement password = driver.findElement(By.id("passwd"));

        emailAddr.sendKeys(EMAIL);
        password.sendKeys(WRONG_PASSWORD);

        password.sendKeys(Keys.RETURN);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(5000));
        WebElement alertElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert.alert-danger")));

        // Check if the alert contains the expected text
        String alertText = alertElement.getText();
        Assertions.assertTrue(alertText.contains("Authentication failed."), "Incorrect password alert not displayed.");
    }

    @Test
    @DisplayName("Search positive")
    public void testSearchPos() {
        driver.get(WEBSITE_LINK);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(10000));
        WebElement searchBox = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("search_query_top")));

        searchBox.sendKeys("dress");
        searchBox.sendKeys(Keys.RETURN);

        WebElement unorderedList = driver.findElement(By.id("product_list"));
        int listItemCount = unorderedList.findElements(By.className("ajax_block_product")).size();

        Assertions.assertEquals(7, listItemCount);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }

        driver.navigate().back();
    }

    @Test
    @DisplayName("Search negative")
    public void testSearchNeg() {
        driver.get(WEBSITE_LINK);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(10000));
        WebElement searchBox = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("search_query_top")));

        searchBox.sendKeys("jacket");
        searchBox.sendKeys(Keys.RETURN);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }

        WebElement alertElement = driver.findElement(By.className("alert-warning"));
        String alertText = alertElement.getText();

        Assertions.assertTrue(alertText.contains("No results were found"));
    }

    @Test
    @DisplayName("Search negative 2")
    public void testSearchNeg2() {
        driver.get(WEBSITE_LINK);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(10000));
        WebElement searchBox = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("search_query_top")));

        searchBox.sendKeys("random");
        searchBox.sendKeys(Keys.RETURN);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }

        WebElement alertElement = driver.findElement(By.className("alert-warning"));
        String alertText = alertElement.getText();

        Assertions.assertTrue(alertText.contains("No results were found"));
    }

    @Test
    @DisplayName("Adding item to shopping cart positive")
    public void testAddItemToShoppingCartPos() {
        driver.get(WEBSITE_LINK);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(5000));
        WebElement linkElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.product-name[title='Printed Summer Dress']")));
        String href = linkElement.getAttribute("href");
        driver.get(href);
        WebElement liElement = driver.findElement(By.id("color_11"));
        liElement.click();
        WebElement selectElement = driver.findElement(By.id("group_1"));
        Select select = new Select(selectElement);
        select.selectByValue("2");
        driver.navigate().refresh();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        WebElement buttonElement = driver.findElement(By.cssSelector("button.exclusive"));
        buttonElement.click();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }

        WebDriverWait waitForConfirmation = new WebDriverWait(driver, Duration.ofMillis(5000));
        WebElement element = waitForConfirmation.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("span.title")));

        Assertions.assertNotNull(element, "Element not found.");
        String actualText = element.getText();
        String expectedText = "Product successfully added to your shopping cart";
        Assertions.assertEquals(expectedText, actualText, "Text content mismatch.");
    }

    @Test
    @DisplayName("Adding item to shopping cart negative")
    public void testAddItemToShoppingCartNeg() {
        driver.get(WEBSITE_LINK);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(5000));
        WebElement linkElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.product-name[title='Printed Summer Dress']")));
        String href = linkElement.getAttribute("href");
        driver.get(href);
        WebElement liElement = driver.findElement(By.id("color_11"));
        liElement.click();
        WebElement selectElement = driver.findElement(By.id("group_1"));
        Select select = new Select(selectElement);
        select.selectByValue("2");
        driver.navigate().refresh();

        WebElement spanElement = driver.findElement(By.id("quantityAvailable"));
        String text = spanElement.getText();
        int exceedOne = Integer.parseInt(text) + 1;
        WebElement inputElement = driver.findElement(By.id("quantity_wanted"));
        inputElement.clear();
        inputElement.sendKeys(String.valueOf(exceedOne)); // Specified value to be input
        WebElement addToCartBtn = driver.findElement(By.cssSelector("button.exclusive"));
        addToCartBtn.click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        WebDriverWait waitErrorMessage = new WebDriverWait(driver, Duration.ofMillis(5000));
        WebElement errorMessageElement = waitErrorMessage.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("p.fancybox-error")));

        Assertions.assertNotNull(errorMessageElement, "Error message element not found.");
        String actualErrorMessage = errorMessageElement.getText();
        String expectedErrorMessage = "There are not enough products in stock.";
        Assertions.assertEquals(expectedErrorMessage, actualErrorMessage, "Error message text mismatch.");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
    }

    @Test
    @DisplayName("Checkout process positive")
    public void testCheckoutProcessPos() {
        driver.get(WEBSITE_LINK);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(5000));
        WebElement linkElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.product-name[title='Printed Summer Dress']")));
        String href = linkElement.getAttribute("href");
        driver.get(href);
        WebElement liElement = driver.findElement(By.id("color_11"));
        liElement.click();
        WebElement selectElement = driver.findElement(By.id("group_1"));
        Select select = new Select(selectElement);
        select.selectByValue("2");
        driver.navigate().refresh();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        WebElement buttonElement = driver.findElement(By.cssSelector("button.exclusive"));
        buttonElement.click();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }

        WebDriverWait waitForConfirmation = new WebDriverWait(driver, Duration.ofMillis(5000));
        WebElement element = waitForConfirmation.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("span.title")));

        Assertions.assertNotNull(element, "Element not found.");
        String actualText = element.getText();
        String expectedText = "Product successfully added to your shopping cart";
        Assertions.assertEquals(expectedText, actualText, "Text content mismatch.");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        WebElement proceedToCheckout = driver.findElement(By.cssSelector("a.btn.btn-default.button.button-medium[title='Proceed to checkout']"));
        proceedToCheckout.click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        WebElement finalProceedToCheckout = driver.findElement(By.cssSelector("a.btn.btn-default.button.button-medium.standard-checkout[title='Proceed to checkout']"));
        finalProceedToCheckout.click();

        WebDriverWait waitEmail = new WebDriverWait(driver, Duration.ofMillis(5000));
        WebElement emailAddr = waitEmail.until(ExpectedConditions.presenceOfElementLocated(By.id("email")));

        WebElement password = driver.findElement(By.id("passwd"));

        emailAddr.sendKeys(EMAIL);
        password.sendKeys(PASSWORD);

        password.sendKeys(Keys.RETURN);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        WebElement processAddress = driver.findElement(By.cssSelector("button.button.btn.btn-default.button-medium[name='processAddress']"));
        processAddress.click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        WebElement checkboxElement = driver.findElement(By.id("cgv"));
        checkboxElement.click();
        WebElement processCarrier = driver.findElement(By.cssSelector("button.button.btn.btn-default.standard-checkout.button-medium[name='processCarrier']"));
        processCarrier.click();

        WebElement anchorElement = driver.findElement(By.cssSelector("a.bankwire"));
        String hrefPayment = anchorElement.getAttribute("href");
        driver.get(hrefPayment);

        WebElement submitPaymentButton = driver.findElement(By.cssSelector("button.button.btn.btn-default.button-medium[type='submit']"));
        submitPaymentButton.click();

        WebElement alertElement = driver.findElement(By.cssSelector("p.alert.alert-success"));
        Assertions.assertNotNull(alertElement, "Alert element not found.");
        String alertText = alertElement.getText();
        Assertions.assertTrue(alertText.contains("Your order on My Store is complete."), "Alert text mismatch.");


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        driver.get(WEBSITE_LINK);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        WebElement signOutLink = driver.findElement(By.cssSelector("a.logout"));
        signOutLink.click();


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
    }

    @Test
    @DisplayName("Checkout process negative")
    public void testCheckoutProcessNeg() {
        driver.get(WEBSITE_LINK);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(5000));
        WebElement linkElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.product-name[title='Printed Summer Dress']")));
        String href = linkElement.getAttribute("href");
        driver.get(href);
        WebElement liElement = driver.findElement(By.id("color_11"));
        liElement.click();
        WebElement selectElement = driver.findElement(By.id("group_1"));
        Select select = new Select(selectElement);
        select.selectByValue("2");
        driver.navigate().refresh();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        WebElement buttonElement = driver.findElement(By.cssSelector("button.exclusive"));
        buttonElement.click();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }

        WebDriverWait waitForConfirmation = new WebDriverWait(driver, Duration.ofMillis(5000));
        WebElement element = waitForConfirmation.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("span.title")));

        Assertions.assertNotNull(element, "Element not found.");
        String actualText = element.getText();
        String expectedText = "Product successfully added to your shopping cart";
        Assertions.assertEquals(expectedText, actualText, "Text content mismatch.");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        WebElement proceedToCheckout = driver.findElement(By.cssSelector("a.btn.btn-default.button.button-medium[title='Proceed to checkout']"));
        proceedToCheckout.click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        WebElement finalProceedToCheckout = driver.findElement(By.cssSelector("a.btn.btn-default.button.button-medium.standard-checkout[title='Proceed to checkout']"));
        finalProceedToCheckout.click();

        WebDriverWait waitEmail = new WebDriverWait(driver, Duration.ofMillis(5000));
        WebElement emailAddr = waitEmail.until(ExpectedConditions.presenceOfElementLocated(By.id("email")));

        WebElement password = driver.findElement(By.id("passwd"));

        emailAddr.sendKeys(EMAIL);
        password.sendKeys(WRONG_PASSWORD);

        password.sendKeys(Keys.RETURN);

        WebDriverWait waitWrongPassword = new WebDriverWait(driver, Duration.ofMillis(5000));
        WebElement alertElement = waitWrongPassword.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert.alert-danger")));
        String alertText = alertElement.getText();
        Assertions.assertTrue(alertText.contains("Authentication failed."), "Incorrect password alert not displayed.");
    }

    @Test
    @DisplayName("Extra feature testing: order history")
    public void testCheckOrderHistory() {
        driver.get(WEBSITE_LINK);
        WebElement signInLink = driver.findElement(By.cssSelector("a[title='Log in to your customer account']"));
        signInLink.click();

        WebElement emailAddr = driver.findElement(By.id("email"));
        WebElement password = driver.findElement(By.id("passwd"));

        emailAddr.sendKeys(EMAIL);
        password.sendKeys(PASSWORD);

        password.sendKeys(Keys.RETURN);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(5000));
        WebElement anchorElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[title='Orders']")));
        String href = anchorElement.getAttribute("href");
        driver.get(href);

        WebDriverWait waitBankWire = new WebDriverWait(driver, Duration.ofMillis(5000));
        WebElement element = waitBankWire.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("td.history_method")));
        Assertions.assertNotNull(element, "Element not found.");
        String actualText = element.getText();
        String expectedText = "Bank wire";
        Assertions.assertEquals(expectedText, actualText, "Text content mismatch.");

        WebElement signOutLink = driver.findElement(By.cssSelector("a.logout"));
        signOutLink.click();
    }
}

