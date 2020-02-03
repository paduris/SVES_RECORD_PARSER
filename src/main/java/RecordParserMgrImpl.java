/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import parse.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * 
 * @author paduris
 */
public class RecordParserMgrImpl {

	/**
	 * process
	 * 
	 * @param lineStr
	 * @throws FileNotFoundException
	 * @throws JAXBException
	 */
	public String process(String lineStr) throws FileNotFoundException,
			JAXBException {

		String file = "SVESInbound.xml";
		InputStream inputstream = RecordParserMgrImpl.class.getResourceAsStream(file);



		JAXBContext jaxbContext = JAXBContext.newInstance(Interface.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		Interface response = (Interface) jaxbUnmarshaller
				.unmarshal(inputstream);
		List<Line> lines = response.getLine();

		String recordType = String.valueOf(lineStr.charAt(153));
		StringBuilder builder = new StringBuilder();

		for (Line line : lines) {

			if (line.getIdentify().getCharacter().equals(recordType)) {

				builder.append("____________________  " + line.getType()
						+ "______________________\n\n");

				int index = 0;

				List<Section> sectionList = line.getSection();

				for (Section section : sectionList) {
					String fieldValue = null;
					Fields fields = section.getFields();
					List<Field> fieldList = fields.getField();
					for (Field field : fieldList) {

						int fieldLength = Integer.valueOf(field.getLength());
						int length = lineStr.length();
						if (length >= (index + fieldLength)) {
							fieldValue = lineStr.substring(index, index
									+ fieldLength);
						} else {
							fieldValue = lineStr.substring(index, length);
						}
						index = index + fieldLength;
						if (index >= length) {
							break;
						}

						builder.append(pad(field.getName(), 50, ' ')
								+ pad("=", 10, ' ') + " " + fieldValue + "\n");
					}

				}
				builder.append("____________________________________________________________\n\n\n");

			}
		}
		return builder.toString();
	}

	/**
	 * pad
	 * 
	 * @param str
	 * @param size
	 * @param padChar
	 * @return
	 */
	public String pad(String str, int size, char padChar) {
		StringBuffer padded = new StringBuffer(str);
		while (padded.length() < size) {
			padded.append(padChar);
		}
		return padded.toString();
	}


}
