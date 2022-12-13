package com.example.eksamen3backend.service;

import com.example.eksamen3backend.model.ContactPerson;
import com.example.eksamen3backend.model.Conversation;
import com.example.eksamen3backend.model.Corporation;

import java.util.List;
import java.util.Set;

public interface IConversation extends CrudService<Conversation, Long> {
    Set<Conversation> getConversationsByCorporationOrderByDateDesc(Corporation corporation);
    Set<Conversation>getConversationsByContactPersonOrderByDateDesc(ContactPerson contactPerson);


}

