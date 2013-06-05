package fr.utc.nf28.moka.util;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PictureUtils {

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

			try {
				FileOutputStream fos = new FileOutputStream(image);
				if (fos != null) {
					newImage.compress(Bitmap.CompressFormat.JPEG, 90, fos);
					fos.close();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
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
				(float) maxImageSize / realImage.getWidth(),
				(float) maxImageSize / realImage.getHeight());
		final int width = Math.round((float) ratio * realImage.getWidth());
		final int height = Math.round((float) ratio * realImage.getHeight());

		final Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
				height, filter);
		return newBitmap;
	}
}
