package fr.utc.nf28.moka.util;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static fr.utc.nf28.moka.util.LogUtils.LOGE;

public final class ImageUtils {
	private static final String TEMP_IMAGE_NAME = "moka_upload_temp.jpg";
	/**
	 * hight resolution
	 */
	public static float PICTURE_SIZE_HD = 1900;
	/**
	 * basic resolution
	 */
	public static float PICTURE_SIZE_MR = 1200;
	/**
	 * low resolution
	 */
	public static float PICTURE_SIZE_LR = 800;
	private static File sTempFile;

	/**
	 * Allowed user to resize the temp Picture
	 *
	 * @param size : new size of your image
	 *             PICTURE_SIZE_1900
	 *             PICTURE_SIZE_1200
	 *             PICTURE_SIZE_800
	 */
	public static void resize(File image, float size) {
		if (image.exists()) {
			final Bitmap realImage = BitmapFactory.decodeFile(image.getAbsolutePath());
			final Bitmap newImage = scaleDown(realImage, size, true);

			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(image);
				newImage.compress(Bitmap.CompressFormat.JPEG, 90, fos);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Scale down image
	 *
	 * @param realImage    : image from Temp picture
	 * @param maxImageSize : new max size of image
	 * @param filter       : true
	 * @return new Bitmap resized
	 */
	private static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
		final float ratio = Math.min(
				maxImageSize / realImage.getWidth(),
				maxImageSize / realImage.getHeight());
		final int width = Math.round(ratio * realImage.getWidth());
		final int height = Math.round(ratio * realImage.getHeight());
		return Bitmap.createScaledBitmap(realImage, width, height, filter);
	}

	/**
	 * Create a File for saving our temp image
	 */
	public static File getTempPictureFile() {
		if (sTempFile == null) {
			final File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
					Environment.DIRECTORY_PICTURES), "Moka");

			// Create the storage directory if it does not exist
			if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
				LOGE("Moka", "failed to create directory");
				return new File("/");
			}

			// Create a temp image file name
			sTempFile = new File(mediaStorageDir.getPath() + File.separator + TEMP_IMAGE_NAME);
		}

		return sTempFile;
	}
}
