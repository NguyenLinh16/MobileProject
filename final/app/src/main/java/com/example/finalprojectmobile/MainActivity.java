package com.example.finalprojectmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.ResultReceiver;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {
    private ImageView img;
    private ImageView img1, img2, img3, img4, img5, img6, img7, img8, img9;
    private ImageView img_Delete;
    private TextView tv_score;
    private EditText time_CounDown;
    private Button btn_Add;
    private CountDownTimer timer;
    private RelativeLayout relativeLayout;
    private Dialog dialog;
    private int score;
    private int countTurn;
    private int img_location = 0;
    private float locationDefaultX, locationDefaultY;
    private Button Finish, Logout;


    protected void InitUI() {
        //FindID
        relativeLayout = findViewById(R.id.container);
        tv_score = (TextView) findViewById(R.id.textView_scores);
        time_CounDown = (EditText) findViewById(R.id.editText_timeCountDown);
        img = (ImageView) findViewById(R.id.imageViewMain);
        img1 = (ImageView) findViewById(R.id.imageView1);
        img2 = (ImageView) findViewById(R.id.imageView2);
        img3 = (ImageView) findViewById(R.id.imageView3);
        img4 = (ImageView) findViewById(R.id.imageView4);
        img5 = (ImageView) findViewById(R.id.imageView5);
        img6 = (ImageView) findViewById(R.id.imageView6);
        img7 = (ImageView) findViewById(R.id.imageView7);
        img8 = (ImageView) findViewById(R.id.imageView8);
        img9 = (ImageView) findViewById(R.id.imageView9);
        img_Delete = (ImageView) findViewById(R.id.imageViewDelete);
        btn_Add = (Button) findViewById(R.id.buttonAdd);
        Finish = findViewById(R.id.finish);
        Logout = findViewById(R.id.logout);
        //Logic
        countTurn = 0;
        score = 0;
        tv_score.setText("Scores:           " + score);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Khởi tạo UI
        InitUI();
        //Tạo sự kiện touch cho img
        img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (countTurn == 0) {
                        //Lưu vị trí mặc định của img trong lần chạm đầu tiên
                        locationDefaultX = img.getX();
                        locationDefaultY = img.getY();
                       // gamePlay();
                    }
                    countTurn++;//Lưu số lần chạm
                    //Khởi tạo Bóng và bắt đầu kéo
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                    v.startDragAndDrop(data, shadowBuilder, v, 0);
                }
                return false;
            }

        });
        relativeLayout.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                if (event.getAction() == DragEvent.ACTION_DRAG_STARTED) {
                    return true;
                } else if (event.getAction() == DragEvent.ACTION_DRAG_LOCATION) {
                    //Cập nhật liên tục vị trí img khi người dùng kéo
                    img.setX(event.getX() - 110);
                    img.setY(event.getY() - 40);
                    setBackGround(event);//Hàm xử lý hiệu ứng khi Drag
                    return true;
                } else if (event.getAction() == DragEvent.ACTION_DROP) {

                        img.setX(event.getX() - 100);
                        img.setY(event.getY() - 40);
                    return true;
                } else if (event.getAction() == DragEvent.ACTION_DRAG_ENTERED) {
                    return true;
                } else if (event.getAction() == DragEvent.ACTION_DRAG_EXITED) {
                    return true;
                } else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED) {

                        score++;// Cộng điểm người chơi
                        tv_score.setText("Scores:           " + score);//Hiển thị lên tv_score
                    return true;
                }
                return false;
            }
        });
        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khởi tạo một đối tượng Intent mới với loại ACTION_GET_CONTENT
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //Nếu không tồn tại biến img thì cho hiển thị và set vị trí ban đầu
                if (img.getVisibility() == View.GONE) {
                    img.setX(locationDefaultX);
                    img.setY(locationDefaultY);
                }
                // Đặt các thuộc tính MIME_TYPE và EXTRA_ALLOW_MULTIPLE của đối tượng Intent
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Khởi chạy hoạt động Intent
                startActivityForResult(intent, MODE_APPEND);
            }
        });
        img_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img.setVisibility(View.INVISIBLE);
            }
        });
        Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openResultdialog(score);
            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Nếu kết quả là thành công
        if (resultCode == RESULT_OK) {
            if (data == null) {
                return;
            }
            // Lấy URI của tệp ảnh
            Uri uri = data.getData();
            // Truy cập tệp ảnh
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                // Hiển thị tệp ảnh
                img.setImageBitmap(bitmap);
                img.setVisibility(View.VISIBLE);
                //Thông báo kết quả
                if (btn_Add.getText().equals("Update")) {
                    Toast.makeText(this, "Update Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Add Success", Toast.LENGTH_SHORT).show();
                }
                btn_Add.setText("ADD");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected void openResultdialog(int scores) {
        // Tạo hộp thoại
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_result);
        // Lấy cửa sổ của hộp thoại
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        // Thiết lập kích thước và nền của cửa sổ
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Thiết lập vị trí của cửa sổ
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        // Không cho phép đóng hộp thoại khi nhấn bên ngoài
        dialog.setCancelable(false);
        // Lấy các thành phần trong hộp thoại
        TextView tv_totalScore = dialog.findViewById(R.id.textView_total);
        Button btn_Again = dialog.findViewById(R.id.button_playAgain);
        Button btn_result = dialog.findViewById(R.id.button_viewResult);
        // Thiết lập sự kiện cho nút Chơi lại
        btn_Again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); //Tắt hộp thoại
            }
        });
        // Thiết lập sự kiện cho nút Xem kết quả
        btn_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hien Thi database
                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                startActivity(intent);
                finish();
            }
        });
        // Thiết lập số điểm đã đạt được
        tv_totalScore.setText(String.valueOf(scores));
        // Hiển thị hộp thoại
        dialog.show();

    }


    protected void setBackGround(DragEvent event) {
        // Kiểm tra xem hình ảnh chính có nằm trong vùng hình ảnh thứ nhất hay không
        if (InTrue(event, img1)) // Nếu true
        {
            img_location = 1; // lưu vị trí hiện tại của con trỏ chuột nằm ở img1
            img1.setBackgroundColor(Color.GREEN); // đổi màu background img1
        } else if (InTrue(event, img1) == false) //Nếu false
        {
            img1.setBackgroundColor(Color.parseColor("#06B10D")); // trả màu background về màu mặc định
        }
        // Tương tự với các img từ img1 -> img9
        if (InTrue(event, img2)) {
            img_location = 2;
            img2.setBackgroundColor(Color.GREEN);

        } else if (InTrue(event, img2) == false) {
            img2.setBackgroundColor(Color.parseColor("#06B10D"));
        }

        if (InTrue(event, img3)) {
            img_location = 3;
            img3.setBackgroundColor(Color.GREEN);
        }
        if (InTrue(event, img3) == false) {
            img3.setBackgroundColor(Color.parseColor("#06B10D"));
        }

        if (InTrue(event, img4)) {
            img_location = 4;
            img4.setBackgroundColor(Color.GREEN);
        }
        if (InTrue(event, img4) == false) {
            img_location = 4;
            img4.setBackgroundColor(Color.parseColor("#06B10D"));
        }

        if (InTrue(event, img5)) {
            img_location = 5;
            img5.setBackgroundColor(Color.GREEN);
        }
        if (InTrue(event, img5) == false) {
            img5.setBackgroundColor(Color.parseColor("#06B10D"));
        }

        if (InTrue(event, img6)) {
            img_location = 6;
            img6.setBackgroundColor(Color.GREEN);
        }
        if (InTrue(event, img6) == false) {
            img6.setBackgroundColor(Color.parseColor("#06B10D"));
        }

        if (InTrue(event, img7)) {
            img_location = 7;
            img7.setBackgroundColor(Color.GREEN);
        }
        if (InTrue(event, img7) == false) {
            img7.setBackgroundColor(Color.parseColor("#06B10D"));
        }

        if (InTrue(event, img8)) {
            img_location = 8;
            img8.setBackgroundColor(Color.GREEN);
        }
        if (InTrue(event, img8) == false) {
            img8.setBackgroundColor(Color.parseColor("#06B10D"));
        }

        if (InTrue(event, img9)) {
            img_location = 9;
            img9.setBackgroundColor(Color.GREEN);
        }
        if (InTrue(event, img9) == false) {
            img9.setBackgroundColor(Color.parseColor("#06B10D"));
        }
    }
    protected boolean InTrue(DragEvent event, ImageView imageView) {
        int x1 = (int) event.getX();
        int y1 = (int) event.getY();
        int x2 = (int) imageView.getX();
        int y2 = (int) imageView.getY();
        int width2 = imageView.getWidth();
        int height2 = imageView.getHeight();
        if ((x1 >= x2 && x1 <= x2 + width2) &&
                (y1 >= y2 && y1 <= y2 + height2)) {
            return true;
        } else {
            return false;
        }
    }
}
