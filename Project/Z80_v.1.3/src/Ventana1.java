import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
//import java.awt.Font;
//import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
//import java.io.File;
import java.io.*;
import java.util.EventObject;
import java.lang.Character;
//import javax.swing.ImageIcon;
//import javax.swing.JFrame;
import java.awt.image.BufferedImage;
import java.io.File;
//import java.awt.Image;
import javax.imageio.ImageIO;



public class Ventana1 extends JFrame implements ActionListener{
	
	JTextField cajaDir,cajaEditar,cajaDireccion,cajaDirCarga,cajaRegistro;
	char banderas[];
	JLabel dirLabel,codigoLabel,evento,evento1,dirCargaLabel,label,eventoReg,errorLabel,frecuenciaLabel,mensajeLabel;
	JLabel[] contenidoRegistrosLabel=new JLabel[24];
	JLabel[] fila=new JLabel[16];
	JLabel[] columna=new JLabel[16];
	JLabel[] memoriaLabel=new JLabel[256]; // falta
	JLabel[] registrosLabel=new JLabel[14];
	JLabel[] pilaLabel=new JLabel[26];
	JLabel[] desensambleLabel=new JLabel[9];
	JButton limpiar,botonIr,editarBoton,botonActualizar,siguienteBoton,cargarHexBoton,pasoApaso,corridaLibre,restablecerBoton;
	JFileChooser explorador;
	FileNameExtensionFilter filter;
	File archivo;
	int posicionX=0,posicion,n,dircarga;
	String textoCaja,path;
	Font courier,courierMini,arial,courierBOLD;
	Master memo1=new Master();
	BufferedReader br;
	int i,w,contNOP;
	float frecuencia;

	JRadioButton[] frec=new JRadioButton[5];
	ButtonGroup grupoFrec;

	String HEXADECIMAL = "0123456789ABCDEF";
	
