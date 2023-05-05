package western;

import java.util.List;

public abstract class Personnage implements Nommable
{
    private static Boisson BOISSON_PAR_DEFAUT = new Boisson("eau", Genre.FEMININ);
    private String nom;
    private Boisson boisson;

    public Personnage(String nom)
    {
        this(nom, BOISSON_PAR_DEFAUT);
    }

    public Personnage(String nom, Boisson boisson)
    {
        this.nom = nom;
        this.boisson = boisson;
    }

    public void dire(String texte)
    {
        System.out.println(String.format("%s - %s", this.nom, texte));
    }
    public void sePresenter()
    {
        String presentation = String.format("Bonjour, je suis %s et j'aime %s.",
                this.getPseudo(), this.boisson.getNomAvecArticleDefini());
        dire(presentation);
    }

    public void boire(Boisson boisson)
    {
        String deLaBoisson = boisson.getNomAvecArticlePartitif();
        if (boisson == this.boisson)
            dire(String.format("Ah ! boire %s ! GLOUPS !", deLaBoisson));
        else
            dire("GLOUPS ! Faut vraiment avoir soif pour boire " + deLaBoisson
                    + " ! j'aurais préféré boire " + this.boisson.getNomAvecArticlePartitif() + ".");
    }

    public void boire()
    {
        boire(this.boisson);
    }

    public String getNom()
    {
        return this.nom;
    }
    public Boisson getBoisson()
    {
        return this.boisson;
    }

    public static String getPseudos(List<? extends Nommable> personnages)
    {
        String pseudos;
        int n = personnages.size();
        if (n == 0)
            pseudos = "";
        else
            pseudos = personnages.get(0).getNom();
        for (int i = 1; i < n - 1; i++)
        {
            pseudos += ", " + personnages.get(i).getPseudo();
        }
        if (n > 1)
            pseudos += " et " + personnages.get(n-1).getPseudo();
        return pseudos;
    }

    public static Boisson getBoissonParDefaut()
    {
        return BOISSON_PAR_DEFAUT;
    }
}