import java.util.ArrayList;
import java.util.List;

/**
 * Created by vramouss on 13/03/18.
 */
public class Capteur {
    int numeroCapteur;
    List<Integer> zoneSurveillee;
    int dureeVie;

    public Capteur(int _dureeVie, int _numeroCapteur){
        numeroCapteur = _numeroCapteur;
        zoneSurveillee = new ArrayList<Integer>();
        dureeVie = _dureeVie;
    }

    public void addZoneDansListe(String[] zoneSureveilleParCapteur){
        for (int i = 0; i < zoneSureveilleParCapteur.length ; i++) {
            zoneSurveillee.add(Integer.parseInt(zoneSureveilleParCapteur[i]));
        }
    }

    public String getZone() {
        String zone = "";
        for (int i = 0; i < zoneSurveillee.size(); i++) {
           zone += zoneSurveillee.get(i)+",";
        }

        return zone;
    }

    public List<Integer> getListeZone() {
        return zoneSurveillee;
    }
}
