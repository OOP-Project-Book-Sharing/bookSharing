package com.example.first_draft;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;

public class ChatPageController {

    @FXML private Label usernameLabel;
    @FXML private VBox chatBox;
    @FXML private ScrollPane chatScrollPane;
    @FXML private TextField messageField;
    @FXML private TextField recipientField;
    @FXML private Button sendButton;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String username;

    @FXML
    private void initialize() {
        // Assign a temporary username for testing; replace with actual login username later
        username = "User" + (int)(Math.random() * 1000);
        usernameLabel.setText("Chat - " + username);

        // Disable message input and send button until connected
        sendButton.setDisable(true);
        messageField.setDisable(true);

        // Connect to the chat server in a new thread
        connectToServer();
    }

    private void connectToServer() {
        new Thread(() -> {
            try {
                socket = new Socket("localhost", 5000);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Wait for server to ask for username
                String serverPrompt = in.readLine();
                if ("Enter username:".equals(serverPrompt)) {
                    out.println(username);
                }

                // Enable sending messages on the JavaFX Application Thread
                Platform.runLater(() -> {
                    sendButton.setDisable(false);
                    messageField.setDisable(false);
                });

                // Start listening for incoming messages
                String line;
                while ((line = in.readLine()) != null) {
                    String msg = line;
                    Platform.runLater(() -> addMessage(msg));
                }

            } catch (IOException e) {
                Platform.runLater(() -> addMessage("Unable to connect to server"));
                e.printStackTrace();
            }
        }).start();

        // Set send button action
        sendButton.setOnAction(e -> sendMessage());
        messageField.setOnAction(e -> sendMessage()); // allow Enter key to send
    }

    @FXML
    private void sendMessage() {
        if (out == null) {
            addMessage("Connection not ready. Please wait...");
            return;
        }

        String recipient = recipientField.getText().trim();
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            if (!recipient.isEmpty()) {
                out.println("@" + recipient + " " + message);
                addMessage("(You → " + recipient + "): " + message);
            } else {
                out.println(message);
                addMessage("(You): " + message);
            }
            messageField.clear();
        }
    }

    private void addMessage(String message) {
        Label msgLabel = new Label(message);
        msgLabel.setWrapText(true);
        chatBox.getChildren().add(msgLabel);

        // Auto-scroll to bottom
        chatScrollPane.layout();
        chatScrollPane.setVvalue(1.0);
    }

    /** Optional: Call this to close socket on application exit */
    public void closeConnection() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUsername(String currentUser) {
        this.username = currentUser;
        usernameLabel.setText("Chat - " + username);
    }
}
