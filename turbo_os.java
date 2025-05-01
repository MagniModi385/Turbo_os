import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Stack;

  public class Turbo_OS {
    static Scanner sc = new Scanner(System.in);
    static Connection con;
    static String name;

    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Turbo_os", "root", "");
        if (con == null) {
            System.out.println("Sorry OS encountered an issue");
        } else {
            System.out.println("Welcome to turbo OS");
            System.out.println();
            System.out.println("Please login to complete the setup");
            System.out.println();

            login l1 = new login();
            l1.start();
            l1.join();
            name = l1.username;
            System.out.println();
            do {
                String q = "SELECT captcha_code FROM captcha_codes WHERE id=?";
                PreparedStatement pst = con.prepareStatement(q);
                int ran = (int) (Math.random() * 100);
                ran++;
                pst.setInt(1, ran);
                ResultSet rs = pst.executeQuery();
                String captcha = "";
                while (rs.next()) {
                    captcha = rs.getString("captcha_code");
                }
                String input;
                System.out.println(captcha);
                System.out.println();
                System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
                System.out.print("Enter captcha as shown (case sensitive) - ");
                input = sc.next();
                System.out.println();
                if (input.equals(captcha)) {
                    System.out.println("Your OS Setup is completed !");
                    System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
                    break;
                } else {
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    System.out.println("Invalid Captcha");
                    System.out.println("Try again");
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    Thread.sleep(3000);
                }
            } while (true);
            Home h = new Home();
            h.start();
            h.join();
        }
    }
}

class login extends Thread {
    Scanner sc = new Scanner(System.in);
    String userMail = "";
    String username = "";

    @Override
    public void run() {
        try {
            loginUser();
        } catch (Exception e) {
            System.out.println("Login failed");
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    synchronized void loginUser() throws Exception {
        boolean b = true;
        while (b) {
            System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
            System.out.println("Press 1 if you do not have a google account");
            System.out.println("Press 2 if you have a google account");
            System.out.println("Press 3 to exit");
            System.out.print("Enter your choice - ");
            int choice;
            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println();
                sc.nextLine();
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println("I did not understand your input");
                System.out.println("Please try again");
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println();
                Thread.sleep(3000);
                continue;
            }
            System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
            System.out.println();
            switch (choice) {
                case 1: {
                    newUserRegistration();
                }
                    break;
                case 2: {
                    oldUserLogin();
                    b = false;
                }
                    break;
                case 3: {
                    b = false;
                }
                    break;
                default: {
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    System.out.println("Oops I did not understand your input");
                    System.out.println("Please Choose again");
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    System.out.println();
                }
                    break;
            }
        }
    }

    synchronized void newUserRegistration() throws Exception {
        boolean b = true;
        while (b) {
            String q = "INSERT INTO login_details VALUES (?,?,?,?)";
            PreparedStatement pst = Turbo_OS.con.prepareStatement(q);
            String mail;
            System.out.println("--------------------------------------------------------------------------------");
            System.out.print("Enter gmailID - ");
            try {
                mail = sc.next();
                System.out.println();
            } catch (InputMismatchException e) {
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println("Invalid mail ID");
                System.out.println("Try again");
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println();
                Thread.sleep(2000);
                continue;
            }
            String q1 = "SELECT GmailID FROM login_details";
            CallableStatement cst = Turbo_OS.con.prepareCall(q1);
            ResultSet rs = cst.executeQuery();
            boolean found = false;
            while (rs.next()) {
                if (rs.getString("GmailID").equalsIgnoreCase(mail)) {
                    found = true;
                }
            }
            if (found) {
                System.out.println();
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println("gmail already exists");
                System.out.println("Choose different gmail");
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println();
                Thread.sleep(2000);
                continue;
            }
            long number;
            System.out.print("Enter mobile number - ");
            try {
                number = sc.nextLong();
                System.out.println();
                long temp = number;
                int count = 0;
                while (temp > 0) {
                    temp = temp / 10;
                    count++;
                }
                if (count != 10) {
                    throw new NumberTooLongException();
                }
            } catch (InputMismatchException | NumberTooLongException e) {
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println("Invalid Phone number");
                System.out.println("Try again");
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println();
                sc.nextLine();
                Thread.sleep(2000);
                continue;
            }
            username = "";
            System.out.print("Enter username - ");
            try {
                sc.nextLine();
                username = sc.nextLine();
                System.out.println();
            } catch (InputMismatchException e) {
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println("Invalid User Name");
                System.out.println("Try again");
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println();
                Thread.sleep(2000);
                continue;
            }
            String password;
            System.out.print("Enter password - ");
            try {
                password = sc.nextLine();
                System.out.println();
            } catch (InputMismatchException e) {
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println("Invalid password");
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                continue;
            }
            pst.setString(1, mail);
            pst.setString(2, password);
            pst.setString(3, username);
            pst.setLong(4, number);
            int r = pst.executeUpdate();
            if (r > 0) {
                System.out.println("Registration successfull");
                b = false;
            } else {
                System.out.println("Registration failed");
                Thread.sleep(2000);
                System.out.println("Please Try again");
                System.out.println();
                continue;
            }
            System.out.println("--------------------------------------------------------------------------------");
            File f = new File("User registration.txt");
            FileWriter fw = new FileWriter(f, true);
            BufferedWriter bw = new BufferedWriter(fw);
            String q1234 = "{CALL getCurrentTime()}";
            CallableStatement cst1234 = Turbo_OS.con.prepareCall(q1234);
            Timestamp ts1234 = null;
            ResultSet rs1234 = cst1234.executeQuery();
            while (rs1234.next()) {
                ts1234 = rs1234.getTimestamp(1);
            }
            bw.write("New User details - ");
            bw.newLine();
            bw.write("Username : " + username);
            bw.newLine();
            bw.write("Mail : " + mail);
            bw.newLine();
            bw.write("Number : " + number);
            bw.newLine();
            bw.write("Date : " + ts1234);
            bw.newLine();
            bw.write(
                    "-----------------------------------------------------------------------------------------------------------");
            bw.newLine();
            bw.flush();
            bw.close();
        }
    }

    synchronized void oldUserLogin() throws Exception {
        String UserMail;
        boolean b = true;
        while (b) {
            System.out.println("--------------------------------------------------------------------------------");
            System.out.print("Enter your gmail ID - ");
            try {
                UserMail = sc.nextLine();
                System.out.println();
            } catch (InputMismatchException e) {
                System.out.println("Invalid gmail");
                System.out.println("Try again");
                System.out.println();
                Thread.sleep(2000);
                continue;
            }
            String password;
            System.out.print("Enter your password - ");
            try {
                password = sc.nextLine();
                System.out.println();
            } catch (InputMismatchException e) {
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println("Invalid password");
                System.out.println("Try again");
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println();
                Thread.sleep(2000);
                continue;
            }
            String q = "SELECT * FROM login_details";
            boolean found = false;
            Statement st = Turbo_OS.con.createStatement();
            ResultSet rs = st.executeQuery(q);
            while (rs.next()) {
                if (UserMail.equalsIgnoreCase(rs.getString("GmailID"))) {
                    if (password.equalsIgnoreCase(rs.getString("password"))) {
                        found = true;
                        System.out.println("Welcome " + rs.getString("username"));
                    }
                }
            }
            if (!found) {
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println("The entered ID and/or Password are incorret");
                System.out.println("Try again");
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println();
                Thread.sleep(2000);
            } else {
                b = false;
            }
            System.out.println("--------------------------------------------------------------------------------");
            System.out.println("--------------------------------------------------------------------------------");
            String q1234 = "{CALL getCurrentTime()}";
            CallableStatement cst1234 = Turbo_OS.con.prepareCall(q1234);
            Timestamp ts1234 = null;
            ResultSet rs1234 = cst1234.executeQuery();
            while (rs1234.next()) {
                ts1234 = rs1234.getTimestamp(1);
            }
            File f = new File("User login.txt");
            FileWriter fw = new FileWriter(f, true);
            BufferedWriter bw = new BufferedWriter(fw);
            username = "";
            String mail = "";
            long number = 0l;
            String query = "SELECT * FROM login_details WHERE GmailID='" + UserMail + "'";
            PreparedStatement pst = Turbo_OS.con.prepareStatement(query);
            ResultSet rs1 = pst.executeQuery();
            while (rs1.next()) {
                username = rs1.getString("username");
                mail = rs1.getString("GmailID");
                number = rs1.getLong("phoneNumber");
                bw.write("User Login details - ");
                bw.newLine();
                bw.write("Username : " + username);
                bw.newLine();
                bw.write("Mail : " + mail);
                bw.newLine();
                bw.write("Number : " + number);
                bw.newLine();
                bw.write("Date : " + ts1234);
                bw.newLine();
                bw.write(
                        "-----------------------------------------------------------------------------------------------------------");
                bw.newLine();
                bw.flush();
                bw.close();
            }
        }
    }
}

class NumberTooLongException extends Exception {
    NumberTooLongException() {
        super();
    }

    NumberTooLongException(String s){
        super(s);
    }
}

class Home extends Thread {
    Scanner sc = new Scanner(System.in);

    public void run() {
        beginFunctioning();
    }

    void beginFunctioning() {
        boolean b = true;
        while (b) {
            int choice;
            System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
            System.out.println("Press 1 to use Calculator");
            System.out.println("Press 2 to view Calendar");
            System.out.println("Press 3 to view Clock");
            System.out.println("Press 4 to use Contacts");
            System.out.println("Press 5 to use Notes");
            System.out.println("Press 6 to play games");
            System.out.print("Enter your choice (0 to exit) - ");
            try {
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Please enter a number");
                System.out.println();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e1) {
                    System.out.println(e1);
                }
                sc.nextLine();
                continue;
            }
            switch (choice) {
                case 0: {
                    b = false;
                }
                    break;
                case 1: {
                    Calci c = new Calci();
                    try {
                        c.calculate();
                    } catch (Exception e) {
                        System.out.println("Calculator encountered an issue");
                    }
                }
                    break;
                case 2: {
                    CalendarDisplay cd = new CalendarDisplay();
                    cd.displayCalendar();
                }
                    break;
                case 3: {
                    myClock c = new myClock();
                    c.runClock();
                }
                    break;
                case 4:{
                    MyPhoneBook pb=new MyPhoneBook();
                    try {
                        pb.contacts();
                    } catch (Exception e) {
                        System.out.println("Contacts under maintenance");
                    }
                }
                    break;
                case 5: {
                    Notes n = new Notes();
                    try {
                        n.personalNotes();
                    } catch (Exception ex) {
                        System.out.println("Notes Encountered an issue");
                    }
                }
                    break;
                case 6: {
                    gaming g = new gaming();
                    try {
                        g.startGaming();
                    } catch (Exception ex) {
                        System.out.println("Games not installed");
                    }
                }
                default: {
                    System.out.println("Invalid choice");
                    System.out.println();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e1) {
                        System.out.println(e1);
                    }
                }
                    break;
            }
        }
    }
}

