# 📚 BookPanda - Book Sharing Platform

<div align="center">

  ![BookPanda Logo](first_draft/src/main/resources/com/example/first_draft/images/logo.png)

  ### 💾 [**Download Windows Installer**](https://drive.google.com/file/d/1N44BgLfjMljwaKDzvGARJ9zi0ksvtLpy/view?usp=sharing)
  *Note: Does not include existing databases, chatlogs, and images - starts with fresh data*
  
  ### 📥 [**Download Executable JAR File**](https://drive.google.com/file/d/1KsnhfojAjngrScvNWYVY5Aq-gbKkq6O-/view?usp=sharing)

</div>

---

## 📖 About BookPanda

BookPanda is a comprehensive book sharing platform that allows users to buy, rent, and exchange books with other book enthusiasts. Built with JavaFX, it provides a modern, intuitive interface with real-time chat functionality for seamless communication between users.

### 🎥 [**Watch Demonstration Video**](https://youtu.be/28lsGtrP_fI)

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

### Option 1: Download Executable JAR File

#### Prerequisites
- **Java Runtime Environment (JRE) 21 or higher** - [Download here](https://www.oracle.com/java/technologies/downloads/)

#### Steps

1. **Download the JAR file** from [this link](https://drive.google.com/file/d/1KsnhfojAjngrScvNWYVY5Aq-gbKkq6O-/view?usp=sharing)

2. **Extract the downloaded file** (if it's in a zip/archive)

3. **Run the application**
   
   Windows (cmd.exe):
   ```cmd
   REM Navigate to the directory where you downloaded the JAR file
   cd path\to\downloaded\file
   
   REM Run the JAR file
   java -jar BookPanda.jar
   ```
   
   Linux/macOS:
   ```bash
   # Navigate to the directory where you downloaded the JAR file
   cd path/to/downloaded/file
   
   # Run the JAR file
   java -jar BookPanda.jar
   ```
   
   **Or simply double-click** the JAR file if you have Java properly configured on your system.

---

### Option 2: Windows Installer

#### Prerequisites
- **Windows 10/11** operating system

#### Steps

1. **Download the Windows Installer** from [this link](https://drive.google.com/file/d/1N44BgLfjMljwaKDzvGARJ9zi0ksvtLpy/view?usp=sharing)

2. **Run the installer**
   - Double-click the installer file (`.msi`)
   - Follow the installation wizard prompts
   - Complete the installation

3. **Launch BookPanda**
   - Find BookPanda in your Desktop shortcut
   - Double-click to launch the application
   
   **Note**: The installer includes all required dependencies, including Java Runtime, so you don't need to install Java separately. However, it does not include the existing databases, chatlogs, and user-uploaded images - the application will start with fresh/empty data files.

---

### Option 3: Build from Source

#### Prerequisites
- **Java Development Kit (JDK) 21 or higher** - [Download here](https://www.oracle.com/java/technologies/downloads/)
- **Maven** (optional - Maven wrapper included in project)
- **Git** - [Download here](https://git-scm.com/downloads)

#### Build Steps (Windows - cmd.exe)

```cmd
REM Clone the repository
git clone https://github.com/OOP-Project-Book-Sharing/bookSharing.git

REM Navigate to the Maven project folder
cd bookSharing/first_draft

REM Build and run with Maven wrapper (recommended)
mvnw.cmd clean javafx:run
```


#### Build Steps (Linux/macOS)

```bash
# Clone the repository
git clone https://github.com/OOP-Project-Book-Sharing/bookSharing.git

# Navigate to the Maven project folder
cd bookSharing/first_draft

# Build and run with Maven wrapper
./mvnw clean javafx:run
```

#### Alternative: Using Installed Maven

```cmd
REM If you have Maven installed globally
mvn clean javafx:run
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
└── first_draft/                   # Maven project root
    │
    ├── pom.xml                    # Maven configuration
    ├── mvnw / mvnw.cmd            # Maven wrapper scripts
    │
    ├── database/                  # Data storage (books.dat, users.dat, genres.txt)
    ├── images/                    # User-uploaded book covers
    ├── chatlogs/                  # Chat message history per user
    ├── out/                       # For executable Java File
    │
    ├── src/main/
    │   ├── java/
    │   │   ├── module-info.java
    │   │   └── com/example/first_draft/
    │   │       ├── Main.java
    │   │       ├── Main_1.java
    │   │       ├── Book.java
    │   │       ├── User.java
    │   │       ├── BookDatabase.java
    │   │       ├── UserDatabase.java
    │   │       ├── GenreDatabase.java
    │   │       ├── controller/     # JavaFX FXML controllers
    │   │       ├── cart/           # Shopping cart system
    │   │       └── chat/           # Real-time messaging
    │   │
    │   └── resources/com/example/first_draft/
    │       ├── css/                # Stylesheets
    │       ├── fxml/               # UI layouts
    │       └── images/             # Static UI resources (logo, etc.)
    │
    └── target/                     # Build output (auto-generated)
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
- **RAM**: 4GB minimum, 8GB recommended
- **Disk Space**: 500MB for application + dependencies

---

## 📝 Usage Guide

### First Time Setup

1. **Start the Application**
   
   Windows (cmd.exe):
   ```cmd
   REM Navigate to BookPanda folder
   cd bookSharing\first_draft
   
   REM Run with Maven wrapper
   mvnw.cmd clean javafx:run
   ```
   
   Linux/macOS:
   ```bash
   # Navigate to BookPanda folder
   cd bookSharing/first_draft
   
   # Run with Maven wrapper
   ./mvnw clean javafx:run
   ```

2. **Register an Account**
   - Click "Sign Up" on the login page
   - Fill in your details (username, email, phone, location, password)
   - Password must be 8+ characters with uppercase, lowercase, and numbers

3. **Start the Chat Server** (Optional - for chat features)
   
   The chat server runs on port 5000. You can start it manually:
   
   Windows:
   ```cmd
   cd first_draft\src\main\java
   java com.example.first_draft.chat.ChatServer
   ```
   
   Linux/macOS:
   ```bash
   cd first_draft/src/main/java
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


