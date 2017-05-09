/**
 * 
 */
package br.com.soserver.comm;

import br.com.soserver.serialization.Serialization;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.TooManyListenersException;

/**
 * @author Ercilio Nascimento
 */
public class PortManager extends BasePortManager {
	SerialPort serialPort;
	/** The port we're normally going to use. */
	private static final String PORT_NAMES[] = {
		// "/dev/tty.usbserial-A9007UX1", // Mac OS X
		"/dev/ttyUSB0", // Linux
		// "COM9", // Windows
	};

	private static PortManager instance;

	/**
	 * A BufferedReader which will be fed by a InputStreamReader
	 * converting the bytes into characters
	 * making the displayed results codepage independent
	 */
	private BufferedReader input;
	/** The output stream to the port */
	private OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;

	/* Saves the communication handler */
	private Communication communication;


    public static PortManager getInstance() {
		if (instance == null) {
			instance = new PortManager();
			instance.initialize();
		}

		return instance;
	}

	public static void destroyInstance() {
        if (instance == null) {
            return;
        }
        instance.close();
        instance = null;
    }

    public BufferedReader getInput() {
        return input;
    }

    public OutputStream getOutput() {
        return this.output;
    }


    public void addEventListener(SerialPortEventListener listener) throws TooManyListenersException {
        serialPort.addEventListener(listener);
    }

    public void notifyOnDataAvailable( boolean enable ) {
        serialPort.notifyOnDataAvailable(enable);
    }
    public void notifyOnOutputEmpty( boolean enable ) {
        serialPort.notifyOnOutputEmpty(enable);
    }
    public void notifyOnCTS( boolean enable ) {
        serialPort.notifyOnCTS(enable);
    }
    public void notifyOnDSR( boolean enable ) {
        serialPort.notifyOnDSR(enable);
    }
    public void notifyOnRingIndicator( boolean enable ) {
        serialPort.notifyOnRingIndicator(enable);
    }
    public void notifyOnCarrierDetect( boolean enable ) {
        serialPort.notifyOnCarrierDetect(enable);
    }
    public void notifyOnOverrunError( boolean enable ) {
        serialPort.notifyOnOverrunError(enable);
    }
    public void notifyOnParityError( boolean enable ) {
        serialPort.notifyOnParityError(enable);
    }
    public void notifyOnFramingError( boolean enable ) {
        serialPort.notifyOnFramingError(enable);
    }
    public void notifyOnBreakInterrupt( boolean enable ) {
        serialPort.notifyOnBreakInterrupt(enable);
    }


	private void initialize() {
		CommPortIdentifier portId = null;
		@SuppressWarnings("unchecked") Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
		// First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			System.out.println(currPortId.getName());
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);

			// open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			// serialPort.enableReceiveTimeout(1000);
			serialPort.enableReceiveThreshold(0);
			System.out.println("Server is listening port " + serialPort.getName());
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	/**
	 * This should be called when you stop using the port.
	 * This will prevent port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}
}
