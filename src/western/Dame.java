package western;

public class Dame extends Personnage
{
    private boolean estLibre = true;
    private Cowboy sauveur;
    private HorsLaLoi ravisseur;

    public Dame(String nom)
    {
        super(nom, boissonParDefaut);
    }

    public Dame(String nom, Boisson boisson)
    {
        super(nom, boisson);
    }

    @Override
    public String getPseudo()
    {
        return "Miss " + super.getNom();
    }

    public void seFaireKidnapper(HorsLaLoi horsLaLoi)
    {
        this.dire("Hiiii ! Au secours ! " + horsLaLoi.getPseudo() + " m'enlève !");
        this.estLibre = false;
        this.ravisseur = horsLaLoi;
    }

    public void seFaireLiberer(Cowboy cowBoy)
    {
        if (this.estLibre)
            throw new IllegalStateException("La dame est déjà libre");
        this.dire(String.format("Merci %s de m'avoir libérée.", cowBoy.getPseudo()));
        this.estLibre = true;
        this.sauveur = cowBoy;
    }

    public boolean estLibre()
    {
        return this.estLibre;
    }

    @Override
    public void sePresenter()
    {
        super.sePresenter();
        String texte;
        if (this.estLibre)
        {
            texte = "Je suis libre";
            if (this.sauveur != null)
            {
                texte += " grace à " + sauveur.getPseudo();
            }
        }
        else
        {
            texte = "Je suis captive de cet escroc de " + this.ravisseur.getPseudo();
        }
        this.dire(texte + ".");
    }

    private static Boisson boissonParDefaut = new Boisson("thé", Genre.MASCULIN);

    public static Boisson getBoissonParDefaut()
    {
        return boissonParDefaut;
    }

    public static void setBoissonParDefaut(Boisson boisson)
    {
        boissonParDefaut = boisson;
    }

    public HorsLaLoi getRavisseur()
    {
        return this.ravisseur;
    }
}
