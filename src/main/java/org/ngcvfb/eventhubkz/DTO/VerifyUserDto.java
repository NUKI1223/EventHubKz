package org.ngcvfb.eventhubkz.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyUserDto {
    private String email;
    private String verificationCode;

}
