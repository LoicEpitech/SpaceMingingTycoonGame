package world.asset;

public class DecorData {
    public String path;
    public double x, y, width, height;
    public boolean aboveBackground; // true = au-dessus du background, false = en-dessous

    public DecorData(String path, double x, double y, double width, double height) {
        this(path, x, y, width, height, true); // par défaut true
    }

    public DecorData(String path, double x, double y, double width, double height, boolean aboveBackground) {
        this.path = path;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.aboveBackground = aboveBackground;
    }
}
