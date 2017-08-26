package in.bhargavrao.stackoverflow.natty.filters;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializer;

import in.bhargavrao.stackoverflow.natty.model.Post;
import in.bhargavrao.stackoverflow.natty.utils.CheckUtils;
import in.bhargavrao.stackoverflow.natty.utils.FilePathUtils;

public class NewBlacklistedFilter implements Filter {

	private Post post;
    private double value;
    private double maxValue;
    private String listedWord;

    public NewBlacklistedFilter(Post post) {
        this.post = post;
        value = 0;
        maxValue = 2.5;
        listedWord = null;
    }
	
	@Override
	public boolean filter() {
		//the usual check first
		String listedWord = CheckUtils.checkForBlackListedWords(post);
        if(listedWord!=null){
            this.listedWord = listedWord;
            //System.out.println(this.listedWord + " is blackisted");
            
            //calculate the value
            String jsonString;
            try {
            	jsonString = new String(Files.readAllBytes(Paths.get(FilePathUtils.intelligentBlacklistFile)));
            } catch (Throwable e) {
            	e.printStackTrace();
            	return false;
            }
                        
            JsonParser parser = new JsonParser();
            
            JsonElement list = null;
            JsonArray array = null;
            try {
            	list = parser.parse(jsonString);
            	array = list.getAsJsonArray();
            } catch (Throwable e) {
            	e.printStackTrace();
            	return false;
            }
                                    
            for(JsonElement item : array) {
            	JsonObject object = null;
            	
            	try {
            		object = item.getAsJsonObject();
            	} catch (Throwable e) {
                	e.printStackTrace();
                	return false;
                }
            	            	
            	String phrase = object.get("phrase").getAsString();
            	if (phrase != null && phrase.equals(this.listedWord)) {
            		            		
            		JsonObject feedbacks = object.getAsJsonObject("feedback_counts");
            		JsonElement tpObj = feedbacks.get("tp");
            		JsonElement fpObj = feedbacks.get("fp");
            		JsonElement tnObj = feedbacks.get("tn");
            		JsonElement neObj = feedbacks.get("ne");
            		
            		double tp = tpObj != null ? tpObj.getAsDouble() : 0;
        			double fp = fpObj != null ? fpObj.getAsDouble() : 0;
        			double tn = tnObj != null ? tnObj.getAsDouble() : 0;
        			double ne = neObj != null ? neObj.getAsDouble() : 0;
        			
        			double totalPosts = tp+fp+tn+ne;
            		
            		//Score calculation: ((1−fps/postsMatchingThePhrase)^maxScore) * maxScore
            		this.value = Math.pow((1 - (fp / totalPosts)), this.maxValue) * this.maxValue;
            		
            		//round value
            		this.value = Math.round(this.value*10.0)/10.0;
            		
            		return true;
            		
            	}
            }
            
            return true;
        }
        return false;
	}

	@Override
    public double getValue() {
        return value;
    }

    @Override
    public String description() {
        return "Contains Blacklisted Word - "+listedWord;
    }

}
