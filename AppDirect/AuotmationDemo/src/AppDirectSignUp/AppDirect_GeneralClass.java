package AppDirectSignUp;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AppDirect_GeneralClass {
	private ArrayList<String> arrValidationMessage = new ArrayList<String>();
	
	public ArrayList<String> getArrValidationMessage(){
		return this.arrValidationMessage;
	}
	
	private String strActivationLink; 
	
	public String getStrActivationLink(){
		return this.strActivationLink;
	}
	
	private String strEmailAddress;
	
	public void setStrEmailAddress(String strEmailAddress){
		this.strEmailAddress = strEmailAddress;
	}
	
	private String strPassword;
	
	public void setStrPassword(String strPassword){
		this.strPassword = strPassword;
	}
	
	public boolean funcGetProjectTerms(String strFilePath,String strLanguage, Integer intNumber) throws Exception{
		
		String strLocalFileName = strFilePath;

		File fXmlFile = new File(strLocalFileName);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);		
		//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		NodeList nList = doc.getElementsByTagName("Language");
		//System.out.println("----------------------------");
		try{
			for (int temp = 0; temp < nList.getLength(); temp++) {
				 
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					 
					Element eElement = (Element) nNode;
					String strMessageTagName = "";
					if(eElement.getAttribute("id").toString().equals(strLanguage)){
						for(int i=1;i<=intNumber;i++){
							strMessageTagName = "message";
								strMessageTagName = strMessageTagName + Integer.toString(i);
								this.arrValidationMessage.add(eElement.getElementsByTagName(strMessageTagName).item(0).getTextContent().toString());
								
							}
						}
						
					}
							
				}
				
			}catch(Error e){
				return false;
			}

			return true;
			
		}

	
	public String funcGetActivationLink() throws Exception {
		String strReceivingHost="imap.gmail.com";//for imap protocol
		String strLogin = this.strEmailAddress;
		String strPassword = this.strPassword;
		String strReturnURL = "";
        Properties props2=System.getProperties();
        props2.setProperty("mail.store.protocol", "imaps");
        // I used imaps protocol here
 
        Session session2=Session.getDefaultInstance(props2, null);
		try {

			Store store=session2.getStore("imaps");
	
			store.connect(strReceivingHost,strLogin, strPassword);
	
			Folder folder=store.getFolder("INBOX");//get inbox
	
			folder.open(Folder.READ_ONLY);//open folder only to read
	
			Message message[]=folder.getMessages();
			int intTotalAmount = message.length-1;
			String strMessageBody = message[intTotalAmount].getContent().toString();
			int intBeginNumber = strMessageBody.indexOf("https");
			int intEndNumber = strMessageBody.indexOf("If")-8;
			strReturnURL = strMessageBody.substring(intBeginNumber,intEndNumber).toString();
			//System.out.println("------------------------------");
			//print subjects of all mails in the inbox
	
			//System.out.println("Mail Subject:- " + message[intTotalAmount].getSubject());
			//System.out.println("Mail Body:- " + strMessageBody);
			//System.out.println("Mail URL:- " + strReturnURL);
	
		//close connections

			folder.close(true);
	
			store.close();
			return strReturnURL;

		} catch (Exception e) {
			System.out.println(e.toString());
			return "";

		}
		
	}
}
