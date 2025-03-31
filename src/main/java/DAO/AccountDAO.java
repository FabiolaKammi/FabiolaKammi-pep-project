package DAO;

import Model.Account;
import java.sql.*;
import Util.ConnectionUtil;

public class AccountDAO {
    private Connection connection = ConnectionUtil.getConnection();

    public Account registerAccount(Account account) {
        try {
            // Check if username already exists
            String checkSql = "SELECT * FROM Account WHERE username = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkSql);
            checkStmt.setString(1, account.getUsername());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                return null; // Account already exists
            }

            // Insert new account
            String sql = "INSERT INTO Account (username, password) VALUES (?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, account.getUsername());
            stmt.setString(2, account.getPassword());
            stmt.executeUpdate();

            // Retrieve generated account_id
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                account.setAccount_id(generatedKeys.getInt(1));
            }

            return account;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Account login(String username, String password) {
        try {
            String sql = "SELECT * FROM Account WHERE username = ? AND password = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
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
    try {
        String sql = "SELECT * FROM Account WHERE username = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
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
