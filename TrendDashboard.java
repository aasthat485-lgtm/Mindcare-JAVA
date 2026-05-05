package mindcare;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class TrendDashboard extends JFrame {

    public TrendDashboard(ArrayList<Mood> moodList) {
        setTitle("Weekly Mood Trend Dashboard");
        setSize(550, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 250, 255));

        JLabel title = new JLabel("📊 Weekly Mood Trend Analysis", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setOpaque(true);
        title.setBackground(new Color(102, 178, 255));
        title.setForeground(Color.WHITE);
        title.setPreferredSize(new Dimension(550, 60));
        panel.add(title, BorderLayout.NORTH);

        JTextArea resultArea = new JTextArea();
        resultArea.setFont(new Font("Arial", Font.PLAIN, 16));
        resultArea.setEditable(false);
        resultArea.setBackground(Color.WHITE);
        resultArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        resultArea.setText(analyzeWeeklyTrend(moodList));

        panel.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        add(panel);
        setVisible(true);
    }

    private String analyzeWeeklyTrend(ArrayList<Mood> moodList) {
        if (moodList.size() < 7) {
            return "Please add at least 7 mood entries for weekly trend analysis.";
        }

        HashMap<String, Integer> moodCount = new HashMap<>();
        int start = moodList.size() - 7;

        for (int i = start; i < moodList.size(); i++) {
            String mood = normalizeMood(moodList.get(i).getMood());
            moodCount.put(mood, moodCount.getOrDefault(mood, 0) + 1);
        }

        int max = 0;
        for (int count : moodCount.values()) {
            if (count > max) max = count;
        }

        ArrayList<String> dominantMoods = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : moodCount.entrySet()) {
            if (entry.getValue() == max) {
                dominantMoods.add(entry.getKey());
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("📅 Weekly Analysis (Last 7 Entries)\n\n");

        for (String mood : moodCount.keySet()) {
            sb.append(mood).append(" : ").append(moodCount.get(mood)).append("\n");
        }

        if (dominantMoods.size() == 7) {
            sb.append("\n⭐ Dominant Weekly Mood: None\n\n");
        } else {
            String label = (dominantMoods.size() > 1) ? "\n⭐ Dominant Weekly Moods: " : "\n⭐ Dominant Weekly Mood: ";
            sb.append(label).append(String.join(", ", dominantMoods)).append("\n\n");
        }
        
        sb.append("💡 Recommendation: ").append(getMultiMoodSuggestion(dominantMoods));

        return sb.toString();
    }

    private String normalizeMood(String mood) {
        if (mood.contains("Happy")) return "Happy";
        if (mood.contains("Sad")) return "Sad";
        if (mood.contains("Anger")) return "Anger";
        if (mood.contains("Fear")) return "Fear";
        if (mood.contains("Stressed")) return "Stressed";
        if (mood.contains("Surprised")) return "Surprised";
        return "Other";
    }

    private String getMultiMoodSuggestion(ArrayList<String> moods) {
        if (moods.size() >= 3) {
            return "Your emotional landscape is quite varied. Focus on small, consistent routines to keep things stable and maintain a positive track forward.";
        } else if (moods.size() == 2) {
            String m1 = moods.get(0);
            String m2 = moods.get(1);

            // --- HANDLING "OTHER" COMBINATIONS ---
            if (m1.equals("Other") || m2.equals("Other")) {
                String known = m1.equals("Other") ? m2 : m1;
                return "You are navigating " + known + " alongside other varied emotions. It is important to handle these shifting feelings by talking to trusted people or a professional counselor.";
            }

            // --- SPECIFIC LOGICAL COMBINATIONS ---
            if (moods.contains("Happy")) {
                return "You're balancing positive energy with other challenges. Use your 'Happy' days as a foundation to mindfully process your other emotions.";
            }
            if (moods.contains("Sad") && moods.contains("Stressed")) {
                return "The combination of sadness and stress can be exhausting. Focus on deep rest and gentle self-care to ease the mental pressure.";
            }
            if (moods.contains("Anger") && moods.contains("Stressed")) {
                return "Tension and frustration are high. Practice breathing exercises and physical movement to release built-up stress safely.";
            }
            if (moods.contains("Fear") && moods.contains("Stressed")) {
                return "Anxiety and pressure are interacting. Break your worries into small, manageable steps and focus only on what you can control today.";
            }
            if (moods.contains("Sad") && moods.contains("Anger")) {
                return "You are experiencing a complex mix of grief and frustration. Allow yourself to feel both without judgment while seeking a healthy outlet for expression.";
            }

            // --- GENERAL FALLBACK FOR REMAINING PAIRS ---
            return "You are navigating a dual emotional trend. Acknowledge the validity of both feelings and take intentional, balanced steps toward your well-being.";
        } else {
            return getSuggestion(moods.get(0));
        }
    }

    private String getSuggestion(String mood) {
        switch (mood) {
            case "Happy": return "Great emotional stability this week. Keep maintaining your positive habits ✨";
            case "Sad": return "Try talking to a trusted friend or take time for self-care 💙";
            case "Fear": return "Break big tasks into smaller steps and focus on one at a time.";
            case "Anger": return "Take deep breaths, pause, and avoid reacting immediately.";
            case "Stressed": return "Take short breaks, sleep well, and prioritize important tasks.";
            case "Surprised": return "Reflect on unexpected events and note whether they were positive or stressful.";
            default: return "Maintain regular mood tracking for better personalized insights.";
        }
    }
}