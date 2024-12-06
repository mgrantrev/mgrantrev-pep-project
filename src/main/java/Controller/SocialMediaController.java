package Controller;

import Service.AccountService;
import Service.MessageService;
import Model.Message;
import Model.Account;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

  
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postRegisterHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postMessagesHandler);
        app.get("/messages", this::getMessagesHandler);        
        app.get("/messages/{message_id}", this::getMessagesByMessageIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessagesByMessageIdHandler);
        app.patch("/messages/{message_id}", this::patchMessagesByMessageIdHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountIdHandler);
        return app;
    }


    private void postRegisterHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if(addedAccount!=null){
            context.json(mapper.writeValueAsString(addedAccount));
            context.status(200);
        }else{
            context.status(400);
        }
    }

    private void postLoginHandler(Context context) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account loginAccount = accountService.getAccountByLogin(account);
        if(loginAccount!=null){
            context.json(mapper.writeValueAsString(loginAccount));
            context.status(200);
        }else{
            context.status(401);
        }
    }
    private void postMessagesHandler(Context context) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        List<Account> accounts = accountService.getAllAccounts();
        boolean userFound = false;
        for(Account A : accounts) {
            if(A.getAccount_id() == message.posted_by) {
                userFound= true;
            }
        }
        Message newMessage = messageService.insertMessage(message);
        if(newMessage!=null && userFound){
            context.json(mapper.writeValueAsString(newMessage));
            context.status(200);
        }else{
            context.status(400);
        }
    }
    private void getMessagesHandler(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
    }
    private void getMessagesByMessageIdHandler(Context context) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        System.out.println("Message id = " + messageId);
        List<Message> messages = messageService.getAllMessages();
        for(Message m : messages) {
            if(m.getMessage_id() == messageId) {
                context.json(mapper.writeValueAsString(m));
             }
        }
    }
    private void deleteMessagesByMessageIdHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        System.out.println("Message id = " + messageId);
        Message deletedMessage = messageService.deleteMessage(messageId);
        if(deletedMessage!=null){
            context.json(mapper.writeValueAsString(deletedMessage));
            context.status(200);
        }      
    }

    private void patchMessagesByMessageIdHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(context.body());
        String messageText = jsonNode.get("message_text").asText();

        //Message message = mapper.readValue(context.body(), Message.class);
        System.out.println(messageText);
        System.out.println(messageId);

        Message updatedMessage = messageService.updateMessage(messageId, messageText);
        if(updatedMessage!=null){
            context.json(mapper.writeValueAsString(updatedMessage));
            context.status(200);
        }else{
            context.status(400);
        }      
        

    }

    private void getMessagesByAccountIdHandler(Context context) throws JsonProcessingException {
        int accountId = Integer.parseInt(context.pathParam("account_id"));
        System.out.println("Account id = " + accountId);
        List<Message> messages = messageService.getAllMessages();
        List<Message> userMessages = new ArrayList<>();
        
        for(Message m : messages) {
            if(m.getPosted_by() == accountId) {
                userMessages.add(m);
             }
        }
        context.json(userMessages);
    }


}