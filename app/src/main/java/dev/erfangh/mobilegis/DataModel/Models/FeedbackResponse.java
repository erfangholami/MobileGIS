package dev.erfangh.mobilegis.DataModel.Models;

import com.google.gson.annotations.SerializedName;

public class FeedbackResponse
{
    @SerializedName("SendFeedbackResult")
    private boolean isSent;

    public FeedbackResponse(boolean isSent)
    {
        this.isSent = isSent;
    }

    public boolean isSent()
    {
        return isSent;
    }

    public void setSent(boolean sent)
    {
        isSent = sent;
    }
}
