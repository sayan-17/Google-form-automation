import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
	
	private static String[] formLinks = {
			"https://docs.google.com/forms/d/e/1FAIpQLSdDXLulaBbzr0gZk-CoWlmi-n8duRKr-Izo27L1-8QhP1U0fg/viewform?usp=sf_link"
	};
	
	private static String[] filePath = {
			"C:\\Users\\sayan\\Desktop\\Projects\\Java\\FillForm\\sample.txt",
	};
	
	private List<String> getTexts(String pathOfFile) throws FileNotFoundException {
		File file = new File(pathOfFile);
		Scanner sc = new Scanner(file);
		List<String> texts = new ArrayList<>();
		while (sc.hasNextLine()){
			texts.add(sc.nextLine());
		}
		return texts;
	}
	
	private String getRandomText(String path) throws FileNotFoundException {
		Random random = new Random();
		List<String > texts = getTexts(path);
		int index = random.nextInt(texts.size()-1);
		return texts.get(index);
	}
	
	public void fillForm(String url, String path) throws InterruptedException, FileNotFoundException {
		
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\sayan\\Downloads\\chromedriver_win32 (2)\\chromedriver.exe");
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
					int index = random.nextInt(radioButtons.size());
					System.out.println(radioButtons.size() + " : " + index);
					radioButtons.get(index).click();
					System.out.println("Page : " + page + "Index : " + index + ", " + radioButtons.get(index).getText());
				}
				if (textAreas.size() != 0) {
					for (WebElement textArea : textAreas) {
						textArea.sendKeys(getRandomText(path));
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
		for (int i = 0; i < 10; ++i) {
			try {
				main.fillForm(formLinks[0], filePath[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
