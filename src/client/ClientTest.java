import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ClientTest {
    public static void main(String[] args){
        JFrame frame = new JFrame();
        Object result = JOptionPane.showInputDialog(frame, "Enter IP Adress");
        Client bob;
        bob = new Client((String)result); //change this later
        bob.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        bob.startRunning();
    }
}
