import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUI extends JFrame{
    private JPanel main;

    public GUI(int width, int height){
        this.setSize(new Dimension(width, height));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        main = new JPanel();

        JButton startServerButton = new JButton("Start");
        startServerButton.addActionListener(e -> {
            Main.startServer();
        });

        JButton stopServerButton = new JButton("Stop");
        stopServerButton.addActionListener(e -> {
            Main.stopServer();
        });
        


        main.add(startServerButton);
        main.add(stopServerButton);

        this.add(main);

        // Safely shutting down the server threads
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Main.stopServer();
            }
        });

        this.setVisible(true);
    }





}
