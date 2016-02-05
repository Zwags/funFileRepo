package simplegui;

public class RGB {

    public int r, g, b;
    public int brightness;

    public RGB() {
        r = g = b = 0;
    }

    public RGB(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
        brightness = (int) Math.round(0.2989 * r + 0.5870 * g + 0.1140 * b);
    }
}
