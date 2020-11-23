// Generated by Selenium IDE
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;
public class Scenario1crationadministrationdecompteaccesTest {
  private WebDriver driver;
  private Map<String, Object> vars;
  JavascriptExecutor js;
  @Before
  public void setUp() {
    driver = new ChromeDriver();
    js = (JavascriptExecutor) driver;
    vars = new HashMap<String, Object>();
  }
  @After
  public void tearDown() {
    driver.quit();
  }
  @Test
  public void scenario1crationadministrationdecompteacces() {
    driver.get("http://localhost:8080/JSPvulnerabilites/");
    driver.manage().window().setSize(new Dimension(945, 1020));
    driver.findElement(By.linkText("s\'enregistrer")).click();
    driver.findElement(By.id("login")).sendKeys("root");
    driver.findElement(By.id("pwd")).click();
    driver.findElement(By.id("pwd")).sendKeys("monpassword");
    driver.findElement(By.id("repwd")).click();
    driver.findElement(By.id("repwd")).sendKeys("monpassword");
    driver.findElement(By.name("submit")).click();
    assertThat(driver.findElement(By.cssSelector(".erreur")).getText(), is("Ce nom d\\\'utilisateur existe déjà"));
    driver.findElement(By.id("login")).sendKeys("testselenium");
    driver.findElement(By.id("pwd")).click();
    driver.findElement(By.id("pwd")).sendKeys("monpassword");
    driver.findElement(By.id("repwd")).click();
    driver.findElement(By.id("repwd")).sendKeys("monpassword");
    driver.findElement(By.name("submit")).click();
    assertThat(driver.getTitle(), is("Vulnérabilités"));
    driver.findElement(By.linkText("se déconnecter")).click();
    assertThat(driver.getTitle(), is("Vulnérabilités - Connexion"));
    driver.findElement(By.id("login")).click();
    driver.findElement(By.id("login")).sendKeys("root");
    driver.findElement(By.id("pwd")).click();
    driver.findElement(By.id("pwd")).sendKeys("depart0");
    driver.findElement(By.name("submit")).click();
    driver.findElement(By.linkText("Administrer utilisateurs")).click();
    driver.findElement(By.xpath("//td[text()=\'testselenium\']//following::td[2]//child::button[@name=\'resetpwd\'] ")).click();
    driver.findElement(By.linkText("se déconnecter")).click();
    assertThat(driver.getTitle(), is("Vulnérabilités - Connexion"));
    driver.findElement(By.id("login")).click();
    driver.findElement(By.id("login")).sendKeys("testselenium");
    driver.findElement(By.id("pwd")).click();
    driver.findElement(By.id("pwd")).sendKeys("monpassword");
    driver.findElement(By.name("submit")).click();
    assertThat(driver.getTitle(), is("Vulnérabilités - Connexion"));
    assertThat(driver.findElement(By.cssSelector(".erreur")).getText(), is("La connexion a échoué"));
    driver.findElement(By.id("pwd")).click();
    driver.findElement(By.id("pwd")).sendKeys("depart0");
    driver.findElement(By.name("submit")).click();
    assertThat(driver.getTitle(), is("Vulnérabilités"));
  }
}