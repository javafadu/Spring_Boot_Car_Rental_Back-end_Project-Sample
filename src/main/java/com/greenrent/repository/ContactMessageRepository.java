package com.greenrent.repository;

import com.greenrent.domain.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // bunu koymasak da olabilirdi JPA dan dolayi
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {


    public List<ContactMessage> findByName(String name);


}
