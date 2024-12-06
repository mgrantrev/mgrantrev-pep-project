package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            //Write SQL logic here
            String sql = "Select * from message;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public Message insertMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        
        try {
            if(!message.getMessage_text().isEmpty() && message.getMessage_text().length()<=255){

            //Write SQL logic here
            String sql = "Insert into message (posted_by, message_text, time_posted_epoch) values (?, ?, ?)" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //write preparedStatement's setString and setInt methods here.
            //preparedStatement.setInt(1, message.getMessage_id());
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();

            if(pkeyResultSet.next()){
                int generated_message_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;

    }

    public Message deleteMessage(int messageId) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> currentMessages = getAllMessages();
        Message deletedMessage = new Message();
    
        try {
    
            //Write SQL logic here
            String sql = "Delete from message where message_id = ?" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //write preparedStatement's setString and setInt methods here.
            preparedStatement.setInt(1, messageId);

            int affectedRows = preparedStatement.executeUpdate();

            if(affectedRows > 0) {
            for(Message m : currentMessages){
                if(m.getMessage_id() == messageId) {
                    deletedMessage =m;
                }
            }
            return deletedMessage;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message updateMessage(int messageId, String messageText) {
        Connection connection = ConnectionUtil.getConnection();
    
        try {
    
            //Write SQL logic here
            if(messageText != null && messageText.length() <255 && messageText.length() > 0) {
                String sql = "Update message set message_text = ? where message_id = ?" ;
                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                //write preparedStatement's setString and setInt methods here.
                preparedStatement.setString(1, messageText);
                preparedStatement.setInt(2, messageId);

                preparedStatement.executeUpdate();

                List<Message> updatedMessages = getAllMessages();
                for(Message m : updatedMessages) {
                    if(m.getMessage_id() == messageId) {
                        return m;
                    }
                }
             }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    
    }
    
}
