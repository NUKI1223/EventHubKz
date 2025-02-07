package org.ngcvfb.eventhubkz.DTO;

import lombok.Getter;
import lombok.Setter;


import java.util.Map;

@Getter
@Setter
public class UserDTO {


    private String username;


    private String email;


    private String password;

    private String description;

    private String role; // Обычно тип Enum, здесь для простоты String

    private String avatarUrl;

    private Map<String, String> contacts;


}
