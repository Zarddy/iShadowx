package club.zarddy.library.util.camera;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    Context mContext;
    SurfaceHolder mSurfaceHolder;
    private CameraSurfaceViewCallBack mCallBack;

    public interface CameraSurfaceViewCallBack {
        void onSurfaceCreated();
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mSurfaceHolder = getHolder();
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);//translucent半透明 transparent透明
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Thread openThread = new Thread() {
            @Override
            public void run() {
                if (mCallBack != null) {
                    mCallBack.onSurfaceCreated();
                }
            }
        };
        openThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        CameraInterface.getInstance().doStopCamera();
    }

    public SurfaceHolder getSurfaceHolder(){
        return mSurfaceHolder;
    }

    public void setCameraSurfaceViewCallBack(CameraSurfaceViewCallBack callBack) {
        this.mCallBack = callBack;
    }
}
