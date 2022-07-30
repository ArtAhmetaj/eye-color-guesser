package models;

import java.util.Objects;

public class RgbPixel {

    private final int red;
    private final int green;
    private final int blue;


    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public int sumOfPixels(){
        return this.red + this.green + this.blue;
    }


    public RgbPixel(int rgbPixel) {
        this.red =  (rgbPixel >> 16) & 0x0ff;
        this.green = (rgbPixel >> 8) & 0x0ff;
        this.blue = (rgbPixel) & 0x0ff;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RgbPixel rgbPixel = (RgbPixel) o;
        return red == rgbPixel.red && green == rgbPixel.green && blue == rgbPixel.blue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(red, green, blue);
    }
}
