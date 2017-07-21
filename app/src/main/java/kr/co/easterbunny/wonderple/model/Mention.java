package kr.co.easterbunny.wonderple.model;

import android.widget.EditText;

import kr.co.easterbunny.wonderple.mention.Mentionable;


/**
 * A mention inserted into the {@link EditText}. All mentions inserted into the
 * {@link EditText} must implement the {@link Mentionable} interface.
 */
public class Mention implements Mentionable {

    private String mentionName;

    private int offset;

    private int length;

    @Override
    public String toString() {
        return null;
    }

    @Override
    public int getMentionOffset() {
        return offset;
    }

    @Override
    public int getMentionLength() {
        return length;
    }

    @Override
    public String getMentionName() {
        return mentionName;
    }

    @Override
    public void setMentionOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public void setMentionLength(int length) {
        this.length = length;
    }

    @Override
    public void setMentionName(String mentionName) {
        this.mentionName = mentionName;
    }
}
