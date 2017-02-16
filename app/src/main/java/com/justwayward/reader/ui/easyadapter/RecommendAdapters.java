package com.justwayward.reader.ui.easyadapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.appbook.book.R;
import com.justwayward.reader.base.Constant;
import com.justwayward.reader.bean.Recommend;
import com.justwayward.reader.manager.SettingManager;
import com.justwayward.reader.utils.FileUtils;
import com.justwayward.reader.utils.FormatUtils;
import com.justwayward.reader.view.recyclerview.adapter.BaseViewHolder;
import com.justwayward.reader.view.recyclerview.adapter.RecyclerArrayAdapter;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.NumberFormat;

/**
 * @author yuyh.
 * @date 2016/9/7.
 */
public class RecommendAdapters extends RecyclerArrayAdapter<Recommend.RecommendBooks> {
    public RecommendAdapters(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(final ViewGroup parent, int viewType) {
        return new BaseViewHolder<Recommend.RecommendBooks>(parent, R.layout.item_recommend_list) {
            @Override
            public void setData(final Recommend.RecommendBooks item) {
                super.setData(item);
                if (item.isNetwore) {
                    View view = getView(R.id.ivRecommendCover);
                    Picasso.with(mContext).load(item.bookInfo.getLogo()).into((ImageView) view);
                    holder.setText(R.id.tvRecommendTitle,item.bookInfo.getBook_name()).setText(R.id.tvRecommendShort,item.bookInfo.getBook_author()+" | "+item.bookInfo.getBook_number());
                } else {
                    String latelyUpdate = "";
                    if (!TextUtils.isEmpty(FormatUtils.getDescriptionTimeFromDateString(item.updated))) {
                        latelyUpdate = FormatUtils.getDescriptionTimeFromDateString(item.updated) + ":";
                    }
                    holder.setText(R.id.tvRecommendTitle, item.title)
                            .setText(R.id.tvLatelyUpdate, latelyUpdate)
                            .setText(R.id.tvRecommendShort, item.lastChapter)
                            .setVisible(R.id.ivTopLabel, item.isTop)
                            .setVisible(R.id.ckBoxSelect, item.showCheckBox)
                            .setVisible(R.id.ivUnReadDot, FormatUtils.formatZhuiShuDateString(item.updated)
                                    .compareTo(item.recentReadingTime) > 0);
                    if (item.isFromSD) {
                        holder.setImageResource(R.id.ivRecommendCover, R.drawable.home_shelf_txt_icon);
                        File file = new File(FileUtils.getChapterPath(item._id, 1));
                        if (file.exists()) {
                            double progress = ((double) SettingManager.getInstance().getReadProgress(item._id)[2]) / file.length();
                            NumberFormat fmt = NumberFormat.getPercentInstance();
                            fmt.setMaximumFractionDigits(2);
                            holder.setText(R.id.tvRecommendShort, "当前阅读进度：" + fmt.format(progress));
                        }
                    } else if (!SettingManager.getInstance().isNoneCover()) {
                        holder.setRoundImageUrl(R.id.ivRecommendCover, Constant.IMG_BASE_URL + item.cover,
                                R.drawable.cover_default);
                    } else {
                        holder.setImageResource(R.id.ivRecommendCover, R.drawable.cover_default);
                    }

                }

                CheckBox ckBoxSelect = holder.getView(R.id.ckBoxSelect);
                ckBoxSelect.setChecked(item.isSeleted);
                ckBoxSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                            item.isSeleted = true;
                        } else {
                            item.isSeleted = false;
                        }
                    }
                });
            }
        };
    }

}
