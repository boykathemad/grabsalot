package net.grabsalot.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;

import net.grabsalot.util.Base64;
import net.grabsalot.util.Serializer;

public class Serializer {

	private static String serializedPrefix = "DontTouchThis:";

	public static byte[] serialize(Object object) {
		byte[] value = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream o = new ObjectOutputStream(baos);
			o.writeObject(object);
			o.close();
			baos.close();
			value = baos.toByteArray();
		} catch (IOException e) {
		}
		return value;
	}

	public static String serialize(Object object, boolean asString) {
		return Base64.encodeBytes(Serializer.serialize(object));
	}

	public static Object deserialize(byte[] serial) {
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(serial);
			ObjectInputStream i = new ObjectInputStream(bais);
			return i.readObject();
		} catch (UnsupportedEncodingException e) {
		} catch (IOException e) {
		} catch (ClassNotFoundException e) {
		}
		return new Object();
	}

	public static Object deserialize(String serial) {
		try {
			return Serializer.deserialize(Base64.decode(serial));
		} catch (UnsupportedEncodingException e) {
		} catch (IOException e) {
		}
		return null;
	}

	public static String toString(Object object) {
		String serial = null;
		if (object == null) {
			serial = "";
		} else if (object instanceof Integer || object instanceof Float || object instanceof Boolean || object instanceof String) {
			serial = object.toString();
		} else {
			serial = serializedPrefix + serialize(object, true);
		}
		return serial;
	}

	public static Object fromString(String serial) {
		if (serial.length() == 0) {
			return null;
		} else if (serial.startsWith(serializedPrefix)) {
			return deserialize(serial.replace(serializedPrefix, ""));
		} else if (serial.matches("(true|false)")) {
			return Boolean.valueOf(serial);
		} else if (serial.matches("\\-?[0-9]+\\.[0-9]*")) {
			return Float.parseFloat(serial);
		} else if (serial.matches("\\-?[0-9]+")) {
			return Integer.parseInt(serial);
		}
		return serial;
	}
}
