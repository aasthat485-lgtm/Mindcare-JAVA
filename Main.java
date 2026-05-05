package mindcare;
public class Main {
    public static void main(String[] args) {
               MindCareSplash splash = new MindCareSplash(3000);
                splash.showSplash();  
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               
                new LoginPage(); 
            }
        });
    }
}