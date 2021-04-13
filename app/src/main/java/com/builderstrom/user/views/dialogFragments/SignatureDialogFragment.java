package com.builderstrom.user.views.dialogFragments;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.widget.LinearLayout;

import com.builderstrom.user.R;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.views.customViews.DrawingView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;


public class SignatureDialogFragment extends BaseDialogFragment {

    @BindView(R.id.mLayout) LinearLayout mLayout;
    private DrawingView dv;
    private Paint mPaint;
    private File outPutFile;
    private Callback mCallback;

    @OnClick({R.id.btnSave, R.id.btnCancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSave:
                saveFile();
                break;
            case R.id.btnCancel:
                dismiss();
                break;
        }
    }

    private void saveFile() {
        if (mCallback != null) {
            FileOutputStream ostream = null;
            try {
                ostream = new FileOutputStream(outPutFile);
                Bitmap well = dv.getBitmap();
//                Bitmap save = Bitmap.createBitmap(320, 480, Bitmap.Config.ARGB_8888);
                Bitmap save = Bitmap.createBitmap(250, 100, Bitmap.Config.ARGB_8888);
                Paint paint = new Paint();
                paint.setColor(Color.WHITE);
                Canvas now = new Canvas(save);
//                now.drawRect(new Rect(0, 0, 320, 480), paint);
                now.drawRect(new Rect(0, 0, 250, 100), paint);
//                now.drawBitmap(well, new Rect(0, 0, well.getWidth(), well.getHeight()), new Rect(0, 0, 320, 480), null);
                now.drawBitmap(well, new Rect(0, 0, well.getWidth(), well.getHeight()), new Rect(0, 0, 250, 100), null);

                if (save == null) {
                    System.out.println("NULL bitmap save\n");
                }
                save.compress(Bitmap.CompressFormat.PNG, 100, ostream);
            } catch (Exception e) {
                e.printStackTrace();
            }

            mCallback.getImage(outPutFile);
            dismiss();
        }

    }

    @Override
    protected boolean isFullScreenDialog() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_fragment_signature;
    }

    @Override
    protected void bindViews(View view) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);
        dv = new DrawingView(requireActivity(), mPaint);
        mLayout.addView(dv);
        try {
            outPutFile = CommonMethods.createImageFile(requireActivity());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void init() {
    }


    public void setmCallback(Callback mCallback) {
        this.mCallback = mCallback;
    }

    public interface UpdateBackList {
        void updateList();
    }

    public interface Callback {
        void getImage(File imageFile);
    }

}
