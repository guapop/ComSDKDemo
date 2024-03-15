package com.lucasliu.serialporthelper;






import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

/**
 * @Company    :
 * @Author     :  Lucas     联系WX:780203920
 * @Date       :2019/4/3 0003 17:24
 * @Description :LogListAdapter.java
 *
 * <String, BaseViewHolder>
 */
public class LogListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public LogListAdapter(List list) {
        super(R.layout.item_layout, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

        helper.setText(R.id.textView, item);

    }

}
