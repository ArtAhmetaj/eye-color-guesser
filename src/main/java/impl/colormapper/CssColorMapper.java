package impl.colormapper;

import interfaces.colormapper.ColorMapper;
import models.RgbPixel;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public class CssColorMapper implements ColorMapper {
    private static final Map<RgbPixel, String> PIXEL_STRING_MAP;

    private static final int MAXIMUM_DISTANCE = 4;

    @Inject
    public CssColorMapper(){

    }

    @Override
    public String getNameOfColor(RgbPixel rgbPixel) {
        String colorName = "invalid";
        double minimumDistance = Double.MAX_VALUE;
        for(var entry : PIXEL_STRING_MAP.entrySet()){
            var calculatedDistance = calculateDistance(rgbPixel, entry.getKey());
            if (calculatedDistance < minimumDistance) {
                calculatedDistance = minimumDistance;
                colorName =entry.getValue();
            }
        }

        return  colorName;
    }

    @Override
    public boolean isColorCloseToGroupOfColors(RgbPixel rgbPixel, List<RgbPixel> comparableColors) {
        for(var color : comparableColors){
           var distance =  calculateDistance(rgbPixel,color);
           if(distance < MAXIMUM_DISTANCE) return true;
        }
        return false;
    }

    private  double calculateDistance(RgbPixel inputPixel, RgbPixel comparablePixel) {
        double redDistance = inputPixel.getRed() - comparablePixel.getRed();
        double greenDistance = inputPixel.getGreen() - comparablePixel.getGreen();
        double blueDistance = inputPixel.getBlue() - comparablePixel.getBlue();

        return Math.sqrt(Math.pow(redDistance, 2) + Math.pow(greenDistance, 2) + Math.pow(blueDistance, 2));
    }

    static {
        PIXEL_STRING_MAP = Map.<RgbPixel, String>ofEntries(
                entry(new RgbPixel(0, 0, 0), "black"),
                entry(new RgbPixel(192, 192, 192), "silver"),
                entry(new RgbPixel(255, 255, 255), "white"),
                entry(new RgbPixel(128, 0, 0), "maroon"),
                entry(new RgbPixel(255, 0, 0), "red"),
                entry(new RgbPixel(128, 0, 128), "purple"),
                entry(new RgbPixel(0, 128, 0), "green"),
                entry(new RgbPixel(0, 255, 0), "lime"),
                entry(new RgbPixel(128, 128, 0), "olive"),
                entry(new RgbPixel(255, 255, 0), "yellow"),
                entry(new RgbPixel(0, 0, 128), "navy"),
                entry(new RgbPixel(0, 0, 255), "blue"),
                entry(new RgbPixel(0, 128, 128), "teal"),
                entry(new RgbPixel(255, 165, 0), "orange"),
                entry(new RgbPixel(240, 248, 255), "aliceblue"),
                entry(new RgbPixel(250, 235, 215), "antiquewhite"),
                entry(new RgbPixel(127, 255, 212), "aquamarine"),
                entry(new RgbPixel(240, 255, 255), "azure"),
                entry(new RgbPixel(245, 245, 220), "beige"),
                entry(new RgbPixel(255, 228, 196), "bisque"),
                entry(new RgbPixel(255, 235, 205), "blanchedalmond"),
                entry(new RgbPixel(138, 43, 226), "blueviolet"),
                entry(new RgbPixel(165, 42, 42), "brown"),
                entry(new RgbPixel(222, 184, 135), "burlywood"),
                entry(new RgbPixel(95, 158, 160), "cadetblue"),
                entry(new RgbPixel(127, 255, 0), "chartreuse"),
                entry(new RgbPixel(210, 105, 30), "chocolate"),
                entry(new RgbPixel(255, 127, 80), "coral"),
                entry(new RgbPixel(100, 149, 237), "cornflowerblue"),
                entry(new RgbPixel(255, 248, 220), "cornsilk"),
                entry(new RgbPixel(220, 20, 60), "crimson"),
                entry(new RgbPixel(0, 255, 255), "cyan"),
                entry(new RgbPixel(0, 0, 139), "darkblue"),
                entry(new RgbPixel(0, 139, 139), "darkcyan"),
                entry(new RgbPixel(184, 134, 11), "darkgoldenrod"),
                entry(new RgbPixel(0, 100, 0), "darkgreen"),
                entry(new RgbPixel(169, 169, 169), "darkgrey"),
                entry(new RgbPixel(189, 183, 107), "darkkhaki"),
                entry(new RgbPixel(139, 0, 139), "darkmagenta"),
                entry(new RgbPixel(85, 107, 47), "darkolivegreen"),
                entry(new RgbPixel(255, 140, 0), "darkorange"),
                entry(new RgbPixel(153, 50, 204), "darkorchid"),
                entry(new RgbPixel(139, 0, 0), "darkred"),
                entry(new RgbPixel(233, 150, 122), "darksalmon"),
                entry(new RgbPixel(143, 188, 143), "darkseagreen"),
                entry(new RgbPixel(72, 61, 139), "darkslateblue"),
                entry(new RgbPixel(47, 79, 79), "darkslategray"),
                entry(new RgbPixel(0, 206, 209), "darkturquoise"),
                entry(new RgbPixel(148, 0, 211), "darkviolet"),
                entry(new RgbPixel(255, 20, 147), "deeppink"),
                entry(new RgbPixel(0, 191, 255), "deepskyblue"),
                entry(new RgbPixel(105, 105, 105), "dimgray"),
                entry(new RgbPixel(30, 144, 255), "dodgerblue"),
                entry(new RgbPixel(178, 34, 34), "firebrick"),
                entry(new RgbPixel(255, 250, 240), "floralwhite"),
                entry(new RgbPixel(34, 139, 34), "forestgreen"),
                entry(new RgbPixel(220, 220, 220), "gainsboro"),
                entry(new RgbPixel(248, 248, 255), "ghostwhite"),
                entry(new RgbPixel(255, 215, 0), "gold"),
                entry(new RgbPixel(218, 165, 32), "goldenrod"),
                entry(new RgbPixel(173, 255, 47), "greenyellow"),
                entry(new RgbPixel(128, 128, 128), "grey"),
                entry(new RgbPixel(240, 255, 240), "honeydew"),
                entry(new RgbPixel(255, 105, 180), "hotpink"),
                entry(new RgbPixel(205, 92, 92), "indianred"),
                entry(new RgbPixel(75, 0, 130), "indigo"),
                entry(new RgbPixel(255, 255, 240), "ivory"),
                entry(new RgbPixel(240, 230, 140), "khaki"),
                entry(new RgbPixel(230, 230, 250), "lavender"),
                entry(new RgbPixel(255, 240, 245), "lavenderblush"),
                entry(new RgbPixel(124, 252, 0), "lawngreen"),
                entry(new RgbPixel(255, 250, 205), "lemonchiffon"),
                entry(new RgbPixel(173, 216, 230), "lightblue"),
                entry(new RgbPixel(240, 128, 128), "lightcoral"),
                entry(new RgbPixel(224, 255, 255), "lightcyan"),
                entry(new RgbPixel(250, 250, 210), "lightgoldenrodyellow"),
                entry(new RgbPixel(144, 238, 144), "lightgreen"),
                entry(new RgbPixel(211, 211, 211), "lightgrey"),
                entry(new RgbPixel(255, 182, 193), "lightpink"),
                entry(new RgbPixel(255, 160, 122), "lightsalmon"),
                entry(new RgbPixel(32, 178, 170), "lightseagreen"),
                entry(new RgbPixel(135, 206, 250), "lightskyblue"),
                entry(new RgbPixel(119, 136, 153), "lightslategrey"),
                entry(new RgbPixel(176, 196, 222), "lightsteelblue"),
                entry(new RgbPixel(255, 255, 224), "lightyellow"),
                entry(new RgbPixel(50, 205, 50), "limegreen"),
                entry(new RgbPixel(250, 240, 230), "linen"),
                entry(new RgbPixel(255, 0, 255), "magenta"),
                entry(new RgbPixel(102, 205, 170), "mediumaquamarine"),
                entry(new RgbPixel(0, 0, 205), "mediumblue"),
                entry(new RgbPixel(186, 85, 211), "mediumorchid"),
                entry(new RgbPixel(147, 112, 219), "mediumpurple"),
                entry(new RgbPixel(60, 179, 113), "mediumseagreen"),
                entry(new RgbPixel(123, 104, 238), "mediumslateblue"),
                entry(new RgbPixel(0, 250, 154), "mediumspringgreen"),
                entry(new RgbPixel(72, 209, 204), "mediumturquoise"),
                entry(new RgbPixel(199, 21, 133), "mediumvioletred"),
                entry(new RgbPixel(25, 25, 112), "midnightblue"),
                entry(new RgbPixel(245, 255, 250), "mintcream"),
                entry(new RgbPixel(255, 228, 225), "mistyrose"),
                entry(new RgbPixel(255, 228, 181), "moccasin"),
                entry(new RgbPixel(255, 222, 173), "navajowhite"),
                entry(new RgbPixel(253, 245, 230), "oldlace"),
                entry(new RgbPixel(107, 142, 35), "olivedrab"),
                entry(new RgbPixel(255, 69, 0), "orangered"),
                entry(new RgbPixel(218, 112, 214), "orchid"),
                entry(new RgbPixel(238, 232, 170), "palegoldenrod"),
                entry(new RgbPixel(152, 251, 152), "palegreen"),
                entry(new RgbPixel(175, 238, 238), "paleturquoise"),
                entry(new RgbPixel(219, 112, 147), "palevioletred"),
                entry(new RgbPixel(255, 239, 213), "papayawhip"),
                entry(new RgbPixel(255, 218, 185), "peachpuff"),
                entry(new RgbPixel(205, 133, 63), "peru"),
                entry(new RgbPixel(255, 192, 203), "pink"),
                entry(new RgbPixel(221, 160, 221), "plum"),
                entry(new RgbPixel(176, 224, 230), "powderblue"),
                entry(new RgbPixel(188, 143, 143), "rosybrown"),
                entry(new RgbPixel(65, 105, 225), "royalblue"),
                entry(new RgbPixel(139, 69, 19), "saddlebrown"),
                entry(new RgbPixel(250, 128, 114), "salmon"),
                entry(new RgbPixel(244, 164, 96), "sandybrown"),
                entry(new RgbPixel(46, 139, 87), "seagreen"),
                entry(new RgbPixel(255, 245, 238), "seashell"),
                entry(new RgbPixel(160, 82, 45), "sienna"),
                entry(new RgbPixel(135, 206, 235), "skyblue"),
                entry(new RgbPixel(106, 90, 205), "slateblue"),
                entry(new RgbPixel(112, 128, 144), "slategray"),
                entry(new RgbPixel(255, 250, 250), "snow"),
                entry(new RgbPixel(0, 255, 127), "springgreen"),
                entry(new RgbPixel(70, 130, 180), "steelblue"),
                entry(new RgbPixel(210, 180, 140), "tan"),
                entry(new RgbPixel(216, 191, 216), "thistle"),
                entry(new RgbPixel(255, 99, 71), "tomato"),
                entry(new RgbPixel(64, 224, 208), "turquoise"),
                entry(new RgbPixel(238, 130, 238), "violet"),
                entry(new RgbPixel(245, 222, 179), "wheat"),
                entry(new RgbPixel(245, 245, 245), "whitesmoke"),
                entry(new RgbPixel(154, 205, 50), "yellowgreen"),
                entry(new RgbPixel(102, 51, 153), "rebeccapurple")
        );
    }


}
