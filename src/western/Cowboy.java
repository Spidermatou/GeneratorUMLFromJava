package western;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Cowboy extends Personnage
{
    private int gains = 0;

    private Set<HorsLaLoi> captures = new HashSet<>();

    public Cowboy(String nom)
    {
        super(nom, boissonParDefaut);
    }

    public Cowboy(String nom, Boisson boisson)
    {
        super(nom, boisson);
    }

    @Override
    public String getPseudo()
    {
        return this.getNom();
    }

    public void liberer(Dame dame)
    {
        if (dame.estLibre())
            throw new IllegalStateException("La dame est déjà libre");
        dire(String.format("Voilà %s, tu es libre maintenant !", dame.getPseudo()));
        dame.seFaireLiberer(this);
    }

    public void liberer(List<Dame> captives)
    {
        for (Dame dame : captives)
        {
            this.liberer(dame);
        }
    }

    public void capturer(HorsLaLoi horsLaLoi)
    {
        if (!horsLaLoi.estLibre())
            throw new IllegalStateException("Le brigand n'est pas libre");
        List<Dame> captives = horsLaLoi.getCaptives();
        horsLaLoi.seFaireCapturer(this);
        this.liberer(captives);
        this.gains += horsLaLoi.getRecompense();
        this.captures.add(horsLaLoi);
    }

    public void tirerSur(HorsLaLoi brigand)
    {
        this.dire(String.format("PAN ! PAN ! Prend ça, chacal de %s !", brigand.getPseudo()));
        brigand.seFaireTirerDessus(this);
    }

    @Override
    public void sePresenter()
    {
        super.sePresenter();
        if (this.gains > 0)
            this.dire("J’ai déjà empoché " + this.gains + "$ en capturant "
                    + getPseudos(List.copyOf(this.captures)) + ".");
    }

    private static Boisson boissonParDefaut = new Boisson("bière", Genre.FEMININ);

    public static Boisson getBoissonParDefaut()
    {
        return boissonParDefaut;
    }

    public static void setBoissonParDefaut(Boisson boisson)
    {
        boissonParDefaut = boisson;
    }

}


