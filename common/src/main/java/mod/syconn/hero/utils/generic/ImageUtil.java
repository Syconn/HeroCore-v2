package mod.syconn.hero.utils.generic;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

public class ImageUtil {

    public static void modifyTexture(DynamicTexture texture, TriFunction<Integer, Integer, Integer, Integer> function) {
        var image = texture.getPixels();
        if (image != null) {
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    image.setPixelRGBA(x, y, function.apply(x, y, image.getPixelRGBA(x, y)));
                }
            }
            texture.upload();
        }
    }

    public static int[][] cut(@NotNull NativeImage image, int uX, int uY, int width, int height) { // TODO MAY NEED TO UPLOAD?
        var pixels = new int[width][height];
        for (var x = uX; x < uX + width; x++) {
            for (var y = uY; y < uY + height; y++) {
                pixels[x - uX][y - uY] = image.getPixelRGBA(x, y);
                image.setPixelRGBA(x, y, ColorUtil.packArgb(0, 0, 0, 0));
            }
        }
        return pixels;
    }

    public static void paste(@NotNull NativeImage image, int[][] pixels, int uX, int uY, int width, int height) { // TODO MAY NEED TO UPLOAD?
        for (var x = uX; x < uX + width; x++) for (var y = uY; y < uY + height; y++) image.setPixelRGBA(x, y, pixels[x - uX][y - uY]);
    }

    public static void erase(@NotNull NativeImage image, int uX, int uY, int width, int height) { // TODO MAY NEED TO UPLOAD?
        for (var x = uX; x < uX + width; x++) for (var y = uY; y < uY + height; y++) image.setPixelRGBA(x, y, ColorUtil.packArgb(0, 0, 0, 0));
    }

    public static void translate(@NotNull NativeImage image, int uX, int uY, int width, int height, int xOffset, int yOffset) {
        ImageUtil.paste(image, ImageUtil.cut(image, uX, uY, width, height), uX + xOffset, uY + yOffset, width, height);
    }
}
