package cst.baseappsetup.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Session(
    @SerializedName("JwtToken")
    @Expose val accessToken: String,
    @SerializedName("isFirstLogin")
    @Expose val firstLogin: Boolean/*,
    @SerializedName("refresh_token")
    @Expose val refreshToken: String TODO find usages and uncomment*/,
    @SerializedName("hasTemporaryPassword")
    @Expose val hasTemporaryPassword: Boolean
)




