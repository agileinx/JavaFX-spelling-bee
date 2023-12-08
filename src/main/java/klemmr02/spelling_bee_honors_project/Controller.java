//Maxwell Klema
//klemmr02
// FALL 2023 CS16600 HONORS PROJECT SUBMISSION


package klemmr02.spelling_bee_honors_project;

import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.image.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Controller {

	//<----------JAVAFX VARIABLES ---------->
	
	@FXML
    private TextField enterNameTxtField;

    @FXML
    private TextField guessWordTxtField;

    @FXML
    private Button letterBtnCenter;

    @FXML
    private Button letterBtnFive;

    @FXML
    private Button letterBtnFour;

    @FXML
    private Button letterBtnOne;

    @FXML
    private Button letterBtnSix;

    @FXML
    private Button letterBtnThree;

    @FXML
    private Button letterBtnTwo;

    @FXML
    private Label profileNameLbl;

    @FXML
    private HBox profilePictureHBox;

    @FXML
    private VBox spelling_bee_logo_vbox;

    @FXML
    private Label statFiveLbl;

    @FXML
    private Label statFourLbl;

    @FXML
    private Label statOneLbl;

    @FXML
    private Label statThreeLbl;

    @FXML
    private Label statTwoLbl;

    @FXML
    private Label wordsFoundLbl;
    
    @FXML
    private Label guessFeedbackLbl;
    
    @FXML
    private Label playerRankLbl;

    
    //<---------- CONTOLLER.JAVA VARIABLES  ---------->
    
    
    //stat variables
    int wordsFound;
    int points;
    int totalLengthOfWordsFound;
    int totalLengthOfAllValidWords;
    String playerRank;
    String currentGame = "";
    
    //arrays
    ArrayList<String> validWords = new ArrayList<String>();
    ArrayList<String> validWordsFound = new ArrayList<String>();
    
    String[] allTiers = {"Beginner", "Good Start", "Moving Up", "Good", "Solid", "Nice", "Great", "Amazing", "Genius", "Queen Bee"};
    Boolean[] scaleTransitionPlayed = {true, false, false, false, false, false, false, false, false, false};
    int[] pointsRequiredForTiers = {0, 2, 5, 8, 15, 25, 40, 50, 70, 100};
    
    //char[] allChars = {'B', 'N', 'D', 'L', 'H', 'A', 'I'};
    char[] allChars;
    //stores all buttons (besides center because that is not reshuffled)
    ArrayList<Button> letterButtons;

	
    //other misc variables.
    File wordList;
    Scanner wordScanner;
    ImageView defaultPPicView;
    ImageView userPPicView;
    Boolean firstLaunch = true;
    
    
    
    //<---------- INITIALIZATION METHOD | RUNS ON EVERY NEW GAME ---------->
    
    
    
    public void initialize() throws FileNotFoundException {
    	
	 	//spelling bee logo
    	if (firstLaunch) {
    		Image myLogo = new Image("file:images/spellingbeelogo.png");
    		
    		ImageView spellingBeeLogo = new ImageView(myLogo);
    		spellingBeeLogo.setFitHeight(62);
    		spellingBeeLogo.setPreserveRatio(true);
        	
    		spelling_bee_logo_vbox.getChildren().add(spellingBeeLogo);
    	}
		
		
		//default profile picture image
    	if (firstLaunch) {
    		
    		Image defaultPPic = new Image("file:images/defaultppic.jpeg");
    		
    		defaultPPicView = new ImageView(defaultPPic);
    		defaultPPicView.setFitHeight(100);
    		defaultPPicView.setPreserveRatio(true);
    		
    		profilePictureHBox.getChildren().add(defaultPPicView);
    		
    		//Words Found List Wrap
    		wordsFoundLbl.setWrapText(true);
    		
    		//Adding valid words to ArrayList
    		
    		validWords = new ArrayList<String>();
    		validWordsFound = new ArrayList<String>();
    		
    	}
    	
    	
    	firstLaunch = false;
    	
    	//clear word lists
    	validWords.clear();
    	validWordsFound.clear();
    	
		wordList = new File(randomGame());
		wordScanner = new Scanner(wordList);
		
		while(wordScanner.hasNext()) {
			validWords.add(wordScanner.next());
		}
		
		//adds new game letters to board
		shuffleLetters(null);
		letterBtnCenter.setText(String.valueOf(allChars[0]));
		
		totalLengthOfAllValidWords = calcTotalLengthOfValidWords();
		
		System.out.println(validWords);
		
		//GUI stats
		wordsFound = 0;
		points = 0;
		playerRank = "";
		
		statOneLbl.setText("Points: " + points);
		statThreeLbl.setText("Words Found: " + wordsFound);
		statTwoLbl.setText("Points to Next Rank: " + pointsToNextRank());
		statFourLbl.setText("% Words Found: " + String.format("%.1f", wordsFoundPercent()) + "%");
		statFiveLbl.setText("Complexity Score: N/A");
		playerRankLbl.setText("Rank: " + currentRank());
		wordsFoundLbl.setText("");
		guessFeedbackLbl.setText("");
		
		wordScanner.close();
		
    }
    
    //chooses a random game to play out of three possible games (wordlists)
    public String randomGame() {
    	
    	Boolean newGameChosen = false;
    	allChars = new char[7];
    	
    	//loops to ensure that a new game is not the same as the last game.
    	while(!newGameChosen) {
    		
    		int randomInt = (int) (Math.random() * (3 - 0));
    		
    		if (randomInt == 0) {
        		
    			if (!currentGame.equals("GameOneWordList.txt")) {
    				
    				currentGame = "GameOneWordList.txt";
	        		char[] gameOneChars = {'B', 'N', 'D', 'L', 'H', 'A', 'I'};
	        		for (int i = 0; i < gameOneChars.length; i++) {
	        			allChars[i] = gameOneChars[i];
	        		}
	        		newGameChosen = true;
    				
    			} else {
        			continue;
        		}
        		
        		
        	} else if (randomInt == 1) {
        		
        		
    			if (!currentGame.equals("GameTwoWordList.txt")) {
    				
    				currentGame = "GameTwoWordList.txt";
            		char[] gameTwoChars = {'A', 'C', 'R', 'I', 'G', 'H', 'P'};
            		for (int i = 0; i < gameTwoChars.length; i++) {
            			allChars[i] = gameTwoChars[i];
            		}
            		newGameChosen = true;
	    				
    			} else {
        			continue;
        		}
        		
        		
        	} else {
        		
				if (!currentGame.equals("GameThreeWordList.txt")) {
    				
					currentGame = "GameThreeWordList.txt";
	        		char[] gameThreeChars = {'O', 'A', 'T', 'L', 'E', 'F', 'M'};
	        		for (int i = 0; i < gameThreeChars.length; i++) {
	        			allChars[i] = gameThreeChars[i];
	        		}
	        		newGameChosen = true;
	    				
    			} else {
        			continue;
        		}
        		
        		
        	}
    		
    	}
    	
    	
    	return currentGame;
    	
    }
    
    //<---------- JAVAFX ANIMATION IMPLEMENTATION ---------->
    
    //creating new rotate transition
    void rotateTextBox() {
    	
    	RotateTransition rotateTxtBox = new RotateTransition(Duration.millis(200), guessWordTxtField);
        
    	rotateTxtBox.setFromAngle(5);
        rotateTxtBox.setByAngle(-5);
        
        rotateTxtBox.play();
    	
    }
    
    //used to scale the rank label
    void scaleTierLabel() {
    	
    	ScaleTransition st = new ScaleTransition(Duration.millis(500), playerRankLbl);
    	
    	st.setByX(1.1);
        st.setByY(1.1);
        st.setCycleCount(2);
        st.setAutoReverse(true);
    
        st.play();
    	
    }
    
    //used to scale pangrams
    void scalePangrams(String newText) {
    	
    	ScaleTransition st = new ScaleTransition(Duration.millis(500), wordsFoundLbl);
    	
    	st.setByX(1.25);
        st.setByY(1.25);
        st.setCycleCount(2);
        st.setAutoReverse(true);
    
        st.play();
        
        st.setOnFinished(event -> {
           
        	 wordsFoundLbl.setText(newText);
        	 
        });
    	
    }
    
    //used a timeline to fill the textbox on correct guess
    void fillTxtBoxGreen() {
    	
    	Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(guessWordTxtField.styleProperty(), "-fx-background-color: white;")),
                new KeyFrame(Duration.millis(50), new KeyValue(guessWordTxtField.styleProperty(), "-fx-background-color: #cfffb8;")),
                new KeyFrame(Duration.millis(100), new KeyValue(guessWordTxtField.styleProperty(), "-fx-background-color: #a6ff78;"))
        );
    	
    	timeline.setCycleCount(4);
    	timeline.setAutoReverse(true);
    	
    	timeline.play();
    	
    	//resets textbox when animation finishes
    	timeline.setOnFinished(event -> {
            
    		guessWordTxtField.setText("");
	    	guessWordTxtField.setPromptText("Enter a word");
	    	
       });
    	
    }
    
    
    //used a timeline to fill the textbox on correct guess
    void fillTxtBoxRed() {
    	
    	Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(guessWordTxtField.styleProperty(), "-fx-background-color: white;")),
                new KeyFrame(Duration.millis(50), new KeyValue(guessWordTxtField.styleProperty(), "-fx-background-color: #fcb6b6;")),
                new KeyFrame(Duration.millis(100), new KeyValue(guessWordTxtField.styleProperty(), "-fx-background-color: #f76060;"))
        );
    	
    	timeline.setCycleCount(4);
    	timeline.setAutoReverse(true);
    	
    	timeline.play();
    	
    }
    
    
    //used a timeline to fill the textbox on correct PANGRAM guess
    void fillTxtBoxRainbow() {
    	
    	Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(guessWordTxtField.styleProperty(), "-fx-background-color: white;")),
                new KeyFrame(Duration.millis(20), new KeyValue(guessWordTxtField.styleProperty(), "-fx-background-color: #f76060;")),
                new KeyFrame(Duration.millis(40), new KeyValue(guessWordTxtField.styleProperty(), "-fx-background-color: #f7ca60;")),
                new KeyFrame(Duration.millis(60), new KeyValue(guessWordTxtField.styleProperty(), "-fx-background-color: #eff760;")),
                new KeyFrame(Duration.millis(80), new KeyValue(guessWordTxtField.styleProperty(), "-fx-background-color: #a6ff78;")),
                new KeyFrame(Duration.millis(100), new KeyValue(guessWordTxtField.styleProperty(), "-fx-background-color: #60b6f7;")),
                new KeyFrame(Duration.millis(120), new KeyValue(guessWordTxtField.styleProperty(), "-fx-background-color: #c060f7")),
                new KeyFrame(Duration.millis(140), new KeyValue(guessWordTxtField.styleProperty(), "-fx-background-color: #f760e3;"))
        );
    	
    	timeline.setCycleCount(2);
    	timeline.setAutoReverse(true);
    	
    	timeline.play();
    	
    	//resets textbox when animation finishes
    	timeline.setOnFinished(event -> {
            
    		guessWordTxtField.setText("");
	    	guessWordTxtField.setPromptText("Enter a word");
	    	
       });
    	
    }
    
   
    
    //when user presses enter, a guess is submitted
  	 @FXML
  	 void globalKeyEvents(KeyEvent e) {
  		 
  		 if (e.getCode() == KeyCode.ENTER) {
  			 
  			 guessWord(null);
  			
  			 
  		 }
  		 
  	 }
  		 
  		
    	
    //<---------- GUI-BUTTON METHODS ---------->
    
    
    
    @FXML
    void choosePic(ActionEvent event) {

		//Chooses a picture from current user directory
    	FileChooser picChooser = new FileChooser();
    	FileChooser.ExtensionFilter fileFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
    	
    	picChooser.getExtensionFilters().add(fileFilter);
    	
    	File projectDirectory = new File(System.getProperty("user.dir"));
    	picChooser.setInitialDirectory(projectDirectory);
    	
    	File selectedFile = picChooser.showOpenDialog(statTwoLbl.getScene().getWindow()); //stage is the parameter
    	
    	//set profile picture to file chosen (assuming its a valid image)
		
    	String selectedImage = selectedFile.toString();
    	
    	Image userPPic = new Image("file:" + selectedImage);
    	
    	//clear previous image
    	profilePictureHBox.getChildren().remove(userPPicView);
    	
		userPPicView = new ImageView(userPPic);
		userPPicView.setFitHeight(100);
		userPPicView.setFitWidth(100);
		userPPicView.setPreserveRatio(false);
		
		profilePictureHBox.getChildren().remove(defaultPPicView);
		profilePictureHBox.getChildren().add(userPPicView);
		
		//Image defaultPPic = new Image("file:images/defaultppic.jpeg");
    
    }

    
    //makes guess word text field empty
    @FXML
    void clearWord(ActionEvent event) {
    	
    	guessWordTxtField.setText("");
    	guessWordTxtField.setPromptText("Enter a word");
    	guessFeedbackLbl.setText("");
    	
    }

    
    //sets the user's profile name
    @FXML
    void editProfileName(ActionEvent event) {
    	
    	String profileName = enterNameTxtField.getText();
    	profileNameLbl.setText("Name: " + profileName);
    	
    	//clear .txt field after changing
    	enterNameTxtField.setText("");
    	enterNameTxtField.setPromptText("Change Name");
    	
    }
    
    
    //adds a letter to word text field when button is clicked
    @FXML
    void addChar(ActionEvent event) {
    	
    	//get the button and letter that was clicked
    	Button buttonClicked = (Button) event.getSource(); 
    	String buttonLetter = buttonClicked.getText();
    	
    	//add letter to text field
    	guessWordTxtField.appendText(buttonLetter);
    	
    }
    
    
    //removes char at the end of the guess word text field
    @FXML
    void deleteLastChar(ActionEvent event) {
    		
    	String wordGuessed = guessWordTxtField.getText();
    	if (wordGuessed.length() > 0) {
    		String wordGuessedModified = wordGuessed.substring(0, wordGuessed.length() - 1);
    		guessWordTxtField.setText(wordGuessedModified);
    	}
 
    	
    	guessFeedbackLbl.setText("");
    	
    }
    
    
    //function performed when word is guessed
    @FXML
    void guessWord(ActionEvent event) {
    	
    	//check if word is in the word list
    	String guessedWord = guessWordTxtField.getText();
    	for (int i = 0; i < validWords.size(); i++) {
    		guessedWord = guessedWord.toLowerCase();
    		if (guessedWord.equals(validWords.get(i))) {
    			
    			
    			//check the length of the words found arrayList
    			if (validWordsFound.size() > 0) {
    				
    				//check if word is already in found word list
    				for (int j = 0; j < validWordsFound.size(); j++) {
        				if (guessedWord.equals(validWordsFound.get(j))){
        					
        					rotateTextBox();
        					fillTxtBoxRed();
        					
        					guessFeedbackLbl.setText("Word already found!");
        					break;
        					
        				} else if ((j == validWordsFound.size() - 1) && !(guessedWord.equals(validWordsFound.get(j)))) {
        					
        					validWordsFound.add(validWords.get(i));
        	    			
        					updateStats(guessedWord);
        	    			
        	    			//update GUI words found
        					
        					
        					//checks if the valid word is a pangram to apply scale transition
        					if (isPangram(validWords.get(i))) {
        						fillTxtBoxRainbow();
        						String prevWordsFoundLbl = wordsFoundLbl.getText();
            	    			wordsFoundLbl.setText(validWords.get(i));
            	    			scalePangrams(prevWordsFoundLbl + validWords.get(i) + ", ");
            	    			
        					} else {
        						wordsFoundLbl.setText(wordsFoundLbl.getText() + validWords.get(i) + ", ");
        						
        						//change txtbox background to green
            					fillTxtBoxGreen();
        					}
        	    			
        	    	    	guessFeedbackLbl.setText("");
        	    		
        	    			//exit method
        	    			break;
        	    			
        				}
        			}
    				
    			} else {
    				
    				validWordsFound.add(validWords.get(i));
	    			
    				updateStats(guessedWord);
	    			
    				//change txtbox background to green
					fillTxtBoxGreen();
    				
	    			//update GUI words found
	    			
					//checks if the valid word is a pangram to apply scale transition
					if (isPangram(validWords.get(i))) {
						fillTxtBoxRainbow();
						String prevWordsFoundLbl = wordsFoundLbl.getText();
    	    			wordsFoundLbl.setText(validWords.get(i));
    	    			scalePangrams(prevWordsFoundLbl + validWords.get(i) + ", ");
    	    			
					} else {
						wordsFoundLbl.setText(wordsFoundLbl.getText() + validWords.get(i) + ", ");
						
						//change txtbox background to green
    					fillTxtBoxGreen();
					}
	    			
	    	    	guessFeedbackLbl.setText("");
	    		
	    			//exit method
	    			break;
	    			
    			}
    			
    			break;
    			
    		}
    		
    		//if its the last index of the loop and the guessedWord does not equal any of the valid words
    		else if ((i == validWords.size() - 1) && !(guessedWord.equals(validWords.get(i)))) {
    			
    			rotateTextBox();
    			fillTxtBoxRed();
    			guessFeedbackLbl.setText("Invalid word!");
    			
    		}
    	}
    }

    @FXML
    void newGame(ActionEvent event) throws FileNotFoundException {

    	initialize();
    	
    }

    //exits the GUI
    @FXML
    void quitGame(ActionEvent event) {

    	Platform.exit();
    	
    }

    //Shuffles the letter on the game board (besides the center letter)
    @FXML
    void shuffleLetters(ActionEvent event) {

    	
    	letterButtons = new ArrayList<Button>();
    	
    	List<Button> buttonsAdd = Arrays.asList(letterBtnOne, letterBtnTwo, letterBtnThree, letterBtnFour, letterBtnFive, letterBtnSix);

    	
    	letterButtons.addAll(buttonsAdd);
    	
    	//using collections class to shuffle the letterButtons arrayList
    	Collections.shuffle(letterButtons);
    	
    	//assigns letterButtons values to letters in letters array
    	for (int i = 1; i < allChars.length; i++) {
    		letterButtons.get(i-1).setText(String.valueOf(allChars[i]));
    	}
    	
    }
   
	 
	 //<---------- USER COMPLEXITY STATISTIC METHODS ---------->
    
    //user complexity calculates the average length of guessed words to average length across word bank
    //scores > 1 suggest more smaller words exist, scores < 1 suggest more larger words exist
	 
	 
	 
	 //total used to calculate total length of all valid words. Used to calculate complexity score.
	 public int calcTotalLengthOfValidWords() throws FileNotFoundException {
		 
		 int length = 0;
		 
		 wordList = new File("gameOneWordList.txt");
		 wordScanner = new Scanner(wordList);
		 
		 while(wordScanner.hasNext()) {
			length += wordScanner.next().length();
		 }
		 
		 return length;
	 }
	 
	//method uses to calculate user complexity
	 public String calcComplexity() {
		 
		 double averageLengthOfWordsFound = totalLengthOfWordsFound / (double) validWordsFound.size();
		 double averageLengthOfAllWords = totalLengthOfAllValidWords / (double) validWords.size();
		 
		 double complexityScore = averageLengthOfWordsFound / averageLengthOfAllWords;
		 
		 String formattedComplexity = String.format("%.2f", complexityScore);
		 
		 return formattedComplexity;
		 
	 }
		 
	 //<---------- STATISTIC METHODS ---------->
	 
	 
	 
	 //updates player statistics
	 public void updateStats(String validWord) {
		 
		 
		 
		 
		 if (!isPangram(validWord)) {
			 points += 5;
		 } else {
			 points += 14;
		 }
		 
		 
		 wordsFound++;
		 
		 
		 totalLengthOfWordsFound += validWord.length();
		 
		 
		 statOneLbl.setText("Points: " + points);
		 statThreeLbl.setText("Words Found: " + wordsFound);
		 playerRankLbl.setText("Rank: " + currentRank());
		 statTwoLbl.setText("Points to Next Rank: " + pointsToNextRank());
		 
		 statFourLbl.setText("% Words Found: " + String.format("%.1f", wordsFoundPercent()) + "%");
		 statFiveLbl.setText("Complexity Score: " + calcComplexity());
		 
		 
	 }
	 
	 //method used to determine if a guess is a pangram
	 public boolean isPangram(String word) {
		 
		 for (int i = 0; i < allChars.length; i++) {
			 if (word.contains(String.valueOf(allChars[i]).toLowerCase())) {
				 if (i == allChars.length - 1) {
					 return true;
					 
					 
				 } else {
					 continue;
				 }
				 
			 }
			 else {
				 return false;
			 }
		 }
		 
		 return false;
		 
	 }
	 
	 
	 //divides word found by total words to find % of words found
	 public double wordsFoundPercent() {
		 
		 return (((double) wordsFound / validWords.size()) * 100);
		 
	 }
	 
	 
	 //gets the player's current rank (based on points earned)	 
	 public String currentRank() {
		 
		 for (int i = 1; i < pointsRequiredForTiers.length; i++) {
			 if (pointsRequiredForTiers[i] <= points) {
				 if (points >= 100) {
					 playerRank = "Queen Bee";
				 } if (scaleTransitionPlayed[i] == false) {
					 scaleTierLabel();
					 scaleTransitionPlayed[i] = true;
				 }
				 continue;
			 } else if (pointsRequiredForTiers[i] > points) {
				 playerRank = allTiers[i-1];
				 break;
			 }
		 }
		 
		 return playerRank;
		 
	 }
	 
	 
	 //calculates the number of points needed to reach the next rank
	 public int pointsToNextRank() {
		 
		 int currentIndex = 0;
		 int pointsRemaining = 0;
		 
		 if (points >= 100) {
			 return 0;
		 }
		 
		 //getting index that matches to current rank
		 for (int i = 0; i < allTiers.length; i++) {
			 if (playerRank.equals(allTiers[i])) {
				 currentIndex = i;
				 break;
			 }
		 }
		 
	
		 if (currentIndex < 10) {
			 pointsRemaining = pointsRequiredForTiers[currentIndex+1] - points;
		 }
	 	 
	 	 
	 	 return pointsRemaining;
		 
		 
	 }
	 
	  
	 

}
