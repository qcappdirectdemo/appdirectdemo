package AppDirectSignUp;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AppDirect_RegularActivation {
	
	public WebDriver FFdriver;
	private ArrayList<String> arrValidationMessage = new ArrayList<String>();
	public String strLanguage = "English";
	
	@BeforeMethod
	public void beforeMethod() {
		System.out.println("================Test Started================");
		FFdriver = new FirefoxDriver();
		FFdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	@Test
	public void main()throws Exception{
		
		AppDirect_GeneralClass oAppDirect_GeneralClass = new AppDirect_GeneralClass();
		String strLocalFileName = "../TestSettingXML/AppDirectActivation.xml";
		String strLanguage = this.strLanguage;
		String strEmailAddress = "newapp201509183@gmail.com";
		String strPassword = "Test1234D";
		Integer intNumber = 6;
		boolean boolGetMessageStrings = oAppDirect_GeneralClass.funcGetProjectTerms(strLocalFileName, strLanguage, intNumber);
		this.arrValidationMessage = oAppDirect_GeneralClass.getArrValidationMessage();
		if(!boolGetMessageStrings){
			System.out.println("========Failed to get all message strings. Quit========");
		}else{
			
			boolean boolTestResult =true;
			FFdriver.get("http://www.appdirect.com/");
			FFdriver.manage().window().maximize();
			
			FFdriver.findElement(By.linkText("Login")).click();
			FFdriver.findElement(By.linkText("Sign Up")).click();
			Thread.sleep(3000);
			FFdriver.findElement(By.name("emailAddress")).clear();
			FFdriver.findElement(By.name("emailAddress")).sendKeys(strEmailAddress);
			FFdriver.findElement(By.name("userSignupButton")).click();
			
			Thread.sleep(10000);

			try{
				oAppDirect_GeneralClass.setStrEmailAddress(strEmailAddress);
				oAppDirect_GeneralClass.setStrPassword(strPassword);
				String strLink = oAppDirect_GeneralClass.funcGetActivationLink();
				System.out.println(strLink);
				if(strLink.indexOf("https") != -1){
					FFdriver.get(strLink);
					if(FFdriver.findElements(By.cssSelector("h2.mb10")).size() != 0){
						 String strActivePageMessage = FFdriver.findElement(By.cssSelector("h2.mb10")).getText().toString();
						 if(!strActivePageMessage.equals(arrValidationMessage.get(5))){
							 System.out.println("===The 'Activate your account.' message is Not matched===");
							 boolTestResult = false;
						 }
					 }else{
						 System.out.println("===The 'Activate your account.' element is Not found===");
						 boolTestResult = false;
					 }
				}
				
			}catch(Error e){
				boolTestResult = false;
			}

			if(boolTestResult){
				System.out.println("=====Activation Validation Passed=====");
			}else{
				System.out.println("=====Activation Validation Failed=====");
			}
			
		}
	}


	@AfterMethod
	public void afterMethod() {
		FFdriver.close();
		//FFdriver.quit();
		System.out.println("================Test Completed================");
	}

	
}
