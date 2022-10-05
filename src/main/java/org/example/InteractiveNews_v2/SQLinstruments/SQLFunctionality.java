package org.example.InteractiveNews_v2.SQLinstruments;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class SQLFunctionality {
    public void update (Connection connection, Timestamp datetime, String url, String newsdata, String tag){

        String updateTable = "INSERT INTO NewsInterfaxPortal (datetime, url, newsdata, tag) VALUES (?, ?, ?, ?);";

        try {
            PreparedStatement stmt = connection.prepareStatement(updateTable);
            stmt.setTimestamp(1, datetime);
            stmt.setString(2, url);
            stmt.setString(3, newsdata);
            stmt.setString(4, tag);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error update method! " + e.getMessage());
        }
    }

    public void deleteByURL (Connection connection, String url){
        String cmdSSQLDeleteByURL = "DELETE FROM NewsInterfaxPortal WHERE url=(?);";
        try {
            PreparedStatement stmt;
            for (int i = 0; i <= 9; i++) {
                stmt = connection.prepareStatement(cmdSSQLDeleteByURL);
                stmt.setString(1, url + i);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error delete by URL method! " + e.getMessage());
        }
    }

    public NewsRecord getLastInfo (Connection connection){
        String cmdSQLLastRecord = "SELECT * FROM NewsInterfaxPortal WHERE id = (SELECT MAX(id) FROM NewsInterfaxPortal);";
        NewsRecord lastRecord = new NewsRecord();
        try(Statement stmt = connection.createStatement()){

            ResultSet rs = stmt.executeQuery(cmdSQLLastRecord);

            if (rs.next()){
                lastRecord.set(rs.getInt("id"),
                        rs.getTimestamp("datetime"),
                        rs.getString("url"),
                        rs.getString("newsdata"),
                        rs.getString("tag"));
                return lastRecord;
            } else
                return null;

        }catch (SQLException e){
            return null;
        }
    }

    public void deleteDuplicateBy (Connection connection, String tableColumnName){
        String cmdFindAndDeleteDuplicate =  "DELETE FROM newsinterfaxportal table_1 USING newsinterfaxportal table_2 WHERE table_1.id < table_2.id AND table_1." +
                tableColumnName + "  = table_2." + tableColumnName + ";";
        try {
            PreparedStatement stmt = connection.prepareStatement(cmdFindAndDeleteDuplicate);
            stmt.executeUpdate();

        }catch (SQLException ex){
            ex.getStackTrace();
            System.out.println(ex.getMessage());
        }
    }

    public ArrayList printTags (Connection connection){
        String cmdPrintTags = "SELECT DISTINCT tag FROM NewsInterfaxPortal";
        ArrayList<String> tags = new ArrayList<>();
        try(Statement stmt = connection.createStatement()){
            ResultSet resultSet = stmt.executeQuery(cmdPrintTags);
            while(resultSet.next()){
                tags.add(resultSet.getString("tag"));
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tags;
    }

    public ArrayList<NewsRecord> selectByTag (Connection connection, String tag){
        String cmdSelectByTag = "SELECT * FROM NewsInterfaxPortal WHERE tag = '" + tag + "' ORDER BY id";
        return getSQLStatement(connection, cmdSelectByTag);
    }

    public ArrayList<NewsRecord> selectByDate (Connection connection, String date){
        LocalDateTime convertedMaxDate = dateToTimestamp(date).toLocalDateTime().plusHours(23).plusMinutes(59).plusSeconds(59);
        String cmdSelectByDate = "SELECT * FROM NewsInterfaxPortal WHERE datetime BETWEEN '" + dateToTimestamp(date) + "' AND ' ORDER BY id"
                + Timestamp.valueOf(convertedMaxDate) + "'";
        return getSQLStatement(connection, cmdSelectByDate);
    }

    public ArrayList<NewsRecord> selectByDateTime (Connection connection, String dateTime){
        LocalDateTime convertedMinDate = dateTimeToTimestamp(dateTime).toLocalDateTime().minusHours(1);
        LocalDateTime convertedMaxDate = dateTimeToTimestamp(dateTime).toLocalDateTime().plusHours(1);
        String cmdSelectByDate = "SELECT * FROM NewsInterfaxPortal WHERE datetime BETWEEN '" + Timestamp.valueOf(convertedMinDate) + "' AND ' ORDER BY id"
                + Timestamp.valueOf(convertedMaxDate) + "'";
        return getSQLStatement(connection, cmdSelectByDate);
    }

    public ArrayList<NewsRecord> selectByTagAndDate (Connection connection, String tag, String date){
        LocalDateTime convertedMaxDate = dateToTimestamp(date).toLocalDateTime().plusHours(23).plusMinutes(59).plusSeconds(59);
        String cmdSelectByDate = "SELECT * FROM NewsInterfaxPortal WHERE tag = '" + tag + "' AND (datetime BETWEEN '" + dateToTimestamp(date) + "' AND ' ORDER BY id"
                + Timestamp.valueOf(convertedMaxDate) + "')";
        return getSQLStatement(connection, cmdSelectByDate);
    }

    public boolean checkTimestampFormat (String dateTime){
        try{
            Timestamp checkingTimestamp = Timestamp.valueOf(dateTime);
            return true;
        }catch (Exception ex){
            return false;
        }
    }

    public boolean checkLocaleDateTimeFormat (String dateTime){
        try{
            LocalDateTime checkingTimestamp = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            return true;
        }catch (Exception ex){
            return false;
        }
    }

    public Timestamp dateToTimestamp (String dateTime){
        try {
            LocalDate takeDate = LocalDate.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDateTime takeDateTime = takeDate.atTime(0,0,0,0);
            return Timestamp.valueOf(takeDateTime);
        }catch (Exception ex){
            return Timestamp.valueOf(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0));
        }
    }

    public Timestamp dateTimeToTimestamp (String dateTime){
        try {
            LocalDateTime takeDateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            return Timestamp.valueOf(takeDateTime);
        }catch (Exception ex){
            return Timestamp.valueOf(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0));
        }
    }

    public ArrayList<NewsRecord> getSQLStatement(Connection connection, String sqlStatement){
        ArrayList<NewsRecord> news = new ArrayList<>();
        try(Statement stmt = connection.createStatement()){
            ResultSet resultSet = stmt.executeQuery(sqlStatement);
            while(resultSet.next()){
                NewsRecord nr = new NewsRecord();
                nr.set( resultSet.getInt("id"),
                        resultSet.getTimestamp("datetime"),
                        resultSet.getString("url"),
                        resultSet.getString("newsdata"),
                        resultSet.getString("tag"));
                news.add(nr);
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return news;
    }
}
