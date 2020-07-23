package at.kapschcs.testing;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ParseJsonTest extends AbstractBaseTest {
	
	File jsonFile;
	
	@Test
	public void testReadFileSuccess() {
		ClassPathResource resource = new ClassPathResource("create.json");
		assertTrue(resource.getPath() != null, "resource not found");
		
		try {
		jsonFile = resource.getFile();
		ObjectMapper mapper = new ObjectMapper();
		
		Map<String, String> rootMap = new HashMap<>();
	
		JsonNode root = mapper.readTree(jsonFile);
				
		JsonNode paramsNode = root.get("params");
		
		
		System.out.println(paramsNode.toPrettyString());
		
		Map<String, String> paramsMap = mapper.convertValue(paramsNode, new TypeReference<Map<String,String>>(){});
		assertTrue(paramsMap.size()>0,"no k/v pairs in map");
			
		
		for (Map.Entry<String, String> e : paramsMap.entrySet()) {
			System.out.println(e.getKey() + " "+ e.getValue());
		}
		
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
	}
	
	@Test
	public void testRemoveNodeSuccess() {
		ClassPathResource resource = new ClassPathResource("create.json");
		assertTrue(resource.getPath() != null, "resource not found");
		
		try {
			File jsonFile = resource.getFile();
			ObjectMapper mapper = new ObjectMapper();
			ObjectNode rootObjectNode = (ObjectNode) mapper.readTree(jsonFile);
			ObjectNode paramsObjectNode = (ObjectNode) rootObjectNode.get("params");
			
			rootObjectNode.remove("params");
			
			Map<String,String> rootMap = mapper.convertValue(rootObjectNode, new TypeReference<Map<String,String>>() {});
			Map<String, String> paramsMap = mapper.convertValue(paramsObjectNode, new TypeReference<Map<String,String>>() {});
			
			for (Map.Entry<String, String> e : rootMap.entrySet()) {
				System.out.println("root: " + e.getKey() + " " + e.getValue());
			}
			
			for (Map.Entry<String, String> e : paramsMap.entrySet()) {
				System.out.println("params: " + e.getKey() +  " " + e.getValue());
			}
		
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
	}

}
