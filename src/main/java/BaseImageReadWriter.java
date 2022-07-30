import dagger.Component;
import interfaces.ImageReadWriter;
import models.ImageData;
import models.RgbPixel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@Component
public class BaseImageReadWriter implements ImageReadWriter {


    @Override
    public ImageData readImage(String path) throws IOException {
        BufferedImage image = ImageIO.read(new File(path));

        //TODO: implement specific exceptions based on use case, file not found will do for now
        if (image == null) throw new FileNotFoundException();
        var imageBuilder = new ImageData.ImageDataBuilder();
        return imageBuilder.setHeight(image.getHeight()).setRgbPixels(getRgbPixels(image))
                .setWidth(image.getWidth()).build();


    }

    @Override
    public void saveImage(String path, String suffix, int[][] rgbPixels) throws IOException {
        var savedOutput = new File(String.format("%s.%s", path, suffix));
        var bufferedImage = new BufferedImage(rgbPixels.length, rgbPixels[0].length, BufferedImage.TYPE_INT_RGB);

        ImageIO.write(bufferedImage, suffix, savedOutput);
    }

    private RgbPixel[][] getRgbPixels(BufferedImage image) {
        var imagePixels = new RgbPixel[image.getHeight()][image.getWidth()];
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                imagePixels[i][j] = new RgbPixel(image.getRGB(i, j));
            }
        }
        return imagePixels;
    }
}
