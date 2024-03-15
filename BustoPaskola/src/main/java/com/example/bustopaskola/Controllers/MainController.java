package com.example.bustopaskola.Controllers;

import com.example.bustopaskola.Calculations.AnuitetoPaskola;
import com.example.bustopaskola.Calculations.modelClass;
import com.example.bustopaskola.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.example.bustopaskola.Calculations.LinearPaskola;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class MainController implements Initializable {
    @FXML
    private Label welcomeText;
    @FXML
    private Label warningLabel;
    @FXML
    private TextField textPaskolosSuma;
    @FXML
    private TextField textMetiniaiProcentai;
    @FXML
    private TextField textPaskolosTipas;
    @FXML
    private Button calculateButton;
    @FXML
    private Label metaiLabel;
    @FXML
    private Label menesiaiLabel;
    @FXML
    private Slider metaiSlider;
    @FXML
    private Slider menesiaiSlider;
    @FXML
    private ChoiceBox<String> choicePaskolosTipas;
    @FXML
    private TextField textKiekMen;

    @FXML
    private TextField textNuoMen;
    private String[] tipai = {"Linijinis", "Anuiteto"};
    String paskolosTipas ;
    double paskolosSuma,metiniaiProcentai;
    int metai = 15, menesiai;
    int atidejimoMenKiek = 0, atidejimoNuoMen = 0;




    @FXML
    private TableView<modelClass> tableView;

    @FXML
    private TableColumn<modelClass, Double> columnImoka;

    @FXML
    private TableColumn<modelClass, Double> columnKreditai;

    @FXML
    private TableColumn<modelClass, Integer> columnMen;

    @FXML
    private TableColumn<modelClass, Double> columnNesumok;

    @FXML
    private TableColumn<modelClass, Double> columnPalūkanos;


    public void calculate (ActionEvent event){
        try {
            paskolosSuma = Double.parseDouble(textPaskolosSuma.getText());
            metiniaiProcentai = Double.parseDouble(textMetiniaiProcentai.getText()) * 0.01;
            if (textKiekMen.getText().isEmpty() || textKiekMen.getText().equals("0")) {
                atidejimoMenKiek = 0;
            } else {
                atidejimoMenKiek = Integer.parseInt(textKiekMen.getText());
            }

            if (textNuoMen.getText().isEmpty() || textNuoMen.getText().equals("0")) {
                atidejimoNuoMen = 0;
            } else {
                atidejimoNuoMen = Integer.parseInt(textNuoMen.getText());
            }
            if (paskolosSuma < 0) {
                warningLabel.setText("Įveskite TEIGIAMĄ skaičių į paskolos sumos lauką.");
            } else if (metiniaiProcentai < 0) {
                warningLabel.setText("Įveskite TEIGIAMĄ skaičių į metinių procentų lauką.");
            } else if (metiniaiProcentai > 1) {
                warningLabel.setText("Įveskite skaičių MAŽESNĮ UŽ 100 į metinių procentų lauką.");
            }
            else if (paskolosTipas == null){
                warningLabel.setText("Pasirinkite paskolos tipą.");
            }
                else if(paskolosTipas =="Linijinis"){
                LinearPaskola paskola = new LinearPaskola(this);

                warningLabel.setText("žr. paskolos_rezultatai.txt");
                tableView.setItems(paskola.getResults());
                }
                else if(paskolosTipas =="Anuiteto"){
                AnuitetoPaskola paskola = new AnuitetoPaskola(this);
                warningLabel.setText("žr. paskolos_rezultatai.txt");
                tableView.setItems(paskola.getResults());
                }


        }
        catch(NumberFormatException e){
            warningLabel.setText("Įveskite SKAIČIŲ į paskolos ir paskolos sumos lauką sumos lauką.");
        }
        catch(Exception e){
            warningLabel.setText("ERROR");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        columnMen.setCellValueFactory(new PropertyValueFactory<>("Month"));
        columnImoka .setCellValueFactory(new PropertyValueFactory<>("MonthlyPayment"));
        columnKreditai.setCellValueFactory(new PropertyValueFactory<>("Principal"));
        columnPalūkanos.setCellValueFactory(new PropertyValueFactory<>("Interest"));
        columnNesumok.setCellValueFactory(new PropertyValueFactory<>("RemainingBalance"));

        metaiSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                metai = (int)metaiSlider.getValue();
                metaiLabel.setText(Integer.toString(metai)+ " m.");
            }
        });
        menesiaiSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                menesiai = (int)menesiaiSlider.getValue();
                menesiaiLabel.setText(Integer.toString(menesiai)+ " mėn.");
            }
        });

        choicePaskolosTipas.getItems().addAll(tipai);
        choicePaskolosTipas.setOnAction(this::setPaskolosTipas);


    }

    public void switchToScene2(ActionEvent event) throws IOException {
        calculate(event);
        if (warningLabel.getText() == "žr. paskolos_rezultatai.txt") {
            LinearPaskola data = new LinearPaskola(this);
            Node node = (Node)event.getSource();
            Stage stage = (Stage)node.getScene().getWindow();
            stage.close();

            stage.setUserData(data);
            FXMLLoader fxmlLoaderStage2 = new FXMLLoader(Main.class.getResource("Graph.fxml"));
            try {
                Scene scene2 = new Scene(fxmlLoaderStage2.load(), 600, 400);
                stage.setScene(scene2);
                stage.setTitle("Palyginimas");
                stage.show();
            } catch(IOException e){
                throw new RuntimeException(e);
            }

        }
    }

    public void setPaskolosTipas (ActionEvent event){

        paskolosTipas = choicePaskolosTipas.getValue();
    }


    public double getPaskolosSuma() {
        return paskolosSuma;
    }

    public double getMetiniaiProcentai() {
        return metiniaiProcentai;
    }

    public int getMetai() {
        return metai;
    }

    public int getMenesiai() {
        return menesiai;
    }

    public String getPaskolosTipas() {
        return paskolosTipas;
    }

    public int getAtidejimoMenKiek(){return atidejimoMenKiek;}
    public int getAtidejimoNuoMen(){return atidejimoNuoMen;}
}

