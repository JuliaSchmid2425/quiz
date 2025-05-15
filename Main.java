//que torni a preguntar quantes preguntes vol fer a la segona repeticio del loop


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;
import java.util.Random;

public class Main {
    public static void main(String[] args) {

        String[] questions = new String[20];
        String[] answerA = new String[20];
        String[] answerB = new String[20];
        String[] answerC = new String[20];
        String[] correctAnswers = new String[20];
        String username;
        int questionQuantity = 0;
        int score = 0;
        char chosenQuiz = ' ';
        boolean playAgain = true;
        double scorePercentage = 0;

        presentGame();
        username = askUsername();

        do{
            chosenQuiz = chooseQuiz(chosenQuiz, score, questions, answerA, answerB, answerC, correctAnswers, questionQuantity);
            questionQuantity = chooseQuestionQuantity(questionQuantity);
            showQuestions(questions, answerA, answerB, answerC, correctAnswers, score, questionQuantity);
            score: checkAnswer (score, answerOption);
            showScoreMessage(score, questionQuantity, scorePercentage);
            writeStats(username, score, questionQuantity, chosenQuiz);
            playAgain = askPlayAgain(playAgain);
        }while(playAgain);

        System.out.println("\nThank you for playing! Have a nice day :] Byeeee !!!");
    }

    private static void presentGame(){
        System.out.println("\n\nThis is a multiple choice quiz.");
        System.out.println("Each question will be presented with 3 possible answers, of which only one will be correct.");
        System.out.println("If you get 3 consecutive questions wrong you'll loose the game.");
        System.out.println("You only need to type the letter the answer corresponds to.");
    }

    private static String askUsername(){
        Scanner sc = new Scanner(System.in);
        String username;

        System.out.print("But first... Please choose a username: ");
        username = sc.nextLine();

        return username;
    }

    private static char chooseQuiz(char chosenQuiz, int score, String[] questions, String[] answerA, String[] answerB, String[] answerC, String[] correctAnswers, int questionQuantity) {
        Scanner sc = new Scanner(System.in);

        String empty;

        System.out.println("\nWhat quiz do you want to answer? \n  1-Cat diet and health \n  2-What to take into account when you just adopted a dog ");

        chosenQuiz = ' ';       //sempre estigui buit al principi de la partida

        while (chosenQuiz != '1' && chosenQuiz != '2') {
            System.out.println("Please type either 1 or 2");
            chosenQuiz = sc.next().trim().charAt(0);
            empty = sc.nextLine();   //guardar qualsevol cosa que s'hagi quedat al buffer
        }
        if (chosenQuiz =='1' || chosenQuiz  =='2'){
            sortTextFile(questions, answerA, answerB, answerC, correctAnswers, chosenQuiz);
            score = showQuestionsAndCheckAnswer(questions, answerA, answerB, answerC, correctAnswers, score, questionQuantity);
        } else{
            System.out.println("\nSeems like something went wrong... Please enter either be 1 or 2");
        }
        return chosenQuiz;
    }

    private static int checkAnswer (int score) {
        Scanner sc = new Scanner(System.in);

        score = 0;
        int wrongScore = 0;

        //en cas de resposta correcta
        if (answerOption.equalsIgnoreCase(correctAnswers[indexSelectedQuestions])) {
            score++;
            wrongScore = 0;
            System.out.println("Congrats:) You got it right!! \n\nYou've got " + score + " question(s) right\n");
        }
        //en cas de resposta incorrecta
        else if ("A".equalsIgnoreCase(answerOption) || "B".equalsIgnoreCase(answerOption) || "C".equalsIgnoreCase(answerOption)) {
            wrongScore++;
            System.out.println("Aww... Seems like you got it wrong:(\nThe correct answer would've been " +
                    correctAnswers[indexSelectedQuestions] + "\n\n You've got " + score + " question(s) right\n " +
                    "You've got " + wrongScore + " question(s) wrong in a row\n");
        } else System.out.println("Please enter either A, B or C! \n");

        //en cas d'equivocar-se en 3 preguntes seguides
        if (wrongScore == 3) {
            System.out.println("GAME OVER!! \nYou got 3 in a row wrong.");
            return -1;      //per que el loop del main sàpiga que no s'ha de calcular la nota
        }
        retun score;
    }

