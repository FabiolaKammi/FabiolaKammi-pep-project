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

    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    public Message deleteMessageById(int messageId){
        return messageDAO.deleteMessageById(messageId);
    }

    public Message updateMessage(int messageId, String messageText) {
        if (messageText.isBlank() || messageText.length() > 255) {
            return null;
        }
        return messageDAO.updateMessage(messageId, messageText);
    }

    public List<Message> getMessagesByUserId(int accountId) {
        return messageDAO.getMessagesByUserId(accountId);
    }
}
