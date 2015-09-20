package AppDirectSignUp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class AppDirect_RegularSignUp {
	public WebDriver FFdriver;
	private ArrayList<String> arrValidationMessage = new ArrayList<String>();
	public String strLanguage = "English";
	
	//@Parameters("Test_Langauge")
	@BeforeMethod
	public void beforeMethod() {
		System.out.println("================Test Started================");
		FFdriver = new FirefoxDriver();
		FFdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		//strLanguage = Test_Langauge;
		
	}
	
	@Test
	public void main() throws Exception {
		//Initialize validation message from xml file
		//If initialization failed, it quits the test
		AppDirect_GeneralClass oAppDirect_GeneralClass = new AppDirect_GeneralClass();
		String strLocalFileName = "../TestSettingXML/AppDirect.xml";
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
			//Check Login page title
			if(this.funcCheckPageTitle(3)){
				
			}
			
			FFdriver.findElement(By.linkText("Sign Up")).click();
			//Check Sign up page title
			if(this.funcCheckPageTitle(4)){
				
			}
			
			//Switch language on UI
			boolean boolContiune = true;
			if(!strLanguage.equals("English")){
				boolean boolSwitchLanguage = this.funcSwitchLanguage(strLanguage);
				if(!boolSwitchLanguage){
					System.out.println("=====Switch to " + strLanguage + " Failed. Quit");
					boolContiune = false;
				}
			}
			Thread.sleep(3000);
			if(boolContiune){
				//Valid Sign Up
				System.out.println("===Test Valid Sign Up Started===");
				if(this.funcValidSignUp()){
					System.out.println("===Test Valid Sign Up Passed===");
				}else{
					System.out.println("===Test Valid Sign Up Failed===");
				}
				
				
				//Return to Sign up page
				//Also can be done by FFdriver.navigate().refresh();
				FFdriver.findElement(By.linkText(this.arrValidationMessage.get(11))).click();
				FFdriver.findElement(By.linkText(this.arrValidationMessage.get(10))).click();
				
				boolean boolTestResult = true;
				String strInput = "";
				String strBaseline = "";
				//Empty Input
				System.out.println("===Test Empty Input Started===");
				strBaseline = "['webshims.valueMissing.defaultMessage'] = ";
				boolTestResult = this.funcInvalidInputValidation(strInput, strBaseline, 0);
				if(boolTestResult){
					System.out.println("===Test Empty Input Passed===");
				}else{
					System.out.println("===Test Empty Input Failed===");
				}
				
				//Invalid Email Address 1
				System.out.println("===Test Invalid Input 1 Started===");
				strInput = "sdfdsfdsfdsfds";
				strBaseline = "['signup.popup.email.patternMismatch'] = ";
				Thread.sleep(1000);
				boolTestResult = this.funcInvalidInputValidation(strInput, strBaseline, 1);
				if(boolTestResult){
					System.out.println("===Test Invalid Input '" + strInput +  "' Passed===");
				}else{
					System.out.println("===Test Invalid Input '" + strInput +  "' Failed===");
				}
				FFdriver.findElement(By.linkText(this.arrValidationMessage.get(11))).click();
				FFdriver.findElement(By.linkText(this.arrValidationMessage.get(10))).click();
				
				//Invalid Email Address 2
				System.out.println("===Test Invalid Input 2 Started===");
				strInput = "test@test";
				strBaseline = "['signup.popup.email.patternMismatch'] = ";
				Thread.sleep(1000);
				boolTestResult = this.funcInvalidInputValidation(strInput, strBaseline, 1);
				if(boolTestResult){
					System.out.println("===Test Invalid Input '" + strInput +  "' Passed===");
				}else{
					System.out.println("===Test Invalid Input '" + strInput +  "' Failed===");
				}
				FFdriver.findElement(By.linkText(this.arrValidationMessage.get(11))).click();
				FFdriver.findElement(By.linkText(this.arrValidationMessage.get(10))).click();
				
				//Duplicated Email Address
				System.out.println("===Test Invalid Input 3 Started===");
				strInput = "test@test.com";
				strBaseline = "['email.in.use'] = ";
				Thread.sleep(1000);
				boolTestResult = this.funcInvalidInputValidation(strInput, strBaseline, 2);
				if(boolTestResult){
					System.out.println("===Test Duplicated Input Passed===");
				}else{
					System.out.println("===Test Duplicated Input Failed===");
				}
				
			}
			
		}
		
		
	}


	@AfterMethod
	public void afterMethod() {
		FFdriver.close();
		//FFdriver.quit();
		System.out.println("================Test Completed================");
	}
	
	public boolean funcValidSignUp() throws Exception{
		/*
		 * Valid Sign Up Test
		 * 1. Generate a dynamic email address to sign up.
		 * 2. Compare successful messages.
		 */
		boolean boolTestResult = true;
		String strInput = "";
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//get current date time with Date()
		Date date = new Date();
		strInput = dateFormat.format(date).toString().replace(" ", "_");
		strInput = strInput.replace(":", "_");
		strInput = strInput + "@test.com";
		System.out.println("Sign Up with new email address " + strInput);
		FFdriver.findElement(By.name("emailAddress")).clear();
		FFdriver.findElement(By.name("emailAddress")).sendKeys(strInput);
		FFdriver.findElement(By.name("userSignupButton")).click();
		Thread.sleep(3000);
		if(FFdriver.findElements(By.cssSelector("div.round-white-cont.confirm > span")).size() == 2){
			List<WebElement> webSpans = FFdriver.findElements(By.cssSelector("div.round-white-cont.confirm > span"));
			Integer intSpanIndex = 0;
			for (WebElement webSpan: webSpans){
				intSpanIndex = intSpanIndex + 1;
				String strSuccessMessage = webSpan.getText().toString();
				String strDefaultMessage = this.arrValidationMessage.get(intSpanIndex+4);
				if(intSpanIndex==1){
					strDefaultMessage = strDefaultMessage.replace("[email address]", strInput);
					//System.out.println("strDefaultMessage is " + strDefaultMessage);
				}
				if(!strSuccessMessage.equals(strDefaultMessage)){
					boolTestResult = false;
					System.out.println("=====Sign Up final message " + intSpanIndex + " is not correct=====");
					//System.out.println("strSuccessMessage is " + strSuccessMessage);
				}else{
					//System.out.println("Good " + intSpanIndex);
				}
			 }
			
		}else{
			boolTestResult = false;
			System.out.println("=====Sign Up is not triggered properly=====");
		}
		return boolTestResult;
	}
	
	public boolean funcInvalidInputValidation(String strInput, String strBaseline, int intMessageIndex) throws Exception{
		/*
		 * Invalid Input Validation
		 * 1. Empty input
		 * It will verify 
		 *  a. input class changed to "form-ui-invalid".
		 *  b. error message from source code "Please fill out this field."
		 */
		boolean boolTestResult = true;
		
		boolean existsDefault = FFdriver.findElements(By.xpath("//input[@class='defaultValue'][@name='emailAddress']")).size() !=0;
		if(existsDefault){
			System.out.println("Default field was loaded");
		}else{
			System.out.println("Default field was NOT loaded");
			//boolTestResult = false;
		}
		FFdriver.findElement(By.name("emailAddress")).clear();
		FFdriver.findElement(By.name("emailAddress")).sendKeys(strInput);
		FFdriver.findElement(By.name("userSignupButton")).click();
		//System.out.println("The source code is: " + "\r\n" + strSourceCode);
		boolean existsError = FFdriver.findElements(By.xpath("//input[@class='form-ui-invalid'][@name='emailAddress']")).size() !=0;
		if(existsError){
			System.out.println("Error class was caught");
		}else{
			System.out.println("Error class was NOT caught");
			boolTestResult = false;
		}
		
		String strSourceCode = FFdriver.getPageSource();
		String strErrorMessage = this.funcGetErrorMessageFromSourceCode(strSourceCode, strBaseline);
		if(strErrorMessage.equals(this.arrValidationMessage.get(intMessageIndex))){
			System.out.println("Error message is correct");
		}else{
			System.out.println("Error message is NOT correct");
			boolTestResult = false;
		}
		
		return boolTestResult;
	}
	
	public boolean funcCheckPageTitle(int intMessageIndex) throws Exception{
		boolean boolTestResult = true;
		String strPageTitle = FFdriver.getTitle().toString();
		if(strPageTitle.equals(this.arrValidationMessage.get(intMessageIndex))){
			System.out.println("Page Title Compare is correct");
		}else{
			System.out.println("Page Title Compare is NOT correct");
			boolTestResult = false;
		}
		return boolTestResult;
	}
	
	public boolean funcSignUpUIValidation() throws Exception{
		boolean boolTestResult = true;
		
		
		
		return boolTestResult;
	}
	
	private String funcGetErrorMessageFromSourceCode(String strSourceCode, String strBaseLine) throws Exception {
		String strResult = "";
		Integer intStartIndex = strSourceCode.indexOf(strBaseLine);
		if(intStartIndex == -1){
			System.out.println("The error message start element was not inclueded in the source code");
		}else{
			Integer intStartIndex2 = strSourceCode.indexOf("= '",intStartIndex);
			Integer intEndIndex = strSourceCode.indexOf(";", intStartIndex);
			if(intEndIndex == -1 || intStartIndex2 == -1){
				System.out.println("The error message end element was not inclueded in the source code");
			}else{
				strResult = strSourceCode.substring(intStartIndex2+3, intEndIndex-1);
			}
		}
		return strResult;
	}
	
	private boolean funcSwitchLanguage(String strRequiredLanguage) throws Exception{
		boolean boolTestResult = true;

		if(strRequiredLanguage.equals("French")){
			if(FFdriver.findElements(By.cssSelector("#id21 > span")).size() !=0){
				FFdriver.findElement(By.cssSelector("#id21 > span")).click();
			}else{
				System.out.println("====Switch to " + strRequiredLanguage + " element is Not found====");
				boolTestResult = false;
			}
			
		}
		
		return boolTestResult;
	}
}
