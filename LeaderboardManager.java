
//Leaderboard Manager
// Features: sorting toggle, persistent data, CSV import/export, range filter, delete, clear, stats, dark mode, highlight top

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

class Participant implements Serializable {
    String name;
    int score;
    LocalDateTime submissionTime;

    public Participant(String name, int score) {
        this.name = name;
        this.score = score;
        this.submissionTime = LocalDateTime.now();
    }

    public void updateScore(int newScore) {
        this.score = newScore;
        this.submissionTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return name + " | Score: " + score + " | Time: " + submissionTime+"\n";
    }
}

class Leaderboard implements Serializable {
    Map<String, Participant> participants = new HashMap<>();

    public void addParticipant(String name, int score) {
        participants.putIfAbsent(name, new Participant(name, score));
        participants.get(name).updateScore(score);
    }

    public void deleteParticipant(String name) {
        participants.remove(name);
    }

    public java.util.List<Participant> getSortedLeaderboard(boolean sortByScore) {
        return participants.values().stream().sorted((a, b) -> {
            if (sortByScore) {
                return b.score != a.score ? b.score - a.score : a.submissionTime.compareTo(b.submissionTime);
            } else {
                return a.name.compareToIgnoreCase(b.name);
            }
        }).collect(Collectors.toList());
    }

    public java.util.List<Participant> getTopK(int k, boolean sortByScore) {
        return getSortedLeaderboard(sortByScore).subList(0, Math.min(k, participants.size()));
    }

    public java.util.List<Participant> getRange(int min, int max, boolean sortByScore) {
        return getSortedLeaderboard(sortByScore).stream()
                .filter(p -> p.score >= min && p.score <= max).collect(Collectors.toList());
    }

    public void exportToCSV(String file) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write("Name,Score,Time");
        for (Participant p : getSortedLeaderboard(true)) {
            writer.write(p.name + "," + p.score + "," + p.submissionTime + "\n");
        }
        writer.close();
    }

    public void importFromCSV(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        reader.readLine(); // skip header
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 2) {
                String name = parts[0];
                int score = Integer.parseInt(parts[1]);
                addParticipant(name, score);
            }
        }
        reader.close();
    }

    public void saveData(String file) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
        out.writeObject(participants);
        out.close();
    }

    public void loadData(String file) throws IOException, ClassNotFoundException {
        File f = new File(file);
        if (!f.exists()) return;
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        participants = (Map<String, Participant>) in.readObject();
        in.close();
    }

    public void resetLeaderboard() {
        participants.clear();
    }

    public int totalParticipants() {
        return participants.size();
    }

    public double averageScore() {
        return participants.values().stream().mapToInt(p -> p.score).average().orElse(0);
    }

    public Participant highestScorer() {
        return participants.values().stream().max(Comparator.comparingInt(p -> p.score)).orElse(null);
    }
}

public class LeaderboardManager {
    private static Leaderboard leaderboard = new Leaderboard();
    private static JTextArea outputArea;
    private static boolean sortByScore = true;
    private static final String DATA_FILE = "leaderboard.dat";

