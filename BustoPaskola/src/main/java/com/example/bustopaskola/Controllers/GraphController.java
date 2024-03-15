package com.example.bustopaskola.Controllers;

import com.example.bustopaskola.Calculations.LinearPaskola;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Window;

import java.net.URL;
import java.util.ResourceBundle;

public class GraphController implements Initializable {

    @FXML
    private LineChart<?, ?> lineChart;

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;

    @FXML
    double menesineImoka, menesis;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        XYChart.Series series1 = new XYChart.Series();

        series1.getData().add(new XYChart.Data("1", 544));
        series1.getData()   .add(new XYChart.Data("2", 774));

        lineChart.getData().addAll(series1);

    }

}


