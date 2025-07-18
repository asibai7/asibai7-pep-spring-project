package com.example.service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
@Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(Integer messageId) {
        Optional<Message> optMessage = messageRepository.findById(messageId);
        return optMessage.orElse(null);
    }

    public int deleteMessage(Integer messageId) {
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return 1;
        }
        return 0;
    }

    public int updateMessageText(Integer messageId, String newText) {
        Optional<Message> optMessage = messageRepository.findById(messageId);
        if (optMessage.isPresent()) {
            Message message = optMessage.get();
            message.setMessageText(newText);
            messageRepository.save(message);
            return 1;
        }
        return 0;
    }

    public List<Message> getMessagesByAccountId(Integer accountId) {
        return messageRepository.findByPostedBy(accountId);
    }

    public boolean messageExists(Integer messageId) {
        return messageRepository.existsById(messageId);
    }

    public boolean accountExists(Integer accountId) {
        return accountRepository.existsById(accountId);
    }
}