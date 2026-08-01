package com.msp.client;

import com.msp.dto.UserDTO;
import com.msp.exceptions.UserException;
import org.springframework.stereotype.Component;

@Component
public class UserClientFallback implements UserClient {

    @Override
    public UserDTO getUserById(Long userId) throws UserException {
        return null;
    }
}
