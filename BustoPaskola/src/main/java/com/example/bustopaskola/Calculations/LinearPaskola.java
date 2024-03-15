package com.example.bustopaskola.Calculations;
import com.example.bustopaskola.Controllers.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileWriter;
import java.io.IOException;

public class LinearPaskola {

    String paskolosTipas;
    double paskolosSuma, metiniaiProcentai;
    int metai, menesiai;
    int paskolosTrukme;
    double nesumoketiKreditai, sumoketiKreditai = 0 ,kreditai;
    double  menesineImoka;
    double menesiniaiProcentai;
    double palukanos;
    double isVisoPermoketa = 0;

    int atidejimoKiek, atidejimoNuo;

    private ObservableList<modelClass> results = FXCollections.observableArrayList();

    public ObservableList<modelClass> getResults() {
        return results;
    }

    MainController data;



    public LinearPaskola(MainController controller) {

        this.paskolosTipas = controller.getPaskolosTipas();
        this.paskolosSuma = controller.getPaskolosSuma();
        this.metiniaiProcentai = controller.getMetiniaiProcentai();
        this.metai = controller.getMetai();
        this.menesiai = controller.getMenesiai();
        this.atidejimoNuo = controller.getAtidejimoNuoMen();
        this.atidejimoKiek = controller.getAtidejimoMenKiek();

        suskaiciuoti();

    }

    public void suskaiciuoti(){
        System.out.println("Linijine");
        paskolosTrukme  = metai * 12 + menesiai + atidejimoKiek;

        try (FileWriter writer = new FileWriter("paskolos_rezultatai.txt")) {
            writer.write("Linijinė Būsto Paskola \n");
            writer.write(String.format("%-15s", "Mėnesis"));
            writer.write(String.format("%-30s", "Mėnesinė įmoka")) ;
            writer.write(String.format ("%-20s", "Kreditai" ));
            writer.write(String.format("%-30s", "Palūkanos"));
            writer.write("Nesumokėti kreditai\n");
            for (int i = 1; i < paskolosTrukme + 1; i++) {

                kreditai = paskolosSuma / paskolosTrukme;

                if (i > atidejimoNuo && i <= (atidejimoNuo + atidejimoKiek)) {

                    kreditai = 0;
                }

                nesumoketiKreditai = paskolosSuma - sumoketiKreditai;
                palukanos = nesumoketiKreditai* metiniaiProcentai /12;
                menesineImoka = kreditai + palukanos;
                sumoketiKreditai += kreditai;
                isVisoPermoketa += palukanos;



                writer.write(String.format("%-15s", i + " Mėn."));
                writer.write(String.format("%-30s", String.format("%.2f", menesineImoka)));
                writer.write(String.format("%-20s", String.format("%.2f", kreditai)));
                writer.write(String.format("%-30s", String.format("%.2f", palukanos)));
                writer.write(String.format("%.2f", nesumoketiKreditai));

                writer.write("\n");
                double formattedMenesineImoka = Double.parseDouble(String.format("%.2f", menesineImoka));
                double formattedKreditai = Double.parseDouble(String.format("%.2f", kreditai));
                double formattedPalukanos = Double.parseDouble(String.format("%.2f", palukanos));
                double formattedNesumoketiKreditai = Double.parseDouble(String.format("%.2f", nesumoketiKreditai));

                modelClass result = new modelClass(i, formattedMenesineImoka, formattedKreditai, formattedPalukanos, formattedNesumoketiKreditai);
               // modelClass result = new modelClass(i,menesineImoka,kreditai,palukanos,nesumoketiKreditai);
            results.add(result);
            }
            writer.write("Papildomai sumoketa:"+ isVisoPermoketa);

        writer.close();

         }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

}
