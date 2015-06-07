import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Random;

/**
 * Created by Wouter on 6/7/2015.
 */
public class UserInputData {
    DatabaseAccessObject dao = new DatabaseAccessObject();
    Random random = new Random();
    public static final int MAX_RUNS = 600;
    public static ArrayList<String> klassen = new ArrayList<String>();
    public UserInputData(){
        int counter = 0;
        Connection connection = dao.connect();
        long duurtotaal = 0;
        while(counter < MAX_RUNS){
            long beginTijd = System.currentTimeMillis();
            String student = String.format("%07d", random.nextInt(9999999));
            addStudent(student,connection);
            if(random.nextInt(30) == 1){
                String klas = "klas"+random.nextInt(Integer.MAX_VALUE);
                addKlas(klas,connection);
            }
            if(klassen != null){
                addStudentToKlas(student,klassen.get(random.nextInt(klassen.size())),connection);
            }
            if(random.nextInt(30) == 1){
                String module = "module"+random.nextInt(Integer.MAX_VALUE);
                addModule(module,connection);
                if(klassen != null){
                    for(int i = 0; i < klassen.size(); i++) {
                        if (random.nextInt((int) (100 * 0.15)) == 1) {
                            addKlasToModule(klassen.get(i),module,connection);
                        }
                    }
                }
            }
            dao.commit(connection);
            long eindTijd = System.currentTimeMillis();
            long duurInMS = eindTijd - beginTijd;
            duurtotaal += duurInMS;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println((counter+1)+" of the "+MAX_RUNS);
            counter++;
        }
        dao.disconnect(connection);
        System.out.println((duurtotaal/MAX_RUNS));
    }

    public void addStudent(String name,Connection connection){
        String query = "Insert into";
        // insert als niet bestaat
    }
    public void addKlas(String name,Connection connection){
        // check of klas bestaat
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //get current date time with Date()
        Date date = new Date();
        String query = "Insert into klassen(naam, startdatum,einddatum) VALUES("+name+","+0+","+dateFormat.format(date)+")";
        if(searchList(name,klassen)){
            klassen.add(name);
            dao.update(query,connection);
        }

        // zo niet maak
    }
    public boolean searchList(String x, ArrayList<String> list){
        for(int i =0; i<list.size();i++){
            if(list.get(i).equals(x)){
                return false;
            }
        }
        return true;

    }
    public void addModule(String name,Connection connection){

    }
    public void addKlasToModule(String klas,String module,Connection connection){

    }
    public void addStudentToKlas(String student, String klas,Connection connection){

    }
    public ArrayList<String> getKlassen(Connection connection){
        String query = "Select naam From klassen";

        ArrayList<String> aantal = dao.readString(query, connection);
        return aantal;
    }

    public static void main(String[] args) {
        startThread("1");
        startThread("2");
        startThread("3");
    }
    public static void startThread(String name){
        new Thread(new Runnable() {
            @Override
            public void run() {
                new UserInputData();
            }
        }, name).start();
    }

}
