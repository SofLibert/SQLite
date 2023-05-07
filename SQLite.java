import java.sql.*;

public class SQLite {
    public static void main(String[] args) throws ClassNotFoundException, SQLException{
        conn.Conn();
        conn.CreateDB();

        conn.ReadDB();
        conn.CloseDB();
    }
}

class conn {
    public static Connection conn;
    public static Statement statmt;
    public static ResultSet resSet;

    // --------ПОДКЛЮЧЕНИЕ К БАЗЕ ДАННЫХ--------
    public static void Conn() throws ClassNotFoundException, SQLException
    {
        conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:D:\\Programs\\SQLite\\Лабораторная_№7.s3db");

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
    public static void ReadDB() throws SQLException
    {
        resSet = statmt.executeQuery("SELECT Название, Сайт FROM Школы");

        System.out.println("Выполняю запрос!");
        System.out.println();

        while(resSet.next())
        {
            String Name = resSet.getString("Название");
            String Website = resSet.getString("Сайт");
            System.out.print("Название: " + Name + " ");
            System.out.print("Сайт: " + Website);
            System.out.println();
        }

        System.out.println();
        System.out.println("Запрос выполнен!");
        System.out.println();

        resSet = statmt.executeQuery("SELECT Руководитель FROM Школы");

        System.out.println("Выполняю запрос!");
        System.out.println();

        while(resSet.next())
        {
            String Supervisor = resSet.getString("Руководитель");
            System.out.print("Руководитель: " + Supervisor + " ");
            System.out.println();
        }

        System.out.println();
        System.out.println("Запрос выполнен!");
        System.out.println();

        resSet = statmt.executeQuery("SELECT Название, Телефон, Код AS Номер_в_списке FROM Школы " +
                "GROUP BY Название");

        System.out.println("Выполняю запрос!");
        System.out.println();

        while(resSet.next())
        {
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

        resSet = statmt.executeQuery("SELECT Название, Адрес, Код AS Номер_в_списке FROM Школы " +
                "GROUP BY Название");

        System.out.println("Выполняю запрос!");
        System.out.println();

        while(resSet.next())
        {
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