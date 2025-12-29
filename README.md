# 📚 BookPanda - Book Sharing Platform

<div align="center">
  
  ![BookPanda Logo](src/main/resources/com/example/first_draft/images/logo.png)
  
  **A modern JavaFX-based book sharing and rental platform that connects book lovers**
  
  [![Download APK](https://img.shields.io/badge/Download-APK-brightgreen?style=for-the-badge&logo=android)](https://github.com/yourusername/bookpanda/releases/latest/download/bookpanda.apk)
  [![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)](https://www.oracle.com/java/)
  [![JavaFX](https://img.shields.io/badge/JavaFX-21.0.6-blue?style=for-the-badge&logo=java)](https://openjfx.io/)
  [![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)](LICENSE)

</div>

---

## 📖 About BookPanda

BookPanda is a comprehensive book sharing platform that allows users to buy, rent, and exchange books with other book enthusiasts. Built with JavaFX, it provides a modern, intuitive interface with real-time chat functionality for seamless communication between users.

### ✨ Key Features

- 🔐 **User Authentication** - Secure login and registration system with password validation
- 📚 **Book Management** - Add, edit, and manage your personal book collection
- 🛒 **Shopping Cart** - Add books to cart for buying or renting
- 💬 **Real-time Chat** - Built-in messaging system to communicate with other users
- 🔍 **Smart Search** - Search books by title, author, or owner username
- 📊 **Book Tracking** - Track books you've rented out, borrowed, and available for sale
- 🎨 **Modern UI** - Beautiful gradient designs with smooth animations
- 📅 **Rental Management** - Automatic calculation of rental costs and due dates
- 🔄 **Return System** - Late fee calculation for overdue book returns

---

## 🚀 Download & Installation

### Option 1: Download APK (Android)

📱 **[Download BookPanda APK](https://github.com/yourusername/bookpanda/releases/latest/download/bookpanda.apk)**

1. Download the APK file from the link above
2. Enable "Install from Unknown Sources" in your Android settings
3. Install and run the application

> **Note:** Replace `yourusername` with your actual GitHub username in the link above

### Option 2: Build from Source

#### Prerequisites
- Java Development Kit (JDK) 21 or higher
- Maven 3.6 or higher
- JavaFX SDK 21.0.6

#### Build Steps

```bash
# Clone the repository
git clone https://github.com/<yourusername>/bookpanda.git
cd bookpanda/first_draft

# Build with Maven
mvn clean install

# Run the application
mvn javafx:run
```

---

## 🎯 Features in Detail

### 1. User Management
- **Registration**: Create account with email, phone, and location
- **Login**: Secure authentication with password encryption
- **Profile Management**: Update personal information and password
- **Password Visibility**: Toggle to show/hide passwords

### 2. Book Operations
- **Add Books**: Upload book covers, set prices for buying/renting
- **Edit Books**: Modify book details and availability status
- **Search**: Find books by title, author, or owner
- **Categories**: Browse books by genre

### 3. Transaction System
- **Buy Books**: Purchase books permanently
- **Rent Books**: Rent for custom duration with automatic cost calculation
- **Shopping Cart**: Add multiple books before checkout
- **Batch Purchase**: Buy all cart items with single password confirmation

### 4. Communication
- **User Chat**: Real-time messaging between users
- **Chat History**: Persistent chat logs for all conversations
- **User List**: See all available users for chat

### 5. Book Tracking
- **For Sale/Rent**: View your available books
- **Rented Out**: Track books you've rented to others
- **Borrowed**: Monitor books you're currently renting
- **Due Dates**: See when rented books need to be returned

---

## 🏗️ Project Structure

```
bookSharing/
│
├── README.md                      # Main project documentation
│
└── first_draft/                   # Maven project root
    │
    ├── pom.xml                    # Maven configuration
    ├── mvnw                       # Maven wrapper (Unix)
    ├── mvnw.cmd                   # Maven wrapper (Windows)
    │
    ├── database/                  # Data storage
    │   ├── books.dat              # Serialized book data
    │   ├── users.dat              # Serialized user data
    │   └── genres.txt             # Book genres list
    │
    ├── images/                    # Uploaded book cover images
    │   └── [timestamped_images]
    │
    ├── chatlogs/                  # Chat conversation logs
    │   └── [username_folders]/
    │
    ├── src/main/
    │   ├── java/com/example/first_draft/
    │   │   ├── app/               # Application entry point
    │   │   │   └── Main.java
    │   │   │
    │   │   ├── controllers/       # JavaFX controllers
    │   │   │   ├── LoginController.java
    │   │   │   ├── MainLayoutController.java
    │   │   │   ├── HomePageController.java
    │   │   │   ├── BookDetailsController.java
    │   │   │   ├── MyBooksController.java
    │   │   │   ├── SearchPageController.java
    │   │   │   ├── AddBookController.java
    │   │   │   ├── AccountPageController.java
    │   │   │   └── ...
    │   │   │
    │   │   ├── model/             # Data models
    │   │   │   ├── Book.java
    │   │   │   └── User.java
    │   │   │
    │   │   ├── dbManager/         # Database management
    │   │   │   ├── BookDatabase.java
    │   │   │   ├── UserDatabase.java
    │   │   │   └── GenreDatabase.java
    │   │   │
    │   │   ├── cart/              # Shopping cart functionality
    │   │   │   ├── Cart.java
    │   │   │   ├── CartItem.java
    │   │   │   └── CartController.java
    │   │   │
    │   │   └── chat/              # Chat system
    │   │       ├── ChatServer.java
    │   │       ├── ClientHandler.java
    │   │       ├── ChatLogManager.java
    │   │       └── ChatPageController.java
    │   │
    │   └── resources/com/example/first_draft/
    │       ├── fxml/              # FXML layout files
    │       ├── css/               # Stylesheets
    │       └── images/            # UI resources
    │
    └── target/                    # Build output directory
        └── classes/
```

---

## 🛠️ Technologies Used

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 21+ | Core programming language |
| JavaFX | 21.0.6 | UI framework |
| Maven | 3.6+ | Build automation |
| CSS3 | - | UI styling with gradients |
| Socket Programming | - | Real-time chat |
| Serialization | - | Data persistence |

---

## 💻 System Requirements

### Minimum Requirements
- **OS**: Windows 10/11, macOS 10.14+, or Linux
- **Java**: JDK 21 or higher

---

## 📝 Usage Guide

### First Time Setup

1. **Start the Application**
   ```bash
   mvn javafx:run
   ```

2. **Register an Account**
   - Click "Sign Up" on the login page
   - Fill in your details (username, email, phone, location, password)
   - Password must be 8+ characters with uppercase, lowercase, and numbers

3. **Start the Chat Server** (Optional - for chat features)
   ```bash
   cd src/main/java
   java com.example.first_draft.chat.ChatServer
   ```

### Adding a Book

1. Navigate to "My Books" → Click "Add New Book+"
2. Fill in book details (title, author, description, genre)
3. Upload a cover image
4. Set buy price and/or rent price per day
5. Click "Save"

### Buying or Renting

1. Browse books on the Home page or use Search
2. Click on a book to see details
3. Click "Add to Cart" and choose "Buy" or "Rent"
4. For rentals, select the rental period
5. Go to Cart → Click "Purchase All"
6. Enter your password to confirm

### Chatting with Users

1. Navigate to the Chat page
2. Select a user from the list
3. Type your message and press Enter or click Send
4. Chat history is automatically saved

---

## 🔐 Security Features

- **Password Validation**: Enforces strong passwords with multiple character types
- **Password Confirmation**: All transactions require password verification
- **Data Encryption**: User passwords are securely stored
- **Session Management**: Secure user session handling

---