    //guardar preguntes i respostes en arrays corresponents
    private static void sortTextFile(String[] questions, String[] answerA, String[] answerB, String[] answerC, String[] correctAnswers, char chosenQuiz) {

        BufferedReader br = null;

        int lineCounter = 1;
        String line;

        try {
            br = new BufferedReader(new FileReader("src/resources/question_set" + chosenQuiz + ".txt"));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        //sap quina línia va a quin array per la resta de la divisió
        try {
            while ((line = br.readLine()) != null) {
                if (lineCounter % 5 == 1) {
                    questions[lineCounter / 5] = line;
                }
                else if (lineCounter % 5 == 2) {
                    answerA[lineCounter / 5] = line;
                }
                else if (lineCounter % 5 == 3) {
                    answerB[lineCounter / 5] = line;
                }
                else if (lineCounter % 5 == 4) {
                    answerC[lineCounter / 5] = line;
                }
                else if (lineCounter % 5 == 0) {
                    correctAnswers[(lineCounter / 5) - 1] = line;
                }
                lineCounter++;
            }
        }
        catch (IOException e) {
            System.out.println("Error when reading file");
        }
    }

    private static int chooseQuestionQuantity(int questionQuantity){
        Scanner sc = new Scanner(System.in);
        String questionQuantityStr;
        boolean exitLoop = false;

        do {
            System.out.println("How many questions would you like to answer? The minimum is 5 and maximum is 20");
            questionQuantityStr = sc.nextLine().trim();

            //només accepti 5-20 com a resposta
            if (questionQuantityStr.matches("\\d+")) {     //mira que sigui un número
                questionQuantity = Integer.parseInt(questionQuantityStr);
                if (questionQuantity >= 5 && questionQuantity <= 20) {
                    exitLoop = true;
                } else {
                    System.out.println("\nOops... Please enter a number between 5 and 20");
                }
            } else {
                System.out.println("\nOops... Please enter a number between 5 and 20");
            }
            while (!exitLoop) ;
        }
        return questionQuantity;
    }

    private static String showQuestions (String[] questions, String[] answerA, String[] answerB, String[] answerC)

        int[] selectedQuestions = randomQuestionSelector(questionQuantity);
        int indexSelectedQuestions;
        String answerOption = "";               //reiniciar al principi de cada partida

        for (int i = 0; i < selectedQuestions.length; i++) {
            indexSelectedQuestions = selectedQuestions[i];
            answerOption = "";    //reiniciar cada vegada que es repeteixi el loop

            while ((!"A".equalsIgnoreCase(answerOption) && !"B".equalsIgnoreCase(answerOption) && !"C".equalsIgnoreCase(answerOption))) {
                //ensenyar les preguntes i possibles respostes
                System.out.print((i + 1) + "- "); //mostrar per quina pregunta es va
                System.out.println(questions[indexSelectedQuestions]);
                System.out.println(answerA[indexSelectedQuestions]);
                System.out.println(answerB[indexSelectedQuestions]);
                System.out.println(answerC[indexSelectedQuestions]);
                answerOption = sc.nextLine().trim();
        }
        return answerOption;
    }

    //crear array amb quantitat escollida pel jugador de números aleatòris sense repeticions
    private static int[] randomQuestionSelector(int questionQuantity) {

        Random random = new Random();
        int[] totalQuestions = new int[20];
        int[] randomizedQuestions = new int[questionQuantity];

        //omplir array 0-19
        for (int i = 0; i < totalQuestions.length; i++) {
            totalQuestions[i] = i;
        }

        //intercanviar elements en ordre, per elements en ordre aleatòri
        for (int i = totalQuestions.length - 1; i >= 0; i--) {
            int j = random.nextInt(i + 1);
            swap(totalQuestions, i, j);
        }

        //agafar només x primeres posicions i guardar a nou array
        System.arraycopy(totalQuestions, 0, randomizedQuestions, 0, questionQuantity);
        return randomizedQuestions;
    }

    //canviar ordre de les variables
    private static void swap (int[] totalQuestions, int i, int j) {

        int temporary = totalQuestions[i];
        totalQuestions[i] = totalQuestions[j];
        totalQuestions[j] = temporary;
    }

    private static void showScoreMessage(int score, int questionQuantity, double scorePercentage) {
        double finalScore;
        String finalMessage1 = "\nUh... You're not ready to get a pet yet, but keep studying!";
        String finalMessage2 = "\nYou're good at it but keep learning!!";
        String finalMessage3 = "\nWow! You know all the basics about owning a pet!";
        String finalMessage4 = "\nWOWOWOWOW You got all right!! Congrats!!!";

        //ensenyar finalMessage només si no ha perdut 3 vegades seguides
        if (score != -1) {
            scorePercentage = calculateScorePercentage(score, questionQuantity);

            if (scorePercentage <= 33) {
                System.out.println(finalMessage1);
            } else if (scorePercentage <= 66) {
                System.out.println(finalMessage2);
            } else if (scorePercentage <= 99) {
                System.out.println(finalMessage3);
            } else {
                System.out.println(finalMessage4);
            }
            System.out.println("\nYour final score is: " + String.format("%.2f", scorePercentage) + "%");    //per que la nota tingui 2 decimals
            System.out.println("You got " + score + " questions correctly and " + (questionQuantity - score) + " questions incorrectly out of the " + " questions you chose at the begging.");
        }
    }

    //calcular nota final
    private static double calculateScorePercentage(int score, int questionQuantity) {

        //passar int a double per que la nota tingui 2 decimals
        double scoreDouble = Integer.valueOf(score);
        double questionQuantityDouble = Integer.valueOf(questionQuantity);

        double scorePercentage = scoreDouble * 100 / questionQuantityDouble;

        return scorePercentage;
    }

    private static void writeStats(String username, int score, int questionQuantity, char chosenQuiz){
        BufferedWriter bw = null;
        //per enregistrar la data i hora
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd-HH:mm:ss").format(Calendar.getInstance().getTime());

        try {
            bw = new BufferedWriter(new FileWriter("src/resources/player_stats" + chosenQuiz + ".txt", true));    //no es sobreescriguin les dades

            bw.write("Username: " + username );
            bw.newLine();
            bw.write("DateTime game ends: " + timeStamp );
            bw.newLine();
            if (score != -1) {
                bw.write("Correct answers: " + score);
                bw.newLine();
                bw.write("Incorrect answers: " + (questionQuantity - score));
                bw.newLine();
            }
            //en cas d'haver perdut per 3 errors seguits
            else {
                bw.write("Lost after 3 consecutive errors");
                bw.newLine();
            }
            //separar entre jugadors
            bw.write(("_").repeat(30));
            bw.newLine();
        }
        catch(FileNotFoundException e) {
            System.out.println("File Not Found");
        }
        catch(IOException e) {
            System.out.println("Error when writing file");
        }
        //tancar el fitxer quan s'hagi acabat
        finally {
            try {
                bw.close();
            }
            catch (IOException e) {
                System.out.println("Error when closing file");
            }
        }
    }

    private static boolean askPlayAgain(boolean playAgain) {
        Scanner sc = new Scanner(System.in);

        char playAgainAnswer = ' ';
        String empty;

        System.out.print("\nWould you like to play again? ");

        //només accepta yes/no com a resposta
        while (playAgainAnswer != 'N' && playAgainAnswer != 'Y' && playAgainAnswer != 'y' && playAgainAnswer != 'n') {
            System.out.println("Please only answer with yes or no");
            playAgainAnswer = sc.next().trim().charAt(0);
            empty = sc.nextLine();      //guardar qualsevol cosa que s'hagi quedat al buffer
            playAgainAnswer = Character.toUpperCase(playAgainAnswer);

            if (playAgainAnswer == 'Y') {
                System.out.println("Sure!! Let's start another game!");
                playAgain = true;
            }
            else if (playAgainAnswer == 'N') {
                playAgain = false;
            }
        }
        return playAgain;
        return playAgain;
    }
}