	//int VK_ENTER;
	

public Ventana1(){
	super("SIMULADOR Z80");
	//setIconImage(Toolkit.getDefaultToolkit().getImage("/imagenes/Z-80_64.png"));
	
	 Image icon = new ImageIcon(getClass().getResource("imagenes/Z-80_64.png")).getImage();
        setIconImage(icon);
 

	setSize(800,600);
	setResizable(false);
	setLayout(null);
	//getContentPane().setBackground(new java.awt.Color(255,255,255));
	courier=new Font("Courier",Font.PLAIN,13);
	arial=new Font("Arial",Font.PLAIN,13);
	courierBOLD=new Font("Courier", Font.BOLD,13);
	memo1.limpiarMem(); 
	printMemo();

	explorador = new JFileChooser();
	explorador.setDialogTitle("Selecciona el archivo hexadecimal");
	filter = new FileNameExtensionFilter(" .hex  Z80 ", "hex");
	explorador.setFileFilter(filter);


	for(int i=0;i<16;i++){
		fila[i].setBounds(58+i*22,10,10,15);
		fila[i].setFont(courier);
		add(fila[i]);
	}

	for(int i=0;i<16;i++){
		columna[i].setBounds(10,30+i*20,35,15);
		columna[i].setFont(courier);
		add(columna[i]);
	}

	for(int j=0;j<16;j++){
		for(int i=0;i<16;i++){
			memoriaLabel[i+16*j].setBounds(50+i*22,30+j*20,18,15);
			memoriaLabel[i+16*j].setFont(courier);
			add(memoriaLabel[i+16*j]);
		}
	}


	printPila();
	for(int i=0;i<26;i++){
		add(pilaLabel[i]);
		if(i!=0){
			pilaLabel[i].setFont(courier);
		}
		
	}

	siguienteBoton=new JButton("SIGUIENTE ->");
	siguienteBoton.setBounds(665,380,115,50);
	siguienteBoton.addActionListener(this);
	siguienteBoton.setVisible(false);
	siguienteBoton.setFocusable(true); 
	siguienteBoton.addKeyListener(keyListenerSig);
	add(siguienteBoton);

	pasoApaso=new JButton("Paso a Paso");
	pasoApaso.setBounds(170,380,110,40);
	pasoApaso.setVisible(true);
	pasoApaso.addActionListener(this);
	add(pasoApaso);

	corridaLibre=new JButton("Corrida Libre");
	corridaLibre.setBounds(45,380,110,40);
	corridaLibre.setVisible(true);
	corridaLibre.addActionListener(this);
	add(corridaLibre);

	restablecerBoton=new JButton("RESTABLECER");
	restablecerBoton.setBounds(90,380,150,40);
	restablecerBoton.setVisible(false);
	restablecerBoton.addActionListener(this);
	add(restablecerBoton);

	limpiar=new JButton("Limpiar Memoria");
	limpiar.setBounds(450,10,135,20);
	limpiar.setFocusable(false);
	limpiar.addActionListener(this);
	add(limpiar);

	cajaDir=new JTextField();
	cajaDir.setBounds(450,40,50,20);
	cajaDir.setFont(courier);
	add(cajaDir);
	//cajaDir.setFocusable(false);
	cajaDir.addKeyListener(keyListenerDireccion);

	cajaRegistro=new JTextField();
	cajaRegistro.setVisible(false);
	cajaRegistro.setFont(courier);
	add(cajaRegistro);
	cajaRegistro.addKeyListener(keyListenerRegistros);

	botonIr=new JButton("Ir a Direccion");
	botonIr.setBounds(510,40,115,20);
	botonIr.addActionListener(this);
	botonIr.setFocusable(false);
	add(botonIr);

	dirLabel=new JLabel("Direccion: " + memo1.ajusteMem(posicionX));
	dirLabel.setBounds(450,70,130,30);
	dirLabel.setForeground(Color.RED);
	add(dirLabel);

	editarBoton=new JButton("Editar Contenido");
	editarBoton.setBounds(450,100,135,20);
	editarBoton.addActionListener(this);
	editarBoton.setFocusable(false);
	add(editarBoton);

	cajaEditar=new JTextField();
	cajaEditar.setBounds(450,100,30,20);
	add(cajaEditar);
	cajaEditar.setVisible(false);

	botonActualizar=new JButton("Actualizar");
	botonActualizar.setBounds(510,100,115,20);
	botonActualizar.addActionListener(this);
	add(botonActualizar);
	botonActualizar.setVisible(false);

	registrosLabel[0]=new JLabel("REGISTROS INTERNOS");
	registrosLabel[0].setBounds(450,125,230,30);
	add(registrosLabel[0]);

		registrosLabel[1]=new JLabel("A              A' "); //+memo1.impFormatMemo(memo1.Asec)
		registrosLabel[2]=new JLabel("F              F' ");
		registrosLabel[3]=new JLabel("B       C      B'        C'");
		registrosLabel[4]=new JLabel("D       E      D'        E'");
		registrosLabel[5]=new JLabel("H       L      H'        L'");
		registrosLabel[6]=new JLabel("IX             IY  ");
		registrosLabel[7]=new JLabel("PC             SP  ");
		registrosLabel[8]=new JLabel("I");
		registrosLabel[9]=new JLabel("R");
		registrosLabel[10]=new JLabel("        "+mostrarBanderas());
		registrosLabel[11]=new JLabel("        sz h pnc");
		registrosLabel[12]=new JLabel("IFF");
		registrosLabel[13]=new JLabel("T");

	registrosLabel[10].setForeground(Color.BLUE); 

	for(int i=1;i<10;i++){
		registrosLabel[i].setBounds(450,140+20*i,250,15);
		registrosLabel[i].setFont(courier);
		add(registrosLabel[i]);
	}

	registrosLabel[10].setBounds(450,300,250,15);
	add(registrosLabel[10]);
	registrosLabel[10].setFont(courier);
	registrosLabel[11].setBounds(450,320,250,15);
	registrosLabel[11].setFont(courier);
	add(registrosLabel[11]);
	registrosLabel[12].setBounds(610,300,250,15);
	registrosLabel[12].setFont(courier);
	add(registrosLabel[12]);
	registrosLabel[13].setBounds(610,320,250,15);
	registrosLabel[13].setFont(courier);
	add(registrosLabel[13]);

//REGISTROS

	contenidoRegistrosLabel[0]=new JLabel(formatoRegistros(memo1.A));
	contenidoRegistrosLabel[0].setBounds(465,160,30,15);

	contenidoRegistrosLabel[1]=new JLabel(formatoRegistros(memo1.F));
	contenidoRegistrosLabel[1].setBounds(465,180,30,15);

	contenidoRegistrosLabel[2]=new JLabel(formatoRegistros(memo1.B));
	contenidoRegistrosLabel[2].setBounds(465,200,30,15);

	contenidoRegistrosLabel[3]=new JLabel(formatoRegistros(memo1.C));
	contenidoRegistrosLabel[3].setBounds(490,200,30,15);

	contenidoRegistrosLabel[4]=new JLabel(formatoRegistros(memo1.D));
	contenidoRegistrosLabel[4].setBounds(465,220,30,15);

	contenidoRegistrosLabel[5]=new JLabel(formatoRegistros(memo1.E));
	contenidoRegistrosLabel[5].setBounds(490,220,30,15);

	contenidoRegistrosLabel[6]=new JLabel(formatoRegistros(memo1.H));
	contenidoRegistrosLabel[6].setBounds(465,240,30,15);

	contenidoRegistrosLabel[7]=new JLabel(formatoRegistros(memo1.L));
	contenidoRegistrosLabel[7].setBounds(490,240,30,15);

	contenidoRegistrosLabel[8]=new JLabel(memo1.ajusteMem(memo1.IX));
	contenidoRegistrosLabel[8].setBounds(475,260,60,15);


	contenidoRegistrosLabel[9]=new JLabel(memo1.ajusteMem(memo1.IY));
	contenidoRegistrosLabel[9].setBounds(595,260,60,15);

	contenidoRegistrosLabel[10]=new JLabel(memo1.ajusteMem(memo1.PC));
	contenidoRegistrosLabel[10].setBounds(475,280,60,15);

	contenidoRegistrosLabel[11]=new JLabel(formatoRegistros(memo1.I));
	contenidoRegistrosLabel[11].setBounds(465,300,60,15);

	contenidoRegistrosLabel[12]=new JLabel(formatoRegistros(memo1.R));
	contenidoRegistrosLabel[12].setBounds(465,320,60,15);

	contenidoRegistrosLabel[13]=new JLabel(""+memo1.IFF);
	contenidoRegistrosLabel[13].setBounds(645,300,60,15);

	contenidoRegistrosLabel[14]=new JLabel(""+memo1.T);
	contenidoRegistrosLabel[14].setBounds(635,320,60,15);

	contenidoRegistrosLabel[15]=new JLabel(formatoRegistros(memo1.Ac));
	contenidoRegistrosLabel[15].setBounds(595,160,30,15);

	contenidoRegistrosLabel[16]=new JLabel(formatoRegistros(memo1.Fc));
	contenidoRegistrosLabel[16].setBounds(595,180,30,15);

	contenidoRegistrosLabel[17]=new JLabel(formatoRegistros(memo1.Bc));
	contenidoRegistrosLabel[17].setBounds(595,200,30,15);

	contenidoRegistrosLabel[18]=new JLabel(formatoRegistros(memo1.Cc));
	contenidoRegistrosLabel[18].setBounds(620,200,30,15);

	contenidoRegistrosLabel[19]=new JLabel(formatoRegistros(memo1.Dc));
	contenidoRegistrosLabel[19].setBounds(595,220,30,15);

	contenidoRegistrosLabel[20]=new JLabel(formatoRegistros(memo1.Ec));
	contenidoRegistrosLabel[20].setBounds(620,220,30,15);

	contenidoRegistrosLabel[21]=new JLabel(formatoRegistros(memo1.Hc));
	contenidoRegistrosLabel[21].setBounds(595,240,30,15);

	contenidoRegistrosLabel[22]=new JLabel(formatoRegistros(memo1.Lc));
	contenidoRegistrosLabel[22].setBounds(620,240,30,15);

	contenidoRegistrosLabel[23]=new JLabel(memo1.ajusteMem(memo1.SP));
	contenidoRegistrosLabel[23].setBounds(595,280,60,15);


	for(int i=0;i<24;i++){
			contenidoRegistrosLabel[i].setFont(courier);
			add(contenidoRegistrosLabel[i]);
	}

	desensambleLabel[0]=new JLabel("              D E S E N S A M B L E");
	desensambleLabel[0].setBounds(330,360,300,20);
	add(desensambleLabel[0]); 
	desensambleLabel[1]=new JLabel("  CL    PC    HEXADECIMAL   INSTRUCCION");
	desensambleLabel[1].setBounds(330,380,300,20);
	for(int i=2;i<9;i++){
		desensambleLabel[i]=new JLabel("                             ");
		desensambleLabel[i].setBounds(330,370+20*i,400,20);
		desensambleLabel[i].setFont(courier);
	}
	for(int i=1;i<9;i++){
		//desensambleLabel[i].setFont(courier);
		add(desensambleLabel[i]);
	}
	desensambleLabel[8].setForeground(Color.RED);
	for(int i=0;i<9;i++){
		desensambleLabel[i].setVisible(true);
	}
	
	cargarHexBoton=new JButton("Cargar  .hex");
	cargarHexBoton.setBounds(45,470,240,40);
	cargarHexBoton.setVisible(true);
	cargarHexBoton.addActionListener(this);
	add(cargarHexBoton);

	errorLabel=new JLabel("");
	errorLabel.setBounds(50,515,240,40);
	errorLabel.setVisible(true);
	errorLabel.setForeground(Color.RED);
	add(errorLabel);

	mensajeLabel=new JLabel("");
	mensajeLabel.setBounds(50,535,240,40);
	mensajeLabel.setVisible(true);
	mensajeLabel.setForeground(Color.BLUE);
	add(mensajeLabel);

	dirCargaLabel=new JLabel("Direccion de Carga: ");
	dirCargaLabel.setBounds(70,430,150,30);
	add(dirCargaLabel);

	cajaDirCarga=new JTextField("0000");
	cajaDirCarga.setBounds(200,435,50,20);
	cajaDirCarga.setFont(courier);
	cajaDirCarga.addKeyListener(keyListenerDirCarga);
	add(cajaDirCarga);

	codigoLabel=new JLabel(" ");
	codigoLabel.setBounds(250,500,400,30);
	add(codigoLabel);

	cajaDireccion=new JTextField();
	cajaDireccion.setVisible(false);
	cajaDireccion.setFont(courier);
	add(cajaDireccion);
	cajaDireccion.setFocusable(true);

	//Frecuancias
	
	frec[0]=new JRadioButton("   2 MHz",false);
	frec[1]=new JRadioButton("   4 MHz",true);
	frec[2]=new JRadioButton("   8 MHz",false);
	frec[3]=new JRadioButton("  12 MHz",false);
	frec[4]=new JRadioButton("  20 MHz",false);


	grupoFrec=new ButtonGroup();

	for(int i=0;i<5;i++){
		grupoFrec.add(frec[i]);
		frec[i].setBounds(680,420+i*25,100,18);
		add(frec[i]);
	}

	frecuenciaLabel=new JLabel("FRECUENCIA");
	frecuenciaLabel.setBounds(685,380,115,50);
	add(frecuenciaLabel);

	mouseActionMemoriaLabel();
	mouseActionRegistros();
	mouseActionCajaDir();

	//Cerrar correctamente la ventana 
	

	this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
	

}


// FIN VENTANA()
void printMemo(){

	posicion=memo1.ajusteInt(posicionX);
	for(int i=0;i<16;i++){
		String etiqueta=Integer.toHexString(i);
		fila[i]=new JLabel(etiqueta.toUpperCase());
	}
	
	for(int i=0;i<16;i++){
		columna[i]=new JLabel(memo1.impDir(posicion+i*16));
	}

	for(int j=0;j<16;j++){
		for(int i=0;i<16;i++){
			memoriaLabel[i+16*j]=new JLabel(memo1.impFormatMemo(posicion+(i+16*j)));
		}
	}

}

void printPila(){
	pilaLabel[0]=new JLabel(" ------------------");
	pilaLabel[0].setBounds(700,315,100,10);
	pilaLabel[0].setFont(arial);
	for(int i=1;i<25;i++){
		pilaLabel[i]=new JLabel("|        |");
		pilaLabel[i].setBounds(700,320-12*i,100,12);
		pilaLabel[i].setFont(arial);
	}
	pilaLabel[25]=new JLabel("STACK");
	pilaLabel[25].setBounds(720,15,50,15);

}

void actualizarPila(){
	System.out.println(memo1.stackRec);
	
	if(memo1.marcaPila<25){
		for(int i=(memo1.marcaPila+1);i<25;i++){
		pilaLabel[i].setText("|        |");
		}

		for(int i=1;i<(memo1.marcaPila+1);i++){
		pilaLabel[i].setText("|   "+formatoRegistros(memo1.memo[65535-(i-1)])+"   |");
		}
	}

	if(memo1.marcaPila>24){
		int ppila=memo1.marcaPila-24;
		for(int i=1;i<25;i++){
			pilaLabel[i].setText("|   "+formatoRegistros(memo1.memo[65535-(i-1)-ppila])+"   |");
		}
	}

	
}

void actualizarDesensamble(){
		if(memo1.HALT==0){
			memo1.obtenerInstruccion(memo1.PC);
			if(memo1.INSTRUCCION=="NOP"){
				contNOP++;
			}
			else{
				contNOP=0;
			}
			if(contNOP==100){
				memo1.HALT=1;
			}
		}
		if(verificaError()==false){
			actualizarRegistros();
			desensambleLabel[2].setText(desensambleLabel[3].getText());
			desensambleLabel[3].setText(desensambleLabel[4].getText());
			desensambleLabel[4].setText(desensambleLabel[5].getText());
			desensambleLabel[5].setText(desensambleLabel[6].getText());
			desensambleLabel[6].setText(desensambleLabel[7].getText());
			desensambleLabel[7].setText(desensambleLabel[8].getText());
			desensambleLabel[8].setText(memo1.ajusteMem(memo1.CL)+" "+memo1.ajusteMem(memo1.PC-1)+"     "+memo1.inst+"         " +memo1.INSTRUCCION);
			actualizarPila();
			refrescarMem();
		}
		if(verificaError()==true){
			errorLabel.setText("  ERROR   ");
			if(memo1.PilaError==true){
				errorLabel.setText("  ERROR  PILA "+memo1.errorPila);
			}
			mensajeLabel.setText(" Al realizar:         "+memo1.INSTRUCCION);
			siguienteBoton.setVisible(false);

		}
		
		if(memo1.HALT==1){
			if(contNOP==100){
				errorLabel.setText("   EL PROGRAMA SE DETUVO ");
				mensajeLabel.setText("    POR SEGURIDAD ");
			}
			else{
				errorLabel.setText("          Tiempo de Ejecucion:");
				mensajeLabel.setText("          "+calcTiempo()+" microsegundos");
			}
		}

}

void refrescarLabelMem(){
	for(int i=0;i<256;i++){
		memoriaLabel[i].setVisible(true);
	}
}


void refrescarMem(){
	posicion=memo1.ajusteInt(posicionX);
	//dirLabel.setText("Direccion: " + memo1.ajusteMem(posicionX));
	cajaDireccion.setVisible(false);
	int j=0;
		for(int i=0;i<16;i++){
			int posicionY=ajusteMemImp(posicion,j);
			
			for(int z=0;z<16;z++){
				memoriaLabel[z+16*i].setText(memo1.impFormatMemo(posicion+z+16*j));
			}
			columna[i].setText(memo1.impDir(posicionY));
			j++;
			if(posicionY==65520){
				j=0;
				posicion=0;
			}
			
		}
	for(int i=0;i<256;i++){
		memoriaLabel[i].setVisible(true);
	}

	editarBoton.setVisible(true);
	botonActualizar.setVisible(false);
	cajaEditar.setVisible(false);
}

public void actualizarRegistros(){
	contenidoRegistrosLabel[0].setText(formatoRegistros(memo1.A));
	contenidoRegistrosLabel[1].setText(formatoRegistros(memo1.F));
	contenidoRegistrosLabel[2].setText(formatoRegistros(memo1.B));
	contenidoRegistrosLabel[3].setText(formatoRegistros(memo1.C));
	contenidoRegistrosLabel[4].setText(formatoRegistros(memo1.D));
	contenidoRegistrosLabel[5].setText(formatoRegistros(memo1.E));
	contenidoRegistrosLabel[6].setText(formatoRegistros(memo1.H));
	contenidoRegistrosLabel[7].setText(formatoRegistros(memo1.L));
	contenidoRegistrosLabel[8].setText(memo1.ajusteMem(memo1.IX));
	contenidoRegistrosLabel[9].setText(memo1.ajusteMem(memo1.IY));
	contenidoRegistrosLabel[10].setText(memo1.ajusteMem(memo1.PC));
	contenidoRegistrosLabel[11].setText(formatoRegistros(memo1.I));
	contenidoRegistrosLabel[12].setText(formatoRegistros(memo1.R));
	contenidoRegistrosLabel[13].setText(""+memo1.IFF);
	contenidoRegistrosLabel[14].setText(""+memo1.T);
	contenidoRegistrosLabel[15].setText(formatoRegistros(memo1.Ac));
	contenidoRegistrosLabel[16].setText(formatoRegistros(memo1.Fc));
	contenidoRegistrosLabel[17].setText(formatoRegistros(memo1.Bc));
	contenidoRegistrosLabel[18].setText(formatoRegistros(memo1.Cc));
	contenidoRegistrosLabel[19].setText(formatoRegistros(memo1.Dc));
	contenidoRegistrosLabel[20].setText(formatoRegistros(memo1.Ec));
	contenidoRegistrosLabel[21].setText(formatoRegistros(memo1.Hc));
	contenidoRegistrosLabel[22].setText(formatoRegistros(memo1.Lc));
	contenidoRegistrosLabel[23].setText(memo1.ajusteMem(memo1.SP));

	for(int i=0;i<23;i++){
		contenidoRegistrosLabel[i].setVisible(true);
		//if(Integer.parseInt(contenidoRegistrosLabel[i].getText(),16)>127||Integer.parseInt(contenidoRegistrosLabel[i].getText(),16)<-128){
		//	contenidoRegistrosLabel[i].setText("--");
		//}
	}

	registrosLabel[10].setText("        " +mostrarBanderas());

}

int ajusteMemImp(int x,int y){
		x=x+(16*y);
		return x;
		
}
	public void actionPerformed(ActionEvent e){	
		String comando=e.getActionCommand();
		switch(comando){
			case "Limpiar Memoria":
				memo1.limpiarMem();
				memo1.HALT=0;
				//pasoApaso.setVisible(true);
				//corridaLibre.setVisible(true);
				memo1.T=0;
				errorLabel.setText("");
				mensajeLabel.setText("");
				//siguienteBoton.setVisible(false);
				actualizarRegistros();
				refrescarMem();
				break;
			case "Editar Contenido":
				
				editarBoton.setVisible(false);
				cajaEditar.setVisible(true);
				botonActualizar.setVisible(true);
				//refrescarMem();
				break;

			case "Ir a Direccion":
				textoCaja=cajaDir.getText();
				if(textoCaja.equals("")||Integer.parseInt(textoCaja,16)>65536||Integer.parseInt(textoCaja,16)<0){
					System.out.println("Error");
					cajaDir.setText("");
				}
				else{
					posicionX=(Integer.parseInt(textoCaja,16));
					cajaDir.setText("");
					dirLabel.setText("Direccion: " + memo1.ajusteMem(posicionX));
					refrescarMem();
				}
				
				break;

			case "Actualizar":
				textoCaja=cajaEditar.getText();
				if(textoCaja.equals("")||Integer.parseInt(textoCaja,16)>256||Integer.parseInt(textoCaja,16)<0){
					System.out.println("Error");
					cajaEditar.setText("");
				}
				else{
					memo1.memo[posicionX]=Integer.parseInt(cajaEditar.getText(),16);
					cajaEditar.setText("");
					refrescarMem();
				}
				break;

			case "Cargar  .hex":
				int opcion = explorador.showOpenDialog(explorador);
				errorLabel.setText("");
				mensajeLabel.setText("");
				File archivito=explorador.getSelectedFile();
               
               	int dircarga=Integer.parseInt(cajaDirCarga.getText(),16);
               	System.out.println("Direccion de Carga: "+dircarga);
                    //Si presionamos el boton ABRIR en pathArchivo obtenemos el path del archivo
                    if (opcion == JFileChooser.APPROVE_OPTION) {
                        path= explorador.getSelectedFile().getAbsolutePath();
						//System.out.println(path);
						memo1.ValidarChecksum(path,dircarga);
						if(memo1.CHECKSUMv==0){
							errorLabel.setText("         ARCHIVO CORRUPTO");
							mensajeLabel.setText(" No coincide el byte de checksum");
						}
						else{
							memo1.LeerArchivo(path,dircarga);
							cargarHexBoton.setVisible(false);
							restablecerBoton.setVisible(false);
						}
						

					}
					if (opcion!=JFileChooser.APPROVE_OPTION){
						System.out.println("CERRAR filechooser");
						//refrescarMem();
						break;
					}
					//codigoLabel.setText(lectura.verificarFin(path)+"  "+lectura.leer(path));
					refrescarMem();
					memo1.PC=dircarga;
					actualizarRegistros();
					pasoApaso.setVisible(true);
					corridaLibre.setVisible(true);
					dirCargaLabel.setVisible(false);
					cajaDirCarga.setVisible(false);

			break;

			case "SIGUIENTE ->":
				refrescarMem();
				dircarga=Integer.parseInt(cajaDirCarga.getText(),16);
               	System.out.println("Direccion de Carga: "+dircarga);
               	memo1.botonOK=1;
				actualizarDesensamble();

				break;

			case "Paso a Paso":
				pasoApaso.setVisible(false);
				corridaLibre.setVisible(false);
				cajaDir.setFocusable(false);
				restablecerBoton.setFocusable(false);
				//this.getRootPane().setDefaultButton(siguienteBoton);
				siguienteBoton.requestFocusInWindow();
				siguienteBoton.setVisible(true);
				for(int i=0;i<5;i++){
					frec[i].setVisible(false);
				}
				opcionFrecuencia();
				restablecerBoton.setVisible(true);
				break;
			case "Corrida Libre":
				pasoApaso.setVisible(false);
				corridaLibre.setVisible(false);
				opcionFrecuencia();
				for(int i=0;i<5;i++){
					frec[i].setVisible(false);
				}
				restablecerBoton.setVisible(true);
				while(memo1.HALT==0){
					actualizarDesensamble();
				}
				break;
			case "RESTABLECER":
				restablecerBoton.setVisible(false);
				memo1.HALT=0;
				memo1.T=0;
				memo1.PC=0;
				memo1.CL=0;
				contNOP=0;
				memo1.marcaPila=0;
				corridaLibre.setVisible(true);
				pasoApaso.setVisible(true);
				errorLabel.setText("");
				mensajeLabel.setText("");
				for(int i=0;i<5;i++){
					frec[i].setVisible(true);
				}
				for(int i=2;i<9;i++){
					desensambleLabel[i].setText(" ");
				}
				cargarHexBoton.setVisible(true);
				dirCargaLabel.setVisible(true);
				siguienteBoton.setVisible(false);
				cajaDirCarga.setText("0000");
				cajaDirCarga.setVisible(true);
				actualizarPila();
				limpiarRegistros();
				break;

		}
}

void mouseActionMemoriaLabel(){
	for(int j=0;j<16;j++){
		for(int i=0;i<16;i++){
			memoriaLabel[i+16*j].addMouseListener(new MouseListener()
		{	
		
	public void mouseClicked(MouseEvent arg0) {
		refrescarLabelMem();
		posicionX=Integer.parseInt(columna[0].getText(),16);
		evento=(JLabel)arg0.getComponent();
		cajaDireccion.setBounds(evento.getX()-5,evento.getY()-4,30,21);
		evento.setVisible(false);
		identificarJLabel(evento);
		MostrarCajaDireccion();

	}
	public void mouseEntered(MouseEvent arg1) {
		evento1= (JLabel)arg1.getComponent();
		evento1.setForeground(Color.RED); 
		evento1.setFont(courierBOLD);
	}
	public void mouseExited(MouseEvent arg1) {
		JLabel evento1= (JLabel)arg1.getComponent();
		evento1.setForeground(Color.BLACK); 
		evento1.setFont(courier);
	}
	public void mousePressed(MouseEvent arg0) {

	}
	public void mouseReleased(MouseEvent arg0) {
	
	}
	});

	}
	}
}

public void MostrarCajaDireccion(){
	cajaDireccion.setVisible(true);
	cajaDireccion.requestFocusInWindow();
	cajaDireccion.setText("");
	cajaDireccion.addKeyListener(keyListenerMemoria);

}

public String formatoRegistros(int x){
	
	if (x<16&&x>=0){
			String memoryFormat=Integer.toHexString(x);
			return ("0"+memoryFormat.toUpperCase());
		}
		else{
		if(x<0){
		String memoryFormat=Integer.toHexString(x);
		char[] car=memoryFormat.toCharArray();
		memoryFormat=(""+car[6]+car[7]).toString();
		return memoryFormat.toUpperCase();
	}
		else{
			String memoryFormat=Integer.toHexString(x);
			return String.format("%s",memoryFormat.toUpperCase());
		}
		}

}

//


KeyListener keyListenerMemoria = new KeyListener() {
      public void keyPressed(KeyEvent keyEvent) {
      }

      public void keyReleased(KeyEvent keyEvent) {
        int key = keyEvent.getKeyCode();
        String cadena=cajaDireccion.getText();
        cajaDireccion.setText(cadena.toUpperCase());
        try{
        if(key==keyEvent.VK_ENTER)
        { 	

        	memo1.memo[posicionX]=Integer.parseInt(cajaDireccion.getText(),16);
        	cajaDireccion.setVisible(false);
        	memoriaLabel[w].setText(memo1.impFormatMemo(posicionX));
        	memoriaLabel[w].setVisible(true);

        }
        if(key==keyEvent.VK_SPACE)
        { 
 			memo1.memo[posicionX]=Integer.parseInt(cajaDireccion.getText(),16);
        	cajaDireccion.setVisible(false);
        	System.out.println("Caja :" +Integer.parseInt(cajaDireccion.getText(),16));
        	memoriaLabel[w].setText(memo1.impFormatMemo(posicionX));
        	memoriaLabel[w].setVisible(true);

			//SIGUIENTE DIRECCION
			
        	posicionX++;
        	w++;
        	cajaDireccion.setBounds(memoriaLabel[w].getX()-5,memoriaLabel[w].getY()-4,30,21);
        	memoriaLabel[w].setVisible(false);
        	MostrarCajaDireccion();
        	dirLabel.setText("Direccion: " + memo1.ajusteMem(posicionX));

        }
    	}catch(NumberFormatException ex){
        	System.out.println("->");
    	}
      }

      public void keyTyped(KeyEvent keyEvent) {
      	String texto=cajaDireccion.getText();
      	char caracter = keyEvent.getKeyChar();
      	caracter=Character.toUpperCase(caracter);
      	int key = keyEvent.getKeyCode();
        if (texto.length()==2||!isHex(caracter))
 			{
     			keyEvent.consume();
			}
      }

    };

public void identificarJLabel(JLabel evento){
	w=0;
	posicion=memo1.ajusteInt(posicionX);
	while(cajaDireccion.getX()!=memoriaLabel[w].getX()-5||cajaDireccion.getY()!=memoriaLabel[w].getY()-4){
		w++;
	}
	System.out.println("posicionX = "+formatoRegistros(w+posicion)+"  Label:  "+memoriaLabel[w].getText());
	posicionX=w+posicion;
	dirLabel.setText("Direccion: " + memo1.ajusteMem(posicionX));

}

public String mostrarBanderas(){
	String Fbinario=Integer.toBinaryString(memo1.F);
	banderas=Fbinario.toCharArray();
	while(banderas.length!=8){
            Fbinario="0"+Fbinario;
            banderas=Fbinario.toCharArray();
            }
    return Fbinario;
}

// KEYLISTENER DIRECCION

KeyListener keyListenerDireccion = new KeyListener() {
	public void keyPressed(KeyEvent keyEvento) {
      }

      public void keyReleased(KeyEvent keyEvento) {
        int key = keyEvento.getKeyCode();
        String cadena=cajaDir.getText();
        cajaDir.setText(cadena.toUpperCase());
        if(key==keyEvento.VK_ENTER)
        { 
      				if(cajaDir.getText().equals("")){
      					System.out.println("Vacio");
      				}
      				else{
      					posicionX=(Integer.parseInt(cajaDir.getText(),16));
						cajaDir.setText("");
						dirLabel.setText("Direccion: " + memo1.ajusteMem(posicionX));
						refrescarMem();
      				}
        }
      }
      public void keyTyped(KeyEvent keyEvento) {
      	char caracter = keyEvento.getKeyChar();
      	caracter=Character.toUpperCase(caracter);
        if (cajaDir.getText().length()== 4||!isHex(caracter))
 			{
     			keyEvento.consume();
			}
      }
    };

// KEYLISTENER CAJA DIR CARGA

