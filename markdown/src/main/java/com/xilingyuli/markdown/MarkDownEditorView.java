package com.xilingyuli.markdown;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by xilingyuli on 2017/2/28.
 */

public class MarkDownEditorView extends EditText {
    public MarkDownEditorView(Context context) {
        super(context);
    }

    public MarkDownEditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarkDownEditorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MarkDownEditorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void header(int level) {
        String source = getText()+"";
        int begin = getLineBeginIndex();
        int end = getNextLineBeginIndex();

        String result = source.substring(begin, end);
        String newStr = "######".substring(0, level) + " ";
        //取消标题
        if(result.startsWith(newStr))
            result = result.replace(newStr,"");
        //增加标题
        else
        {
            result = newStr + result;
            if(begin>0 && source.charAt(begin-1)!='\n')
                result = "\n" + result;
        }
        getText().replace(begin, end, result);
        this.setSelection(begin, end);
    }

    public void insertLine()
    {
        String source = getText()+"";
        int start = getSelectionStart();

        String newStr = "---";
        if(start==0 || source.charAt(start-1)!='\n')
            newStr = "\n"+newStr;
        if(start==source.length() || source.charAt(start)!='\n')
            newStr += "\n";
        getText().insert(start, newStr);
    }

    public void bold()
    {
        textStyle("**");
    }

    public void italic()
    {
        textStyle("_");
    }

    public void strikethrough()
    {
        textStyle("~~");
    }

    private void textStyle(String str)
    {
        int num = str.length();
        String source = getText()+"";
        int start = getSelectionStart();
        int end = getSelectionEnd();

        String result = source.substring(start, end);
        //取消
        if(source.substring(0,start).endsWith(str) && source.substring(end).startsWith(str)) {
            getText().replace(start - num, end + num, result);
            this.setSelection(start - num, end - num);
        }
        //添加
        else {
            getText().replace(start, end, str + result + str);
            this.setSelection(start + num);
        }
    }

    public int getLineBeginIndex()
    {
        String source = getText()+"";
        int lastEnter = source.substring(0,getSelectionStart()).lastIndexOf('\n');
        return lastEnter==-1 ? 0 : lastEnter+1;
    }

    public int getNextLineBeginIndex()
    {
        String source = getText()+"";
        int nextEnter = source.indexOf('\n',getSelectionStart());
        return nextEnter==-1 ? source.length() : nextEnter+1;
    }
}
