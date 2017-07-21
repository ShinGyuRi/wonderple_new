package kr.co.easterbunny.wonderple.mention;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.library.util.NetworkUtil;
import kr.co.easterbunny.wonderple.model.LoadMentionUserListResult;
import kr.co.easterbunny.wonderple.model.MentionUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Converts all the user data in the file users.json to an array of {@link MentionUser} objects.
 */
public class MentionsLoaderUtils {

    private final Context context;
    private List<LoadMentionUserListResult.Users> userList;
    String iid, uid;

    public MentionsLoaderUtils(final Context context, String iid, String uid) {
        this.context = context;
        this.uid = uid;
        this.iid = iid;
        loadUsers();
    }

    /**
     * Loads users
     */
    private void loadUsers() {
        Call<LoadMentionUserListResult> loadMentionUserListResultCall = NetworkUtil.getInstace().loadUserList(iid, uid);
        loadMentionUserListResultCall.enqueue(new Callback<LoadMentionUserListResult>() {
            @Override
            public void onResponse(Call<LoadMentionUserListResult> call, Response<LoadMentionUserListResult> response) {
                LoadMentionUserListResult loadMentionUserListResult = response.body();

                String message = loadMentionUserListResult.getMessage();

                if ("user list load done".equals(message)) {
                    if (loadMentionUserListResult.getUsers() != null) {
                        userList = loadMentionUserListResult.getUsers();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoadMentionUserListResult> call, Throwable t) {

            }
        });
    }

    /**
     * Search for user with name matching {@code query}.
     *
     * @return a list of users that matched {@code query}.
     */
    public List<LoadMentionUserListResult.Users> searchUsers(String query) {
        final List<LoadMentionUserListResult.Users> searchResults = new ArrayList<>();
        if (StringUtils.isNotBlank(query)) {
            query = query.toLowerCase(Locale.US);
            if (userList != null && !userList.isEmpty()) {
                for (LoadMentionUserListResult.Users user : userList) {
                    final String name = user.getName().toLowerCase();
                    if (name.startsWith(query) || name.contains(query)) {
                        searchResults.add(user);
                    }
                }
            }

        }
        return searchResults;
    }

}
