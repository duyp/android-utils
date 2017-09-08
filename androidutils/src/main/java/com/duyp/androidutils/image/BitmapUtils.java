// "Therefore those skilled at the unorthodox
// are infinite as heaven and earth,
// inexhaustible as the great rivers.
// When they come to an end,
// they begin again,
// like the days and months;
// they die and are reborn,
// like the four seasons."
//
// - Sun Tsu,
// "The Art of War"

package com.duyp.androidutils.image;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.duyp.androidutils.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

/**
 * Utility class that deals with operations with an bitmap.
 */
final class BitmapUtils {

    private static final int MAX_WIDTH = 1500;
    private static final int MAX_HEIGHT = 1500;
    private static final int DEFAULT_JPEG_QUALITY = 75;

    private static final Rect EMPTY_RECT = new Rect();
    private static final String TAG = "BitmapUtils";

    /**
     * Used to know the max texture size allowed to be rendered
     */
    private static int mMaxTextureSize;

    /**
     * Used to rotate bitmap
     */
    private static Matrix matrix = new Matrix();

    /**
     * Rotate the given image by reading the Exif value of the image (uri).<br>
     * If no rotation is required the image will not be rotated.<br>
     * New bitmap is created and the old one is recycled.
     */
    static int getRotationByExif(Context context, Uri uri) {
        try {
            File file = FileUtils.getFileFromUri(context, uri);
            if (file.exists()) {
                ExifInterface ei = new ExifInterface(file.getAbsolutePath());
                return getRotationByExif(ei);
            } else {
                // for API 24 and above
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    InputStream is = context.getContentResolver().openInputStream(uri);
                    ExifInterface ei = new ExifInterface(is);
                    return getRotationByExif(ei);
                }
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return 0;
    }


    /**
     * Get image rotation by given Exif value
     * @param exif ExifInterface
     * @return rotation
     */
    public static int getRotationByExif(ExifInterface exif) {
        int degrees;
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                degrees = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                degrees = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                degrees = 270;
                break;
            default:
                degrees = 0;
                break;
        }
        return degrees;
    }

    /**
     * get rotation by file's exif
     * @param filePath input file path
     * @return rotation
     */
    private static int getRotationByExif(String filePath) {
        int orientation = 0;
        try {
            ExifInterface exif = new ExifInterface(filePath);
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    orientation = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    orientation = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    orientation = 90;
                    break;
                case ExifInterface.ORIENTATION_NORMAL:
                    orientation = 0;
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return orientation;
    }

    /**
     * Get {@link ExifInterface} orientation from given rotation
     * @param rotation given rotation
     * @return one of {@link ExifInterface#ORIENTATION_NORMAL}, {@link ExifInterface#ORIENTATION_ROTATE_90}
     *          {@link ExifInterface#ORIENTATION_ROTATE_180}, {@link ExifInterface#ORIENTATION_ROTATE_270}
     */
    public static int getExifOrientationFromRotation(int rotation) {
        switch (rotation) {
            case 90:
                return ExifInterface.ORIENTATION_ROTATE_90;
            case 180:
                return ExifInterface.ORIENTATION_ROTATE_180;
            case 270:
                return ExifInterface.ORIENTATION_ROTATE_270;
            default:
                return ExifInterface.ORIENTATION_NORMAL;
        }
    }

    public static Bitmap scaleBitmap(Bitmap bm) {
        return scaleBitmap(bm, MAX_WIDTH, MAX_HEIGHT);
    }

    public static Bitmap scaleBitmap(Bitmap bm, int maxWidth, int maxHeight) {
        int w = bm.getWidth();
        int h = bm.getHeight();
        if (w * h > maxWidth * maxHeight) {
            float x = (float) w / h;
            return Bitmap.createScaledBitmap(bm,
                    (int) (w > h ? maxWidth : maxHeight * x),
                    (int) (h > w ? maxHeight : maxWidth / x),
                    true);
        }
        return bm;
    }

    @Nullable
    public static byte[] getByteArrayFromBitmap(Bitmap bm, int jpegQuality, boolean scaleWithDefaultMaxSize) {
        if (scaleWithDefaultMaxSize) {
            bm = scaleBitmap(bm);
        }
        return getByteArrayFromBitmap(bm, jpegQuality);
    }

    @Nullable
    public static byte[] getByteArrayFromBitmap(Bitmap bm, int jpegQuality, int maxWidth, int maxHeight) {
        bm = scaleBitmap(bm, maxWidth, maxHeight);
        Log.d("image_size", "After resize: " + toString(bm));
        return getByteArrayFromBitmap(bm, jpegQuality);
    }

    @Nullable
    public static byte[] getByteArrayFromBitmap(Bitmap bm, int jpegQuality) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, jpegQuality, stream);
            return stream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Compress input bitmap with specific jpeg quality
     * @param bm input bitmap
     * @param jpegQuality ouput jpeg quality
     * @return compressed bitmap
     */
    public static Bitmap compressBitmap(Bitmap bm, int jpegQuality) {
        if (jpegQuality > 0 && jpegQuality <= 100) {
            ByteArrayOutputStream obj = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, jpegQuality, obj);
            byte[] b = obj.toByteArray();
            bm = BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        return bm;
    }

