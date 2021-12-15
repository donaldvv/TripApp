package com.donald.service.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericResponseMessage {
    public String message;

    public GenericResponseMessage(String message) {
        this.message = message;
    }

}
