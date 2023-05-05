package western;

import java.util.ArrayList;
import java.util.List;


public class Brigand extends Personnage implements HorsLaLoi
{
    protected static String LOOK_PAR_DEFAUT = "le méchant";
    private String look = LOOK_PAR_DEFAUT;
    private int recompense = 100;
    private boolean estLibre = true;

    private List<Dame> captives = new ArrayList<>();
    private Cowboy gardien;

    public Brigand(String nom)
    {
        super(nom, boissonParDefaut);
    }

    public Brigand(String nom, Boisson boisson)
    {
        super(nom, boisson);
    }

    public Brigand(String nom, Boisson boisson, String look)
    {
        super(nom, boisson);
        this.look = look;
    }

    public String getLook()
    {
        return this.look;
    }

    public int getRecompense()
    {
        return this.recompense;
    }

    public boolean estLibre() { return this.estLibre; }

    public void kidnapper(Dame dame)
    {
        dame.seFaireKidnapper(this);
        dire(String.format("Ah ah ! %s tu es mienne désormais.", dame.getPseudo()));
        this.recompense += 50;
        captives.add(dame);
    }

    public void seFaireCapturer(Cowboy cowBoy)
    {
        dire(String.format("Damned, je suis fait ! %s, tu m'as eu ! "
                + "Tu n'emporteras pas les %s$ au paradis.", cowBoy.getPseudo(), this.recompense));
        this.estLibre = false;
        this.gardien = cowBoy;
        this.captives.clear();
    }

    public void seFaireTirerDessus(Cowboy cowBoy)
    {
        dire(String.format("Tu n'es qu'un coyote, %s !", cowBoy.getPseudo()));
    }

    @Override
    public String getPseudo()
    {
        return super.getNom() + " " + this.look;
    }

    public List<Dame> getCaptives()
    {
        return new ArrayList<>(this.captives);
    }

    @Override
    public void sePresenter()
    {
        super.sePresenter();
        this.dire("Ma tête est mise à prix " + this.recompense + "$ !");
        String texte;
        if (this.estLibre)
        {
            texte = "Je suis libre";
            if (!this.captives.isEmpty())
                texte += " et en bonne compagnie avec " + getPseudos(this.captives);
        }
        else
        {
            texte = "Je suis le prisonnier de " + this.gardien.getPseudo();
        }
        this.dire(texte + ".");
    }

    private static Boisson boissonParDefaut = new Boisson("tord-boyaux", Genre.MASCULIN);

    public static Boisson getBoissonParDefaut()
    {
        return boissonParDefaut;
    }

    public static void setBoissonParDefaut(Boisson boisson)
    {
        boissonParDefaut = boisson;
    }

}