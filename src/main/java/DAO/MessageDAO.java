package DAO;

import Model.Message;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Util.ConnectionUtil;

public class MessageDAO {
    

    public Message createMessage(int posted_by, String message_text, long time_posted_epoch) {
       
            String sql = "INSERT INTO Messages (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            
            
            stmt.setInt(1, posted_by);
            stmt.setString(2, message_text);
            stmt.setLong(3, time_posted_epoch);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0){
                ResultSet rs = stmt.getGeneratedKeys();
            
                if (rs.next()) { 
                    int messageId = rs.getInt(1);
                    return new Message(messageId, posted_by, message_text, time_posted_epoch);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Messages";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                messages.add(new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public Message getMessageById(int message_id) {
        try {
            String sql = "SELECT * FROM Messages WHERE message_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, message_id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteMessage(int message_id) {
        try {
            String sql = "DELETE FROM Messages WHERE message_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, message_id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateMessage(int message_id, String message_text) {
        try {
            String sql = "UPDATE Messages SET message_text = ? WHERE message_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, message_text);
            stmt.setInt(2, message_id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Message> getMessagesByUserId(int account_id) {
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Messages WHERE posted_by = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, account_id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                messages.add(new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
}
