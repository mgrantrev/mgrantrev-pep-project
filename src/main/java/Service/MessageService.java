package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message deleteMessage(int messageId) {
        return messageDAO.deleteMessage(messageId);
    }


    public Message insertMessage(Message message) {
        return messageDAO.insertMessage(message);
    }
    public Message updateMessage(int messageId, String messageText) {
        return messageDAO.updateMessage(messageId, messageText);
    }
}
