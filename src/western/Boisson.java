package western;

public class Boisson
{
    private final Substantif substantif;

    public static final Boisson EAU = new Boisson("eau", Genre.FEMININ);
    
    public Boisson(String nom, Genre genre)
    {
        this.substantif = new Substantif(nom, genre);
    }

    public String getNom()
    {
        return this.substantif.getNom();
    }

    public Genre getGenre()
    {
        return this.substantif.getGenre();
    }

    public String getNomAvecArticleIndefini()
    {
        return substantif.avecArticleIndefini();
    }

    public String getNomAvecArticlePartitif()
    {
        return substantif.avecArticlePartitif();
    }

    public String getNomAvecArticleDefini()
    {
        return substantif.avecArticleDefini();
    }

    public String getNomAvecPreposition(String preposition)
    {
        return substantif.avecPreposition(preposition);
    }

}