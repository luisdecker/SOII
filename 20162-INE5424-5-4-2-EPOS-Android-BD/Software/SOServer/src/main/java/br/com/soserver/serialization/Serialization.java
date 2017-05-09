package br.com.soserver.serialization;
import org.apache.commons.lang3.StringUtils;

import java.lang.NumberFormatException;

public class Serialization {

	public String serialize(Message message) {
		String data = message.getData();
		int ldata = data.length();
		String tmp = message.getAddress() + message.getFunctionCode() + data;
		int checksum = calculateLRC(tmp);
		String result = ":" + message.getAddress() + message.getFunctionCode()
										+ data + StringUtils.leftPad(Message.toHex(checksum), 2, '0')+ "\r\n";
		return result;
	}
	public Message deserialize(String content) {
		Message result = new Message();
		result.setAddress(content.substring(1,3));
		result.setFunctionCode(content.substring(3,5));
		int ind = content.lastIndexOf('\r');
		if (ind == -1) {
			return null;
		}

		result.setDataSlice(content.substring(5, ind-2), 0, ind-7);
		int checksum;
		try {
			checksum = result.fromHex(content.substring(ind-2, ind));
		} catch (NumberFormatException e) {
			return null;
		}

		String tmp = result.getAddress() + result.getFunctionCode() + result.getData();
		int checksum2 = calculateLRC(tmp);
		if (checksum2 != checksum || checksum2 == -1) {
			result = null;
		}
		return result;
	}
	public static int calculateLRC(String b) {
		try {
			int LRC = 0;
			int l = b.length();
			for (int i = 0; i < l; i += 2) {
				LRC += Message.fromHex(b.substring(i, i+2));
			}
			return ((byte) LRC) & 0xff;
		}catch (NumberFormatException e){
			return 0;
		}
	}
		// unsigned char nLRC = 0; // LRC char initialized
		// int l = strlen(data);
		// for (int i = 0; i < l; i += 2) {
		//   char *s = Message::sliceChar(data, i, i+2);
		//   nLRC -= Message::fromHex(s);
		//}
		//delete [] data;
}
