package echoclient;

import java.util.ArrayList;

public interface EchoListener {

    void messageArrived(String data);

    void updateUserList(ArrayList<String> userNames);
}
