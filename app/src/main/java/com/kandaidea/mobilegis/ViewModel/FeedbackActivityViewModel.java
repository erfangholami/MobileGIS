package com.kandaidea.mobilegis.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.kandaidea.mobilegis.DataModel.Constants;
import com.kandaidea.mobilegis.DataModel.Models.Token;
import com.kandaidea.mobilegis.DataModel.Retrofit.RetrofitMethods;

public class FeedbackActivityViewModel extends ViewModel
{
    Context context;
    private RetrofitMethods retrofitMethods;
    private Token token;

    public void sendFeedback(String message)
    {
        retrofitMethods.sendFeedback(token.readToken(), message);

    }

    public void init(Context applicationContext)
    {
        context = applicationContext;
        retrofitMethods = new RetrofitMethods();
        token = new Token(context);
    }
}
