package v1;
import java.util.*;

public class Driver {
    public static void main(String[] args) {
    	
    	
    	List<String> keywords = new ArrayList<String>();
    	keywords.add("sneeze"); // arr should eventually contain ALL keywords from the user speech so it can be analyzed.
    	
    	ExternalAnalyzer ext = new ExternalAnalyzer(keywords);
    	ext.gatherQueryInformation(); // I have placed print statements within multiple methods that is called from here.
    }
}
