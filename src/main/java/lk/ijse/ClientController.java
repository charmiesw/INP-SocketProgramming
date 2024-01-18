package lk.ijse;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientController {
    @FXML
    public AnchorPane root;
    @FXML
    public TextField txtMsg;
    @FXML
    public TextArea txtView;

    Socket remoteSocket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    String message = " ";

    public void initialize() {
        new Thread(() -> {
            try {
                remoteSocket = new Socket("localhost", 3002);

                dataInputStream = new DataInputStream(remoteSocket.getInputStream());
                dataOutputStream = new DataOutputStream(remoteSocket.getOutputStream());

                while (!message.equals("end")) {
                    message = dataInputStream.readUTF();
                    txtView.appendText("\nServer : " + message);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @FXML
    public void btnSendOnAction(ActionEvent actionEvent) {
        try {
            dataOutputStream.writeUTF(txtMsg.getText().trim());
            dataOutputStream.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
