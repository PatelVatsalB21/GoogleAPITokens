# GoogleAPITokens
Getting tokens for access to any Google API

This repository is for demonstration to how to get `access_token`, `refresh_token` and `auth_code` required by any Google API.

Following things are required beforehand

- Client Id and Client Secret
- Google Sign In Integration

# Working

Newly signed google account is refrenced as `account` in code, with `GoogleSignIn.getLastSignedInAccount()` we can get latest signed in account.

**Authcode** can be found by `account.getServerAuthCode()`.

With following entities a POST request is made. They are of two types - `offline` and `online`. 

By using conventional online method we get only **access_token**, while by offline method we get **refresh_token** too.

# Additional Information
For Client Id and Secret we need to click `Create Credential` on [Google Developer Console](https://console.developers.google.com/apis/credentials) and select type `OAuth Client ID`

For Google Sign In integration refer [this](https://developers.google.com/identity/sign-in/android/start-integrating)
