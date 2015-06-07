import java.sql.Connection;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Thomas on 7-6-2015.
 */
public class UserRequestData {


    DatabaseAccessObject dao = new DatabaseAccessObject();
    public static final int MAX_RUNS = 600;


    public static void main(String[] args) {
        startThread("1");
        startThread("2");


    }

    public static void startThread(String name) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new UserRequestData();
            }
        }, name).start();
    }

    public UserRequestData() {
        System.out.println("---------------------------------------------");
        Connection connection = dao.connect();
        int counter = 0;
        float duurtotaal = 0;
        while (counter < MAX_RUNS) {
            String[] student = getRandomStudent(connection);
            long beginTijd = System.currentTimeMillis();
            ArrayList<String> list = getStudentModules(student[0],student[1],connection);

            // Doe je ding


            dao.commit(connection);
            long eindTijd = System.currentTimeMillis();
            long duurInMS = eindTijd - beginTijd;
            duurtotaal += duurInMS;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println((counter + 1) + " of the " + MAX_RUNS);
            counter++;
        }
        dao.disconnect(connection);
        System.out.println((duurtotaal / MAX_RUNS));
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++");
    }


    public void getAllStudents(){
        String query = "Select voornaam, achternaam FROM studenten LIMIT 1";

    }
    public String[] getRandomStudent(Connection connection){
        String query = "Select voornaam,achternaam FROM studenten";
        ArrayList<String[]> list = dao.readMultiString(query, connection);
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));

    }

    public ArrayList<String> getStudentModules(String voornaam, String achternaam, Connection connection){
        String query = "Select modulecode " +
                "From modules " +
                "Join module_klassen ON modules.modulecode = module_klassen.module " +
                "Join student_klassen ON module_klassen.klas = student_klassen.klas " +
                "Join studenten ON student_klassen.student = studenten.studentnummer " +
                "WHERE voornaam = '"+voornaam+"' AND achternaam = '"+achternaam+"';";
        return dao.readString(query,connection);
    }


}