	KeyListener keyListenerDirCarga= new KeyListener() {
	public void keyPressed(KeyEvent keyEvento) {
      }

      public void keyReleased(KeyEvent keyEvento) {
        int key = keyEvento.getKeyCode();
        String cadena=cajaDirCarga.getText();
        cajaDirCarga.setText(cadena.toUpperCase());
        //if(key==keyEvento.VK_ENTER)
        //{ 
 					//memo1.PC=(Integer.parseInt(cajaDirCarga.getText(),16));
				//	actualizarRegistros();
        //}
      }
      public void keyTyped(KeyEvent keyEvento) {
      	char caracter = keyEvento.getKeyChar();
      	caracter=Character.toUpperCase(caracter);
        if (cajaDirCarga.getText().length()== 4||!isHex(caracter))
 			{
     			keyEvento.consume();
			}
      }
    };


// MOUSE REGISTROS


void mouseActionRegistros(){
	for(int i=0;i<11;i++){
	contenidoRegistrosLabel[i].addMouseListener(new MouseListener()
		{	
		
	public void mouseClicked(MouseEvent arg0) {
		actualizarRegistros();
		eventoReg=(JLabel)arg0.getComponent();
		if(eventoReg==contenidoRegistrosLabel[8]||eventoReg==contenidoRegistrosLabel[9]||eventoReg==contenidoRegistrosLabel[10]){
			cajaRegistro.setBounds(eventoReg.getX()-5,eventoReg.getY()-3,44,18);
		}
		else{
			cajaRegistro.setBounds(eventoReg.getX()-5,eventoReg.getY()-3,30,18);
		}	
		eventoReg.setVisible(false);
		MostrarCajaRegistro();
	}
	public void mouseEntered(MouseEvent arg1) {
		evento1= (JLabel)arg1.getComponent();
		evento1.setForeground(Color.RED); 
		evento1.setFont(courierBOLD);
	}
	public void mouseExited(MouseEvent arg1) {
		JLabel evento1= (JLabel)arg1.getComponent();
		evento1.setForeground(Color.BLACK); 
		evento1.setFont(courier);
	}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
	});
	}
}

// CAJA REGISTROS

public void MostrarCajaRegistro(){
	cajaRegistro.setVisible(true);
	cajaRegistro.requestFocusInWindow();
	cajaRegistro.setText("");
	cajaRegistro.addKeyListener(keyListenerRegistros);
}

// KEYLISTENER CAJA REGISTROS


KeyListener keyListenerRegistros = new KeyListener() {
	public void keyPressed(KeyEvent keyEvento) {
        
      }

      public void keyReleased(KeyEvent keyEvento) {
        int key = keyEvento.getKeyCode();
        String cadena=cajaRegistro.getText();
        cajaRegistro.setText(cadena.toUpperCase());
        if(key==keyEvento.VK_ENTER)
        { 		if(eventoReg==contenidoRegistrosLabel[0]){
        			memo1.A=Integer.parseInt(cadena,16);
        		}
        		if(eventoReg==contenidoRegistrosLabel[1]){
        			memo1.F=Integer.parseInt(cadena,16);
        		}
        		if(eventoReg==contenidoRegistrosLabel[2]){
        			memo1.B=Integer.parseInt(cadena,16);
        		}
        		if(eventoReg==contenidoRegistrosLabel[3]){
        			memo1.C=Integer.parseInt(cadena,16);
        		}
        		if(eventoReg==contenidoRegistrosLabel[4]){
        			memo1.D=Integer.parseInt(cadena,16);
        		}
        		if(eventoReg==contenidoRegistrosLabel[5]){
        			memo1.E=Integer.parseInt(cadena,16);
        		}
        		if(eventoReg==contenidoRegistrosLabel[6]){
        			memo1.H=Integer.parseInt(cadena,16);
        		}
        		if(eventoReg==contenidoRegistrosLabel[7]){
        			memo1.L=Integer.parseInt(cadena,16);
        		}
        		if(eventoReg==contenidoRegistrosLabel[8]){
        			memo1.IX=Integer.parseInt(cadena,16);
        		}
        		if(eventoReg==contenidoRegistrosLabel[9]){
        			memo1.IY=Integer.parseInt(cadena,16);
        		}
        		if(eventoReg==contenidoRegistrosLabel[10]){
        			memo1.PC=Integer.parseInt(cadena,16);
        		}
					cajaRegistro.setVisible(false);
					actualizarRegistros();	
        }
      }
      public void keyTyped(KeyEvent keyEvento) {
      	char caracter = keyEvento.getKeyChar();
      	caracter=Character.toUpperCase(caracter);
      	if(eventoReg==contenidoRegistrosLabel[8]||eventoReg==contenidoRegistrosLabel[9]||eventoReg==contenidoRegistrosLabel[10]){
			if (cajaRegistro.getText().length()== 4||!isHex(caracter))
 			{
     			keyEvento.consume();
			}
		}
		else{
			if (cajaRegistro.getText().length()== 2||!isHex(caracter))
 			{
     			keyEvento.consume();
			}
		}
      }
    };

//SIGUIENTE KEY LISTENER BOTON

KeyListener keyListenerSig = new KeyListener() {
	public void keyPressed(KeyEvent keyEvento) {
        	int key = keyEvento.getKeyCode();
        	if(key==keyEvento.VK_ENTER)
        	{ 
					actualizarDesensamble();
        	}
      }

      public void keyReleased(KeyEvent keyEvento) {

      		
      }
      public void keyTyped(KeyEvent keyEvento) {
      	
      }
    };

