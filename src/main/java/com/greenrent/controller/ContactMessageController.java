package com.greenrent.controller;

import com.greenrent.domain.ContactMessage;
import com.greenrent.service.ContactMessageService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contactmessage")
@AllArgsConstructor
public class ContactMessageController {

    @Autowired
    private ContactMessageService contactMessageService;

    @PostMapping("/visitor")
    public ResponseEntity<Map<String,String>> createMessage(@Valid @RequestBody ContactMessage contactMessage) {
        contactMessageService.createContactMessage(contactMessage);

        Map<String,String> map = new HashMap<>();
        map.put("message","Contact Message Successfully Created");
        map.put("status","true");

        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ContactMessage>> getAllContactMessage() {
        List<ContactMessage> list = contactMessageService.getAll();
        return ResponseEntity.ok(list);
    }


}
