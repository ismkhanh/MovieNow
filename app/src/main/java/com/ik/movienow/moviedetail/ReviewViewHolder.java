package com.ik.movienow.moviedetail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ik.movienow.R;

import butterknife.BindView;
import butterknife.ButterKnife;


class ReviewViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_review_author) TextView tvAuthor;
    @BindView(R.id.tv_review_content) TextView tvContent;
    @BindView(R.id.tv_review_label) TextView tvReviewLabel;
    @BindView(R.id.horizontal_line) View horizontalLine;

    ReviewViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    void addReview(String author, String content, boolean showLabel) {
        tvAuthor.setText(author);
        tvContent.setText(content);
        if (showLabel) {
            tvReviewLabel.setVisibility(View.VISIBLE);
            horizontalLine.setVisibility(View.VISIBLE);
        }
        else {
            tvReviewLabel.setVisibility(View.GONE);
            horizontalLine.setVisibility(View.GONE);
        }
    }
}
