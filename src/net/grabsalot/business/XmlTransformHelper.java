/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.grabsalot.business;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author madboyka
 */
public class XmlTransformHelper {

	static {
		System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
	}

	public static OutputStream transform(String styleSheet, String xmlFile) throws TransformerException, TransformerConfigurationException {
		try {
			return transform(new FileInputStream(styleSheet), new FileInputStream(xmlFile));
		}
		catch (FileNotFoundException ex) {
			return null;
		}
	}

	public static OutputStream transform(InputStream styleSheet, InputStream xmlFile) throws TransformerException, TransformerConfigurationException {
		TransformerFactory tfactory = TransformerFactory.newInstance();
		Transformer transformer = tfactory.newTransformer(new StreamSource(styleSheet));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		transformer.transform(new StreamSource(xmlFile), new StreamResult(baos));
		return baos;
	}
}