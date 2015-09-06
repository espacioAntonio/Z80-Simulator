import java.io.*;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Character;

public class Master{
    int I,R,IFF=1,CL=-1,nn,n,CY,CY2,T=0,A=0,B=0,C=0,D=0,E=0,F=0,H=0,L=0,AF,BC,DE,HL,HALT=0,Ac=0,Bc=0,Cc=0,Dc=0,Ec=0,Fc=0,Hc=0,Lc=0,IX=0,IY=0,WR=0,AFc,BCc,DEc,HLc;
    int PC=0,SP=65535,ok=1,botonOK;
    char cCY[],cCY2[],cWR[],cn[],cnn[],cIFF[],cIX[],cIY[],cinst2[];
    String sCY,sCY2,sWR,inst,inst2,sn,snn,sIX,sIY,instmin2; 
    int[] memo=new int[65536];
    boolean PilaError;

       String insta,instamin,instmin,errorPila;
       char cinst[],cinsta[];


    Scanner lectura;
    char[] caract=new char[46];
    int par,checksum,CHECKSUMv;
    String linea,linea2,INSTRUCCION,INSTRUCCIONHEX,ck;
    int tamLinea,j,intPar,stackRec=0,marcaPila=0;
    char[] aj=new char[2];
    char[] vCheck=new char[2];


// Leer Archivo


 void LeerArchivo(String x,int dirCarga){

        FileReader entrada=null;
        int contador=0,intPar=1,psc=0;
         StringBuffer str=new StringBuffer();
         try  {
            entrada=new FileReader(x);
            int c;
            while((c=entrada.read())!=-1){
                if((char)c!=':'){
                    if(contador==1){
                        str.append((char)c);
                    }
                    if(contador==2){
                        str.append((char)c);
                        tamLinea=Integer.parseInt(str.toString(),16);
                        tamLinea=tamLinea*2;
                    }
                    if(contador==8){
                        str.delete(0,3);
                    }
                    if(contador>8&&tamLinea>0){
                        //str.append((char)c);
                        if(intPar==1){
                            str.append((char)c);
                            intPar++;
                        }
                        else{
                            str.append((char)c);
                            par=Integer.parseInt(str.toString(),16);
                            memo[dirCarga+psc]=par;
                            psc++;
                            intPar=1;
                            str.delete(0,3);
                        }
                        tamLinea--;
                    }
                    contador++;
                }
                else{
                    contador=1;
                }
            }
       }catch (IOException ex) {
            System.out.println("ERROR");
       }finally{
//cerrar los flujos de datos
            if(entrada!=null){
                try{
                    entrada.close();
                }catch(IOException ex){}
            }
            System.out.println("el bloque finally siempre se ejecuta");
       }
    }

void ValidarChecksum(String x,int dirCarga){

        FileReader entrada=null;
        int contador=0,intPar=1,psc=0;
         StringBuffer str=new StringBuffer();
         try  {
            entrada=new FileReader(x);
            int c;
            CHECKSUMv=1;
            ck="vacio";
            while((c=entrada.read())!=-1){
                if((char)c!=':'){
                    if(contador==1){
                        str.append((char)c);
                    }
                    if(contador==2){
                        str.append((char)c);
                        tamLinea=Integer.parseInt(str.toString(),16);
                        tamLinea=tamLinea*2;
                        checksum=tamLinea/2;
                    }
                    if(contador>2&&contador<=8){
                        if(contador==3){
                            str.delete(0,3);
                            str.append((char)c);
                        }
                        if(contador==4){
                            str.append((char)c);
                            checksum=checksum+Integer.parseInt(str.toString(),16);
                        }
                        if(contador==5){
                            str.delete(0,3);
                            str.append((char)c);
                        }
                        if(contador==6){
                            str.append((char)c);
                            checksum=checksum+Integer.parseInt(str.toString(),16);
                        }
                        if(contador==7){
                            str.delete(0,3);
                            str.append((char)c);
                        }
                        if(contador==8){
                            str.append((char)c);
                            checksum=checksum+Integer.parseInt(str.toString(),16);
                            str.delete(0,3);
                        }
                    }
                    if(contador>8&&tamLinea>-2){
                        if(tamLinea>0){
                           if(intPar==1){
                            str.append((char)c);
                            intPar++;
                            }
                            else{
                                str.append((char)c);
                                par=Integer.parseInt(str.toString(),16);
                                checksum=checksum+par;
                                intPar=1;
                                str.delete(0,3);
                            }
                        }
                        else{
                            if(tamLinea==0){
                                str.append((char)c);
                            }
                            if(tamLinea==-1){
                                str.append((char)c);
                                ck=str.toString();
                                str.delete(0,3);
                            }
                        }
                          tamLinea--;
                    }
                    contador++;
                }
                else{
                    if(ck!="vacio"){
                        checksum=0-checksum;
                        String check=Integer.toHexString(checksum);
                        System.out.println("CHECKSUM=  "+check);
                        System.out.println("ck=  "+ck);
                        vCheck=check.toCharArray();
                        check=Character.toString(vCheck[6])+Character.toString(vCheck[7]);
                        //check=check.toUpperCase();
                        System.out.println("CHECKSUM=  "+check);
                        if(Integer.parseInt(ck,16)!=Integer.parseInt(check,16)){
                            CHECKSUMv=0;
                        }
                        
                    }
                    contador=1;
                }
            }
       }catch (IOException ex) {
            System.out.println("ERROR");
       }finally{
//cerrar los flujos de datos
            if(entrada!=null){
                try{
                    entrada.close();
                }catch(IOException ex){}
            }
            System.out.println("el bloque finally siempre se ejecuta");
       }
    }


    // Memoria 
    

    void limpiarMem(){
        for(int i=0;i<65536;i++){
        memo[i]=0;
        }
    }

    int impMemo(int x){
        return memo[x];
    }

    String impFormatMemo(int x){
        if (memo[x]<16){
            String memoryFormat=Integer.toHexString(impMemo(x));
            return ("0"+memoryFormat.toUpperCase());
        }
        else{
        String memoryFormat=Integer.toHexString(impMemo(x));
        return memoryFormat.toUpperCase();
        }
    }

    String AjusteHEX(int x){

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

    String ajusteMem(int y){

        if(y<0){
            String dirFormat=Integer.toHexString(y);
            return (dirFormat.toUpperCase());
        }
        else{
        if (y<16){
            String dirFormat=Integer.toHexString(y);
            return ("000"+dirFormat.toUpperCase());
        }
        else{
            if (y<256){
                String dirFormat=Integer.toHexString(y);
                return ("00"+dirFormat.toUpperCase());
            }
            else {
                if(y<4096){
                String dirFormat=Integer.toHexString(y);
                return ("0"+dirFormat.toUpperCase());
                }
                else{
                    String dirFormat=Integer.toHexString(y);
                    return (dirFormat.toUpperCase());
                }
            }
        }
        }
    }
    String impDir(int x){
        int y=0;
        int xx=x/16;
        while(xx>0){
            y=y+16;
            xx--; 
        }
        return ajusteMem(y);
    }
    int ajusteInt(int x){ 
        int y=0;
        int xx=x/16;
        while(xx>0){
            y=y+16;
            xx--; 
        }
        return y;
    }


// SWITCH  DESENSAMBLE  

    void obtenerInstruccion(int dirCarga){
            PC=dirCarga;
            PilaError=false;
  
              instmin=Integer.toHexString(memo[PC]);
              inst=instmin.toUpperCase();
              cinst=inst.toCharArray();
              //botonOK=0;
              while (cinst.length!=2){
              inst="0"+inst;
              cinst=inst.toCharArray();
              }
            CL++;
           switch(inst){

            case "00":  //NOP
                T=T+4;
                //(ocupa 4 tiempos de reloj)
                System.out.println("NOP");
                INSTRUCCION="NOP";
                break;

            case "01": //LD BC,nn
                PC=PC+2;
                C=memo[PC-1];
                B=memo[PC];
                INSTRUCCION="LD BC,"+AjusteHEX(memo[PC])+AjusteHEX(memo[PC-1]);
                CL=CL+2;
                T=T+10;
                break;

            case "02":  //LD (BC),A
                BC=obtenRC(B,C);
                memo[BC]=A;
                T=T+7;
                INSTRUCCION="LD (BC),A";
                break;

            case "03":  //INC BC
                BC=obtenRC(B,C);
                BC=BC+1;
                INSTRUCCION="INC BC";
                B=obtenRDH(BC);
                C=obtenRDL(BC);
                T=T+6;
                break;
                
            case "04":  //INC B
                B=B+1;
                F=grabarFlag("N",'0',F);
                WR=B;                
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);
                INSTRUCCION="INC B";
                T=T+4;
                break;
                
            case "05":  //DEC B
                B=B-1;
                F=grabarFlag("N",'1',F);
                WR=B;                
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);
                INSTRUCCION="DEC B";
                T=T+4;
                break;
                
            case "06":  //LD B,n
                PC=PC+1;
                B=memo[PC];
                INSTRUCCION="LD B,"+AjusteHEX(memo[PC]);
                CL=CL+1;
                T=T+7;
                break;

            case "07":  //RLCA
                CY=obtenFlag("S",A);
                sCY=Integer.toBinaryString(CY);
                cCY=sCY.toCharArray();
                F=grabarFlag("C",cCY[0],F);
                A=RI(A);
                F=grabarFlag("N",'0',F);
                F=grabarFlag("H",'0',F);
                INSTRUCCION="RLCA";
                T=T+4;
                break;
                
            case "08":  //EX AF,A'F'
                AF=obtenRC(A,F);
                AFc=obtenRC(Ac,Fc);
                WR=AF;
                AF=AFc;
                AFc=WR;
                A=obtenRDH(AF);
                F=obtenRDL(AF);
                Ac=obtenRDH(AF);
                Fc=obtenRDL(AF);
                INSTRUCCION="EX AF,A'F'";
                T=T+4;
                break;
                
            case "09":  //ADD HL,BC
                BC=obtenRC(B,C);
                HL=obtenRC(H,L);
                HL=HL+BC;                
                INSTRUCCION="ADD HL,BC";
                H=obtenRDH(HL);
                L=obtenRDL(HL);
                F=grabarFlag("N",'0',F);
                WR=HL;
                F=determinarFlag("C",WR,F);
                F=determinarFlag("H",WR,F);
                T=T+11;
                break;
                
            case "0A":  //LD A,(BC)
                INSTRUCCION="LD A,(BC)";
                BC=obtenRC(B,C);
                A=memo[BC];
                T=T+13;
                break;

            case "0B":  //DEC BC
                BC=obtenRC(B,C);
                BC=BC-1;
                INSTRUCCION="DEC BC";
                B=obtenRDH(BC);
                C=obtenRDL(BC);
                T=T+6;
                break;

            case "0C":  //INC C
                INSTRUCCION="INC C";
                C=C+1;
                F=grabarFlag("N",'0',F);
                WR=C;
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);
                T=T+4;
                break;
                
