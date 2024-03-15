package com.example.bustopaskola.Calculations;

import com.example.bustopaskola.Controllers.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileWriter;
import java.io.IOException;

public class AnuitetoPaskola extends LinearPaskola {
    public AnuitetoPaskola(MainController controller) {
        super(controller);
    }



    public void suskaiciuoti(){
        System.out.println("Anuiteto");
        paskolosTrukme  = metai * 12 + menesiai + atidejimoKiek;

        menesiniaiProcentai = metiniaiProcentai / 12;
        menesineImoka = (paskolosSuma * menesiniaiProcentai * Math.pow(1 + menesiniaiProcentai, paskolosTrukme))
                / (Math.pow(1 + menesiniaiProcentai, paskolosTrukme) - 1);

        try (FileWriter writer = new FileWriter("paskolos_rezultatai.txt")) {
            writer.write("Anuiteto Būsto Paskola \n");
            writer.write(String.format("%-15s", "Mėnesis"));
            writer.write(String.format("%-30s", "Mėnesinė įmoka")) ;
            writer.write(String.format ("%-20s", "Kreditai" ));
            writer.write(String.format("%-30s", "Palūkanos"));
            writer.write("Nesumokėti kreditai\n");
            for (int i = 1; i < paskolosTrukme + 1; i++) {

                kreditai = menesineImoka - palukanos;

                if (i > atidejimoNuo && i <= (atidejimoNuo + atidejimoKiek)) {

                    kreditai = 0;
                }

                nesumoketiKreditai = paskolosSuma - sumoketiKreditai;
                palukanos = nesumoketiKreditai * menesiniaiProcentai;
                sumoketiKreditai += kreditai;


                writer.write(String.format("%-15s", i + " Mėn."));
                writer.write(String.format("%-30s", String.format("%.2f", menesineImoka)));
                writer.write(String.format("%-20s", String.format("%.2f", kreditai)));
                writer.write(String.format("%-30s", String.format("%.2f", palukanos)));
                writer.write(String.format("%.2f", nesumoketiKreditai));

                isVisoPermoketa += palukanos;

                writer.write("\n");
                double formattedMenesineImoka = Double.parseDouble(String.format("%.2f", menesineImoka));
                double formattedKreditai = Double.parseDouble(String.format("%.2f", kreditai));
                double formattedPalukanos = Double.parseDouble(String.format("%.2f", palukanos));
                double formattedNesumoketiKreditai = Double.parseDouble(String.format("%.2f", nesumoketiKreditai));

                modelClass result = new modelClass(i, formattedMenesineImoka, formattedKreditai, formattedPalukanos, formattedNesumoketiKreditai);
                // modelClass result = new modelClass(i,menesineImoka,kreditai,palukanos,nesumoketiKreditai);
                getResults().add(result);
            }

            writer.write("Papildomai sumokėta:" +isVisoPermoketa);


            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
