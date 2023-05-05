package western;

import java.util.List;

public interface HorsLaLoi extends Nommable
{
    boolean estLibre();
    int getRecompense();
    void kidnapper(Dame dame);
    void seFaireCapturer(Cowboy cowBoy);
    void seFaireTirerDessus(Cowboy cowBoy);
    String getLook();
    List<Dame> getCaptives();
}
