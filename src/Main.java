import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import sun.net.www.ApplicationLaunchException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
	
	private final String[] places = {"Noida, Uttar Pradesh", "Meerut, Uttar Pradesh", "Rajgir, Bihar", "Ludhiana, Punjab", "Amritsar, Punjab",
								"Pune, Maharashtra", "Hyderabad, Andhra Pradesh", "Kolkata, West Bengal", "Lucknow, Uttar Pradesh",
								"Allahabad, Uttar Pradesh",
								"Indore, Madhya Pradesh", "Bhopal, Madhya Pradesh", "Bangalore, Telangana", "Patna, Bihar", "Ranchi, Jharkhand"};
	
	private String getRandomPlace(){
		Random random = new Random();
		int index = random.nextInt(places.length-1);
		return places[index];
	}
	
	public void fillForm(String url, int flag) throws InterruptedException {
		
		System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Selenium-jar-files\\Web drivers\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		
		boolean isFormFilled = false;
		
		driver.get(url);
		
		int page = 1;
		
		while (true) {
			
			if(isFormFilled)
				break;
		
			List<WebElement> sections = driver.findElements(By.className("freebirdFormviewerViewNumberedItemContainer"));
			
			for (WebElement section : sections) {
				
				List<WebElement> radioButtons = section.findElements(By.className("docssharedWizToggleLabeledLabelWrapper"));
				List<WebElement> textAreas = section.findElements(By.className("quantumWizTextinputPaperinputInput"));
				
				if (radioButtons.size() != 0) {
					
					Random random = new Random();
					int index = random.nextInt(radioButtons.size());;
					
					if(page == 1 && sections.get(0).equals(section)){
						index = random.nextInt(radioButtons.size()) + 1;
					} else if (page == 1 && sections.get(1).equals(section)){
						index = random.nextInt(radioButtons.size() - 1);
					} else if (page == 2){
						index = (flag < 7)? 0: 1;
					}
					
					radioButtons.get(index).click();
					System.out.println("Page : " + page + "Index : " + index + ", " + radioButtons.get(index).getText());
//					Thread.sleep(1000);
				}
				if (textAreas.size() != 0) {
					if(page == 7)
						break;
					for (WebElement textArea : textAreas) {
						textArea.sendKeys(getRandomPlace());
					}
				}
			}
			WebElement buttonDiv = driver.findElement(By.className("freebirdFormviewerViewNavigationLeftButtons"));
			
			List<WebElement> buttons = buttonDiv.findElements(By.className("appsMaterialWizButtonPaperbuttonContent"));
			
			for (WebElement button : buttons) {
				if (button.getText().equals("Next")) {
					
					button.click();
					++page;
					
				} else if (button.getText().equals("Submit")) {
					
					button.click();
					isFormFilled = true;
					driver.close();
					
				}
			}
		}
		
	}
	
	public static void main(String[] args) {
		Main main = new Main();
		for (int i = 0; i < 10;) {
			try {
				main.fillForm("https://forms.gle/jxcou82LrAR4ozFc9", i);
				i++;
				Thread.sleep(30*1000);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}
}
