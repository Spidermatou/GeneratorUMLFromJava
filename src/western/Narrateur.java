package western;

public class Narrateur extends Personnage
{
    public Narrateur()
    {
        super("");
    }
    public Narrateur(String nom)
    {
        super(nom);
    }

    @Override
    public void dire(String texte)
    {
        System.out.println(texte);
    }

    @Override
    public void boire(Boisson boisson)
    {
        if (boisson != new Boisson("eau", Genre.FEMININ))
            throw new UnsupportedOperationException();
    }

    @Override
    public void boire()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getPseudo()
    {
        return this.getNom() + " le narrateur";
    }

}
