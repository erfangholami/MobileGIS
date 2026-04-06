package dev.erfangh.mobilegis.DataModel.Models;

import android.content.Context;
import android.content.SharedPreferences;

import dev.erfangh.mobilegis.DataModel.Constants;
import dev.erfangh.mobilegis.R;

import static android.content.Context.MODE_PRIVATE;

public class Token
{
    private Context context;
    private SharedPreferences tokenPref;

    public Token(Context context)
    {
        this.context = context;
        tokenPref = this.context.getSharedPreferences(this.context.getString(R.string.token_shareprefrence_name), Context.MODE_PRIVATE);
    }
    public Token(Context context, String token)
    {
        this(context);
        writeToken(token);
    }

    public void writeToken(String token)
    {
        tokenPref.edit().putString(context.getString(R.string.token_shareprefrence_key), token).apply();
    }
    public String readToken()
    {
        return tokenPref.getString(context.getString(R.string.token_shareprefrence_key), "");
    }
}
