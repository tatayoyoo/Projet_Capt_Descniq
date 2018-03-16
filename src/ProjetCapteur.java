import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by vramouss on 13/03/18.
 */
public class ProjetCapteur {
    static final Scanner input = new Scanner(System.in);
    public static void main(String[] args) {
        List<Capteur> capteurs = new ArrayList<Capteur>();

        System.out.println("Entrer 0 pour que vous puissiez entrer les données ou 1 pour lire dans un fichier");
        int nombre = input.nextInt();

        int M =0;

        if (nombre == 0){
            capteurs = entrerParLUtilisateur();
            System.out.println("Entrer le nombre de zones à surveiller : ");
            M = input.nextInt();
        }else if (nombre == 1){
            capteurs = entrerViaFichier();
        }else{
            return;
        }

        List<List<Capteur>> listConfig = afficheConfigurations(capteurs,M);
        for (int i = 0; i < listConfig.size(); i++) {
            for (int j = 0; j < listConfig.get(i).size(); j++) {
                System.out.print(listConfig.get(i).get(j)+" ");
            }
            System.out.println();
        }
    }

    private static void afficheConfigurationsElem(List<List<Capteur>> listConfig, int nombreZones) {
        List<String> listString = new ArrayList<String>();
        for (int i = 0; i < nombreZones ; i++) {
            listString.add(String.valueOf(i + 1));
        }

        //Recuperer listConfig.get(1)
        //Supprimer le premier element puis parcourir la liste et voir si tous les capteurs couvrent toutes les zones
        //Si oui --> configurations non elementaires
        //Si non --> remet la liste en place puis suppression second element et on regarde si tous les capteurs couvrent les zones
        //etc .....
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
                        for (int k = 0; k < listCapteursDansZone.size(); k++) {
                            System.out.print("Capteur numéro : "+listCapteursDansZone.get(k).numeroCapteur+"+");
                        }
                        listConfigurations.add(listCapteursDansZone);
                        System.out.println();
                        zone = "";
                        nombreAppartenantATab = 0;
                        listCapteursDansZone = new ArrayList<Capteur>();
                        configurationElem = false;
                    }
                    nombreAppartenantATab = 0;
                }
            }
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

    private static List<Capteur> entrerViaFichier() {
        return new ArrayList<Capteur>();
    }
}

//AfficheConfigurationsValides
/*List<String> listCapteursCouvrantZones = new ArrayList<String>();
        List<List<Capteur>> listCapteursZone = new ArrayList<List<Capteur>>();
        List<Capteur> capteursDansZone = new ArrayList<Capteur>();
        String zone = "";
        for (int i = 0; i < listCapteurs.size(); i++) {
            for (int j = 0; j <listCapteurs.size(); j++) {
                zone+=listCapteurs.get(j).getZone();
                System.out.println(zone);
                if (!(listCapteursCouvrantZones.contains(zone))){
                    listCapteursCouvrantZones.add(zone);
                }
                capteursDansZone.add(listCapteurs.get(j));
                if (zone.split(",").length == tab.length){
                    listCapteursCouvrantZones.add(zone);
                    listCapteursZone.add(capteursDansZone);
                    System.out.println(listCapteurs.get(j));
                    break;
                }
            }
            zone = "";
            capteursDansZone = new ArrayList<Capteur>();
        }

        for (int i = 0; i < listCapteursCouvrantZones.size(); i++) {
            System.out.println(listCapteursZone.get(i));
        }*/