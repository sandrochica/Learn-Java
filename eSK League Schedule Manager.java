import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class RoundRobinManagerGUI {
    private static class RoundRobinManager {
        private ArrayList<String> teams;

        public RoundRobinManager() {
            teams = new ArrayList<>();
        }

        public String addTeam(String teamName) {
            if (teamName != null && !teamName.isEmpty() && !teams.contains(teamName)) {
                teams.add(teamName);
                return "Team \"" + teamName + "\" added successfully!";
            }
            return "Team \"" + teamName + "\" already exists or invalid name.";
        }

        public String updateTeam(int index, String newName) {
            if (index >= 0 && index < teams.size() && newName != null && !newName.isEmpty()) {
                String oldName = teams.get(index);
                teams.set(index, newName);
                return "Team \"" + oldName + "\" updated to \"" + newName + "\".";
            }
            return "Invalid index or name.";
        }

        public String deleteTeam(int index) {
            if (index >= 0 && index < teams.size()) {
                String removedTeam = teams.remove(index);
                return "Team \"" + removedTeam + "\" deleted successfully!";
            }
            return "Invalid index.";
        }

        public String generateSchedule() {
            if (teams.size() < 3) {
                return "Not enough teams to generate a schedule. Minimum 3 teams required.";
            }
            if (teams.size() > 10) {
                return "Too many teams. Maximum 10 teams allowed.";
            }

            ArrayList<String> schedule = new ArrayList<>();
            ArrayList<String> tempTeams = new ArrayList<>(teams);
            if (tempTeams.size() % 2 != 0) {
                tempTeams.add("Bye");
            }

            int numRounds = tempTeams.size() - 1;
            int numMatchesPerRound = tempTeams.size() / 2;

            for (int round = 0; round < numRounds; round++) {
                StringBuilder roundMatches = new StringBuilder("Round " + (round + 1) + ":\n");
                for (int match = 0; match < numMatchesPerRound; match++) {
                    String team1 = tempTeams.get(match);
                    String team2 = tempTeams.get(tempTeams.size() - match - 1);
                    if (!team1.equals("Bye") && !team2.equals("Bye")) {
                        roundMatches.append(team1).append(" vs ").append(team2).append("\n");
                    }
                }
                schedule.add(roundMatches.toString());
                tempTeams.add(1, tempTeams.remove(tempTeams.size() - 1)); // Rotate teams
            }

            teams = new ArrayList<>(teams.subList(0, teams.size() - (tempTeams.contains("Bye") ? 1 : 0))); // Remove
                                                                                                           // dummy team
                                                                                                           // if added
            return String.join("\n\n", schedule);
        }

        public ArrayList<String> getTeams() {
            return teams;
        }
    }

    public static void main(String[] args) {
        RoundRobinManager manager = new RoundRobinManager();

        // JFrame setup
        JFrame frame = new JFrame("eSK-League Schedule Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(new BorderLayout());

        // Header Section
        JPanel headerPanel = new JPanel(new FlowLayout());
        headerPanel.add(new JLabel("e"));
        headerPanel.add(new JLabel("S", JLabel.CENTER) {
            {
                setForeground(Color.BLUE);
            }
        });
        headerPanel.add(new JLabel("K", JLabel.CENTER) {
            {
                setForeground(Color.RED);
            }
        });
        headerPanel.add(new JLabel("-League Schedule Manager"));
        frame.add(headerPanel, BorderLayout.NORTH);

        // Add Team Section
        JPanel addPanel = new JPanel();
        JTextField teamField = new JTextField(20);
        JButton addButton = new JButton("Add Team");
        addButton.addActionListener(e -> {
            String teamName = teamField.getText();
            String message = manager.addTeam(teamName);
            showMessage(frame, message);
            updateTeamList(frame, manager.getTeams());
            teamField.setText("");
        });
        addPanel.add(teamField);
        addPanel.add(addButton);
        frame.add(addPanel, BorderLayout.CENTER);

        // Update Team Section
        JPanel updatePanel = new JPanel();
        JTextField updateIndexField = new JTextField(5);
        JTextField updateNameField = new JTextField(15);
        JButton updateButton = new JButton("Update Team");
        updateButton.addActionListener(e -> {
            try {
                int index = Integer.parseInt(updateIndexField.getText()) - 1;
                String newName = updateNameField.getText();
                String message = manager.updateTeam(index, newName);
                showMessage(frame, message);
                updateTeamList(frame, manager.getTeams());
            } catch (NumberFormatException ex) {
                showMessage(frame, "Invalid index. Please enter a valid number.", true);
            }
        });
        updatePanel.add(updateIndexField);
        updatePanel.add(updateNameField);
        updatePanel.add(updateButton);
        frame.add(updatePanel, BorderLayout.SOUTH);

        // Delete Team Section
        JPanel deletePanel = new JPanel();
        JTextField deleteIndexField = new JTextField(5);
        JButton deleteButton = new JButton("Delete Team");
        deleteButton.addActionListener(e -> {
            try {
                int index = Integer.parseInt(deleteIndexField.getText()) - 1;
                String message = manager.deleteTeam(index);
                showMessage(frame, message);
                updateTeamList(frame, manager.getTeams());
            } catch (NumberFormatException ex) {
                showMessage(frame, "Invalid index. Please enter a valid number.", true);
            }
        });
        deletePanel.add(deleteIndexField);
        deletePanel.add(deleteButton);
        frame.add(deletePanel, BorderLayout.EAST);

        // Generate Schedule Section
        JPanel schedulePanel = new JPanel();
        JButton generateButton = new JButton("Generate Schedule");
        generateButton.addActionListener(e -> {
            String schedule = manager.generateSchedule();
            if (schedule.contains("Not enough teams") || schedule.contains("Too many teams")) {
                showMessage(frame, schedule, true);
            } else {
                showMessage(frame, schedule);
            }
        });
        schedulePanel.add(generateButton);
        frame.add(schedulePanel, BorderLayout.WEST);

        // Text Area for Team List
        JTextArea teamListArea = new JTextArea(10, 50);
        teamListArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(teamListArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Display frame
        frame.setVisible(true);

        // Update the team list display
        updateTeamList(frame, manager.getTeams());
    }

    private static void showMessage(JFrame frame, String message) {
        showMessage(frame, message, false);
    }

    private static void showMessage(JFrame frame, String message, boolean isError) {
        JOptionPane.showMessageDialog(frame, message, isError ? "Error" : "Info",
                isError ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
    }

    private static void updateTeamList(JFrame frame, ArrayList<String> teams) {
        JTextArea teamListArea = (JTextArea) ((JScrollPane) frame.getContentPane().getComponent(2)).getViewport()
                .getView();
        StringBuilder teamList = new StringBuilder();
        for (int i = 0; i < teams.size(); i++) {
            teamList.append((i + 1)).append(". ").append(teams.get(i)).append("\n");
        }
        teamListArea.setText(teamList.toString());
    }
}
