package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ChartGroup {

    private ObservableList<XYChart.Data<Integer, Integer>> numberSeriesData = FXCollections.observableArrayList();
    private ObservableList<XYChart.Data<Integer, Integer>> wordSeriesData = FXCollections.observableArrayList();

    private final ObservableList<XYChart.Series<Integer, Integer>> seriesList = FXCollections.observableArrayList();

    private final NumberAxis xAxis = new NumberAxis("x", 0, 50, 2);
    private final NumberAxis yAxis = new NumberAxis("y", 0, 100, 5);

    private final LineChart chart = new LineChart(xAxis, yAxis, seriesList);
    private final Group chartGroup = new Group();

    private double mouseOnChartX;
    private double mouseOnChartY;

    private int zoom = 0;

    public ChartGroup() {

        chart.setLayoutY(20);

        Button zoomIn = new Button(" + ");
        zoomIn.setOnAction(actionEvent -> doZoomIn());

        Button zoomOut = new Button(" - ");
        zoomOut.setOnAction(actionEvent -> doZoomOut());

        HBox zoomBox = new HBox(zoomIn, zoomOut);
        zoomBox.setLayoutX(420);
        zoomBox.setLayoutY(500);
        zoomBox.setSpacing(5);

        setChartPressAndDragEvents();

        chartGroup.getChildren().addAll(chart, zoomBox);
    }

    public Group getChartGroup() {
        return chartGroup;
    }

    public void updateNumberSeriesList(Integer x, Integer y) {
        numberSeriesData.add(new XYChart.Data<>(x, y));
    }

    public void updateWordSeriesList(Integer x, Integer y) {
        wordSeriesData.add(new XYChart.Data<>(x, y));
    }

    public void createNewNumberSeries(String fx, Integer leftThreshold, Integer rightThreshold) {
        String seriesName = "f(x) = " + fx + ", x ∈ [" + leftThreshold + "; " + rightThreshold + "]";

        numberSeriesData = FXCollections.observableArrayList();
        XYChart.Series<Integer, Integer> series = new XYChart.Series<>(seriesName, numberSeriesData);
        seriesList.add(series);
    }

    public void createNewWordSeries(Integer number) {
        String seriesName = "f(x) = word search time, x ∈ [1; " + number + "]";

        wordSeriesData = FXCollections.observableArrayList();
        XYChart.Series<Integer, Integer> series = new XYChart.Series<>(seriesName, wordSeriesData);
        seriesList.add(series);
    }

    private void doZoomIn() {
        if (zoom < 4) {
            xAxis.setLowerBound(xAxis.getLowerBound() + xAxis.getTickUnit());
            xAxis.setUpperBound(xAxis.getUpperBound() - xAxis.getTickUnit());
            yAxis.setLowerBound(yAxis.getLowerBound() + yAxis.getTickUnit());
            yAxis.setUpperBound(yAxis.getUpperBound() - yAxis.getTickUnit());
            zoom += 1;
        }
    }

    private void doZoomOut() {
        xAxis.setLowerBound(xAxis.getLowerBound() - xAxis.getTickUnit());
        xAxis.setUpperBound(xAxis.getUpperBound() + xAxis.getTickUnit());
        yAxis.setLowerBound(yAxis.getLowerBound() - yAxis.getTickUnit());
        yAxis.setUpperBound(yAxis.getUpperBound() + yAxis.getTickUnit());
        zoom -= 1;
    }

    public void setChartScroll() {
        chart.setOnScroll(scrollEvent -> {
            if(scrollEvent.getDeltaY() > 0) {
                doZoomIn();
            }
            if (scrollEvent.getDeltaY() < 0) {
                doZoomOut();
            }
        });
    }

    public void clearChartScroll() {
        chart.setOnScroll(scrollEvent -> {});
    }

    private void setChartPressAndDragEvents() {

        chart.setOnMousePressed(mouseEvent -> {
            mouseOnChartX = mouseEvent.getX();
            mouseOnChartY = mouseEvent.getY();
        });

        chart.setOnMouseDragged(mouseEvent -> {

            xAxis.setLowerBound(xAxis.getLowerBound() + (mouseOnChartX - mouseEvent.getX()) / 1);
            xAxis.setUpperBound(xAxis.getUpperBound() + (mouseOnChartX - mouseEvent.getX()) / 1);
            mouseOnChartX = mouseEvent.getX();

            yAxis.setLowerBound(yAxis.getLowerBound() + (mouseEvent.getY() - mouseOnChartY) * 1);
            yAxis.setUpperBound(yAxis.getUpperBound() + (mouseEvent.getY() - mouseOnChartY) * 1);
            mouseOnChartY = mouseEvent.getY();

        });

    }

}