    /**
     * Decode input byte array to bitmap
     * @param data input byte array
     * @param rotationDegree use in case of input bytes come from camera
     * @return output bitmap
     */
    @Nullable
    public static Bitmap decodeByteArray(byte[] data, float rotationDegree) {
        try {
            Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (rotationDegree != 0) {
                bm = rotateBitmap(bm, rotationDegree);
            }
            return bm;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * decode input byte array to scaled bitmap
     * @param data input bytes array
     * @param rotationDegree use in case of input bytes come from camera
     * @param maxWidth maximum width limit
     * @param maxHeight maximum height linmit
     * @return output Bitmap
     */
    @Nullable
    public static Bitmap decodeByteArrayToScaledBitmap(byte[] data, float rotationDegree, int maxWidth, int maxHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);

        int reqWidth = options.outWidth;
        int reqHeight = options.outHeight;

        float ratio = (float) options.outWidth / options.outHeight;
        if (reqWidth >= maxWidth && reqWidth > reqHeight) {
            reqWidth = maxWidth;
            reqHeight = (int) (reqWidth / ratio);
        } else if (reqHeight >= maxHeight && reqHeight > reqWidth) {
            reqHeight = maxHeight;
            reqWidth = (int) (reqHeight * ratio);
        }
//		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        options.inScaled = true;
        options.inDensity = options.outWidth;
        options.inTargetDensity = reqWidth;

        Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        if (rotationDegree != 0) {
            bm = rotateBitmap(bm, rotationDegree);
        }
        if (bm != null) {
            int w = bm.getWidth();
            int h = bm.getHeight();
            Log.d("TAG_CAMERA", "ImageTools->Created bitmap: " + w + ", " + h);
        }
        return bm;
    }

    /**
     * Decode image file to byte array
     * @param file input file
     * @param maxWidth maximum width limit
     * @param maxHeight maximum height linmit
     * @return output bytes
     */
    public static byte[] decodeImageFileToByArray(@NonNull File file, int jpegQuality, int maxWidth, int maxHeight) {
        Bitmap bm = decodeFileToScaledBitmap(file, 0, maxWidth, maxHeight);
        return getByteArrayFromBitmap(bm, jpegQuality);
    }

    /**
     * Decode image file to bitmap
     * @param file input file
     * @param deviceRotation device rotation showing bitmap right rotation as user see
     * @param maxWidth maximum width limit
     * @param maxHeight maximum height linmit
     * @return output Bitmap
     */
    public static Bitmap decodeFileToScaledBitmap(@NonNull File file, int deviceRotation, int maxWidth, int maxHeight) {
        int fileRotation = getRotationByExif(file.getPath());
        Log.d(TAG, "loading file..." + file.length() / 1024 + "KB, Orientation: " + fileRotation);
        long time = SystemClock.elapsedRealtime();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), options);

        int originWidth = options.outWidth;

        int reqWidth = options.outWidth;
        int reqHeight = options.outHeight;

