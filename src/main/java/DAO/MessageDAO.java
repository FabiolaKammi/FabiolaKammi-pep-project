package DAO;

import java.sql.Connection;
import Model.Message;
import Util.ConnectionUtil;
import Util.ConnectionUtil;

public class MessageDAO {
    // Create a new Message
     
    public Message createMessage(Message message){
        // Connectiong to the database
        Connection connection = ConnectionUtil.getConnection();
        try {
            
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }

    return null;
    }
    // Update Message
    public Message updateMessage(Message message){
        try {
            
        } catch (Exception e) {
            // TODO: handle exception
            e.getMessage();
        }

        return null;
    }
    // Deleting a message
    public void deleteMessage(Message message){
        try {
            
        } catch (Exception e) {
            // TODO: handle exception
            e.getMessage();
        }
        
    }
}
