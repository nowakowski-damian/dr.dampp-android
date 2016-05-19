package com.thirteendollars.drdampp.connection;

import android.os.AsyncTask;
import android.util.Log;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by Damian Nowakowski on 27.04.16.
 */
public class UDPManager {


    private InetAddress mServerAddr;
    private DatagramSocket mClientSocket;
    private int mPortNumber;


    public UDPManager(String serverAddress, int portNumber) throws UnknownHostException, SocketException {
        mServerAddr = InetAddress.getByName(serverAddress);
        mClientSocket = new DatagramSocket();
        mPortNumber = portNumber;
    }


    public void send(byte[] dataToSend ){
        new SendDataTask().execute(dataToSend);
    }


    private class SendDataTask extends AsyncTask<byte[],Void,IOException>{

        @Override
        protected IOException doInBackground(byte[]... data) {
            DatagramPacket sendPacket = new DatagramPacket(data[0], data[0].length, mServerAddr, mPortNumber);
            Log.d(getClass().getName(),"Data: "+data[0][0] +" "+data[0][1] +" "+data[0][2] +" "+data[0][3]);
            try {
                mClientSocket.send(sendPacket);
            } catch (IOException e) {
                return e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(IOException exception) {
            if( exception != null){
                //TODO: send package again?
                Log.e(getClass().getName(),exception.toString() );
            }
            else{
                Log.d(getClass().getName(),"Data send successfully" );
            }
        }
    }
}




















