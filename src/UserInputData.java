import java.sql.Connection;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Wouter on 6/7/2015.
 */
public class UserInputData {
    DatabaseAccessObject dao = new DatabaseAccessObject();

    public static final int MAX_RUNS = 600;
    public static ArrayList<String> klassen = new ArrayList<String>();
    public UserInputData(){
        int counter = 0;
        Connection connection = dao.connect();
        long duurtotaal = 0;
        while(counter < MAX_RUNS){
            long beginTijd = System.currentTimeMillis();
            String voornaam =  ThreadLocalRandom.current().nextInt(9999999)+"";
            String studentNummer = String.format("%07d",ThreadLocalRandom.current().nextInt(9999999));
            StringBuilder nummer = new StringBuilder(studentNummer);
            nummer.setCharAt(0, '1');
            System.err.println(studentNummer);
            String achternaam = ThreadLocalRandom.current().nextInt(9999999)+"";
            addStudent(nummer,voornaam,achternaam,connection);
            if(ThreadLocalRandom.current().nextInt(30) == 1){
                String klas = "KLAS"+ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
                addKlas(klas,connection);
            }
            if(klassen.size() > 0){
                addStudentToKlas(nummer,klassen.get(ThreadLocalRandom.current().nextInt(klassen.size())),connection);
            }
            if(ThreadLocalRandom.current().nextInt(30) == 1){
                String module = "module"+ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
                addModule(module,connection);
                if(klassen.size() > 0){
                    for (String aKlassen : klassen) {
                        if (ThreadLocalRandom.current().nextInt((int) (100 * 0.15)) == 1) {
                            addKlasToModule(aKlassen, module, connection);
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

    public void addStudent(StringBuilder nummer,String achternaam, String name,Connection connection){
        String query = "Insert into studenten(studentnummer, voornaam, achternaam) VALUES ('"+nummer+"','"+name+"','"+achternaam+"')";
        dao.update(query,connection);

    }
    public void addKlas(String name,Connection connection){
        String query = "Insert into klassen(naam) VALUES('"+name+"')";
        if(searchList(name,klassen)){
            klassen.add(name);
            dao.update(query,connection);
        }
    }
    public boolean searchList(String x, ArrayList<String> list){
        for (String aList : list) {
            if (aList.equals(x)) {
                return false;
            }
        }
        return true;

    }
    public void addModule(String name,Connection connection){
        String query = "insert into modules(modulecode) VALUES ('"+name+"')";
        dao.update(query,connection);

    }
    public void addKlasToModule(String klas,String module,Connection connection){
        String query = "insert into module_klassen(module,klas) VALUES('"+module+"','"+klas+"')";
        dao.update(query,connection);

    }
    public void addStudentToKlas(StringBuilder student, String klas,Connection connection){
        String query = "insert into student_klassen(student,klas) VALUES('"+student+"','"+klas+"')";
        dao.update(query,connection);

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
