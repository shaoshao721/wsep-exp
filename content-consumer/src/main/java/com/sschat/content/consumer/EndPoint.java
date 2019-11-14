package com.sschat.content.consumer;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EndPoint {

    private String host;
    private Integer port;

    public EndPoint(String endPointString) {
        String[] endpoint = endPointString.split(":");
        this.host = endpoint[0];
        this.port = Integer.parseInt(endpoint[1]);
    }

}
