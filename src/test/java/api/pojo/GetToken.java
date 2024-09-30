package api.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetToken {
    public  String token;
    public GetToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public GetToken() {

  }
}
