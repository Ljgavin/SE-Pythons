package v1;
import java.util.*;

public class ExternalAnalyzer {
	/*
	 * sysIDS will hold similar symptoms the program suspects the user may have (contains)
	 * sysIDSPriority will extract direct matches from the user so that a symptom can be verified as a symptom after NLP & can be used for potential diagnosis 
	 * keywords will be initialized to the argument given in the class constructor. these words will be processed from NLP and identified as keywords
	 */
    private HashMap<String, String> symptomIDSPriority, symptomIDS; 
    private List<String> diagnoses;
    private List<String> keywords;
    
    
    
    public ExternalAnalyzer(List<String> keywords) {
    	this.keywords = keywords;
    	symptomIDSPriority = new HashMap<String, String>();
    	symptomIDS = new HashMap<String, String>();
    	diagnoses = new ArrayList<String>();
    }
    
    /*
     * A third party site has a database full of various symptoms. These symptoms and respective IDS
     * will be extracted and stored for further analysis
     */
    public void gatherQueryInformation() {
    	String query = Requests.apiCall("https://healthservice.priaid.ch/symptoms?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6Im5pcmF2cDQ1ODJAZ21haWwuY29tIiwicm9sZSI6IlVzZXIiLCJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9zaWQiOiI4NTMzIiwiaHR0cDovL3NjaGVtYXMubWljcm9zb2Z0LmNvbS93cy8yMDA4LzA2L2lkZW50aXR5L2NsYWltcy92ZXJzaW9uIjoiMTA5IiwiaHR0cDovL2V4YW1wbGUub3JnL2NsYWltcy9saW1pdCI6IjEwMCIsImh0dHA6Ly9leGFtcGxlLm9yZy9jbGFpbXMvbWVtYmVyc2hpcCI6IkJhc2ljIiwiaHR0cDovL2V4YW1wbGUub3JnL2NsYWltcy9sYW5ndWFnZSI6ImVuLWdiIiwiaHR0cDovL3NjaGVtYXMubWljcm9zb2Z0LmNvbS93cy8yMDA4LzA2L2lkZW50aXR5L2NsYWltcy9leHBpcmF0aW9uIjoiMjA5OS0xMi0zMSIsImh0dHA6Ly9leGFtcGxlLm9yZy9jbGFpbXMvbWVtYmVyc2hpcHN0YXJ0IjoiMjAyMi0wOS0yMCIsImlzcyI6Imh0dHBzOi8vYXV0aHNlcnZpY2UucHJpYWlkLmNoIiwiYXVkIjoiaHR0cHM6Ly9oZWFsdGhzZXJ2aWNlLnByaWFpZC5jaCIsImV4cCI6MTY2NDkwODgwNSwibmJmIjoxNjY0OTAxNjA1fQ.4liz3k6U8AiKUQPVaGDjBs5ElF5WJwYEbjiKd3RV1Rc&format=json&language=en-gb");
    	
    	List<String> allSymptomIDS = getAllSymptomIDS(query);
    	
    	List<String> allSymptomNames = getAllSymptomNames(query);
    	
    	aggregateUserSymptoms(allSymptomIDS, allSymptomNames);
    	
    	System.out.println("QUERY (FROM API):" + query);
    	System.out.println("SYMPTOM IDS FROM QUERY:" + allSymptomIDS);
    	System.out.println("SYMPTOM NAMES FROM QUERY:" + allSymptomNames);
    }
    
    /*
     * This method will use the symptom ids and symptom names gathered to find corresponding
     * matches (comparing keyword to each of the symptom names). If there is a match found, the
     * id will be stored in the respective list between symptomIDSPriority & symptomIDS
     */
    public void aggregateUserSymptoms(List<String> allSymptomIDS, List<String> allSymptomNames) {
    	for(String keyword : keywords) {
    	    for(int i = 0; i<allSymptomNames.size(); i++) {
    		    String name = allSymptomNames.get(i).toLowerCase();
    		    String symptomID = allSymptomIDS.get(i).toLowerCase();
    		
    		    if(name.equals(keyword))
    		    	symptomIDSPriority.put(symptomID, name);
    		    else if(name.contains(keyword.substring(0, keyword.length() - 1)) || keyword.contains(name))
    		    	symptomIDS.put(symptomID, name);
    		 
    	    }
    	}
    	System.out.println("Priority: " + symptomIDSPriority);
    	System.out.println("Suspected Symptom IDS: " + symptomIDS);
    }
    
    
    /*
     * get all possible symptom ids from the query
     * @return List<String> IDS
     */
    public List<String> getAllSymptomIDS(String query) {
    	List<String> IDS = new ArrayList<>();
    	String id = "";
    	for(int i = 0; i<query.length(); i++) {
    		String current = query.substring(i, i+1);
    		if("0123456789".contains(current)) {
    			id += current;
    		}
    		
    		if(current.equals(",") && !id.equals("")) {
    			IDS.add(id);
    			id = "";
    		}
    	}
    	return IDS;
    }
    
    /*
     * get all possible symptom names from the query
     * @return List<String> symptomNames
     */
    public List<String> getAllSymptomNames(String query) {
    	List<String> symptomNames = new ArrayList<String>();
    	while(query.contains("Name")) {
    		int index = query.indexOf("Name");
    		query = query.substring(index + 7);
    		symptomNames.add(query.substring(0, query.indexOf('"')));
    		query = query.substring(query.indexOf("}"));
    	}
    	return symptomNames;
    }
}
