package org.pytorch.demo.objectdetection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import java.util.List;

public class ImageAdapter extends BaseAdapter {

    private Context context;
    private List<String> imagePaths;

    public ImageAdapter(Context context, List<String> imagePaths) {
        this.context = context;
        this.imagePaths = imagePaths;
    }

    @Override
    public int getCount() {
        return imagePaths.size();
    }

    @Override
    public Object getItem(int position) {
        return imagePaths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.grid_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String imagePath = imagePaths.get(position);
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        Bitmap squareBitmap = makeSquareBitmap(bitmap); // 정사각형 비트맵 생성
        holder.imageView.setImageBitmap(squareBitmap);

        return convertView;
    }

    private Bitmap makeSquareBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int size = Math.min(width, height);

        int x = (width - size) / 2;
        int y = (height - size) / 2;

        Bitmap squareBitmap = Bitmap.createBitmap(bitmap, x, y, size, size);

        // 정사각형 비트맵의 세로 길이를 조금 더 줄임
        float scaleFactor = 0.5f;
        int newHeight = (int) (squareBitmap.getHeight() * scaleFactor);
        int newY = (squareBitmap.getHeight() - newHeight) / 2;
        squareBitmap = Bitmap.createBitmap(squareBitmap, 0, newY, squareBitmap.getWidth(), newHeight);

        return squareBitmap;
    }



    private static class ViewHolder {
        ImageView imageView;
    }
}
