package org.pytorch.demo.objectdetection;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class BoardItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_item);

        ImageView boardImage = findViewById(R.id.board_image);
        Bitmap image = getIntent().getParcelableExtra("image");
        boardImage.setImageBitmap(image);
    }
}
