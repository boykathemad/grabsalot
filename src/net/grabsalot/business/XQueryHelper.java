/*
package net.grabsalot.business;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQConstants;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;
import net.sf.saxon.xqj.SaxonXQDataSource;

public class XQueryHelper {

	public XQueryHelper() {
	}


	public static XQResultSequence runQuery(String query, String filename)
			throws FileNotFoundException, XQException, UnsupportedEncodingException {
		return runQuery(new ByteArrayInputStream(query.getBytes("UTF-8")), new FileInputStream(filename));
	}

	public static XQResultSequence runQuery(InputStream queryStream, FileInputStream fileStream) throws XQException {
		XQDataSource ds = new SaxonXQDataSource();
		XQConnection conn = ds.getConnection();
		XQPreparedExpression exp = conn.prepareExpression(queryStream);
		exp.bindDocument(XQConstants.CONTEXT_ITEM, fileStream, null, conn.createDocumentType());
		return exp.executeQuery();
	}

	public static void writeResult(XQResultSequence result, OutputStream out) throws XQException {
		Properties props = new Properties();
		props.setProperty("method", "xml");
		props.setProperty("indent", "yes");
		props.setProperty("omit-xml-declaration", "yes");
		props.setProperty("{http://saxon.sf.net/}indent-spaces", "1");
		result.writeSequence(System.out, props);
	}
}
*/