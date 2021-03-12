package com.visionary.googletokens;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    TextView AuthCode, access_token, refresh_token;
    Button btnTokens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AuthCode = findViewById(R.id.authCode);
        access_token = findViewById(R.id.access_token);
        refresh_token = findViewById(R.id.refresh_token);
        btnTokens = findViewById(R.id.btn_token);

        btnTokens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GmailAuth gmailAuth = new GmailAuth();
                gmailAuth.Init(MainActivity.this);
            }
        });
    }

    public class GmailAuth {
        public String clientId = "";
        public String clientSecret = "";
        public GoogleSignInAccount account;
        public String accessToken = "", refreshToken = "";

        public void Init(Context context) {
            //Please setup Google Sign In before this step to prevent any error.
            account = GoogleSignIn.getLastSignedInAccount(context);
            AccessRefreshToken();
        }

        public void AccessRefreshToken() {
            if (account != null) {
                String authCode = account.getServerAuthCode();
                AuthCode.setText(authCode);
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody;

                if (refreshToken.isEmpty()) {
                    requestBody = new FormBody.Builder()
                            .add("grant_type", "authorization_code")
                            .add("client_id", clientId)
                            .add("client_secret", clientSecret)
                            .add("redirect_uri", "")
                            .add("access_type", "offline ")
                            .add("prompt", "consent")
                            .add("code", authCode)
                            .build();
                } else {
                    requestBody = new FormBody.Builder()
                            .add("grant_type", "authorization_code")
                            .add("client_id", clientId)
                            .add("client_secret", clientSecret)
                            .add("redirect_uri", "")
                            .add("code", authCode)
                            .build();
                }

                final Request request = new Request.Builder()
                        .url("https://www.googleapis.com/oauth2/v4/token")
                        .post(requestBody)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response)
                            throws IOException {
                        String json = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            accessToken = jsonObject.getString("access_token");
                            access_token.setText(accessToken);
                            if (jsonObject.getString("refresh_token") != null
                                    && !jsonObject.getString("refresh_token").isEmpty()) {
                                refreshToken = jsonObject.getString("refresh_token");
                                refresh_token.setText(refreshToken);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    }
                });
            }
        }

    }

}