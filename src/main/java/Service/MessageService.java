package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {
    private MessageDAO messageDAO = new MessageDAO();

    public Message createMessage(Message message) {
        int posted_by = message.getPosted_by();
        String message_text = message.getMessage_text();
        long time_posted_epoch = message.getTime_posted_epoch();

        if (message_text.isBlank() || message_text.length() > 255) {
            return null;
        }
        Message createdMessage = messageDAO.createMessage(posted_by, message_text, time_posted_epoch);
        return createdMessage;
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int message_id) {
        return messageDAO.getMessageById(message_id);
    }

    public boolean deleteMessage(int message_id) {
        return messageDAO.deleteMessage(message_id);
    }

    public boolean updateMessage(int message_id, String message_text) {
        if (message_text == null || message_text.isBlank() || message_text.length() > 255) {
            return false;
        }
        return messageDAO.updateMessage(message_id, message_text);
    }

    public List<Message> getMessagesByUserId(int account_id) {
        return messageDAO.getMessagesByUserId(account_id);
    }
}
