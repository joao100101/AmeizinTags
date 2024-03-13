package io.github.joao100101.ameizintags.database;


import java.sql.*;

import static org.bukkit.Bukkit.getLogger;

public class SQLite {
    private String path;


    public SQLite(String path){
        this.path = path;
    }

    public Connection getConnection() throws SQLException {
       return DriverManager.getConnection("jdbc:sqlite:" + path);
    }

    public void createTable(){
        String sql = "create table if not exists users_tags(uuid varchar(255) not null primary key, tagatual varchar(255), taganterior varchar(255));";
        try (Connection con = getConnection(); Statement statement = con.createStatement()){
            statement.execute(sql);
        }catch (SQLException e){
            getLogger().warning("Erro ao se conectar com o banco de dados.");
            e.printStackTrace();
        }
    }

    public boolean existInTable(String uuid){
        String sql = "select uuid from users_tags where uuid = ?";
        try (Connection con = getConnection(); PreparedStatement preparedStatement = con.prepareStatement(sql)){
            preparedStatement.setString(1, uuid);
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();
        }catch (SQLException e){
            getLogger().warning("Erro ao se conectar com o banco de dados.");
            e.printStackTrace();
        }

        return false;
    }

    public String getTagAtual(String uuid){
        String sql = "select tagatual from users_tags where uuid = ?";
        try (Connection con = getConnection(); PreparedStatement preparedStatement = con.prepareStatement(sql)){
            preparedStatement.setString(1, uuid);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return rs.getString("tagatual");
            }
        }catch (SQLException e){
            getLogger().warning("Erro ao se conectar com o banco de dados.");
            e.printStackTrace();
        }
        return "";
    }

    public String getTagAnterior(String uuid){
        String sql = "select taganterior from users_tags where uuid = ?";
        try (Connection con = getConnection(); PreparedStatement preparedStatement = con.prepareStatement(sql)){
            preparedStatement.setString(1, uuid);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return rs.getString("taganterior");
            }
        }catch (SQLException e){
            getLogger().warning("Erro ao se conectar com o banco de dados.");
            e.printStackTrace();
        }
        return "";
    }

    private void createTagUser(String uuid, String tag){
        String sql = "insert into users_tags (uuid, tagatual, taganterior) values (?,?, ?)";
        try (Connection con = getConnection(); PreparedStatement preparedStatement = con.prepareStatement(sql)){
            preparedStatement.setString(1, uuid);
            preparedStatement.setString(2, tag);
            preparedStatement.setString(3, "");
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            getLogger().warning("Erro ao se conectar com o banco de dados.");
            e.printStackTrace();
        }
    }

    public void saveTag(String uuid, String newTag, String oldTag){
        if(existInTable(uuid)){
            String sql = "update users_tags set tagatual = ?, taganterior = ? where uuid = ?";
            try (Connection con = getConnection(); PreparedStatement preparedStatement = con.prepareStatement(sql)){
                preparedStatement.setString(1, newTag);
                preparedStatement.setString(2, oldTag);
                preparedStatement.setString(3, uuid);
                preparedStatement.executeUpdate();
            }catch (SQLException e){
                getLogger().warning("Erro ao se conectar com o banco de dados.");
                e.printStackTrace();
            }
        }else{
            createTagUser(uuid, newTag);
        }
    }


}
