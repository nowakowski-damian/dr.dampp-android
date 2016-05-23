package com.thirteendollars.drdampp.connection;

import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.Log;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeoutException;

/**
 * Created by Damian Nowakowski on 27.04.16.
 */
public abstract class UDPManager {




    private final int RESPONSE_TIMEOUT_MS = 200;
    private InetAddress mServerAddr;
    private DatagramSocket mSocket;
    private int mPortNumber;
    private Queue<byte[]> mRequestsQueue;

    //AsyncTask variables
    private ConnectionThread mConnection;
    private byte[] data;
    private int skippedResponses = 0;
    private boolean wasPacketSent = false;



    public UDPManager(String serverAddress, int portNumber) throws UnknownHostException, SocketException {
        mServerAddr = InetAddress.getByName(serverAddress);
        mSocket = new DatagramSocket(9876);
        mPortNumber = portNumber;
        mRequestsQueue = new ConcurrentLinkedQueue<>();
        startListening();
    }

    private void startListening() {
        mConnection = new ConnectionThread();
        mConnection.execute();
    }


    public void setNewSettings(String serverIpAddress, int serverPortNumber) throws UnknownHostException {
        mPortNumber = serverPortNumber;
        mServerAddr = InetAddress.getByName(serverIpAddress);
    }


    public void send(byte[] dataToSend) {
        mRequestsQueue.add(dataToSend);
    }





    private class ConnectionThread extends AsyncTask< Void, Void, SocketTimeoutException> {

        @Override
        protected SocketTimeoutException doInBackground(Void... param) {

                DatagramPacket packet;

                if( !mRequestsQueue.isEmpty() ){

                        // send data
                        try {
                            data = mRequestsQueue.poll();
                            packet = new DatagramPacket(data, data.length, mServerAddr, mPortNumber);
                            mSocket.send(packet);
                            if(!wasPacketSent) {
                                skippedResponses = 0;
                                wasPacketSent=true;
                            }
                            Log.d(getClass().getName(), "Sent data: " + data[0] + " " + data[1] + " " + data[2] + " " + data[3]);
                        } catch (IOException exception) {
                            Log.e(getClass().getName(), exception.toString());
                        }
                }

                // just receive data
                try {
                    data = new byte[APIDecoder.MAX_PACKET_LENGTH];
                    packet = new DatagramPacket(data, data.length);
                    mSocket.setSoTimeout(RESPONSE_TIMEOUT_MS);
                    mSocket.receive(packet);
                    wasPacketSent=false;
                    Log.d(getClass().getName(), "Received data: " + data[0] + " " + data[1] + " " + data[2] + " " + data[3]);
                } catch (SocketTimeoutException exception) {
                    if(wasPacketSent){
                        skippedResponses++;
                        return exception;
                    }
                    else {
                        return null;
                    }
                } catch (SocketException exception) {
                    Log.e(getClass().getName(), exception.toString());
                } catch (IOException exception) {
                    Log.e(getClass().getName(), exception.toString());
                }

            return null;
        }

        @Override
        protected void onPostExecute(SocketTimeoutException exception) {
            super.onPostExecute(exception);
            if(exception==null){
                onDataReceived(data);
            }
            else{
                onResponseSkipped(skippedResponses);
            }
            startListening();
        }
    }


    public abstract void onResponseSkipped(int skippedResponses);
    public abstract void onDataReceived(byte[] data);

}






















