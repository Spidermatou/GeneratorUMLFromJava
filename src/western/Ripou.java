package western;

import java.util.List;

public class Ripou extends Cowboy implements HorsLaLoi
{
    private Brigand brigand;

    public Ripou(String nom)
    {
        super(nom, getBoissonParDefaut());
        this.brigand = new Brigand(super.getNom());
    }

    public Ripou(String nom, Boisson boisson)
    {
        super(nom, boisson);
        this.brigand = new Brigand(super.getNom(), boisson);
    }

    public Ripou(String nom, Boisson boisson, String look)
    {
        super(nom, boisson);
        this.brigand = new Brigand(super.getNom(), boisson, look);
    }

    public String getLook()
    {
        return this.brigand.getLook();
    }

    @Override
    public boolean estLibre()
    {
        return this.brigand.estLibre();
    }

    @Override
    public int getRecompense()
    {
        return this.brigand.getRecompense();
    }

    @Override
    public void kidnapper(Dame dame)
    {
        this.brigand.kidnapper(dame);
    }

    @Override
    public List<Dame> getCaptives()
    {
        return this.brigand.getCaptives();
    }

    @Override
    public void seFaireCapturer(Cowboy cowBoy)
    {
        this.brigand.seFaireCapturer(cowBoy);
    }

    @Override
    public void seFaireTirerDessus(Cowboy cowBoy)
    {
        this.brigand.seFaireTirerDessus(cowBoy);
    }

    @Override
    public String getPseudo()
    {
        String nom;

        if (this.estLibre())
            nom = super.getPseudo();
        else
            nom = this.brigand.getPseudo();
        return nom;
    }

    @Override
    public void sePresenter()
    {
        if (this.estLibre())
            super.sePresenter();
        else
            this.brigand.sePresenter();
    }

    @Override
    public void liberer(Dame dame)
    {
        if (dame.estLibre())
            throw new IllegalStateException("La dame est déjà libre");
        this.kidnapper(dame);
    }

}