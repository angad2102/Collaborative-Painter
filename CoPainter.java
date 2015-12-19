import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CoPainter{

	 JFrame frame1;
	 JPanel panel;
	 JLabel hlabel, plabel;
	 JTextField host, port;
	 JButton sh,ch;

	 public String IP;
	 public int Port;
	 public int choice; // 1: server; 2: client

	 public static void main(String[] args) {
		CoPainter F = new CoPainter(); F.go();
	 }

	 public void go() {
		 frame1 = new JFrame();
		 frame1.setTitle( "Collaborative Painter" );
		 panel = new JPanel();
		 frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 frame1.getContentPane().add(panel);
		 hlabel = new JLabel("Host:");
		 panel.add( hlabel);
		 host = new JTextField(20);
		 panel.add(host);
		 sh = new JButton("Start as a host");
		 ch = new JButton("Connect to a host");
		 plabel = new JLabel("Port:");
		 panel.add(plabel);
		 port = new JTextField(20);
		 panel.add(port);
		 panel.add(sh);
		 panel.add(ch);
		 sh.addActionListener(new Host());
		 ch.addActionListener(new Client());
		 frame1.setSize(300, 125);
		 frame1.setVisible(true);
	 } // End of go()

	 class Client implements ActionListener {
		 public void actionPerformed(ActionEvent e) {

			 choice=2;
			 try {
			 IP=host.getText();
			 Port=Integer.parseInt(port.getText());
			 Paint n = new Paint(IP,choice,Port);
			 frame1.setVisible(false);
			 frame1.dispose();
			 n.go();
			 }
			 catch (Exception E) {

			 }

		 }
	 }

	 class Host implements ActionListener {
		 public void actionPerformed(ActionEvent e) {

			 choice=1;
				try {
					 Port=Integer.parseInt(port.getText());
					 Paint n = new Paint("",choice,Port);
					 frame1.setVisible(false);
					 frame1.dispose();
					 n.go();


					}
					catch (Exception E) {

					}


		}
	}
}
