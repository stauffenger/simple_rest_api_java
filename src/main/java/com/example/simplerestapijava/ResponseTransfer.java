package com.example.simplerestapijava;

import lombok.Getter;
import lombok.Setter;

public class ResponseTransfer {
	private @Getter @Setter String message; 

    public ResponseTransfer(String message) {
        this.setMessage(message);
    }
}
