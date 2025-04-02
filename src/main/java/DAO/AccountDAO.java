package DAO;

import Model.Account;
import java.sql.*;
import Util.ConnectionUtil;

public class AccountDAO {
    
   
    
    public Account registerAccount(String userName, String passWord) {

        String sql = "INSERT INTO Account (username, password) VALUES (?, ?)";
        try(Connection connection = ConnectionUtil.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {
            // Insert new account
            stmt.setString(1, userName);
            stmt.setString(2, passWord);
            int rowsAffected = stmt.executeUpdate();

            // Retrieve generated account_id
            if(rowsAffected > 0){
                try(ResultSet rs = stmt.getGeneratedKeys()){

                    if(rs.next()){
                        int accountId = rs.getInt(1);
                        return new Account(accountId, userName, passWord);
                    }
                }
            
            
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Account login(String username, String password) {
        String sql = "SELECT * FROM Account WHERE username = ? AND password = ?";

        try (Connection connection = ConnectionUtil.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ){
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Account getAccountByUsername(String username) {
        String sql = "SELECT * FROM Account WHERE username = ?";
    try(Connection connection = ConnectionUtil.getConnection();
    PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    ){
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();

        // If the username exists in the database
        if (rs.next()) {
            // Return the Account object with all details
            return new Account(
                rs.getInt("account_id"),
                rs.getString("username"),
                rs.getString("password") // Saved password
            );
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null; // Return null if no match is found
    }

    
    
}
