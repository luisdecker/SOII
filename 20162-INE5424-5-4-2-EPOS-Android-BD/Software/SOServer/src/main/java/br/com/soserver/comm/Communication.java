package br.com.soserver.comm;

import br.com.soserver.serialization.Message;
import br.com.soserver.serialization.Serialization;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by fernando on 06/11/16.
 */
public class Communication implements SerialPortEventListener {
    private BufferedReader input;
    private OutputStream out;
    private int address;
    private List<Integer> sent;
    private Serialization serialization;
    private BlockingQueue<Message> receiveSendQueue;
    private BlockingQueue<Message> receiveQueue;
    private long lastGCrun;
    /* Device Address: */
    private static int deviceAddress = 0;
    /* Global instance */
    private static Communication instance;



    public Communication(int address, Reader input, OutputStream out, Serialization serialization) {
        this.address = address;
        if (input instanceof BufferedReader) {
            this.input = (BufferedReader) input;
        } else {
            this.input = new BufferedReader(input);
        }
        this.out = out;
        this.sent = new LinkedList<>();
        this.serialization = serialization;
        this.receiveSendQueue = new LinkedBlockingDeque<>();
        this.receiveQueue = new LinkedBlockingDeque<>();
    }


    public Communication(int address, BasePortManager manager, Serialization serialization) {
        this.address = address;
        this.input = manager.getInput();
        this.out = manager.getOutput();
        this.sent = new LinkedList<>();
        this.serialization = serialization;
        this.receiveSendQueue = new LinkedBlockingDeque<>();
        this.receiveQueue = new LinkedBlockingDeque<>();
    }

    public static Communication getInstance() {
        if (instance == null) {
            PortManager port = PortManager.getInstance();
            Serialization serialization = new Serialization();
            instance = new Communication(deviceAddress, port, serialization);
            try {
                port.addEventListener(instance);
            } catch (TooManyListenersException e) {
                e.printStackTrace();
            }
            port.notifyOnDataAvailable(true);
        }
        return instance;
    }

    public Message send(Message msg, int retries) {
        Message ack = null;
        int msgAddress = Message.fromHex(msg.getAddress());
        System.out.println("Enviando mensagem para "+msgAddress);
        int msgFunctionCode = Message.fromHex(msg.getFunctionCode());
        synchronized (this.sent) {
            this.sent.add(msgAddress);
        }
        while(ack == null && retries > 0) {
            synchronized (this.out) {
                String toSend = this.serialization.serialize(msg);
                System.out.println("Escrevendo '"+toSend+"' na porta..");
                try {
                    this.out.write(toSend.getBytes());
                } catch (IOException e) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e2) {

                    }
                    continue;
                }
            }
            retries--;
            long start = System.currentTimeMillis();
            while (ack == null) {
                long dif = System.currentTimeMillis()-start;
                if (dif > 2000) {
                    break;
                }
                synchronized (this.receiveSendQueue) {
                    try {
                        receiveSendQueue.wait(2000);
                    } catch (InterruptedException e) {
                        continue;
                    }
                    int count = this.receiveSendQueue.size();
                    while (ack == null && count > 0) {
                        ack = this.receiveSendQueue.poll();
                        if (
                                msgAddress != Message.fromHex(ack.getAddress()) ||
                                (
                                        msgFunctionCode != Message.fromHex(msg.getFunctionCode()) &&
                                        (msgFunctionCode+128) != Message.fromHex(msg.getFunctionCode())
                                )
                        ) {
                            // Probably not what we want, so we just re-add in the query and get a new element
                            this.receiveSendQueue.add(ack);
                            ack = this.receiveSendQueue.poll();
                        }
                        count--;
                    } while (ack == null && count > 0);
                }
            }
            retries--;
        }
        synchronized (this.sent) {
            this.sent.remove((Object) msgAddress);
        }
        this.runGC();
        return ack;
    }

    public Message send(Message msg) {
        return send(msg, 1);
    }

    public boolean ack(Message msg) throws IOException {
        int msgAddress = Message.fromHex(msg.getAddress());
        if (msgAddress != this.address) {
            // A message, to be sent to ack, should have the same address of this communication object..
            return false;
        }
        synchronized (this.out) {
            String toSend = this.serialization.serialize(msg);
            this.out.write(toSend.getBytes());
        }
        return true;
    }

    public Message receive() {
        Message result = null;
        while (result == null) {
            try {
                result = this.receiveQueue.take();
            } catch (InterruptedException e) {
                continue;
            }
        }
        return result;
    }

    @Override
    public void serialEvent(SerialPortEvent ev) {
        System.out.println("Evento recebido");
        if (ev.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            String line = "";
            try {
                line = input.readLine();
            } catch (Exception e) {
                e.printStackTrace();
                line = "";
            }
            if (line.isEmpty()) {
                System.out.println("Linha esta vazia :(");
                return;
            }
            line += "\r\n"; // We need to re-add because....well, readLine removes that!
            System.out.println("Recebida linha: "+line);
            Message msg = this.serialization.deserialize(line);
            if (msg == null) {
                return;
            }
            int msgAddress = Message.fromHex(msg.getAddress());
            System.out.println("Recebida mensagem de "+msgAddress);
            synchronized (this.sent) {
                if (msgAddress == this.address) {
                    this.receiveQueue.add(msg);
                } else if (this.sent.contains(msgAddress)) {
                    this.receiveSendQueue.add(msg);
                }
            }
            this.runGC();
        }
    }

    public void runGC() {
        synchronized (this.receiveSendQueue) {
            long now = System.currentTimeMillis();
            long dif =  now - this.lastGCrun;
            if (dif < 2000) {
                return;
            }
            this.lastGCrun = now;
            int count = this.receiveSendQueue.size();
            while (count > 0) {
                Message msg = this.receiveSendQueue.poll();
                int msgAddress = Message.fromHex(msg.getAddress());
                if (this.sent.contains(msgAddress)) {
                    this.receiveSendQueue.add(msg);
                }
                count--;
            }
        }
    }
}
