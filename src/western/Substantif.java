package western;

public class Substantif // un nom
{
    private String nom;
    private Genre genre;

    public Substantif(String nom, Genre genre)
    {
        this.nom = nom;
        this.genre = genre;
    }

    public String getNom()
    {
        return nom;
    }

    public Genre getGenre()
    {
        return this.genre;
    }

    @Override
    public String toString()
    {
        return this.nom;
    }

    public static boolean isVowel(char c)
    {
        return "aeiouy".indexOf(c) != -1;
    }

    public static boolean startsWithVowel(String s)
    {
        return s.length() > 0 && isVowel(s.charAt(0));
    }

    public String getArticleIndefini()
    {
        String article;
        if (this.genre == Genre.MASCULIN)
            article = "un";
        else
            article = "une";
        return article;
    }

    public String avecArticleIndefini()
    {
        return this.getArticleIndefini() + " " + this.nom;
    }

    public static String getArticleDefini(Genre genre)
    {
        return (genre == Genre.MASCULIN)? "le" : "la";
    }

    public String getArticleDefini()
    {
        String article = getArticleDefini(this.genre);
        if (elision())
            article = faireElision(article);
        return article;
    }

    public String avecArticleDefini()
    {
        return this.avecElision(this.getArticleDefini());
    }

    public static String faireElision(String article)
    {
        return article.substring(0, article.length()-1) + "'" ;
    }

    public boolean elision()
    {
        return startsWithVowel(this.nom); // pour simplifier on ignore la règle du h aspiré
    }

    public String getArticlePartitif()
    {
        String articleDefini = this.getArticleDefini();
        String articlePartitif;
        if (articleDefini.equals("le"))
            articlePartitif = "du";
        else
            articlePartitif = "de " + articleDefini;
        return articlePartitif;
    }

    public String avecArticlePartitif()
    {
        return this.avecElision(this.getArticlePartitif());
    }

    public String avecElision(String article)
    {
        if (!elision())
            article += " ";
        return article + nom;
    }

    public String avecPreposition(String preposition)
    {
        if (elision())
            preposition = faireElision(preposition);
        return this.avecElision(preposition);
    }
}
