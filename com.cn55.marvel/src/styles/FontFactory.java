package styles;

import java.awt.*;

public abstract class FontFactory {
    public static Font tabPaneFont() { return  new Font("Product Sans", Font.BOLD, 42); }
    public static Font toolbarButtonFont() { return new Font("Product Sans", Font.BOLD, 30); }
    public static Font buttonFont() { return new Font("Product Sans", Font.BOLD, 28); }
    public static Font labelFont() { return new Font("Product Sans", Font.BOLD, 26); }
    public static Font sliderFont() { return new Font("Product Sans", Font.BOLD, 20); }
    public static Font comboboxFont() { return new Font("Product Sans", Font.PLAIN, 26); }
    static Font titledBorderFont() { return new Font("Product Sans", Font.BOLD, 32); }
    public static Font textFieldFont() { return new Font("Product Sans", Font.PLAIN, 22); }
    public static Font textAreaFont() { return new Font("Product Sans", Font.PLAIN, 26); }
    public static Font tableDataFont() { return new Font("Product Sans", Font.PLAIN, 28); }
    public static Font textPaneFont() { return new Font("Product Sans", Font.BOLD,28); }
    public static Font errorFont() { return new Font("Product Sans", Font.BOLD, 26); }
}
