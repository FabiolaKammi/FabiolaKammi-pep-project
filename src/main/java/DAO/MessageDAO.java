package DAO;

import Model.Message;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Util.ConnectionUtil;

public class MessageDAO {
    

    public Message createMessage(int posted_by, String message_text, long time_posted_epoch) {
       
        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            
            stmt.setInt(1, posted_by);
            stmt.setString(2, message_text);
            stmt.setLong(3, time_posted_epoch);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0){
                try(ResultSet rs = stmt.getGeneratedKeys()){
            
                    if (rs.next()) { 
                        int messageId = rs.getInt(1);
                        return new Message(messageId, posted_by, message_text, time_posted_epoch);
                    }
                }
                
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message";
        try {
            Connection connection = ConnectionUtil.getConnection();
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

    public Message getMessageById(int messageId) {
        String sql = "SELECT * FROM message WHERE message_id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, messageId);
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

    public Message deleteMessageById(int messageId) {
        String sql = "SELECT * FROM message WHERE message_id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){
       
            stmt.setInt(1, messageId);
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

    public Message updateMessage(int messageId, String messageText) {
        Message messageToUpdate = getMessageById(messageId);
        String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
        
        try (Connection connection = ConnectionUtil.getConnection();
         
        PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, messageText);
            stmt.setInt(2, messageId);
            
            int rowsAffected = stmt.executeUpdate();

                if(rowsAffected > 0){
                    messageToUpdate.setMessage_text(messageText);
                    return messageToUpdate;
                }
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messageToUpdate;
    }

    public List<Message> getMessagesByUserId(int accountId) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message WHERE posted_by = ?";

        try (Connection connection = ConnectionUtil.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){
         
            stmt.setInt(1, accountId);
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
