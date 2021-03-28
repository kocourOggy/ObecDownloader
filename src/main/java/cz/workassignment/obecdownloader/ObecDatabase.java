package cz.workassignment.obecdownloader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author stepaiv3
 */
public final class ObecDatabase {

    private String url = null;
    private Connection connection = null;

    private final String CREATE_OBEC_TABLE_SQL = "CREATE TABLE IF NOT EXISTS Obec (\n"
            + "	Kod integer NOT NULL,\n"
            + "	Nazev varchar(50) NOT NULL,\n"
            + "	PRIMARY KEY(Kod)\n"
            + ");";

    private final String CREATE_CASTOBCE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS CastObce (\n"
            + "	Kod integer NOT NULL,\n"
            + "	Nazev varchar(50) NOT NULL,\n"
            + "	KodObce integer NOT NULL,\n"
            + "	PRIMARY KEY(Kod),\n"
            + "	FOREIGN KEY(KodObce) REFERENCES Obec(Kod)\n"
            + ");";

    private final String INSERT_OBEC_SQL = "INSERT INTO Obec VALUES (?, ?);";
    private final String INSERT_CASTOBCE_SQL = "INSERT INTO CastObce VALUES (?, ?, ?);";

    private final String SELECT_OBECKOD_SQL = "SELECT Kod FROM Obec WHERE Kod=?;";

    public ObecDatabase(String url) throws SQLException {
        this.url = url;
        this.openConnection();
        this.createObecTable();
        this.createCastObceTable();
    }

    private void createTableFromString(String sql) throws SQLException {
        try ( Statement statement = this.connection.createStatement()) {
            statement.execute(sql);
        }
    }

    private void createObecTable() throws SQLException {
        this.createTableFromString(CREATE_OBEC_TABLE_SQL);
    }

    private void createCastObceTable() throws SQLException {
        this.createTableFromString(CREATE_CASTOBCE_TABLE_SQL);
    }

    public void insertAllObec(List<Obec> listOfObec) {
        for (var obec : listOfObec) {
            this.insertObec(obec);
        }
    }

    public void insertAllCastObce(List<CastObce> listOfCastObce) {
        for (var castObce : listOfCastObce) {
            this.insertCastObce(castObce);
        }
    }

    public void insertAllCastObceIfObecExist(List<CastObce> listOfCastObce) {
        for (var castObce : listOfCastObce) {
            this.insertCastObceIfObecExist(castObce);
        }
    }

    public Boolean obecExists(Integer obecKod) throws Exception {
        Boolean exist = true;
        try ( PreparedStatement statement = this.connection.prepareStatement(this.SELECT_OBECKOD_SQL)) {
            statement.setInt(1, obecKod);
            ResultSet result = statement.executeQuery();
            if (!result.isBeforeFirst()) {
                exist = false;
            }
        }
        return exist;
    }

    public void insertObec(Obec obec) {
        try ( PreparedStatement statement = this.connection.prepareStatement(this.INSERT_OBEC_SQL)) {
            statement.setInt(1, obec.Kod);
            statement.setString(2, obec.Nazev);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Unable to insert obec in DB.");
            System.out.println(e);
        }
    }

    public void insertCastObceIfObecExist(CastObce castObce) {
        try {
            if (!this.obecExists(castObce.KodObce)) {
                return;
            }
            this.insertCastObce(castObce);
        }
        catch (Exception e) {
           System.out.println("Unable to insert cast obce in DB conditionally.");
           System.out.println(e);
        }
    }

    public void insertCastObce(CastObce castObce) {
        try ( PreparedStatement statement = this.connection.prepareStatement(this.INSERT_CASTOBCE_SQL)) {
            statement.setInt(1, castObce.Kod);
            statement.setString(2, castObce.Nazev);
            statement.setInt(3, castObce.KodObce);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Unable to insert cast obce in DB.");
            System.out.println(e);
        }
    }

    public void openConnection() throws SQLException {
        this.connection = DriverManager.getConnection(this.url);
    }

    public void closeConnection() throws SQLException {
        this.connection.close();
        this.connection = null;
    }
}
