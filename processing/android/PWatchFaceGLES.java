package processing.android;

import android.annotation.TargetApi;
import android.graphics.Point;
import android.graphics.Rect;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLDisplay;
import android.os.Bundle;
import android.support.wearable.complications.ComplicationData;
import android.support.wearable.watchface.Gles2WatchFaceService;
import android.support.wearable.watchface.Gles2WatchFaceService.Engine;
import android.support.wearable.watchface.WatchFaceStyle.Builder;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.WindowInsets;
import android.view.WindowManager;
import java.lang.reflect.Method;
import processing.core.PApplet;

@TargetApi(21)
public class PWatchFaceGLES extends Gles2WatchFaceService implements AppComponent {
    private static final int[] CONFIG_ATTRIB_LIST = new int[]{12352, 4, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12325, 16, 12344};
    private Point size;
    private DisplayMetrics metrics;
    private PWatchFaceGLES.GLES2Engine engine;

    public PWatchFaceGLES() {
    }

    public void initDimensions() {
        this.metrics = new DisplayMetrics();
        this.size = new Point();
        WindowManager wm = (WindowManager)this.getSystemService("window");
        Display display = wm.getDefaultDisplay();
        CompatUtils.getDisplayParams(display, this.metrics, this.size);
    }

    public int getDisplayWidth() {
        return this.size.x;
    }

    public int getDisplayHeight() {
        return this.size.y;
    }

    public float getDisplayDensity() {
        return this.metrics.density;
    }

    public int getKind() {
        return 2;
    }

    public PApplet createSketch() {
        return new PApplet();
    }

    public void setSketch(PApplet sketch) {
        this.engine.sketch = sketch;
    }

    public PApplet getSketch() {
        return this.engine.sketch;
    }

    public boolean isService() {
        return true;
    }

    public ServiceEngine getEngine() {
        return this.engine;
    }

    public void requestDraw() {
        if (this.engine != null) {
            this.engine.invalidateIfNecessary();
        }

    }

    public boolean canDraw() {
        return false;
    }

    public void dispose() {
    }

    public void requestPermissions() {
    }

    public Engine onCreateEngine() {
        this.engine = new PWatchFaceGLES.GLES2Engine();
        return this.engine;
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.engine != null) {
            this.engine.onDestroy();
        }

    }

    private class GLES2Engine extends Engine implements ServiceEngine {
        private PApplet sketch;
        private Method compUpdatedMethod;
        private Method tapCommandMethod;
        private boolean isRound;
        private Rect insets;
        private boolean lowBitAmbient;
        private boolean burnInProtection;

        private GLES2Engine() {
            super(PWatchFaceGLES.this);
            this.isRound = false;
            this.insets = new Rect();
            this.lowBitAmbient = false;
            this.burnInProtection = false;
        }

        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            this.setWatchFaceStyle((new Builder(PWatchFaceGLES.this)).setAcceptsTapEvents(true).build());
            this.sketch = PWatchFaceGLES.this.createSketch();
            this.sketch.initSurface(PWatchFaceGLES.this, (SurfaceHolder)null);
            this.initTapEvents();
            this.initComplications();
            PWatchFaceGLES.this.requestPermissions();
        }

        public EGLConfig chooseEglConfig(EGLDisplay eglDisplay) {
            int[] numEglConfigs = new int[1];
            EGLConfig[] eglConfigs = new EGLConfig[1];
            if (!EGL14.eglChooseConfig(eglDisplay, PWatchFaceGLES.CONFIG_ATTRIB_LIST, 0, eglConfigs, 0, eglConfigs.length, numEglConfigs, 0)) {
                throw new RuntimeException("eglChooseConfig failed");
            } else if (numEglConfigs[0] == 0) {
                throw new RuntimeException("no matching EGL configs");
            } else {
                return eglConfigs[0];
            }
        }

        public void onGlContextCreated() {
            super.onGlContextCreated();
        }

        public void onGlSurfaceCreated(int width, int height) {
            super.onGlSurfaceCreated(width, height);
            if (this.sketch != null) {
                this.sketch.surfaceChanged();
                this.sketch.setSize(width, height);
            }

        }

        private void initTapEvents() {
            try {
                this.tapCommandMethod = this.sketch.getClass().getMethod("onTapCommand", Integer.TYPE, Integer.TYPE, Integer.TYPE, Long.TYPE);
            } catch (Exception var2) {
                this.tapCommandMethod = null;
            }

        }

        private void initComplications() {
            try {
                this.compUpdatedMethod = this.sketch.getClass().getMethod("onComplicationDataUpdate", Integer.TYPE, ComplicationData.class);
            } catch (Exception var2) {
                this.compUpdatedMethod = null;
            }

        }

        private void invalidateIfNecessary() {
            if (this.isVisible() && !this.isInAmbientMode()) {
                this.invalidate();
            }

        }

        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            this.invalidateIfNecessary();
        }

        public void onPropertiesChanged(Bundle properties) {
            super.onPropertiesChanged(properties);
            this.lowBitAmbient = properties.getBoolean("low_bit_ambient", false);
            this.burnInProtection = properties.getBoolean("burn_in_protection", false);
        }

        public void onApplyWindowInsets(WindowInsets insets) {
            super.onApplyWindowInsets(insets);
            this.insets.set(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(), insets.getSystemWindowInsetRight(), insets.getSystemWindowInsetBottom());
        }

        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (this.sketch != null) {
                if (visible) {
                    this.sketch.onResume();
                } else {
                    this.sketch.onPause();
                }
            }

        }

        public void onPeekCardPositionUpdate(Rect rect) {
        }

        public void onTimeTick() {
            this.invalidate();
        }

        public void onDraw() {
            super.onDraw();
            if (this.sketch != null) {
                this.sketch.handleDraw();
            }

        }

        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
            if (this.sketch != null) {
                this.sketch.surfaceTouchEvent(event);
            }

        }

        public void onTapCommand(int tapType, int x, int y, long eventTime) {
            if (this.tapCommandMethod != null) {
                try {
                    this.tapCommandMethod.invoke(tapType, x, y, eventTime);
                } catch (Exception var7) {
                }

                this.invalidate();
            }

        }

        public void onComplicationDataUpdate(int complicationId, ComplicationData complicationData) {
            if (this.compUpdatedMethod != null) {
                try {
                    this.compUpdatedMethod.invoke(complicationId, complicationData);
                } catch (Exception var4) {
                }

                this.invalidate();
            }

        }

        public void onDestroy() {
            super.onDestroy();
            if (this.sketch != null) {
                this.sketch.onDestroy();
            }

        }

        public float getXOffset() {
            return 0.0F;
        }

        public float getYOffset() {
            return 0.0F;
        }

        public float getXOffsetStep() {
            return 0.0F;
        }

        public float getYOffsetStep() {
            return 0.0F;
        }

        public int getXPixelOffset() {
            return 0;
        }

        public int getYPixelOffset() {
            return 0;
        }

        public boolean isRound() {
            return this.isRound;
        }

        public Rect getInsets() {
            return this.insets;
        }

        public boolean useLowBitAmbient() {
            return this.lowBitAmbient;
        }

        public boolean requireBurnInProtection() {
            return this.burnInProtection;
        }

        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            if (this.sketch != null) {
                this.sketch.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }

        }
    }
}
