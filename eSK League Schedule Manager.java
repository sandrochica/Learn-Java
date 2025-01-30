import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class RoundRobinManagerGUI {
    private ArrayList<String> teams = new ArrayList<>();
    private JFrame frame;
    private JTextArea displayText;
    private JLabel teamListLabel;
    private JTextField teamEntry, updateIndexEntry, updateNameEntry, deleteIndexEntry;

    public RoundRobinManagerGUI() {
        // Initialize the main frame
        frame = new JFrame("eSK-League Schedule Manager");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Header Section
        JPanel headerPanel = new JPanel();
        JLabel headerText = new JLabel("e");
        headerText.setFont(new Font("Arial", Font.BOLD, 18));
        headerText.setForeground(Color.BLACK);

        JLabel headerTextS = new JLabel("S");
        headerTextS.setFont(new Font("Arial", Font.BOLD, 18));
        headerTextS.setForeground(Color.BLUE);

        JLabel headerTextK = new JLabel("K");
        headerTextK.setFont(new Font("Arial", Font.BOLD, 18));
        headerTextK.setForeground(Color.RED);

        JLabel headerTextRest = new JLabel("-League Schedule Manager");
        headerTextRest.setFont(new Font("Arial", Font.BOLD, 18));
        headerTextRest.setForeground(Color.BLACK);

        headerPanel.add(headerText);
        headerPanel.add(headerTextS);
        headerPanel.add(headerTextK);
        headerPanel.add(headerTextRest);
        frame.add(headerPanel, BorderLayout.NORTH);

        // Center Panel for Inputs and Buttons
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Add Team Section
        JPanel addPanel = new JPanel();
        teamEntry = new JTextField(20);
        JButton addButton = new JButton("Add Team");
        addButton.addActionListener(e -> addTeam());
        addPanel.add(teamEntry);
        addPanel.add(addButton);
        centerPanel.add(addPanel);

        // Update Team Section
        JPanel updatePanel = new JPanel();
        updateIndexEntry = new JTextField("Index", 5);
        updateNameEntry = new JTextField("New Name", 15);
        JButton updateButton = new JButton("Update Team");
        updateButton.addActionListener(e -> updateTeam());
        updatePanel.add(updateIndexEntry);
        updatePanel.add(updateNameEntry);
        updatePanel.add(updateButton);
        centerPanel.add(updatePanel);

        // Delete Team Section
        JPanel deletePanel = new JPanel();
        deleteIndexEntry = new JTextField(5);
        JButton deleteButton = new JButton("Delete Team");
        deleteButton.addActionListener(e -> deleteTeam());
        deletePanel.add(deleteIndexEntry);
        deletePanel.add(deleteButton);
        centerPanel.add(deletePanel);

        // Generate Schedule Button
        JButton generateButton = new JButton("Generate Schedule");
        generateButton.addActionListener(e -> generateSchedule());
        centerPanel.add(generateButton);

        // Team List Label
        teamListLabel = new JLabel();
        teamListLabel.setVerticalAlignment(SwingConstants.TOP);
        centerPanel.add(teamListLabel);

        frame.add(centerPanel, BorderLayout.CENTER);

        // Scrollable Display Text
        displayText = new JTextArea(15, 50);
        displayText.setEditable(false);
        displayText.setLineWrap(true);
        displayText.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(displayText);
        frame.add(scrollPane, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void addTeam() {
        String teamName = teamEntry.getText().trim();
        if (!teamName.isEmpty() && !teams.contains(teamName)) {
            teams.add(teamName);
            showMessage("Team \"" + teamName + "\" added successfully!", false);
            updateTeamList();
        } else {
            showMessage("Team \"" + teamName + "\" already exists or invalid name.", true);
        }
        teamEntry.setText("");
    }

    private void updateTeam() {
        try {
            int index = Integer.parseInt(updateIndexEntry.getText().trim()) - 1;
            String newName = updateNameEntry.getText().trim();
            if (index >= 0 && index < teams.size() && !newName.isEmpty()) {
                String oldName = teams.get(index);
                teams.set(index, newName);
                showMessage("Team \"" + oldName + "\" updated to \"" + newName + "\".", false);
                updateTeamList();
            } else {
                showMessage("Invalid index or name.", true);
            }
        } catch (NumberFormatException e) {
            showMessage("Invalid index. Please enter a valid number.", true);
        }
    }

    private void deleteTeam() {
        try {
            int index = Integer.parseInt(deleteIndexEntry.getText().trim()) - 1;
            if (index >= 0 && index < teams.size()) {
                String removedTeam = teams.remove(index);
                showMessage("Team \"" + removedTeam + "\" deleted successfully!", false);
                updateTeamList();
            } else {
                showMessage("Invalid index.", true);
            }
        } catch (NumberFormatException e) {
            showMessage("Invalid index. Please enter a valid number.", true);
        }
    }

    private void generateSchedule() {
        if (teams.size() < 3) {
            showMessage("Not enough teams to generate a schedule. Minimum 3 teams required.", true);
            return;
        }
        if (teams.size() > 10) {
            showMessage("Too many teams. Maximum 10 teams allowed.", true);
            return;
        }

        ArrayList<String> tempTeams = new ArrayList<>(teams);
        if (tempTeams.size() % 2 != 0) {
            tempTeams.add("Bye");
        }

        StringBuilder schedule = new StringBuilder();
        int numRounds = tempTeams.size() - 1;
        int numMatchesPerRound = tempTeams.size() / 2;

        for (int round = 0; round < numRounds; round++) {
            schedule.append("Round ").append(round + 1).append(":\n");
            for (int match = 0; match < numMatchesPerRound; match++) {
                String team1 = tempTeams.get(match);
                String team2 = tempTeams.get(tempTeams.size() - 1 - match);
                if (!team1.equals("Bye") && !team2.equals("Bye")) {
                    schedule.append(team1).append(" vs ").append(team2).append("\n");
                }
            }
            schedule.append("\n");
            tempTeams.add(1, tempTeams.remove(tempTeams.size() - 1));
        }

        displayText.setText(schedule.toString());
    }

    private void updateTeamList() {
        StringBuilder teamList = new StringBuilder();
        for (int i = 0; i < teams.size(); i++) {
            teamList.append(i + 1).append(". ").append(teams.get(i)).append("\n");
        }
        teamListLabel.setText("<html>" + teamList.toString().replace("\n", "<br>") + "</html>");
    }

    private void showMessage(String message, boolean isError) {
        JOptionPane.showMessageDialog(frame, message, isError ? "Error" : "Success", isError ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RoundRobinManagerGUI::new);
    }
}
