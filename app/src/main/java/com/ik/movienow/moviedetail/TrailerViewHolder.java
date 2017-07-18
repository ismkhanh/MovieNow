package com.ik.movienow.moviedetail;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ik.movienow.R;
import com.ik.movienow.common.Config;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


class TrailerViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_trailer_number) TextView tvTrailerNumber;
    @BindView(R.id.tv_trailer_label) TextView tvTrailerLabel;
    @BindView(R.id.horizontal_line) View horizontalLine;
    TrailerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    /**
     * Sets the trailer number to the textview and
     * also the video id is set as a tag to retrieve it inside the onClick method
     *
     * @param number trailer number
     * @param videoId Id of the video
     * @param showLabel if true will show Trailer label. Only the first item be passed true
     */
    void setTrailerNumber(String number, String videoId, boolean showLabel){
        tvTrailerNumber.setText(String.format("Trailer %s", number));
        tvTrailerNumber.setTag(videoId);
        if (showLabel) {
            tvTrailerLabel.setVisibility(View.VISIBLE);
            horizontalLine.setVisibility(View.VISIBLE);
        }
        else {
            tvTrailerLabel.setVisibility(View.GONE);
            horizontalLine.setVisibility(View.GONE);
        }
    }

    /**
     * Open the browser to view youtube video
     *
     * @param view view on which click listener is called
     */
    @OnClick(R.id.tv_trailer_number)
    void onTrailerClick(View view){
        String videoId = (String) view.getTag();
        view.getContext().startActivity(
                new Intent(Intent.ACTION_VIEW,
                        Uri.parse(Config.YOUTUBE_BASE_URI+videoId)));
    }
}
