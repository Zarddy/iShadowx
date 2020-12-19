package club.zarddy.ishadowx.model;

import lombok.Data;

@Data
public class IShadowAccount {

    private String ip;

    private String port;

    private String password;

    private String method;

    public IShadowAccount() {

    }

    public IShadowAccount(String ip, String port, String password, String method) {
        this.ip = ip;
        this.port = port;
        this.password = password;
        this.method = method;
    }
}
