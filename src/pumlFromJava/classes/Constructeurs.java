package pumlFromJava.classes;

import pumlFromJava.PumlDiagram;

import javax.lang.model.element.*;
import java.util.ArrayList;
import java.util.List;

public class Constructeurs
{
    private final Element grandElement;
    private final Element petitElement;

    public Constructeurs(Element grandElement,Element petitElement)
    {
        //On a 2 éléments
        //Le grand élément est la classe
        this.grandElement=grandElement;
        //Le petit est le constructeur (on fait une vérification dans la classe PumlDiagramm pour savoir si c'est un constructeur)
        this.petitElement=petitElement;
    }

    public String obtenirLeOuLesConstructeurs()
    {
        String constructeur="";

        //Je récupère les modificateurs
        Modificateurs recupLesModificateur=new Modificateurs(petitElement);
        constructeur+=recupLesModificateur.obtenirLesModificateurs();

        //Il y a juste que el.toString() écrit le nom entier avec le package

        //On écrit le stéréotype <<create>> et le nom de la classe
        constructeur+=" <<create>> "+ PumlDiagram.subStr(grandElement.getSimpleName().toString())+" (";

        //Parametres
        //Je met dans une liste, chaque type de paramêtre du constructeur
        //(grâce à la methode subStrParametre)
        List<String> fin = subStrParametre(petitElement.asType().toString());
        int i=0;

        //Je cast et récupère dans une liste de VariableElement les nom des paramêtre
        ExecutableElement ee=(ExecutableElement)petitElement;
        List<? extends  VariableElement> lst=ee.getParameters();

        //Comme on a le même élément (c'est-à-dire le constructeur), les 2 listes auront la même taille (car un nom de paramêtre correspond à un type)

        //Pour chaque élément dans la liste des nom
        for(VariableElement ve:lst)
        {
            if(lst.size()>0)
            {
                if (i != 0)
                    constructeur += ",";

                //J'écris en premier le nom du paramêtre et son type
                constructeur += ve + ":";
                constructeur += fin.get(i);

                i++;
            }
        }

        //Je termine avec une parenthèse
        constructeur+=")\n";

        return constructeur;
    }

    //Une autre méthode maison qui enlève void à la fin (car dans petitElement.asType(), cela met le type et le type de retour d'un constructeur est void)
    public String enleverVoidALaFin(String texte)
    {
        try
        {
            return texte.substring(0,texte.length()-4);
        }
        catch (IndexOutOfBoundsException enDehorsDesLimites)
        {
            System.out.println(enDehorsDesLimites);
            return texte;
        }
    }

    //Une méthode pour enlever ")void" à la fin
    //C'est comme la méthode enleverVoidALaFin, sauf que j'enlève en plus une parenthèse
    public static String enleverVoidParentheseALaFin(String texte)
    {
        try
        {
            return texte.substring(0,texte.length()-4);
        }
        catch (IndexOutOfBoundsException enDehorsDesLimites)
        {
            System.out.println(enDehorsDesLimites);
            return texte;
        }
    }

    //Une méthode qui enlève une parenthèse au début et parenthèse et void à la fin
    public static String enleverParentheseDebutEtFinEtVoid(String s)
    {
        try
        {
            s=s.substring(1,s.length()-1);
            s=enleverVoidParentheseALaFin(s);
            return s;
        }
        catch(IndexOutOfBoundsException e)
        {
            System.out.println(e);
            return s;
        }
    }

    //La méthode qui renvoie une liste avec le nom des paramêtres du constructeur
    public static List<String> subStrParametre(String nom)
    {
        //J'enlève la 1ère et dernière parenthèse et void à la fin
        nom=enleverParentheseDebutEtFinEtVoid(nom);
        //System.out.println(nom);
        String retour = "";
        //La liste qui contiendra les nom des paramêtres
        List<String> listePara = new ArrayList<>();

        int nbPara=0;
        int nbAjout=0;

        //Si je n'ai pas une chaîne vide cela veut dire que j'ai au moins un paramêtre
        if(!nom.equals(""))
            nbPara=1;

        //Je compte chaque virgule et incrémente de 1 le nombre de paramêtre
        for(int i=0;i<nom.length();i++)
            if(nom.charAt(i)==',')
                nbPara++;

        boolean fini=false;
        boolean passe=false;

        //Tant que je n'ai pas ajouté dans la liste, le nombre de paramêtre
        while(nbPara!=nbAjout)
        {
            //Si je n'ai qu'un seul paramêtre
            if(nbPara==1)
            {
                //Je récupère le nom du paramêtre
                retour=PumlDiagram.subStr(nom);
                //Et j'ajoute à la liste
                listePara.add(retour);
                //J'incrémente la variable qui compte le nombre d'ajoute
                nbAjout++;
                //Je mets le bouléen passe à true pour passer la partie suivante
                passe=true;
            }

            fini=false;
            int i=0;

            //Si je ne passe pas (donc que j'ai plus de 1 paramêtre)
            if(!passe)
            {
                while (!fini)
                {
                    if (i == nom.length())
                    {
                        retour = PumlDiagram.subStr(nom);
                        listePara.add(retour);
                        nbAjout++;
                        fini = true;
                        //System.out.println(retour);
                    }
                    else if (nom.charAt(i) == ',')
                    {
                        retour = nom.substring(0, i);
                        retour = PumlDiagram.subStr(retour);
                        nom = nom.substring(i + 1, nom.length());
                        listePara.add(retour);
                        nbAjout++;
                        fini = true;
                        //System.out.println(retour);
                    }

                    i++;
                }
            }
        }

        //System.out.println(listePara);
        return listePara;
    }
}
