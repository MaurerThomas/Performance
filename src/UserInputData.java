import java.sql.Connection;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Wouter on 6/7/2015.
 */
public class UserInputData {
    DatabaseAccessObject dao = new DatabaseAccessObject();
    Random random = new Random();
    public static final int MAX_RUNS = 600;
    public UserInputData(){
        int counter = 0;
        Connection connection = dao.connect();
        long duurtotaal = 0;
        while(counter < MAX_RUNS){
            long beginTijd = System.currentTimeMillis();
            String student = String.format("%07d", random.nextInt(9999999));
            addStudent(student);
            if(random.nextInt(30) == 1){
                addKlas("klas"+random.nextInt(Integer.MAX_VALUE));
            }
            ArrayList<String> klassen = getKlassen();
            if(klassen != null){
                addStudentToKlas(student,klassen.get(random.nextInt(klassen.size())));
            }
            if(random.nextInt(30) == 1){
                String module = "module"+random.nextInt(Integer.MAX_VALUE);
                addModule(module);
                if(klassen != null){
                    for(int i = 0; i < klassen.size(); i++) {
                        if (random.nextInt((int) (100 * 0.15)) == 1) {
                            addKlasToModule(klassen.get(i),module);
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
            System.out.println(counter+" of the "+MAX_RUNS);
            counter++;
        }
        dao.disconnect(connection);
        System.out.println((duurtotaal/MAX_RUNS));
    }

    public void addStudent(String name){
        // insert als niet bestaat
    }
    public void addKlas(String name){
        // check of klas bestaat

        // zo niet maak
    }
    public void addModule(String name){

    }
    public void addKlasToModule(String klas,String module){

    }
    public void addStudentToKlas(String student, String klas){

    }
    public ArrayList<String> getKlassen(){
        return null;
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
