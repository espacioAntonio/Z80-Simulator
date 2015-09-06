public class Principal{
	public static void main(String args[]){
		Ventana1 ventana=new Ventana1();
		Ventana0 menu=new Ventana0();
		menu.setVisible(true);
		while(menu.simulador==0){
			ventana.setVisible(false);
		}
		menu.setVisible(false);
		ventana.setVisible(true);
	}
}