package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        WebDriver driver = new ChromeDriver();

        driver.get("http://automationpractice.multiformis.com/index.php");


        System.out.println("Viewing website: " + driver.getTitle());

        WebElement searchBox = driver.findElement(By.id("search_query_top"));

        searchBox.sendKeys("dress");
        searchBox.sendKeys(Keys.RETURN);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }

        driver.navigate().back();

        WebElement signInLink = driver.findElement(By.cssSelector("a[title='Log in to your customer account']"));
        signInLink.click();

        WebElement emailAddr = driver.findElement(By.id("email"));
        WebElement password = driver.findElement(By.id("passwd"));

        emailAddr.sendKeys("harthuang990517@gmail.com");
        password.sendKeys("Polly@981106");

        password.sendKeys(Keys.RETURN);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        WebElement signOutLink = driver.findElement(By.className("logout"));
        signOutLink.click();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        driver.navigate().back();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
        driver.navigate().back();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
        driver.navigate().back();


        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
        }

        driver.quit();
    }
}