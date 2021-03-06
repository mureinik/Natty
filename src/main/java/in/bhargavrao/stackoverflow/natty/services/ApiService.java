package in.bhargavrao.stackoverflow.natty.services;

import com.google.gson.JsonObject;
import in.bhargavrao.stackoverflow.natty.utils.ApiUtils;
import in.bhargavrao.stackoverflow.natty.utils.JsonUtils;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

/**
 * Created by bhargav.h on 29-Sep-16.
 */
public class ApiService {

    private String apiKey;
    private String autoflagKey;
    private String autoflagToken;
    private String userId;
    private String site;

    private static int quota=0;

    public ApiService(String site){

        PropertyService service = new PropertyService();

        this.site = site;
        this.apiKey = service.getApiKey();
        this.autoflagKey = service.getAutoFlagKey();
        this.autoflagToken = service.getAutoFlagToken();
        this.userId = service.getUserId();

    }

    public JsonObject getQuestionDetailsByIds(List<Integer> questionIdList) throws IOException {
        JsonObject questionJson = ApiUtils.getQuestionDetailsByIds(questionIdList,site,apiKey);
        JsonUtils.handleBackoff(questionJson);
        quota = questionJson.get("quota_remaining").getAsInt();
        return questionJson;
    }

    public JsonObject getQuestionDetailsById(Integer questionId) throws IOException{
        JsonObject questionJson = ApiUtils.getQuestionDetailsById(questionId,site,apiKey);
        JsonUtils.handleBackoff(questionJson);
        quota = questionJson.get("quota_remaining").getAsInt();
        return questionJson;
    }

    public JsonObject getAnswerDetailsById(Integer answerId) throws IOException{
        JsonObject answerJson = ApiUtils.getAnswerDetailsById(answerId,site,apiKey);
        JsonUtils.handleBackoff(answerJson);
        quota = answerJson.get("quota_remaining").getAsInt();
        return answerJson;
    }

    public JsonObject getAnswerDetailsByIds(List<Integer> answerIdList) throws IOException{
        JsonObject answerJson = ApiUtils.getAnswerDetailsByIds(answerIdList,site,apiKey);
        JsonUtils.handleBackoff(answerJson);
        quota = answerJson.get("quota_remaining").getAsInt();
        return answerJson;
    }

    public JsonObject getFirstPageOfAnswers(Instant fromTimestamp) throws IOException{
        JsonObject answersJson = ApiUtils.getFirstPageOfAnswers(fromTimestamp,site,apiKey);
        JsonUtils.handleBackoff(answersJson);
        quota = answersJson.get("quota_remaining").getAsInt();
        return answersJson;
    }
    public JsonObject getAnswerFlagOptions(Integer answerId) throws IOException{
        JsonObject flagOptionsJson = ApiUtils.getAnswerFlagOptions(answerId,site,autoflagKey,autoflagToken);
        JsonUtils.handleBackoff(flagOptionsJson);
        quota = flagOptionsJson.get("quota_remaining").getAsInt();
        return flagOptionsJson;
    }

    public JsonObject flagAnswer(Integer answerId, Integer flagType) throws IOException{
        JsonObject flaggedPost = ApiUtils.FlagAnswer(answerId,flagType,site,autoflagKey,autoflagToken);
        JsonUtils.handleBackoff(flaggedPost);
        quota = flaggedPost.get("quota_remaining").getAsInt();
        return flaggedPost;
    }

    public JsonObject addComment(String comment, Integer postId) throws IOException{
        JsonObject addedComment = ApiUtils.addComment(comment,postId,site,autoflagKey,autoflagToken);
        JsonUtils.handleBackoff(addedComment);
        quota = addedComment.get("quota_remaining").getAsInt();
        return addedComment;
    }

    public JsonObject deleteComment(Integer commentId) throws IOException{
        JsonObject deletedComment  = ApiUtils.deleteComment(commentId,site,autoflagKey,autoflagToken);
        JsonUtils.handleBackoff(deletedComment);
        quota = deletedComment.get("quota_remaining").getAsInt();
        return deletedComment;
    }

    public JsonObject getComments() throws IOException{
        JsonObject commentList = ApiUtils.getComments(Integer.parseInt(userId),site,apiKey);
        JsonUtils.handleBackoff(commentList);
        quota = commentList.get("quota_remaining").getAsInt();
        return commentList;
    }

    public JsonObject getMentions(Instant fromTimestamp) throws IOException{
        JsonObject mentionsList = ApiUtils.getMentions(fromTimestamp, Integer.parseInt(userId),site,apiKey);
        JsonUtils.handleBackoff(mentionsList);
        quota = mentionsList.get("quota_remaining").getAsInt();
        return mentionsList;
    }

    public int getQuota(){
        return quota;
    }
}
