package com.example.first_draft;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ChatPageController {

    @FXML private Label usernameLabel;
    @FXML private VBox chatBox;
    @FXML private ScrollPane chatScrollPane;
    @FXML private TextField messageField;
    @FXML private Button sendButton;
    @FXML private ListView<String> userList;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String username;
    private String currentChatUser = "";

    @FXML
    private void initialize() {
        sendButton.setDisable(true);
        messageField.setDisable(true);

        connectToServer();

        userList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, selectedUser) -> {
            if (selectedUser != null) {
                currentChatUser = selectedUser;
                loadChatWith(selectedUser);
            }
        });
    }

    public void setUsername(String currentUser) {
        this.username = currentUser;
        usernameLabel.setText("Chat - " + username);
    }

    private void loadChatWith(String otherUser) {
        chatBox.getChildren().clear();
        if (otherUser == null || otherUser.isEmpty()) return;

        currentChatUser = otherUser;

        // Load history from ChatLogManager
        List<String> history = ChatLogManager.loadMessages(username, otherUser);
        for (String msg : history) {
            boolean isMe = msg.startsWith("(Me →");
            addMessage(msg, isMe ? username : otherUser);
        }
    }

    // Update connectToServer() to remove duplicate saving for incoming messages
    private void connectToServer() {
        new Thread(() -> {
            try {
                socket = new Socket("localhost", ChatServer.PORT);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                out.println(username);

                Platform.runLater(() -> {
                    sendButton.setDisable(false);
                    messageField.setDisable(false);
                });

                String line;
                while ((line = in.readLine()) != null) {

                    if (line.contains("|")) {
                        String[] parts = line.split("\\|", 2);
                        String sender = parts[0];
                        String msgContent = parts[1];

                        // ✅ Only save incoming messages from server perspective
                       // ChatLogManager.saveMessage(sender, username, msgContent, false);

                        if (sender.equals(currentChatUser)) {
                            String formatted = "(" + sender + " → Me): " + msgContent;
                            Platform.runLater(() -> addMessage(formatted, sender));
                        }
                    } else if (line.startsWith("USER_LIST:")) {
                        updateUserList(line.substring(10));
                    }
                }

            } catch (IOException e) {
                Platform.runLater(() -> addMessage("Unable to connect", "System"));
                e.printStackTrace();
            }
        }).start();

        sendButton.setOnAction(e -> sendMessage());
        messageField.setOnAction(e -> sendMessage());
    }


    private void updateUserList(String csvNames) {
        String[] names = csvNames.split(",");
        Platform.runLater(() -> {
            userList.getItems().clear();
            for (String user : names) {
                if (!user.equals(username) && !user.isEmpty()) userList.getItems().add(user);
            }
        });
    }

    @FXML
    private void sendMessage() {
        String message = messageField.getText().trim();
        if (currentChatUser.isEmpty() || message.isEmpty()) return;

        out.println("@" + currentChatUser + " " + message);

        String formatted = "(Me → " + currentChatUser + "): " + message;
        //ChatLogManager.saveMessage(username, currentChatUser, message, true);
        addMessage(formatted, username);

        messageField.clear();
    }

    private void addMessage(String message, String sender) {
        HBox msgContainer = new HBox();
        Label msgLabel = new Label(message);
        msgLabel.setWrapText(true);
        msgLabel.setMaxWidth(300);
        msgLabel.getStyleClass().add("message-bubble");

        if (sender.equals(username)) {
            msgContainer.setStyle("-fx-alignment: CENTER-RIGHT;");
            msgLabel.getStyleClass().add("message-right");
        } else {
            msgContainer.setStyle("-fx-alignment: CENTER-LEFT;");
            msgLabel.getStyleClass().add("message-left");
        }

        msgContainer.getChildren().add(msgLabel);
        chatBox.getChildren().add(msgContainer);

        // 🚀 Auto-scroll
        Platform.runLater(() -> {
            chatScrollPane.setVvalue(1.0);
            chatScrollPane.layout();
            Platform.runLater(() -> chatScrollPane.setVvalue(chatScrollPane.getVmax()));
        });
    }
}
