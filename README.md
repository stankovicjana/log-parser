# Log Analysis and Sharing Platform (Clojure Web App)

## Overview

This is a full-stack Clojure web application that provides user-friendly tools for parsing, analyzing, and sharing logs. It includes real-time error tracking and email notifications for specific file paths, with social features for sharing results with friends.

---

### Features

### User Management
- User registration and login
- Session-based authentication
- Profile section with personal information
- Add, edit, and remove friends
- Share parsed log analysis with friends

### Log Parsing & Analysis
- Upload `.txt` log files for analysis
- Highlight key events and errors
- Store analyzed results per user
- Display parsed logs in structured and searchable format

You can find a sample log file here:  
[resources/logs/...](resources/logs/...)

### Email Notifications
- Automatically monitor specific log files (e.g., from a watched path)
- Detect errors in real-time
- Notify users via email with attached logs when issues occur

### Sent Email Tracking
- Every email sent from the system (including notifications) is stored in the database
- Logged per user, with full subject, body, recipient, and timestamp

---

## Technologies Used

- **Clojure** – backend logic and routing (Compojure, Ring)
- **PostgreSQL** – relational database
- **clojure.java.jdbc** – database interaction
- **Hiccup** – HTML generation
- **Migratus** – database migrations

---

## Getting Started

1. Clone the repo:
   ```bash
   git clone https://github.com/stankovicjana/log-parser.git
   cd log-parser
2. Set up PostgreSQL and run migrations:
   lein migratus migrate
3. Run the app:
   lein run
4. Visit the app at http://localhost:3000

Environment Variables (required for email)

Set DATABASE_PARSER e.g. postgresql://admin:admin@localhost:5432/parser

Notes
- Email notifications are sent when an error line is detected in a watched log file.

- All sent emails are saved in the sent_emails table and linked to the sending user.
