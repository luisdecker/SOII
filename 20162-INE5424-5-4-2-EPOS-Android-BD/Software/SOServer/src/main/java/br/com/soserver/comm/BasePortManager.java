/**
 * 
 */
package br.com.soserver.comm;

import java.io.BufferedReader;
import java.io.OutputStream;

/**
 * @author Ercilio Nascimento
 */
public abstract class BasePortManager {

	public abstract BufferedReader getInput();
	public abstract OutputStream getOutput();


}