class myClock {
    static Scanner sc = new Scanner(System.in);

    void runClock() {
        boolean b = true;
        while (b) {
            System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
            System.out.println("Press 1 to print current time");
            System.out.println("Press 2 to see alarm list");
            System.out.println("Press 3 to use stop watch");
            System.out.println("Press 4 to use timer");
            System.out.print("Enter your choice (0 to exit) - ");
            int choice;
            try {
                choice = sc.nextInt();
                System.out.println();
            } catch (InputMismatchException e) {
                System.out.println("Please enter a number");
                System.out.println();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e1) {
                    System.out.println(e1);
                }
                sc.nextLine();
                continue;
            }
            System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
            switch (choice) {
                case 0: {
                    b = false;
                }
                    break;
                case 1: {
                    getTime();
                }
                    break;
                case 2: {
                    getAlarm();
                }
                    break;
                case 3: {
                    stopWatch();
                }
                    break;
                case 4: {
                    int time;
                    System.out.print("Enter time (in seconds) - ");
                    try {
                        time = sc.nextInt();
                        System.out.println();
                    } catch (InputMismatchException e) {
                        System.out.println("Please enter a number");
                        continue;
                    }
                    timer(time);
                }
                    break;
                default: {
                    System.out.println("No such option available");
                    System.out.println();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e1) {
                        System.out.println(e1);
                    }
                }
                    break;
            }
        }
    }

    static void getTime() {
        String query = "{CALL getClock()}";
        try {
            CallableStatement cst = Turbo_OS.con.prepareCall(query);
            ResultSet rs = cst.executeQuery();
            Timestamp ts = null;
            while (rs.next()) {
                ts = rs.getTimestamp(1);
            }
            System.out.println(ts);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    static void getAlarm() {
        try {
            File f = new File("Alarm.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
            BufferedReader br = new BufferedReader(new FileReader(f));
            String alarm = br.readLine();
            boolean add = false;
            if (alarm == null) {
                System.out.println("You don't have any alarms set");
                System.out.println();
                while (true) {
                    System.out.print("Do you want to set alarms ? ");
                    String s = sc.next();
                    System.out.println();
                    if (s.equalsIgnoreCase("yes")) {
                        add = true;
                        break;
                    } else if (s.equalsIgnoreCase("no")) {
                        break;
                    } else {
                        System.out.println("Please enter yes or no");
                        System.out.println();
                    }

                }
            } else {
                while (alarm != null) {
                    System.out.println(alarm);
                    System.out.println();
                    alarm = br.readLine();
                }
                while (true) {
                    System.out.print("Do you want to set more alarms ? ");
                    String s = sc.next();
                    System.out.println();
                    if (s.equalsIgnoreCase("yes")) {
                        add = true;
                        break;
                    } else if (s.equalsIgnoreCase("no")) {
                        break;
                    } else {
                        System.out.println("Please enter yes or no");
                        System.out.println();
                    }

                }
            }
            while (add) {
                System.out.print("Enter alarm time - ");
                String s1 = sc.nextLine();
                System.out.println();
                sc.nextLine();
                bw.write(s1);
                bw.newLine();
                bw.write("--------------------------------------------");
                bw.flush();
                System.out.println("Alarm Set");
                System.out.println();
                System.out.print("Do you want to set more alarms ? ");
                sc.nextLine();
                String s = sc.next();
                System.out.println();
                if (!s.equalsIgnoreCase("yes")) {
                    add = false;
                }
            }
            bw.close();
            br.close();
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    static void stopWatch() {
        StopWatch st = new StopWatch();
        System.out.print("Press any key to start the timer - ");
        sc.next();
        System.out.println();
        st.start();
        System.out.println("*************************************************************************************");
        System.out.print("Press any key to stop the timer - ");
        myThread mt = new myThread();
        mt.start();
        try {
            mt.join();
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        int time = StopWatch.i;
        int temp = time;
        int hour = 0, min = 0, sec = 0;
        if (time > 3600) {
            hour = temp / 3600;
            min = (temp / 60) % 60;
            sec = temp % 3600;
        } else if (time > 60) {
            min = temp / 60;
            sec = temp % 60;
        } else {
            sec = temp;
        }
        System.out.println(hour + " hrs " + min + " mins " + sec + " seconds");
    }

    static void timer(int timing) {
        timer.time = timing;
        timer t = new timer();
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        System.out.println("Time up !");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        System.out.println();
    }
}

class StopWatch extends Thread {
    static int i;
    static boolean b;

    public void run() {
        i = 0;
        b = true;
        while (b) {
            i++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }
}

class myThread extends Thread {
    Scanner sc = new Scanner(System.in);

    public void run() {
        if (sc.nextLine() != null) {
            StopWatch.b = false;
            synchronized (this) {
                notify();
            }
        }
    }
}

class timer extends Thread {
    static int time;

    public void run() {
        for (int i = 1; i < time + 1; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }
}

class CalendarDisplay {
    void displayCalendar() {
        Scanner sc = new Scanner(System.in);
        System.out.println();
        System.out.print("Enter the year: ");
        int year = sc.nextInt();
        System.out.println();
        int month = 1;
        while (true) {
            displayCalendar(month, year);
            month++;
            System.out.println("::::::::::::::::::::::::::::::");
            if (month > 12) {
                break;
            }
        }
    }

    static void displayCalendar(int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1); // months are indexed from 0 in Calendar
        cal.set(Calendar.DAY_OF_MONTH, 1); // start at the 1st of the month

        int FirstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK); // day of the week
        int DaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH); // number of days in the month
        System.out
                .println("\n   " + cal.getDisplayName(Calendar.MONTH, Calendar.LONG, java.util.Locale.US) + " " + year);
        System.out.println("Sun Mon Tue Wed Thu Fri Sat");
        for (int i = 1; i < FirstDayOfWeek; i++) {
            System.out.print("    ");
        }
        // Print days of the month
        for (int day = 1; day <= DaysInMonth; day++) {
            System.out.printf("%3d ", day);
            if ((day + FirstDayOfWeek - 1) % 7 == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }
}

class Notes {
    static Scanner sc = new Scanner(System.in);

    void personalNotes() throws Exception {
        System.out.println("THESE ARE YOUR PERSONAL NOTES");
        boolean b = true;
        while (b) {
            int choice;
            System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
            System.out.println("Press 1 to create a new note");
            System.out.println("Press 2 to view a particular note");
            System.out.println("Press 3 to delete a note");
            System.out.print("Enter your choice (0 to exit) - ");
            try {
                choice = sc.nextInt();
                System.out.println();
            } catch (Exception e) {
                System.out.println("Please enter a number ");
                Thread.sleep(2000);
                System.out.println();
                sc.nextLine();
                continue;
            }
            System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
            switch (choice) {
                case 0: {
                    b = false;
                }
                    break;
                case 1: {
                    createNotes();
                }
                    break;
                case 2: {
                    System.out.print("Enter note title to fetch - ");
                    sc.nextLine();
                    String title = sc.nextLine().toLowerCase();
                    System.out.println();
                    getNote(title);
                }
                    break;
                case 3: {
                    System.out.print("Enter note title to delete - ");
                    sc.nextLine();
                    String title = sc.nextLine().toLowerCase();
                    System.out.println();
                    deleteNote(title);
                }
                    break;
                default: {
                    System.out.println("No such option available");
                    Thread.sleep(2000);
                    System.out.println();
                }
                    break;
            }
        }
    }

    static void createNotes() throws Exception {
        System.out.print("Enter note title - ");
        sc.nextLine();
        String title = sc.nextLine().toLowerCase();
        System.out.println();
        File f = new File(title + ".txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
        System.out.println("Enter your note (Type exit when completed)");
        myLinkedList mll = new myLinkedList();
        while (true) {
            String note = sc.nextLine();
            if (note.equalsIgnoreCase("exit")) {
                break;
            } else {
                mll.insertNode(note);
            }
        }
        while (true) {
            if (mll.head == null) {
                break;
            } else {
                bw.write(mll.deleteHead());
                bw.newLine();
                bw.flush();
            }
        }
        bw.close();
    }

    static void getNote(String title) throws Exception {
        File f = new File(title + ".txt");
        myLinkedList fetchedNotes = new myLinkedList();
        if (f.exists()) {
            BufferedReader bw = new BufferedReader(new FileReader(f));
            String note = bw.readLine();
            while (note != null) {
                fetchedNotes.insertNode(note);
                note = bw.readLine();
            }
            fetchedNotes.display(fetchedNotes.head);
        } else {
            System.out.println("No such note exists");
            Thread.sleep(2000);
        }
    }

    static void deleteNote(String title) throws Exception {
        File f = new File(title + ".txt");
        if (f.exists()) {
            if (f.delete()) {
                System.out.println("Note deleted");
                System.out.println();
            } else {
                System.out.println("Note could not be deleted");
                System.out.println();
            }
        } else {
            System.out.println("Note does not exist");
        }
    }
}

class myLinkedList {
    class Node {
        String note;
        Node next;

        Node(String s) {
            this.note = s;
            this.next = null;
        }
    }

    Node head;

    void insertNode(String s) {
        Node n = new Node(s);
        if (head == null) {
            head = n;
        } else {
            Node temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = n;
        }
    }

    String deleteHead() {
        if (head == null) {
            return null;
        } else {
            String ret = head.note;
            head = head.next;
            return ret;
        }
    }

    void display(Node dummy) {
        if (dummy != null) {
            System.out.println(dummy.note);
            display(dummy.next);
        }
    }
}

class Node {
    String data;
    Node next;
    Node prev;

    // Constructor
    public Node(String data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }
}

class DoublyLinkedList {
    Node head; // Head of the list

    // Constructor
    public DoublyLinkedList() {
        this.head = null;
    }

    // Method to insert a new node at the front of the list
    public void insertFront(String data) {
        Node n = new Node(data);
        n.next = head;
        if (head != null) {
            head.prev = n;
        }
        head = n;
    }

    // Method to insert a new node at the end of the list
    public void insertEnd(String data) {
        Node n = new Node(data);
        if (head == null) {
            head = n;
            return;
        }
        Node temp = head;
        while (temp.next != null) {
            temp = temp.next;
        }
        temp.next = n;
        n.prev = temp;
    }

    // Method to print the list from front to end
    public void displayForward() {
        Node temp = head;
        while (temp != null) {
            System.out.print(temp.data + " ");
            temp = temp.next;
        }
        System.out.println();
    }

    // Method to print the list from end to front
    public void displayBackward() {
        Node temp = head;
        if (temp == null)
            return;

        // Traverse to the last node
        while (temp.next != null) {
            temp = temp.next;
        }

        // Traverse backward
        while (temp != null) {
            System.out.print(temp.data + " ");
            temp = temp.prev;
        }
        System.out.println();
    }

    // Method to empty the list(For History Clear Option)
    public void clearList() {
        head = null;
    }
}

class Calci {

    // Function to evaluate an infix expression
    public static int evaluateInfix(String expression) {
        // Convert infix to postfix
        String postfix = infixToPostfix(expression);
        // Evaluate postfix expression
        return evaluatePostfix(postfix);
    }

    // Function to convert infix expression to postfix expression
    private static String infixToPostfix(String expression) {
        StringBuilder result = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            // If the character is an operand (including multi-digit numbers), add it to the
            // output
            if (Character.isDigit(c)) {
                while (i < expression.length() && Character.isDigit(expression.charAt(i))) {
                    result.append(expression.charAt(i));
                    i++;
                }
                result.append(' '); // Adding space to separate numbers
                i--; // Adjust for the next loop iteration
            }
            // If the character is '(', push it to the stack
            else if (c == '(') {
                stack.push(c);
            }
            // If the character is ')', pop and output from the stack until '(' is found
            else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    result.append(stack.pop()).append(' ');
                }
                stack.pop();
            }
            // An operator is encountered
            else {
                while (!stack.isEmpty() && precedence(c) <= precedence(stack.peek())) {
                    result.append(stack.pop()).append(' ');
                }
                stack.push(c);
            }
        }

        // Pop all the operators from the stack
        while (!stack.isEmpty()) {
            if (stack.peek() == '(')
                throw new IllegalArgumentException("Invalid Expression");
            result.append(stack.pop()).append(' ');
        }

        return result.toString();
    }

    // Function to return precedence of operators
    private static int precedence(char ch) {
        switch (ch) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
        }
        return -1;
    }

    // Function to evaluate a postfix expression
    private static int evaluatePostfix(String expression) {
        Stack<Integer> stack = new Stack<>();
        String[] tokens = expression.split("\\s+");

        for (String token : tokens) {
            // If the token is a number, push it to the stack
            if (token.matches("\\d+")) {
                stack.push(Integer.parseInt(token));
            }
            // If the token is an operator, pop two elements from stack and apply the
            // operator
            else {
                int val1 = stack.pop();
                int val2 = stack.pop();

                switch (token) {
                    case "+":
                        stack.push(val2 + val1);
                        break;
                    case "-":
                        stack.push(val2 - val1);
                        break;
                    case "*":
                        stack.push(val2 * val1);
                        break;
                    case "/":
                        if (val1 == 0)
                            throw new ArithmeticException("Division by zero");
                        stack.push(val2 / val1);
                        break;
                }
            }
        }
        return stack.pop();
    }

    void calculate() throws Exception {
        Scanner sc = new Scanner(System.in);
        Statement st = Turbo_OS.con.createStatement();
        DoublyLinkedList dll = new DoublyLinkedList();

        // CREATE TABLE TO SAVE CALCULATION HISTORY IN DATABASE
        String tableCreation = "CREATE TABLE IF NOT EXISTS History_Calculation (" +
                "Id INT AUTO_INCREMENT PRIMARY KEY, " +
                "Expression VARCHAR(255), " +
                "Result DOUBLE, " +
                "Time TIMESTAMP" +
                ")";
        st.execute(tableCreation);
        boolean b = true;
        String expression = "";
        int result = 0;
        while (b) {
            System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
            System.out.println(
                    "1.Calculation\n2.Clear History\n3.Watch History\n4.Save History to Database\n5.Exit");
            System.out.print("Enter Your choice:- ");
            int choice = 0;
            try {
                sc.nextLine();
                choice = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Please enter a number");
                continue;
            }
            System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
            switch (choice) {
                case 1:
                    // CALCULATION LOGIC
                    try {
                        System.out.print("Enter Your Infix Expression:- ");
                        expression = sc.next();
                        result = evaluateInfix(expression);
                        System.out.println("\n" + expression + " = " + result + "\n");
                        dll.insertEnd(expression + " = " + result + "\n");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    // CLEAR HISTORY AS PER USER INPUT
                    dll.clearList();
                    break;
                case 3: // SHOW HISTORY TO USER AS PER INPUT
                    System.out.println("\n-------------------------------------------------------------------------");
                    dll.displayForward();
                    System.out.println("-------------------------------------------------------------------------");

                    break;
                case 4:
                    // INSERT INTO TABLE
                    String sql = "Insert into History_Calculation(Expression,Result,Time) values ('" + expression + "',"
                            + result + ", now())";
                    int r = st.executeUpdate(sql);
                    System.out.println((r > 0) ? "Calculation Saved to History" : "Failed to Save the Calculation");
                    break;
                case 5:
                    b = false;
                    break;
                default:
                    System.out.println("Invalid Choice:- ");
            }

        }
    }
}

class DivisionByZeroException extends Exception {
    DivisionByZeroException(String message) {
        super(message);
    }
}

class InputInvalidException extends Exception {
    InputInvalidException(String message) {
        super(message);
    }
}

class gaming {
    static Scanner sc = new Scanner(System.in);

    void startGaming() throws Exception {
        Player p1 = new Player();
        p1.playing();
    }
}

class Player {
    Scanner sc = new Scanner(System.in);
    ArrayList<games> game = new ArrayList<>();

    public synchronized void playing() {
        try {
            String name = Turbo_OS.name;
            System.out.println();
            String q = "SELECT*FROM available_games";
            Statement st = Turbo_OS.con.createStatement();
            ResultSet rs = st.executeQuery(q);
            while (rs.next()) {
                games g = new games(rs.getInt("ID"), rs.getString("name"), rs.getInt("MaxPlayers"));
                game.add(g);

            }
            System.out.println(
                    "########################################################################################");
            System.out.println(
                    "Game Number            Game Name            Maximum Players");
            System.out.println();
            try {
                @SuppressWarnings("rawtypes")
                Iterator itr;
                itr = game.iterator();
                while (itr.hasNext()) {
                    System.out.println(itr.next());
                }
            } catch (NoSuchElementException e) {
                System.out.println("Hi");
            }
            System.out.println(
                    "########################################################################################");
            System.out.println();
            System.out.print("Enter game number you want to play () - ");
            int choice;
            try {
                choice = sc.nextInt();
                System.out.println();
            } catch (InputMismatchException e) {
                System.out.println("Please Enter a number ");
                System.out.println();
                sc.nextLine();
                return;
            } catch (NoSuchElementException e) {
                System.out.println("Please Enter a number ");
                System.out.println();
                choice = sc.nextInt();
                sc.nextLine();
                return;
            }
            switch (choice) {
                case 1: {
                    DigitalCricket dc = new DigitalCricket();
                    try {
                        dc.playDigitalCricket();
                    } catch (Exception e) {
                        System.out.println("Digital Cricket under Maintencnace");
                    }
                    sc.nextLine();
                    select s = new select();
                    try {
                        s.selectAgain();
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
                    break;
                case 2: {
                    boolean b1 = true;
                    while (b1) {
                        System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
                        System.out.println("Press 1 for 1 player");
                        System.out.println("Press 2 for 2 player");
                        System.out.print("Your input - ");
                        int numbOfPlayers;
                        try {
                            numbOfPlayers = sc.nextInt();
                            System.out.println();
                        } catch (InputMismatchException e) {
                            System.out.println("I did not understand your input");
                            System.out.println();
                            System.out.println("Try again");
                            System.out.println();
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException ex) {
                                System.out.println(ex);
                            }
                            continue;
                        }
                        System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
                        switch (numbOfPlayers) {
                            case 1: {
                                TicTacToe tt = new TicTacToe();
                                try {
                                    tt.playTicTacToe(1, name);
                                } catch (Exception ex) {
                                    System.out.println(ex);
                                }
                                b1 = false;
                            }
                                break;
                            case 2: {

                                TicTacToe tt = new TicTacToe();
                                try {
                                    tt.playTicTacToe(2, name);
                                } catch (Exception ex) {
                                    System.out.println(ex);
                                }
                                b1 = false;
                            }
                                break;
                            default: {
                                System.out.println("No such option available , Sorry !");
                                System.out.println();
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    System.out.println(e);
                                }
                            }
                                break;
                        }
                    }
                    select s = new select();
                    try {
                        s.selectAgain();
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
                    break;
                case 3: {
                    boolean b1 = true;
                    while (b1) {
                        System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
                        System.out.println("Press 1 for single player");
                        System.out.println("Press 2 for double player");
                        System.out.print("Your input - ");
                        int numbOfPlayers;
                        try {
                            numbOfPlayers = sc.nextInt();
                            System.out.println();
                        } catch (InputMismatchException e) {
                            System.out.println("I did not understand your input");
                            System.out.println();
                            System.out.println("Try again");
                            System.out.println();
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException ex) {
                                System.out.println(ex);
                            }
                            System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
                            continue;
                        }
                        switch (numbOfPlayers) {
                            case 1: {

                                HangmanGame hg = new HangmanGame();
                                try {
                                    hg.playHangmanGame(1, name);
                                } catch (Exception ex) {
                                    System.out.println(ex);
                                }

                                b1 = false;
                            }
                                break;
                            case 2: {
                                HangmanGame hg = new HangmanGame();
                                try {
                                    hg.playHangmanGame(2, name);
                                } catch (Exception ex) {
                                    System.out.println(ex);
                                }
                            }
                                b1 = false;
                                break;
                            default: {
                                System.out.println("No such option available , Sorry !");
                                System.out.println();
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    System.out.println(e);
                                }
                            }
                                break;
                        }
                    }
                    select s = new select();
                    try {
                        s.selectAgain();
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
                    break;
                default: {
                    System.out.println("No such game available");
                    System.out.println("Choose again");
                    System.out.println();
                }
                    break;
            }
        } catch (SQLException ex) {
            System.out.println("Games under maintenance");
        }
    }
}

class games {
    int id;
    String name;
    int MaxPlayers;

    games(int id, String name, int MaxPlayers) {
        this.id = id;
        this.name = name;
        this.MaxPlayers = MaxPlayers;
    }

    @Override
    public String toString() {
        return "Game Number = " + id + " , Game Name = " + name + ", Maximum Players = " + MaxPlayers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxPlayers() {
        return MaxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        MaxPlayers = maxPlayers;
    }
}

class select {
    Scanner input = new Scanner(System.in);

    void selectAgain() throws Exception {
        String cont;
        Player pl = new Player();
        try {
            System.out.println("Do you want to continue playing ? ");
            // input.nextLine();
            cont = input.next();
            System.out.println();
        } catch (NoSuchElementException e) {
            cont = input.next();
        }
        if (cont.equalsIgnoreCase("yes")) {
            pl.playing();
        }
    }
}

class DigitalCricket {
    static Scanner sc;

    synchronized public void playDigitalCricket() throws Exception {
        sc = new Scanner(System.in);
        System.out.println("Hello ! Welcome to digicricket");
        Thread.sleep(3000);
        System.out.println();
        System.out.println("I am your gaming assistant Pixel-Wiz :)");
        Thread.sleep(2000);
        System.out.println();
        System.out.println("Let me quickly explain you the rules :-");
        System.out.println();
        Thread.sleep(3000);
        rules();
        System.out.println("Good luck ! I won't go easy on you .");
        Thread.sleep(2000);
        DigiCricket dc = new DigiCricket();
        dc.run();
        System.out.println("It was nice playing with you !");
        Thread.sleep(2000);
        System.out.println("Hope you had fun .");
    }

    static void rules() {
        try {
            System.out.println("__________________________________________________________________________");
            System.out.println();
            System.out.println("--> You are supposed to enter a number between 1 and 6 .");
            System.out.println();
            Thread.sleep(2000);
            System.out.println(
                    "--> When you are batting , the number you choose becomes the run you scored in that ball .");
            System.out.println("--> You get out if the number you entered is same as the number I choose .");
            Thread.sleep(8000);
            System.out.println();
            System.out.println("--> When you are balling , you are supposed to guess the number I will choose .");
            System.out.println("--> If we choose the same number , I am out .");
            System.out.println();
            Thread.sleep(7000);
            System.out.println("--> Whoever scores more runs in 5 overs wins the match .");
            System.out.println();
            Thread.sleep(2000);
            System.out.println("__________________________________________________________________________");
            System.out.println();
            boolean b = true;
            while (b) {
                System.out.print("Are you ready to play ? ");
                String s = sc.next();
                System.out.println();
                if (s.equalsIgnoreCase("yes")) {
                    b = false;
                } else {
                    System.out.println("Sorry I did not understand your response");
                }
            }
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }
}

class DigiCricket {
    Scanner sc = new Scanner(System.in);
    String play;

    public void run() {
        boolean b = true;
        while (b) {
            char choice;
            System.out.println();
            System.out.println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
            System.out.println("Let's do the toss");
            System.out.println();
            System.out.println("Press 1 for Heads");
            System.out.println("Press 2 for Tails");
            System.out.print("Enter your choice - ");
            choice = sc.next().charAt(0);
            System.out.println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
            System.out.println();
            if (choice != '1' && choice != '2') {
                System.out.println();
                System.out.println("Please enter 1 or 2");
            } else {
                int t = (int) (Math.random() * 2);
                char toss;
                if (t == 1) {
                    toss = '1';
                } else {
                    toss = '2';
                }
                if (toss == choice) {
                    System.out.println("Congratulations ! You won the toss");
                    boolean b1 = true;
                    while (b1) {
                        System.out.println("Choose (bat or ball) - ");
                        this.play = sc.next();
                        if (play.equalsIgnoreCase("bat") || play.equalsIgnoreCase("ball")) {
                            b1 = false;
                            b = false;
                        } else {
                            System.out.println("Oops! Invalid choice ");
                        }
                    }
                } else {
                    System.out.println("Looks like I just got lucky");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }
                    int ran = (int) (Math.random() * 2);
                    if (ran == 1) {
                        System.out.println("I choose to bat first");
                        play = "ball";
                        b = false;
                    } else {
                        System.out.println("I choose to ball first");
                        play = "bat";
                        b = false;
                    }
                }
                normalCricket();
            }
        }
    }

    synchronized void normalCricket() {
        int botRuns;
        int playerRuns;
        if (play.equalsIgnoreCase("bat")) {
            playerRuns = batOpen();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            System.out.println("You gave me a target of " + (playerRuns + 1) + " runs");
            System.out.println("Good luck with balling");
            botRuns = ballTarget(playerRuns + 1);
            if (playerRuns > botRuns) {
                System.out.println("Congratulations ! You win .");
                System.out.println();
                System.out.println(
                        "**********************************************************************************************************************************************************");
                System.out.println();

            } else if (botRuns > playerRuns) {
                System.out.println("Seems like you are having a bad day , I win but you gave me a nice challenge");
                System.out.println();
                System.out.println(
                        "**********************************************************************************************************************************************************");
                System.out.println();
            } else {
                System.out.println("Seems like we are tied , it was nice playing with you");
                System.out.println();
                System.out.println(
                        "**********************************************************************************************************************************************************");
                System.out.println();
            }
        } else {
            botRuns = ballOpen();
            System.out.println();
            System.out.println("I gave you a target of " + (botRuns + 1) + " runs");
            System.out.println();
            System.out.println("Your turn to bat");
            System.out.println();
            playerRuns = batTarget(botRuns + 1);
            if (playerRuns > botRuns) {
                try {
                    System.out.println("Congratulations ! You win .");
                    System.out.println();
                    System.out.println(
                            "**********************************************************************************************************************************************************");
                    System.out.println();
                    File f = new File("cricket_log.txt");
                    FileWriter fw = new FileWriter(f, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write("Pixel-Wiz lost");
                    bw.newLine();
                    bw.write(
                            "**********************************************************************************************************************************************************");
                    bw.newLine();
                    bw.close();
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            } else if (botRuns > playerRuns) {
                System.out.println("Seems like you are having a bad day , I win but you gave me a nice challenge");
                System.out.println();
                System.out.println(
                        "**********************************************************************************************************************************************************");
                System.out.println();
            } else {
                System.out.println("Seems like we are tied , it was nice playing with you");
                System.out.println();
                System.out.println(
                        "**********************************************************************************************************************************************************");
                System.out.println();
            }
        }
    }

    int batOpen() {
        int runs = 0;
        int totalRuns = 0;
        boolean b2 = true;
        MyStack bat = new MyStack(6);
        myQueue ba = new myQueue(5);
        int ballcount = 0;
        int overcount = 0;
        boolean noBall = false;
        int extraRuns = 0;
        while (b2) {
            System.out.println("****************************************************************");
            System.out.println();
            if (overcount == 5) {
                System.out.println("Damn You survived all my balls");
                System.out.println();
                System.out.println("You are quite skilled");
                while (!bat.isEmpty()) {
                    runs += bat.pop();
                }
                ba.Enqueue(runs);
                b2 = false;
                continue;
            }
            ballcount++;
            System.out.print("Your number - ");
            int n;
            try {
                n = sc.nextInt();
                System.out.println();
            } catch (InputMismatchException e) {
                System.out.println("Please enter a number");
                System.out.println();
                sc.nextLine();
                continue;
            }
             int ball = (int) (Math.random() * 8);
            System.out.println("My number is " + ball);
            System.out.println();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            System.out.println();
            if (n > 6 || n < 1) {
                System.out.println("Invalid number");
                ballcount--;
                continue;
            } else if (noBall) {
                if (ball != n) {
                    System.out.println("You scored " + n + " runs in the free hit");
                    System.out.println();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }
                    extraRuns += n;
                } else {
                    System.out.println("You are safe");
                }
                noBall = false;
            } else if (ball == n) {
                System.out.println("Yahoo! You are out");
                System.out.println();
                System.out.println("You played well but better luck next time");
                while (!bat.isEmpty()) {
                    runs += bat.pop();
                }
                ba.Enqueue(runs);
                b2 = false;
                continue;
            } else if (ball == 0) {
                System.out.println("Oops it's a wide");
                System.out.println();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                System.out.println("You get an extra run");
                System.out.println();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                ballcount--;
                extraRuns += 1;
            } else if (ball == 7) {
                System.out.println("Ah! it's a no ball ");
                System.out.println();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                System.out.println("You get a free hit");
                System.out.println();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                extraRuns += 1;
                ballcount--;
                noBall = true;
                continue;
            } else {
                System.out.println("You scored " + n + " runs");
                System.out.println();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                System.out.println("Now play the next ball");
                System.out.println();
                bat.push(n);
            }
            if (ballcount == 6) {
                overcount++;
                while (!bat.isEmpty()) {
                    runs += bat.pop();
                }
                System.out.println("You scored " + runs + " runs in over " + overcount);
                System.out.println();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                ba.Enqueue(runs);
                runs = 0;
                ballcount = 0;
            }
            System.out.println("****************************************************************");
            System.out.println();
        }
        while (!ba.isEmpty()) {
            totalRuns += ba.Dequeue();
        }
        totalRuns += extraRuns;
        return totalRuns;
    }

    int ballTarget(int target) {
        int runs = 0;
        int totalRuns = 0;
        boolean b2 = true;
        MyStack ball = new MyStack(6);
        myQueue ba = new myQueue(5);
        int ballcount = 0;
        int overcount = 0;
        while (b2) {
            System.out.println("****************************************************************");
            System.out.println();
            if (overcount == 5) {
                System.out.println("Look I survived all your balls");
                while (!ball.isEmpty()) {
                    runs += ball.pop();
                }
                ba.Enqueue(runs);
                b2 = false;
                continue;
            }
            ballcount++;
            System.out.print("Your number - ");
            int n;
            try {
                n = sc.nextInt();
                System.out.println();
            } catch (InputMismatchException e) {
                System.out.println("Please enter a number");
                System.out.println();
                sc.nextLine();
                continue;
            }
            int bat = (int) (Math.random() * 6);
            bat += 1;
            if (n > 6 || n < 1) {
                System.out.println("Invalid number");
                ballcount--;
                continue;
            } else if (bat == n) {
                System.out.println("Oops! I am out");
                System.out.println();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                System.out.println("You played well .");
                System.out.println();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                while (!ball.isEmpty()) {
                    runs += ball.pop();
                }
                ba.Enqueue(runs);
                b2 = false;
                continue;
            } else {
                System.out.println("I scored " + bat + " runs");
                System.out.println();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                System.out.println("Now the next ball");
                System.out.println();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                System.out.println();
                target -= bat;
                ball.push(bat);
            }
            if (ballcount == 6) {
                overcount++;
                while (!ball.isEmpty()) {
                    runs += ball.pop();
                }
                System.out.println("I scored " + runs + " runs in " + overcount + " overs");
                System.out.println();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                ba.Enqueue(runs);
                runs = 0;
                ballcount = 0;
            }
            if (target <= 0) {
                System.out.println("Let's gooo ! I win ");
                System.out.println();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                while (!ball.isEmpty()) {
                    runs += ball.pop();
                }
                ba.Enqueue(runs);
                b2 = false;
            }
            System.out.println("****************************************************************");
            System.out.println();
        }
        while (!ba.isEmpty()) {
            totalRuns += ba.Dequeue();
        }
        return totalRuns;
    }

    int ballOpen() {
        int runs = 0;
        int totalRuns = 0;
        boolean b2 = true;
        MyStack ball = new MyStack(6);
        myQueue ba = new myQueue(5);
        int ballcount = 0;
        int overcount = 0;
        while (b2) {
            System.out.println("****************************************************************");
            System.out.println();
            if (overcount == 5) {
                System.out.println("Look I survived all your balls");
                System.out.println();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                while (!ball.isEmpty()) {
                    runs += ball.pop();
                }
                ba.Enqueue(runs);
                b2 = false;
                continue;
            }
            ballcount++;
            System.out.print("Your number - ");
            int n;
            try {
                n = sc.nextInt();
                System.out.println();
            } catch (InputMismatchException e) {
                System.out.println("Please enter a number");
                System.out.println();
                sc.nextLine();
                continue;
            }
            int bat = (int) (Math.random() * 6);
            bat += 1;
            if (n > 6 || n < 1) {
                System.out.println("Invalid number");
                ballcount--;
                continue;
            } else if (bat == n) {
                System.out.println("Oops! I am out");
                System.out.println();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                System.out.println("You played well .");
                while (!ball.isEmpty()) {
                    runs += ball.pop();
                }
                ba.Enqueue(runs);
                b2 = false;
                continue;
            } else {
                System.out.println("I scored " + bat + " runs");
                System.out.println();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                System.out.println("Now the next ball");
                System.out.println();
                ball.push(bat);
            }
            if (ballcount == 6) {
                overcount++;
                while (!ball.isEmpty()) {
                    runs += ball.pop();
                }
                System.out.println("I scored " + runs + " runs in over " + overcount);
                System.out.println();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                ba.Enqueue(runs);
                runs = 0;
                ballcount = 0;
            }
            System.out.println("****************************************************************");
            System.out.println();
        }
        while (!ba.isEmpty()) {
            totalRuns += ba.Dequeue();
        }
        return totalRuns;
    }

    int batTarget(int target) {
        int runs = 0;
        int totalRuns = 0;
        boolean b2 = true;
        MyStack bat = new MyStack(6);
        myQueue ba = new myQueue(5);
        int ballcount = 0;
        int overcount = 0;
        boolean noBall = false;
        int extraRuns = 0;
        while (b2) {
            System.out.println("****************************************************************");
            System.out.println();
            if (overcount == 5) {
                System.out.println("Damn You survived all my balls");
                System.out.println();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                System.out.println("You are quite skilled");
                while (!bat.isEmpty()) {
                    runs += bat.pop();
                }
                ba.Enqueue(runs);
                b2 = false;
                continue;
            }
            ballcount++;
            System.out.print("Your number - ");
            int n;
            try {
                n = sc.nextInt();
                System.out.println();
            } catch (InputMismatchException e) {
                System.out.println("Please enter a number");
                System.out.println();
                sc.nextLine();
                continue;
            }
            int ball = (int) (Math.random() * 8);
            System.out.println("My number is " + ball);
            if (n > 6 || n < 1) {
                System.out.println("Invalid number");
                ballcount--;
                continue;
            } else if (noBall) {
                if (ball != n) {
                    System.out.println("You scored " + n + " runs in the free hit");
                    System.out.println();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }
                    extraRuns += n;
                } else {
                    System.out.println("You are safe");
                    System.out.println();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }
                    System.out.println();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }
                }
                noBall = false;
            } else if (ball == n) {
                System.out.println("Yahoo! You are out");
                System.out.println();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                System.out.println();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                System.out.println("You played well but better luck next time");
                System.out.println();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                while (!bat.isEmpty()) {
                    runs += bat.pop();
                }
                ba.Enqueue(runs);
                b2 = false;
                continue;
            } else if (ball == 0) {
                System.out.println("Oops it's a wide");
                System.out.println();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                System.out.println("You get an extra run");
                System.out.println();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                ballcount--;
                extraRuns += 1;
            } else if (ball == 7) {
                System.out.println("Ah! it's a no ball ");
                System.out.println();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                System.out.println("You get a free hit");
                System.out.println();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                extraRuns += 1;
                ballcount--;
                noBall = true;
                continue;
            } else {
                System.out.println("You scored " + n + " runs");
                System.out.println();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                System.out.println("Now play the next ball");
                System.out.println();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                target -= n;
                System.out.println();
                bat.push(n);
            }
            if (ballcount == 6) {
                overcount++;
                while (!bat.isEmpty()) {
                    runs += bat.pop();
                }
                System.out.println("You scored " + runs + " runs in over " + overcount);
                System.out.println();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                ba.Enqueue(runs);
                runs = 0;
                ballcount = 0;
            }
            if (target <= 0) {
                System.out.println("You defeated me");
                System.out.println();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                while (!bat.isEmpty()) {
                    runs += bat.pop();
                }
                ba.Enqueue(runs);
                b2 = false;
            }
            System.out.println("****************************************************************");
            System.out.println();
        }
        while (!ba.isEmpty()) {
            totalRuns += ba.Dequeue();
        }
        totalRuns += extraRuns;
        return totalRuns;
    }
}

class MyStack {
    int ar[];
    int top = -1;
    int size;

    public MyStack(int size) {
        this.size = size;
        this.ar = new int[this.size];
    }

    synchronized void push(int n) {
        if (!isFull()) {
            top++;
            ar[top] = n;
        }

    }

    boolean isFull() {
        return top >= size - 1;
    }

    boolean isEmpty() {
        return top == -1;
    }

    int pop() {
        if (!isEmpty()) {
            if (top < size) {
                int r = ar[top];
                top--;
                return r;
            }
            return -1;
        }
        return -1;
    }
}

class myQueue {
    int front;
    int rear;
    int ar[];
    int size;

    public myQueue(int size) {
        this.size = size;
        this.ar = new int[size];
        this.front = -1;
        this.rear = -1;
    }

    synchronized void Enqueue(int n) {
        rear++;
        ar[rear] = n;
        if (front == -1) {
            front++;
        }
    }

    boolean isFull() {
        return rear >= size - 1;
    }

    boolean isEmpty() {
        return rear == -1;
    }

    int Dequeue() {
        if (!isEmpty()) {
            int r = ar[front];
            front++;
            if (front == rear + 1) {
                front = rear = -1;
            }
            return r;
        }
        return -1;
    }
}

class TicTacToe {
    static String player1;
    static String player2;
    static Scanner sc;
    static int numbOfPlayer;

    public void playTicTacToe() {
        try {
            playTicTacToe(numbOfPlayer, player1);
        } catch (Exception e) {
            System.out.println("TicTacToe under Maintenance");
        }
        System.out.println("TicTacToe Ended");
    }

    synchronized public void playTicTacToe(int numbOfPlayer, String userName) throws Exception {
        sc = new Scanner(System.in);
        player1 = userName;
        System.out.println("Welcome to tic tac toe !");
        System.out.println();
        Thread.sleep(2000);
        System.out.println("I am PixelWiz, your gaming assistant :) ");
        System.out.println();
        Thread.sleep(2000);
        System.out.println(
                "_______________________________________________________________________________________________________________________");
        System.out.println();
        System.out.println("The rules are simple - ");
        System.out.println();
        Thread.sleep(2000);
        System.out.println("--> You need to make a sequence of crosses or O's to win based on whatever you choose");
        System.out.println(
                "_______________________________________________________________________________________________________________________");
        System.out.println();
        gamePlay(numbOfPlayer);
    }

    synchronized void gamePlay(int player) throws Exception {
        switch (player) {
            case 1: {
                System.out.println("Good luck " + TicTacToe.player1);
                System.out.println();
                gameSinglePlayer g = new gameSinglePlayer();
                g.start();
                g.join();
            }
                break;
            case 2: {
                System.out.print("Enter player 2 name - ");
                TicTacToe.player2 = sc.next();
                System.out.println();
                System.out.println("Good luck " + TicTacToe.player1 + " and " + TicTacToe.player2);
                gameDoublePlayer gdp = new gameDoublePlayer(TicTacToe.player1, TicTacToe.player2);
                player1 p1 = new player1(gdp);
                player2 p2 = new player2(gdp);
                if (gdp.ch1 == 'X') {
                    p1.start();
                    p2.start();
                    p2.join();
                } else {
                    p2.start();
                    p1.start();
                    p1.join();
                }
            }
            default: {
                System.out.println("Invalid choice");
            }
                break;
        }
    }
}

class gameSinglePlayer extends Thread {
    Scanner sc = new Scanner(System.in);
    char ar[][] = new char[3][3];
    char ch = 'X';
    char bot = 'X';

    public gameSinglePlayer() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                ar[i][j] = '0';
            }
        }
    }

    @Override
    public void run() {
        onePlayer();
    }

    synchronized void onePlayer() {
        boolean b = true;
        while (b) {
            System.out.print("Enter your character (X or O) - ");
            ch = sc.next().toUpperCase().charAt(0);
            System.out.println();
            switch (ch) {
                case 'X': {
                    bot = 'O';
                    b = false;
                }
                    break;
                case 'O': {
                    bot = 'X';
                    b = false;
                }
                    break;
                default: {
                    System.out.println("Invalid choice");
                    System.out.println("Choose again");
                }
                    break;
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        System.out.println();
        System.out.println("      1|      1|      1");
        System.out.println("       |       |       ");
        System.out.println("       |       |       ");
        System.out.println("1______|2______|3______");
        System.out.println("      2|      2|      2");
        System.out.println("       |       |       ");
        System.out.println("       |       |       ");
        System.out.println("1______|2______|3______");
        System.out.println("      3|      3|      3");
        System.out.println("       |       |       ");
        System.out.println("       |       |       ");
        System.out.println("1      |2      |3      ");
        System.out.println();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        while (!fullArray(ar)) {
            if (getResult(ar, ch) || getResult(ar, bot)) {
                break;
            }
            int y = 0;
            boolean x123 = false;
            try {
                System.out.print("Enter column index - ");
                sc.nextLine();
                y = sc.nextInt();
                System.out.println();
            } catch (InputMismatchException ie) {
                System.out.println("Please enter a number");
                System.out.println("Enter indices again");
                System.out.println();
                x123 = true;
            } finally {
                if (x123) {
                    continue;
                }
            }
            System.out.println();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            if (y == 1 || y == 2 || y == 3) {
                int x = 0;
                boolean x1234 = false;
                try {
                    System.out.print("Enter row index - ");
                    sc.nextLine();
                    x = sc.nextInt();
                    System.out.println();
                } catch (InputMismatchException ie) {
                    System.out.println("Please enter a number");
                    System.out.println("Enter indices again");
                    System.out.println();
                    x123 = true;
                } finally {
                    if (x123) {
                        continue;
                    }
                }
                if (x == 1 || x == 2 || x == 3) {
                    boolean insertion = insertIntoArray((x - 1), (y - 1), ch);
                    if (insertion) {
                        System.out.println("Index not empty");
                        System.out.println();
                    } else {
                        boolean b2 = true;
                        while (b2) {
                            int y1 = (int) (Math.random() * 3);
                            int x1 = (int) (Math.random() * 3);
                            boolean botInsertion = insertIntoArray((x1), (y1), bot);
                            if (botInsertion) {

                            } else {
                                System.out.println();
                                System.out.println("      1|      1|      1");
                                System.out.println("       |       |       ");
                                System.out.println("   " + print(ar[0][0]) + "   |   " + print(ar[1][0]) + "   |   "
                                        + print(ar[2][0]) + "   ");
                                System.out.println("1______|2______|3______");
                                System.out.println("      2|      2|      2");
                                System.out.println("       |       |       ");
                                System.out.println("   " + print(ar[0][1]) + "   |   " + print(ar[1][1]) + "   |   "
                                        + print(ar[2][1]) + "   ");
                                System.out.println("1______|2______|3______");
                                System.out.println("      3|      3|      3");
                                System.out.println("       |       |       ");
                                System.out.println("   " + print(ar[0][2]) + "   |   " + print(ar[1][2]) + "   |   "
                                        + print(ar[2][2]) + "   ");
                                System.out.println("1      |2      |3      ");
                                System.out.println();
                                b2 = false;
                            }
                        }
                    }
                } else {
                    System.out.println("Invalid Index");
                    System.out.println("Enter indices again");
                }
            } else {
                System.out.println("Invalid Index");
                System.out.println("Enter indices again");
            }
        }
        System.out.println();
        System.out.println("      1|      1|      1");
        System.out.println("       |       |       ");
        System.out.println("   " + print(ar[0][0]) + "   |   " + print(ar[1][0]) + "   |   "
                + print(ar[2][0]) + "   ");
        System.out.println("1______|2______|3______");
        System.out.println("      2|      2|      2");
        System.out.println("       |       |       ");
        System.out.println("   " + print(ar[0][1]) + "   |   " + print(ar[1][1]) + "   |   "
                + print(ar[2][1]) + "   ");
        System.out.println("1______|2______|3______");
        System.out.println("      3|      3|      3");
        System.out.println("       |       |       ");
        System.out.println("   " + print(ar[0][2]) + "   |   " + print(ar[1][2]) + "   |   "
                + print(ar[2][2]) + "   ");
        System.out.println("1      |2      |3      ");
        System.out.println();
        boolean playerResult = getResult(ar, ch);
        boolean botResult = getResult(ar, bot);
        if (playerResult) {
            System.out.println("Congratulations ! you win ");
        } else if (botResult) {
            System.out.println("Looks like I got lucky ");
            System.out.println("Better luck next time");
        } else {
            System.out.println("Seems we are tied");
            System.out.println("You played quite well");
        }
    }

    boolean insertIntoArray(int x, int y, char ch) {
        if (ar[x][y] == '0') {
            ar[x][y] = ch;
            return false;
        } else {
            return true;
        }
    }

    char print(char ch) {
        if (ch == '0') {
            return ' ';
        } else {
            return ch;
        }
    }

    boolean fullArray(char[][] arr) {
        boolean flag = false;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (arr[i][j] == '0') {
                    return flag;
                }
            }
        }
        return flag;
    }

    boolean getResult(char[][] ar, char ch) {
        if (ar[0][0] == ch && ar[0][1] == ch && ar[0][2] == ch) {
            return true;
        } else if (ar[1][0] == ch && ar[1][1] == ch && ar[1][2] == ch) {
            return true;
        } else if (ar[2][0] == ch && ar[2][1] == ch && ar[2][2] == ch) {
            return true;
        } else if (ar[0][0] == ch && ar[1][1] == ch && ar[2][2] == ch) {
            return true;
        } else if (ar[0][2] == ch && ar[1][1] == ch && ar[2][0] == ch) {
            return true;
        } else if (ar[0][0] == ch && ar[1][0] == ch && ar[2][0] == ch) {
            return true;
        } else if (ar[0][1] == ch && ar[1][1] == ch && ar[2][1] == ch) {
            return true;
        } else
            return ar[0][2] == ch && ar[1][2] == ch && ar[2][2] == ch;
    }
}

class gameDoublePlayer {
    String player1;
    String player2;
    char ch1;
    char ch2;
    boolean flag = true;
    Scanner sc = new Scanner(System.in);
    char ar[][] = new char[3][3];

    gameDoublePlayer(String player, String players) {
        this.player1 = player;
        this.player2 = players;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                ar[i][j] = '0';
            }
        }
        System.out.println("Let's do the toss");
        System.out.println();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        System.out.println("If it's a head then " + player1 + " wins");
        System.out.println("If it's a tail then " + player2 + " wins");
        System.out.println();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        System.out.println("Now tossing the coin");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        int toss = (int) (Math.random() * 3);
        if (toss == 1) {
            System.out.println("It's heads");
            System.out.println("Congratulations ! " + player1 + " wins the toss");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            boolean b = true;
            while (b) {
                System.out.print(player1 + " enter your choice (X or O) - ");
                ch1 = sc.next().toUpperCase().charAt(0);
                switch (ch1) {
                    case 'X': {
                        ch2 = 'O';
                        b = false;
                    }
                        break;
                    case 'O': {
                        ch2 = 'X';
                        b = false;
                    }
                        break;
                    default: {
                        System.out.println("Invalid choice ");
                        System.out.println();
                    }
                        break;
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            System.out.println(player1 + " plays " + ch1);
            System.out.println();
            System.out.println(player2 + " plays " + ch2);
            System.out.println();
            flag = true;
            System.out.println(player1 + "'s turn");
        } else {
            System.out.println("It's tails");
            System.out.println("Congratulations ! " + player2 + " wins the toss");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            boolean b = true;
            while (b) {
                System.out.print(player2 + " enter your choice (X or O) - ");
                ch2 = sc.next().toUpperCase().charAt(0);
                switch (ch2) {
                    case 'X': {
                        ch1 = 'O';
                        b = false;
                    }
                        break;
                    case 'O': {
                        ch1 = 'X';
                        b = false;
                    }
                        break;
                    default: {
                        System.out.println("Invalid choice ");
                        System.out.println();
                    }
                        break;
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            System.out.println(player1 + " plays " + ch1);
            System.out.println();
            System.out.println(player2 + " plays " + ch2);
            System.out.println();
            flag = false;
            System.out.println(player2 + "'s turn");
        }
        System.out.println();
        System.out.println("      1|      1|      1");
        System.out.println("       |       |       ");
        System.out.println("   " + print(ar[0][0]) + "   |   " + print(ar[1][0]) + "   |   "
                + print(ar[2][0]) + "   ");
        System.out.println("1______|2______|3______");
        System.out.println("      2|      2|      2");
        System.out.println("       |       |       ");
        System.out.println("   " + print(ar[0][1]) + "   |   " + print(ar[1][1]) + "   |   "
                + print(ar[2][1]) + "   ");
        System.out.println("1______|2______|3______");
        System.out.println("      3|      3|      3");
        System.out.println("       |       |       ");
        System.out.println("   " + print(ar[0][2]) + "   |   " + print(ar[1][2]) + "   |   "
                + print(ar[2][2]) + "   ");
        System.out.println("1      |2      |3      ");
        System.out.println();
    }

    synchronized void playerOne() {
        while (!flag) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        while (!fullArray(ar)) {
            if (getResult(ar, ch1) || getResult(ar, ch2)) {
                break;
            }
            System.out.println(TicTacToe.player1 + "'s turn");
            int y = 0;
            boolean x123 = false;
            try {
                System.out.print("Enter column index - ");
                sc.nextLine();
                y = sc.nextInt();
                System.out.println();
            } catch (InputMismatchException ie) {
                System.out.println("Please enter a number");
                System.out.println("Enter indices again");
                System.out.println();
                x123 = true;
            } finally {
                if (x123) {
                    continue;
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            if (y == 1 || y == 2 || y == 3) {
                int x = 0;
                boolean x1234 = false;
                try {
                    System.out.print("Enter row index - ");
                    sc.nextLine();
                    x = sc.nextInt();
                    System.out.println();
                } catch (InputMismatchException ie) {
                    System.out.println("Please enter a number");
                    System.out.println("Enter indices again");
                    System.out.println();
                    x123 = true;
                } finally {
                    if (x123) {
                        continue;
                    }
                }
                if (x == 1 || x == 2 || x == 3) {
                    boolean insertion = insertIntoArray((x - 1), (y - 1), ch1);
                    if (insertion) {
                        System.out.println("Index not empty");
                        System.out.println("Enter Indices again");
                        System.out.println();
                    } else {
                        System.out.println();
                        System.out.println("      1|      1|      1");
                        System.out.println("       |       |       ");
                        System.out.println("   " + print(ar[0][0]) + "   |   " + print(ar[1][0]) + "   |   "
                                + print(ar[2][0]) + "   ");
                        System.out.println("1______|2______|3______");
                        System.out.println("      2|      2|      2");
                        System.out.println("       |       |       ");
                        System.out.println("   " + print(ar[0][1]) + "   |   " + print(ar[1][1]) + "   |   "
                                + print(ar[2][1]) + "   ");
                        System.out.println("1______|2______|3______");
                        System.out.println("      3|      3|      3");
                        System.out.println("       |       |       ");
                        System.out.println("   " + print(ar[0][2]) + "   |   " + print(ar[1][2]) + "   |   "
                                + print(ar[2][2]) + "   ");
                        System.out.println("1      |2      |3      ");
                        System.out.println();
                        flag = false;
                        notify();
                        break;
                    }
                } else {
                    System.out.println("Invalid index");
                    System.out.println("Enter Indices again");
                }
            } else {
                System.out.println("Invalid index");
                System.out.println("Enter Indices again");
            }
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    synchronized void playerTwo() {
        while (flag) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        while (!fullArray(ar)) {
            if (getResult(ar, ch1) || getResult(ar, ch2)) {
                break;
            }
            System.out.println(TicTacToe.player2 + "'s turn");
            int y = 0;
            boolean x123 = false;

            try {
                System.out.print("Enter column index - ");
                sc.nextLine();
                y = sc.nextInt();
                System.out.println();
            } catch (InputMismatchException ie) {
                System.out.println("Please enter a number");
                System.out.println("Enter indices again");
                System.out.println();
                x123 = true;
            } finally {
                if (x123) {
                    continue;
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            if (y == 1 || y == 2 || y == 3) {
                int x = 0;
                boolean x1234 = false;
                try {
                    System.out.print("Enter row index - ");
                    sc.nextLine();
                    x = sc.nextInt();
                    System.out.println();
                } catch (InputMismatchException ie) {
                    System.out.println("Please enter a number");
                    System.out.println("Enter indices again");
                    System.out.println();
                    x123 = true;
                } finally {
                    if (x123) {
                        continue;
                    }
                }
                if (x == 1 || x == 2 || x == 3) {
                    boolean insertion = insertIntoArray((x - 1), (y - 1), ch2);
                    if (insertion) {
                        System.out.println("Index not empty");
                        System.out.println();
                    } else {
                        System.out.println();
                        System.out.println("      1|      1|      1");
                        System.out.println("       |       |       ");
                        System.out.println("   " + print(ar[0][0]) + "   |   " + print(ar[1][0]) + "   |   "
                                + print(ar[2][0]) + "   ");
                        System.out.println("1______|2______|3______");
                        System.out.println("      2|      2|      2");
                        System.out.println("       |       |       ");
                        System.out.println("   " + print(ar[0][1]) + "   |   " + print(ar[1][1]) + "   |   "
                                + print(ar[2][1]) + "   ");
                        System.out.println("1______|2______|3______");
                        System.out.println("      3|      3|      3");
                        System.out.println("       |       |       ");
                        System.out.println("   " + print(ar[0][2]) + "   |   " + print(ar[1][2]) + "   |   "
                                + print(ar[2][2]) + "   ");
                        System.out.println("1      |2      |3      ");
                        System.out.println();
                        flag = true;
                        notify();
                        break;
                    }
                } else {
                    System.out.println("Invalid index");
                    System.out.println("Enter Indices again");
                }
            } else {
                System.out.println("Invalid index");
                System.out.println("Enter Indices again");
            }
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    boolean insertIntoArray(int x, int y, char ch) {
        if (ar[x][y] == '0') {
            ar[x][y] = ch;
            return false;
        } else {
            return true;
        }
    }

    static char print(char ch) {
        if (ch == '0') {
            return ' ';
        } else {
            return ch;
        }
    }

    boolean fullArray(char[][] arr) {
        boolean flags = false;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (arr[i][j] == '0') {
                    return flags;
                }
            }
        }
        return flags;
    }

    boolean getResult(char[][] ar, char ch) {
        if (ar[0][0] == ch && ar[0][1] == ch && ar[0][2] == ch) {
            return true;
        } else if (ar[1][0] == ch && ar[1][1] == ch && ar[1][2] == ch) {
            return true;
        } else if (ar[2][0] == ch && ar[2][1] == ch && ar[2][2] == ch) {
            return true;
        } else if (ar[0][0] == ch && ar[1][1] == ch && ar[2][2] == ch) {
            return true;
        } else if (ar[0][2] == ch && ar[1][1] == ch && ar[2][0] == ch) {
            return true;
        } else if (ar[0][0] == ch && ar[1][0] == ch && ar[2][0] == ch) {
            return true;
        } else if (ar[0][1] == ch && ar[1][1] == ch && ar[2][1] == ch) {
            return true;
        } else
            return ar[0][2] == ch && ar[1][2] == ch && ar[2][2] == ch;
    }
}

class player1 extends Thread {
    gameDoublePlayer gdp;

    player1(gameDoublePlayer gdp) {
        this.gdp = gdp;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            gdp.playerOne();
            if (gdp.getResult(gdp.ar, gdp.ch1) || gdp.fullArray(gdp.ar)) {
                break;
            }
        }
    }
}

class player2 extends Thread {
    gameDoublePlayer gdp;

    player2(gameDoublePlayer gdp) {
        this.gdp = gdp;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            gdp.playerTwo();
            if (gdp.getResult(gdp.ar, gdp.ch2) || gdp.fullArray(gdp.ar)) {
                break;
            }
        }
        if (gdp.getResult(gdp.ar, gdp.ch1)) {
            System.out.println("Well played " + TicTacToe.player1);
            System.out.println("Congratulations on your victory");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
            System.out.println("You played well too " + TicTacToe.player2);
            System.out.println("Better Luck next time");
        } else if (gdp.getResult(gdp.ar, gdp.ch2)) {
            System.out.println("Congratulations on your victory " + TicTacToe.player2);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
            System.out.println("You played well too " + TicTacToe.player1);
            System.out.println("Better Luck next time");
        } else {
            System.out.println("You both are equally skilled");
            System.out.println("It's a tie but both played well");
        }
    }
}

class HangmanGame {
    static Connection con;
    static String player1;

    public void playHangmanGame(int player, String username) throws Exception {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/console_chaos", "root", "");
        if (con == null) {
            System.out.println("Game under maintenance");
        } else {
            player1 = username;
            System.out.println("Hello ! Welcome to Hangman");
            Thread.sleep(3000);
            System.out.println();
            System.out.println("I am your assistant Pixel-wiz ");
            System.out.println();
            Thread.sleep(3000);
            System.out.println("Here's a quick tour to the rules - ");
            Thread.sleep(3000);
            Rules r1 = new Rules();
            r1.start();
            r1.join();
            System.out.println("Let's Start");
            System.out.println();
            Thread.sleep(1000);
            switch (player) {
                case 1: {
                    SinglePlayer sp = new SinglePlayer();
                    sp.start();
                    sp.join();
                }
                    break;
                case 2: {
                    DoublePlayer dp = new DoublePlayer();
                    dp.start();
                    dp.join();
                }
                    break;
                default: {
                    System.out.println("Oops ! I did not understand your input ");
                    System.out.println("Try again");
                    System.out.println();
                }
                    break;
            }
        }
    }
}

class SinglePlayer extends Thread {
    Scanner sc = new Scanner(System.in);

    @Override
    public void run() {
        try {
            String q = "SELECT * FROM hangman_words where id=? ";
            PreparedStatement pst = HangmanGame.con.prepareCall(q);
            System.out.println();
            System.out.println("Here are 5 easy words");
            System.out.println();
            for (int i = 0; i < 3; i++) {
                int ran = (int) (Math.random() * 100);
                ran++;
                pst.setInt(1, ran);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    String word = rs.getString("word");
                    String hint = rs.getString("Hint");
                    singlePlayer(word, hint);
                }
                System.out.println();
                System.out.println("Here are 3 medium words");
                System.out.println();
            }
            for (int i = 0; i < 2; i++) {
                int ran = (int) (Math.random() * 100);
                ran += 101;
                pst.setInt(1, ran);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    String word = rs.getString("word");
                    String hint = rs.getString("hint");
                    singlePlayer(word, hint);
                }
            }
            System.out.println();
            System.out.println("Here's one hard word");
            System.out.println();
            int ran = (int) (Math.random() * 100);
            ran += 201;
            pst.setInt(1, ran);
            ResultSet rs = pst.executeQuery();
            String hint = "";
            String word = "";
            while (rs.next()) {
                word = rs.getString("word");
                hint = rs.getString("hint");
            }
            singlePlayer(word, hint);
        } catch (SQLException ex) {
            System.out.println("Hangman under maintenance");
        }
    }

    boolean singlePlayer(String word, String hint) {
        boolean correct = false;
        String wordString = word.toLowerCase();
        char[] wordArray = wordString.toCharArray();
        char[] answer = new char[wordArray.length];
        for (int i = 0; i < answer.length; i++) {
            answer[i] = '_';
        }
        boolean b = true;
        int x = 7;
        // int y = answer.length;
        while (b) {
            System.out.println("Hint : " + hint);
            System.out.println(
                    "***********************************************************************************************");
            switch (x) {
                case 7: {
                    System.out.println("|");
                    System.out.println("|");
                    System.out.println("|");
                    System.out.println("|");
                    System.out.println("|");
                    System.out.println("|");
                    System.out.println("|");
                    System.out.println("|");
                }
                    break;
                case 6: {
                    System.out.println("|");
                    System.out.println("|");
                    System.out.println("|");
                    System.out.println("|");
                    System.out.println("|                       O           ");
                    System.out.println("|");
                    System.out.println("|");
                    System.out.println("|");
                }
                    break;
                case 5: {
                    System.out.println("|");
                    System.out.println("|");
                    System.out.println("|");
                    System.out.println("|");
                    System.out.println("|                       O           ");
                    System.out.println("|                       |           ");
                    System.out.println("|                       |           ");
                    System.out.println("|");
                }
                    break;
                case 4: {
                    System.out.println("|");
                    System.out.println("|");
                    System.out.println("|");
                    System.out.println("|");
                    System.out.println("|                       O           ");
                    System.out.println("|                       |           ");
                    System.out.println("|                      -|-           ");
                    System.out.println("|                      | |          ");
                }
                    break;
                case 3: {
                    System.out.println("|");
                    System.out.println("|");
                    System.out.println("|");
                    System.out.println("|");
                    System.out.println("|                       O           ");
                    System.out.println("|                      -|-           ");
                    System.out.println("|                      -|-           ");
                    System.out.println("|                      | |          ");
                }
                    break;
                case 2: {
                    System.out.println("|");
                    System.out.println("|");
                    System.out.println("|                        _            ");
                    System.out.println("|                       |            ");
                    System.out.println("|                       O            ");
                    System.out.println("|                      -|-           ");
                    System.out.println("|                      -|-           ");
                    System.out.println("|                      | |          ");
                }
                    break;
                case 1: {
                    System.out.println("|");
                    System.out.println("|                          |          ");
                    System.out.println("|                        __|          ");
                    System.out.println("|                       |            ");
                    System.out.println("|                       O            ");
                    System.out.println("|                      -|-           ");
                    System.out.println("|                      -|-           ");
                    System.out.println("|                      | |          ");
                }
                    break;
                case 0: {
                    System.out.println("|                         |          ");
                    System.out.println("|                         |          ");
                    System.out.println("|                        _|          ");
                    System.out.println("|                       |            ");
                    System.out.println("|                       O            ");
                    System.out.println("|                      -|-           ");
                    System.out.println("|                      -|-           ");
                    System.out.println("|                      | |          ");
                    System.out.println("|");
                    System.out.println("|                      " + wordString);
                    b = false;
                    correct = false;
                    System.out.println("|");
                    System.out.println("|                    Game   Over     ");
                    System.out.println();
                    System.out.println(
                            "-------#-------#-------#-------#-------#-------#-------#-------#-------#-------#-------#-------#-------");
                    System.out.println();
                    System.out.println();
                    System.out.println(
                            "-------#-------#-------#-------#-------#-------#-------#-------#-------#-------#-------#-------#-------");
                    System.out.println();
                }
                    break;
            }
            System.out.println();
            System.out.print("           ");
            for (char i : answer) {
                System.out.print(i + " ");
            }
            System.out.println();
            int j;
            for (j = 0; j < answer.length; j++) {
                if (answer[j] != wordArray[j]) {
                    break;
                }
            }
            if (j == answer.length) {
                System.out.println();
                System.out.println("Congratulations you guessed the word");
                System.out.println();
                System.out.println(
                        "-------#-------#-------#-------#-------#-------#-------#-------#-------#-------#-------#-------#-------");
                System.out.println();
                System.out.println();
                System.out.println(
                        "-------#-------#-------#-------#-------#-------#-------#-------#-------#-------#-------#-------#-------");
                System.out.println();
                b = false;
                correct = true;
            }
            if (b) {
                System.out.println();
                System.out.println(" " + x + " lives remaining");
                System.out.println();
                char ans;
                try {
                    ans = sc.next().toLowerCase().charAt(0);
                } catch (InputMismatchException ex) {
                    System.out.println("Please Enter a valid character");
                    System.out.println();
                    sc.nextLine();
                    continue;
                }
                boolean flag = false;
                for (int i = 0; i < answer.length; i++) {
                    if (ans == wordArray[i]) {
                        flag = true;
                        answer[i] = ans;
                    }
                }
                if (!flag) {
                    System.out.println("Character not found");
                    System.out.println();
                    x--;
                }
            }
        }
        return correct;
    }
}

class DoublePlayer extends Thread {
    Scanner sc = new Scanner(System.in);
    static String p2;

    @Override
    public void run() {
        System.out.println();
        String p1 = HangmanGame.player1;
        System.out.print("Enter name of Player 2 - ");
        p2 = sc.nextLine();
        System.out.println();
        int point1 = 0, point2 = 0;
        boolean x = true;
        while (x) {
            System.out.println();
            System.out.print(p1 + " Enter word for " + p2 + " to guess - ");
            String word1 = sc.next().toLowerCase();
            System.out.println();
            sc.nextLine();
            System.out.print(p1 + " Enter hint for " + p2 + " - ");
            String hint1 = sc.nextLine();
            System.out.println();
            boolean chance1 = new SinglePlayer().singlePlayer(word1, hint1);
            if (chance1 == true) {
                point2 += 1;
                point1 -= 1;
            } else {
                point2 -= 1;
                point1 += 1;
            }
            System.out.println();
            System.out.print(p2 + " Enter word for " + p1 + " to guess - ");
            String word2 = sc.next();
            System.out.println();
            sc.nextLine();
            System.out.print(p2 + " Enter hint for " + p1 + " - ");
            String hint2 = sc.nextLine();
            System.out.println();
            boolean chance2 = new SinglePlayer().singlePlayer(word2, hint2);
            if (chance2 == true) {
                point1 += 1;
                point2 -= 1;
            } else {
                point1 -= 1;
                point2 += 1;
            }
            System.out.println(p1 + " you are playing with " + point1 + " points");
            System.out.println(p2 + " you are playing with " + point2 + " points");
            if (point1 > point2) {
                System.out.println(p1 + " Wins");
            } else if (point1 < point2) {
                System.out.println(p2 + " Wins");
            } else {
                System.out.println("There is a tie between both players");
            }
            System.out.println("Thank You !");
            x = false;
        }
    }
}

class Rules extends Thread {
    @Override
    public void run() {
        System.out.println("__________________________________________________________________________");
        System.out.println();
        System.out.println("---> All you need to do is guess the word letter by letter using the hints provided.");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
        System.out.println();
        System.out.println("---> Be sure to do it before you hang the man");
        System.out.println();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
        System.out.println("---> You've got 7 lives .");
        System.out.println("__________________________________________________________________________");
        System.out.println();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
        System.out.println("Good luck !");
        System.out.println();
    }
}

// Class representing a contact
class Contact {
    String name;
    String phoneNumber;
    Contact left, right;

    public Contact(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        left = right = null;
    }
}

// Class representing the Binary Search Tree
class Phonebook {
    private Contact root;

    public Phonebook() {
        root = null;
    }

    // Method to insert a new contact
    public void insert(String name, String phoneNumber) {
        root = insertRec(root, name, phoneNumber);
        long l=Long.parseLong(phoneNumber);
        try {
            String query="INSERT INTO contacts VALUES (?,?)";
            PreparedStatement pst=Turbo_OS.con.prepareStatement(query);
            pst.setLong(1, l);
            pst.setString(2,name);
            pst.execute();
        } catch (SQLException e) {
            
        }
    }

    private Contact insertRec(Contact root, String name, String phoneNumber) {
        if (root == null) {
            return new Contact(name, phoneNumber);
        }
        if (name.compareTo(root.name) < 0) {
            root.left = insertRec(root.left, name, phoneNumber);
        } else if (name.compareTo(root.name) > 0) {
            root.right = insertRec(root.right, name, phoneNumber);
        } else {
            // Update phone number if contact already exists
            root.phoneNumber = phoneNumber;
        }
        return root;
    }

    // Method to search for a contact
    public String search(String name) {
        return searchRec(root, name);
    }

    private String searchRec(Contact root, String name) {
        if (root == null) {
            return "Contact not found.";
        }
        if (name.equals(root.name)) {
            return "Name: " + root.name + ", Phone Number: " + root.phoneNumber;
        }
        return name.compareTo(root.name) < 0 ? searchRec(root.left, name) : searchRec(root.right, name);
    }

    // Method to delete a contact
    public void delete(String name) {
        root = deleteRec(root, name);
    }

    private Contact deleteRec(Contact root, String name) {
        if (root == null) {
            return root;
        }
        if (name.compareTo(root.name) < 0) {
            root.left = deleteRec(root.left, name);
        } else if (name.compareTo(root.name) > 0) {
            root.right = deleteRec(root.right, name);
        } else {
            // Node with only one child or no child
            if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            }

            // Node with two children: Get the inorder successor (smallest in the right
            // subtree)
            root.name = minValue(root.right);
            root.phoneNumber = search(root.name).split(", ")[1].split(": ")[1]; // Get the phone number of the successor
            root.right = deleteRec(root.right, root.name);
        }
        return root;
    }

    private String minValue(Contact root) {
        String minv = root.name;
        while (root.left != null) {
            minv = root.left.name;
            root = root.left;
        }
        return minv;
    }

    // Method to display contacts in sorted order
    public void display() {
        displayRec(root);
    }

    private void displayRec(Contact root) {
        if (root != null) {
            displayRec(root.left);
            System.out.println("Name: " + root.name + ", Phone Number: " + root.phoneNumber);
            displayRec(root.right);
        }
    }
}

// Main class to run the phonebook application
class MyPhoneBook {
    void contacts() throws Exception {
        Scanner scanner = new Scanner(System.in);
        Phonebook phonebook = new Phonebook();
        int choice;

        do {
            System.out.println("\nPhonebook Menu:");
            System.out.println("1. Add Contact");
            System.out.println("2. Update Contact");
            System.out.println("3. Delete Contact");
            System.out.println("4. Search Contact by Name");
            System.out.println("5. View All Contacts");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter Name: ");
                    String addName = scanner.nextLine();
                    System.out.print("Enter Phone Number: ");
                    String addPhoneNumber = scanner.nextLine();
                    try {
                        if (addPhoneNumber.length() != 10) {
                            throw new NumberTooLongException("Please enter number of 10 digits");
                        }
                    } catch (NumberTooLongException ex) {
                        System.out.println(ex);
                        continue;
                    }
                    phonebook.insert(addName, addPhoneNumber);
                    System.out.println("Contact added successfully.");
                    break;
                case 2:
                    System.out.print("Enter Name to Update: ");
                    String updateName = scanner.nextLine();
                    System.out.print("Enter New Phone Number: ");
                    String updatePhoneNumber = scanner.nextLine();
                    try {
                        if (updatePhoneNumber.length() != 10) {
                            throw new NumberTooLongException("Please enter number of 10 digits");
                        }
                    } catch (NumberTooLongException ex) {
                        System.out.println(ex);
                        continue;
                    }
                    phonebook.insert(updateName, updatePhoneNumber);
                    System.out.println("Contact updated successfully.");
                    break;
                case 3:
                    System.out.print("Enter Name to Delete: ");
                    String deleteName = scanner.nextLine();
                    phonebook.delete(deleteName);
                    System.out.println("Contact deleted successfully.");
                    break;
                case 4:
                    System.out.print("Enter Name to Search: ");
                    String searchName = scanner.nextLine();
                    String result = phonebook.search(searchName);
                    System.out.println(result);
                    break;
                case 5:
                    System.out.println("All Contacts:");
                    phonebook.display();
                    break;
                case 6:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (choice != 6);
        scanner.close();
    }
}

