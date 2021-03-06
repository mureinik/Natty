package in.bhargavrao.stackoverflow.natty.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.tunaki.stackoverflow.chat.Message;
import fr.tunaki.stackoverflow.chat.Room;
import in.bhargavrao.stackoverflow.natty.services.ApiService;
import in.bhargavrao.stackoverflow.natty.services.FileStorageService;
import in.bhargavrao.stackoverflow.natty.services.StorageService;
import in.bhargavrao.stackoverflow.natty.utils.CheckUtils;
import in.bhargavrao.stackoverflow.natty.utils.CommandUtils;
import in.bhargavrao.stackoverflow.natty.utils.SentinelUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bhargav.h on 20-Oct-16.
 */
public class Fetch implements SpecialCommand {

    private Message message;
    private String sitename;
    private String siteurl;

    public Fetch(Message message, String sitename, String siteurl) {
        this.message = message;
        this.sitename = sitename;
        this.siteurl = siteurl;
    }

    @Override
    public boolean validate() {

        return CommandUtils.checkForCommand(message.getPlainContent(),"fetch");
    }

    @Override
    public String description() {
        return "Returns a list of posts that need feedback, See the wiki for more details";
    }

    @Override
    public void execute(Room room) {

        String data = CommandUtils.extractData(message.getPlainContent()).trim();
        StorageService service = new FileStorageService();
        try{
            List<String> lines = service.getReports(sitename);
            String returnString = String.join("; ", lines);

            if(data.split(" ")[0].equals("links") && lines.size()!=0) {
                if (lines.size() == 0)
                    room.replyTo(message.getId(), "All reports have been tended to");
                else {
                    int stopValue = lines.size();
                    if(data.split(" ").length==2&&CheckUtils.checkIfInteger(data.split(" ")[1])){
                        stopValue = Integer.parseInt(data.split(" ")[1]);
                    }

                    String links = "";
                    int i = 0;
                    for(String line: lines) {
                        links += "["+line.trim()+"](//"+siteurl+"/a/"+line.trim()+"); ";
                        i++;
                        if(i==stopValue)
                            break;
                    }
                    room.replyTo(message.getId(), links);
                }
            }
            else if(data.split(" ")[0].equals("FMS") && lines.size()!=0) {

                String links = "";
                for(String line: lines) {
                    links += "["+line.trim()+"](http://51.254.218.90:8000/Natty/"+line.trim()+".html); ";
                }
                room.replyTo(message.getId(), links);
            }
            else if(data.split(" ")[0].equals("deleted") && lines.size()!=0) {
                if(lines.size()>100)
                {
                    room.send("There are more than 100 requests. Hence fetching deleted posts from the first 100 only");
                    lines = lines.subList(0,100);
                }
                ApiService apiService = new ApiService(siteurl);
                List<Integer> answerIds = lines.stream().map(Integer::parseInt).collect(Collectors.toList());
                JsonObject answersJson = apiService.getAnswerDetailsByIds(answerIds);

                if(answersJson.has("items")){
                    JsonArray answers = answersJson.get("items").getAsJsonArray();
                    for (JsonElement element: answers){
                        JsonObject answer = element.getAsJsonObject();
                        String answerId = answer.get("answer_id").getAsString();
                        int index = lines.indexOf(answerId);
                        if (index!=-1){
                            lines.remove(index);
                        }
                    }
                }
                String links = "";
                for(String line: lines) {
                    links += "["+line.trim()+"](//"+siteurl+"/a/"+line.trim()+"); ";
                }

                if(links.trim().equals(""))
                    links = "There are no deleted posts";
                room.replyTo(message.getId(), links);
            }
            else if(data.split(" ")[0].equals("nondeleted") && lines.size()!=0) {
                if(lines.size()>100)
                {
                    room.send("There are more than 100 requests. Hence fetching non deleted posts from the first 100 only");
                    lines = lines.subList(0,100);
                }
                ApiService apiService = new ApiService(siteurl);
                List<Integer> answerIds = lines.stream().map(Integer::parseInt).collect(Collectors.toList());
                JsonObject answersJson = apiService.getAnswerDetailsByIds(answerIds);
                String links = "";

                if(answersJson.has("items")){
                    JsonArray answers = answersJson.get("items").getAsJsonArray();
                    for (JsonElement element: answers){
                        JsonObject answer = element.getAsJsonObject();
                        String answerId = answer.get("answer_id").getAsString();
                        links += "["+answerId+"](//"+siteurl+"/a/"+answerId+"); ";
                    }
                }

                if(links.trim().equals(""))
                    links = "There are no non deleted posts";
                room.replyTo(message.getId(), links);
            }
            else if(data.split(" ")[0].toLowerCase().equals("sentinel") && lines.size()!=0) {
                String links = "";
                for(String line: lines) {
                    String postId = line.trim();
                    String sentinelId = service.getSentinelId(sitename,postId);
                    sentinelId = sentinelId.replace(postId+",","");
                    if(sentinelId.equals("-1"))
                        links+= postId+"; ";
                    else
                        links+= "["+postId+"]("+SentinelUtils.getSentinelMainUrl(sitename)+"/posts/"+sentinelId+"); ";
                }
                room.replyTo(message.getId(), links);
            }
            else if((data.split(" ")[0].equals("amount") || data.split(" ")[0].equals("count") || data.split(" ")[0].equals("number"))  && lines.size()!=0) {
                room.replyTo(message.getId(), Integer.toString(lines.size()));
            }
            else {

                if (lines.size() == 0)
                    room.replyTo(message.getId(), "All reports have been tended to");
                else
                    room.replyTo(message.getId(), returnString);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }


    }

    @Override
    public String name() {
        return "fetch";
    }
}
