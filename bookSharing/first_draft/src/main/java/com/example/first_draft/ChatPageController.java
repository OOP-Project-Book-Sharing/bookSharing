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

        userList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, selectedUser) -> {
            if (selectedUser != null && !selectedUser.trim().isEmpty()) {
                currentChatUser = selectedUser;
                loadChatWith(selectedUser);
            }
        });

        sendButton.setOnAction(e -> sendMessage());
        messageField.setOnAction(e -> sendMessage());
    }

    public void setUsername(String currentUser) {
        this.username = currentUser;
        usernameLabel.setText("Chat - " + username);
        connectToServer();
    }

    private void loadChatWith(String otherUser) {
        chatBox.getChildren().clear();
        if (otherUser == null || otherUser.isEmpty()) return;
        currentChatUser = otherUser;

        List<String> history = ChatLogManager.loadMessages(username, otherUser);
        for (String msg : history) {
            boolean isMe = msg.startsWith("(Me →");
            addMessage(msg, isMe ? username : otherUser);
        }
    }

    private void connectToServer() {
        new Thread(() -> {
            try {
                socket = new Socket("localhost", ChatServer.PORT);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                if (username == null || username.trim().isEmpty()) {
                    socket.close();
                    return;
                }
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

                        if (sender.equals(currentChatUser)) {
                            String formatted = "(" + sender + " → Me): " + msgContent;
                            Platform.runLater(() -> addMessage(formatted, sender));
                        } else {


                        }
                    } else if (line.startsWith("USER_LIST:")) {
                        String csv = line.substring("USER_LIST:".length());
                        updateUserList(csv);
                    } else {
                        String systemMsg = line;
                        Platform.runLater(() -> addMessage(systemMsg, "System"));
                    }
                }
            } catch (IOException e) {
                Platform.runLater(() -> addMessage("Unable to connect to server", "System"));
                e.printStackTrace();
            }
        }).start();
    }

    private void updateUserList(String csvNames) {
        if (csvNames == null) csvNames = "";
        String[] names = csvNames.split(",");
        Platform.runLater(() -> {
            userList.getItems().clear();
            for (String raw : names) {
                if (raw == null) continue;
                String user = raw.trim();
                if (user.isEmpty()) continue;
                if ("null".equalsIgnoreCase(user)) continue;
                if (username != null && user.equals(username)) continue;
                userList.getItems().add(user);
            }
        });
    }

    @FXML
    private void sendMessage() {
        String message = messageField.getText().trim();
        if (currentChatUser == null || currentChatUser.isEmpty() || message.isEmpty()) return;

        out.println("@" + currentChatUser + " " + message);

        String formatted = "(Me → " + currentChatUser + "): " + message;
        addMessage(formatted, username);
        messageField.clear();
    }

    private void addMessage(String message, String sender) {
        HBox msgContainer = new HBox();
        Label msgLabel = new Label(message);
        msgLabel.setWrapText(true);
        msgLabel.setMaxWidth(300);
        msgLabel.getStyleClass().add("message-bubble");

        if (sender != null && sender.equals(username)) {
            msgContainer.setStyle("-fx-alignment: CENTER-RIGHT;");
            msgLabel.getStyleClass().add("message-right");
        } else if ("System".equals(sender)) {
            msgContainer.setStyle("-fx-alignment: CENTER;");
            msgLabel.getStyleClass().add("message-system");
        } else {
            msgContainer.setStyle("-fx-alignment: CENTER-LEFT;");
            msgLabel.getStyleClass().add("message-left");
        }

        msgContainer.getChildren().add(msgLabel);
        Platform.runLater(() -> {
            chatBox.getChildren().add(msgContainer);

            chatScrollPane.layout();
            chatScrollPane.setVvalue(1.0);
            Platform.runLater(() -> chatScrollPane.setVvalue(chatScrollPane.getVmax()));
        });
    }
}
