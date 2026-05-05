import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class MindCareSplash extends JWindow {
    private int duration; // Duration of the spiral convergence
    private int elapsedTime = 0;
    private int revealDuration = 1000; // 1 second for the gradual logo fade
    private int holdDuration = 1000; // 1 second to see the logo after reveal
    private Timer animationTimer; // This now correctly uses javax.swing.Timer
    private Random random = new Random();
    private ArrayList<SpiralStreak> streaks = new ArrayList<>();
    private Image mindCareLogo;

    public MindCareSplash(int d) {
        this.duration = d;
        setSize(450, 400);
        setLocationRelativeTo(null);
        try {
            // Ensure this path is correct on your computer
            String path = "C:\\Users\\vansh\\Downloads\\Gemini_Generated_Image_qvssctqvssctqvss.png";
            mindCareLogo = new ImageIcon(path).getImage();
        } catch (Exception e) {
            System.out.println("Logo not found, continuing with text only.");
        }

        for (int i = 0; i < 15; i++) {
            streaks.add(new SpiralStreak(i * 24)); 
        }
    }

    public void showSplash() {
        JPanel canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                
                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // --- PHASE 1: STREAK CONVERGENCE ---
                if (elapsedTime <= duration) {
                    float spiralProgress = (float) elapsedTime / duration;
                    for (SpiralStreak s : streaks) {
                        s.update(centerX, centerY, spiralProgress);
                        g2.setColor(s.color);
                        g2.setStroke(new BasicStroke(s.thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        g2.drawLine((int)s.lastX, (int)s.lastY, (int)s.x, (int)s.y);
                    }
                } 
                // --- PHASE 2 & 3: LOGO REVEAL AND HOLD ---
                else {
                    float revealProgress = (float) (elapsedTime - duration) / revealDuration;
                    revealProgress = Math.min(1.0f, revealProgress); // Cap at 100%

                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, revealProgress));
                    
                    if (mindCareLogo != null) {
                        g2.drawImage(mindCareLogo, (getWidth()-140)/2, (getHeight()-220)/2, 140, 140, this);
                    }
                    
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 42));
                    g2.setColor(new Color(74, 144, 226));
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString("MindCare", (getWidth() - fm.stringWidth("MindCare")) / 2, 280);
                }

                // Border logic
                g2.setComposite(AlphaComposite.SrcOver);
                g2.setColor(new Color(74, 144, 226));
                g2.setStroke(new BasicStroke(4));
                g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                
                g2.dispose();
            }
        };
        
        add(canvas);
        setVisible(true);
        
        // This is the Swing Timer that handles the transition
        animationTimer = new Timer(16, e -> {
            elapsedTime += 16;
            
            // Total time = convergence + reveal + hold duration
            if (elapsedTime >= (duration + revealDuration + holdDuration)) { 
                animationTimer.stop();
                setVisible(false);
                dispose();
                // Opens the Login Page automatically
                SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
            }
            canvas.repaint();
        });
        animationTimer.start();
    }

    class SpiralStreak {
        double angle, radius, x, y, lastX, lastY;
        float thickness = 18f;
        Color color;
        double startAngle;
        Color[] palette = {new Color(102, 0, 153), new Color(138, 43, 226), new Color(0, 51, 153)};
        
        SpiralStreak(int startAngle) { 
            this.startAngle = Math.toRadians(startAngle); 
            this.color = palette[random.nextInt(palette.length)]; 
        }
        
        void update(int centerX, int centerY, float progress) {
            radius = 350 * Math.max(0, 1.0 - progress);
            angle = startAngle + (progress * 12); 
            
            lastX = (x == 0) ? centerX + Math.cos(angle) * radius : x;
            lastY = (y == 0) ? centerY + Math.sin(angle) * radius : y;
            
            x = centerX + Math.cos(angle) * radius; 
            y = centerY + Math.sin(angle) * radius;
        }
    }

    public static void main(String[] args) { 
        new MindCareSplash(2000).showSplash(); 
    }
}