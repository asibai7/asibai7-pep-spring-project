package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private MessageService messageService;

    //User Registration
    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        if (account.getUsername() == null || account.getUsername().isBlank()
                || account.getPassword() == null || account.getPassword().length() < 4) {
            return ResponseEntity.badRequest().build();
        }
        if (accountService.getAccountByUsername(account.getUsername()) != null) {
            return ResponseEntity.status(409).build();
        }
        Account saved = accountService.createAccount(account);
        return ResponseEntity.ok(saved);
    }

    //Login
    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        Account found = accountService.verifyLogin(account.getUsername(), account.getPassword());
        if (found == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(found);
    }

    //Create New Message
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        if (message.getMessageText() == null || message.getMessageText().isBlank()
                || message.getMessageText().length() >= 255
                || !accountService.accountExists(message.getPostedBy())) {
            return ResponseEntity.badRequest().build();
        }
        Message saved = messageService.createMessage(message);
        return ResponseEntity.ok(saved);
    }

    //Get All Messages
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    //Get One Message Given Message Id
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable int messageId) {
        Message message = messageService.getMessageById(messageId);
        return ResponseEntity.ok(message);
    }

    //Delete Message Given Message Id
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable int messageId) {
        int deletedRows = messageService.deleteMessage(messageId);
        if (deletedRows == 0) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.ok(deletedRows);
    }

    //Update Message Given Message Id
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessageText(@PathVariable int messageId, @RequestBody Message incoming) {
        String newText = incoming.getMessageText();
        if (newText == null || newText.isBlank() || newText.length() >= 255) {
            return ResponseEntity.badRequest().build();
        }
        int updatedRows = messageService.updateMessageText(messageId, newText);
        if (updatedRows == 0) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(updatedRows);
    }

    //Get All Messages Given User Id
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccountId(@PathVariable int accountId) {
        List<Message> messages = messageService.getMessagesByAccountId(accountId);
        return ResponseEntity.ok(messages);
    }
}
