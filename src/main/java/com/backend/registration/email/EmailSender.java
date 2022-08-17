package com.backend.registration.email;

import org.springframework.stereotype.Repository;

public interface EmailSender {

    void send(String to,String email);
}