    public static void main(String[] args) {
        try { leaderboard.loadData(DATA_FILE); } catch (Exception e) { }

        JFrame frame = new JFrame("Leaderboard Manager");
        frame.setSize(900, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        Font font = new Font("Arial", Font.PLAIN, 14);
        JTextField nameField = new JTextField(10);
        JTextField scoreField = new JTextField(5);
        JTextField topKField = new JTextField(3);
        JTextField minScoreField = new JTextField(3);
        JTextField maxScoreField = new JTextField(3);
        JTextField deleteField = new JTextField(10);

        JButton addBtn = new JButton("Add/Update");
        JButton viewBtn = new JButton("View");
        JButton topKBtn = new JButton("Top K");
        JButton toggleBtn = new JButton("Toggle Sort(Score/Lexicographic)");
        JButton resetBtn = new JButton("Reset");
        JButton expBtn = new JButton("Export CSV");
        JButton impBtn = new JButton("Import CSV");
        JButton rangeBtn = new JButton("Score Range");
        JButton delBtn = new JButton("Delete");
        JButton clearBtn = new JButton("Clear Display");
        JButton statsBtn = new JButton("Stats");
        JButton darkBtn = new JButton("Dark Mode");

        outputArea = new JTextArea(30, 75);
        outputArea.setFont(font);
        outputArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(outputArea);

        for (Component c : new Component[]{nameField, scoreField, topKField, minScoreField, maxScoreField, deleteField,
            addBtn, viewBtn, topKBtn, toggleBtn, resetBtn, expBtn, impBtn, rangeBtn, delBtn, clearBtn, statsBtn, darkBtn}) {
            c.setFont(font);
        }

        frame.add(new JLabel("Name:")); frame.add(nameField);
        frame.add(new JLabel("Score:")); frame.add(scoreField); frame.add(addBtn);
        frame.add(viewBtn); frame.add(toggleBtn); frame.add(new JLabel("Top K:")); frame.add(topKField); frame.add(topKBtn);
        frame.add(new JLabel("Min:")); frame.add(minScoreField); frame.add(new JLabel("Max:")); frame.add(maxScoreField); frame.add(rangeBtn);
        frame.add(new JLabel("Delete:")); frame.add(deleteField); frame.add(delBtn);
        frame.add(statsBtn); frame.add(clearBtn); frame.add(resetBtn); frame.add(expBtn); frame.add(impBtn); frame.add(darkBtn);
        frame.add(scroll);

        ActionListener save = e -> { try { leaderboard.saveData(DATA_FILE); } catch (Exception ignored) {} };

        addBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String sc = scoreField.getText().trim();
            try {
                int s = Integer.parseInt(sc);
                leaderboard.addParticipant(name, s);
                outputArea.append("Added: " + name + " - " + s + "");
                save.actionPerformed(null);
            } catch (Exception ex) { outputArea.append("Invalid input."); 
        }
        });

        viewBtn.addActionListener(e -> showLeaderboard());
        toggleBtn.addActionListener(e -> { sortByScore = !sortByScore; showLeaderboard(); });
        topKBtn.addActionListener(e -> {
            try {
                int k = Integer.parseInt(topKField.getText().trim());
                outputArea.append("Top " + k + ":");
                for (Participant p : leaderboard.getTopK(k, sortByScore)) outputArea.append(p + "");
            } catch (Exception ex) { outputArea.append("Invalid K value."); 
        }
        });

        rangeBtn.addActionListener(e -> {
            try {
                int min = Integer.parseInt(minScoreField.getText().trim());
                int max = Integer.parseInt(maxScoreField.getText().trim());
                outputArea.append("Participants in range [" + min + "-" + max + "]:");
                for (Participant p : leaderboard.getRange(min, max, sortByScore)) outputArea.append(p + "");
            } catch (Exception ex) { outputArea.append("Invalid range."); 
        }
        });

        delBtn.addActionListener(e -> {
            String del = deleteField.getText().trim();
            leaderboard.deleteParticipant(del);
            outputArea.append("Deleted " + del + "");
            save.actionPerformed(null);
        });

        statsBtn.addActionListener(e -> {
            outputArea.append("Stats:\n");
            outputArea.append("Total: " + leaderboard.totalParticipants() + "\n");
            outputArea.append("Average: " + leaderboard.averageScore() + "\n");
            Participant top = leaderboard.highestScorer();
            outputArea.append("Top: " + (top != null ? top : "N/A") + "\n");
        });

        resetBtn.addActionListener(e -> {
            leaderboard.resetLeaderboard();
            outputArea.append("Reset complete.");
            save.actionPerformed(null);
        });

        expBtn.addActionListener(e -> {
            try { leaderboard.exportToCSV("leaderboard.csv"); outputArea.append("Exported."); }
            catch (IOException ex) { outputArea.append("Export failed."); 
        }
        });

        impBtn.addActionListener(e -> {
            try { leaderboard.importFromCSV("leaderboard.csv"); outputArea.append("Imported."); 
        }
            catch (IOException ex) { outputArea.append("Import failed."); 
        }
        });

        clearBtn.addActionListener(e -> outputArea.setText(""));

        darkBtn.addActionListener(e -> {
            outputArea.setBackground(Color.DARK_GRAY);
            outputArea.setForeground(Color.WHITE);
        });

        new javax.swing.Timer(10000, e -> showLeaderboard()).start();
        frame.setVisible(true);
    }

    private static void showLeaderboard() {
        outputArea.setText("Leaderboard:\n");
        int rank = 1;
        for (Participant p : leaderboard.getSortedLeaderboard(sortByScore)) {
            outputArea.append((rank == 1 ? "[Top] " : "") + (rank++) + ". " + p + "");
        }
    }
}
