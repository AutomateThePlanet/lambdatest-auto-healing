package localpage;

import lombok.Data;

@Data
public class ClientInfo {
    public String firstName;
    public String lastName;
    public String username;
    public String email;
    public String address1;
    public String address2;
    public int country;
    public int state;
    public String zip;
    public String cardName;
    public String cardNumber;
    public String cardExpiration;
    public String cardCVV;
}
