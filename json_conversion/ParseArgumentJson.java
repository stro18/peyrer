import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ParseArgumentJson {

	public static void main(String[] args) throws Exception {
		String filename = (args.length > 0) ? args[0] : new Scanner(System.in).nextLine();
		
		JsonFactory jf = new MappingJsonFactory();
		JsonParser jp = jf.createParser(new File(filename));
		ObjectMapper mapper = new ObjectMapper();
		
		JsonToken current = jp.nextToken();
		if(current != JsonToken.START_OBJECT) throw new Exception("Root should be object.");
		
		String outName = filename.substring(0, filename.lastIndexOf(".")) + ".ndjson";
		FileWriter out = new FileWriter(outName);
		
		while(jp.nextToken() != JsonToken.END_OBJECT) {
			String fieldName = jp.getCurrentName();
			current = jp.nextToken();
			if(fieldName.equals("arguments")) {
				if(current == JsonToken.START_ARRAY) {
					while(jp.nextToken() != JsonToken.END_ARRAY) {
						JsonNode node;
						try {
							node = jp.readValueAsTree();
						} catch (JsonParseException e) {
							System.err.println(e.getMessage());
							break;
						}
						out.write(mapper.writeValueAsString(node).replace("\n|\r", ""));
						out.write('\n');
						System.out.println(((FileInputStream) jp.getInputSource()).available() / 1000 / 1000 + "MB left");
					}
				} else {
					System.err.println("'arguments' should be array: skipping.");
					jp.skipChildren();
				}
			} else {
				System.err.println("Unprocessed property: " + fieldName);
				jp.skipChildren();
			}
		}
		out.flush();
		out.close();
	}
}
