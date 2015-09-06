import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.awt.Font;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.*;
import java.util.EventObject;
import java.lang.Character;
import java.awt.Image;
import javax.swing.ImageIcon;


public class Ventana0 extends JFrame implements ActionListener{

	JButton simuladorBoton,ensambladorBoton;
	JLabel hola,mensaje;
	int simulador=0;
	JLabel FI,UNAM,LOGO;
	JEditorPane info;
	JTextField Info;
	public Ventana0(){
		super("Z80");
		Image icon = new ImageIcon(getClass().getResource("imagenes/Z-80_64.png")).getImage();
        setIconImage(icon);
		//setIconImage(Toolkit.getDefaultToolkit().getImage("/imagenes/Z-80_64.png"));
		//this.setIconImage("/imagenes/Z-80_64.png"); 
		setSize(800,600);
		setResizable(false);
		setLayout(null);

		getContentPane().setBackground(new java.awt.Color(255,255,255));

		simuladorBoton=new JButton("SIMULADOR");
		simuladorBoton.setBounds(330,160,140,40);
		simuladorBoton.addActionListener(this);
		add(simuladorBoton);

		ensambladorBoton=new JButton("ENSAMBLADOR");
		ensambladorBoton.setBounds(330,230,140,40);
		ensambladorBoton.addActionListener(this);
		add(ensambladorBoton);

		mensaje=new JLabel("     PROXIMAMENTE");
		mensaje.setBounds(330,230,140,40);
		mensaje.setVisible(false);
		add(mensaje);

		ImageIcon icono = new javax.swing.ImageIcon(getClass().getResource("/imagenes/unam.png"));

		 Image imagen = icono.getImage();
		 ImageIcon iconoEscalado = new ImageIcon (imagen.getScaledInstance(331/2,392/2,Image.SCALE_SMOOTH));
		 FI=new JLabel();
		 FI.setIcon(iconoEscalado);
		 FI.setBounds(100,100,350/2,392/2);
		 add(FI);

		 ImageIcon icono2 = new javax.swing.ImageIcon(getClass().getResource("/imagenes/fi_e.jpg"));

		 Image imagen2 = icono2.getImage();
		 ImageIcon iconoEscalado2 = new ImageIcon (imagen2.getScaledInstance(331/2,392/2,Image.SCALE_SMOOTH));
		 UNAM=new JLabel();
		 UNAM.setIcon(iconoEscalado2);
		 UNAM.setBounds(530,100,(331/2),392/2);
		 add(UNAM);

		  ImageIcon icono3 = new javax.swing.ImageIcon(getClass().getResource("/imagenes/Z-80_256.png"));

		 Image imagen3 = icono3.getImage();
		 ImageIcon iconoEscalado3 = new ImageIcon (imagen3.getScaledInstance(86,86,Image.SCALE_SMOOTH));
		 LOGO=new JLabel();
		 LOGO.setIcon(iconoEscalado3);
		 LOGO.setBounds(357,30,128,128);
		 add(LOGO);

		 info=new JEditorPane();
		 info.setContentType("text/html");
		 info.setEditable(false);
		 //info.setEnabled(false);
		 info.setText(
		"<center>"+"<head><base href=\"file:d:/\"></head><br>"+
		"<b>Universidad Nacional Autonoma de M\u00e9xico</b><br>" + 
		"<br>Facultad de Ingenier\u00eda<br>" +
		"<br>Templos Carbajal Alberto <br><br>"+
		"Cruz Mart\u00ednez Antonio<br>"+
		"Delgadillo Hern\u00E1ndez Estefania<br>"+
		"Guerra Hern\u00E1ndez Vicente Manuel <br>"+
		"Luna Flores Lorena<br>"+"</center>"+
		"<P align=right>2013</P>");
		 info.setBounds(100,320,600,300);
		 add(info); 
		 

		 

		 //Cerrar correctamente la ventana 
	

	this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});

	}
	public void actionPerformed(ActionEvent e){
		String comando=e.getActionCommand();
		switch(comando){
			case "SIMULADOR":
				simulador=1;
				simuladorBoton.setText("HOLA");
			break;
			case "ENSAMBLADOR":
				ensambladorBoton.setVisible(false);
				mensaje.setVisible(true);
			break;
		}
	}
}