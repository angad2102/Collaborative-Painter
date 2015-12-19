import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Paint {
	public String IP;
	public int Port;
	public int choice;

	ObjectInputStream is;
	ObjectOutputStream os;
	ArrayList<ObjectInputStream> IS = new ArrayList<ObjectInputStream>();
	ArrayList<ObjectOutputStream> OS = new ArrayList<ObjectOutputStream>();

	MyPanel p;
	ArrayList<Point> points = new ArrayList<Point>();
	ArrayList<Point> points2 = new ArrayList<Point>();
	JFrame frame;
	ArrayList<ArrayList<Point>> path = new ArrayList<ArrayList<Point>>();
	JButton blu;
	JButton re;
	JButton bla;
	JButton wh;
	JButton gr;
	JButton pe1;
	JButton pe2;
	JButton pe3;
	JButton pe4;
	Color color=Color.black;
	Integer size=20;
	ArrayList<Color> clr = new ArrayList<Color>();
	ArrayList<Integer> sz = new ArrayList<Integer>();

	public Paint(String ip,int ch,int po) {
		IP=ip;
		choice=ch;
		Port=po;
	}
	public void go() {
		JMenuBar MenuBar = new JMenuBar();
		JMenu menu1 = new JMenu("Action");
		JMenuItem clear = new JMenuItem("clear");
		JMenuItem save = new JMenuItem("save");
		JMenuItem load = new JMenuItem("load");
		JMenuItem exit = new JMenuItem("exit");
		clear.addActionListener(new clear());
		save.addActionListener(new save());
		load.addActionListener(new load());
		exit.addActionListener(new exit());
		menu1.add(clear);
		menu1.add(save);
		menu1.add(load);
		menu1.add(exit);
		ImageIcon blue = new ImageIcon("blue.png");
		ImageIcon red = new ImageIcon("red.png");
		ImageIcon black = new ImageIcon("black.png");
		ImageIcon white = new ImageIcon("white.png");
		ImageIcon green = new ImageIcon("green.png");
		ImageIcon point1 = new ImageIcon("p1.png");
		ImageIcon point2 = new ImageIcon("p2.png");
		ImageIcon point3 = new ImageIcon("p3.png");
		ImageIcon point4 = new ImageIcon("p4.png");
		blu = new JButton(blue);
		re = new JButton(red);
		bla = new JButton(black);
		wh = new JButton(white);
		gr = new JButton(green);
		pe1 = new JButton(point1);
		pe2 = new JButton(point2);
		pe3 = new JButton(point3);
		pe4 = new JButton(point4);
		frame = new JFrame();
		frame.setJMenuBar(MenuBar);
		MenuBar.add(menu1);
		frame.setTitle( "Collaborative Painter" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
		p = new MyPanel();
		frame.getContentPane().add(p);
		p.addMouseListener(p);
		p.addMouseMotionListener(p);
		frame.setSize( 600, 600);
		frame.setVisible( true);
		JPanel p1 = new JPanel();
		frame.getContentPane().add(BorderLayout.SOUTH, p1);
		p1.add(blu);
		blu.addActionListener(new colorb());
		p1.add(re);
		re.addActionListener(new colorr());
		p1.add(bla);
		bla.addActionListener(new colorbl());
		p1.add(wh);
		wh.addActionListener(new colorw());
		p1.add(gr);
		gr.addActionListener(new colorg());
		p1.add(pe1);
		pe1.addActionListener(new pen1());
		p1.add(pe2);
		pe2.addActionListener(new pen2());
		p1.add(pe3);
		pe3.addActionListener(new pen3());
		p1.add(pe4);
		pe4.addActionListener(new pen4());
		Thread t2 = new Thread(new POI()); t2.start();
		if(choice==2){
			Thread t = new Thread(new InReader()); t.start();
		}
	}

	public class InReader implements Runnable {
		@SuppressWarnings("unchecked")
		public void run() {
			while (true) {
				try {
					if(choice==2) {
						float s = (float) is.readObject();
						System.out.println(s);
						points2 = (ArrayList<Point>) is.readObject();
						path.add(points2);
						clr.add((Color) is.readObject());
						sz.add((Integer) is.readObject());
						p.repaint();
			 			if(s==2.0) {
				 			System.out.println("abchd");
							path.clear();
							clr.clear();
							sz.clear();
							color=Color.BLACK;
							size=20;
							points.clear();
							p.clear();
						}
			 		if (s==3.0) {
				 		JOptionPane.showMessageDialog(frame, "Host is gone!");
				 		try {
							is.close();
							os.close();
						} catch (IOException e1) {
						e1.printStackTrace();
						}
						frame.setVisible(false);
						frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			 		}
			 		System.out.println(s);
				}

			}
				catch (Exception e) {

				}
			}
		}
	}

	public class OutReader implements Runnable {
		public void run() {
			try {
			float o=1;
				if(choice==2) {
					os.reset();
					//	os.writeObject(o);
					os.writeObject(points);
					os.writeObject(color);
					os.writeObject(size);
					os.flush();
					}
				else if(choice==1) {
					float ip=1;
					for(int i=0;i<OS.size();i++) {
						OS.get(i).reset();
						OS.get(i).writeObject(ip);
						OS.get(i).writeObject(points);
						OS.get(i).writeObject(color);
						OS.get(i).writeObject(size);
						OS.get(i).flush();
					}
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public class IR implements Runnable {
		Socket s;
		ObjectInputStream OIS;
		public  IR(Socket a){
			s=a;
		}
		public void run() {
			try {
				OIS = new ObjectInputStream(s.getInputStream());
				try {
					while (true){
						points2 = (ArrayList<Point>) OIS.readObject();
						path.add(points2);
						Color a;
						Integer b;
						a=(Color) OIS.readObject();
						clr.add(a);
						b=(Integer) OIS.readObject();
						sz.add(b);
						p.repaint();
						float send = 1;
						for(int i=0;i<OS.size();i++) {
							OS.get(i).reset();
							OS.get(i).writeObject(send);
							OS.get(i).writeObject(points2);
							OS.get(i).writeObject(a);
							OS.get(i).writeObject(b);
							OS.get(i).flush();
						}
					}
				}
				 catch (IOException b) {

				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
		}
	}
}

	public class POI implements Runnable {
		ServerSocket ss;
		 Socket s;
			public void run() {
				if (choice == 1) {
					try {
						ss = new ServerSocket(Port);
						frame.setTitle("Collaborative Painter (HOST)");
						while (true){
							s = ss.accept();
							os = new ObjectOutputStream(s.getOutputStream()); System.out.println("Connection established!");
							OS.add(os);
							Thread t = new Thread(new IR(s));
							t.start();
							float o=1;
							for(int i=0;i<path.size();i++) {
								os.reset();
								os.writeObject(o);
								os.writeObject(path.get(i));
								os.writeObject(clr.get(i));
								os.writeObject(sz.get(i));
								os.flush();
							}
						}
					 } catch (IOException e) {
						 	e.printStackTrace();
							System.out.println("jklf");
						 JOptionPane.showMessageDialog(frame, "Unable to listen to port "+Port+"!");
						 frame.setVisible(false);
						 frame.dispose();
						 CoPainter c = new CoPainter();
						 c.go();
					}
				}
				else if (choice == 2) {
					try {
						s = new Socket(IP, Port);
						frame.setTitle("Collaborative Painter (CLIENT)");
						os = new ObjectOutputStream(s.getOutputStream()); is = new ObjectInputStream(s.getInputStream()); System.out.println("Connection established!");
					} catch (IOException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(frame, "Unable to connect to host!");
						 frame.setVisible(false);
						 frame.dispose();
						 CoPainter c = new CoPainter();
						 c.go();
					}
				}
		}
	}

	class clear implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(choice==1){
			path.clear();
			clr.clear();
			sz.clear();
			color=Color.BLACK;
			size=20;
			points.clear();
			p.clear();
			float send = 2;
			for(int i=0;i<OS.size();i++) {
				try {
				OS.get(i).reset();
				Point a = new Point();
				Point b = new Point();
				a.x=0;
				a.y=0;
				b.x=0;
				b.y=0;
				points.add(a);
				points.add(b);
				OS.get(i).writeObject(send);
			  OS.get(i).writeObject(points);
				OS.get(i).writeObject(color);
				OS.get(i).writeObject(0);
				OS.get(i).flush();
				points.clear();
				}
				catch (IOException eb) {
					System.out.println("abcd");
				}
			 }
			}
		}
	}

	class save implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser();
			int returnVal = chooser.showSaveDialog(frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					FileOutputStream f = new FileOutputStream(chooser.getSelectedFile());
					ObjectOutputStream os = new ObjectOutputStream(f);
					os.reset();
					os.writeObject(path);
					os.writeObject(clr);
					os.writeObject(sz);
					os.flush();
					os.close();
				}
				catch (IOException e1 ) {
					e1.printStackTrace();
				}
			}
		}
	}

	class load implements ActionListener {
		@SuppressWarnings("unchecked")
		public void actionPerformed(ActionEvent e) {
			if (choice ==1){
			JFileChooser chooser = new JFileChooser();
			int returnVal = chooser.showOpenDialog(frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					FileInputStream f = new FileInputStream(chooser.getSelectedFile());
					try {
						ObjectInputStream is1 = new ObjectInputStream(f);
						path.clear();
						clr.clear();
						sz.clear();
						points.clear();
						path = (ArrayList<ArrayList<Point>>) is1.readObject();
						clr = (ArrayList<Color>) is1.readObject();
						sz = (ArrayList<Integer>) is1.readObject();
						is1.close();
						p.repaint();
						float send = 2;
						for(int i=0;i<OS.size();i++)
						{
							try {
							OS.get(i).reset();
							Point a = new Point();
							Point b = new Point();
							a.x=0;
							a.y=0;
							b.x=0;
							b.y=0;
							points.add(a);
							points.add(b);
							OS.get(i).writeObject(send);
						  OS.get(i).writeObject(points);
							OS.get(i).writeObject(color);
							OS.get(i).writeObject(0);
							OS.get(i).flush();
							points.clear();
							}
							catch (IOException eb) {
								System.out.println("abcd");
							}
						}

						for(int i=0;i<OS.size();i++) {
							try {
							send =1;
							for(int j=0;j<path.size();j++){
								OS.get(i).writeObject(send);
						  	OS.get(i).writeObject(path.get(j));
								OS.get(i).writeObject(clr.get(j));
								OS.get(i).writeObject(sz.get(j));
								OS.get(i).flush();
								points.clear();
							}
						}
							catch (IOException eb) {
								System.out.println("abcd");
							}
						}
					}
					catch (Exception C) {
						System.out.println("hub");
					}
				}
				catch (Exception C ) {
					System.out.println("hub2");
				}
			}
		}
	}
}

	class exit implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(choice==2) {
			try {
				is.close();
				os.close();
				} catch (IOException e1) {
				e1.printStackTrace();
				}
			frame.setVisible(false);
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
			else if (choice ==1) {
				float send=3;
				for(int i=0;i<OS.size();i++) {
					try {
						OS.get(i).reset();
						Point a = new Point();
						Point b = new Point();
						a.x=0;
						a.y=0;
						b.x=0;
						b.y=0;
						points.add(a);
						points.add(b);
						OS.get(i).writeObject(send);
				  	OS.get(i).writeObject(points);
						OS.get(i).writeObject(color);
						OS.get(i).writeObject(0);
						OS.get(i).flush();
						points.clear();
						OS.get(i).close();
					}
					catch (IOException eb) {
						System.out.println("abcd");
						}
					}
					frame.setVisible(false);
					frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		}
	}

	class colorb implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			color=Color.blue;
		}
	}

	class colorr implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			color=Color.red;
		}
	}

	class colorbl implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			color=Color.black;
		}
	}

	class colorw implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			color=Color.white;
		}
	}

	class colorg implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			color=Color.green;
		}
	}

	class pen1 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			size=2;
		}
	}

	class pen2 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			size=5;
		}
	}

	class pen3 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			size=10;
		}
	}

	class pen4 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			size=20;
		}
	}

	class MyPanel extends JPanel implements MouseListener, MouseMotionListener {
		/**
		 *
		 */
	private static final long serialVersionUID = 1L;
	public void paintComponent( Graphics g) {
		g.setColor(Color.white); //Erase the previous figures
		g.fillRect(0, 0, getWidth(), getHeight());
		Point prevPoint = null;
		for (int i = 0; i < path.size(); i++) {
			if(g instanceof Graphics2D)	{
				Graphics2D g2D = (Graphics2D) g;
				g2D.setStroke(new BasicStroke(sz.get(i), BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND));
			}
			prevPoint = null;
			g.setColor(clr.get(i)); //Erase the previous figures
			for (Point p: path.get(i)) {
				if (prevPoint != null) {
					g.drawLine(prevPoint.x, prevPoint.y, p.x, p.y);
				}
					prevPoint = p;
			}
		}
		if(g instanceof Graphics2D) {
			Graphics2D g2D = (Graphics2D) g;
			g2D.setStroke(new BasicStroke(size, BasicStroke.CAP_ROUND,
			BasicStroke.JOIN_ROUND));
		}
		prevPoint = null;
		g.setColor(color);
		for (Point p: points) {
			if (prevPoint != null) {
				g.drawLine(prevPoint.x, prevPoint.y, p.x, p.y);
			}
			prevPoint = p;
		}
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		points.add(event.getPoint());
		repaint();
	}
	@Override
	public void mouseMoved(MouseEvent event) { }
	@Override
	public void mouseClicked(MouseEvent event) { }
	@Override
	public void mouseEntered(MouseEvent event) { }
	@Override
	public void mouseExited(MouseEvent event) { }
	@Override
	public void mousePressed(MouseEvent event) {
		points = new ArrayList<Point>();
		points.add(event.getPoint());
		repaint();
	}
	@Override
	public void mouseReleased(MouseEvent event) {
		path.add(points);
		clr.add(color);
		sz.add(size);
		Thread t = new Thread(new OutReader()); t.start();
	}
	public void clear() {
		repaint();
	}
} // End of MyPanel
}
