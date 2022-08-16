package interfaces.colormapper;

import models.RgbPixel;
import org.w3c.dom.css.RGBColor;

import java.util.List;

public interface ColorMapper {
    String getNameOfColor(RgbPixel rgbPixel);
    boolean isColorCloseToGroupOfColors(RgbPixel rgbPixel, List<RgbPixel> comparableColors);

}