    // Verifica RadioButtons
    
    public void opcionFrecuencia(){
    	if(frec[0].isSelected()==true)
		{
 			System.out.print("2 MHz");
 			frecuencia=2;
		}
		if(frec[1].isSelected()==true)
		{
 			System.out.print("4 MHz");
 			frecuencia=4;
		}
		if(frec[2].isSelected()==true)
		{
 			System.out.print("8 MHz");
 			frecuencia=8;
		}
		if(frec[3].isSelected()==true)
		{
 			System.out.print("12 MHz");
 			frecuencia=12;
		}
		if(frec[4].isSelected()==true)
		{
 			System.out.print("20 MHz");
 			frecuencia=20;
		}
    }

    //DEJA EN 00 los REGISTROS
 
 	public void limpiarRegistros(){
 		memo1.A=0;
 		memo1.B=0;
 		memo1.C=0;
 		memo1.D=0;
 		memo1.E=0;
 		memo1.F=0;
 		memo1.H=0;
 		memo1.L=0;
 		memo1.Ac=0;
 		memo1.Bc=0;
 		memo1.Cc=0;
 		memo1.Dc=0;
 		memo1.Ec=0;
 		memo1.Fc=0;
 		memo1.Hc=0;
 		memo1.Lc=0;
 		memo1.IX=0;
 		memo1.IY=0;
 		memo1.SP=65535;
 		memo1.IFF=1;
 		memo1.I=0;
 		memo1.R=0;
 		actualizarRegistros();
 	}
 // caja Direccion
 
 void mouseActionCajaDir(){
	
	cajaDir.addMouseListener(new MouseListener()
		{	
		
	public void mouseClicked(MouseEvent arg0) {
		cajaDir.setFocusable(true);
		cajaDir.requestFocusInWindow();
	}
	public void mouseEntered(MouseEvent arg1) {
		//cajaDir.setFocusable(true);
	}
	public void mouseExited(MouseEvent arg1) {
		cajaDir.setFocusable(true);
		cajaDir.requestFocusInWindow();
	}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
	});
	}


// Calculo de tiempo

	public float calcTiempo(){
		float tiempo=(float)memo1.T/frecuencia;
		return tiempo;
	}

// Verifica Error

	public boolean verificaError(){
		if(memo1.A>127||memo1.B>127||memo1.C>127||memo1.D>127||memo1.E>127||memo1.F>127||memo1.H>127||memo1.L>127||memo1.PilaError==true){
			return true;
		}
		else
			return false;
	}

//VERIFICA SI ES HEXADECIMAL 

    public boolean isHex(char ch) {
		return HEXADECIMAL.indexOf(ch) > -1;
	}


}






	 



