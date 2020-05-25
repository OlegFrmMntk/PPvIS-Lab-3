package view;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainWindow extends Application {

    private Scene scene;
    private ChartGroup chartGroupObject;

    @Override
    public void start(Stage primaryStage) throws Exception{

        BorderPane root = getWindowLayout();
        primaryStage.setTitle("Function graphing");
        scene = new Scene(root, 830, 520);

        setSceneKeyPressEvents();
        primaryStage.setScene(scene);

        primaryStage.show();

    }

    private BorderPane getWindowLayout() {

        BorderPane windowLayout = new BorderPane();


        Table table = new Table();

        Group tableGroup = table.getGroup();
        HBox tableBox = new HBox(new Label("        "), tableGroup);

        chartGroupObject = new ChartGroup();
        Group chartGroup = chartGroupObject.getChartGroup();

        ParametersForm parametersForm = new ParametersForm(chartGroupObject, table);
        Group parametersFormGroup = parametersForm.getGroup();

        VBox generalGroup = new VBox();
        generalGroup.setSpacing(20);
        generalGroup.getChildren().addAll(tableBox, parametersFormGroup);

        windowLayout.setLeft(generalGroup);
        windowLayout.setRight(chartGroup);

        return windowLayout;

    }

    public void setSceneKeyPressEvents() {
        scene.setOnKeyPressed(ke -> {
            KeyCode keyCode = ke.getCode();
            if (keyCode.equals(KeyCode.CONTROL)) {
                chartGroupObject.setChartScroll();
            }
        });
        scene.setOnKeyReleased(ke -> {
            KeyCode keyCode = ke.getCode();
            if (keyCode.equals(KeyCode.CONTROL)) {
                chartGroupObject.clearChartScroll();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
