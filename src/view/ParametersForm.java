package view;

import controller.CalculationThread;
import controller.SearchThread;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;


public class ParametersForm {

    private final Group group = new Group();

    private Integer x = 0;
    private Integer rightThreshold;
    private Integer n = 1;

    private final ConcurrentLinkedQueue<Integer> numberQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Integer> wordQueue = new ConcurrentLinkedQueue<>();

    private Thread numberCalculationThread = new Thread();
    private Thread wordCalculationThread = new Thread();


    public ParametersForm(ChartGroup chartGroup, Table table) {

        setNumberTempLine(chartGroup);
        setWordTempLine(chartGroup);

        Label inputRangeFromLabel = new Label("                   x from: ");
        TextField inputLeftThreshold = new TextField();
        HBox leftThresholdBox = new HBox(inputRangeFromLabel, inputLeftThreshold);

        Label inputRangeToLabel = new Label("                       x to: ");
        TextField inputRightThreshold = new TextField();
        HBox rightThresholdBox = new HBox(inputRangeToLabel, inputRightThreshold);

        Button startBuildButton = new Button("    Start    ");
        Button stopBuildButton = new Button("    Stop    ");
        Label spaceLabel = new Label("                          ");
        HBox startStopButtonBox = new HBox(spaceLabel, startBuildButton, stopBuildButton);
        startStopButtonBox.setSpacing(15);

        VBox buttonsGroup = new VBox(leftThresholdBox, rightThresholdBox, startStopButtonBox);
        buttonsGroup.setSpacing(5);

        String book = readBook();

        startBuildButton.setOnAction(actionEvent -> {
            if (!numberCalculationThread.isAlive()) {
                if (integerCheck(inputLeftThreshold.getText())) {
                    x = Integer.parseInt(inputLeftThreshold.getText());

                    if (integerCheck(inputRightThreshold.getText())) {
                        if (Integer.parseInt(inputRightThreshold.getText()) >= x) {
                            rightThreshold = Integer.parseInt(inputRightThreshold.getText());

                            numberQueue.clear();
                            table.clearTable();

                            chartGroup.createNewNumberSeries("x - 8", x, rightThreshold);

                            numberCalculationThread = new Thread(new CalculationThread(x, rightThreshold, numberQueue));

                            numberCalculationThread.start();
                        }
                        else {
                            errorAlert();
                        }
                    }

                }

                if (!wordCalculationThread.isAlive()) {
                    wordQueue.clear();

                    chartGroup.createNewWordSeries(rightThreshold);
                    wordCalculationThread = new Thread(new SearchThread(rightThreshold, wordQueue, table, book));
                    wordCalculationThread.start();
                }
            }
        });

        stopBuildButton.setOnAction(actionEvent -> {
            if (!numberCalculationThread.isInterrupted()) {

                numberCalculationThread.interrupt();
                wordCalculationThread.interrupt();

                numberQueue.clear();
                wordQueue.clear();

                table.clearTable();

                inputLeftThreshold.clear();
                inputRightThreshold.clear();
            }
        });

        group.getChildren().addAll(buttonsGroup);
    }

    public Group getGroup() {
        return group;
    }

    private void errorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Error");
        alert.setHeaderText("Impossible to draw a graphic");
        alert.setContentText("Enter the correct data");

        alert.showAndWait();
    }

    private boolean integerCheck(String text) {

        boolean isInteger = false;

        String numberMatcher = "^-?[0-9]*$";
        if (!text.isEmpty()) {
            if (!text.matches(numberMatcher)) {
                errorAlert();
            } else {
                isInteger = true;
            }
        }

        return isInteger;
    }

    private String readBook() {
        StringBuilder tmpBook = new StringBuilder();
        try(FileInputStream fin = new FileInputStream("test//Brigzz.txt"))
        {
            System.out.printf("File size: %d bytes \n", fin.available());

            int i = -1;
            while((i = fin.read()) != -1){

                tmpBook.append((char) i);
            }
        }
        catch(IOException ex) {

            System.out.println(ex.getMessage());
        }

        return tmpBook.toString();
    }

    private void setNumberTempLine(ChartGroup chartGroup) {
        Timeline numberTempLine = new Timeline();
        numberTempLine.setCycleCount(Timeline.INDEFINITE);
        numberTempLine.getKeyFrames().add(new KeyFrame(Duration.millis(100),
                actionEvent -> {
                    if (!numberQueue.isEmpty()) {
                        chartGroup.updateNumberSeriesList(x, numberQueue.poll());
                        x += 1;
                    }
                }));
        numberTempLine.play();
    }

    private void setWordTempLine(ChartGroup chartGroup) {
        Timeline wordTempLine = new Timeline();
        wordTempLine.setCycleCount(Timeline.INDEFINITE);
        wordTempLine.getKeyFrames().add(new KeyFrame(Duration.millis(100),
                actionEvent -> {
                    if (!wordQueue.isEmpty()) {
                        chartGroup.updateWordSeriesList(n, wordQueue.poll());
                        n += 1;
                    }
                }));
        wordTempLine.play();
    }
}
