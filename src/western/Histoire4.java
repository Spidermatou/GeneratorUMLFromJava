package western;

//  @ Project : Western
//  @ File Name : Histoire6.java
//  @ Date : 25/03/2023
//  @ Author : R. Schneider
//
//

public class Histoire4
{
    public static void main(String[] args)
    {
        Cowboy luke = new Cowboy("Luke");
        Brigand averell = new Brigand("Averell", new Boisson("whisky", Genre.MASCULIN), "le nigaud");
        Dame scarlett = new Dame("Scarlett");
        Dame daisy = new Dame("Daisy");
        Narrateur narrateur = new Narrateur("Ed");

        Ripou pat = new Ripou("Pat", new Boisson("scotch", Genre.MASCULIN), "le ripou");

        /* Les présentations */
        narrateur.sePresenter();
        narrateur.dire("Enfin ... surtout quand il fait trop chaud pour boire autre chose.");

        narrateur.dire("Il était une fois, à l'Ouest de Java Town, six personnages bien singuliers :");
        scarlett.sePresenter();
        daisy.sePresenter();
        luke.sePresenter();
        averell.sePresenter();
        pat.sePresenter();

        /* un peu d'action */
        narrateur.dire("Et maintenant, un peu d'action.");

        narrateur.dire(averell.getNom() + " kidnappe " + scarlett.getNom()
                + " et " + daisy.getNom() + ".");
        averell.kidnapper(scarlett);
        averell.kidnapper(daisy);

        narrateur.dire("Mais " + pat.getNom() + " intervient sans tarder et capture "
                + averell.getNom() + " pour s'emparer des dames captives.");
        pat.tirerSur(averell);
        pat.capturer(averell);

        narrateur.dire("Vous avez bien dit Pat ?");
        pat.sePresenter();

        narrateur.dire("C'était compter sans " + luke.getNom() + " qui intervient aussitôt et capture "
                + pat.getNom() + " pour libérer les dames.");
        luke.tirerSur(pat);
        luke.capturer(pat);

        narrateur.dire("Bien joué " + luke.getPseudo() + "!");
        luke.sePresenter();

        narrateur.dire("Mesdames, pouvez-vous nous confirmer que vous êtes libres ? ");
        scarlett.sePresenter();
        daisy.sePresenter();

        narrateur.dire("Et le ripou a-t-il été confondu ? ");
        pat.sePresenter();

        narrateur.dire("Il en reste un qui s'en est plutôt bien tiré !");
        averell.sePresenter();

        narrateur.dire("That's all folks.");
    }
}

