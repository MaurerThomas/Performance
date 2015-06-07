import java.sql.Connection;

/**
 * Created by Thomas on 7-6-2015.
 */
public class UserRequestData {


    DatabaseAccessObject dao = new DatabaseAccessObject();
    public static final int MAX_RUNS = 600;

    Connection connection = dao.connect();
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
        getAllStudents();

        int counter = 0;
        Connection connection = dao.connect();
        long duurtotaal = 0;
        while (counter < MAX_RUNS) {
            long beginTijd = System.currentTimeMillis();


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
    }


    public void getAllStudents(){
        String query = "Select voornaam, achternaam FROM studenten LIMIT 1";

    }

    public void getStudentModules(String voornaam, String achternaam){
        String query = "";

    }


}



