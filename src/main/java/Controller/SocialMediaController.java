package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.Map;

import java.util.List;

public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();

        // Endpoints
        app.post("/register", this::registerAccountHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserHandler);

        return app;
    }

    private void registerAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);

        Account registeredAccount = accountService.registerAccount(account);
        if (registeredAccount != null) {
            ctx.json(mapper.writeValueAsString(registeredAccount));
        } else {
            ctx.status(400).result("Account registration failed.");
        }
    }

    private void loginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account loginAccount = mapper.readValue(ctx.body(), Account.class);

        Account authenticatedAccount = accountService.login(loginAccount.getUsername(), loginAccount.getPassword());
        if (authenticatedAccount != null) {
            ctx.json(mapper.writeValueAsString(authenticatedAccount));
        } else {
            ctx.status(401).result("Login failed. Unauthorized.");
        }
    }

    private void createMessageHandler(Context ctx) throws JsonProcessingException {
       
            ObjectMapper mapper = new ObjectMapper();
            Message message = mapper.readValue(ctx.body(),Message.class);

            System.out.println(message);
            Message createdMessage = messageService.createMessage(message);
                System.out.println(createdMessage);
            if (createdMessage == null) {
                ctx.status(400);
                
            } else {
                ctx.status(200).json(createdMessage);
            }
        
    }

    private void getAllMessagesHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    private void getMessageByIdHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);

        if (message != null) {
            ctx.json(message);
        } else {
            ctx.status(200);
        }
    }

    private void deleteMessageByIdHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessageById(messageId);

        if(deletedMessage != null){
            ctx.json(deletedMessage).status(200);
        } else {
            ctx.status(200).result(""); // Idempotent DELETE
        }
    }

    private void updateMessageHandler(Context ctx) throws JsonProcessingException {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();
        @SuppressWarnings("unchecked")
        Map<String, String> messageMap = mapper.readValue(ctx.body(), Map.class);
        String messageText = messageMap.get("message_text");

         Message updatedMessage = messageService.updateMessage(messageId, messageText);
        if (updatedMessage != null) {
            
            ctx.json(updatedMessage).status(200);
        } else {
            ctx.status(400);
        }
    }

    private void getMessagesByUserHandler(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> userMessages = messageService.getMessagesByUserId(accountId);
        ctx.json(userMessages).status(200);
    }
}