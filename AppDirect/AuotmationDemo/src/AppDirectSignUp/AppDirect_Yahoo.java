package AppDirectSignUp;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class AppDirect_Yahoo {
	public WebDriver FFdriver;
	private ArrayList<String> arrValidationMessage = new ArrayList<String>();
	public String strLanguage = "English";
	
	private String strYahooEmailAddress;
	
	public void setStrYahooEmailAddress(String strYahooEmailAddress){
		this.strYahooEmailAddress = strYahooEmailAddress;
	}
	
	public String getStrYahooEmailAddress(){
		return this.strYahooEmailAddress;
	}
	
	private String strPassword;
	
	public void setStrPassword(String strPassword){
		this.strPassword = strPassword;
	}
	
	public String getStrPassword(){
		return this.strPassword;
	}
	
	
	 @BeforeMethod
	 public void beforeMethod() {
		 System.out.println("================Test Started================");
		 FFdriver = new FirefoxDriver();
		 FFdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);		 
	 }
	
	 @Test
	 public void main()throws Exception{
		//Initialize validation message from xml file
		 AppDirect_GeneralClass oAppDirect_GeneralClass = new AppDirect_GeneralClass();
		 String strLocalFileName = "../TestSettingXML/AppDirectYahoo.xml";
		 String strLanguage = this.strLanguage;
		 Integer intNumber = 12;
		 boolean boolGetMessageStrings = oAppDirect_GeneralClass.funcGetProjectTerms(strLocalFileName, strLanguage, intNumber);
		 this.arrValidationMessage = oAppDirect_GeneralClass.getArrValidationMessage();
		 if(!boolGetMessageStrings){
			 System.out.println("========Failed to get all message strings. Quit========");
		 }else{
			 FFdriver.get("http://www.appdirect.com/");
			 FFdriver.manage().window().maximize();
			 FFdriver.findElement(By.linkText("Login")).click();
			 FFdriver.findElement(By.linkText("Sign Up")).click();
			 this.strYahooEmailAddress = "newapp201509182@yahoo.com";  //Set test Yahoo email address here
			 this.strPassword = "Test1234D";           //Set test Yahoo password here
			 boolean boolTestResult = this.funcYahooSignUpNewAccount();
			 if(boolTestResult){
				 System.out.println("======='Sign up with Yahoo' New Account test Passed=======");
			 }else{
				 System.out.println("======='Sign up with Yahoo' New Account test Failed=======");
			 }
		 }

		 
	 }


	 @AfterMethod
	 public void afterMethod() {
		 FFdriver.close();
		 //FFdriver.quit();
		 System.out.println("================Test Completed================");
	 }

	 public boolean funcYahooSignUpNewAccount() throws Exception {
		 boolean boolTestResult = true;
		 String strEmailAddress = this.strYahooEmailAddress;
		 String strPassword = this.strPassword;
		 
		 if(FFdriver.findElements(By.cssSelector("span.idp-text")).size() != 0){
			 FFdriver.findElement(By.cssSelector("span.idp-text")).click();
			 FFdriver.findElement(By.id("login-username")).clear();
			 FFdriver.findElement(By.id("login-username")).sendKeys(strEmailAddress);
			 FFdriver.findElement(By.id("login-passwd")).clear();
			 FFdriver.findElement(By.id("login-passwd")).sendKeys(strPassword);
			 FFdriver.findElement(By.id("persistent")).click();
			 FFdriver.findElement(By.id("login-signin")).click();
			 if(FFdriver.findElements(By.id("agree")).size() != 0){
				 FFdriver.findElement(By.id("agree")).click();
				 if(FFdriver.findElements(By.cssSelector("h2.mb10")).size() != 0){
					 String strActivePageMessage = FFdriver.findElement(By.cssSelector("h2.mb10")).getText().toString();
					 if(!strActivePageMessage.equals(arrValidationMessage.get(0))){
						 System.out.println("===The 'Activate your account.' message is Not found===");
						 boolTestResult = false;
					 }
				 }else{
					 System.out.println("===The 'Activate your account.' element is Not found===");
					 boolTestResult = false;
				 }
			 }else{
				 System.out.println("===The 'Agree' button is Not found===");
				 boolTestResult = false;
				 return boolTestResult;
			 }
			 
		 }else{
			 System.out.println("===The 'Sign up with Yahoo!' element is Not found===");
			 boolTestResult = false;
		 }
		 
		 return boolTestResult;
	 }
}
