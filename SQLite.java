import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.sql.*;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class TextFieldTest extends JFrame {
    JTextField smallField;
    public TextFieldTest() {
        super("Введите пароль!");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Создание текстовых полей
        smallField = new JTextField(15);
        smallField.setToolTipText("Короткое поле");

        // Слушатель окончания ввода
        smallField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Отображение введенного текста
                if (smallField.getText().equals("Admin")) {
                    JOptionPane.showMessageDialog(TextFieldTest.this, "Вы подключены как администратор!");

                    try {
                        Admin.Conn();
                        Admin.CreateDB();
                        //Admin.WriteDB(); Если нужно поменять или добавить значения в таблице.
                        Admin.SampleRequest();
                        Admin.GroupRequest();
                        Admin.SelectQueryWithParameter();
                        Admin.CloseDB();
                    }
                    catch (ClassNotFoundException | SQLException ex) {
                        throw new RuntimeException(ex);
                    }

                }
                else {
                    JOptionPane.showMessageDialog(TextFieldTest.this, "Вы подключены как сторонний пользователь!");

                    try {
                        User.Conn();
                        User.CreateDB();
                        User.WriteDB();
                        User.SampleRequest();
                        User.GroupRequest();
                        User.SelectQueryWithParameter();
                        User.CloseDB();
                    }
                    catch (ClassNotFoundException | SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        // Создание панели с текстовыми полями
        JPanel contents = new JPanel(new FlowLayout(FlowLayout.LEFT));
        contents.add(smallField);
        setContentPane(contents);
        // Определяем размер окна и выводим его на экран
        setSize(400, 130);
        setVisible(true);
    }

    public static void main(String[] args){
        new TextFieldTest();

        try {
            File fXmlFile = new File("Школы.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("Школы");
            System.out.println();
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                {
                    Element eElement = (Element) nNode;
                    System.out.println("Код: " + eElement.getElementsByTagName("Код").item(0).getTextContent());
                    System.out.println("Название: " + eElement.getElementsByTagName("Полное_x0020_наименование_x0020_организации").item(0).getTextContent());
                    System.out.println("Адрес: " + eElement.getElementsByTagName("Адрес").item(0).getTextContent());
                    System.out.println("Телефон: " + eElement.getElementsByTagName("Телефон").item(0).getTextContent());
                    System.out.println("Руководитель: " + eElement.getElementsByTagName("Руководитель").item(0).getTextContent());
                    System.out.println("Сайт: " + eElement.getElementsByTagName("Сайт").item(0).getTextContent());
                    System.out.println("E-mail: " + eElement.getElementsByTagName("E-mail").item(0).getTextContent());
                    System.out.println();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

interface SampleRequestIF{
    static void SampleRequest(){
        //Тело интерфейса запроса на выборку.
    }
}

interface GroupRequestIF{
    static void GroupRequest(){
        //Тело интерфейса группового запроса.
    }
}

interface SelectQueryWithParameterIF{
    static void SelectQueryWithParameter(){
        //Тело интерфейса запроса на выборку с параметром.
    }
}

class Admin implements SampleRequestIF, GroupRequestIF, SelectQueryWithParameterIF{
    public static Connection conn;
    public static Statement statmt;
    public static ResultSet resSet;

    // --------ПОДКЛЮЧЕНИЕ К БАЗЕ ДАННЫХ--------
    public static void Conn() throws ClassNotFoundException, SQLException
    {
        Scanner sc = new Scanner(System.in);

        conn = null;
        Class.forName("org.sqlite.JDBC");

        System.out.println();
        System.out.println("Введите расположение базы данных!"); //C:\Users\Kirill\IdeaProjects\SQLite\Лабораторная_№7.s3db

        String url = sc.nextLine();

        conn = DriverManager.getConnection("jdbc:sqlite:" + url);

        System.out.println();
        System.out.println("База данных подключена!");
        System.out.println();
    }

    // --------Создание таблицы--------
    public static void CreateDB() throws SQLException
    {
        statmt = conn.createStatement();
        statmt.execute("CREATE TABLE if not exists 'Школы' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'Название' TEXT NULL, 'Адрес' TEXT NULL, 'Телефон' TEXT NULL, 'Руководитель' TEXT NULL, 'Сайт' TEXT NULL, 'E-mail' TEXT NULL);");

        System.out.println("Таблица создана или уже существует!");
        System.out.println();
    }

    // --------Заполнение таблицы--------
    public static void WriteDB() throws SQLException
    {
        statmt.execute("INSERT INTO 'Школы' ('Название', 'Адрес', 'Телефон', 'Руководитель', 'Сайт', 'E-mail') VALUES ('Муниципальное бюджетное общеобразовательное учреждение «Гимназия № 1 имени Н.М. Пржевальского» города Смоленска (МБОУ «Гимназия № 1 им. Н.М. Пржевальского»)', '214000, г. Смоленск, ул. Ленина, 4', '+7 (4812) 38-21-80', 'Слободич Анжела Николаевна', 'smolgip.ru', 'pregik.sm@mail.ru'); ");

        System.out.println("Таблица заполнена!");
        System.out.println();
    }

    // -------- Вывод таблицы--------
    public static void SampleRequest() throws SQLException {
        Scanner sc = new Scanner(System.in);

        System.out.println("Введите запрос!");
        System.out.println();

        String Line = sc.nextLine();

        resSet = statmt.executeQuery(Line); //SELECT Название, Сайт FROM Школы

        System.out.println();
        System.out.println("Выполняю запрос!");
        System.out.println();

        while (resSet.next()) {
            String strLine = Line.replace(",", "");
            String[] Words = strLine.split(" ");
            for (String word : Words){
                if (word.equals("Название") || word.equals("Адрес") || word.equals("Телефон") || word.equals("Руководитель") || word.equals("Сайт") || word.equals("E-mail")) {
                    String wordstr = resSet.getString(word);
                    System.out.print(word + ": " + wordstr + " ");
                }
            }
            System.out.println();
        }

        System.out.println();
        System.out.println("Запрос выполнен!");
        System.out.println();

        System.out.println("Введите запрос!");
        System.out.println();

        resSet = statmt.executeQuery("SELECT Руководитель FROM Школы");

        System.out.println("Выполняю запрос!");
        System.out.println();

        while (resSet.next()) {
            String Supervisor = resSet.getString("Руководитель");
            System.out.print("Руководитель: " + Supervisor + " ");
            System.out.println();
        }

        System.out.println();
        System.out.println("Запрос выполнен!");
        System.out.println();
    }

    public static void GroupRequest() throws SQLException {
        System.out.println("Введите запрос!");
        System.out.println();

        resSet = statmt.executeQuery("SELECT Название, Телефон, Код AS Номер_в_списке FROM Школы " +
                "GROUP BY Название");

        System.out.println("Выполняю запрос!");
        System.out.println();

        while (resSet.next()) {
            String Name = resSet.getString("Название");
            int Id = resSet.getInt("Номер_в_списке");
            String Telephone = resSet.getString("Телефон");
            System.out.print("Название: " + Name + " ");
            System.out.print("Номер_в_списке: " + Id + " ");
            System.out.print("Телефон: " + Telephone);
            System.out.println();
        }

        System.out.println();
        System.out.println("Запрос выполнен!");
        System.out.println();

        System.out.println("Введите запрос!");
        System.out.println();

        resSet = statmt.executeQuery("SELECT Название, Адрес, Код AS Номер_в_списке FROM Школы " +
                "GROUP BY Название");

        System.out.println("Выполняю запрос!");
        System.out.println();

        while (resSet.next()) {
            String Name = resSet.getString("Название");
            int Id = resSet.getInt("Номер_в_списке");
            String Address = resSet.getString("Адрес");
            System.out.print("Название: " + Name + " ");
            System.out.print("Номер_в_списке: " + Id + " ");
            System.out.print("Адрес: " + Address);
            System.out.println();
        }

        System.out.println();
        System.out.println("Запрос выполнен!");
        System.out.println();
    }

    public static void SelectQueryWithParameter() throws SQLException {

        System.out.println("Введите запрос!");
        System.out.println();

        resSet = statmt.executeQuery("SELECT * FROM Школы " +
                "WHERE Код > 20");

        System.out.println("Выполняю запрос!");
        System.out.println();

        while(resSet.next())
        {
            int Id = resSet.getInt("Код");
            String Name = resSet.getString("Название");
            String Address = resSet.getString("Адрес");
            String Telephone = resSet.getString("Телефон");
            String Website = resSet.getString("Сайт");
            String Supervisor = resSet.getString("Руководитель");
            String Email = resSet.getString("E-mail");
            System.out.print("Код: " + Id + " ");
            System.out.print("Название: " + Name + " ");
            System.out.print("Адрес: " + Address + " ");
            System.out.print("Телефон: " + Telephone + " ");
            System.out.print("Сайт: " + Website + " ");
            System.out.print("Руководитель: " + Supervisor + " ");
            System.out.print("E-mail: " + Email + " ");
            System.out.println();
        }

        System.out.println();
        System.out.println("Запрос выполнен!");
        System.out.println();

        resSet = statmt.executeQuery("SELECT Школы.Телефон FROM Школы " +
                "INNER JOIN Телефоны " +
                "ON Телефоны.Телефон = Школы.Телефон " +
                "WHERE Школы.Код <= 10");

        while(resSet.next()) {
            String Telephone = resSet.getString("Телефон");
            System.out.print("Телефон: " + Telephone);
            System.out.println();
        }

        System.out.println();
        System.out.println("Запрос выполнен!");
        System.out.println();
    }

    // --------ОТКЛЮЧЕНИЕ OT БАЗЫ ДАННЫХ--------
    public static void CloseDB() throws SQLException
    {
        conn.close();
        statmt.close();
        resSet.close();

        System.out.println("База данных отключена!");
    }
}

class User implements SampleRequestIF, GroupRequestIF, SelectQueryWithParameterIF{
    public static Connection conn;
    public static Statement statmt;
    public static ResultSet resSet;

    // --------ПОДКЛЮЧЕНИЕ К БАЗЕ ДАННЫХ--------
    public static void Conn() throws ClassNotFoundException, SQLException
    {
        Scanner sc = new Scanner(System.in);

        conn = null;
        Class.forName("org.sqlite.JDBC");

        System.out.println();
        System.out.println("Введите расположение базы данных!");

        String url = sc.nextLine();

        conn = DriverManager.getConnection("jdbc:sqlite:" + url);

        System.out.println();
        System.out.println("База данных подключена!");
        System.out.println();
    }

    // --------Создание таблицы--------
    public static void CreateDB() throws SQLException {
        statmt = conn.createStatement();

        System.out.println("У вас нет прав создавать новые таблицы!");
        System.out.println();
    }

    // --------Заполнение таблицы--------
    public static void WriteDB()
    {
        System.out.println("У вас нет прав редактировать данные в таблице!");
        System.out.println();
    }

    // -------- Вывод таблицы--------
    public static void SampleRequest() throws SQLException {
        Scanner sc = new Scanner(System.in);

        System.out.println("Введите запрос!");
        System.out.println();

        String Line = sc.nextLine();

        resSet = statmt.executeQuery(Line); //SELECT Название, Сайт FROM Школы

        System.out.println();
        System.out.println("Выполняю запрос!");
        System.out.println();

        while (resSet.next()) {
            String strLine = Line.replace(",", "");
            String[] Words = strLine.split(" ");
            for (String word : Words){
                if (word.equals("Название") || word.equals("Адрес") || word.equals("Телефон")|| word.equals("Руководитель")|| word.equals("Сайт") || word.equals("E-mail")) {
                    String wordstr = resSet.getString(word);
                    System.out.print(word + ": " + wordstr + " ");
                }
            }
            System.out.println();
        }

        System.out.println();
        System.out.println("Запрос выполнен!");
        System.out.println();

        System.out.println("Введите запрос!");
        System.out.println();

        resSet = statmt.executeQuery("SELECT Руководитель FROM Школы");

        System.out.println("Выполняю запрос!");
        System.out.println();

        while (resSet.next()) {
            String Supervisor = resSet.getString("Руководитель");
            System.out.print("Руководитель: " + Supervisor + " ");
            System.out.println();
        }

        System.out.println();
        System.out.println("Запрос выполнен!");
        System.out.println();
    }

    public static void GroupRequest() throws SQLException {
        System.out.println("Введите запрос!");
        System.out.println();

        resSet = statmt.executeQuery("SELECT Название, Телефон, Код AS Номер_в_списке FROM Школы " +
                "GROUP BY Название");

        System.out.println("Выполняю запрос!");
        System.out.println();

        while (resSet.next()) {
            String Name = resSet.getString("Название");
            int Id = resSet.getInt("Номер_в_списке");
            String Telephone = resSet.getString("Телефон");
            System.out.print("Название: " + Name + " ");
            System.out.print("Номер_в_списке: " + Id + " ");
            System.out.print("Телефон: " + Telephone);
            System.out.println();
        }

        System.out.println();
        System.out.println("Запрос выполнен!");
        System.out.println();

        System.out.println("Введите запрос!");
        System.out.println();

        resSet = statmt.executeQuery("SELECT Название, Адрес, Код AS Номер_в_списке FROM Школы " +
                "GROUP BY Название");

        System.out.println("Выполняю запрос!");
        System.out.println();

        while (resSet.next()) {
            String Name = resSet.getString("Название");
            int Id = resSet.getInt("Номер_в_списке");
            String Address = resSet.getString("Адрес");
            System.out.print("Название: " + Name + " ");
            System.out.print("Номер_в_списке: " + Id + " ");
            System.out.print("Адрес: " + Address);
            System.out.println();
        }

        System.out.println();
        System.out.println("Запрос выполнен!");
        System.out.println();
    }

    public static void SelectQueryWithParameter() throws SQLException {

        System.out.println("Введите запрос!");
        System.out.println();

        resSet = statmt.executeQuery("SELECT * FROM Школы " +
                "WHERE Код > 20");

        System.out.println("Выполняю запрос!");
        System.out.println();

        while(resSet.next()) {
            int Id = resSet.getInt("Код");
            String Name = resSet.getString("Название");
            String Address = resSet.getString("Адрес");
            String Telephone = resSet.getString("Телефон");
            String Website = resSet.getString("Сайт");
            String Supervisor = resSet.getString("Руководитель");
            String Email = resSet.getString("E-mail");
            System.out.print("Код: " + Id + " ");
            System.out.print("Название: " + Name + " ");
            System.out.print("Адрес: " + Address + " ");
            System.out.print("Телефон: " + Telephone + " ");
            System.out.print("Сайт: " + Website + " ");
            System.out.print("Руководитель: " + Supervisor + " ");
            System.out.print("E-mail: " + Email + " ");
            System.out.println();
        }

        System.out.println();
        System.out.println("Запрос выполнен!");
        System.out.println();

        System.out.println("Введите запрос!");
        System.out.println();

        resSet = statmt.executeQuery("SELECT Школы.Телефон FROM Школы " +
                "INNER JOIN Телефоны " +
                "ON Телефоны.Телефон = Школы.Телефон " +
                "WHERE Школы.Код <= 10");

        while(resSet.next()) {
            String Telephone = resSet.getString("Телефон");
            System.out.print("Телефон: " + Telephone);
            System.out.println();
        }

        System.out.println();
        System.out.println("Запрос выполнен!");
        System.out.println();
    }

    // --------ОТКЛЮЧЕНИЕ OT БАЗЫ ДАННЫХ--------
    public static void CloseDB() throws SQLException
    {
        conn.close();
        statmt.close();
        resSet.close();

        System.out.println("База данных отключена!");
    }
}