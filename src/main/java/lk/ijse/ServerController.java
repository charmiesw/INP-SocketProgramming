package lk.ijse;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerController {
    @FXML
    public AnchorPane root;
    @FXML
    public TextField txtMsg;
    @FXML
    public TextArea txtView;

    ServerSocket serverSocket;
    Socket localSocket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    String message = " ";

    public void initialize(){
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(3002);
                txtView.appendText("Server is started!");

                localSocket = serverSocket.accept();
                txtView.appendText("\nClient connected");

                dataInputStream = new DataInputStream(localSocket.getInputStream());
                dataOutputStream = new DataOutputStream(localSocket.getOutputStream());

                while (!message.equals("End")){
                    message = dataInputStream.readUTF();
                    txtView.appendText("\nClient : " + message);
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
