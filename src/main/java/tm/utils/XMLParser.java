/*
*
*    This file is part of Tile Monster.
*
*    Tile Monster is free software; you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation; either version 2 of the License, or
*    (at your option) any later version.
*
*    Tile Monster is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*/

package tm.utils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class XMLParser {

	public static Document parse(InputStream inputStream, final String dtdResource)
			throws SAXException, SAXParseException, ParserConfigurationException, IOException
	{
		Document document=null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(true);
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setEntityResolver(new EntityResolver() {
				public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
					return new InputSource(getClass().getResourceAsStream(dtdResource));
				}
			});
			builder.setErrorHandler(
			  new org.xml.sax.ErrorHandler() {      // ignore fatal errors (an exception is guaranteed)
				  public void fatalError(SAXParseException exception)
				  throws SAXException {
				  }
				  // treat validation errors as fatal
				  public void error(SAXParseException e)
				  throws SAXParseException
				  {
					throw e;
				  }
				  // dump warnings too
				  public void warning(SAXParseException err)
				  throws SAXParseException
				  {
					System.out.println("** Warning"
					   + ", line " + err.getLineNumber()
					   + ", uri " + err.getSystemId());
					System.out.println("   " + err.getMessage());
				  }
			  }
			);
			document = builder.parse(inputStream);  // parse file
		} catch (SAXParseException spe) {
			throw spe;
		} catch (SAXException sxe) {
			throw sxe;
		} catch (ParserConfigurationException pce) {
		   // Parser with specified options can't be built
		   throw pce;
		} catch (IOException ioe) {
		   throw ioe;
		}
		return document;
	}

	public static String getNodeValue(Node n) {
		String value = "";
		if (n != null) {
			NodeList children = n.getChildNodes();
			for(int j=0; j<children.getLength(); j++ ) {
				Node child = children.item(j);
				if(child.getNodeType() == Node.TEXT_NODE) {
					value = value + child.getNodeValue();
				}
			}
		}
		return value;
	}   // getNodeValue()

}
