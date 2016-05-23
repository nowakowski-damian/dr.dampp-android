package com.thirteendollars.drdampp.connection;

/**
 * Created by Damian Nowakowski on 27.04.16.
 */
public class APIDecoder {


    public static final int MAX_PACKET_LENGTH =4;
    private final static byte TEST_CONNECTION_ID=-1;
    private final byte SET_MOTORS_ID=0;
    private final byte SET_SWITCH_ID=1;
    private final byte SET_ALL_SWITCHES_ID=1;
    private final byte LEFT_ID=2;
    private final byte RIGHT_ID=2;



    public byte[] setMotors(int leftLevel, int rightLevel ){

        byte[] pack = new byte[MAX_PACKET_LENGTH];
        pack[0]=SET_MOTORS_ID;
        pack[1]=(byte)leftLevel;
        pack[2]=(byte)rightLevel;
        return pack;
    }

    public byte[] setSwitch(int switchIndex, boolean turnOn  ){

        byte[] pack = new byte[MAX_PACKET_LENGTH];
        pack[0]=SET_SWITCH_ID;
        pack[1]=(byte)switchIndex;
        pack[2]= (byte)( turnOn ? 1:0 );
        return pack;
    }


    public byte[] setAllSwitches( boolean turnOn  ){

        byte[] pack = new byte[MAX_PACKET_LENGTH];
        pack[0]=SET_ALL_SWITCHES_ID;
        pack[1]=-1;
        pack[2]= (byte)( turnOn ? 1:0 );
        return pack;
    }


    public byte[] turnLeft( int angle ){

        byte[] pack = new byte[MAX_PACKET_LENGTH];
        pack[0]=LEFT_ID;
        pack[1]=0;
        pack[2]= (byte) angle;
        return pack;
    }

    public byte[] turnRight( int angle  ){

        byte[] pack = new byte[MAX_PACKET_LENGTH];
        pack[0]=RIGHT_ID;
        pack[1]=1;
        pack[2]= (byte) angle;
        return pack;
    }

    public static byte[] testConnection(){

        byte[] pack = new byte[MAX_PACKET_LENGTH];
        pack[0]=TEST_CONNECTION_ID;
        pack[1]=TEST_CONNECTION_ID;
        pack[2]= TEST_CONNECTION_ID;
        pack[3]=TEST_CONNECTION_ID;
        return pack;
    }

    public static boolean arePacketsEqual(byte[] pack1,byte[] pack2){

        if(pack1[0]==pack2[0] &&
           pack1[1]==pack2[1] &&
           pack1[2]==pack2[2] &&
           pack1[3]==pack2[3] ){
                return true;
        }
        else return false;
    }



}
