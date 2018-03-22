import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by vramouss on 13/03/18.
 */
public class ProjetCapteur {
    static final Scanner input = new Scanner(System.in);
    private static int nbsZone;

    public static void main(String[] args) {
        List<Capteur> capteurs = new ArrayList<Capteur>();

        System.out.println("Entrer 0 pour que vous puissiez entrer les données ou 1 pour lire dans un fichier");
        int nombre = input.nextInt();



        if (nombre == 0){
            capteurs = entrerParLUtilisateur();
            System.out.println("Entrer le nombre de zones à surveiller : ");
            nbsZone = input.nextInt();
        }else if (nombre == 1){
            capteurs = entrerViaFichier("src/fichier_test/petit_test_1");
        }else{
            return;
        }

        //Affiche les configurations elementaires
        List<List<Capteur>> listConfig = afficheConfigurations(capteurs,nbsZone);
        listConfig = trieListe(listConfig);
        for (int i = 0; i < listConfig.size(); i++) {
            for (int j = 0; j < listConfig.get(i).size(); j++) {
                System.out.print(listConfig.get(i).get(j).numeroCapteur+" ");
            }
            System.out.println();
        }
        //Fin d'affichage

        //Remplissage String pour fichier.lp
        String maximize = "Maximize ";
        String variable = "BINARY \n";
        for (int i = 0; i < capteurs.size(); i++) {
            if (i != capteurs.size()-1){
                maximize += "t"+i+" + ";
            }else{
                maximize += "t"+i+"";
            }
            variable+="t"+i+"\n";
        }
        variable+="END";
        //System.out.println(maximize);
        //System.out.println(variable);

        String[] nbLignesAAjouterDansFichier = new String[capteurs.size()];
        String ligneTab = "";
        int numCapteur;
        //Peuplement tableau de String
        for (int i = 0; i < capteurs.size(); i++) {
            numCapteur = capteurs.get(i).numeroCapteur;
            for (int j = 0; j < listConfig.size(); j++) {
                for (int k = 0; k < listConfig.get(j).size(); k++) {
                    if (listConfig.get(j).get(k).numeroCapteur == numCapteur){
                        ligneTab+="t"+j+" ";
                    }
                }
            }
            //System.out.println(ligneTab);
            nbLignesAAjouterDansFichier[i] = ligneTab;
            ligneTab = "";
        }
        //Fin peuplement tableau

        //Ecriture de la ligne correcte pour le fichier.lp
        for (int i = 0; i < nbLignesAAjouterDansFichier.length; i++) {
            String[] tabInter = nbLignesAAjouterDansFichier[i].split(" ");
            String ligneAChanger = "";
            for (int j = 0; j < tabInter.length; j++) {
                if (j != tabInter.length-1){
                    ligneAChanger+=tabInter[j]+"+";
                }else{
                    ligneAChanger+=tabInter[j]+"<="+capteurs.get(i).dureeVie;
                }
            }
            //System.out.println(ligneAChanger);
            nbLignesAAjouterDansFichier[i] = ligneAChanger;
        }
        //Fin écriture

        try {
            ecritDansFichier(variable,maximize,nbLignesAAjouterDansFichier);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Runtime.getRuntime().exec("glpsol -cpxlp fichier.lp -o solution");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void ecritDansFichier(String variable, String maximize, String[] nbLignesAAjouterDansFichier) throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter("src/fichier.lp"),true);
        printWriter.println(maximize);
        printWriter.println();
        printWriter.println("Subject To");
        printWriter.println();
        for (int i = 0; i < nbLignesAAjouterDansFichier.length; i++) {
            printWriter.println(nbLignesAAjouterDansFichier[i]);
        }
        printWriter.println();
        printWriter.println(variable);
        printWriter.close();
    }

    private static List<List<Capteur>> trieListe(List<List<Capteur>> listConfig){
        List<Capteur> listI;
        List<Capteur> listJ;
        int nombreEgalite = 0;

        for (int i = 0; i < listConfig.size(); i++) {
            listI = listConfig.get(i);
            for (int j = 0; j < listConfig.size(); j++) {
                listJ = listConfig.get(j);
                if (i!=j && listI.size() == listJ.size()){
                    for (int k = 0; k < listI.size(); k++) {
                        if (listI.contains(listJ.get(k))){
                            nombreEgalite++;
                        }
                    }
                    if (nombreEgalite == listI.size()){
                        listConfig.remove(j);
                    }
                }
                nombreEgalite = 0;
            }
        }

        return listConfig;
    }

