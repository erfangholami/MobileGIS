package dev.erfangh.mobilegis.viewmodel;

import androidx.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import dev.erfangh.mobilegis.DataModel.Constants;
import dev.erfangh.mobilegis.DataModel.Models.FeedbackResponse;
import dev.erfangh.mobilegis.DataModel.Models.Token;
import dev.erfangh.mobilegis.DataModel.Retrofit.RetrofitMethods;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class FeedbackActivityViewModel extends ViewModel
{
    private static final String TAG = FeedbackActivityViewModel.class.getSimpleName();
    private Context context;
    private RetrofitMethods retrofitMethods;
    private Token token;

    public void sendFeedback(String message)
    {
        retrofitMethods.sendFeedback(token.readToken(), message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<FeedbackResponse>()
                {
                    @Override
                    public void onNext(FeedbackResponse feedbackResponse)
                    {
                        Log.v(TAG, "FeedbackResponse " + feedbackResponse.isSent());
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        Log.v(TAG, "FeedbackResponseError " + e.getMessage());
                    }

                    @Override
                    public void onComplete()
                    {
                        Log.v(TAG, "FeedbackResponseComplete");
                    }
                });

    }

    public void init(Context applicationContext)
    {
        context = applicationContext;
        retrofitMethods = new RetrofitMethods();
        token = new Token(context);
    }
}
