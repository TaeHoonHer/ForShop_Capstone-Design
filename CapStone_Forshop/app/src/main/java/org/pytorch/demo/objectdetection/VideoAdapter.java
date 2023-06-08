package org.pytorch.demo.objectdetection;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

import java.util.ArrayList;

public class VideoAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> videoList; // 동영상 파일 경로 목록

    public VideoAdapter(Context context, ArrayList<String> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    @Override
    public int getCount() {
        return videoList.size();
    }

    @Override
    public Object getItem(int position) {
        return videoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(240, 240));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        String videoPath = videoList.get(position);

        // 영상 파일의 확장자가 .mp4인 경우에만 이미지를 가져옴
        if (videoPath.endsWith(".mp4")) {
            // 비디오의 썸네일 이미지를 가져옴
            Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.MINI_KIND);
            if (thumbnail != null) {
                // 이미지뷰에 썸네일 이미지 설정
                imageView.setImageBitmap(thumbnail);
            } else {
                // 가져올 수 없는 경우 기본 이미지를 설정하거나 처리를 원하는 대로 처리할 수 있음
                imageView.setImageResource(R.drawable.logo);
            }
        } else {
            // 다른 확장자의 영상 파일인 경우 기본 이미지를 설정하거나 처리를 원하는 대로 처리할 수 있음
            imageView.setImageResource(R.drawable.logo);
        }

        return imageView;
    }
}