            case "0D":  //DEC C
                INSTRUCCION="DEC C";
                C=C-1;
                F=grabarFlag("N",'1',F);
                WR=C;                
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);
                T=T+4;
                break;
                
            case "0E":  //LD C,n
                INSTRUCCION="LD C,"+AjusteHEX(memo[PC+1]);
                PC=PC+1;
                C=memo[PC];
                CL++;
                T=T+7;
                break;

            case "0F":  //RRCA
                CY=obtenFlag("C",A);
                sCY=Integer.toBinaryString(CY);
                cCY=sCY.toCharArray();
                F=grabarFlag("C",cCY[0],F);
                A=RD(A);
                F=grabarFlag("N",'0',F);
                F=grabarFlag("H",'0',F);
                INSTRUCCION="RRCA";
                T=T+4;
                break;
                
            case "11":  //LD DE,nn
                INSTRUCCION="LD DE,"+AjusteHEX(memo[PC+2])+AjusteHEX(memo[PC+1]);
                PC=PC+2;
                E=memo[PC-1];
                D=memo[PC];
                CL=CL+2;
                T=T+10;
                break;

            case "12":  //LD (DE),A
                INSTRUCCION="LD (DE),A";
                DE=obtenRC(D,E);
                memo[DE]=A;
                T=T+7;
                break;
                
            case "13":  //INC DE
                INSTRUCCION="INC DE";
                DE=obtenRC(D,E);
                DE=DE+1;
                D=obtenRDH(DE);
                E=obtenRDL(DE);
                T=T+6;
                break;
                
            case "14":  //INC D
                INSTRUCCION="INC D";
                D=D+1;
                F=grabarFlag("N",'0',F);
                WR=D;
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);
                T=T+4;
                break;
                
            case "15":  //DEC D
                INSTRUCCION="DEC D";
                D=D-1;
                F=grabarFlag("N",'1',F);
                WR=D;
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);
                T=T+4;
                break;
                
            case "16":  //LD D,n
                INSTRUCCION="LD D,"+AjusteHEX(memo[PC+1]);
                PC=PC+1;
                D=memo[PC];
                CL++;
                T=T+7;
                break;

            case "17":  //RLA
                CY=obtenFlag("C",F);
                CY2=obtenFlag("S",A);
                sCY2=Integer.toBinaryString(CY2);
                cCY2=sCY2.toCharArray();
                F=grabarFlag("C",cCY2[0],F);
                A=RIC(A,CY);
                F=grabarFlag("N",'0',F);
                F=grabarFlag("H",'0',F);
                INSTRUCCION="RLA";
                T=T+4;
                break;
                
            case "18":  //JR eti
                PC=PC+1;
                n=memo[PC];
                if(n>127)
                 n=n-256;
                PC=PC+n;
                INSTRUCCION="JR "+AjusteHEX(n );
                CL=CL+1;
                T=T+12;
                break;

            case "19":  //ADD HL,DE
                DE=obtenRC(D,E);
                HL=obtenRC(H,L);
                HL=HL+DE;
                INSTRUCCION="ADD HL,DE";
                H=obtenRDH(HL);
                L=obtenRDL(HL);
                F=grabarFlag("N",'0',F);
                WR=HL;
                F=determinarFlag("C",WR,F);
                F=determinarFlag("H",WR,F);
                T=T+11;
                break;
                
            case "1A":  //LD A,(DE)
                INSTRUCCION="LD A,(DE)";
                DE=obtenRC(D,E);
                A=memo[DE];
                T=T+7;
                break;

            case "1B":  //DEC DE
                INSTRUCCION="DEC DE";
                DE=obtenRC(D,E);
                DE=DE-1;
                D=obtenRDH(DE);
                E=obtenRDL(DE);
                T=T+6;
                break;
                
            case "1C":  //INC E
                INSTRUCCION="INC E";
                E=E+1;
                F=grabarFlag("N",'0',F);
                WR=E;
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);
                T=T+4;
                break;
                
            case "1D":  //DEC E
                INSTRUCCION="DEC E";
                E=E-1;
                F=grabarFlag("N",'1',F);
                WR=E;
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);
                T=T+4;
                break;
                
            case "1E":  //LD E,n
                INSTRUCCION="LD E,"+AjusteHEX(memo[PC+1]);
                PC=PC+1;
                E=memo[PC];
                CL++;
                T=T+7;
                break;

            case "1F":  //RRA
                CY=obtenFlag("C",F);
                CY2=obtenFlag("C",A);
                sCY2=Integer.toBinaryString(CY2);
                cCY2=sCY2.toCharArray();
                F=grabarFlag("C",cCY2[0],F);
                A=RDC(A,CY);                
                F=grabarFlag("N",'0',F);
                F=grabarFlag("H",'0',F);
                INSTRUCCION="RRA";
                T=T+4;
                break;
                
            case "20":  //JR NZ,eti
                CY=obtenFlag("Z",F);
                PC=PC+1;
                n=memo[PC];
                if(n>127)
                 n=n-256;
                INSTRUCCION="JR NZ,"+AjusteHEX(n);
                if(CY==0){
                PC=PC+n;
                //PC--;
                T=T+5;
                }
                CL=CL+1;
                T=T+7;
                break;

            case "21":  //LD HL,nn
                INSTRUCCION="LD HL,"+AjusteHEX(memo[PC+2])+AjusteHEX(memo[PC+1]);
                PC=PC+1;
                L=memo[PC];
                PC=PC+1;
                H=memo[PC];
                CL=CL+2;
                T=T+10;
                break;

            case "22":  //LD (nn),HL
                INSTRUCCION="LD ("+AjusteHEX(memo[PC+2])+AjusteHEX(memo[PC+1])+"),HL";
                HL=obtenRC(H,L);
                PC=PC+2;
                sn=AjusteHEX(memo[PC])+AjusteHEX(memo[PC-1]);
                nn=Integer.parseInt(sn,16);
                memo[nn]=L;
                memo[nn+1]=H;
                CL=CL+2;
                T=T+16;
                break;

            case "23":  //INC HL
                INSTRUCCION="INC HL";
                HL=obtenRC(H,L);
                HL=HL+1;
                H=obtenRDH(HL);
                L=obtenRDL(HL);
                T=T+6;
                break;
                
            case "24":  //INC H
                INSTRUCCION="INC H";
                H=H+1;
                F=grabarFlag("N",'0',F);
                WR=H;
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);
                T=T+4;
                break;
                
            case "25":  //DEC H
                INSTRUCCION="DEC H";
                H=H-1;
                F=grabarFlag("N",'1',F);
                WR=H;
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);
                T=T+4;
                break;
                
            case "26":  //LD H,n
                INSTRUCCION="LD H,"+AjusteHEX(memo[PC+1]);
                PC=PC+1;
                H=memo[PC];
                CL++;
                T=T+7;
                break;

            case "27":  //DAA
                INSTRUCCION="DAA";
                aj=AjusteHEX(A).toCharArray();
                System.out.println(AjusteHEX(A));
                System.out.println("aj[1]:  "+aj[1]);
                if(!Character.isDigit(aj[1])){
                    A=A+6;
                }

                T=T+4;
                break;
                
            case "28":  //JR Z,eti
                CY=obtenFlag("Z",F);
                PC=PC+1;
                n=memo[PC];
                if(n>127)
                 n=n-256;
                INSTRUCCION="JR Z,"+AjusteHEX(n);
                if(CY==1){
                PC=PC+n;
                //PC--;
                T=T+5;
                }
                CL=CL+1;
                T=T+7;
                break;

            case "29":  //ADD HL,HL
                INSTRUCCION="ADD HL,HL";
                HL=obtenRC(H,L);
                HL=HL+HL;
                H=obtenRDH(HL);
                L=obtenRDL(HL);
                F=grabarFlag("N",'0',F);
                WR=HL;
                F=determinarFlag("C",WR,F);
                F=determinarFlag("H",WR,F);
                T=T+11;
                break;

            case "2A":  //LD HL,(nn)
                INSTRUCCION="LD HL,("+AjusteHEX(memo[PC+2])+AjusteHEX(memo[PC+1])+")";
                HL=obtenRC(H,L);
                PC=PC+2;
                sn=AjusteHEX(memo[PC])+AjusteHEX(memo[PC-1]);
                nn=Integer.parseInt(sn,16);
                L=memo[nn];
                H=memo[nn+1];
                CL=CL+2;
                T=T+16;
                break;

            case "2B":  //DEC HL
                INSTRUCCION="DEC HL";
                HL=obtenRC(H,L);
                HL=HL-1;                
                H=obtenRDH(HL);
                L=obtenRDL(HL);
                T=T+6;
                break;
                
            case "2C":  //INC L
                INSTRUCCION="INC L";
                L=L+1;
                F=grabarFlag("N",'0',F);
                WR=L;
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);
                T=T+4;
                break;
                
            case "2D":  //DEC L
                L=L-1;
                F=grabarFlag("N",'1',F);
                WR=L;
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);
                T=T+4;
                INSTRUCCION="DEC L";
                break;
                
            case "2E":  //LD L,n
                INSTRUCCION="LD L,"+AjusteHEX(memo[PC+1]);
                PC=PC+1;
                L=memo[PC];
                T=T+7;
                break;

            case "2F":  //CPL
                sWR=Integer.toBinaryString(A);
                cWR=sWR.toCharArray();
                for(int i=0;i<8;i++){
                    if(cWR[i]=='0')
                        cWR[i]='1';
                    else
                        cWR[i]='0';
                }
                sWR=String.valueOf(cWR);
                A=Integer.parseInt(sWR,2);
                F=grabarFlag("N",'1',F);
                F=grabarFlag("H",'1',F);
                INSTRUCCION="CPL";
                T=T+4;
                break;

            case "30":  //JR NC,eti
                CY=obtenFlag("C",F);
                PC=PC+1;
                n=memo[PC];
                if(n>127)
                 n=n-256;
                INSTRUCCION="JR NC,"+AjusteHEX(n);
                if(CY==0){
                PC=PC+n;
                //PC--;
                T=T+5;
                }
                CL=CL+1;
                T=T+7;
                break;

            case "31":  //SP,nn
                INSTRUCCION="LD SP,"+AjusteHEX(memo[PC+2])+AjusteHEX(memo[PC+1]);
                PC=PC+2;
                sn=AjusteHEX(memo[PC])+AjusteHEX(memo[PC-1]);
                SP=Integer.parseInt(sn,16);
                CL=CL+2;
                T=T+10;
                break;

            case "32"://LD (nn),A
                INSTRUCCION="LD ("+AjusteHEX(memo[PC+2])+AjusteHEX(memo[PC+1])+"),A";
                PC=PC+2;
                sn=AjusteHEX(memo[PC])+AjusteHEX(memo[PC-1]);
                nn=Integer.parseInt(sn,16);
                memo[nn]=A;
                CL=CL+2;
                T=T+13;
                break;

            case "33": // INC SP
                INSTRUCCION="INC SP";
                SP++;
                T=T+6;
                break;

            case "34":  //INC (HL)
                INSTRUCCION="INC (HL)";
                HL=obtenRC(H,L);
                memo[HL]=memo[HL]+1;
                F=grabarFlag("N",'0',F);
                WR=HL;
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);
                T=T+11;
                break;

            case "35":  //DEC (HL)
                INSTRUCCION="DEC (HL)";
                HL=obtenRC(H,L);
                memo[HL]=memo[HL]-1;
                F=grabarFlag("N",'1',F);
                WR=HL;
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);
                T=T+11;
                break;

            case "36":  //LD (HL),n
                INSTRUCCION="LD (HL),"+AjusteHEX(memo[PC+1]);
                HL=obtenRC(H,L);
                PC=PC+1;
                memo[HL]=memo[PC];
                CL++;
                T=T+10;
                break;

            case "37":  //SCF
                F=grabarFlag("C",'1',F);
                F=grabarFlag("N",'0',F);
                F=grabarFlag("H",'0',F);
                INSTRUCCION="SCF";
                T=T+4;
                break;
                
            case "38":  //JR C,eti
                CY=obtenFlag("C",F);
                PC=PC+1;
                n=memo[PC];
                if(n>127)
                 n=n-256;
                INSTRUCCION="JR C,"+AjusteHEX(n);
                if(CY==1){
                PC=PC+n;
                //PC--;
                T=T+5;
                }
                CL=CL+1;
                T=T+7;
                break;

            case "39":  //ADD HL,SP
                INSTRUCCION="ADD HL,SP";
                String spl=Integer.toHexString(pop());
                String sph=Integer.toHexString(pop());
                String ssp=spl+sph;
                HL=obtenRC(H,L);
                HL=Integer.parseInt(ssp)+HL;
                push(Integer.parseInt(sph));
                push(Integer.parseInt(spl));
                L=obtenRDH(HL);
                H=obtenRDL(HL);
                T=T+11;
                break;
                
            case "3A":  //LD A,(nn)
                INSTRUCCION="LD A,("+AjusteHEX(memo[PC+2])+AjusteHEX(memo[PC+1])+")";
                PC=PC+2;
                sn=AjusteHEX(memo[PC])+AjusteHEX(memo[PC-1]);
                nn=Integer.parseInt(sn,16);
                A=memo[nn];
                CL=CL+2;
                T=T+13;
                break;

            case "3B":  //DEC SP
                INSTRUCCION="DEC SP";
                SP=SP-1;
                T=T+6;
                break;
                
            case "3C":      //INC A
                A=A+1;
                F=grabarFlag("N",'0',F);
                WR=A;
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);
                T=T+4;
                INSTRUCCION="INC A";
                break;
                
            case "3D":  //DEC A
                INSTRUCCION="DEC A";
                A=A-1;
                F=grabarFlag("N",'1',F);
                WR=A;
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);
                T=T+4;
                INSTRUCCION="DEC A";
                break;
                
            case "3E":  //LD A,n
                PC=PC+1;
                A=memo[PC];
                INSTRUCCION="LD A,"+AjusteHEX(memo[PC]);
                CL++;
                T=T+7;
                break;

            case "3F":  //CCF
                CY=obtenFlag("C",F);
                if(CY==0)
                F=grabarFlag("C",'1',F);
                else
                F=grabarFlag("C",'0',F);
                F=grabarFlag("N",'0',F);
                INSTRUCCION="CCF";
                T=T+4;
                break;
                
            case "40":  //LD B,B
                B=B+0;
                T=T+4;
                INSTRUCCION="LD B,B";
                break;
                
            case "41":  //LD B,C
                B=C;
                T=T+4;
                INSTRUCCION="LD B,C";
                break;
                
            case "42":  //LD B,D
                B=D;
                T=T+4;
                INSTRUCCION="LD B,D";
                break;
                
            case "43":  //LD B,E
                B=E;
                T=T+4;
                INSTRUCCION="LD B,E";
                break;
                
            case "44":  //LD B,H
                B=H;
                T=T+4;
                INSTRUCCION="LD B,H";
                break;
                
            case "45":  //LD B,L
                B=L;
                T=T+4;
                INSTRUCCION="LD B,L";
                break;
                
            case "46":  //LD B,(HL)
                HL=obtenRC(H,L);
                B=memo[HL];
                T=T+7;
                INSTRUCCION="LD B,(HL)";
                break;
                
            case "47":  //LD B,A
                B=A;
                T=T+4;
                INSTRUCCION="LD B,A";
                break;
                
            case "48":  //LD C,B
                C=B;
                T=T+4;
                INSTRUCCION="LD C,B";
                break;
                
            case "49":  //LD C,C
                C=C+0;
                T=T+4;
                INSTRUCCION="LD C,C";
                break;
                
            case "4A":  //LD C,D
                C=D;
                T=T+4;
                INSTRUCCION="LD C,D";
                break;
                
            case "4B":     //LD C,E
                C=E;
                T=T+4;
                INSTRUCCION="LD C,E";
                break;
                
            case "4C":
                C=H;
                T=T+4;
                INSTRUCCION="LD C,H";
                break;
                
            case "4D":
                C=L;
                T=T+4;
                INSTRUCCION="LD C,L";
                break;
                
            case "4E":
                HL=obtenRC(H,L);
                C=memo[HL];
                T=T+7;
                INSTRUCCION="LD C,(HL)";
                break;

            case "4F":
                INSTRUCCION="LD C,A";
                C=A;
                T=T+4;
                break;
                
            case "50":
                INSTRUCCION="LD D,B";
                D=B;
                T=T+4;
                break;
                
            case "51":
                INSTRUCCION="LD D,C";
                D=C;
                T=T+4;
                break;
                
            case "52":
                INSTRUCCION="LD D,D";
                D=D+0;
                T=T+4;
                break;
                
            case "53":
                D=E;
                T=T+4;
                INSTRUCCION="LD D,E";
                break;
                
            case "54":
                D=H;
                T=T+4;
                INSTRUCCION="LD D,H";
                break;
                
            case "55":
                D=L;
                T=T+4;
                INSTRUCCION="LD D,L";
                break;
                
            case "56":                
                HL=obtenRC(H,L);
                D=memo[HL];
                T=T+7;
                INSTRUCCION="LD D,(HL)";
                break;
                
            case "57":
                D=A;
                T=T+4;
                INSTRUCCION="LD D,A";
                break;
                
            case "58":
                E=B;
                T=T+4;
                INSTRUCCION="LD E,B";
                break;
                
            case "59":
                E=C;
                T=T+4;
                INSTRUCCION="LD E,C";
                break;
                
            case "5A":
                E=D;
                T=T+4;
                INSTRUCCION="LD E,D";
                break;
                
            case "5B":
                E=E+0;
                T=T+4;
                INSTRUCCION="LD E,E";
                break;
                
            case "5C":
                E=H;
                T=T+4;
                INSTRUCCION="LD E,H";
                break;
                
            case "5D":
                E=L;
                T=T+4;
                INSTRUCCION="LD E,L";
                break;
                
            case "5E":
                HL=obtenRC(H,L);
                E=memo[HL];
                T=T+7;
                INSTRUCCION="LD E,(HL)";
                break;
                
            case "5F":
                E=A;
                T=T+4;
                INSTRUCCION="LD E,A";
                break;
                
            case "60":
                H=B;
                T=T+4;
                INSTRUCCION="LD H,B";
                break;
                
            case "61":
                INSTRUCCION="LD H,C";
                H=C;
                T=T+4;
                break;
                
            case "62":
                INSTRUCCION="LD H,D";
                H=D;
                T=T+4;
                break;
                
            case "63":
                INSTRUCCION="LD H,E";
                H=E;
                T=T+4;
                break;
                
            case "64":
                INSTRUCCION="LD H,H";
                H=H+0;
                T=T+4;
                break;
                
            case "65":
                INSTRUCCION="LD H,L";
                H=L;
                T=T+4;
                break;
                
            case "66":
                INSTRUCCION="LD H,(HL)";
                HL=obtenRC(H,L);
                H=memo[HL];
                T=T+7;
                break;
                
            case "67":
                INSTRUCCION="LD H,A";
                H=A;
                T=T+4;
                break;
                
            case "68":
                L=B;
                T=T+4;
                INSTRUCCION="LD L,B";
                break;
                
            case "69":
                L=C;
                T=T+4;
                INSTRUCCION="LD L,C";
                break;
                
            case "6A":
                L=D;
                T=T+4;
                INSTRUCCION="LD L,D";
                break;
                
            case "6B":
                L=E;
                T=T+4;
                INSTRUCCION="LD L,E";
                break;
                
            case "6C":
                L=H;
                T=T+4;
                INSTRUCCION="LD L,H";
                break;
                
            case "6D":
                L=L+0;
                T=T+4;
                INSTRUCCION="LD L,L";
                break;
                
            case "6E":
                HL=obtenRC(H,L);
                L=memo[HL];
                T=T+7;
                INSTRUCCION="LD L,(HL)";
                break;
                
            case "6F":
                L=A;
                T=T+4;
                INSTRUCCION="LD L,A";
                break;
                
            case "70":
                HL=obtenRC(H,L);
                memo[HL]=B;
                T=T+7;
                INSTRUCCION="LD (HL),B";
                break;
                
            case "71":
                HL=obtenRC(H,L);
                memo[HL]=C;
                T=T+7;
                INSTRUCCION="LD (HL),C";
                break;
                
            case "72":
                HL=obtenRC(H,L);
                memo[HL]=D;
                T=T+7;
                INSTRUCCION="LD (HL),D";
                break;
                
            case "73":
                HL=obtenRC(H,L);
                memo[HL]=E;
                T=T+7;
                INSTRUCCION="LD (HL),E";
                break;
                
            case "74":
                HL=obtenRC(H,L);
                memo[HL]=H;
                T=T+7;
                INSTRUCCION="LD (HL),H";
                break;
                
            case "75":
                HL=obtenRC(H,L);
                memo[HL]=L;
                T=T+7;
                INSTRUCCION="LD (HL),L";
                break;
                
            case "76":  //HALT
                HALT=1;
                T=T+4;
                INSTRUCCION="HALT";
                break;
                
            case "77":  //LD (HL),A
                HL=obtenRC(H,L);
                memo[HL]=A;
                T=T+7;
                INSTRUCCION="LD (HL),A";
                break;
                
            case "78":
                A=B;
                T=T+4;
                INSTRUCCION="LD A,B";
                break;
                
            case "79":
                A=C;
                T=T+4;
                INSTRUCCION="LD A,C";
                break;
                
            case "7A":
                A=D;
                T=T+4;
                INSTRUCCION="LD A,D";
                break;
                
            case "7B":
                A=E;
                T=T+4;
                INSTRUCCION="LD A,E";
                break;
                
            case "7C":
                A=H;
                T=T+4;
                INSTRUCCION="LD A,H";
                break;
                
            case "7D":
                A=L;
                T=T+4;
                INSTRUCCION="LD A,L";
                break;
                
            case "7E":
                HL=obtenRC(H,L);
                A=memo[HL];
                T=T+7;
                INSTRUCCION="LD A,(HL)";
                break;
                
            case "7F":
                A=A+0;
                T=T+4;
                INSTRUCCION="LD A,A";
                break;
                
            case "80":  //ADD B
                INSTRUCCION="ADD B";
                A=A+B;
                F=grabarFlag("N",'0',F);
                WR=A;
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+4;
                break;
                
            case "81":  //ADD C
                A=A+C;
                F=grabarFlag("N",'0',F);
                WR=A;
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+4;
                INSTRUCCION="ADD C";
                break;
                
            case "82":  //ADD D
                A=A+D;
                F=grabarFlag("N",'0',F);
                WR=A;
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+4;
                INSTRUCCION="ADD D";
                break;
                
            case "83":  //ADD E
                A=A+E;
                F=grabarFlag("N",'0',F);
                WR=A;
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+4;
                INSTRUCCION="ADD E";
                break;
                
            case "84":  //ADD H
                A=A+H;
                F=grabarFlag("N",'0',F);
                WR=A;
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+4;
                INSTRUCCION="ADD H";
                break;
                
            case "85":  //ADD L
                A=A+L;
                F=grabarFlag("N",'0',F);
                WR=A;
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+4;
                INSTRUCCION="ADD L";
                break;
                
            case "86":  //ADD (HL)
                HL=obtenRC(H,L);
                INSTRUCCION="ADD (HL)";
                A=A+memo[HL];
                F=grabarFlag("N",'0',F);
                WR=A;
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+7;
                break;
                
            case "87":  //ADD A
                A=A+A;
                F=grabarFlag("N",'0',F);
                WR=A;
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+4;
                INSTRUCCION="ADD A";
                break;
                
            case "88":  //ADC B
                CY=obtenFlag("C",F);
                A=A+B+CY;
                F=grabarFlag("N",'0',F);
                WR=A;
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+4;
                INSTRUCCION="ADC B";
                break;
                
            case "89":  //ADC C
                CY=obtenFlag("C",F);
                A=A+C+CY;
                F=grabarFlag("N",'0',F);
                WR=A;
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+4;
                INSTRUCCION="ADC C";
                break;
                
            case "8A":  //ADC D
                CY=obtenFlag("C",F);
                A=A+D+CY;
                F=grabarFlag("N",'0',F);
                WR=A;
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+4;
                INSTRUCCION="ADC D";
                break;
                
            case "8B":  //ADC E
                CY=obtenFlag("C",F);
                A=A+E+CY;
                F=grabarFlag("N",'0',F);
                WR=A;
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+4;
                INSTRUCCION="ADC E";
                break;
                
            case "8C":  //ADC H
                CY=obtenFlag("C",F);
                A=A+H+CY;
                F=grabarFlag("N",'0',F);
                WR=A;
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+4;
                INSTRUCCION="ADC H";
                break;
                
            case "8D":  //ADC L
                CY=obtenFlag("C",F);
                A=A+L+CY;
                F=grabarFlag("N",'0',F);
                WR=A;
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+4;
                INSTRUCCION="ADC L";
                break;
                
            case "8E":  //ADC (HL)
                HL=obtenRC(H,L);
                CY=obtenFlag("C",F);
                A=A+memo[HL]+CY;
                F=grabarFlag("N",'0',F);
                WR=A;
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+7;
                INSTRUCCION="ADC (HL)";
                break;
                
            case "8F":  //ADC A
                CY=obtenFlag("C",F);
                A=A+A+CY;
                F=grabarFlag("N",'0',F);
                WR=A;
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+4;
                INSTRUCCION="ADC A";
                break;
                
            case "90":
                INSTRUCCION="SUB B";
                A=A-B;
                F=grabarFlag("N",'1',F);
                WR=A;
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+4;
                break;
                
            case "91":
                INSTRUCCION="SUB C";
                A=A-C;
                F=grabarFlag("N",'1',F);
                WR=A;
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+4;
                break;
                
            case "92":  //SUB D
                A=A-D;
                F=grabarFlag("N",'1',F);
                WR=A;
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+4;
                INSTRUCCION="SUB D";
                break;
                
            case "93":
                INSTRUCCION="SUB E";
                A=A-E;
                F=grabarFlag("N",'1',F);
                WR=A;
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+4;
                break;
                
            case "94":  //SUB H
                A=A-H;
                F=grabarFlag("N",'1',F);
                WR=A;
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+4;
                INSTRUCCION="SUB H";
                break;
                
            case "95":  //SUB L
                A=A-L;
                F=grabarFlag("N",'1',F);
                WR=A;
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+4;
                INSTRUCCION="SUB L";
                break;
                
            case "96": // SUB (HL)
                HL=obtenRC(H,L);
                A=A-HL;
                F=grabarFlag("N",'1',F);
                WR=A;
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+7;
                INSTRUCCION="SUB (HL)";
                break;
                
            case "97":
                INSTRUCCION="SUB A";
                A=A-A;
                F=grabarFlag("N",'1',F);
                WR=A;
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+4;
                break;
                
            case "98":
                CY=obtenFlag("C",F);
                A=A-B-CY;
                F=grabarFlag("N",'1',F);
                WR=A;
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+4;
                INSTRUCCION="SBC B";
                break;
                
            case "99":
                CY=obtenFlag("C",F);
                A=A-C-CY;
                F=grabarFlag("N",'1',F);
                WR=A;
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+4;
                INSTRUCCION="SBC C";
                break;
                
            case "9A":
                CY=obtenFlag("C",F);
                A=A-D-CY;
                F=grabarFlag("N",'1',F);
                WR=A;
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+4;
                INSTRUCCION="SBC D";
                break;
                
            case "9B":
                CY=obtenFlag("C",F);
                A=A-E-CY;
                F=grabarFlag("N",'1',F);
                WR=A;
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+4;
                INSTRUCCION="SBC E";
                break;
                
            case "9C":
                CY=obtenFlag("C",F);
                A=A-H-CY;
                F=grabarFlag("N",'1',F);
                WR=A;
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+4;
                INSTRUCCION="SBC H";
                break;
                
            case "9D":
                CY=obtenFlag("C",F);
                A=A-L-CY;
                F=grabarFlag("N",'1',F);
                WR=A;
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+4;
                INSTRUCCION="SBC L";
                break;
                
            case "9E":
                HL=obtenRC(H,L);
                CY=obtenFlag("C",F);
                A=A-memo[HL]-CY;
                F=grabarFlag("N",'1',F);
                WR=A;
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+7;
                INSTRUCCION="SBC (HL)";
                break;
                
            case "9F":
                CY=obtenFlag("C",F);
                A=A-A-CY;
                F=grabarFlag("N",'1',F);
                WR=A;
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+4;
                INSTRUCCION="SBC A";
                break;
                
            case "A0": // AND B
                A=AND(A,B);
                WR=A;
                F=grabarFlag("C",'0',F);
                F=grabarFlag("N",'0',F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("P",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("H",WR,F);
                T=T+4;
                INSTRUCCION="AND B";
                break;
                
            case "A1":
                A=AND(A,C);
                WR=A;
                F=grabarFlag("C",'0',F);
                F=grabarFlag("N",'0',F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("P",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("H",WR,F);
                T=T+4;
                INSTRUCCION="AND C";
                break;
                
            case "A2":
                A=AND(A,D);
                WR=A;
                F=grabarFlag("C",'0',F);
                F=grabarFlag("N",'0',F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("P",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("H",WR,F);
                T=T+4;
                INSTRUCCION="AND D";
                break;
                
            case "A3":
                A=AND(A,E);
                WR=A;
                F=grabarFlag("C",'0',F);
                F=grabarFlag("N",'0',F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("P",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("H",WR,F);
                T=T+4;
                INSTRUCCION="AND E";
                break;
                
            case "A4":
                A=AND(A,H);
                WR=A;
                F=grabarFlag("C",'0',F);
                F=grabarFlag("N",'0',F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("P",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("H",WR,F);
                T=T+4;
                INSTRUCCION="AND H";
                break;
                
            case "A5":
                A=AND(A,L);
                WR=A;
                F=grabarFlag("C",'0',F);
                F=grabarFlag("N",'0',F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("P",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("H",WR,F);
                T=T+4;
                INSTRUCCION="AND L";
                break;
                
            case "A6":
                HL=obtenRC(H,L);
                A=AND(A,memo[HL]);
                WR=A;
                F=grabarFlag("C",'0',F);
                F=grabarFlag("N",'0',F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("P",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("H",WR,F);
                T=T+7;
                INSTRUCCION="AND (HL)";
                break;
                
            case "A7":
                A=AND(A,A);
                WR=A;
                F=grabarFlag("C",'0',F);
                F=grabarFlag("N",'0',F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("P",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("H",WR,F);
                T=T+4;
                INSTRUCCION="AND A";
                break;
                
            case "A8":
                A=XOR(A,B);
                WR=A;
                F=grabarFlag("C",'0',F);
                F=grabarFlag("N",'0',F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("P",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("H",WR,F);
                T=T+4;
                INSTRUCCION="XOR B";
                break;
                
            case "A9":
                A=XOR(A,C);
                WR=A;
                F=grabarFlag("C",'0',F);
                F=grabarFlag("N",'0',F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("P",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("H",WR,F);
                T=T+4;
                INSTRUCCION="XOR C";
                break;
                
            case "AA":
                A=XOR(A,D);
                WR=A;
                F=grabarFlag("C",'0',F);
                F=grabarFlag("N",'0',F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("P",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("H",WR,F);
                T=T+4;
                INSTRUCCION="XOR D";
                break;
                
            case "AB":
                A=XOR(A,E);
                WR=A;
                F=grabarFlag("C",'0',F);
                F=grabarFlag("N",'0',F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("P",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("H",WR,F);
                T=T+4;
                INSTRUCCION="XOR E";
                break;
                
            case "AC":
                A=XOR(A,H);
                WR=A;
                F=grabarFlag("C",'0',F);
                F=grabarFlag("N",'0',F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("P",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("H",WR,F);
                T=T+4;
                INSTRUCCION="XOR H";
                break;
                
            case "AD":
                A=XOR(A,L);
                WR=A;
                F=grabarFlag("C",'0',F);
                F=grabarFlag("N",'0',F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("P",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("H",WR,F);
                T=T+4;
                INSTRUCCION="XOR L";
                break;
                
            case "AE":
                HL=obtenRC(H,L);
                A=XOR(A,memo[HL]);
                WR=A;
                F=grabarFlag("C",'0',F);
                F=grabarFlag("N",'0',F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("P",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("H",WR,F);
                T=T+7;
                INSTRUCCION="XOR (HL)"; 
                break;
                
            case "AF":
                A=XOR(A,A);
                WR=A;
                F=grabarFlag("C",'0',F);
                F=grabarFlag("N",'0',F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("P",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("H",WR,F);
                T=T+4;
                INSTRUCCION="XOR A";
                break;
                
            case "B0":
                A=OR(A,B);
                WR=A;
                F=grabarFlag("C",'0',F);
                F=grabarFlag("N",'0',F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("P",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("H",WR,F);
                T=T+4;
                INSTRUCCION="OR B";
                break;
                
            case "B1":
                A=OR(A,C);
                WR=A;
                F=grabarFlag("C",'0',F);
                F=grabarFlag("N",'0',F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("P",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("H",WR,F);
                T=T+4;
                INSTRUCCION="OR C";
                break;
                
            case "B2":
                A=OR(A,D);
                WR=A;
                F=grabarFlag("C",'0',F);
                F=grabarFlag("N",'0',F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("P",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("H",WR,F);
                T=T+4;
                INSTRUCCION="OR D";
                break;
                
            case "B3":
                A=OR(A,E);
                WR=A;
                F=grabarFlag("C",'0',F);
                F=grabarFlag("N",'0',F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("P",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("H",WR,F);
                T=T+4;
                INSTRUCCION="OR E";
                break;
                
            case "B4":
                A=OR(A,H);
                WR=A;
                F=grabarFlag("C",'0',F);
                F=grabarFlag("N",'0',F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("P",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("H",WR,F);
                T=T+4;
                INSTRUCCION="OR H";
                break;
                
            case "B5":
                A=OR(A,L);
                WR=A;
                F=grabarFlag("C",'0',F);
                F=grabarFlag("N",'0',F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("P",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("H",WR,F);
                T=T+4;
                INSTRUCCION="OR L";
                break;
                
            case "B6":
                HL=obtenRC(H,L);
                A=OR(A,memo[HL]);
                WR=A;
                F=grabarFlag("C",'0',F);
                F=grabarFlag("N",'0',F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("P",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("H",WR,F);
                T=T+7;
                INSTRUCCION="OR (HL)";
                break;
                
            case "B7":
                A=OR(A,A);
                WR=A;
                F=grabarFlag("C",'0',F);
                F=grabarFlag("N",'0',F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("P",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("H",WR,F);
                T=T+4;
                INSTRUCCION="OR A";
                break;
                
            case "B8":
                WR=A-B;
                F=grabarFlag("N",'1',F);
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+4;                
                INSTRUCCION="CP B";
                break;
                
            case "B9":
                WR=A-C;
                F=grabarFlag("N",'1',F);
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+4;                
                INSTRUCCION="CP C";
                break;
                
            case "BA":
                WR=A-D;
                F=grabarFlag("N",'1',F);
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+4;                
                INSTRUCCION="CP D";
                break;
                
            case "BB":
                WR=A-E;
                F=grabarFlag("N",'1',F);
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+4;                
                INSTRUCCION="CP E";
                break;
                
            case "BC":
                WR=A-H;
                F=grabarFlag("N",'1',F);
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+4;                
                INSTRUCCION="CP H";
                break;
                
            case "BD":
                WR=A-L;
                F=grabarFlag("N",'1',F);
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+4;                
                INSTRUCCION="CP L";
                break;
                
            case "BE":
                HL=obtenRC(H,L);
                WR=A-memo[HL];
                F=grabarFlag("N",'1',F);
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+7;                
                INSTRUCCION="CP (HL)";
                break;
                
            case "BF":
                WR=A-A;
                F=grabarFlag("N",'1',F);
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);                
                T=T+4;                
                INSTRUCCION="CP A";
                break;
                
            case "C0":  //RET NZ
                INSTRUCCION="RET NZ";
                if(verificaPilaPOP()==true){
                CY=obtenFlag("Z",F);
                if(CY==0){
                CY=pop();
                WR=pop();
                PC=obtenRC(WR,CY);
                PC--;
                T=T+6;
                }
                T=T+5;  
                }   
                else
                    PilaError=true;           
                break;
                
            case "C1":  //POP BC
                INSTRUCCION="POP BC";
                if(verificaPilaPOP()==true){
                C=pop();
                B=pop();
                T=T+10;}
                else
                    PilaError=true;
                break;
                
            case "C2":  //JP NZ,nn
                PC=PC+2;
                sn=AjusteHEX(memo[PC])+AjusteHEX(memo[PC-1]);
                nn=Integer.parseInt(sn,16);
                CY=obtenFlag("Z",F);
                INSTRUCCION="JP NZ,"+ajusteMem(nn);
                if(CY==0){
                PC=nn-1;
                }
                CL=CL+2;
                T=T+10;
                break;

            case "C3":  //JP nn
                PC=PC+2;
                sn=AjusteHEX(memo[PC])+AjusteHEX(memo[PC-1]);
                nn=Integer.parseInt(sn,16);
                PC=nn-1;
                INSTRUCCION="JP "+ajusteMem(nn);
                CL=CL+2;
                T=T+10;
                break;

            case "C4":  //CALL NZ,nn 
                PC=PC+2;
                sn=AjusteHEX(memo[PC])+AjusteHEX(memo[PC-1]);
                nn=Integer.parseInt(sn,16);
                INSTRUCCION="CALL NZ,"+ajusteMem(nn);
                CY=obtenFlag("Z",F);
                if(CY==0){
                WR=obtenRDH(PC+1);
                push(WR);
                CY=obtenRDL(PC+1);
                push(CY);
                PC=nn-1;
                }
                T=T+17;
                break;

            case "C5":  //PUSH BC
                if(verificaPilaPUSH()==true){
                push(B);
                push(C);
                INSTRUCCION="PUSH BC";
                stackRec=stackRec+2;
                T=T+11;}
                else
                    PilaError=true;   
                break;
                
            case "C6":  //ADD n
                PC=PC+1;
                A=A+memo[PC];
                F=determinarFlag("C",A,F);
                F=determinarFlag("Z",A,F);
                F=determinarFlag("P/V",A,F);
                F=determinarFlag("S",A,F);
                F=determinarFlag("H",A,F);
                F=grabarFlag("N",'0',F);
                INSTRUCCION="ADD "+AjusteHEX(memo[PC]);
                CL++;
                T=T+7;
                break;

            case "C7":  //RST 00
                INSTRUCCION="RST 00";
                if(verificaPilaPUSH()==true){
                WR=obtenRDH(PC+1);
                push(WR);
                CY=obtenRDL(PC+1);
                push(CY);
                PC=-1;
                T=T+11;}
                else
                    PilaError=true;   
                break;
                
            case "C8":  //RET Z
                INSTRUCCION="RET Z";
                if(verificaPilaPOP()==true){
                CY=obtenFlag("Z",F);
                if(CY==1){
                CY=pop();
                WR=pop();
                PC=obtenRC(WR,CY);
                PC--;
                T=T+6;
                }
                T=T+5;  } 
                else
                    PilaError=true;                
                break;
                
            case "C9":  //RET
                INSTRUCCION="RET";
                if(verificaPilaPOP()==true){
                CY=pop();
                WR=pop();
                PC=obtenRC(WR,CY);
                PC--;
                T=T+10;  } 
                else
                    PilaError=true;                                
                break;
                
           case "CA":   // JP Z,nn
                INSTRUCCION="JP Z,"+AjusteHEX(memo[PC+2])+AjusteHEX(memo[PC+1]); 
                PC=PC+2;
                sn=AjusteHEX(memo[PC])+AjusteHEX(memo[PC-1]);
                nn=Integer.parseInt(sn,16);
                CY=obtenFlag("Z",F);
                if(CY==1){
                PC=nn-1;
                }
                CL=CL+2;
                T=T+10;
                break;

            case "CC":  //CALL Z,dirc
                PC=PC+2;
                sn=AjusteHEX(memo[PC])+AjusteHEX(memo[PC-1]);
                nn=Integer.parseInt(sn,16);
                INSTRUCCION="CALL Z,"+nn;
                CY=obtenFlag("Z",F);
                if(verificaPilaPUSH()==true){
                if(CY==1){
                WR=obtenRDH(PC+1);
                push(WR);
                CY=obtenRDL(PC+1);
                push(CY);
                PC=nn-1;
                }
                T=T+17;}
                else
                    PilaError=true;   
                break;

            case "CD":  //CALL
                PC=PC+2;
                sn=AjusteHEX(memo[PC])+AjusteHEX(memo[PC-1]);
                nn=Integer.parseInt(sn,16);
                INSTRUCCION="CALL "+nn;
                if(verificaPilaPUSH()==true){
                WR=obtenRDH(PC+1);
                push(WR);
                CY=obtenRDL(PC+1);
                push(CY);
                PC=nn-1;
                T=T+17; }
                else
                    PilaError=true;   
                break;

            case "CE":  //ADC n
                INSTRUCCION="ADC "+AjusteHEX(memo[PC+1]);
                CY=obtenFlag("C",F);
                PC=PC+1;
                A=A+memo[PC]+CY;
                F=determinarFlag("C",A,F);
                F=determinarFlag("Z",A,F);
                F=determinarFlag("P/V",A,F);
                F=determinarFlag("S",A,F);
                F=determinarFlag("H",A,F);
                F=grabarFlag("N",'0',F);
                CL++;
                T=T+7;
                break;

            case "CF":      //RST 08
                INSTRUCCION="RST 08";
                if(verificaPilaPUSH()==true){
                WR=obtenRDH(PC+1);
                push(WR);
                CY=obtenRDL(PC+1);
                push(CY);
                SP=SP-2;
                PC=7;
                T=T+11; }
                else
                    PilaError=true;   
                break;
                
            case "D0":  //RET NC
                INSTRUCCION="RET NC";
                if(verificaPilaPOP()==true){
                CY=obtenFlag("C",F);
                if(CY==0){
                CY=pop();
                WR=pop();
                PC=obtenRC(WR,CY);
                PC--;
                T=T+6;
                }
                T=T+5;     }    
                else
                    PilaError=true;                          
                break;
                
            case "D1":  //POP DE
                INSTRUCCION="POP DE";
                if(verificaPilaPOP()==true){
                E=pop();
                D=pop();
                T=T+10;}
                else
                    PilaError=true;   
                break;
                
            case "D2":  //JP NC,nn
                INSTRUCCION="JP NC,"+AjusteHEX(memo[PC+2])+AjusteHEX(memo[PC+1]); 
                PC=PC+2;
                sn=AjusteHEX(memo[PC])+AjusteHEX(memo[PC-1]);
                nn=Integer.parseInt(sn,16);
                CY=obtenFlag("C",F);
                if(CY==0){
                PC=nn-1;
                }
                CL=CL+2;
                T=T+10;
                break;

            case "D3":  //OUT (np),A
                T=T+11;
                break;
                
            case "D4":  //CALL NC,dirc
                PC=PC+2;
                sn=AjusteHEX(memo[PC])+AjusteHEX(memo[PC-1]);
                nn=Integer.parseInt(sn,16);
                INSTRUCCION="CALL NC,"+nn;
                if(verificaPilaPUSH()==true){
                CY=obtenFlag("C",F);
                if(CY==0){
                WR=obtenRDH(PC+1);
                push(WR);
                CY=obtenRDL(PC+1);
                push(CY);
                PC=nn-1;
                }
                T=T+17; }
                else
                    PilaError=true;   
                break;

            case "D5":  //PUSH DE 
                if(verificaPilaPUSH()==true){
                push(D);
                push(E);
                INSTRUCCION="PUSH DE";
                stackRec=stackRec+2;
                T=T+11; }
                else
                    PilaError=true;   
                break;
                
            case "D6":  //SUB n
                INSTRUCCION="SUB "+AjusteHEX(memo[PC+1]);
                PC=PC+1;
                A=A-memo[PC];
                F=determinarFlag("C",A,F);
                F=determinarFlag("Z",A,F);
                F=determinarFlag("P/V",A,F);
                F=determinarFlag("S",A,F);
                F=determinarFlag("H",A,F);
                F=grabarFlag("N",'1',F);                
                CL++;
                T=T+7;
                break;

            case "D7":  //RST 10
                INSTRUCCION="RST 10";
                if(verificaPilaPUSH()==true){
                WR=obtenRDH(PC+1);
                push(WR);
                CY=obtenRDL(PC+1);
                push(CY);
                PC=15;
                T=T+11; }
                else
                    PilaError=true;   
                break;
                
            case "D8":  //RET C *
                INSTRUCCION="RET C";
                if(verificaPilaPOP()==true){
                CY=obtenFlag("C",F);
                if(CY==1){
                CY=pop();
                WR=pop();
                PC=obtenRC(WR,CY);
                PC--;
                T=T+6;
                }
                T=T+5;   } 
                else
                    PilaError=true;                               
                break;
                
            case "D9":  //EXX
               BC=obtenRC(B,C); 
               BCc=obtenRC(Bc,Cc); 
               WR=BC;
               BC=BCc;
               BCc=WR;
               DE=obtenRC(D,E); 
               DEc=obtenRC(Dc,Ec); 
               WR=DE;
               DE=DEc;
               DEc=WR;
               HL=obtenRC(H,L); 
               HLc=obtenRC(Hc,Lc); 
               WR=HL;
               HL=HLc;
               HLc=WR;
               B=obtenRDH(BC);
               C=obtenRDL(BC);
               D=obtenRDH(DE);
               E=obtenRDL(DE);
               H=obtenRDH(HL);
               L=obtenRDL(HL);
               Bc=obtenRDH(BCc);
               Cc=obtenRDL(BCc);
               Dc=obtenRDH(DEc);
               Ec=obtenRDL(DEc);
               Hc=obtenRDH(HLc);
               Lc=obtenRDL(HLc);
               T=T+4;
                INSTRUCCION="EXX";
                break;
                
            case "DA":  //JP C,nn
                INSTRUCCION="JP C,"+AjusteHEX(memo[PC+2])+AjusteHEX(memo[PC+1]); 
                PC=PC+2;
                sn=AjusteHEX(memo[PC])+AjusteHEX(memo[PC-1]);
                nn=Integer.parseInt(sn,16);
                CY=obtenFlag("C",F);
                if(CY==1){
                PC=nn-1;
                }
                CL=CL+2;
                T=T+10;
                break;

            case "DB":
              INSTRUCCION="IN A,(np)";
                T=T+10;
                break;
                
            case "DC":  //INSTRUCCION="CALL C,nn";
                PC=PC+2;
                sn=AjusteHEX(memo[PC])+AjusteHEX(memo[PC-1]);
                nn=Integer.parseInt(sn,16);
                INSTRUCCION="CALL C,"+nn;
                if(verificaPilaPUSH()==true){
                CY=obtenFlag("C",F);
                if(CY==1){
                WR=obtenRDH(PC+1);
                push(WR);
                CY=obtenRDL(PC+1);
                push(CY);
                PC=nn-1;
                }
                T=T+17; }
                else
                    PilaError=true;   
                break;

            case "DE":
                INSTRUCCION="SBC n";
                CY=obtenFlag("C",F);
                PC=PC+1;
                A=A-memo[PC]-CY;
                F=determinarFlag("C",A,F);
                F=determinarFlag("Z",A,F);
                F=determinarFlag("P/V",A,F);
                F=determinarFlag("S",A,F);
                F=determinarFlag("H",A,F);
                F=grabarFlag("N",'0',F);
                CL++;
                T=T+7;
                break;

            case "DF":  //RST 18
                INSTRUCCION="RST 18";
                if(verificaPilaPUSH()==true){
                WR=obtenRDH(PC+1);
                push(WR);
                CY=obtenRDL(PC+1);
                push(CY);
                PC=23;
                T=T+11; }
                else
                    PilaError=true;   
                break;
                
            case "E0":  //RET PO
                INSTRUCCION="RET PO";
                if(verificaPilaPOP()==true){
                CY=obtenFlag("P",F);
                if(CY==0){
                CY=pop();
                WR=pop();
                PC=obtenRC(WR,CY);
                PC--;
                T=T+6;
                }
                T=T+5;  }   
                else
                    PilaError=true;                              
                break;
                
            case "E1":  //POP HL
                INSTRUCCION="POP HL";
                if(verificaPilaPOP()==true){
                L=pop();
                H=pop();
                T=T+10;}
                else
                    PilaError=true;   
                break;
                
            case "E2":  //JP PO,nn
                INSTRUCCION="JP PO,"+AjusteHEX(memo[PC+2])+AjusteHEX(memo[PC+1]); 
                PC=PC+2;
                sn=AjusteHEX(memo[PC])+AjusteHEX(memo[PC-1]);
                nn=Integer.parseInt(sn,16);
                CY=obtenFlag("P",F);
                if(CY==0){
                PC=nn-1;
                }
                CL=CL+2;
                T=T+10;
                break;

            case "E3":  //EX (SP),HL
                INSTRUCCION="EX (SP),HL";
                CY=pop();
                WR=pop();
                push(H);
                push(L);
                H=WR;
                L=CY;
                T=T+19;
                break;
                
            case "E4":  //CALL PO,nn
                PC=PC+2;
                sn=AjusteHEX(memo[PC])+AjusteHEX(memo[PC-1]);
                nn=Integer.parseInt(sn,16);
                INSTRUCCION="CALL PO,"+nn;
                if(verificaPilaPUSH()==true){
                CY=obtenFlag("P",F);
                if(CY==0){
                WR=obtenRDH(PC+1);
                push(WR);
                CY=obtenRDL(PC+1);
                push(CY);
                PC=nn-1;
                }
                T=T+17; }
                else
                    PilaError=true;   
                break;

            case "E5":
                INSTRUCCION="PUSH HL";
                if(verificaPilaPUSH()==true){
                push(H);
                push(L);
                stackRec=stackRec+2;
                T=T+11;}
                else
                    PilaError=true;   
                break;
                
            case "E6":
                INSTRUCCION="AND n";
                PC=PC+1;
                A=AND(A,memo[PC]);
                WR=A;
                F=grabarFlag("C",'0',F);
                F=grabarFlag("N",'0',F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("P",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("H",WR,F);
                CL++;
                T=T+7;
                break;
                
            case "E7":  //RST 20
                INSTRUCCION="RST 20";
                if(verificaPilaPUSH()==true){
                WR=obtenRDH(PC+1);
                push(WR);
                CY=obtenRDL(PC+1);
                push(CY);
                PC=31;
                T=T+11; }
                else
                    PilaError=true;   
                break;

            case "E8":
//                //RET PE
                INSTRUCCION="RET PE";
                if(verificaPilaPOP()==true){
                CY=obtenFlag("P",F);
                if(CY==1){
                CY=pop();
                WR=pop();
                PC=obtenRC(WR,CY);
                PC--;
                T=T+6;
                }
                T=T+5;   }  
                else
                    PilaError=true;                              
                break;
                
            case "E9":  //JP (HL)
                HL=obtenRC(H,L);
                INSTRUCCION="JP (HL)";
                sn=AjusteHEX(memo[HL+1])+AjusteHEX(memo[HL]);
                nn=Integer.parseInt(sn,16);
                PC=nn-1;
                T=T+4;
                break;
                
            case "EA":  //JP PE,nn
                INSTRUCCION="JP PE,"+AjusteHEX(memo[PC+2])+AjusteHEX(memo[PC+1]); 
                PC=PC+2;
                sn=AjusteHEX(memo[PC])+AjusteHEX(memo[PC-1]);
                nn=Integer.parseInt(sn,16);
                CY=obtenFlag("P",F);
                if(CY==1){
                PC=nn-1;
                }
                CL=CL+2;
                T=T+10;
              break;

            case "EB": //EX DE,HL
                INSTRUCCION="EX DE,HL";
                DE=obtenRC(D,E);
                HL=obtenRC(H,L);
                WR=DE;
                DE=HL;
                HL=WR;
                T=T+4;
                //EX DE,HL
                break;
                
            case "EC":  //CALL PE,dirc
                PC=PC+2;
                sn=AjusteHEX(memo[PC])+AjusteHEX(memo[PC-1]);
                nn=Integer.parseInt(sn,16);
                INSTRUCCION="CALL PE,"+nn;
                if(verificaPilaPUSH()==true){
                CY=obtenFlag("P",F);
                if(CY==1){
                WR=obtenRDH(PC+1);
                push(WR);
                CY=obtenRDL(PC+1);
                push(CY);
                PC=nn-1;
                }
                T=T+17;
                CL=CL+2;}
                else
                    PilaError=true;   
                break;

            case "EE":  //XOR n
                INSTRUCCION="XOR "+AjusteHEX(memo[PC+1]);
                PC++;
                n=memo[PC];
                A=XOR(A,n);
                WR=A;
                F=grabarFlag("C",'0',F);
                F=grabarFlag("N",'0',F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("P",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("H",WR,F);
                T=T+7;
                CL++;
               break;

            case "EF":  //RST 28
                INSTRUCCION="RST 28";
                if(verificaPilaPUSH()==true){
                WR=obtenRDH(PC+1);
                push(WR);
                CY=obtenRDL(PC+1);
                push(CY);
                PC=39;
                T=T+11;}
                else
                    PilaError=true;   
                break;
                
            case "F0":
//                //RET P
                INSTRUCCION="RET P";
                if(verificaPilaPOP()==true){
                CY=obtenFlag("S",F);
                if(CY==0){
                CY=pop();
                WR=pop();
                PC=obtenRC(WR,CY);
                PC--;
                T=T+6;
                }
                T=T+5;   }  
                else
                    PilaError=true;                              
                break;
                
            case "F1":
                INSTRUCCION="POP AF";
                if(verificaPilaPOP()==true){
                F=pop();
                A=pop();
                T=T+10;}
                else
                    PilaError=true;   
                break;
                
            case "F2":  //JP P,nn
                INSTRUCCION="JP P,"+AjusteHEX(memo[PC+2])+AjusteHEX(memo[PC+1]); 
                PC=PC+2;
                sn=AjusteHEX(memo[PC])+AjusteHEX(memo[PC-1]);
                nn=Integer.parseInt(sn,16);
                CY=obtenFlag("S",F);
                if(CY==0){
                PC=nn-1;
                }
                CL=CL+2;
                T=T+10;
                break;

            case "F3":
//                //DI
                IFF=0;
                T=T+4;
                break;
                
            case "F4":  //CALL P,dirc
                PC=PC+2;
                sn=AjusteHEX(memo[PC])+AjusteHEX(memo[PC-1]);
                nn=Integer.parseInt(sn,16);
                INSTRUCCION="CALL P,"+nn;
                if(verificaPilaPUSH()==true){
                CY=obtenFlag("S",F);
                if(CY==0){
                WR=obtenRDH(PC+1);
                push(WR);
                CY=obtenRDL(PC+1);
                push(CY);
                PC=nn-1;
                }
                CL++;
                CL++;
                T=T+17;}
                else
                    PilaError=true;   
                break;

            case "F5":
                INSTRUCCION="PUSH AF";
                if(verificaPilaPUSH()==true){
                push(A);
                push(F);
                stackRec=stackRec+2;
                T=T+11;     }     
                else
                    PilaError=true;         
                break;
                
            case "F6":  //OR in
                INSTRUCCION="OR "+AjusteHEX(memo[PC+1]);
                PC++;
                n=memo[PC];
                A=OR(A,n);
                WR=A;
                F=grabarFlag("C",'0',F);
                F=grabarFlag("N",'0',F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("P",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("H",WR,F);
                T=T+4;
                CL++;
                break;

            case "F7":  //RST 30
                INSTRUCCION="RST 30";
                if(verificaPilaPUSH()==true){
                WR=obtenRDH(PC+1);
                push(WR);
                CY=obtenRDL(PC+1);
                push(CY);
                PC=47;
                T=T+11;}
                else
                    PilaError=true;   
                break;
                
            case "F8":
//                //RET M
                INSTRUCCION="RET M";
                if(verificaPilaPOP()==true){
                CY=obtenFlag("S",F);
                if(CY==1){
                CY=pop();
                WR=pop();
                PC=obtenRC(WR,CY);
                PC--;
                T=T+6;
                }
                T=T+5;  }  
                else
                    PilaError=true;                               
                break;
                
            case "F9"://LD SP,HL
               INSTRUCCION="LD SP,HL";
                HL=obtenRC(H,L);
                SP=HL;
                T=T+6;
                break;
                
            case "FA":  //JP M,nn
                INSTRUCCION="JP M,"+AjusteHEX(memo[PC+2])+AjusteHEX(memo[PC+1]); 
                PC=PC+2;
                sn=AjusteHEX(memo[PC])+AjusteHEX(memo[PC-1]);
                nn=Integer.parseInt(sn,16);
                CY=obtenFlag("S",F);
                if(CY==1){
                PC=nn-1;
                }
                CL=CL+2;
                T=T+10;
                break;

            case "FB":  //EI
                INSTRUCCION="EI";
                IFF=1;
                T=T+4;
                break;
                
            case "FC":  //"CALL M,nn";
                PC=PC+2;
                sn=AjusteHEX(memo[PC])+AjusteHEX(memo[PC-1]);
                nn=Integer.parseInt(sn,16);
                INSTRUCCION="CALL M,"+nn;
                if(verificaPilaPUSH()==true){
                CY=obtenFlag("S",F);
                if(CY==1){
                WR=obtenRDH(PC+1);
                push(WR);
                CY=obtenRDL(PC+1);
                push(CY);
                PC=nn-1;
                }
                CL=CL+2;
                T=T+17;}
                else
                    PilaError=true;   
                break;

            case "FE"://    CP,n
                PC=PC+1;
                WR=A-memo[PC];
                F=grabarFlag("N",'1',F);
                F=determinarFlag("C",WR,F);
                F=determinarFlag("S",WR,F);
                F=determinarFlag("Z",WR,F);
                F=determinarFlag("H",WR,F);
                F=determinarFlag("P/V",WR,F);
                CL++;
                INSTRUCCION="CP "+AjusteHEX(memo[PC]);
                T=T+7;
                break;

            case "FF":  //RST 38
                INSTRUCCION="RST 38";
                if(verificaPilaPUSH()==true){
                WR=obtenRDH(PC+1);
                push(WR);
                CY=obtenRDL(PC+1);
                push(CY);
                PC=55;
                T=T+11;}
                else
                    PilaError=true;   
                break;
                
            default:
              CL++;
              PC=PC+1;
              instamin=Integer.toHexString(memo[PC]);
              insta=instamin.toUpperCase();
              cinsta=insta.toCharArray();

              while (cinsta.length!=2){
              insta="0"+insta;
              cinsta=insta.toCharArray();
              }
              inst=inst+insta;
                break;
        }
          ////// LORENA
           
           SWITCH2();
          //////FIN LORENA
           PC=PC+1;
        
    }
    
     public static int obtenRDH(int UV){

                int U,V;
                String sUV,sU,sV;
                char cUV[];
                sUV=Integer.toHexString(UV);
                cUV=sUV.toCharArray();                    
                while(cUV.length!=4){
                sUV="0"+sUV;
                cUV=sUV.toCharArray();                    
                }
                sU=""+cUV[0]+cUV[1];

                U=Integer.parseInt(sU,16);

        return U;
    }
    public static int obtenRDL(int UV){

                int U,V;
                String sUV,sU,sV;
                char cUV[];
                sUV=Integer.toHexString(UV);
                cUV=sUV.toCharArray();                    
                while(cUV.length!=4){
                sUV="0"+sUV;
                cUV=sUV.toCharArray();                    
                }
                sV=""+cUV[2]+cUV[3];
                V=Integer.parseInt(sV,16);

        return V;
    }
    public static int obtenRC(int X,int Y){
            int XY;
            String sXY,sX,sY;            
            char cX[],cY[];
            sX=Integer.toHexString(X);
            cX=sX.toCharArray();
            while (cX.length!=2){
            sX="0"+sX;
            cX=sX.toCharArray();
            }
            sY=Integer.toHexString(Y);
            cY=sY.toCharArray();
            while (cY.length!=2){
            sY="0"+sY;
            cY=sY.toCharArray();
            }
            sXY=sX+sY;
            XY=Integer.parseInt(sXY,16);
    return XY;    
    }           
    
         public static int obtenFlag(String Bandera,int Flags){ //Recordando que Flags es el registro F
            int Flag=0;
            String sF,sFlag;
            char cF[];
            sF=Integer.toBinaryString(Flags);            
            cF=sF.toCharArray();
            while(cF.length!=8){
            sF="0"+sF;
            cF=sF.toCharArray();
            }
            switch(Bandera){
                case "S":
                    sFlag=""+cF[0];
                    Flag=Integer.parseInt(sFlag,2);
                    break;

                case "Z":
                    sFlag=""+cF[1];
                    Flag=Integer.parseInt(sFlag,2);
                    break;

                case "H":
                    sFlag=""+cF[3];
                    Flag=Integer.parseInt(sFlag,2);
                    break;

                case "P/V":
                    sFlag=""+cF[5];
                    Flag=Integer.parseInt(sFlag,2);
                    break;

                case "N":
                    sFlag=""+cF[6];
                    Flag=Integer.parseInt(sFlag,2);
                    break;

                case "C":
                    sFlag=""+cF[7];
                    Flag=Integer.parseInt(sFlag,2);
                    break;

                default:
                    System.out.println("No existe la bandera que solicitas");
                    break;
            }
            
        return Flag;    
        }
        
        public static int grabarFlag(String Bandera,char Valor,int Flags){ //Recordando que Flags es el registro F
            String sF;
            char cF[];
            sF=Integer.toBinaryString(Flags);            
            cF=sF.toCharArray();
            while(cF.length!=8){
            sF="0"+sF;
            cF=sF.toCharArray();
            }
            switch(Bandera){
                case "S":
                    cF[0]=Valor;
                    sF=String.valueOf(cF);
                    break;

                case "Z":
                    cF[1]=Valor;
                    sF=String.valueOf(cF);
                    break;

                case "H":
                    cF[3]=Valor;
                    sF=String.valueOf(cF);
                    break;

                case "P/V":
                    cF[5]=Valor;
                    sF=String.valueOf(cF);
                    break;

                case "N":
                    cF[6]=Valor;
                    sF=String.valueOf(cF);
                    break;

                case "C":
                    cF[7]=Valor;
                    sF=String.valueOf(cF);
                    break;

                default:
                    System.out.println("No existe la bandera que insertaste");
                    break;
            }
        Flags=Integer.parseInt(sF,2);
        return Flags;    
        }
        
        public static int determinarFlag(String Bandera,int Registro,int Flags){ //Recordando que Flags es el registro F
            String sF;
            char cF[];
            sF=Integer.toBinaryString(Flags);
            cF=sF.toCharArray();
            while(cF.length!=8){
            sF="0"+sF;
            cF=sF.toCharArray();
            }
            switch(Bandera){
                case "S":
                    if (Registro>=0)                        
                    cF[0]='0';
                    else
                    cF[0]='1';
                    sF=String.valueOf(cF);
                    break;

                case "Z":
                    if (Registro==0)
                    cF[1]='1';
                    else
                    cF[1]='0';
                    sF=String.valueOf(cF);
                    break;

                case "H":
                    if(Registro>15)
                        cF[3]='1';
                    else
                        cF[3]='0';                    
                    sF=String.valueOf(cF);
                    break;

                case "P/V":
                    if ((Registro>127) || (Registro<-126))
                    cF[5]='1';
                    else
                    cF[5]='0';
                    sF=String.valueOf(cF);
                    break;
                
                case "P":
                    if ((Registro/2)==0)
                    cF[5]='1';
                    else
                    cF[5]='0';
                    sF=String.valueOf(cF);
                    break;

                case "N":
                    System.out.println("Esta bandera no necesita determinarse");
                    break;

                case "C":
                    if(Registro>255)
                        cF[7]='1';
                    else
                        cF[7]='0';                    
                    sF=String.valueOf(cF);
                    break;

                default:
                    System.out.println("No existe la bandera que insertaste");
                    break;
            }
        Flags=Integer.parseInt(sF,2);            
        return Flags;    
        }

        public static int RI(int Registro){
            String sR;
            char cR[],aux;

            sR=Integer.toBinaryString(Registro);
            cR=sR.toCharArray();
            while(cR.length!=8){
            sR="0"+sR;
            cR=sR.toCharArray();
            }

            aux=cR[0];
    
            for(int i=0;i<7;i++){
                cR[i]=cR[i+1];                
            }
            
            cR[7]=aux;
    
        sR=String.valueOf(cR);

        Registro=Integer.parseInt(sR,2);            
        return Registro;
        }

        public static int RD(int Registro){
            String sR;
            char cR[],aux;

            sR=Integer.toBinaryString(Registro);
            cR=sR.toCharArray();
            while(cR.length!=8){
            sR="0"+sR;
            cR=sR.toCharArray();
            }

            aux=cR[7];
    
            for(int i=7;i>0;i--){
                cR[i]=cR[i-1];                
            }
            
            cR[0]=aux;
    
        sR=String.valueOf(cR);

        Registro=Integer.parseInt(sR,2);            
        return Registro;
        }

        public static int RIC(int Registro,int Acarreo){
            String sR,sA;
            char cR[],cA[],aux;

            sR=Integer.toBinaryString(Registro);
            cR=sR.toCharArray();
            while(cR.length!=8){
            sR="0"+sR;
            cR=sR.toCharArray();
            }

            sA=Integer.toBinaryString(Acarreo);
            cA=sR.toCharArray();

            aux=cA[0];
    
            for(int i=0;i<7;i++){
                cR[i]=cR[i+1];                
            }
            
            cR[7]=aux;
    
        sR=String.valueOf(cR);

        Registro=Integer.parseInt(sR,2);            
        return Registro;
        }

        public static int RDC(int Registro,int Acarreo){
            String sR,sA;
            char cR[],cA[],aux;

            sR=Integer.toBinaryString(Registro);
            cR=sR.toCharArray();
            while(cR.length!=8){
            sR="0"+sR;
            cR=sR.toCharArray();
            }

            sA=Integer.toBinaryString(Acarreo);
            cA=sR.toCharArray();

            aux=cA[0];
    
            for(int i=7;i>0;i--){
                cR[i]=cR[i-1];                
            }
            
            cR[0]=aux;
    
        sR=String.valueOf(cR);

        Registro=Integer.parseInt(sR,2);            
        return Registro;
        }
        public static int AND(int Registro1,int Registro2){
            String sR1,sR2;
            char cR1[],cR2[];
            sR1=Integer.toBinaryString(Registro1);
            sR2=Integer.toBinaryString(Registro2);
            cR1=sR1.toCharArray();
            while(cR1.length!=8){
            sR1="0"+sR1;
            cR1=sR1.toCharArray();
            }
            cR2=sR2.toCharArray();
            while(cR2.length!=8){
            sR2="0"+sR2;
            cR2=sR2.toCharArray();
            }
            for(int i=0;i<8;i++){
                if((cR1[i]=='1')&&(cR2[i]=='1'))
                    cR1[i]='1';
                else
                    cR1[i]='0';
            }
            sR1=String.valueOf(cR1);
            Registro1=Integer.parseInt(sR1,2);            
        return Registro1;
        }

        public static int OR(int Registro1,int Registro2){
            String sR1,sR2;
            char cR1[],cR2[];
            sR1=Integer.toBinaryString(Registro1);
            sR2=Integer.toBinaryString(Registro2);
            cR1=sR1.toCharArray();
            while(cR1.length!=8){
            sR1="0"+sR1;
            cR1=sR1.toCharArray();
            }
            cR2=sR2.toCharArray();
            while(cR2.length!=8){
            sR2="0"+sR2;
            cR2=sR2.toCharArray();
            }
            for(int i=0;i<8;i++){
                if((cR1[i]=='0')&&(cR2[i]=='0'))
                    cR1[i]='0';
                else
                    cR1[i]='1';
            }
            sR1=String.valueOf(cR1);
            Registro1=Integer.parseInt(sR1,2);            
        return Registro1;
        }
        
        public static int XOR(int Registro1,int Registro2){
            String sR1,sR2;
            char cR1[],cR2[];
            sR1=Integer.toBinaryString(Registro1);
            sR2=Integer.toBinaryString(Registro2);
            cR1=sR1.toCharArray();
            while(cR1.length!=8){
            sR1="0"+sR1;
            cR1=sR1.toCharArray();
            }
            cR2=sR2.toCharArray();
            while(cR2.length!=8){
            sR2="0"+sR2;
            cR2=sR2.toCharArray();
            }
            for(int i=0;i<8;i++){
                if(cR1[i]==cR2[i])
                    cR1[i]='0';
                else
                    cR1[i]='1';
            }
            sR1=String.valueOf(cR1);
            Registro1=Integer.parseInt(sR1,2);            
        return Registro1;
        }
        
        // LORENA
        
        public static int SLA(int Registro){
            String sR;
            char cR[],aux;

            sR=Integer.toBinaryString(Registro);
            cR=sR.toCharArray();
            while(cR.length!=8){
            sR="0"+sR;
            cR=sR.toCharArray();
            }

            for(int i=0;i<7;i++){
                cR[i]=cR[i+1];                
            }

            cR[7]=0;
    
        sR=String.valueOf(cR);

        Registro=Integer.parseInt(sR,2);            
        return Registro;
        }
            
        public static int SRL(int Registro){
            String sR;
            char cR[],aux;

            sR=Integer.toBinaryString(Registro);
            cR=sR.toCharArray();
            while(cR.length!=8){
            sR="0"+sR;
            cR=sR.toCharArray();
            }

            for(int i=7;i>0;i--){
                cR[i]=cR[i-1];                
            }

            cR[0]=0;
    
        sR=String.valueOf(cR);

        Registro=Integer.parseInt(sR,2);            
        return Registro;
        }
         
        public static int SRA(int Registro){
            String sR;
            char cR[],aux;

            sR=Integer.toBinaryString(Registro);
            cR=sR.toCharArray();
            while(cR.length!=8){
            sR="0"+sR;
            cR=sR.toCharArray();
            }


            for(int i=7;i>0;i--){
                cR[i]=cR[i-1];                
            }
    
        sR=String.valueOf(cR);

        Registro=Integer.parseInt(sR,2);            
        return Registro;
        }
        
        /// FIN LORENA
        
        //NUEVO METODO
        
void SWITCH2(){

    //switch de lore
      switch(inst){
              

// los siguientes corresponden al RR 
case "CB1F":
     CY=obtenFlag("C",F);
     CY2=obtenFlag("C",A);
     sCY2=Integer.toBinaryString(CY2);
     cCY2=sCY2.toCharArray();
     F=grabarFlag("C",cCY2[0],F);
     A=RDC(A,CY);                
     F=grabarFlag("N",'0',F);
     F=grabarFlag("H",'0',F);
     F=determinarFlag("S",A,F);
     F=determinarFlag("Z",A,F);
     F=determinarFlag("P",A,F);
     F=determinarFlag("C",A,F);
     INSTRUCCION="RR A";
     T=T+4;
     break;
     
case "CB18":
    CY=obtenFlag("C",F);
    CY2=obtenFlag("C",B);
    sCY2=Integer.toBinaryString(CY2);
    cCY2=sCY2.toCharArray();
    F=grabarFlag("C",cCY2[0],F);
    B=RDC(B,CY);                
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",B,F);
    F=determinarFlag("Z",B,F);
    F=determinarFlag("P",B,F);
    F=determinarFlag("C",B,F);
    INSTRUCCION="RR B";
    T=T+4;
    break;
    
case "CB19":
    CY=obtenFlag("C",F);
   CY2=obtenFlag("C",C);
   sCY2=Integer.toBinaryString(CY2);
   cCY2=sCY2.toCharArray();
   F=grabarFlag("C",cCY2[0],F);
   C=RDC(C,CY);                
   F=grabarFlag("N",'0',F);
   F=grabarFlag("H",'0',F);
   F=determinarFlag("S",C,F);
   F=determinarFlag("Z",C,F);
   F=determinarFlag("P",C,F);
   F=determinarFlag("C",C,F);
   INSTRUCCION="RR C";
    T=T+4;
    break;
   
case "CB1A":
     CY=obtenFlag("C",F);
    CY2=obtenFlag("C",D);
    sCY2=Integer.toBinaryString(CY2);
    cCY2=sCY2.toCharArray();
    F=grabarFlag("C",cCY2[0],F);
    D=RDC(D,CY);                
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",D,F);
    F=determinarFlag("Z",D,F);
    F=determinarFlag("P",D,F);
    F=determinarFlag("C",D,F);
    INSTRUCCION="RR D";
    T=T+4;
    break;
    
case "CB1B":
     CY=obtenFlag("C",F);
    CY2=obtenFlag("C",E);
    sCY2=Integer.toBinaryString(CY2);
    cCY2=sCY2.toCharArray();
    F=grabarFlag("C",cCY2[0],F);
    E=RDC(E,CY);                
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",E,F);
    F=determinarFlag("Z",E,F);
    F=determinarFlag("P",E,F);
    F=determinarFlag("C",E,F);
    INSTRUCCION="RR E";
    T=T+4;
    break;
    
case "CB1C":
     CY=obtenFlag("C",F);
    CY2=obtenFlag("C",H);
    sCY2=Integer.toBinaryString(CY2);
    cCY2=sCY2.toCharArray();
    F=grabarFlag("C",cCY2[0],F);
    H=RDC(H,CY);                
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",H,F);
    F=determinarFlag("Z",H,F);
    F=determinarFlag("P",H,F);
    F=determinarFlag("C",H,F);
    INSTRUCCION="RR H";
    T=T+4;
    break;
    
case "CB1D":
     CY=obtenFlag("C",F);
    CY2=obtenFlag("C",L);
    sCY2=Integer.toBinaryString(CY2);
    cCY2=sCY2.toCharArray();
    F=grabarFlag("C",cCY2[0],F);
    L=RDC(L,CY);                
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",L,F);
    F=determinarFlag("Z",L,F);
    F=determinarFlag("P",L,F);
    F=determinarFlag("C",L,F);
    INSTRUCCION="RRL";
    T=T+4;
    break;

case "CB1E":
     CY=obtenFlag("C",F);
     HL=obtenRC(H,L);
     n=memo[HL];
    CY2=obtenFlag("C",n);
    sCY2=Integer.toBinaryString(CY2);
    cCY2=sCY2.toCharArray();
    F=grabarFlag("C",cCY2[0],F);
    n=RDC(n,CY);                
    memo[HL]=n;
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",n,F);
    F=determinarFlag("Z",n,F);
    F=determinarFlag("P",n,F);
    F=determinarFlag("C",n,F);
    INSTRUCCION="RR(HL)";
    T=T+4;
    break;
// los siguientes corresponden al RL 
case "CB17":
     CY=obtenFlag("C",F);
    CY2=obtenFlag("S",A);
    sCY2=Integer.toBinaryString(CY2);
    cCY2=sCY2.toCharArray();
    F=grabarFlag("C",cCY2[0],F);
    A=RIC(A,CY);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",A,F);
    F=determinarFlag("Z",A,F);
    F=determinarFlag("P",A,F);
    F=determinarFlag("C",A,F);
    INSTRUCCION="RL A";
    T=T+8;
    break;
    
case "CB10":
    CY=obtenFlag("C",F);
   CY2=obtenFlag("S",B);
   sCY2=Integer.toBinaryString(CY2);
   cCY2=sCY2.toCharArray();
   F=grabarFlag("C",cCY2[0],F);
   B=RIC(B,CY);
   F=grabarFlag("N",'0',F);
   F=grabarFlag("H",'0',F);
   F=determinarFlag("S",B,F);
   F=determinarFlag("Z",B,F);
   F=determinarFlag("P",B,F);
   F=determinarFlag("C",B,F);
   INSTRUCCION="RL B";
    T=T+8;
    break;
   
case "CB11":
    CY=obtenFlag("C",F);
   CY2=obtenFlag("S",C);
   sCY2=Integer.toBinaryString(CY2);
   cCY2=sCY2.toCharArray();
   F=grabarFlag("C",cCY2[0],F);
   C=RIC(C,CY);
   F=grabarFlag("N",'0',F);
   F=grabarFlag("H",'0',F);
   F=determinarFlag("S",C,F);
   F=determinarFlag("Z",C,F);
   F=determinarFlag("P",C,F);
   F=determinarFlag("C",C,F);
   INSTRUCCION="RL C";
   T=T+8;
   break;
   
case"CB12":
     CY=obtenFlag("C",F);
    CY2=obtenFlag("S",D);
    sCY2=Integer.toBinaryString(CY2);
    cCY2=sCY2.toCharArray();
    F=grabarFlag("C",cCY2[0],F);
    D=RIC(D,CY);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",D,F);
    F=determinarFlag("Z",D,F);
    F=determinarFlag("P",D,F);
    F=determinarFlag("C",D,F);
    INSTRUCCION="RL D";
    T=T+8;
    break;
    
case "CB13":
     CY=obtenFlag("C",F);
    CY2=obtenFlag("S",E);
    sCY2=Integer.toBinaryString(CY2);
    cCY2=sCY2.toCharArray();
    F=grabarFlag("C",cCY2[0],F);
    E=RIC(E,CY);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",E,F);
    F=determinarFlag("Z",E,F);
    F=determinarFlag("P",E,F);
    F=determinarFlag("C",E,F);
    INSTRUCCION="RL E";
    T=T+8;
    break;
    
case "CB14":
     CY=obtenFlag("C",F);
    CY2=obtenFlag("S",H);
    sCY2=Integer.toBinaryString(CY2);
    cCY2=sCY2.toCharArray();
    F=grabarFlag("C",cCY2[0],F);
    H=RIC(H,CY);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",H,F);
    F=determinarFlag("Z",H,F);
    F=determinarFlag("P",H,F);
    F=determinarFlag("C",H,F);
    INSTRUCCION="RL H";
    T=T+8;
    break;
    
case "CB15":
     CY=obtenFlag("C",F);
    CY2=obtenFlag("S",L);
    sCY2=Integer.toBinaryString(CY2);
    cCY2=sCY2.toCharArray();
    F=grabarFlag("C",cCY2[0],F);
    L=RIC(L,CY);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",L,F);
    F=determinarFlag("Z",L,F);
    F=determinarFlag("P",L,F);
    F=determinarFlag("C",L,F);
    INSTRUCCION="RL L";
    T=T+8;
    break;
    
case "CB16":
     CY=obtenFlag("C",F);
     HL=obtenRC(H,L);
     n=memo[HL];
    CY2=obtenFlag("S",n);
    sCY2=Integer.toBinaryString(CY2);
    cCY2=sCY2.toCharArray();
    F=grabarFlag("C",cCY2[0],F);
    n=RIC(n,CY);
    memo[HL]=n;
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",WR,F);
    F=determinarFlag("Z",WR,F);
    F=determinarFlag("P",WR,F);
    F=determinarFlag("C",WR,F);
    INSTRUCCION="RL (HL)";
    T=T+8;
    break;
// los siguientes corresponden al RRC      
case "CB0F":
     CY=obtenFlag("C",A);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    A=RD(A);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",A,F);
    F=determinarFlag("Z",A,F);
    F=determinarFlag("P",A,F);
    F=determinarFlag("C",A,F);
    INSTRUCCION="RRC A";
    T=T+8;
     break; 
     
case "CB08":
     CY=obtenFlag("C",B);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    B=RD(B);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",B,F);
    F=determinarFlag("Z",B,F);
    F=determinarFlag("P",B,F);
    F=determinarFlag("C",B,F);
    INSTRUCCION="RRC B";
    T=T+8;
     break;
         
case "CB09":
     CY=obtenFlag("C",C);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    C=RD(C);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",C,F);
    F=determinarFlag("Z",C,F);
    F=determinarFlag("P",C,F);
    F=determinarFlag("C",C,F);
    INSTRUCCION="RRC C";
     break;
     
case "CB0A":
     CY=obtenFlag("C",D);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    D=RD(D);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",D,F);
    F=determinarFlag("Z",D,F);
    F=determinarFlag("P",D,F);
    F=determinarFlag("C",D,F);
    INSTRUCCION="RRC D";
     break;
     
case "CB0B":
     CY=obtenFlag("C",E);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    E=RD(E);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",E,F);
    F=determinarFlag("Z",E,F);
    F=determinarFlag("P",E,F);
    F=determinarFlag("C",E,F);
    INSTRUCCION="RRC E";
     break;
     
case "CB0C":
     CY=obtenFlag("C",H);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    H=RD(H);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",H,F);
    F=determinarFlag("Z",H,F);
    F=determinarFlag("P",H,F);
    F=determinarFlag("C",H,F);
    INSTRUCCION="RRC H";
     break;
     
case "CB0D":
     CY=obtenFlag("C",L);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    L=RD(L);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",L,F);
    F=determinarFlag("Z",L,F);
    F=determinarFlag("P",L,F);
    F=determinarFlag("C",L,F);
    INSTRUCCION="RRCL L"; 
     break;
     
case "CB0E":
     
     HL=obtenRC(H,L);
     n=memo[HL];
     CY=obtenFlag("C",n);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    n=RD(n);
    memo[HL]=n;
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",WR,F);
    F=determinarFlag("Z",WR,F);
    F=determinarFlag("P",WR,F);
    F=determinarFlag("C",WR,F);
    INSTRUCCION="RRC (HL)";
     break;  
//los siguientes corresponden RLC    
case "CB07":
     CY=obtenFlag("S",A);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    A=RI(A);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",A,F);
    F=determinarFlag("Z",A,F);
    F=determinarFlag("P",A,F);
    F=determinarFlag("C",A,F);
    INSTRUCCION="RLC A";
    T=T+8;
     break;
     
case "CB00":
     CY=obtenFlag("S",B);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    B=RI(B);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",B,F);
    F=determinarFlag("Z",B,F);
    F=determinarFlag("P",B,F);
    F=determinarFlag("C",B,F);
    INSTRUCCION="RLC B";
    T=T+8;
     break;
     
case "CB01":
     CY=obtenFlag("S",C);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    C=RI(C);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",C,F);
    F=determinarFlag("Z",C,F);
    F=determinarFlag("P",C,F);
    F=determinarFlag("C",C,F);
    INSTRUCCION="RLC C";
    T=T+8;
     break;
     
case "CB02":
     CY=obtenFlag("S",D);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    D=RI(D);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",D,F);
    F=determinarFlag("Z",D,F);
    F=determinarFlag("P",D,F);
    F=determinarFlag("C",D,F);
    INSTRUCCION="RLC D";
    T=T+8;
     break;
     
case "CB03":
     CY=obtenFlag("S",E);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    E=RI(E);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",E,F);
    F=determinarFlag("Z",E,F);
    F=determinarFlag("P",E,F);
    F=determinarFlag("C",E,F);
    INSTRUCCION="RLC E";
    T=T+8;
     break;
     
case "CB04":
     CY=obtenFlag("S",H);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    H=RI(H);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",H,F);
    F=determinarFlag("Z",H,F);
    F=determinarFlag("P",H,F);
    F=determinarFlag("C",H,F);
    INSTRUCCION="RLC H"; 
    T=T+8;
     break;
     
case "CB05":
     CY=obtenFlag("S",L);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    L=RI(L);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",L,F);
    F=determinarFlag("Z",L,F);
    F=determinarFlag("P",L,F);
    F=determinarFlag("C",L,F);
    INSTRUCCION="RLC L";
    T=T+8;
     break;
     
case "CB06":
     HL=obtenRC(H,L);
     n=memo[HL];
     CY=obtenFlag("S",n);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    n=RI(n);
    memo[HL]=n;
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",n,F);
    F=determinarFlag("Z",n,F);
    F=determinarFlag("P",n,F);
    F=determinarFlag("C",n,F);
    INSTRUCCION="RLC (HL)";
    T=T+15;
     break;
//los siguientes corresponden a SRA
case "CB2F":
     CY=obtenFlag("C",A);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    A=SRA(A);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",A,F);
    F=determinarFlag("Z",A,F);
    F=determinarFlag("P",A,F);
    F=determinarFlag("C",A,F);
    T=T+4;
     INSTRUCCION="SRA A";
     break;
     
case "CB28":
     CY=obtenFlag("C",B);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    B=SRA(B);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",B,F);
    F=determinarFlag("Z",B,F);
    F=determinarFlag("P",B,F);
    F=determinarFlag("C",B,F);
    T=T+4;
    INSTRUCCION="SRA B";
     break;
     
case "CB29":
     CY=obtenFlag("C",C);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    C=SRA(C);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",C,F);
    F=determinarFlag("Z",C,F);
    F=determinarFlag("P",C,F);
    F=determinarFlag("C",C,F);
    T=T+4;
    INSTRUCCION="SRA C";
     break;
     
case "CB2A":
     CY=obtenFlag("C",D);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    D=SRA(D);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",D,F);
    F=determinarFlag("Z",D,F);
    F=determinarFlag("P",D,F);
    F=determinarFlag("C",D,F);
    T=T+4;
    INSTRUCCION="SRA D";
     break;
     
case "CB2B":
     CY=obtenFlag("C",E);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    E=SRA(E);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",E,F);
    F=determinarFlag("Z",E,F);
    F=determinarFlag("P",E,F);
    F=determinarFlag("C",E,F);
    T=T+4;
    INSTRUCCION="SRA E";
     break;
     
case "CB2C":
     CY=obtenFlag("C",H);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    H=SRA(H);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",H,F);
    F=determinarFlag("Z",H,F);
    F=determinarFlag("P",H,F);
    F=determinarFlag("C",H,F);
    T=T+4;
    INSTRUCCION="SRA H";
     break;
     
case "CB2D":
     CY=obtenFlag("C",L);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    L=SRA(L);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",L,F);
    F=determinarFlag("Z",L,F);
    F=determinarFlag("P",L,F);
    F=determinarFlag("C",L,F);
    T=T+4;
    INSTRUCCION="SRA L";
     break;
     
case "CB2E":
     
     HL=obtenRC(H,L);
     n=memo[HL];
     CY=obtenFlag("C",n);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    n=SRA(n);
    memo[HL]=n;
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",n,F);
    F=determinarFlag("Z",n,F);
    F=determinarFlag("P",n,F);
    F=determinarFlag("C",n,F);
    T=T+4;
    INSTRUCCION="SRA (HL)";
     break;
//las siguientes corresponden a SLA
case "CB27":
     CY=obtenFlag("S",A);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    A=SLA(A);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",A,F);
    F=determinarFlag("Z",A,F);
    F=determinarFlag("P",A,F);
    F=determinarFlag("C",A,F);
    T=T+4;
    INSTRUCCION="SLA A";
     break;
     
case "CB20":
     CY=obtenFlag("S",B);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    B=SLA(B);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",B,F);
    F=determinarFlag("Z",B,F);
    F=determinarFlag("P",B,F);
    F=determinarFlag("C",B,F);
    T=T+4;
    INSTRUCCION="SLA B";
     break;
     
case "CB21":
     CY=obtenFlag("S",C);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    C=SLA(C);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",C,F);
    F=determinarFlag("Z",C,F);
    F=determinarFlag("P",C,F);
    F=determinarFlag("C",C,F);
    T=T+4;
    INSTRUCCION="SLA C";
     break;
     
case "CB22":
     CY=obtenFlag("S",D);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    D=SLA(D);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",D,F);
    F=determinarFlag("Z",D,F);
    F=determinarFlag("P",D,F);
    F=determinarFlag("C",D,F);
    T=T+4;
    INSTRUCCION="SLA D";
     break;
     
case "CB23":
     CY=obtenFlag("S",E);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    E=SLA(E);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",E,F);
    F=determinarFlag("Z",E,F);
    F=determinarFlag("P",E,F);
    F=determinarFlag("C",E,F);
    T=T+4;
    INSTRUCCION="SLA E";
     break;
     
case "CB24":
     CY=obtenFlag("S",H);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    H=SLA(H);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",H,F);
    F=determinarFlag("Z",H,F);
    F=determinarFlag("P",H,F);
    F=determinarFlag("C",H,F);
    T=T+4;
    INSTRUCCION="SLA H";
     break;
     
case "CB25":
     CY=obtenFlag("S",L);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    L=SLA(L);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",L,F);
    F=determinarFlag("Z",L,F);
    F=determinarFlag("P",L,F);
    F=determinarFlag("C",L,F);
    T=T+4;
    INSTRUCCION="SLA L";
     break;
     
case "CB26":
     HL=obtenRC(H,L);
     n=memo[HL];
     CY=obtenFlag("S",n);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    n=SLA(n);
    memo[HL]=n;
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",n,F);
    F=determinarFlag("Z",n,F);
    F=determinarFlag("P",n,F);
    F=determinarFlag("C",n,F);
    T=T+4;
    INSTRUCCION="SLA (HL)";
     break;
//los siguientes corresponden a SRL
case "CB3F":
     CY=obtenFlag("C",A);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    A=SRL(A);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",A,F);
    F=determinarFlag("Z",A,F);
    F=determinarFlag("P",A,F);
    F=determinarFlag("C",A,F);
    T=T+4;
    INSTRUCCION="SRL A"; 
     break;
     
case "CB38":
     CY=obtenFlag("C",B);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    B=SRL(B);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",B,F);
    F=determinarFlag("Z",B,F);
    F=determinarFlag("P",B,F);
    F=determinarFlag("C",B,F);
    T=T+4;
    INSTRUCCION="SRL B";
     break;
     
case "CB39":
     CY=obtenFlag("C",C);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    C=SRL(C);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",C,F);
    F=determinarFlag("Z",C,F);
    F=determinarFlag("P",C,F);
    F=determinarFlag("C",C,F);
    T=T+4;
    INSTRUCCION="SRL C";
     break;
     
case "CB3A":
     CY=obtenFlag("C",D);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    D=SRL(D);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",D,F);
    F=determinarFlag("Z",D,F);
    F=determinarFlag("P",D,F);
    F=determinarFlag("C",D,F);
    T=T+4;
    INSTRUCCION="SRL D";
     break;
     
case "CB3B":
     CY=obtenFlag("C",E);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    E=SRL(E);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",E,F);
    F=determinarFlag("Z",E,F);
    F=determinarFlag("P",E,F);
    F=determinarFlag("C",E,F);
    T=T+4;
    INSTRUCCION="SRL E";
     break;
     
case "CB3C":
     CY=obtenFlag("C",H);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    H=SRL(H);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",H,F);
    F=determinarFlag("Z",H,F);
    F=determinarFlag("P",H,F);
    F=determinarFlag("C",H,F);
    T=T+4;
    INSTRUCCION="SRL H";
     break;
     
case "CB3D":
     CY=obtenFlag("C",L);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    L=SRL(L);
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",L,F);
    F=determinarFlag("Z",L,F);
    F=determinarFlag("P",L,F);
    F=determinarFlag("C",L,F);
    T=T+4;
    INSTRUCCION="SRL L";
     break;
     
case "CB3E":
     HL=obtenRC(H,L);
     WR=memo[HL];
     CY=obtenFlag("C",WR);
    sCY=Integer.toBinaryString(CY);
    cCY=sCY.toCharArray();
    F=grabarFlag("C",cCY[0],F);
    memo[HL]=SRL(WR);
    WR=memo[HL];
    F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",WR,F);
    F=determinarFlag("Z",WR,F);
    F=determinarFlag("P",WR,F);
    F=determinarFlag("C",WR,F);
    T=T+4;
    INSTRUCCION="SRL (HL)";
     break;
//los siguientes corresponden a RLD (HL)
case "ED6F":
//   //Rotacin BCD de (HL) y A a la izquierda
     break;
     
case "ED67":
//   //Rotacin BCD de (HL) y A a la derecha
     break;

case "DDF9":
//   //LD SP,IX
    SP=IX;
    T=T+10;
    INSTRUCCION="LD SP,IX";
     break; 
    
case "FDF9":     
    T=T+16;
    SP=IY;
    INSTRUCCION="LD SP,IY";
     break; 
     
     
case "ED57":
     A=I;
     F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",A,F);
    F=determinarFlag("Z",A,F);
    cIFF=(Integer.toString(IFF)).toCharArray();     
    F=grabarFlag("P/V",cIFF[0],F); 
//
    T=T+9;
        INSTRUCCION="LD A,I";
//
//LD A,I  
     break;
     
case"ED5F":
     A=R;
     F=grabarFlag("N",'0',F);
    F=grabarFlag("H",'0',F);
    F=determinarFlag("S",A,F);
    F=determinarFlag("Z",A,F);
    cIFF=(Integer.toString(IFF)).toCharArray();     
    F=grabarFlag("P/V",cIFF[0],F);
//
    T=T+9;
        INSTRUCCION="LD A,R";
     //LD A,R  
     break;
     
case"ED47":
     I=A;
     T=T+9;
     INSTRUCCION="LD I,A";
     break;
     
case"ED4F":
     R=A;
     T=T+9;
     INSTRUCCION="LD R,A";
     break;
     //
case "DDE5":
                INSTRUCCION="PUSH IX";
                if(verificaPilaPUSH()==true){
                push(obtenRDH(IX));
                push(obtenRDL(IX));
                stackRec=stackRec+2;
                T=T+15;    }
                else
                    PilaError=true;   
    break;

case "FDE5":
                INSTRUCCION="PUSH IY";
                if(verificaPilaPUSH()==true){
                push(obtenRDH(IY));
                push(obtenRDL(IY));
                stackRec=stackRec+2;
                T=T+15;}
                else
                    PilaError=true;   
    break;

case "DDE1":    //POP IX
                INSTRUCCION="POP IX";
                if(verificaPilaPOP()==true){
                CY=pop();
                WR=pop();
                IX=obtenRC(WR,CY);
                T=T+14;    }
                else
                    PilaError=true;   
    break;

case "FDE1":    //POP IY
                INSTRUCCION="POP IY";
                if(verificaPilaPOP()==true){
                CY=pop();
                WR=pop();
                IX=obtenRC(WR,CY);
                T=T+14;    }
                else
                    PilaError=true;   
    break;
    //
case "DDE3":    
//   EX (SP),IX;
    INSTRUCCION="EX (SP),IX";
                CY=pop();
                WR=pop();
                push(obtenRDH(IX));
                push(obtenRDL(IX));
                sn=AjusteHEX(WR)+AjusteHEX(CY);
                IX=Integer.parseInt(sn,16);
                T=T+23;
     break;

case "FDE3":
//   EX (SP),IY;
    INSTRUCCION="EX (SP),IY";
                CY=pop();
                WR=pop();
                push(obtenRDH(IY));
                push(obtenRDL(IY));
                sn=AjusteHEX(WR)+AjusteHEX(CY);
                IY=Integer.parseInt(sn,16);
                T=T+23;
     break;
//
//  //todas estas instrucciones debe de asociarse el valor del registro correspondiente a (C), lo que no s es, que es (C)  
case "ED78":    
      T=T+11;
      F=grabarFlag("N",'0',F);
      F=grabarFlag("H",'0',F);
      F=determinarFlag("S",A,F);
      F=determinarFlag("Z",A,F);
      F=determinarFlag("P",A,F);
      INSTRUCCION="IN A,(C)";
      break;
        
case"ED40":
      F=grabarFlag("N",'0',F);
      F=grabarFlag("H",'0',F);
        F=determinarFlag("S",B,F);
        F=determinarFlag("Z",B,F);
        F=determinarFlag("P",B,F);
        T=T+11;
      INSTRUCCION="IN B,(C)";
        break;
        
    case"ED48":
        F=grabarFlag("N",'0',F);
       F=grabarFlag("H",'0',F);
        F=determinarFlag("S",C,F);
        F=determinarFlag("Z",C,F);
        F=determinarFlag("P",C,F);
        T=T+11;
      INSTRUCCION="IN C,(C)";
        break;
        
    case"ED50":
        F=grabarFlag("N",'0',F);
       F=grabarFlag("H",'0',F);
        F=determinarFlag("S",D,F);
        F=determinarFlag("Z",D,F);
        F=determinarFlag("P",D,F);
        T=T+11;
      INSTRUCCION="IN D,(C)";
        break;
        
    case"ED58":
        F=grabarFlag("N",'0',F);
       F=grabarFlag("H",'0',F);
        F=determinarFlag("S",E,F);
        F=determinarFlag("Z",E,F);
        F=determinarFlag("P",E,F);
        T=T+11;
      INSTRUCCION="IN E,(C)";
        break;
        
    case"ED60":
        F=grabarFlag("N",'0',F);
       F=grabarFlag("H",'0',F);
        F=determinarFlag("S",H,F);
        F=determinarFlag("Z",H,F);
        F=determinarFlag("P",H,F);
        T=T+11;
      INSTRUCCION="IN H,(C)";
        break;
        
    case"ED68":
        F=grabarFlag("N",'0',F);
       F=grabarFlag("H",'0',F);
        F=determinarFlag("S",L,F);
        F=determinarFlag("Z",L,F);
        F=determinarFlag("P",L,F);
        T=T+11;
      INSTRUCCION="IN L,(C)";
        break;
        
    
        
        //EN TODAS ESTAS INSTRUCCIOONES SE ASOCIA (C) al registro correspondiente
        
    case "ED79":
        T=T+12;
      //OUT (C),A
        INSTRUCCION="OUT (C),A";
        break;

    case"ED41":
        T=T+12;
      INSTRUCCION="OUT (C),B";
        break;

    case"ED49":
        T=T+12;
      INSTRUCCION="OUT (C),C";

        break;
    case"ED51":
        T=T+12;
      INSTRUCCION="OUT (C),D";
        break;
    case"ED59":
        T=T+12;
      INSTRUCCION="OUT (C),E";
        break;
    case"ED61":
        T=T+12;
      INSTRUCCION="OUT (C),H";
        break;
    case"ED69":
        T=T+12;
      INSTRUCCION="OUT (C),L";
        break;
//
     case "ED44":
         A=0-A;
         F=grabarFlag("N",'1',F);
         F=determinarFlag("H",A,F);
         F=determinarFlag("S",A,F);
         F=determinarFlag("Z",A,F);
         F=determinarFlag("P/V",A,F);
         F=determinarFlag("C",A,F);
         T=T+8;
         INSTRUCCION="NEG A";   
         break;
         //
     case"DD23":
        IX=IX+1;
        T=T+10;
        INSTRUCCION="INC IX";
        break;
    
     case "FD23":
        IY=IY+1;
        T=T+10;
        INSTRUCCION="INC IY";
        break;

    case "DD2B":
        IX=IX-1;
        T=T+10;
        INSTRUCCION="DEC IX";
        break;
        
    case "FD28":
        IY=IY-1;
        T=T+10;
        INSTRUCCION="DEC IY";
        break;      

    case "ED4A":
        INSTRUCCION="ADC HL,BC";
        CY=obtenFlag("C",F);
        HL=obtenRC(H,L);
        BC=obtenRC(B,C);
        HL=HL+BC+CY;
        F=grabarFlag("N",'0',F);
        F=determinarFlag("S",HL,F);
        F=determinarFlag("Z",HL,F);
        F=determinarFlag("H",HL,F);
        F=determinarFlag("P/V",HL,F);
        F=determinarFlag("C",HL,F);
           H=obtenRDH(HL);
           L=obtenRDL(HL);
        T=T+15;
        break;

case "ED5A":
        INSTRUCCION="ADC HL,DE";
        CY=obtenFlag("C",F);
        HL=obtenRC(H,L);
        DE=obtenRC(D,E);
        HL=HL+DE+CY;
        F=grabarFlag("N",'0',F);
        F=determinarFlag("S",HL,F);
        F=determinarFlag("Z",HL,F);
        F=determinarFlag("H",HL,F);
        F=determinarFlag("P/V",HL,F);
        F=determinarFlag("C",HL,F);
           H=obtenRDH(HL);
           L=obtenRDL(HL);
        T=T+15;
    break;  

case "ED6A":
        INSTRUCCION="ADC HL,HL";
        CY=obtenFlag("C",F);
        HL=obtenRC(H,L);
        HL=HL+HL+CY;
        F=grabarFlag("N",'0',F);
        F=determinarFlag("S",HL,F);
        F=determinarFlag("Z",HL,F);
        F=determinarFlag("H",HL,F);
        F=determinarFlag("P/V",HL,F);
        F=determinarFlag("C",HL,F);
           H=obtenRDH(HL);
           L=obtenRDL(HL);
        T=T+15;
    break;

case "ED7A":
//  //ADC HL,SP
        INSTRUCCION="ADC HL,SP";
        CY=obtenFlag("C",F);
        HL=obtenRC(H,L);
        HL=HL+SP+CY;
        F=grabarFlag("N",'0',F);
        F=determinarFlag("S",HL,F);
        F=determinarFlag("Z",HL,F);
        F=determinarFlag("H",HL,F);
        F=determinarFlag("P/V",HL,F);
        F=determinarFlag("C",HL,F);
           H=obtenRDH(HL);
           L=obtenRDL(HL);
        T=T+15;
    break;

case "ED42":
    INSTRUCCION="SBC HL,BC";
    CY=obtenFlag("C",F);
    HL=obtenRC(H,L);
    BC=obtenRC(B,C);
    HL=HL-BC-CY;
    F=grabarFlag("N",'1',F);
   F=determinarFlag("H",HL,F);
   F=determinarFlag("S",HL,F);
   F=determinarFlag("Z",HL,F);
   F=determinarFlag("P/V",HL,F);
   F=determinarFlag("C",HL,F);      
           H=obtenRDH(HL);
           L=obtenRDL(HL);
    T=T+15;
    break;

case "ED52":
    INSTRUCCION="SBC HL,DE";
        CY=obtenFlag("C",F);
        HL=obtenRC(H,L);
        DE=obtenRC(D,E);
        HL=HL-DE-CY;
        F=grabarFlag("N",'1',F);
        F=determinarFlag("H",HL,F);
        F=determinarFlag("S",HL,F);
        F=determinarFlag("Z",HL,F);
        F=determinarFlag("P/V",HL,F);
        F=determinarFlag("C",HL,F);     
           H=obtenRDH(HL);
           L=obtenRDL(HL);
        T=T+15;
    break;
    

case "ED62":
    INSTRUCCION="SBC HL,HL";
    CY=obtenFlag("C",F);
        HL=obtenRC(H,L);
    HL=obtenRC(H,L);
    HL=HL-HL-CY;
    F=grabarFlag("N",'1',F);
   F=determinarFlag("H",HL,F);
   F=determinarFlag("S",HL,F);
   F=determinarFlag("Z",HL,F);
   F=determinarFlag("P/V",HL,F);
   F=determinarFlag("C",HL,F);      
           H=obtenRDH(HL);
           L=obtenRDL(HL);
    T=T+15;
    break;

case "ED72":
//  //SBC HL,SP;
        INSTRUCCION="SBC HL,SP";
        CY=obtenFlag("C",F);
        HL=obtenRC(H,L);
        HL=HL-SP-CY;
        F=grabarFlag("N",'0',F);
        F=determinarFlag("S",HL,F);
        F=determinarFlag("Z",HL,F);
        F=determinarFlag("H",HL,F);
        F=determinarFlag("P/V",HL,F);
        F=determinarFlag("C",HL,F);
           H=obtenRDH(HL);
           L=obtenRDL(HL);
        T=T+15;
    break;

case "DD09":        
    INSTRUCCION="ADD IX,BC";
    BC=obtenRC(B,C);
       IX=IX+BC;
    F=grabarFlag("N",'0',F);
    F=determinarFlag("H",IX,F);
    F=determinarFlag("C",IX,F);
    T=T+15;
    break;
    
case "DD19":
    INSTRUCCION="ADD IX,DE";
    DE=obtenRC(D,E);        
       IX=IX+DE;
    F=grabarFlag("N",'0',F);
    F=determinarFlag("H",IX,F);
    F=determinarFlag("C",IX,F);
    T=T+15;
    break;

case "DD39":
//  //ADD IX,SP;
    INSTRUCCION="ADD IX,SP";
       IX=IX+SP;
    F=grabarFlag("N",'0',F);
    F=determinarFlag("H",IX,F);
    F=determinarFlag("C",IX,F);
    T=T+15;
    break;

case "DD29":
    INSTRUCCION="ADD IX,IX";
       IX=IX+IX;
    F=grabarFlag("N",'0',F);
    F=determinarFlag("H",IX,F);
    F=determinarFlag("C",IX,F);
    T=T+15; 
    break;  

case "FD09":
    INSTRUCCION="ADD IY,BC";
       BC=obtenRC(B,C);
       IY=IY+BC;
    F=grabarFlag("N",'0',F);
    F=determinarFlag("H",IY,F);
    F=determinarFlag("C",IY,F);
    T=T+15;
    break;
    
case "FD19":
    INSTRUCCION="ADD IY,DE";
       DE=obtenRC(D,E);
       IY=IY+DE;
    F=grabarFlag("N",'0',F);
    F=determinarFlag("H",IY,F);
    F=determinarFlag("C",IY,F);
    T=T+15;
    break;
    
case "FD39":
//  //ADD IY,SP;
    INSTRUCCION="ADD IY,SP";
       IY=IY+SP;
    F=grabarFlag("N",'0',F);
    F=determinarFlag("H",IY,F);
    F=determinarFlag("C",IY,F);
    T=T+15;
    break;

case "FD29":
    INSTRUCCION="ADD IY,IY";
       IY=IY+IY;
    F=grabarFlag("N",'0',F);
    F=determinarFlag("H",IY,F);
    F=determinarFlag("C",IY,F);
    T=T+15;
    break;

case "EDA0": // LDI
   INSTRUCCION="LDI";
   DE=obtenRC(D,E);
   HL=obtenRC(H,L);
   BC=obtenRC(B,C);
   memo[DE]=memo[HL];
   HL=HL+1;
   DE=DE+1;
   BC=BC-1;
   if(BC==0)
    F=grabarFlag("P/V",'0',F);
   else
    F=grabarFlag("P/V",'1',F);    
   F=grabarFlag("H",'0',F);
   F=grabarFlag("N",'0',F);
   T=T+16;
           H=obtenRDH(HL);
           L=obtenRDL(HL);
           D=obtenRDH(DE);
           E=obtenRDL(DE);
           B=obtenRDH(BC);
           C=obtenRDL(BC);
           T=T+16;
           break;
   
case "EDB0":
   INSTRUCCION="LDIR";
   DE=obtenRC(D,E);
   HL=obtenRC(H,L);
   BC=obtenRC(B,C);
   if(BC>0){
   while(BC!=0){
       memo[DE]=memo[HL];
       HL=HL+1;
       DE=DE+1;
       BC=BC-1;
   }
   T=T+5;
   }
   F=grabarFlag("H",'0',F);
   F=grabarFlag("N",'0',F);
   F=grabarFlag("P/V",'0',F);
   T=T+16;
           H=obtenRDH(HL);
           L=obtenRDL(HL);
           D=obtenRDH(DE);
           E=obtenRDL(DE);
           B=obtenRDH(BC);
           C=obtenRDL(BC);
   break;
   
case "EDA8":
   INSTRUCCION="LDD";
   DE=obtenRC(D,E);
   HL=obtenRC(H,L);
   BC=obtenRC(B,C);
   memo[DE]=memo[HL];
   HL=HL-1;
   DE=DE-1;
   BC=BC-1;
   if(BC==0)
    F=grabarFlag("P/V",'0',F);
   else
    F=grabarFlag("P/V",'1',F);            
   F=grabarFlag("H",'0',F);
   F=grabarFlag("N",'0',F);
   T=T+16;
           H=obtenRDH(HL);
           L=obtenRDL(HL);
           D=obtenRDH(DE);
           E=obtenRDL(DE);
           B=obtenRDH(BC);
           C=obtenRDL(BC);
   break;

case"EDB8":
  //LDDR
   INSTRUCCION="LDDR";
   DE=obtenRC(D,E);
   HL=obtenRC(H,L);
   BC=obtenRC(B,C);
   if(BC>0){
   while(BC!=0){
   memo[DE]=memo[HL];
   HL=HL-1;
   DE=DE-1;
   BC=BC-1;
   }
   T=T+5;
   }
   F=grabarFlag("H",'0',F);
   F=grabarFlag("P/V",'0',F);
   F=grabarFlag("N",'0',F);
   T=T+16;
           H=obtenRDH(HL);
           L=obtenRDL(HL);
           D=obtenRDH(DE);
           E=obtenRDL(DE);
           B=obtenRDH(BC);
           C=obtenRDL(BC);
   break;

case"EDA1":
    INSTRUCCION="CPI";
    HL=obtenRC(H,L);
    BC=obtenRC(B,C);
       WR=A-memo[HL];
       HL++;
       BC--;
       
   if(A==memo[HL])
    F=grabarFlag("Z",'1',F);
   else
    F=grabarFlag("Z",'0',F);            

   if(BC==0)
    F=grabarFlag("P/V",'0',F);
   else
    F=grabarFlag("P/V",'1',F);            

   F=grabarFlag("N",'1',F);
   F=determinarFlag("S",WR,F);
    F=determinarFlag("H",WR,F);
       T=T+16;
       break;
   
case"EDB1":
    INSTRUCCION="CPIR";
    HL=obtenRC(H,L);
    BC=obtenRC(B,C);

   if(BC>0){
   while((BC!=0)&&(A!=memo[HL])){
       WR=A-memo[HL];
       HL++;
       BC--;
   }
   T=T+5;
   }
   if(A==memo[HL])
    F=grabarFlag("Z",'1',F);
   else
    F=grabarFlag("Z",'0',F);            

   if(BC==0)
    F=grabarFlag("P/V",'0',F);
   else
    F=grabarFlag("P/V",'1',F);            

   F=grabarFlag("N",'1',F);
    F=determinarFlag("H",WR,F);
    F=determinarFlag("S",WR,F);
       T=T+16;
    break;
    
case"EDA9":
    INSTRUCCION="CPD";
    HL=obtenRC(H,L);
    BC=obtenRC(B,C);
       WR=A-memo[HL];
       HL--;
       BC--;
       
   if(A==memo[HL])
    F=grabarFlag("Z",'1',F);
   else
    F=grabarFlag("Z",'0',F);            

   if(BC==0)
    F=grabarFlag("P/V",'0',F);
   else
    F=grabarFlag("P/V",'1',F);            

   F=grabarFlag("N",'1',F);
   F=determinarFlag("S",WR,F);
    F=determinarFlag("H",WR,F);
       T=T+16;
    break;
  
  
case "EDB9":
    INSTRUCCION="CPDR";
    HL=obtenRC(H,L);
    BC=obtenRC(B,C);
   if(BC>0){
   while((BC!=0)&&(A!=memo[HL])){
       WR=A-memo[HL];
       HL--;
       BC--;
   }
   T=T+5;
   }
   if(A==memo[HL])
    F=grabarFlag("Z",'1',F);
   else
    F=grabarFlag("Z",'0',F);            
   if(BC==0)
    F=grabarFlag("P/V",'0',F);
   else
    F=grabarFlag("P/V",'1',F);            
   F=grabarFlag("N",'1',F);
    F=determinarFlag("H",WR,F);
    F=determinarFlag("S",WR,F);
       T=T+16;
    break;
    
  
case"EDA2":
    INSTRUCCION="* INI";
    break;
    
case"EDB2":
    INSTRUCCION="* INIR";
    break;
    
case"EDAA":
    INSTRUCCION="* IND";
    break;
    
case"EDBA":
    INSTRUCCION="* INDR";
    break;
    
case"EDA3":
    INSTRUCCION="* OUTI";
    break;
    
case"EDB3":
    INSTRUCCION="* OUTIR";
    break;
    
case"EDAB":
    INSTRUCCION="* OUTD";
    break;
    
case"EDBB":
    INSTRUCCION="* OUTDR";
    break;
     //EMPIEZA BIT
  //REGISTRO A 

case "CB47":
       INSTRUCCION="BIT 0,A";
        CY=obtenFlag("C", A);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;

case "CB4F":
       INSTRUCCION="BIT 1,A";
        CY=obtenFlag("N", A);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
   
case "CB57":
       INSTRUCCION="BIT 2,A";
        CY=obtenFlag("P/V", A);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
   
case "CB5F":
       INSTRUCCION="BIT 3,A";
        CY=obtenFlag("3", A);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
case "CB67":
       INSTRUCCION="BIT 4,A";
        CY=obtenFlag("H", A);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break; 

case "CB6F":
       INSTRUCCION="BIT 5,A";
        CY=obtenFlag("5", A);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
   
case "CB77":
       INSTRUCCION="BIT 6,A";
        CY=obtenFlag("Z", A);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
        

case "CB7F":
       INSTRUCCION="BIT 7,A";
        CY=obtenFlag("S",A);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
        
//REGISTRO B
        
case "CB40":
       INSTRUCCION="BIT 0,B";
        CY=obtenFlag("C",B);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;

case "CB48":
       INSTRUCCION="BIT 1,B";
        CY=obtenFlag("N",B);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
   
case "CB50":
       INSTRUCCION="BIT 2,B";
        CY=obtenFlag("P/V",B);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
   
case "CB58":
       INSTRUCCION="BIT 3,B";
        CY=obtenFlag("3",B);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
case "CB60":
       INSTRUCCION="BIT 4,B";
        CY=obtenFlag("H",B);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break; 

case "CB68":
       INSTRUCCION="BIT 5,B";
        CY=obtenFlag("5",B);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
   
case "CB70":
       INSTRUCCION="BIT 6,B";
        CY=obtenFlag("Z",B);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
        

case "CB78":
       INSTRUCCION="BIT 7,B";
        CY=obtenFlag("S",B);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;

    //REGISTRO C
case "CB41":
       INSTRUCCION="BIT 0,C";
        CY=obtenFlag("C",C);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;

case "CB49":
       INSTRUCCION="BIT 1,C";
        CY=obtenFlag("N",C);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
   
case "CB51":
       INSTRUCCION="BIT 2,C";
        CY=obtenFlag("P/V",C);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
   
case "CB59":
       INSTRUCCION="BIT 3,C";
        CY=obtenFlag("3",C);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
   
case "CB61":
       INSTRUCCION="BIT 4,C";
        CY=obtenFlag("H",C);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break; 

case "CB69":
       INSTRUCCION="BIT 5,C";
        CY=obtenFlag("5",C);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
   
case "CB71":
       INSTRUCCION="BIT 6,C";
        CY=obtenFlag("Z",C);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
        
case "CB79":
       INSTRUCCION="BIT 7,C";
        CY=obtenFlag("S",C);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
        
    //REGISTRO D
        
case "CB42":
       INSTRUCCION="BIT 0,D";
        CY=obtenFlag("C",D);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;

case "CB4A":
       INSTRUCCION="BIT 1,D";
        CY=obtenFlag("N",D);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
   
case "CB52":
       INSTRUCCION="BIT 2,D";
        CY=obtenFlag("P/V",D);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
   
case "CB5A":
       INSTRUCCION="BIT 3,D";
        CY=obtenFlag("3",D);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
case "CB62":
       INSTRUCCION="BIT 4,D";
        CY=obtenFlag("H",D);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break; 

case "CB6A":
       INSTRUCCION="BIT 5,D";
        CY=obtenFlag("5",D);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
   
case "CB72":
       INSTRUCCION="BIT 6,D";
        CY=obtenFlag("Z",D);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
        

case "CB7A":
       INSTRUCCION="BIT 7,D";
        CY=obtenFlag("S",D);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
        
    //REGISTRO E 
case "CB43":
       INSTRUCCION="BIT 0,E";
        CY=obtenFlag("C",E);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;

case "CB4B":
       INSTRUCCION="BIT 1,E";
        CY=obtenFlag("N",E);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
   
case "CB53":
       INSTRUCCION="BIT 2,E";
        CY=obtenFlag("P/V",E);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
   
case "CB5B":
       INSTRUCCION="BIT 3,E";
        CY=obtenFlag("3",E);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
   
case "CB63":
       INSTRUCCION="BIT 4,E";
        CY=obtenFlag("H",E);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break; 

case "CB6B":
       INSTRUCCION="BIT 5,E";
        CY=obtenFlag("5",E);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
   
case "CB73":
       INSTRUCCION="BIT 6,E";
        CY=obtenFlag("Z",E);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
        

case "CB7B":
       INSTRUCCION="BIT 7,E";
        CY=obtenFlag("S",E);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
        
    //REGISTRO H
case "CB44":
       INSTRUCCION="BIT 0,H";
        CY=obtenFlag("C",H);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;

case "CB4C":
       INSTRUCCION="BIT 1,H";
        CY=obtenFlag("N",H);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
   
case "CB54":
       INSTRUCCION="BIT 2,H";
        CY=obtenFlag("P/V",H);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
   
case "CB5C":
       INSTRUCCION="BIT 3,H";
        CY=obtenFlag("3",H);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
   
case "CB64":
       INSTRUCCION="BIT 4,H";
        CY=obtenFlag("H",H);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break; 

case "CB6C":
       INSTRUCCION="BIT 5,H";
        CY=obtenFlag("5",H);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
   
case "CB74":
       INSTRUCCION="BIT 6,H";
        CY=obtenFlag("Z",H);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
        

case "CB7C":
       INSTRUCCION="BIT 7,H";
        CY=obtenFlag("S",H);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;

    //REGISTRO L 
case "CB45":
       INSTRUCCION="BIT 0,L";
        CY=obtenFlag("C",L);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;

case "CB4D":
       INSTRUCCION="BIT 1,L";
        CY=obtenFlag("N",L);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
   
case "CB55":
       INSTRUCCION="BIT 2,L";
        CY=obtenFlag("P/V",L);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
   
case "CB5D":
       INSTRUCCION="BIT 3,L";
        CY=obtenFlag("3",L);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
   
case "CB65":
       INSTRUCCION="BIT 4,L";
        CY=obtenFlag("H",L);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break; 

case "CB6D":
       INSTRUCCION="BIT 5,L";
        CY=obtenFlag("5",L);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
   
case "CB75":
       INSTRUCCION="BIT 6,L";
        CY=obtenFlag("Z",L);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
        

case "CB7D":
       INSTRUCCION="BIT 7,L";
        CY=obtenFlag("S",L);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
        
    //REGISTRO WR=memo[HL]  
case "CB46":
       INSTRUCCION="BIT 0,(HL)";
          HL=obtenRC(H,L);
          WR=memo[HL];
               CY=obtenFlag("C",WR);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;

case "CB4E":
       INSTRUCCION="BIT 1,(HL)";
          HL=obtenRC(H,L);
          WR=memo[HL];
               CY=obtenFlag("N",WR);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
   
case "CB56":
       INSTRUCCION="BIT 2,(HL)";
          HL=obtenRC(H,L);
          WR=memo[HL];
               CY=obtenFlag("P/V",WR);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
   
case "CB5E":
       INSTRUCCION="BIT 3,(HL)";
          HL=obtenRC(H,L);
          WR=memo[HL];
               CY=obtenFlag("3",WR);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
   
case "CB66":
       INSTRUCCION="BIT 4,(HL)";
          HL=obtenRC(H,L);
          WR=memo[HL];
               CY=obtenFlag("H",WR);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break; 

case "CB6E":
       INSTRUCCION="BIT 5,(HL)";
          HL=obtenRC(H,L);
          WR=memo[HL];
               CY=obtenFlag("5",WR);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
   
case "CB76":
       INSTRUCCION="BIT 6,(HL)";
          HL=obtenRC(H,L);
          WR=memo[HL];
               CY=obtenFlag("Z",WR);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;
        

case "CB7E":
       INSTRUCCION="BIT 7,(HL)";
          HL=obtenRC(H,L);
          WR=memo[HL];
               CY=obtenFlag("S",WR);
        F=grabarFlag("H",'1',F);
        F=grabarFlag("N",'0',F);
        if(CY==0)
            F=grabarFlag("Z",'1',F);
        else
            F=grabarFlag("Z",'0',F);
        T=T+8;
        break;  
        
        //INICIAN INSTRUCCIONES DE RES 
        //REGISTRO A
case"CB87":
       INSTRUCCION="RES 0,A";
    A=grabarFlag("C",'0',A);
    T=T+8;
    break;
    
case"CB8F":
       INSTRUCCION="RES 1,A";
    A=grabarFlag("N",'0',A);
    T=T+8;
break;

case"CB97":
       INSTRUCCION="RES 2,A";
    A=grabarFlag("P/V",'0',A);
    T=T+8;
break;  
    

case"CB9F":
       INSTRUCCION="RES 3,A";
    A=grabarFlag("3",'0',A);
    T=T+8;
break;  


case"CBA7":
       INSTRUCCION="RES 4,A";
    A=grabarFlag("H",'0',A);
    T=T+8;
break;

case"CBAF":
       INSTRUCCION="RES 5,A";
    A=grabarFlag("5",'0',A);
    T=T+8;
break;

case"CBB7":
       INSTRUCCION="RES 6,A";
    A=grabarFlag("Z",'0',A);
    T=T+8;
break;

case"CBBF":
       INSTRUCCION="RES 7,A";
    A=grabarFlag("S",'0',A);
    T=T+8;
break;


//EMPIEZA REGISTRO B
case"CB80":
       INSTRUCCION="RES 0,B";
    B=grabarFlag("C",'0',B);
    T=T+8;
    break;
    
case"CB88":
       INSTRUCCION="RES 1,B";
    B=grabarFlag("N",'0',B);
    T=T+8;
break;

case"CB90":
       INSTRUCCION="RES 2,B";
    B=grabarFlag("P/V",'0',B);
    T=T+8;
break;  
    

case"CB98":
       INSTRUCCION="RES 3,B";
    B=grabarFlag("3",'0',B);
    T=T+8;
break;  


case"CBA0":
       INSTRUCCION="RES 4,B";
    B=grabarFlag("H",'0',B);
    T=T+8;
break;

case"CBA8":
       INSTRUCCION="RES 5,B";
    B=grabarFlag("5",'0',B);
    T=T+8;
break;

case"CBB0":
       INSTRUCCION="RES 6,B";
    B=grabarFlag("Z",'0',B);
    T=T+8;
break;

case"CBB8":
       INSTRUCCION="RES 7,B";
    B=grabarFlag("S",'0',B);
    T=T+8;
break;      
        

//EMPIEZA REGISTRO C
case"CB81":
       INSTRUCCION="RES 0,C";
    C=grabarFlag("C",'0',C);
    T=T+8;
    break;
    
case"CB89":
       INSTRUCCION="RES 1,C";
    C=grabarFlag("N",'0',C);
    T=T+8;
       break;

case"CB91":
    //RES 2,C
       INSTRUCCION="RES 2,C";
    C=grabarFlag("P/V",'0',C);
    T=T+8;
       break;   
    

case"CB99":
    INSTRUCCION="RES 3,C";
    C=grabarFlag("3",'0',C);
    T=T+8;
       break;   


case"CBA1":
    INSTRUCCION="RES 4,C";
    C=grabarFlag("H",'0',C);
    T=T+8;
       break;   


case"CBA9":
    INSTRUCCION="RES 5,C";
    C=grabarFlag("5",'0',C);
    T=T+8;
       break;

case"CBB1":
    INSTRUCCION="RES 6,C";
    C=grabarFlag("Z",'0',C);
    T=T+8;
       break;

case"CBB9":
    INSTRUCCION="RES 7,C";
    C=grabarFlag("S",'0',C);
    T=T+8;
       break;
    
    
//EMPIEZA REGISTRO D


case"CB82":
    INSTRUCCION="RES 0,D";
    D=grabarFlag("C",'0',D);
    T=T+8;
       break;
    
case"CB8A":
    INSTRUCCION="RES 1,D";
    D=grabarFlag("N",'0',D);
    T=T+8;
       break;

case"CB92":
    INSTRUCCION="RES 2,D";
    D=grabarFlag("P/V",'0',D);
    T=T+8;
       break;
    

case"CB9A":
    INSTRUCCION="RES 3,D";
    D=grabarFlag("3",'0',D);
    T=T+8;
       break;


case"CBA2":
    INSTRUCCION="RES 4,D";
    D=grabarFlag("H",'0',D);
    T=T+8;
       break;

case"CBAA":
    INSTRUCCION="RES 5,D";
    D=grabarFlag("5",'0',D);
    T=T+8;
       break;

case"CBB2":
    INSTRUCCION="RES 6,D";
    D=grabarFlag("Z",'0',D);
    T=T+8;
       break;

case"CBBA":
    INSTRUCCION="RES 7,D";
    D=grabarFlag("S",'0',D);
    T=T+8;
       break;


//EMPIEZA REGISTRO E

case"CB83":
    INSTRUCCION="RES 0,E";
    E=grabarFlag("C",'0',E);
    T=T+8;
       break;
    
case"CB8B":
    INSTRUCCION="RES 1,E";
    E=grabarFlag("N",'0',E);
    T=T+8;
       break;

case"CB93":
    INSTRUCCION="RES 2,E";
    E=grabarFlag("P/V",'0',E);
    T=T+8;
       break;
    

case"CB9B":
    INSTRUCCION="RES 3,E";
    E=grabarFlag("3",'0',E);
    T=T+8;
       break;


case"CBA3":
    INSTRUCCION="RES 4,E";
    E=grabarFlag("H",'0',E);
    T=T+8;
       break;

case"CBAB":
    INSTRUCCION="RES 5,E";
    E=grabarFlag("5",'0',E);
    T=T+8;
       break;

case"CBB3":
    INSTRUCCION="RES 6,E";
    E=grabarFlag("Z",'0',E);
    T=T+8;
       break;

case"CBBB":
    INSTRUCCION="RES 7,E";
    E=grabarFlag("S",'0',E);
    T=T+8;
       break;



//EMPIEZA REGISTRO H


case"CB84":
    INSTRUCCION="RES 0,H";
    H=grabarFlag("C",'0',H);
    T=T+8;
       break;
    
case"CB8C":
    INSTRUCCION="RES 1,H";
    H=grabarFlag("N",'0',H);
    T=T+8;
       break;

case"CB94":
    INSTRUCCION="RES 2,H";
    H=grabarFlag("P/V",'0',H);
    T=T+8;
       break;
    

case"CB9C":
    INSTRUCCION="RES 3,H";
    H=grabarFlag("3",'0',H);
    T=T+8;
       break;


case"CBA4":
    INSTRUCCION="RES 4,H";
    H=grabarFlag("H",'0',H);
    T=T+8;
       break;
case"CBAC":
    INSTRUCCION="RES 5,H";
    H=grabarFlag("5",'0',H);
    T=T+8;
       break;
case"CBB4":
    INSTRUCCION="RES 6,H";
    H=grabarFlag("Z",'0',H);
    T=T+8;
       break;
case"CBBC":
    INSTRUCCION="RES 7,H";
    H=grabarFlag("S",'0',H);
    T=T+8;
       break;

//EMPIEZA REGISTRO L

case"CB85":
    INSTRUCCION="RES 0,L";
    L=grabarFlag("C",'0',L);
    T=T+8;
       break;
    
case"CB8D":
    INSTRUCCION="RES 1,H";
    L=grabarFlag("N",'0',L);
    T=T+8;
       break;

case"CB95":
    INSTRUCCION="RES 2,H";
    L=grabarFlag("P/V",'0',L);
    T=T+8;
       break;
    

case"CB9D":
    INSTRUCCION="RES 3,H";
    L=grabarFlag("3",'0',L);
    T=T+8;
       break;


case"CBA5":
    INSTRUCCION="RES 4,H";
    L=grabarFlag("H",'0',L);
    T=T+8;
       break;

case"CBAD":
    INSTRUCCION="RES 5,H";
    L=grabarFlag("5",'0',L);
    T=T+8;
       break;

case"CBB5":
    INSTRUCCION="RES 6,H";
    L=grabarFlag("Z",'0',L);
    T=T+8;
       break;

case"CBBD":
    INSTRUCCION="RES 7,H";
    L=grabarFlag("S",'0',L);
    T=T+8;
       break;

//EMPIEZA REGISTRO (HL)

case"CB86":
    HL=obtenRC(H,L);
    WR=memo[HL];
    INSTRUCCION="RES 0,(HL)";
    WR=grabarFlag("C",'0',WR);
       memo[HL]=WR;
       T=T+15;
   break;
    
case"CB8E":
    HL=obtenRC(H,L);
    WR=memo[HL];
    INSTRUCCION="RES 1,(HL)";
    WR=grabarFlag("N",'0',WR);
       memo[HL]=WR;
       T=T+15;
   break;

case"CB96":
    HL=obtenRC(H,L);
    WR=memo[HL];
    INSTRUCCION="RES 2,(HL)";
    WR=grabarFlag("P/V",'0',WR);
       memo[HL]=WR;
       T=T+15;
       break;
    

case"CB9E":
    HL=obtenRC(H,L);
    WR=memo[HL];
    INSTRUCCION="RES 3,(HL)";
    WR=grabarFlag("3",'0',WR);
       memo[HL]=WR;
       T=T+15;
       break;


case"CBA6":
    HL=obtenRC(H,L);
    WR=memo[HL];
    INSTRUCCION="RES 4,(HL)";
    WR=grabarFlag("H",'0',WR);
       memo[HL]=WR;
       T=T+15;
       break;

case"CBAE":
    HL=obtenRC(H,L);
    WR=memo[HL];
    INSTRUCCION="RES 5,(HL)";
    WR=grabarFlag("5",'0',WR);
       memo[HL]=WR;
       T=T+15;
       break;
   

case"CBB6":
    HL=obtenRC(H,L);
    WR=memo[HL];
    INSTRUCCION="RES 6,(HL)";
    WR=grabarFlag("Z",'0',WR);
       memo[HL]=WR;
       T=T+15;
       break;
case"CBBE":
    HL=obtenRC(H,L);
    WR=memo[HL];
    INSTRUCCION="RES 7,(HL)";
    WR=grabarFlag("S",'0',WR);
       memo[HL]=WR;
       T=T+15;
       break;


//EMPIEZA SET
//EMPIEZA REGISTRO A


case"CBC7":
    INSTRUCCION="SET 0,A";
    A=grabarFlag("C",'1',A);
    T=T+8;
       break;
    
case"CBCF":
    INSTRUCCION="SET 1,A";
    A=grabarFlag("N",'1',A);
    T=T+8;
       break;

case"CBD7":
    INSTRUCCION="SET 2,A";
    A=grabarFlag("P/V",'1',A);
    T=T+8;
       break;
    

case"CBDF":
    INSTRUCCION="SET 3,A";
    A=grabarFlag("3",'1',A);
    T=T+8;
       break;


case"CBE7":
    INSTRUCCION="SET 4,A";
    A=grabarFlag("H",'1',A);
    T=T+8;
       break;
case"CBEF":
    INSTRUCCION="SET 5,A";
    A=grabarFlag("5",'1',A);
    T=T+8;
       break;

case"CBF7":
    INSTRUCCION="SET 6,A";
    A=grabarFlag("Z",'1',A);
    T=T+8;
       break;
case"CBFF":
    INSTRUCCION="SET 7,A";
    A=grabarFlag("S",'1',A);
    T=T+8;
       break;

//EMPIEZA REGISTRO B


case"CBC0":
    INSTRUCCION="SET 0,B";
    B=grabarFlag("C",'1',B);
    T=T+8;
       break;
    
case"CBC8":
    INSTRUCCION="SET 1,B";
    B=grabarFlag("N",'1',B);
    T=T+8;
       break;

case"CBD0":
    INSTRUCCION="SET 2,B";
    B=grabarFlag("P/V",'1',B);
    T=T+8;
       break;

case"CBD8":
    INSTRUCCION="SET 3,B";
    B=grabarFlag("3",'1',B);
    T=T+8;
       break;

case"CBE0":
    INSTRUCCION="SET 4,B";
    B=grabarFlag("H",'1',B);
    T=T+8;
       break;

case"CBE8":
    INSTRUCCION="SET 5,B";
    B=grabarFlag("5",'1',B);
    T=T+8;
       break;

case"CBF0":
    INSTRUCCION="SET 6,B";
    B=grabarFlag("Z",'1',B);
    T=T+8;
       break;

case"CBF8":
    INSTRUCCION="SET 7,B";
    B=grabarFlag("S",'1',B);
    T=T+8;
       break;
    


//EMPIEZA REGISTRO C


case"CBC1":
    INSTRUCCION="SET 0,C";
    C=grabarFlag("C",'1',C);
    T=T+8;
       break;
    
case"CBC9":
    INSTRUCCION="SET 1,C";
    C=grabarFlag("N",'1',C);
    T=T+8;
       break;
    
case"CBD1":
    INSTRUCCION="SET 2,C";
    C=grabarFlag("P/V",'1',C);
    T=T+8;
       break;

    
    

case"CBD9": 
    INSTRUCCION="SET 3,C";
    C=grabarFlag("3",'1',C);
    T=T+8;
       break;

case"CBAE1":
    INSTRUCCION="SET 4,C";
    C=grabarFlag("H",'1',C);
    T=T+8;
       break;
    

case"CBE9":
    INSTRUCCION="SET 5,C";
    C=grabarFlag("5",'1',C);
    T=T+8;
       break;
    

case"CBF1":
    INSTRUCCION="SET 6,C";
    C=grabarFlag("Z",'1',C);
    T=T+8;
       break;

case"CBF9":
    INSTRUCCION="SET 7,C";
    C=grabarFlag("S",'1',C);
    T=T+8;
       break;   
    
    
//EMPIEZA REGISTRO D


case"CBC2":
    INSTRUCCION="SET 0,D";
    D=grabarFlag("C",'1',D);
    T=T+8;
       break;
    
case"CBCA":
    INSTRUCCION="SET 1,D";
    D=grabarFlag("N",'1',D);
    T=T+8;
       break;

case"CBD2":
    INSTRUCCION="SET 2,D";
    C=grabarFlag("P/V",'1',D);
    T=T+8;
       break;
    

case"CBDA":
    INSTRUCCION="SET 3,D";
    D=grabarFlag("3",'1',D);
    T=T+8;
       break;


case"CBE2":
    INSTRUCCION="SET 4,D";
    D=grabarFlag("H",'1',D);
    T=T+8;
       break;

case"CBEA":
    INSTRUCCION="SET 5,D";
    D=grabarFlag("5",'1',D);
    T=T+8;
       break;

case"CBF2":
    INSTRUCCION="SET 6,D";
    D=grabarFlag("N",'1',D);
    T=T+8;
       break;

case"CBFA":
    INSTRUCCION="SET 7,D";
    D=grabarFlag("S",'1',D);
    T=T+8;
       break;

//EMPIEZA REGISTRO E

case"CBC3":
    INSTRUCCION="SET 0,E";
    E=grabarFlag("C",'1',E);
    T=T+8;
       break;
    
case"CBCB":
    INSTRUCCION="SET 1,E";
    E=grabarFlag("N",'1',E);
    T=T+8;
       break;

case"CBD3":
    INSTRUCCION="SET 2,E";
    E=grabarFlag("P/V",'1',E);
    T=T+8;
       break;
case"CBDB":
    INSTRUCCION="SET 3,E";
    E=grabarFlag("3",'1',E);
    T=T+8;
       break;

case"CBE3":
    INSTRUCCION="SET 4,E";
    E=grabarFlag("H",'1',E);
    T=T+8;
       break;

case"CBEB":
    INSTRUCCION="SET 5,E";
    E=grabarFlag("5",'1',E);
    T=T+8;
       break;

case"CBF3":
    INSTRUCCION="SET 6,E";
    E=grabarFlag("Z",'1',E);
    T=T+8;
       break;

case"CBFB":
    INSTRUCCION="SET 7,E";
    E=grabarFlag("S",'1',E);
    T=T+8;
       break;



//EMPIEZA REGISTRO H


case"CBC4":
    INSTRUCCION="SET 0,H";
    H=grabarFlag("C",'1',H);
    T=T+8;
       break;
    
case"CBCC":
    INSTRUCCION="SET 1,H";
    H=grabarFlag("N",'1',H);
    T=T+8;
       break;

case"CBD4":
    INSTRUCCION="SET 2,H";
    H=grabarFlag("P/V",'1',H);
    T=T+8;
       break;
    

case"CBDC":
    INSTRUCCION="SET 3,H";
    H=grabarFlag("3",'1',H);
    T=T+8;
       break;

case"CBE4":
    INSTRUCCION="SET 4,H";
    H=grabarFlag("H",'1',H);
    T=T+8;
       break;

case"CBEC":
    INSTRUCCION="SET 5,H";
    H=grabarFlag("5",'1',H);
    T=T+8;
       break;

case"CBF4":
    INSTRUCCION="SET 6,H";
    H=grabarFlag("Z",'1',H);
    T=T+8;
       break;

case"CBFC":
    INSTRUCCION="SET 7,H";
    H=grabarFlag("S",'1',H);
    T=T+8;
       break;

//EMPIEZA REGISTRO L

case"CBC5":
    INSTRUCCION="SET 0,L";
    L=grabarFlag("C",'1',L);
    T=T+8;
       break;
    
case"CBCD":
    INSTRUCCION="SET 1,L";
    L=grabarFlag("N",'1',L);
    T=T+8;
       break;

case"CBD5":
    INSTRUCCION="SET 2,L";
    L=grabarFlag("P/V",'1',L);
    T=T+8;
       break;

case"CBDD":
    INSTRUCCION="SET 3,L";
    L=grabarFlag("3",'1',L);
    T=T+8;
       break;

case"CBE5":
    INSTRUCCION="SET 4,L";
    L=grabarFlag("H",'1',L);
    T=T+8;
       break;

case"CBED":
    INSTRUCCION="SET 5,L";
    L=grabarFlag("5",'1',L);
    T=T+8;
       break;

case"CBF5":
    INSTRUCCION="SET 6,L";
    L=grabarFlag("Z",'1',L);
    T=T+8;
       break;

case"CBFD":
    INSTRUCCION="SET 7,L";
    L=grabarFlag("S",'1',L);
    T=T+8;
       break;

//EMPIEZA REGISTRO (HL)

case"CBC6":
        //SET 0,(HL)
    HL=obtenRC(H,L);
    WR=memo[HL];
    INSTRUCCION="SET 0,(HL)";
    WR=grabarFlag("C",'1',WR);
       memo[HL]=WR;
       T=T+15;
       break;
    
case"CBCE":
    HL=obtenRC(H,L);
    WR=memo[HL];
    INSTRUCCION="SET 1,(HL)";
    WR=grabarFlag("N",'1',WR);
       memo[HL]=WR;
       T=T+15;
       break;
case"CBD6":
    HL=obtenRC(H,L);
    WR=memo[HL];
    INSTRUCCION="SET 2,(HL)";
    WR=grabarFlag("P/V",'1',WR);
       memo[HL]=WR;
       T=T+15;
       break;
    

case"CBDE":
    HL=obtenRC(H,L);
    WR=memo[HL];
    INSTRUCCION="SET 3,(HL)";
    WR=grabarFlag("3",'1',WR);
       memo[HL]=WR;
       T=T+15;
       break;


case"CBE6":
    HL=obtenRC(H,L);
    WR=memo[HL];
    INSTRUCCION="SET 4,(HL)";
    WR=grabarFlag("H",'1',WR);
       memo[HL]=WR;
       T=T+15;
       break;

case"CBEE":
    HL=obtenRC(H,L);
    WR=memo[HL];
    INSTRUCCION="SET 5,(HL)";
    WR=grabarFlag("5",'1',WR);
       memo[HL]=WR;
       T=T+15;
       break;

case"CBF6":
    HL=obtenRC(H,L);
    WR=memo[HL];
    INSTRUCCION="SET 6,(HL)";
    WR=grabarFlag("Z",'1',WR);
       memo[HL]=WR;
       T=T+15;
       break;
case"CBFE":
    HL=obtenRC(H,L);
    WR=memo[HL];
    INSTRUCCION="SET 7,(HL)";
    WR=grabarFlag("S",'1',WR);
       memo[HL]=WR;
       T=T+15;
       break;
   
   
   //LOS JP
   
   
 case"DDE9":    //JP (IX)
     INSTRUCCION="JP (IX)";
     sn=AjusteHEX(memo[IX+1])+AjusteHEX(memo[IX]);
     nn=Integer.parseInt(sn,16);
     PC=nn-1;
     T=T+8;
     break;
     
    
   case"FDE9":  //JP (IY)
     INSTRUCCION="JP (IY)";
     sn=AjusteHEX(memo[IY+1])+AjusteHEX(memo[IY]);
     nn=Integer.parseInt(sn,16);
     PC=nn-1;
     T=T+8;
     break;
     
   case"ED4D":
       INSTRUCCION="RETI(RETORNO DE INTERRUPCION)";
       T=T+14;
        break;
      case"ED45":
       INSTRUCCION="RETN(RETORNO DE INTERRUPCION NO ENMASCARABLES)";
       T=T+14;
        break; 
       
case "DD7E":
    //LD A,(IX+ds)
    INSTRUCCION="LD A,(IX+"+AjusteHEX(memo[PC+1])+")";
    PC=PC+1;
    n=memo[PC];
   if(n>127)
    n=n-256;
   A=memo[IX+n];
   T=T+19;
   CL++;
    break;
    
case"DD46":
    INSTRUCCION="LD B,(IX+"+AjusteHEX(memo[PC+1])+")";
    PC=PC+1;
    n=memo[PC];
   if(n>127)
    n=n-256;
   B=memo[IX+n];
    T=T+19;
    CL++;
    break;

case"DD4E":
    INSTRUCCION="LD C,(IX+"+AjusteHEX(memo[PC+1])+")";
    PC=PC+1;
    n=memo[PC];
   if(n>127)
    n=n-256;
   C=memo[IX+n];
    T=T+19;
    CL++;
    break;


case"DD56":
    INSTRUCCION="LD D,(IX+"+AjusteHEX(memo[PC+1])+")";
    PC=PC+1;
    n=memo[PC];
   if(n>127)
    n=n-256;
   D=memo[IX+n];
    T=T+19;
    CL++;
    break;
    
case"DD5E":
    INSTRUCCION="LD E,(IX+"+AjusteHEX(memo[PC+1])+")";
    PC=PC+1;
    n=memo[PC];
   if(n>127)
    n=n-256;
   E=memo[IX+n];
    T=T+19;
    CL++;
    break;
    
    
case"DD66":
    INSTRUCCION="LD H,(IX+"+AjusteHEX(memo[PC+1])+")";
    PC=PC+1;
    n=memo[PC];
   if(n>127)
    n=n-256;
   H=memo[IX+n];
    T=T+19;
    CL++;
    break;
    
case"DD6E":
    INSTRUCCION="LD L,(IX+"+AjusteHEX(memo[PC+1])+")";
    PC=PC+1;
    n=memo[PC];
   if(n>127)
    n=n-256;
   L=memo[IX+n];
    T=T+19;
    CL++;
    break;
    
case"FD7E":
    INSTRUCCION="LD A,(IY+"+AjusteHEX(memo[PC+1])+")";
    PC=PC+1;
    n=memo[PC];
   if(n>127)
    n=n-256;
   A=memo[IY+n];
    T=T+19;
    CL++;
    break;
    
case"FD46":
    INSTRUCCION="LD B,(IY+"+AjusteHEX(memo[PC+1])+")";
    PC=PC+1;
    n=memo[PC];
   if(n>127)
    n=n-256;
   B=memo[IY+n];
    T=T+19;
    CL++;
    break;
    
case"FD4E":
    INSTRUCCION="LD C,(IY+"+AjusteHEX(memo[PC+1])+")";
    PC=PC+1;
    n=memo[PC];
   if(n>127)
    n=n-256;
   C=memo[IY+n];
    T=T+19;
    CL++;
    break;
    
    
case"FD56":
    INSTRUCCION="LD D,(IY+"+AjusteHEX(memo[PC+1])+")";
    PC=PC+1;
    n=memo[PC];
   if(n>127)
    n=n-256;
   D=memo[IY+n];
    T=T+19;
    CL++;
    break;
    
case"FD5E":
    INSTRUCCION="LD E,(IY+"+AjusteHEX(memo[PC+1])+")";
    PC=PC+1;
    n=memo[PC];
   if(n>127)
    n=n-256;
   E=memo[IY+n];
    T=T+19;
    CL++;
    break;
    
case"FD66":
    INSTRUCCION="LD H,(IY+"+AjusteHEX(memo[PC+1])+")";
    PC=PC+1;
    n=memo[PC];
   if(n>127)
    n=n-256;
   H=memo[IY+n];
    T=T+19;
    CL++;
    break;
    
case"FD6E":
    INSTRUCCION="LD L,(IY++"+AjusteHEX(memo[PC+1])+")";
    PC=PC+1;
    n=memo[PC];
   if(n>127)
    n=n-256;
   L=memo[IY+n];
    T=T+19;
    CL++;
    break;
    
////
case "DD77":
    //LD (IX+ds),A,;
    INSTRUCCION="LD (IX+"+AjusteHEX(memo[PC+1])+"),A";
    PC=PC+1;
    n=memo[PC];
   if(n>127)
    n=n-256;
   memo[IX+n]=A;
   T=T+19;
    CL++;
    break;  
    
case"DD70":
    INSTRUCCION="LD (IX+"+AjusteHEX(memo[PC+1])+"),B";
    PC=PC+1;
    n=memo[PC];
   if(n>127)
    n=n-256;
   memo[IX+n]=B;
    T=T+19;
    CL++;
    break;

case"DD71":
    INSTRUCCION="LD (IX+"+AjusteHEX(memo[PC+1])+"),C";
    PC=PC+1;
    n=memo[PC];
   if(n>127)
    n=n-256;
  memo[IX+n]=C;
    T=T+19;
    CL++;
    break;


case"DD72":
    INSTRUCCION="LD (IX+"+AjusteHEX(memo[PC+1])+"),D";
    PC=PC+1;
    n=memo[PC];
   if(n>127)
    n=n-256;
   memo[IX+n]=D;
    T=T+19;
    CL++;
    break;
    
case"DD73":
    INSTRUCCION="LD(IX+"+AjusteHEX(memo[PC+1])+"),E";
    PC=PC+1;
    n=memo[PC];
   if(n>127)
    n=n-256;
   memo[IX+n]=E;
    T=T+19;
    CL++;
    break;
    
    
case"DD74":
    INSTRUCCION="LD (IX+"+AjusteHEX(memo[PC+1])+"),H";
    PC=PC+1;
    n=memo[PC];
   if(n>127)
    n=n-256;
   memo[IX+n]=H;
    T=T+19;
    CL++;
    break;
    
case"DD75":
    INSTRUCCION="LD (IX+"+AjusteHEX(memo[PC+1])+"),L";
    PC=PC+1;
    n=memo[PC];
   if(n>127)
    n=n-256;
   memo[IX+n]=L;
    T=T+19;
    CL++;
    break;

case "DD36":
    INSTRUCCION="LD (IX+"+AjusteHEX(memo[PC+1])+","+AjusteHEX(memo[PC+2])+")";
    PC=PC+1;
    n=memo[PC];
   if(n>127)
    n=n-256;
   PC=PC+1;
   memo[IX+n]=memo[PC];
   CL=CL+2;
   T=T+19;
    break;

case "FD36":    
    INSTRUCCION="LD (IY+"+AjusteHEX(memo[PC+1])+","+AjusteHEX(memo[PC+2])+")";
    PC=PC+1;
    n=memo[PC];
   if(n>127)
    n=n-256;
   PC=PC+1;
   memo[IY+n]=memo[PC];
   CL=CL+2;
   T=T+19;
    break;
    
case"FD77":
    INSTRUCCION="LD (IY+"+AjusteHEX(memo[PC+1])+"),A";
    PC=PC+1;
    n=memo[PC];
   if(n>127)
    n=n-256;
   memo[IY+n]=A;
    T=T+19;
    CL++;
    break;
    
case"FD70":
    INSTRUCCION="LD (IY+"+AjusteHEX(memo[PC+1])+")B";
    PC=PC+1;
    n=memo[PC];
   if(n>127)
    n=n-256;
   memo[IY+n]=B;
    T=T+19;
    CL++;
    break;
    
case"FD71":
    PC=PC+1;
    n=memo[PC];
   if(n>127)
    n=n-256;
   memo[IY+n]=C;
   INSTRUCCION="LD (IY+"+n+"),C";
    T=T+19;
    CL++;
    break;
    
    
case"FD72":
    INSTRUCCION="LD (IY+"+AjusteHEX(memo[PC+1])+"),D";
    PC=PC+1;
    n=memo[PC];
   if(n>127)
    n=n-256;
   memo[IY+n]=D;
    T=T+19;
    CL++;
    break;
    
case"FD73":
    INSTRUCCION="LD (IY+"+AjusteHEX(memo[PC+1])+"),E";
    PC=PC+1;
    n=memo[PC];
   if(n>127)
    n=n-256;
   memo[IY+n]=E;
    T=T+19;
   CL++;
    break;
    
case"FD74":
    INSTRUCCION="LD (IY+"+AjusteHEX(memo[PC+1])+"),H";
    PC=PC+1;
    n=memo[PC];
   if(n>127)
    n=n-256;
   memo[IY+n]=H;
    T=T+19;
   CL++;
    break;
    
case"FD75":
    INSTRUCCION="LD (IY+"+AjusteHEX(memo[PC+1])+"),L";
    PC=PC+1;
    n=memo[PC];
   if(n>127)
    n=n-256;
   memo[IY+n]=L;
    T=T+19;
   CL++;
    break;
    
case"DD21": //LD IX,nn
    INSTRUCCION="LD IX,"+AjusteHEX(memo[PC+2])+AjusteHEX(memo[PC+1]);
    PC=PC+2;
   sn=AjusteHEX(memo[PC])+AjusteHEX(memo[PC-1]);
   nn=Integer.parseInt(sn,16);
   IX=nn;
   CL=CL+2;
   T=T+14;
   break;
   
case"FD21":
    INSTRUCCION="LD IY,"+AjusteHEX(memo[PC+2])+AjusteHEX(memo[PC+1]);
    PC=PC+2;
   sn=AjusteHEX(memo[PC])+AjusteHEX(memo[PC-1]);
   nn=Integer.parseInt(sn,16);
   IY=nn;
   CL=CL+2;
   T=T+14;
   break;  
   
case"ED4B": //LD DD,(nn)
    INSTRUCCION="LD BC,("+AjusteHEX(memo[PC+2])+AjusteHEX(memo[PC+1])+")";
   BC=obtenRC(B,C);
    PC=PC+2;
   sn=AjusteHEX(memo[PC])+AjusteHEX(memo[PC-1]);
   nn=Integer.parseInt(sn,16);
   C=memo[nn]; 
   B=memo[nn+1];
   CL=CL+2;
   T=T+20;
   break;
    
   
case"ED5B": //LD DD,(nn)
    INSTRUCCION="LD DE,("+AjusteHEX(memo[PC+2])+AjusteHEX(memo[PC+1])+")";
   DE=obtenRC(D,E);
    PC=PC+2;
   sn=AjusteHEX(memo[PC])+AjusteHEX(memo[PC-1]);
   nn=Integer.parseInt(sn,16);
   E=memo[nn]; 
   D=memo[nn+1];
   CL=CL+2;
   T=T+20;
   break;

case "ED7B":
//  //LD SP,(nn)";
    INSTRUCCION="LD SP,("+AjusteHEX(memo[PC+2])+AjusteHEX(memo[PC+1])+")";
    PC=PC+2;
   sn=AjusteHEX(memo[PC])+AjusteHEX(memo[PC-1]);
   nn=Integer.parseInt(sn,16);
   sn=AjusteHEX(memo[nn+1])+AjusteHEX(memo[nn]);
   nn=Integer.parseInt(sn,16);
   SP=nn;   
   CL=CL+2;
   T=T+20;
   break;
   
case"DD2A":
    INSTRUCCION="LD IX,("+AjusteHEX(memo[PC+2])+AjusteHEX(memo[PC+1])+")";
    PC=PC+2;
   sn=AjusteHEX(memo[PC])+AjusteHEX(memo[PC-1]);
   nn=Integer.parseInt(sn,16);
   sn=Integer.toHexString(memo[nn]);
   cn=sn.toCharArray();
   while (cn.length!=2){
   sn="0"+sn; //Low
   cn=sn.toCharArray();
   }
   sn=Integer.toHexString(memo[nn+1])+sn;
   IX=Integer.parseInt(sn,16);
   CL=CL+2;
   T=T+20;
   break;
   
case"FD2A":
    INSTRUCCION="LD IY,("+AjusteHEX(memo[PC+2])+AjusteHEX(memo[PC+1])+")";
    PC=PC+2;
   sn=AjusteHEX(memo[PC])+AjusteHEX(memo[PC-1]);
   nn=Integer.parseInt(sn,16);

   sn=Integer.toHexString(memo[nn]);
   cn=sn.toCharArray();
   while (cn.length!=2){
   sn="0"+sn; //Low
   cn=sn.toCharArray();
   }
   sn=Integer.toHexString(memo[nn+1])+sn;

   IY=Integer.parseInt(sn,16);
   CL=CL+2;
   T=T+20;
   break; 
   
   
///////////////////////
   
case"ED43":
    INSTRUCCION="LD (nn),BC";
    INSTRUCCION="LD ("+AjusteHEX(memo[PC+2])+AjusteHEX(memo[PC+1])+"),BC";
   BC=obtenRC(B,C);
    PC=PC+1;
    n=memo[PC];
   sn=Integer.toHexString(n);
   cn=sn.toCharArray();
   while (cn.length!=2){
   sn="0"+sn; //Low
   cn=sn.toCharArray();
   }
   PC=PC+1;
   n=memo[PC];
   sn=Integer.toHexString(n)+sn;
   nn=Integer.parseInt(sn,16);
   memo[nn]=C; 
   memo[nn+1]=B;
   CL=CL+2;
   T=T+20;
   break;
    
   
case"ED53":
    INSTRUCCION="LD ("+AjusteHEX(memo[PC+2])+AjusteHEX(memo[PC+1])+"),DE";
   DE=obtenRC(D,E);
    PC=PC+1;
    n=memo[PC];
   sn=Integer.toHexString(n);
   cn=sn.toCharArray();
   while (cn.length!=2){
   sn="0"+sn; //Low
   cn=sn.toCharArray();
   }
   PC=PC+1;
   n=memo[PC];
   sn=Integer.toHexString(n)+sn;
   nn=Integer.parseInt(sn,16);
   memo[nn]=E; 
   memo[nn+1]=D;
   CL=CL+2;
   T=T+20;
   break;
   
case"DD22":
    INSTRUCCION="LD ("+AjusteHEX(memo[PC+2])+AjusteHEX(memo[PC+1])+"),IX";
   PC=PC+2;
   sn=AjusteHEX(memo[PC])+AjusteHEX(memo[PC-1]);
   nn=Integer.parseInt(sn,16);
   
   memo[nn]=obtenRDL(IX);
   memo[nn+1]=obtenRDH(IX);
   CL=CL+2;
   T=T+20;
   break;
   
case"FD22":
    INSTRUCCION="LD ("+AjusteHEX(memo[PC+2])+AjusteHEX(memo[PC+1])+"),IY";
   PC=PC+2;
   sn=AjusteHEX(memo[PC])+AjusteHEX(memo[PC-1]);
   nn=Integer.parseInt(sn,16);
   
   memo[nn]=obtenRDL(IY);
   memo[nn+1]=obtenRDH(IY);
   CL=CL+2;
   T=T+20;
   break; 
   
   
case"DD86": //ADD (IX +n)
    INSTRUCCION="ADD (IX+"+AjusteHEX(memo[PC+1])+")";
       PC=PC+1;
       n=memo[PC];
       A=A+memo[IX+n];
       F=determinarFlag("C",A,F);
       F=determinarFlag("Z",A,F);
       F=determinarFlag("S",A,F);
       F=determinarFlag("H",A,F);
       F=determinarFlag("P/V",A,F);
       F=grabarFlag("N",'0',F);
       CL++;
    T=T+19;
    break;

case"DD8E":
   INSTRUCCION="ADC (IX+"+AjusteHEX(memo[PC+1])+")";
       PC=PC+1;
       n=memo[PC];
       CY=obtenFlag("C",F);
       A=A+memo[IX+n]+CY;
       F=determinarFlag("C",A,F);
       F=determinarFlag("Z",A,F);
       F=determinarFlag("S",A,F);
       F=determinarFlag("H",A,F);
       F=determinarFlag("P/V",A,F);
       F=grabarFlag("N",'0',F);        
       CL++;
    T=T+19;
    break;
   
case"DD96": //SUB (IX+n)
   INSTRUCCION="SUB (IX+"+AjusteHEX(memo[PC+1])+")";
       PC=PC+1;
       n=memo[PC];
       A=A-memo[IX+n];
       F=determinarFlag("C",A,F);
       F=determinarFlag("Z",A,F);
       F=determinarFlag("S",A,F);
       F=determinarFlag("H",A,F);
       F=determinarFlag("P/V",A,F);
       F=grabarFlag("N",'1',F);        
       CL++;
    T=T+19;
   break;
   
case"DD9E":
   INSTRUCCION="SBC (IX+"+AjusteHEX(memo[PC+1])+")";
       PC=PC+1;
       n=memo[PC];
       CY=obtenFlag("C",F);
       A=A-memo[IX+n]-CY;
       F=determinarFlag("C",A,F);
       F=determinarFlag("Z",A,F);
       F=determinarFlag("S",A,F);
       F=determinarFlag("H",A,F);
       F=determinarFlag("P/V",A,F);
       F=grabarFlag("N",'1',F);        
       CL++;
    T=T+19;
   break;
   
case"DDA6":
   INSTRUCCION="AND (IX+"+AjusteHEX(memo[PC+1])+")";
   PC=PC+1;
   n=memo[PC];
               A=AND(A,memo[IX+n]);
               WR=A;
               F=grabarFlag("C",'0',F);
               F=grabarFlag("N",'0',F);
               F=determinarFlag("Z",WR,F);
               F=determinarFlag("P",WR,F);
               F=determinarFlag("S",WR,F);
               F=determinarFlag("H",WR,F);
       CL++;
               T=T+19;
   break;
   
case"DDAE":
   INSTRUCCION="XOR (IX+"+AjusteHEX(memo[PC+1])+")";
   PC=PC+1;
   n=memo[PC];
               A=XOR(A,memo[IX+n]);
               WR=A;
               F=grabarFlag("C",'0',F);
               F=grabarFlag("N",'0',F);
               F=determinarFlag("Z",WR,F);
               F=determinarFlag("P",WR,F);
               F=determinarFlag("S",WR,F);
               F=determinarFlag("H",WR,F);
       CL++;
               T=T+19;
   break;
   
case"DDB6":
   INSTRUCCION="OR (IX+"+AjusteHEX(memo[PC+1])+")";
   PC=PC+1;
   n=memo[PC];
               A=OR(A,memo[IX+n]);
               WR=A;
               F=grabarFlag("C",'0',F);
               F=grabarFlag("N",'0',F);
               F=determinarFlag("Z",WR,F);
               F=determinarFlag("P",WR,F);
               F=determinarFlag("S",WR,F);
       CL++;
               F=determinarFlag("H",WR,F);
               T=T+19;
   break;
   
case"DDBE":
   INSTRUCCION="CP (IX+"+AjusteHEX(memo[PC+1])+")";
   PC=PC+1;
   n=memo[PC];
   WR=A-memo[IX+n];
               F=grabarFlag("N",'1',F);
               F=determinarFlag("C",WR,F);
               F=determinarFlag("S",WR,F);
               F=determinarFlag("Z",WR,F);
               F=determinarFlag("H",WR,F);
               F=determinarFlag("P/V",WR,F);                
       CL++;
   T=T+19;
   break;
   
case"DD34":
   INSTRUCCION="INC (IX+"+AjusteHEX(memo[PC+1])+")";
   PC=PC+1;
   n=memo[PC];
   memo[IX+n]=memo[IX+n]+1;
               F=grabarFlag("N",'0',F);
               F=determinarFlag("S",memo[IX+n],F);
               F=determinarFlag("Z",memo[IX+n],F);
               F=determinarFlag("H",memo[IX+n],F);
               F=determinarFlag("P/V",memo[IX+n],F);                
       CL++;
   T=T+23;
   break;
   
case"DD35":
   INSTRUCCION="DEC (IX+"+AjusteHEX(memo[PC+1])+")";
   PC=PC+1;
   n=memo[PC];
   memo[IX+n]=memo[IX+n]-1;
               F=grabarFlag("N",'1',F);
               F=determinarFlag("S",memo[IX+n],F);
               F=determinarFlag("Z",memo[IX+n],F);
               F=determinarFlag("H",memo[IX+n],F);
               F=determinarFlag("P/V",memo[IX+n],F);                
       CL++;
   T=T+23;
   break;
   
case"FD86": //LD (IY + n)
   INSTRUCCION="ADD (IY+"+AjusteHEX(memo[PC+1])+")";
       PC=PC+1;
       n=memo[PC];
       A=A+memo[IY+n];
       F=determinarFlag("C",A,F);
       F=determinarFlag("Z",A,F);
       F=determinarFlag("S",A,F);
       F=determinarFlag("H",A,F);
       F=determinarFlag("P/V",A,F);
       F=grabarFlag("N",'0',F);        
       CL++;
    T=T+19;
   break;
   
case"FD8E":
   INSTRUCCION="ADC (IY+"+AjusteHEX(memo[PC+1])+")";
       PC=PC+1;
       n=memo[PC];
       CY=obtenFlag("C",F);
       A=A+memo[IY+n]+CY;
       F=determinarFlag("C",A,F);
       F=determinarFlag("Z",A,F);
       F=determinarFlag("S",A,F);
       F=determinarFlag("H",A,F);
       F=determinarFlag("P/V",A,F);
       F=grabarFlag("N",'0',F);        
       CL++;
    T=T+19;
   break;
   
case"FD96": //SUB (IY+n)
   INSTRUCCION="SUB (IY+"+AjusteHEX(memo[PC+1])+")";
       PC=PC+1;
       n=memo[PC];
       A=A-memo[IY+n];
       F=determinarFlag("C",A,F);
       F=determinarFlag("Z",A,F);
       F=determinarFlag("S",A,F);
       F=determinarFlag("H",A,F);
       F=determinarFlag("P/V",A,F);
       F=grabarFlag("N",'0',F);        
       CL++;
    T=T+19;
   break;
   
case"FD9E":
   INSTRUCCION="SBC (IY+"+AjusteHEX(memo[PC+1])+")";
       PC=PC+1;
       n=memo[PC];
       CY=obtenFlag("C",F);
       A=A-memo[IY+n]-CY;
       F=determinarFlag("C",A,F);
       F=determinarFlag("Z",A,F);
       F=determinarFlag("S",A,F);
       F=determinarFlag("H",A,F);
       F=determinarFlag("P/V",A,F);
       F=grabarFlag("N",'0',F);        
       CL++;
    T=T+19;
   break;
   
case"FDA6":
   INSTRUCCION="AND (IY+"+AjusteHEX(memo[PC+1])+")";
   PC=PC+1;
   n=memo[PC];
               A=AND(A,memo[IY+n]);
               WR=A;
               F=grabarFlag("C",'0',F);
               F=grabarFlag("N",'0',F);
               F=determinarFlag("Z",WR,F);
               F=determinarFlag("P",WR,F);
               F=determinarFlag("S",WR,F);
               F=determinarFlag("H",WR,F);
       CL++;
               T=T+19;
   break;
   
case"FDAE":
   INSTRUCCION="XOR (IY+"+AjusteHEX(memo[PC+1])+")";
   PC=PC+1;
   n=memo[PC];
               A=XOR(A,memo[IY+n]);
               WR=A;
               F=grabarFlag("C",'0',F);
               F=grabarFlag("N",'0',F);
               F=determinarFlag("Z",WR,F);
               F=determinarFlag("P",WR,F);
               F=determinarFlag("S",WR,F);
               F=determinarFlag("H",WR,F);
               T=T+19;
   break;
   
case"FDB6":
   INSTRUCCION="OR (IY+"+AjusteHEX(memo[PC+1])+")";
   PC=PC+1;
   n=memo[PC];
               A=OR(A,memo[IY+n]);
               WR=A;
               F=grabarFlag("C",'0',F);
               F=grabarFlag("N",'0',F);
               F=determinarFlag("Z",WR,F);
               F=determinarFlag("P",WR,F);
               F=determinarFlag("S",WR,F);
               F=determinarFlag("H",WR,F);
       CL++;
               T=T+19;
   break;
   
case"FDBE":
   INSTRUCCION="CP (IY+"+AjusteHEX(memo[PC+1])+")";
   PC=PC+1;
   n=memo[PC];
   WR=A-memo[IY+n];
               F=grabarFlag("N",'1',F);
               F=determinarFlag("C",WR,F);
               F=determinarFlag("S",WR,F);
               F=determinarFlag("Z",WR,F);
               F=determinarFlag("H",WR,F);
               F=determinarFlag("P/V",WR,F);                
       CL++;
   T=T+19;
   break;
   
case"FD34":
   INSTRUCCION="INC (IY+"+AjusteHEX(memo[PC+1])+")";
   PC=PC+1;
   n=memo[PC];
   memo[IY+n]=memo[IY+n]+1;
               F=grabarFlag("N",'0',F);
               F=determinarFlag("S",memo[IY+n],F);
               F=determinarFlag("Z",memo[IY+n],F);
               F=determinarFlag("H",memo[IY+n],F);
               F=determinarFlag("P/V",memo[IY+n],F);                
       CL++;
   T=T+23;
   break;
   
case"FD35":
   INSTRUCCION="DEC (IY+"+AjusteHEX(memo[PC+1])+")";
   PC=PC+1;
   n=memo[PC];
   memo[IY+n]=memo[IY+n]-1;
               F=grabarFlag("N",'0',F);
               F=determinarFlag("S",memo[IY+n],F);
               F=determinarFlag("Z",memo[IY+n],F);
               F=determinarFlag("H",memo[IY+n],F);
               F=determinarFlag("P/V",memo[IY+n],F);                
       CL++;
   T=T+23;
   break;
   
///switch anidado    
case"DDCB":
   PC=PC+1;
   n=memo[PC];
   PC=PC+1;
   instmin2=Integer.toHexString(memo[PC]);
   inst2=instmin2.toUpperCase();
             cinst2=inst2.toCharArray();
             while (cinst2.length!=2){
             inst2="0"+inst2;
             cinst2=inst2.toCharArray();
             }
             SWITCHanidado();
   CL=CL+2;
   break;
   
case"FDCB":
   PC=PC+1;
   n=memo[PC];
   PC=PC+1;
   instmin2=Integer.toHexString(memo[PC]);
   inst2=instmin2.toUpperCase();
             cinst2=inst2.toCharArray();
             while (cinst2.length!=2){
             inst2="0"+inst2;
             cinst2=inst2.toCharArray();
             }
             switch(inst2){
                   case "1E":
                     CY=obtenFlag("C",F);
                     CY2=obtenFlag("C",memo[IY+n]);
                     sCY2=Integer.toBinaryString(CY2);
                     cCY2=sCY2.toCharArray();
                     F=grabarFlag("C",cCY2[0],F);
                     memo[IY+n]=RDC(memo[IY+n],CY);                
                     F=grabarFlag("N",'0',F);
                     F=grabarFlag("H",'0',F);
                     F=determinarFlag("S",memo[IY+n],F);
                     F=determinarFlag("Z",memo[IY+n],F);
                     F=determinarFlag("P",memo[IY+n],F);
                     F=determinarFlag("C",memo[IY+n],F);
                     INSTRUCCION="RR (IY+"+n+")";
                     T=T+23;
                   break;
           
                   case "16":
                     CY=obtenFlag("C",F);
                     CY2=obtenFlag("S",memo[IY+n]);
                     sCY2=Integer.toBinaryString(CY2);
                     cCY2=sCY2.toCharArray();
                     F=grabarFlag("C",cCY2[0],F);
                     memo[IY+n]=RIC(memo[IY+n],CY);                
                     F=grabarFlag("N",'0',F);
                     F=grabarFlag("H",'0',F);
                     F=determinarFlag("S",memo[IY+n],F);
                     F=determinarFlag("Z",memo[IY+n],F);
                     F=determinarFlag("P",memo[IY+n],F);
                     F=determinarFlag("C",memo[IY+n],F);
                     INSTRUCCION="RL (IY+"+n+")";
                     T=T+23;
                   break;
           
                   case "0E":
                     CY=obtenFlag("C",memo[IY+n]);
                     sCY=Integer.toBinaryString(CY);
                     cCY=sCY.toCharArray();
                     F=grabarFlag("C",cCY[0],F);
                     memo[IY+n]=RD(memo[IY+n]);
                     F=grabarFlag("N",'0',F);
                     F=grabarFlag("H",'0',F);
                     INSTRUCCION="RRC (IY+"+n+")";
                     T=T+23;
                   break;
           
                   case "06":
                     CY=obtenFlag("S",memo[IY+n]);
                     sCY=Integer.toBinaryString(CY);
                     cCY=sCY.toCharArray();
                     F=grabarFlag("C",cCY[0],F);
                     memo[IY+n]=RI(memo[IY+n]);
                     F=grabarFlag("N",'0',F);
                     F=grabarFlag("H",'0',F);
                     INSTRUCCION="RLC (IY+"+n+")";
                     T=T+23;
                   break;
           
                   case "2E":
                      CY=obtenFlag("C",memo[IY+n]);
                     sCY=Integer.toBinaryString(CY);
                     cCY=sCY.toCharArray();
                     F=grabarFlag("C",cCY[0],F);
                     memo[IY+n]=SRA(memo[IY+n]);
                     F=grabarFlag("N",'0',F);
                     F=grabarFlag("H",'0',F);
                     F=determinarFlag("S",memo[IY+n],F);
                     F=determinarFlag("Z",memo[IY+n],F);
                     F=determinarFlag("P",memo[IY+n],F);
                     F=determinarFlag("C",memo[IY+n],F);
                     T=T+23;
                     INSTRUCCION="SRA (IY+"+n+")";
                   break;
           
                   case "26":
                      CY=obtenFlag("S",memo[IY+n]);
                     sCY=Integer.toBinaryString(CY);
                     cCY=sCY.toCharArray();
                     F=grabarFlag("C",cCY[0],F);
                     memo[IY+n]=SLA(memo[IY+n]);
                     F=grabarFlag("N",'0',F);
                     F=grabarFlag("H",'0',F);
                     F=determinarFlag("S",memo[IY+n],F);
                     F=determinarFlag("Z",memo[IY+n],F);
                     F=determinarFlag("P",memo[IY+n],F);
                     F=determinarFlag("C",memo[IY+n],F);
                     T=T+23;
                     INSTRUCCION="SLA (IY+"+n+")";
                   break;
           
                   case "3E":
                      CY=obtenFlag("C",memo[IY+n]);
                     sCY=Integer.toBinaryString(CY);
                     cCY=sCY.toCharArray();
                     F=grabarFlag("C",cCY[0],F);
                     memo[IY+n]=SRL(memo[IY+n]);
                     F=grabarFlag("N",'0',F);
                     F=grabarFlag("H",'0',F);
                     F=determinarFlag("S",memo[IY+n],F);
                     F=determinarFlag("Z",memo[IY+n],F);
                     F=determinarFlag("P",memo[IY+n],F);
                     F=determinarFlag("C",memo[IY+n],F);
                     T=T+23;
                     INSTRUCCION="SRL (IY+"+n+")";
//           
                   case "46":
            CY=obtenFlag("C", memo[IY+n]);
                F=grabarFlag("H",'1',F);    
            F=grabarFlag("N",'0',F);
            if(CY==0)
                F=grabarFlag("Z",'1',F);
                else
                        F=grabarFlag("Z",'0',F);
                     T=T+20;
                     INSTRUCCION="BIT 0,(IY+"+n+")";
                   break;
           
                   case "4E":
            CY=obtenFlag("N", memo[IY+n]);
                F=grabarFlag("H",'1',F);    
            F=grabarFlag("N",'0',F);
            if(CY==0)
                F=grabarFlag("Z",'1',F);
                else
                        F=grabarFlag("Z",'0',F);
                     T=T+20;
                     INSTRUCCION="BIT 1,(IY+"+n+")";
                   break;
           
                   case "53":
            CY=obtenFlag("P/V", memo[IY+n]);
                F=grabarFlag("H",'1',F);    
            F=grabarFlag("N",'0',F);
            if(CY==0)
                F=grabarFlag("Z",'1',F);
                else
                        F=grabarFlag("Z",'0',F);
                     T=T+20;
                     INSTRUCCION="BIT 2,(IY+"+n+")";
                   break;
           
                   case "5E":
            CY=obtenFlag("3", memo[IY+n]);
                F=grabarFlag("H",'1',F);    
            F=grabarFlag("N",'0',F);
            if(CY==0)
                F=grabarFlag("Z",'1',F);
                else
                        F=grabarFlag("Z",'0',F);
                     T=T+20;
                     INSTRUCCION="BIT 3,(IY+"+n+")";
                   break;
           
                   case "66":
            CY=obtenFlag("H", memo[IY+n]);
                F=grabarFlag("H",'1',F);    
            F=grabarFlag("N",'0',F);
            if(CY==0)
                F=grabarFlag("Z",'1',F);
                else
                        F=grabarFlag("Z",'0',F);
                     T=T+20;
                     INSTRUCCION="BIT 4,(IY+"+n+")";
                   break;
           
                   case "6E":
            CY=obtenFlag("5", memo[IY+n]);
                F=grabarFlag("H",'1',F);    
            F=grabarFlag("N",'0',F);
            if(CY==0)
                F=grabarFlag("Z",'1',F);
                else
                        F=grabarFlag("Z",'0',F);
                     T=T+20;
                     INSTRUCCION="BIT 5,(IY+"+n+")";
                   break;
           
                   case "76":
            CY=obtenFlag("Z", memo[IY+n]);
                F=grabarFlag("H",'1',F);    
            F=grabarFlag("N",'0',F);
            if(CY==0)
                F=grabarFlag("Z",'1',F);
                else
                        F=grabarFlag("Z",'0',F);
                     T=T+20;
                     INSTRUCCION="BIT 6,(IY+"+n+")";
                   break;
           
                   case "7E":
            CY=obtenFlag("S", memo[IY+n]);
                F=grabarFlag("H",'1',F);    
            F=grabarFlag("N",'0',F);
            if(CY==0)
                F=grabarFlag("Z",'1',F);
                else
                        F=grabarFlag("Z",'0',F);
                     T=T+20;
                     INSTRUCCION="BIT 7,(IY+"+n+")";
                   break;
           
                   case "86":
                     INSTRUCCION="RES 0,(IY+"+n+")";
                     memo[IY+n]=grabarFlag("C",'0',memo[IY+n]);
                     T=T+23;
                   break;
           
                   case "8E":
                     INSTRUCCION="RES 1,(IY+"+n+")";
                     memo[IY+n]=grabarFlag("N",'0',memo[IY+n]);
                     T=T+23;
                   break;
           
                   case "96":
                     INSTRUCCION="RES 2,(IY+"+n+")";
                     memo[IY+n]=grabarFlag("P/V",'0',memo[IY+n]);
                     T=T+23;
                   break;
           
                   case "9E":
                     INSTRUCCION="RES 3,(IY+"+n+")";
                     memo[IY+n]=grabarFlag("3",'0',memo[IY+n]);
                     T=T+23;
                   break;
           
                   case "A6":
                     INSTRUCCION="RES 4,(IY+"+n+")";
                     memo[IY+n]=grabarFlag("H",'0',memo[IY+n]);
                     T=T+23;
                   break;
           
                   case "AE":
                     INSTRUCCION="RES 5,(IY+"+n+")";
                     memo[IY+n]=grabarFlag("5",'0',memo[IY+n]);
                     T=T+23;
                   break;
           
                   case "B6":
                     INSTRUCCION="RES 6,(IY+"+n+")";
                     memo[IY+n]=grabarFlag("Z",'0',memo[IY+n]);
                     T=T+23;
                   break;
           
                   case "BE":
                     INSTRUCCION="RES 7,(IY+"+n+")";
                     memo[IY+n]=grabarFlag("S",'0',memo[IY+n]);
                     T=T+23;
                   break;
           
                   case "C6":
                     INSTRUCCION="SET 0,(IY+"+n+")";
                     memo[IY+n]=grabarFlag("C",'1',memo[IY+n]);
                     T=T+23;
                   break;
           
                   case "CE":
                     INSTRUCCION="SET 1,(IY+"+n+")";
                     memo[IY+n]=grabarFlag("N",'1',memo[IY+n]);
                     T=T+23;
                   break;
           
                   case "D6":
                     INSTRUCCION="SET 2,(IY+"+n+")";
                     memo[IY+n]=grabarFlag("P/V",'1',memo[IY+n]);
                     T=T+23;
                   break;
           
                   case "DE":
                     INSTRUCCION="SET 3,(IY+"+n+")";
                     memo[IY+n]=grabarFlag("3",'1',memo[IY+n]);
                     T=T+23;
                   break;
           
                   case "E6":
                     INSTRUCCION="SET 4,(IY+"+n+")";
                     memo[IY+n]=grabarFlag("H",'1',memo[IY+n]);
                     T=T+23;
                   break;
           
                   case "EE":
                     INSTRUCCION="SET 5,(IY+"+n+")";
                     memo[IY+n]=grabarFlag("5",'1',memo[IY+n]);
                     T=T+23;
                   break;
           
                   case "F6":
                     INSTRUCCION="SET 6,(IY+"+n+")";
                     memo[IY+n]=grabarFlag("Z",'1',memo[IY+n]);
                     T=T+23;
                   break;
           
                   case "FE":
                     INSTRUCCION="SET 7,(IY+"+n+")";
                     memo[IY+n]=grabarFlag("S",'1',memo[IY+n]);
                     T=T+23;
                   break;
                   }//fin swuitch de lore 2bytes
   CL=CL+2;
   break;
      }  
      
    
}
        
        //FIN NUEVO METODO
     
     void SWITCHanidado(){
        switch(inst2){
                   case "1E":
                     CY=obtenFlag("C",F);
                     CY2=obtenFlag("C",memo[IX+n]);
                     sCY2=Integer.toBinaryString(CY2);
                     cCY2=sCY2.toCharArray();
                     F=grabarFlag("C",cCY2[0],F);
                     memo[IX+n]=RDC(memo[IX+n],CY);                
                     F=grabarFlag("N",'0',F);
                     F=grabarFlag("H",'0',F);
                     F=determinarFlag("S",memo[IX+n],F);
                     F=determinarFlag("Z",memo[IX+n],F);
                     F=determinarFlag("P",memo[IX+n],F);
                     F=determinarFlag("C",memo[IX+n],F);
                     INSTRUCCION="RR (IX+"+n+")";
                     T=T+23;
                   break;
           
                   case "16":
                     CY=obtenFlag("C",F);
                     CY2=obtenFlag("S",memo[IX+n]);
                     sCY2=Integer.toBinaryString(CY2);
                     cCY2=sCY2.toCharArray();
                     F=grabarFlag("C",cCY2[0],F);
                     memo[IX+n]=RIC(memo[IX+n],CY);                
                     F=grabarFlag("N",'0',F);
                     F=grabarFlag("H",'0',F);
                     F=determinarFlag("S",memo[IX+n],F);
                     F=determinarFlag("Z",memo[IX+n],F);
                     F=determinarFlag("P",memo[IX+n],F);
                     F=determinarFlag("C",memo[IX+n],F);
                     INSTRUCCION="RL (IX+"+n+")";
                     T=T+23;
                   break;
           
                   case "0E":
                     CY=obtenFlag("C",memo[IX+n]);
                     sCY=Integer.toBinaryString(CY);
                     cCY=sCY.toCharArray();
                     F=grabarFlag("C",cCY[0],F);
                     memo[IX+n]=RD(memo[IX+n]);
                     F=grabarFlag("N",'0',F);
                     F=grabarFlag("H",'0',F);
                     INSTRUCCION="RRC (IX+"+n+")";
                     T=T+23;
                   break;
           
                   case "06":
                     CY=obtenFlag("S",memo[IX+n]);
                     sCY=Integer.toBinaryString(CY);
                     cCY=sCY.toCharArray();
                     F=grabarFlag("C",cCY[0],F);
                     memo[IX+n]=RI(memo[IX+n]);
                     F=grabarFlag("N",'0',F);
                     F=grabarFlag("H",'0',F);
                     INSTRUCCION="RLC (IX+"+n+")";
                     T=T+23;
                   break;
           
                   case "2E":
                      CY=obtenFlag("C",memo[IX+n]);
                     sCY=Integer.toBinaryString(CY);
                     cCY=sCY.toCharArray();
                     F=grabarFlag("C",cCY[0],F);
                     memo[IX+n]=SRA(memo[IX+n]);
                     F=grabarFlag("N",'0',F);
                     F=grabarFlag("H",'0',F);
                     F=determinarFlag("S",memo[IX+n],F);
                     F=determinarFlag("Z",memo[IX+n],F);
                     F=determinarFlag("P",memo[IX+n],F);
                     F=determinarFlag("C",memo[IX+n],F);
                     T=T+23;
                     INSTRUCCION="SRA (IX+"+n+")";
                   break;
           
                   case "26":
                      CY=obtenFlag("S",memo[IX+n]);
                     sCY=Integer.toBinaryString(CY);
                     cCY=sCY.toCharArray();
                     F=grabarFlag("C",cCY[0],F);
                     memo[IX+n]=SLA(memo[IX+n]);
                     F=grabarFlag("N",'0',F);
                     F=grabarFlag("H",'0',F);
                     F=determinarFlag("S",memo[IX+n],F);
                     F=determinarFlag("Z",memo[IX+n],F);
                     F=determinarFlag("P",memo[IX+n],F);
                     F=determinarFlag("C",memo[IX+n],F);
                     T=T+23;
                     INSTRUCCION="SLA (IX+"+n+")";
                   break;
           
                   case "3E":
                      CY=obtenFlag("C",memo[IX+n]);
                     sCY=Integer.toBinaryString(CY);
                     cCY=sCY.toCharArray();
                     F=grabarFlag("C",cCY[0],F);
                     memo[IX+n]=SRL(A);
                     F=grabarFlag("N",'0',F);
                     F=grabarFlag("H",'0',F);
                     F=determinarFlag("S",memo[IX+n],F);
                     F=determinarFlag("Z",memo[IX+n],F);
                     F=determinarFlag("P",memo[IX+n],F);
                     F=determinarFlag("C",memo[IX+n],F);
                     T=T+23;
                     INSTRUCCION="SRL (IX+"+n+")";
                   break;
//           
                   case "46":
            CY=obtenFlag("C", memo[IX+n]);
                F=grabarFlag("H",'1',F);    
            F=grabarFlag("N",'0',F);
            if(CY==0)
                F=grabarFlag("Z",'1',F);
                else
                        F=grabarFlag("Z",'0',F);
                     T=T+20;
                     INSTRUCCION="BIT 0,(IX+"+n+")";
                   break;
           
                   case "4E":
            CY=obtenFlag("N", memo[IX+n]);
                F=grabarFlag("H",'1',F);    
            F=grabarFlag("N",'0',F);
            if(CY==0)
                F=grabarFlag("Z",'1',F);
                else
                        F=grabarFlag("Z",'0',F);
                     T=T+20;
                     INSTRUCCION="BIT 1,(IX+"+n+")";
                   break;
           
                   case "53":
            CY=obtenFlag("P/V", memo[IX+n]);
                F=grabarFlag("H",'1',F);    
            F=grabarFlag("N",'0',F);
            if(CY==0)
                F=grabarFlag("Z",'1',F);
                else
                        F=grabarFlag("Z",'0',F);
                     T=T+20;
                     INSTRUCCION="BIT 2,(IX+"+n+")";
                   break;
           
                   case "5E":
            CY=obtenFlag("3", memo[IX+n]);
                F=grabarFlag("H",'1',F);    
            F=grabarFlag("N",'0',F);
            if(CY==0)
                F=grabarFlag("Z",'1',F);
                else
                        F=grabarFlag("Z",'0',F);
                     T=T+20;
                     INSTRUCCION="BIT 3,(IX+"+n+")";
                   break;
           
                   case "66":
            CY=obtenFlag("H", memo[IX+n]);
                F=grabarFlag("H",'1',F);    
            F=grabarFlag("N",'0',F);
            if(CY==0)
                F=grabarFlag("Z",'1',F);
                else
                        F=grabarFlag("Z",'0',F);
                     T=T+20;
                     INSTRUCCION="BIT 4,(IX+"+n+")";
                   break;
           
                   case "6E":
            CY=obtenFlag("5", memo[IX+n]);
                F=grabarFlag("H",'1',F);    
            F=grabarFlag("N",'0',F);
            if(CY==0)
                F=grabarFlag("Z",'1',F);
                else
                        F=grabarFlag("Z",'0',F);
                     T=T+20;
                     INSTRUCCION="BIT 5,(IX+"+n+")";
                   break;
           
                   case "76":
            CY=obtenFlag("Z", memo[IX+n]);
                F=grabarFlag("H",'1',F);    
            F=grabarFlag("N",'0',F);
            if(CY==0)
                F=grabarFlag("Z",'1',F);
                else
                        F=grabarFlag("Z",'0',F);
                     T=T+20;
                     INSTRUCCION="BIT 6,(IX+"+n+")";
                   break;
           
                   case "7E":
            CY=obtenFlag("S", memo[IX+n]);
                F=grabarFlag("H",'1',F);    
            F=grabarFlag("N",'0',F);
            if(CY==0)
                F=grabarFlag("Z",'1',F);
                else
                        F=grabarFlag("Z",'0',F);
                     T=T+20;
                     INSTRUCCION="BIT 7,(IX+"+n+")";
                   break;
           
                   case "86":
                     INSTRUCCION="RES 0,(IX+"+n+")";
                     memo[IX+n]=grabarFlag("C",'0',memo[IX+n]);
                     T=T+23;
                   break;
           
                   case "8E":
                     INSTRUCCION="RES 1,(IX+"+n+")";
                     memo[IX+n]=grabarFlag("N",'0',memo[IX+n]);
                     T=T+23;
                   break;
           
                   case "96":
                     INSTRUCCION="RES 2,(IX+"+n+")";
                     memo[IX+n]=grabarFlag("P/V",'0',memo[IX+n]);
                     T=T+23;
                   break;
           
                   case "9E":
                     INSTRUCCION="RES 3,(IX+"+n+")";
                     memo[IX+n]=grabarFlag("3",'0',memo[IX+n]);
                     T=T+23;
                   break;
           
                   case "A6":
                     INSTRUCCION="RES 4,(IX+"+n+")";
                     memo[IX+n]=grabarFlag("H",'0',memo[IX+n]);
                     T=T+23;
                   break;
           
                   case "AE":
                     INSTRUCCION="RES 5,(IX+"+n+")";
                     memo[IX+n]=grabarFlag("5",'0',memo[IX+n]);
                     T=T+23;
                   break;
           
                   case "B6":
                     INSTRUCCION="RES 6,(IX+"+n+")";
                     memo[IX+n]=grabarFlag("Z",'0',memo[IX+n]);
                     T=T+23;
                   break;
           
                   case "BE":
                     INSTRUCCION="RES 7,(IX+"+n+")";
                     memo[IX+n]=grabarFlag("S",'0',memo[IX+n]);
                     T=T+23;
                   break;
           
                   case "C6":
                     INSTRUCCION="SET 0,(IX+"+n+")";
                     memo[IX+n]=grabarFlag("C",'1',memo[IX+n]);
                     T=T+23;
                   break;
           
                   case "CE":
                     INSTRUCCION="SET 1,(IX+"+n+")";
                     memo[IX+n]=grabarFlag("N",'1',memo[IX+n]);
                     T=T+23;
                   break;
           
                   case "D6":
                     INSTRUCCION="SET 2,(IX+"+n+")";
                     memo[IX+n]=grabarFlag("P/V",'1',memo[IX+n]);
                     T=T+23;
                   break;
           
                   case "DE":
                     INSTRUCCION="SET 3,(IX+"+n+")";
                     memo[IX+n]=grabarFlag("3",'1',memo[IX+n]);
                     T=T+23;
                   break;
           
                   case "E6":
                     INSTRUCCION="SET 4,(IX+"+n+")";
                     memo[IX+n]=grabarFlag("H",'1',memo[IX+n]);
                     T=T+23;
                   break;
           
                   case "EE":
                     INSTRUCCION="SET 5,(IX+"+n+")";
                     memo[IX+n]=grabarFlag("5",'1',memo[IX+n]);
                     T=T+23;
                   break;
           
                   case "F6":
                     INSTRUCCION="SET 6,(IX+"+n+")";
                     memo[IX+n]=grabarFlag("Z",'1',memo[IX+n]);
                     T=T+23;
                   break;
           
                   case "FE":
                     INSTRUCCION="SET 7,(IX+"+n+")";
                     memo[IX+n]=grabarFlag("S",'1',memo[IX+n]);
                     T=T+23;
                   break;
           
             }
         }  //FIN SWITCH ANIDADO

    //PILA
    
    void push(int x){
        memo[SP]=x;
        SP--;
        marcaPila++;
    }

// PILA :     FFFF - F001

    boolean verificaPilaPUSH(){
        if(SP==61441){  
            errorPila="LLENA";
            return false;  //Devuelve false si la pila esta llena
        }
        else{
            return true;
        }
    }
    boolean verificaPilaPOP(){
        if(SP==65535){  // PILA :     FFFF - F001
            errorPila="VACIA";
            return false;  //Devuelve false si la pila esta vacia
        }
        else{
            return true;
        }
    }
    int pop(){
            SP++;
            marcaPila--;
            int x=memo[SP];
            return x; 
        
    }
        
}