package processing.core;

public interface PConstants {
    int X = 0;
    int Y = 1;
    int Z = 2;
    String JAVA2D = "processing.core.PGraphicsAndroid2D";
    String P2D = "processing.opengl.PGraphics2D";
    String P2DX = "processing.opengl.PGraphics2DX";
    String P3D = "processing.opengl.PGraphics3D";
    String OPENGL = "processing.opengl.PGraphics3D";
    String STEREO = "processing.vr.VRGraphicsStereo";
    String MONO = "processing.vr.VRPGraphicsMono";
    String VR = "processing.vr.VRGraphicsStereo";
    String AR = "processing.ar.ARGraphics";
    String ARCORE = "processing.ar.ARGraphics";
    int OTHER = 0;
    int WINDOWS = 1;
    int MACOSX = 2;
    int LINUX = 3;
    String[] platformNames = new String[]{"other", "windows", "macosx", "linux"};
    float EPSILON = 1.0E-4F;
    float MAX_FLOAT = 3.4028235E38F;
    float MIN_FLOAT = -3.4028235E38F;
    int MAX_INT = 2147483647;
    int MIN_INT = -2147483648;
    int VERTEX = 0;
    int BEZIER_VERTEX = 1;
    int QUADRATIC_VERTEX = 2;
    int CURVE_VERTEX = 3;
    int BREAK = 4;
    /** @deprecated */
    @Deprecated
    int QUAD_BEZIER_VERTEX = 2;
    float PI = 3.1415927F;
    float HALF_PI = 1.5707964F;
    float THIRD_PI = 1.0471976F;
    float QUARTER_PI = 0.7853982F;
    float TWO_PI = 6.2831855F;
    float TAU = 6.2831855F;
    float DEG_TO_RAD = 0.017453292F;
    float RAD_TO_DEG = 57.295776F;
    String WHITESPACE = " \t\n\r\f ";
    int RGB = 1;
    int ARGB = 2;
    int HSB = 3;
    int ALPHA = 4;
    int CMYK = 5;
    int YUV420 = 6;
    int TIFF = 0;
    int TARGA = 1;
    int JPEG = 2;
    int GIF = 3;
    int BLUR = 11;
    int GRAY = 12;
    int INVERT = 13;
    int OPAQUE = 14;
    int POSTERIZE = 15;
    int THRESHOLD = 16;
    int ERODE = 17;
    int DILATE = 18;
    int REPLACE = 0;
    int BLEND = 1;
    int ADD = 2;
    int SUBTRACT = 4;
    int LIGHTEST = 8;
    int DARKEST = 16;
    int DIFFERENCE = 32;
    int EXCLUSION = 64;
    int MULTIPLY = 128;
    int SCREEN = 256;
    int OVERLAY = 512;
    int HARD_LIGHT = 1024;
    int SOFT_LIGHT = 2048;
    int DODGE = 4096;
    int BURN = 8192;
    int CHATTER = 0;
    int COMPLAINT = 1;
    int PROBLEM = 2;
    int PROJECTION = 0;
    int MODELVIEW = 1;
    int CUSTOM = 0;
    int ORTHOGRAPHIC = 2;
    int PERSPECTIVE = 3;
    int GROUP = 0;
    int POINT = 2;
    int POINTS = 3;
    int LINE = 4;
    int LINES = 5;
    int LINE_STRIP = 50;
    int LINE_LOOP = 51;
    int TRIANGLE = 8;
    int TRIANGLES = 9;
    int TRIANGLE_STRIP = 10;
    int TRIANGLE_FAN = 11;
    int QUAD = 16;
    int QUADS = 17;
    int QUAD_STRIP = 18;
    int POLYGON = 20;
    int PATH = 21;
    int RECT = 30;
    int ELLIPSE = 31;
    int ARC = 32;
    int SPHERE = 40;
    int BOX = 41;
    int OPEN = 1;
    int CLOSE = 2;
    int CORNER = 0;
    int CORNERS = 1;
    int RADIUS = 2;
    /** @deprecated */
    int CENTER_RADIUS = 2;
    int CENTER = 3;
    int DIAMETER = 3;
    /** @deprecated */
    int CENTER_DIAMETER = 3;
    int CHORD = 2;
    int PIE = 3;
    int BASELINE = 0;
    int TOP = 101;
    int BOTTOM = 102;
    int NORMAL = 1;
    int IMAGE = 2;
    int CLAMP = 0;
    int REPEAT = 1;
    int MODEL = 4;
    int SHAPE = 5;
    int SQUARE = 1;
    int ROUND = 2;
    int PROJECT = 4;
    int MITER = 8;
    int BEVEL = 32;
    int AMBIENT = 0;
    int DIRECTIONAL = 1;
    int SPOT = 3;
    char BACKSPACE = 'C';
    char TAB = '=';
    char ENTER = 'B';
    char RETURN = 'B';
    char ESC = 'o';
    char DELETE = 'C';
    int CODED = 65535;
    int UP = 19;
    int DOWN = 20;
    int LEFT = 21;
    int RIGHT = 22;
    int BACK = 4;
    int MENU = 82;
    int DPAD = 23;
    int SHIFT = 59;
    int PORTRAIT = 1;
    int LANDSCAPE = 2;
    /** @deprecated */
    @Deprecated
    int ENABLE_NATIVE_FONTS = 1;
    /** @deprecated */
    @Deprecated
    int DISABLE_NATIVE_FONTS = -1;
    int DISABLE_DEPTH_TEST = 2;
    int ENABLE_DEPTH_TEST = -2;
    int ENABLE_DEPTH_SORT = 3;
    int DISABLE_DEPTH_SORT = -3;
    int DISABLE_OPENGL_ERRORS = 4;
    int ENABLE_OPENGL_ERRORS = -4;
    int DISABLE_DEPTH_MASK = 5;
    int ENABLE_DEPTH_MASK = -5;
    int DISABLE_OPTIMIZED_STROKE = 6;
    int ENABLE_OPTIMIZED_STROKE = -6;
    int ENABLE_STROKE_PERSPECTIVE = 7;
    int DISABLE_STROKE_PERSPECTIVE = -7;
    int DISABLE_TEXTURE_MIPMAPS = 8;
    int ENABLE_TEXTURE_MIPMAPS = -8;
    int ENABLE_STROKE_PURE = 9;
    int DISABLE_STROKE_PURE = -9;
    int ENABLE_BUFFER_READING = 10;
    int DISABLE_BUFFER_READING = -10;
    int DISABLE_KEY_REPEAT = 11;
    int ENABLE_KEY_REPEAT = -11;
    int DISABLE_ASYNC_SAVEFRAME = 12;
    int ENABLE_ASYNC_SAVEFRAME = -12;
    int HINT_COUNT = 13;
    String ERROR_BACKGROUND_IMAGE_SIZE = "background image must be the same size as your application";
    String ERROR_BACKGROUND_IMAGE_FORMAT = "background images should be RGB or ARGB";
    String ERROR_TEXTFONT_NULL_PFONT = "A null PFont was passed to textFont()";
    String ERROR_PUSHMATRIX_OVERFLOW = "Too many calls to pushMatrix().";
    String ERROR_PUSHMATRIX_UNDERFLOW = "Too many calls to popMatrix(), and not enough to pushMatrix().";
}
