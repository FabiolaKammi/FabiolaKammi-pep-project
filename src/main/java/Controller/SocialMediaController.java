package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

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
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
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

            Message createdMessage = messageService.createMessage(message);
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
            ctx.status(200).result("Message not found.");
        }
    }

    private void deleteMessageHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.getMessageById(messageId);

        if (deletedMessage != null && messageService.deleteMessage(messageId)) {
            ctx.json(deletedMessage);
        } else {
            ctx.status(200).result(""); // Idempotent DELETE
        }
    }

    private void updateMessageHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        String newMessageText = ctx.body();

        boolean isUpdated = messageService.updateMessage(messageId, newMessageText);
        if (isUpdated) {
            Message updatedMessage = messageService.getMessageById(messageId);
            ctx.json(updatedMessage);
        } else {
            ctx.status(400).result("Message update failed.");
        }
    }

    private void getMessagesByUserHandler(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> userMessages = messageService.getMessagesByUserId(accountId);
        ctx.json(userMessages);
    }
}