        float ratio = (float) options.outWidth / options.outHeight;
        if (reqWidth >= maxWidth && reqWidth >= reqHeight) {
            reqWidth = maxWidth;
            reqHeight = (int) (reqWidth / ratio);
        } else if (reqHeight >= maxHeight && reqHeight > reqWidth) {
            reqHeight = maxHeight;
            reqWidth = (int) (reqHeight * ratio);
        }
        Log.d(TAG, "ImageTools->Source w-h: " + originWidth + "," + options.outHeight);
        Log.d(TAG, "ImageTools->Req w-h: " + reqWidth + "," + reqHeight);
//		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options = new BitmapFactory.Options();
        options.inScaled = true;
        options.inDensity = originWidth;
        options.inTargetDensity = reqWidth;

        try {
            Bitmap bm = BitmapFactory.decodeFile(file.getPath(), options);
            fileRotation = (fileRotation + deviceRotation) % 360;
            if (fileRotation != 0) {
                Log.d(TAG, "ImageTools->Rotating: " + fileRotation);
                bm = rotateBitmap(bm, fileRotation);
            }
            if (bm != null) {
                int w = bm.getWidth();
                int h = bm.getHeight();
                time = SystemClock.elapsedRealtime() - time;
                Log.d(TAG, "ImageTools->Created bitmap: " + w + ", " + h + " in " + time + "ms");
            }
            return bm;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return decodeFileToScaledBitmap(file, deviceRotation, maxWidth - 400, maxHeight - 400);
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        }
    }

    /**
     * Save a bitmap to temporary file
     * @param bm input bitmap
     * @param jpegQuality jpeg quality, from 1 to 100
     * @param rotation image rotation
     * @return temporary file, null if false to create temp file or save bitmap
     */
    public static File saveBitmapToTempFile(Bitmap bm, int jpegQuality, int rotation) {
        try {
            File f = FileUtils.createTemporaryImageFile();
            if (f != null) {
                FileOutputStream out = new FileOutputStream(f);
                bm.compress(Bitmap.CompressFormat.JPEG, jpegQuality, out);
                out.flush();
                out.close();

                // save exif orientation info
                try {
                    ExifInterface exif = new ExifInterface(f.getCanonicalPath());
                    exif.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(getExifOrientationFromRotation(rotation)));
                    exif.saveAttributes();
                } catch (IOException ignored) {
                    ignored.printStackTrace();
                }
                return f;
            } else {
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Decode bitmap from stream using sampling to get bitmap with the requested limit.
     */
    static BitmapSampled decodeSampledBitmap(Context context, Uri uri, int reqWidth, int reqHeight) {

        try {
            ContentResolver resolver = context.getContentResolver();

            // First decode with inJustDecodeBounds=true to check dimensions
            BitmapFactory.Options options = decodeImageForOption(resolver, uri);

            // Calculate inSampleSize
            options.inSampleSize = Math.max(
                    calculateInSampleSizeByReqestedSize(options.outWidth, options.outHeight, reqWidth, reqHeight),
                    calculateInSampleSizeByMaxTextureSize(options.outWidth, options.outHeight));

            // Decode bitmap with inSampleSize set
            Bitmap bitmap = decodeImage(resolver, uri, options);

            return new BitmapSampled(bitmap, options.inSampleSize);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load sampled bitmap: " + uri + "\r\n" + e.getMessage(), e);
        }
    }

    /**
     * Crop image bitmap from given bitmap using the given points in the original bitmap and the given rotation.<br>
     * if the rotation is not 0,90,180 or 270 degrees then we must first crop a larger area of the image that
     * contains the requires rectangle, rotate and then crop again a sub rectangle.<br>
     * If crop fails due to OOM we scale the cropping image by 0.5 every time it fails until it is small enough.
     */
    static BitmapSampled cropBitmapObjectHandleOOM(Bitmap bitmap, float[] points, int degreesRotated,
                                                   boolean fixAspectRatio, int aspectRatioX, int aspectRatioY) {
        int scale = 1;
        while (true) {
            try {
                Bitmap cropBitmap = cropBitmapObjectWithScale(bitmap, points, degreesRotated, fixAspectRatio, aspectRatioX, aspectRatioY, 1 / (float) scale);
                return new BitmapSampled(cropBitmap, scale);
            } catch (OutOfMemoryError e) {
                scale *= 2;
                if (scale > 8) {
                    throw e;
                }
            }
        }
    }

    /**
     * Crop image bitmap from given bitmap using the given points in the original bitmap and the given rotation.<br>
     * if the rotation is not 0,90,180 or 270 degrees then we must first crop a larger area of the image that
     * contains the requires rectangle, rotate and then crop again a sub rectangle.
     *
     * @param scale how much to scale the cropped image part, use 0.5 to lower the image by half (OOM handling)
     */
    private static Bitmap cropBitmapObjectWithScale(Bitmap bitmap, float[] points, int degreesRotated,
                                                    boolean fixAspectRatio, int aspectRatioX, int aspectRatioY, float scale) {

        // get the rectangle in original image that contains the required cropped area (larger for non rectangular crop)
        Rect rect = getRectFromPoints(points, bitmap.getWidth(), bitmap.getHeight(), fixAspectRatio, aspectRatioX, aspectRatioY);

        // crop and rotate the cropped image in one operation
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        matrix.postRotate(degreesRotated, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        Bitmap result = Bitmap.createBitmap(bitmap, rect.left, rect.top, rect.width(), rect.height(), matrix, true);

        if (result == bitmap) {
            // corner case when all bitmap is selected, no worth optimizing for it
            result = bitmap.copy(bitmap.getConfig(), false);
        }

        // rotating by 0, 90, 180 or 270 degrees doesn't require extra cropping
        if (degreesRotated % 90 != 0) {

            // extra crop because non rectangular crop cannot be done directly on the image without rotating first
            result = cropForRotatedImage(result, points, rect, degreesRotated, fixAspectRatio, aspectRatioX, aspectRatioY);
        }

        return result;
    }

    /**
     * Crop image bitmap from URI by decoding it with specific width and height to down-sample if required.<br>
     * Additionally if OOM is thrown try to increase the sampling (2,4,8).
     */
    static BitmapSampled cropBitmap(Context context, Uri loadedImageUri, float[] points,
                                    int degreesRotated, int orgWidth, int orgHeight, boolean fixAspectRatio,
                                    int aspectRatioX, int aspectRatioY, int reqWidth, int reqHeight) {
        int sampleMulti = 1;
        while (true) {
            try {
                // if successful, just return the resulting bitmap
                return cropBitmap(context, loadedImageUri, points,
                        degreesRotated, orgWidth, orgHeight, fixAspectRatio,
                        aspectRatioX, aspectRatioY, reqWidth, reqHeight,
                        sampleMulti);
            } catch (OutOfMemoryError e) {
                // if OOM try to increase the sampling to lower the memory usage
                sampleMulti *= 2;
                if (sampleMulti > 16) {
                    throw new RuntimeException("Failed to handle OOM by sampling (" + sampleMulti + "): " + loadedImageUri + "\r\n" + e.getMessage(), e);
                }
            }
        }
    }



    /**
     * Get left value of the bounding rectangle of the given points.
     */
    static float getRectLeft(float[] points) {
        return Math.min(Math.min(Math.min(points[0], points[2]), points[4]), points[6]);
    }

    /**
     * Get top value of the bounding rectangle of the given points.
     */
    static float getRectTop(float[] points) {
        return Math.min(Math.min(Math.min(points[1], points[3]), points[5]), points[7]);
    }

    /**
     * Get right value of the bounding rectangle of the given points.
     */
    static float getRectRight(float[] points) {
        return Math.max(Math.max(Math.max(points[0], points[2]), points[4]), points[6]);
    }

    /**
     * Get bottom value of the bounding rectangle of the given points.
     */
    static float getRectBottom(float[] points) {
        return Math.max(Math.max(Math.max(points[1], points[3]), points[5]), points[7]);
    }

    /**
     * Get width of the bounding rectangle of the given points.
     */
    static float getRectWidth(float[] points) {
        return getRectRight(points) - getRectLeft(points);
    }

    /**
     * Get heightof the bounding rectangle of the given points.
     */
    static float getRectHeight(float[] points) {
        return getRectBottom(points) - getRectTop(points);
    }

    /**
     * Get horizontal center value of the bounding rectangle of the given points.
     */
    static float getRectCenterX(float[] points) {
        return (getRectRight(points) + getRectLeft(points)) / 2f;
    }

    /**
     * Get verical center value of the bounding rectangle of the given points.
     */
    static float getRectCenterY(float[] points) {
        return (getRectBottom(points) + getRectTop(points)) / 2f;
    }

    /**
     * Get a rectangle for the given 4 points (x0,y0,x1,y1,x2,y2,x3,y3) by finding the min/max 2 points that
     * contains the given 4 points and is a stright rectangle.
     */
    static Rect getRectFromPoints(float[] points, int imageWidth, int imageHeight, boolean fixAspectRatio, int aspectRatioX, int aspectRatioY) {
        int left = Math.round(Math.max(0, getRectLeft(points)));
        int top = Math.round(Math.max(0, getRectTop(points)));
        int right = Math.round(Math.min(imageWidth, getRectRight(points)));
        int bottom = Math.round(Math.min(imageHeight, getRectBottom(points)));

        Rect rect = new Rect(left, top, right, bottom);
        if (fixAspectRatio) {
            fixRectForAspectRatio(rect, aspectRatioX, aspectRatioY);
        }

        return rect;
    }

    /**
     * Fix the given rectangle if it doesn't confirm to aspect ration rule.<br>
     * Make sure that width and height are equal if 1:1 fixed aspect ratio is requested.
     */
    private static void fixRectForAspectRatio(Rect rect, int aspectRatioX, int aspectRatioY) {
        if (aspectRatioX == aspectRatioY && rect.width() != rect.height()) {
            if (rect.height() > rect.width()) {
                rect.bottom -= rect.height() - rect.width();
            } else {
                rect.right -= rect.width() - rect.height();
            }
        }
    }

    /**
     * Write the given bitmap to the given uri using the given compression.
     */
    static void writeBitmapToUri(Context context, Bitmap bitmap, Uri uri, Bitmap.CompressFormat compressFormat, int compressQuality) throws FileNotFoundException {
        OutputStream outputStream = null;
        try {
            outputStream = context.getContentResolver().openOutputStream(uri);
            bitmap.compress(compressFormat, compressQuality, outputStream);
        } finally {
            closeSafe(outputStream);
        }
    }

//    /**
//     * Resize the given bitmap to the given width/height by the given option.<br>
//     */
//    static Bitmap resizeBitmap(Bitmap bitmap, int reqWidth, int reqHeight, CropImageView.RequestSizeOptions options) {
//        try {
//            if (reqWidth > 0 && reqHeight > 0 && (options == CropImageView.RequestSizeOptions.RESIZE_FIT ||
//                    options == CropImageView.RequestSizeOptions.RESIZE_INSIDE ||
//                    options == CropImageView.RequestSizeOptions.RESIZE_EXACT)) {
//
//                Bitmap resized = null;
//                if (options == CropImageView.RequestSizeOptions.RESIZE_EXACT) {
//                    resized = Bitmap.createScaledBitmap(bitmap, reqWidth, reqHeight, false);
//                } else {
//                    int width = bitmap.getWidth();
//                    int height = bitmap.getHeight();
//                    float scale = Math.max(width / (float) reqWidth, height / (float) reqHeight);
//                    if (scale > 1 || options == CropImageView.RequestSizeOptions.RESIZE_FIT) {
//                        resized = Bitmap.createScaledBitmap(bitmap, (int) (width / scale), (int) (height / scale), false);
//                    }
//                }
//                if (resized != null) {
//                    if (resized != bitmap) {
//                        bitmap.recycle();
//                    }
//                    return resized;
//                }
//            }
//        } catch (Exception e) {
//            Log.w("AIC", "Failed to resize cropped image, return bitmap before resize", e);
//        }
//        return bitmap;
//    }

    //region: Private methods

    /**
     * Crop image bitmap from URI by decoding it with specific width and height to down-sample if required.
     *
     * @param orgWidth used to get rectangle from points (handle edge cases to limit rectangle)
     * @param orgHeight used to get rectangle from points (handle edge cases to limit rectangle)
     * @param sampleMulti used to increase the sampling of the image to handle memory issues.
     */
    private static BitmapSampled cropBitmap(Context context, Uri loadedImageUri, float[] points,
                                            int degreesRotated, int orgWidth, int orgHeight, boolean fixAspectRatio,
                                            int aspectRatioX, int aspectRatioY, int reqWidth, int reqHeight, int sampleMulti) {

        // get the rectangle in original image that contains the required cropped area (larger for non rectangular crop)
        Rect rect = getRectFromPoints(points, orgWidth, orgHeight, fixAspectRatio, aspectRatioX, aspectRatioY);

        int width = reqWidth > 0 ? reqWidth : rect.width();
        int height = reqHeight > 0 ? reqHeight : rect.height();

        Bitmap result = null;
        int sampleSize = 1;
        try {
            // decode only the required image from URI, optionally sub-sampling if reqWidth/reqHeight is given.
            BitmapSampled bitmapSampled = decodeSampledBitmapRegion(context, loadedImageUri, rect, width, height, sampleMulti);
            result = bitmapSampled.bitmap;
            sampleSize = bitmapSampled.sampleSize;
        } catch (Exception ignored) {
        }

        if (result != null) {
            try {
                // rotate the decoded region by the required amount
                result = rotateBitmap(result, degreesRotated);

                // rotating by 0, 90, 180 or 270 degrees doesn't require extra cropping
                if (degreesRotated % 90 != 0) {

                    // extra crop because non rectangular crop cannot be done directly on the image without rotating first
                    result = cropForRotatedImage(result, points, rect, degreesRotated, fixAspectRatio, aspectRatioX, aspectRatioY);
                }
            } catch (OutOfMemoryError e) {
                if (result != null) {
                    result.recycle();
                }
                throw e;
            }
            return new BitmapSampled(result, sampleSize);
        } else {
            // failed to decode region, may be skia issue, try full decode and then crop
            return cropBitmap(context, loadedImageUri, points, degreesRotated, fixAspectRatio, aspectRatioX, aspectRatioY, sampleMulti, rect, width, height);
        }
    }

    /**
     * Crop bitmap by fully loading the original and then cropping it, fallback in case cropping region failed.
     */
    private static BitmapSampled cropBitmap(Context context, Uri loadedImageUri, float[] points,
                                            int degreesRotated, boolean fixAspectRatio, int aspectRatioX, int aspectRatioY,
                                            int sampleMulti, Rect rect, int width, int height) {
        Bitmap result = null;
        int sampleSize;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = sampleSize = sampleMulti * calculateInSampleSizeByReqestedSize(rect.width(), rect.height(), width, height);

            Bitmap fullBitmap = decodeImage(context.getContentResolver(), loadedImageUri, options);
            if (fullBitmap != null) {
                try {
                    // adjust crop points by the sampling because the image is smaller
                    float[] points2 = new float[points.length];
                    System.arraycopy(points, 0, points2, 0, points.length);
                    for (int i = 0; i < points2.length; i++) {
                        points2[i] = points2[i] / options.inSampleSize;
                    }

                    result = cropBitmapObjectWithScale(fullBitmap, points2, degreesRotated, fixAspectRatio, aspectRatioX, aspectRatioY, 1);
                } finally {
                    if (result != fullBitmap) {
                        fullBitmap.recycle();
                    }
                }
            }
        } catch (OutOfMemoryError e) {
            if (result != null) {
                result.recycle();
            }
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load sampled bitmap: " + loadedImageUri + "\r\n" + e.getMessage(), e);
        }
        return new BitmapSampled(result, sampleSize);
    }

    /**
     * Decode image from uri using "inJustDecodeBounds" to get the image dimensions.
     */
    private static BitmapFactory.Options decodeImageForOption(ContentResolver resolver, Uri uri) throws FileNotFoundException {
        InputStream stream = null;
        try {
            stream = resolver.openInputStream(uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(stream, EMPTY_RECT, options);
            options.inJustDecodeBounds = false;
            return options;
        } finally {
            closeSafe(stream);
        }
    }

    /**
     * Decode image from uri using given "inSampleSize", but if failed due to out-of-memory then raise
     * the inSampleSize until success.
     */
    private static Bitmap decodeImage(ContentResolver resolver, Uri uri, BitmapFactory.Options options) throws FileNotFoundException {
        do {
            InputStream stream = null;
            try {
                stream = resolver.openInputStream(uri);
                return BitmapFactory.decodeStream(stream, EMPTY_RECT, options);
            } catch (OutOfMemoryError e) {
                options.inSampleSize *= 2;
            } finally {
                closeSafe(stream);
            }
        } while (options.inSampleSize <= 512);
        throw new RuntimeException("Failed to decode image: " + uri);
    }

    /**
     * Decode specific rectangle bitmap from stream using sampling to get bitmap with the requested limit.
     *
     * @param sampleMulti used to increase the sampling of the image to handle memory issues.
     */
    private static BitmapSampled decodeSampledBitmapRegion(Context context, Uri uri, Rect rect, int reqWidth, int reqHeight, int sampleMulti) {
        InputStream stream = null;
        BitmapRegionDecoder decoder = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = sampleMulti * calculateInSampleSizeByReqestedSize(rect.width(), rect.height(), reqWidth, reqHeight);

            stream = context.getContentResolver().openInputStream(uri);
            decoder = BitmapRegionDecoder.newInstance(stream, false);
            do {
                try {
                    return new BitmapSampled(decoder.decodeRegion(rect, options), options.inSampleSize);
                } catch (OutOfMemoryError e) {
                    options.inSampleSize *= 2;
                }
            } while (options.inSampleSize <= 512);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load sampled bitmap: " + uri + "\r\n" + e.getMessage(), e);
        } finally {
            closeSafe(stream);
            if (decoder != null) {
                decoder.recycle();
            }
        }
        return new BitmapSampled(null, 1);
    }

    /**
     * Special crop of bitmap rotated by not stright angle, in this case the original crop bitmap contains parts
     * beyond the required crop area, this method crops the already cropped and rotated bitmap to the final
     * rectangle.<br>
     * Note: rotating by 0, 90, 180 or 270 degrees doesn't require extra cropping.
     */
    private static Bitmap cropForRotatedImage(Bitmap bitmap, float[] points, Rect rect, int degreesRotated,
                                              boolean fixAspectRatio, int aspectRatioX, int aspectRatioY) {
        if (degreesRotated % 90 != 0) {

            int adjLeft = 0, adjTop = 0, width = 0, height = 0;
            double rads = Math.toRadians(degreesRotated);
            int compareTo = degreesRotated < 90 || (degreesRotated > 180 && degreesRotated < 270) ? rect.left : rect.right;
            for (int i = 0; i < points.length; i += 2) {
                if (points[i] >= compareTo - 1 && points[i] <= compareTo + 1) {
                    adjLeft = (int) Math.abs(Math.sin(rads) * (rect.bottom - points[i + 1]));
                    adjTop = (int) Math.abs(Math.cos(rads) * (points[i + 1] - rect.top));
                    width = (int) Math.abs((points[i + 1] - rect.top) / Math.sin(rads));
                    height = (int) Math.abs((rect.bottom - points[i + 1]) / Math.cos(rads));
                    break;
                }
            }

            rect.set(adjLeft, adjTop, adjLeft + width, adjTop + height);
            if (fixAspectRatio) {
                fixRectForAspectRatio(rect, aspectRatioX, aspectRatioY);
            }

            Bitmap bitmapTmp = bitmap;
            bitmap = Bitmap.createBitmap(bitmap, rect.left, rect.top, rect.width(), rect.height());
            if (bitmapTmp != bitmap) {
                bitmapTmp.recycle();
            }
        }
        return bitmap;
    }

    /**
     * Calculate the largest inSampleSize value that is a power of 2 and keeps both
     * height and width larger than the requested height and width.
     */
    private static int calculateInSampleSizeByReqestedSize(int width, int height, int reqWidth, int reqHeight) {
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            while ((height / 2 / inSampleSize) > reqHeight && (width / 2 / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * Calculate the largest inSampleSize value that is a power of 2 and keeps both
     * height and width smaller than max texture size allowed for the device.
     */
    private static int calculateInSampleSizeByMaxTextureSize(int width, int height) {
        int inSampleSize = 1;
        if (mMaxTextureSize == 0) {
            mMaxTextureSize = getMaxTextureSize();
        }
        if (mMaxTextureSize > 0) {
            while ((height / inSampleSize) > mMaxTextureSize || (width / inSampleSize) > mMaxTextureSize) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * Rotate the given bitmap by the given degrees.<br>
     * New bitmap is created and the old one is recycled.
     * @param bitmap input bitmap
     * @param degrees given rotation degree
     * @return output bitmap, null if meet {@link OutOfMemoryError}
     */
    @Nullable
    private static Bitmap rotateBitmap(Bitmap bitmap, float degrees) {
        if (degrees > 0) {
            Matrix matrix = new Matrix();
            matrix.setRotate(degrees);
            try {
                Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
                if (newBitmap != bitmap) {
                    bitmap.recycle();
                }
                return newBitmap;
            } catch (OutOfMemoryError error) {
                return null;
            }
        } else {
            return bitmap;
        }
    }

    /**
     * Get the max size of bitmap allowed to be rendered on the device.<br>
     * http://stackoverflow.com/questions/7428996/hw-accelerated-activity-how-to-get-opengl-texture-size-limit.
     */
    private static int getMaxTextureSize() {
        // Safe minimum default size
        final int IMAGE_MAX_BITMAP_DIMENSION = 2048;

        try {
            // Get EGL Display
            EGL10 egl = (EGL10) EGLContext.getEGL();
            EGLDisplay display = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

            // Initialise
            int[] version = new int[2];
            egl.eglInitialize(display, version);

            // Query total number of configurations
            int[] totalConfigurations = new int[1];
            egl.eglGetConfigs(display, null, 0, totalConfigurations);

            // Query actual list configurations
            EGLConfig[] configurationsList = new EGLConfig[totalConfigurations[0]];
            egl.eglGetConfigs(display, configurationsList, totalConfigurations[0], totalConfigurations);

            int[] textureSize = new int[1];
            int maximumTextureSize = 0;

            // Iterate through all the configurations to located the maximum texture size
            for (int i = 0; i < totalConfigurations[0]; i++) {
                // Only need to check for width since opengl textures are always squared
                egl.eglGetConfigAttrib(display, configurationsList[i], EGL10.EGL_MAX_PBUFFER_WIDTH, textureSize);

                // Keep track of the maximum texture size
                if (maximumTextureSize < textureSize[0]) {
                    maximumTextureSize = textureSize[0];
                }
            }

            // Release
            egl.eglTerminate(display);

            // Return largest texture size found, or default
            return Math.max(maximumTextureSize, IMAGE_MAX_BITMAP_DIMENSION);
        } catch (Exception e) {
            return IMAGE_MAX_BITMAP_DIMENSION;
        }
    }

    /**
     * Close the given closeable object (Stream) in a safe way: check if it is null and catch-log
     * exception thrown.
     *
     * @param closeable the closable object to close
     */
    private static void closeSafe(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) {
            }
        }
    }
    //endregion

    //region: Inner class: BitmapSampled

    /**
     * Holds bitmap instance and the sample size that the bitmap was loaded/cropped with.
     */
    static final class BitmapSampled {

        /**
         * The bitmap instance
         */
        public final Bitmap bitmap;

        /**
         * The sample size used to lower the size of the bitmap (1,2,4,8,...)
         */
        final int sampleSize;

        BitmapSampled(Bitmap bitmap, int sampleSize) {
            this.bitmap = bitmap;
            this.sampleSize = sampleSize;
        }
    }
    //endregion

    public static String toString(Bitmap bm) {
        String result = "";
        if (bm != null) {
            result = "Size: " + bm.getWidth() + "x" + bm.getHeight();
            result += " Length: " + FileUtils.getHumanReadableByteCount(bm.getByteCount(), false);
        }
        return result;
    }
}