    private static boolean affineConfigElementaires(List<Capteur> listConfig, int nbsZone) {
        for (int i = 0; i < listConfig.size(); i++) {
            for (int j = 0; j < listConfig.size(); j++) {
                if (i != j) {
                    if (listConfig.get(i).getZone().contains(listConfig.get(j).getZone())){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static List<List<Capteur>> afficheConfigurations(List<Capteur> listCapteurs,int nombreZones) {
        List<String> listString = new ArrayList<String>();
        for (int i = 0; i < nombreZones ; i++) {
            listString.add(String.valueOf(i+1));
        }

        List<List<Capteur>> listConfigurations = new ArrayList<List<Capteur>>();

        List<Capteur> listCapteursDansZone = new ArrayList<Capteur>();
        String zone = "";
        String[] tableau;
        int nombreAppartenantATab = 0;
        boolean configurationElem = false;

        for (int i = 0; i <listCapteurs.size() ; i++) {
            for (int j = 0; j < listCapteurs.size(); j++) {
                if (i != j){
                    listCapteursDansZone.add(listCapteurs.get(j));
                    zone += listCapteurs.get(j).getZone();
                    tableau = zone.split(",");
                    for (int k = 0; k < tableau.length; k++) {
                        if (listString.contains(tableau[k])){
                            nombreAppartenantATab++;
                            listString.remove(tableau[k]);
                        }
                    }

                    listString = new ArrayList<String>();
                    for (int y = 0; y < nombreZones ; y++) {
                        listString.add(String.valueOf(y+1));
                    }

                    if (nombreAppartenantATab == nombreZones){
                        if (affineConfigElementaires(listCapteursDansZone,nbsZone)){
                            listConfigurations.add(listCapteursDansZone);
                        }
                        zone = "";
                        nombreAppartenantATab = 0;
                        listCapteursDansZone = new ArrayList<Capteur>();
                        configurationElem = false;
                    }
                    nombreAppartenantATab = 0;
                }
            }
        }

        for (int i = 0; i < listCapteurs.size(); i++) {
            listCapteursDansZone.add(listCapteurs.get(i));
            zone += listCapteurs.get(i).getZone();
            tableau = zone.split(",");
            for (int k = 0; k < tableau.length; k++) {
                if (listString.contains(tableau[k])){
                    nombreAppartenantATab++;
                    listString.remove(tableau[k]);
                }
            }

            listString = new ArrayList<String>();
            for (int y = 0; y < nombreZones ; y++) {
                listString.add(String.valueOf(y+1));
            }

            if (nombreAppartenantATab == nombreZones){
                if (affineConfigElementaires(listCapteursDansZone,nbsZone)){
                    listConfigurations.add(listCapteursDansZone);
                }
                zone = "";
                nombreAppartenantATab = 0;
                listCapteursDansZone = new ArrayList<Capteur>();
                configurationElem = false;
            }
            nombreAppartenantATab = 0;
        }

        return listConfigurations;
    }

    private static List<Capteur> entrerParLUtilisateur() {
        System.out.println("Entrer le nombre de capteurs : ");
        int N = input.nextInt();

        List<Capteur> listCapteur = new ArrayList<Capteur>();

        for (int i = 0; i < N; i++) {
            System.out.println("Donner la durée de vie d'un capteur :");
            int dureeViecapteur = input.nextInt();

            Capteur capteur = new Capteur(dureeViecapteur,i+1);

            System.out.println("Donner les zones a surveiller séparées par une virgule");
            String zone = input.nextLine();
            zone = input.nextLine();

            capteur.addZoneDansListe(zone.split(","));

            listCapteur.add(capteur);
        }

        for (Capteur aListCapteur : listCapteur) {
            System.out.println(aListCapteur.dureeVie + "," + aListCapteur.zoneSurveillee);
        }

        return listCapteur;
    }

    private static List<Capteur> entrerViaFichier(String urlFich) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(urlFich));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        List<Capteur> listCapteur = new ArrayList<Capteur>();
        try {

            String line;
            int nbsCapteur = Integer.parseInt(br.readLine());

            nbsZone = Integer.parseInt(br.readLine());

            String dureeVieCpateur = br.readLine();
            String[] tabDureeVie = dureeVieCpateur.split(" ");

            for (int i = 0; i < nbsCapteur; i++) {
                int dureeVie = Integer.parseInt(tabDureeVie[i]);

                Capteur capteur = new Capteur(dureeVie,i+1);

                String[] numZones = br.readLine().split(" ");

                capteur.addZoneDansListe(numZones);

                listCapteur.add(capteur);
            }

            br.close();


        }catch (IOException e){
            e.printStackTrace();
        }

        for (Capteur aListCapteur : listCapteur) {
            System.out.println(aListCapteur.dureeVie + "," + aListCapteur.zoneSurveillee);
        }

        return listCapteur;
    }
}