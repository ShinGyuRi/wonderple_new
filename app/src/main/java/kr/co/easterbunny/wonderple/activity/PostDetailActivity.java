package kr.co.easterbunny.wonderple.activity;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.github.fobid.linkabletext.view.OnLinkClickListener;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.dift.ui.SwipeToAction;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.adapter.CommentAdapter;
import kr.co.easterbunny.wonderple.adapter.TagItemAdapter;
import kr.co.easterbunny.wonderple.adapter.UsersAdapter;
import kr.co.easterbunny.wonderple.databinding.ActivityPostDetailBinding;
import kr.co.easterbunny.wonderple.library.ParentActivity;
import kr.co.easterbunny.wonderple.library.util.AnimationUtil;
import kr.co.easterbunny.wonderple.library.util.Definitions.LIST_TYPE;
import kr.co.easterbunny.wonderple.library.util.JSLog;
import kr.co.easterbunny.wonderple.library.util.NetworkUtil;
import kr.co.easterbunny.wonderple.library.util.SnackbarUtils;
import kr.co.easterbunny.wonderple.library.util.Toaster;
import kr.co.easterbunny.wonderple.library.util.Util;
import kr.co.easterbunny.wonderple.listener.RecyclerItemClickListener;
import kr.co.easterbunny.wonderple.listener.RecyclerViewOnItemClickListener;
import kr.co.easterbunny.wonderple.mention.Mentions;
import kr.co.easterbunny.wonderple.mention.MentionsLoaderUtils;
import kr.co.easterbunny.wonderple.mention.QueryListener;
import kr.co.easterbunny.wonderple.mention.SuggestionsListener;
import kr.co.easterbunny.wonderple.model.CustomBitmapPool;
import kr.co.easterbunny.wonderple.model.LoadCommentResult;
import kr.co.easterbunny.wonderple.model.LoadMentionUserListResult;
import kr.co.easterbunny.wonderple.model.LoadPostResult;
import kr.co.easterbunny.wonderple.model.Mention;
import kr.co.easterbunny.wonderple.dialog.CommentOptionDialog;
import kr.co.easterbunny.wonderple.dialog.CommentReportDeleteDialog;
import kr.co.easterbunny.wonderple.dialog.CommentReportDialog;
import kr.co.easterbunny.wonderple.dialog.CommentReviseOptionDialog;
import kr.co.easterbunny.wonderple.dialog.OptionDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetailActivity extends ParentActivity implements OnLinkClickListener, QueryListener, SuggestionsListener {

    private ActivityPostDetailBinding binding;

    private List<LoadPostResult.TagItem> tagItems = new ArrayList<>();
    private List<LoadCommentResult.Comment> comments = new ArrayList<>();
    private List<String> commentId = new ArrayList<>();
    private List<LoadPostResult.WonderCategory> wonderCategories = new ArrayList<>();
    private List<String> tagUsers = new ArrayList<>();

    private Mentions mentions;
    private MentionsLoaderUtils mentionsLoaderUtils;

    String username = null;
    String profileImgPath = null;
    String uid = null;
    String auid = null;
    String iid = null;
    String imageUrl = null;
//    String commentId = null;
    double positionHeight = 0.0;

    boolean isUndo = false;

    String wonderpleCount = null;
    String wonderCount = null;
    String commentCount = null;
    String checkWonderple = null;
    String checkWonder = null;
    String checkComment = null;

    private UsersAdapter usersAdapter;
    private CommentAdapter commentAdapter;

    private SwipeToAction swipeToAction;

    LinearLayoutManager layoutManager = new LinearLayoutManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_detail);
        binding.setPostDetail(this);
        binding.layoutComment.setContentComment(this);
        binding.layoutCommentField.setCommentField(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initView();
        loadPostData(uid, iid);
        setMentionsList();
        setSendButtonTextWatcher();

    }

    private void initView() {
        binding.imgDot.setVisibility(View.GONE);
        binding.tvWonderple.setVisibility(View.GONE);
        binding.tvWonder.setVisibility(View.GONE);
        binding.tvDesc.setVisibility(View.GONE);
        binding.layoutComment.childLayoutComment.setVisibility(View.GONE);
        binding.btnMention.setVisibility(View.GONE);
        binding.btnHashtag.setVisibility(View.GONE);
        binding.layoutComment.tvPreComment.setVisibility(View.GONE);
        binding.layoutCommentField.btnSend.setEnabled(false);

        Intent intent = getIntent();

        uid = intent.getStringExtra("uid");
        auid = intent.getStringExtra("auid");
        iid = intent.getStringExtra("iid");
        imageUrl = intent.getStringExtra("imgUrl");

        mentions = new Mentions.Builder(this, binding.layoutCommentField.etInputComment)
                .suggestionsListener(this)
                .queryListener(this)
                .build();
        mentionsLoaderUtils = new MentionsLoaderUtils(this, iid, auid);

    }

    /**
     * 게시물 정보 불러오기
     * @param uid user Id
     * @param iid image Id
     */
    private void loadPostData(String uid, String iid) {

        Call<LoadPostResult> loadPostResultCall = NetworkUtil.getInstace().loadPost(iid, uid);
        loadPostResultCall.enqueue(new Callback<LoadPostResult>() {
            @Override
            public void onResponse(Call<LoadPostResult> call, Response<LoadPostResult> response) {
                LoadPostResult loadPostResult = response.body();
                String message = loadPostResult.getMessage().toString();

                if ("the data load succeeded".equals(message)) {
                    profileImgPath = loadPostResult.getPostData().getUser().getImgProfile();
                    username = loadPostResult.getPostData().getUser().getName();
                    wonderpleCount = loadPostResult.getPostData().getWonderpleCount();
                    wonderCount = loadPostResult.getPostData().getWonderCount();
                    commentCount = loadPostResult.getPostData().getCommentCount();
                    positionHeight = Double.parseDouble(loadPostResult.getPostData().getRatio());
                    String descText = loadPostResult.getPostData().getDescText();
                    String uploadTime = loadPostResult.getPostData().getUploadTime();

                    binding.imgPost.setHeightRatio(positionHeight);
                    if (imageUrl != null) {
                        Glide.with(PostDetailActivity.this)
                                .load(imageUrl)
                                .thumbnail(0.1f)
                                .into(binding.imgPost);
                    }

                    if (profileImgPath != null) {
                        Glide.with(PostDetailActivity.this)
                                .load(profileImgPath)
                                .bitmapTransform(new CropCircleTransformation(new CustomBitmapPool()))
                                .into(binding.imgProfile);
                    }

                    binding.tvUsername.setText(username);

                    if (!"0".equals(wonderpleCount) || !"0".equals(wonderCount)) {
                        binding.imgDot.setVisibility(View.VISIBLE);
                    }
                    showWonderpleCount(wonderpleCount);
                    showWonderCount(wonderCount);

                    if (!"".equals(descText)) {
                        binding.tvDesc.setVisibility(View.VISIBLE);
                        binding.tvDesc.setText(descText);
                        binding.tvDesc.setOnLinkClickListener(PostDetailActivity.this);
                    }

                    if (loadPostResult.getTagItems() != null) {
                        tagItems = loadPostResult.getTagItems();
                        wonderCategories = loadPostResult.getWonderCategories();

                        binding.rvTagItem.setAdapter(new TagItemAdapter(PostDetailActivity.this, tagItems, wonderCategories));
                        binding.rvTagItem.setLayoutManager(new LinearLayoutManager(PostDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    }

                    if (!"".equals(loadPostResult.getPostData().getUploadTime())) {
                        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        Date date = null;
                        try {
                            date = inputFormat.parse(uploadTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        DateFormat outputFormat = new SimpleDateFormat("yyyy년 M월 d일 a h:mm");
                        String outputString = outputFormat.format(date);

                        binding.tvDate.setText(outputString);
                    }

                    if (loadPostResult.getPostData() != null) {
                        checkWonderple = loadPostResult.getPostData().getCheckWonderple();
                        checkWonder = loadPostResult.getPostData().getCheckWonder();
                        checkComment = loadPostResult.getPostData().getCheckComment();

                        if (!"0".equals(checkWonderple)) {
                            binding.btnWonderple.setImageResource(R.drawable.posting_tab_wonderple_p);
                        }
                        if (!"0".equals(checkWonder)) {
                            binding.btnWonder.setImageResource(R.drawable.posting_tab_wonder_p);
                        }
                        if (!"0".equals(checkComment)) {
                            binding.btnComment.setImageResource(R.drawable.posting_tab_comment_p);
                        }
                    }

                    setCommentsList();
                }

            }

            @Override
            public void onFailure(Call<LoadPostResult> call, Throwable t) {
                JSLog.E("Error Message: " + t.getMessage(), t);
                JSLog.E("Error Local Message: " + t.getLocalizedMessage(), t);
            }
        });
    }


    private void setMentionsList() {
        binding.layoutUserList.rvMentionList.setLayoutManager(new LinearLayoutManager(this));
        usersAdapter = new UsersAdapter(this);
        binding.layoutUserList.rvMentionList.setAdapter(usersAdapter);

        binding.layoutUserList.rvMentionList.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final LoadMentionUserListResult.Users user = usersAdapter.getItem(position);
                /*
                 * We are creating a mentions object which implements the
                 * <code>Mentionable</code> interface this allows the library to set the offset
                 * and length of the mention.
                 */
                if (user != null) {
                    final Mention mention = new Mention();
                    mention.setMentionName("@" + user.getName());
                    mentions.insertMention(mention);
                }
            }
        }));
    }

    private void setCommentsList() {
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        binding.layoutComment.rvComment.setLayoutManager(layoutManager);

//        StikkyHeaderBuilder.stickTo(binding.scrollView)
//                .setHeader(R.id.child_layout_comment_header, (ViewGroup) getLayoutInflater().inflate(R.layout.content_comment_header, null))
//                .minHeightHeaderDim(R.dimen.post_detail_comment_header_height)
//                .build();

        loadComment();

        onLongClickComment();
    }

    private void onLongClickComment() {
        binding.layoutComment.rvComment.addOnItemTouchListener(new RecyclerViewOnItemClickListener(this, binding.layoutComment.rvComment, new RecyclerViewOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
//                binding.layoutCommentField.etInputComment.append("@"+comments.get(position).getName());
            }

            @Override
            public void onItemLongClick(View v, int position) {
                String cid = commentId.get(position);
                CommentOptionDialog commentOptionDialog = new CommentOptionDialog(PostDetailActivity.this);
                CommentReviseOptionDialog commentReviseOptionDialog = new CommentReviseOptionDialog(PostDetailActivity.this, cid, uid);

                //남 게시물
                if (!uid.equals(auid)) {
                    //남댓글
                    if (!uid.equals(comments.get(position).getAuid())) {
                        //댓글 신고 수행 후
                        if (checkCallIn(position)) {
                            commentReviseOptionDialog.show();
                        }
                        //댓글 신고 수행 전
                        else {
                            CommentReportDialog commentReportDialog = new CommentReportDialog(PostDetailActivity.this, cid, uid, "0");
                            commentReportDialog.show();
                        }
                    }
                    //내댓글
                    else {
                        commentOptionDialog.show();
                        //내 댓글 수정
                        commentOptionDialog.getBinding().btnRevise.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                commentOptionDialog.dismiss();
                                reviseComment(comments.get(position).getDetail());
                            }
                        });
                        //내 댓글 삭제
                        commentOptionDialog.getBinding().btnDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                removeComment(cid, position, commentOptionDialog);
                            }
                        });
                    }
                }
                //내 게시물
                else {
                    //남댓글
                    if (!uid.equals(comments.get(position).getAuid())) {
                        CommentReportDeleteDialog commentReportDeleteDialog = new CommentReportDeleteDialog(PostDetailActivity.this);
                        commentOptionDialog.show();

                        commentReportDeleteDialog.getBinding().btnReport.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //댓글 신고 수행 후
                                if (checkCallIn(position)) {
                                    commentReviseOptionDialog.show();
                                }
                                //댓글 신고 수행 전
                                else {
                                    CommentReportDialog commentReportDialog = new CommentReportDialog(PostDetailActivity.this, cid, uid, "0");
                                    commentReportDialog.show();
                                }
                            }
                        });

                        commentReportDeleteDialog.getBinding().btnDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                commentReportDeleteDialog.dismiss();
                                removeComment(cid, position, commentReportDeleteDialog);
                            }
                        });

                    }
                    //내댓글
                    else {
                        commentOptionDialog.show();
                        //내 댓글 수정
                        commentOptionDialog.getBinding().btnRevise.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                reviseComment(comments.get(position).getDetail());
                            }
                        });
                        //내 댓글 삭제
                        commentOptionDialog.getBinding().btnDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                removeComment(cid, position, commentOptionDialog);
                            }
                        });
                    }
                }
            }
        }));
    }

    private void displaySnackbar() {
        SnackbarUtils.showSnackbar(binding.layoutRoot, R.string.str_remove_comment, R.string.str_undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isUndo = true;
                commentAdapter.notifyDataSetChanged();
            }
        });
    }

    private void removeComment(String cid, int position, Dialog dialog) {
        isUndo = false;
        dialog.dismiss();
        commentAdapter.notifyItemRemoved(position);
        displaySnackbar();

        Util.runDelay(2100, new Runnable() {
            @Override
            public void run() {
                if (!isUndo) {
                    Call<JsonObject> jsonObjectCall = NetworkUtil.getInstace().commentDelete(cid, "0");
                    jsonObjectCall.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            JsonObject jsonObject = response.body();
                            String message = jsonObject.get("message").toString().replace("\"", "");
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {

                        }
                    });
                }
            }
        });


    }

    /**
     * 댓글 수정
     */
    private void reviseComment(String comment) {
        binding.layoutCommentField.childLayoutCommentField.setVisibility(View.VISIBLE);
        binding.layoutCommentField.etInputComment.setText(comment);
    }

    /**
     * 댓글 신고 확인
     */
    boolean result = false;
    private boolean checkCallIn(int position) {
        Call<JsonObject> jsonObjectCall = NetworkUtil.getInstace().checkCallIn(comments.get(position).getCommentId(), uid);
        jsonObjectCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject = response.body();
                String message = jsonObject.get("message").toString().replace("\"", "");

                if ("a comment callin is selected".equals(message)) {
                    result = true;
                } else {
                    result = false;
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

        return result;
    }

    /**
     * 댓글 불러오기
     */
    private void loadComment() {
        Call<LoadCommentResult> loadCommentResultCall = NetworkUtil.getInstace().loadComment(iid, auid);
        loadCommentResultCall.enqueue(new Callback<LoadCommentResult>() {
            @Override
            public void onResponse(Call<LoadCommentResult> call, Response<LoadCommentResult> response) {
                LoadCommentResult loadCommentResult = response.body();
                String message = loadCommentResult.getMessage();

                if ("comment load done".equals(message)) {
                    showCommentCount(commentCount);

                    comments = loadCommentResult.getComments();

                    if (comments != null) {
                        commentAdapter = new CommentAdapter(PostDetailActivity.this, comments, uid);
                        binding.layoutComment.rvComment.setAdapter(commentAdapter);
                        commentAdapter.notifyDataSetChanged();
                        layoutManager.scrollToPositionWithOffset(commentAdapter.getItemCount(), 0);

                        for (int i=0; i<comments.size(); i++) {
                            commentId.add(comments.get(i).getCommentId());
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<LoadCommentResult> call, Throwable t) {

            }
        });
    }

    /**
     * 이전 댓글 불러오기
     */
    private void loadNextComment() {
        Call<LoadCommentResult> loadCommentResultCall = NetworkUtil.getInstace().loadNextComment(iid, uid, "1");
        loadCommentResultCall.enqueue(new Callback<LoadCommentResult>() {
            @Override
            public void onResponse(Call<LoadCommentResult> call, Response<LoadCommentResult> response) {
                LoadCommentResult loadCommentResult = response.body();
                String message = loadCommentResult.getMessage();

                if ("next comment load done".equals(message)) {
                    if (loadCommentResult.getComments() != null) {
                        commentAdapter.add(loadCommentResult.getComments());
                        binding.layoutComment.tvPreComment.setVisibility(View.GONE);

                        for (int i=0; i<loadCommentResult.getComments().size(); i++) {
                            commentId.add(loadCommentResult.getComments().get(i).getCommentId());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<LoadCommentResult> call, Throwable t) {

            }
        });
    }

    private void setSendButtonTextWatcher() {
        binding.layoutCommentField.etInputComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    binding.layoutCommentField.btnSend.setEnabled(true);
                } else {
                    binding.layoutCommentField.btnSend.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, WonderpleListActivity.class);
        intent.putExtra("iid", iid);
        intent.putExtra("uid", uid);

        switch (view.getId()) {
            case R.id.tv_pre_comment:
                loadNextComment();
                break;

            case R.id.btn_comment:
                onClickCommentBtn();
                break;

            case R.id.btn_wonderple:
                onClickWonderpleBtn();
                break;

            case R.id.btn_wonder:
                onClickWonderBtn();
                break;

            case R.id.btn_follow_report:
                OptionDialog dialog = new OptionDialog(this, uid, auid, iid);
                dialog.show();
                break;

            case R.id.btn_mention:
                binding.layoutCommentField.etInputComment.append("@");
                break;

            case R.id.btn_hashtag:
                binding.layoutCommentField.etInputComment.append("#");
                break;

            case R.id.btn_send:
                sendComment();
                break;

            case R.id.tv_wonderple:
                intent.putExtra("listType", LIST_TYPE.WONDERPLE);
                startActivity(intent);
                break;

            case R.id.tv_wonder:
                intent.putExtra("listType", LIST_TYPE.WONDER);
                startActivity(intent);

            case R.id.btn_back:
                onBackPressed();
                break;

        }
    }


    private void onClickCommentBtn() {
        binding.layoutCommentField.childLayoutCommentField.setVisibility(View.VISIBLE);
        binding.layoutCommentField.etInputComment.requestFocus();
//        KeyboardUtils.showKeyboard();
        binding.layoutCommentField.etInputComment.setFocusable(true);
        binding.layoutCommentField.etInputComment.setFocusableInTouchMode(true);

        if (binding.layoutCommentField.etInputComment.hasFocus()) {
            binding.btnMention.setVisibility(View.VISIBLE);
            binding.btnHashtag.setVisibility(View.VISIBLE);
        }
    }

    private void onClickWonderpleBtn() {

        /**
         * if ----> Cancel Wonderple
         * else if ----> Apply Wonderple
         */
        if (!"0".equals(checkWonderple)) {
            binding.btnWonderple.setImageResource(R.drawable.posting_tab_wonderple_n);
            wonderpleCount = String.valueOf(Integer.parseInt(wonderpleCount) - 1);
            showWonderpleCount(wonderpleCount);
            checkWonderple = "0";
        } else if ("0".equals(checkWonderple)) {
            binding.btnWonderple.setImageResource(R.drawable.posting_tab_wonderple_p);
            wonderpleCount = String.valueOf(Integer.parseInt(wonderpleCount) + 1);
            showWonderpleCount(wonderpleCount);
            checkWonderple = "1";
        }

        Call<JsonObject> jsonObjectCall = NetworkUtil.getInstace().wonderpleApply(iid, uid, auid);
        jsonObjectCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject = response.body();
                String message = jsonObject.get("message").toString().replace("\"", "");

                if ("wonderple applied".equals(message)) {
                    JSLog.D("wonderple applied", new Throwable());
                } else if ("wonderple deleted".equals(message)) {
                    JSLog.D("wonderple deleted", new Throwable());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }

    private void onClickWonderBtn() {
        Intent intent = new Intent(this, WonderActivity.class);
        intent.putExtra("tagItems", (Serializable) tagItems);
        intent.putExtra("imageUrl", imageUrl);
        intent.putExtra("iid", iid);
        intent.putExtra("uid", uid);
        intent.putExtra("auid", auid);
        startActivity(intent);

    }

    private void sendComment() {
        String comment = binding.layoutCommentField.etInputComment.getText().toString();
        Call<JsonObject> jsonObjectCall = NetworkUtil.getInstace().commentApply(iid, uid, comment);
        jsonObjectCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject = response.body();
                String message = jsonObject.get("message").toString().replace("\"", "");

                if ("comment applied".equals(message)) {

                    String commentText = comment;

//        ArrayList<int[]> hashtagSpans = getSpans(commentText, '#');

//        if (hashtagSpans != null) {
//            for (int i=0; i<hashtagSpans.size(); i++) {
//                int[] span = hashtagSpans.get(i);
//                int hashTagStart = span[0];
//                int hashTagEnd = span[1];
//
//                commentsContent.setSpan(new Hashtag(this), hashTagStart, hashTagEnd, 0);
//            }
//        }

                    ArrayList<int[]> calloutSpans = getSpans(commentText, '@');

                    SpannableString commentsContent = new SpannableString(commentText);

                    if (calloutSpans != null) {
                        for(int i = 0; i < calloutSpans.size(); i++) {
                            int[] span = calloutSpans.get(i);
                            int calloutStart = span[0];
                            int calloutEnd = span[1];

                            tagUsers.add(commentsContent.subSequence(calloutStart + 1, calloutEnd).toString());
                        }
                    }

                    if (tagUsers != null) {
                        requestNoticeToUser(iid, uid, jsonObject.get("comment_id").toString().replace("\"", ""), String.valueOf(tagUsers.size()), tagUsers);
                        tagUsers.clear();
                    }


                    binding.layoutCommentField.etInputComment.setText("");
                    commentCount = String.valueOf(Integer.parseInt(commentCount) + 1);
                    loadComment();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void requestNoticeToUser(String iid, String uid, String cid, String tagCount, List<String> tag) {
        Call<JsonObject> jsonObjectCall = NetworkUtil.getInstace().commentApplyTo(iid, uid, cid, tagCount, tag);
        jsonObjectCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject = response.body();
                String message = jsonObject.get("message").toString().replace("\"", "");

                if ("comment applied to all tagged users".equals(message)) {
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                JSLog.E("request notice to user error", t);
            }
        });
    }




    private void showWonderpleCount(String wonderpleCount) {
        if (!"0".equals(wonderpleCount)) {
            binding.tvWonderple.setText(getString(R.string.str_post_detail_wonderple_count, wonderpleCount));
            binding.tvWonderple.setVisibility(View.VISIBLE);
            AnimationUtil.setAnimation(binding.tvWonderple);
        } else {
            binding.tvWonderple.setVisibility(View.GONE);
        }
    }

    private void showWonderCount(String wonderCount) {
        if (!"0".equals(wonderCount)) {
            binding.tvWonder.setText(getString(R.string.str_post_detail_wonder_count, wonderCount));
            binding.tvWonder.setVisibility(View.VISIBLE);
            AnimationUtil.setAnimation(binding.tvWonder);
        }
    }

    private void showCommentCount(String commentCount) {
        if (!"0".equals(commentCount)) {
            if (Integer.parseInt(commentCount) > 10) {
                binding.layoutComment.tvPreComment.setVisibility(View.VISIBLE);
            }
        }
        if (!"0".equals(commentCount)) {
            binding.layoutComment.childLayoutComment.setVisibility(View.VISIBLE);
            binding.layoutComment.layoutCommentHeader.tvComment.setText(getString(R.string.str_post_detail_comment_count, commentCount));
            AnimationUtil.setAnimation(binding.layoutComment.layoutCommentHeader.tvComment);
        }
    }

    @Override
    public void onHashtagClick(String hashtag) {
        Toaster.shortToast("Clicked hashtag is " + hashtag);
    }

    @Override
    public void onMentionClick(String mention) {
        Toaster.shortToast("Mention");
    }

    @Override
    public void onEmailAddressClick(String email) {

    }

    @Override
    public void onWebUrlClick(String url) {

    }

    @Override
    public void onPhoneClick(String phone) {

    }

    @Override
    public void onQueryReceived(String query) {
        final List<LoadMentionUserListResult.Users> users = mentionsLoaderUtils.searchUsers(query);
        if (users != null && !users.isEmpty()) {
            usersAdapter.clear();
            usersAdapter.setCurrentQuery(query);
            usersAdapter.addAll(users);
            showMentionsList(true);
        } else {
            showMentionsList(false);
        }
    }

    @Override
    public void displaySuggestions(boolean display) {
        if (display) {
            binding.layoutUserList.childLayoutUserList.setVisibility(View.VISIBLE);
        } else {
            binding.layoutUserList.childLayoutUserList.setVisibility(View.GONE);
        }

    }

    private void showMentionsList(boolean display) {
        binding.layoutUserList.childLayoutUserList.setVisibility(View.VISIBLE);
        if (display) {
            binding.layoutUserList.rvMentionList.setVisibility(View.VISIBLE);
        } else {
            binding.layoutUserList.rvMentionList.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (binding.layoutCommentField.childLayoutCommentField.getVisibility() == View.VISIBLE) {
            binding.layoutCommentField.childLayoutCommentField.setVisibility(View.GONE);
            binding.btnMention.setVisibility(View.GONE);
            binding.btnHashtag.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }
}
