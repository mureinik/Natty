package in.bhargavrao.stackoverflow.natty.model;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Arrays;

/**
 * Created by bhargav.h on 11-Sep-16.
 */
public class Post {
    private String title;
    private String mainTag;
    private Instant answerCreationDate;
    private Instant questionCreationDate;
    private Integer answerID;
    private Integer questionID;
    private String body;
    private String bodyMarkdown;
    private String[] tags;
    private SOUser asker;
    private SOUser answerer;
    private String siteName;
    private String siteUrl;

    public Post() {
    }

    public Post(String title, String mainTag, Instant answerCreationDate, Instant questionCreationDate,
                Integer answerID, Integer questionID, String body, String bodyMarkdown,
                String[] tags, SOUser asker, SOUser answerer, String siteName, String siteUrl) {
        this.title = title;
        this.mainTag = mainTag;
        this.answerCreationDate = answerCreationDate;
        this.questionCreationDate = questionCreationDate;
        this.answerID = answerID;
        this.questionID = questionID;
        this.body = body;
        this.bodyMarkdown = bodyMarkdown;
        this.tags = tags;
        this.asker = asker;
        this.answerer = answerer;
        this.siteName = siteName;
        this.siteUrl = siteUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instant getAnswerCreationDate() {
        return answerCreationDate;
    }

    public void setAnswerCreationDate(Instant answerCreationDate) {
        this.answerCreationDate = answerCreationDate;
    }

    public Instant getQuestionCreationDate() {
        return questionCreationDate;
    }

    public void setQuestionCreationDate(Instant questionCreationDate) {
        this.questionCreationDate = questionCreationDate;
    }

    public Integer getAnswerID() {
        return answerID;
    }

    public void setAnswerID(Integer answerID) {
        this.answerID = answerID;
    }

    public Integer getQuestionID() {
        return questionID;
    }

    public void setQuestionID(Integer questionID) {
        this.questionID = questionID;
    }

    public String getMainTag() {
        return mainTag;
    }

    public void setMainTag(String mainTag) {
        this.mainTag = mainTag;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBodyMarkdown() {
        return bodyMarkdown;
    }

    public void setBodyMarkdown(String bodyMarkdown) {
        this.bodyMarkdown = bodyMarkdown;
    }

    public SOUser getAsker() {
        return asker;
    }

    public void setAsker(SOUser asker) {
        this.asker = asker;
    }

    public SOUser getAnswerer() {
        return answerer;
    }

    public void setAnswerer(SOUser answerer) {
        this.answerer = answerer;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    @Override
    public String toString() {

        JsonObject json = getJson();
        return json.toString();
    }

    @NotNull
    private JsonObject getJson() {
        JsonObject json = new JsonObject();

        json.addProperty("title" , title );
        json.addProperty("mainTag" , mainTag );
        json.addProperty("answerCreationDate" , answerCreationDate.toString());
        json.addProperty("questionCreationDate" , questionCreationDate.toString());
        json.addProperty("answerID" , answerID);
        json.addProperty("questionID" , questionID);
        json.addProperty("body" , body );
        json.addProperty("bodyMarkdown" , bodyMarkdown);
        json.addProperty("tags" , Arrays.toString(tags));
        json.add("asker" , asker.getJson());
        json.add("answerer" , answerer.getJson());
        return json;

    }
}
