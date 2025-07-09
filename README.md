 # 🏆Competitive Leaderboard Manager – Java Swing Desktop App

A desktop-based application that manages competitive programming leaderboards, allowing users to add/update participants, view rankings, export data, and more. Built using Java Swing with real-time data management and score tracking features.

---

## 💡 Features

- 📝 Add / Update Participant Scores
- 📊 View Leaderboard (Sorted by Score & Time)
- 🔍 Search Participants
- 📌 Top-K Ranking Display
- 🧹 Reset Leaderboard
- 📤 Export Leaderboard to CSV
- 🔄 Auto-refresh every 10 seconds
- 🎨 User-friendly GUI with responsive layout

---

## 🛠️ Technologies Used

- **Java** (JDK 17)
- **Java Swing** – GUI framework
- **Data Structures** – HashMap, ArrayList
- **File Handling** – CSV Export via `FileWriter`
- **Timer API** – Auto-refresh logic
- **OOP Concepts** – Encapsulation, Modular Design

---

## 📂 Project Structure
LeaderboardManager/
├── LeaderboardManager.java # Main application with Swing GUI
├── Participant.java # Participant data model
├── Leaderboard.java # Core logic: add, sort, export, etc.
├── leaderboard.csv # (Generated) CSV file on export
└── README.md # Project description
## 🎮 How to Run

1. Clone the Repository
   ```bash
   git clone https://github.com/your-username/LeaderboardManager.git
   cd LeaderboardManager
2. Compile and Run

bash
Copy
Edit
javac LeaderboardManager.java
java LeaderboardManager
📈 Real-World Applications
This project simulates systems used in:

Coding Contests (LeetCode, Codeforces, TopCoder)

Hackathons and Academic Competitions

Classroom Quizzes with ranking

Sports / Game Leaderboards

It mimics real-time ranking systems that sort by score and time and allows CSV data export for reporting or analysis.

📸 Screenshots
(Add screenshots of the GUI here after running the project)

🧠 Concepts Applied
Concept	Application
HashMap	Storing participants and their scores
ArrayList & Sorting	Ranking participants by score + submission time
FileWriter	CSV export feature
LocalDateTime	Timestamp-based tiebreaker in leaderboard
Java Swing	GUI for input/output interactions
Timer API	Auto-refresh leaderboard

✅ Sample Use Case
text
Copy
Edit
Input:
- Name: Alice
- Score: 95

- Name: Bob
- Score: 95 (entered later than Alice)

Leaderboard Output:
1. Alice | Score: 95 | Time: ...
2. Bob   | Score: 95 | Time: ...
📤 Export Output
Exports a file named leaderboard.csv like:

c
Copy
Edit
Name,Score,Time
Alice,95,2025-06-28T14:22:12.308
Bob,90,2025-06-28T14:19:10.125
🔮 Future Enhancements
Persistent storage using database (e.g., SQLite)

Dark mode theme toggle

Chart-based performance tracking

Login system for admins

CSV import functionality

🧾 References
Java SE Docs – Oracle

GeeksforGeeks – Java Swing

LeetCode Leaderboard

Stack Overflow

Java FileWriter Guide

🤝 Author
Yukesh Kumar
💼 B.Tech CSE (AI & ML), LPU
📌 Java & DSA Enthusiast | Competitive Programmer

Feel free to fork, star ⭐, and contribute!

yaml
Copy
Edit

---

Let me know if you'd like me to auto-generate this as a downloadable `README.md` file or embed screenshots for a visual README.
