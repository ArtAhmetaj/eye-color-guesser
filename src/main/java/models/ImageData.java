package models;

public class ImageData {

    private final int height;
    private final int width;
    private final RgbPixel[][] rgbRgbPixels;

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public RgbPixel[][] getRgbPixels() {
        return rgbRgbPixels;
    }


    private ImageData(ImageDataBuilder builder) {
        this.height = builder.height;
        this.width = builder.width;
        this.rgbRgbPixels = builder.rgbRgbPixels;
        ;
    }

    public static class ImageDataBuilder {
        private int height;
        private int width;
        private RgbPixel[][] rgbRgbPixels;


        public ImageDataBuilder setHeight(int height) {
            this.height = height;
            return this;
        }

        public ImageDataBuilder setWidth(int width) {
            this.width = width;
            return this;
        }

        public ImageDataBuilder setRgbPixels(RgbPixel[][] rgbRgbPixels) {
            this.rgbRgbPixels = rgbRgbPixels;
            return this;
        }


        public ImageData build() {
            var imageData = new ImageData(this);
            validateImageObject();
            return imageData;

        }

        private void validateImageObject() {
            //TODO: check if height and width aligns with dimensions of rgbpixels matrix
        }
    }


}
