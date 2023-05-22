package pumlFromJava.classes;

import pumlFromJava.PumlDiagram;

import javax.lang.model.element.*;

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
        //---------------------------
        String constructeur="";

        //Je récupère les modificateurs
        Modificateurs recupLesModificateur=new Modificateurs(petitElement);
        constructeur+=recupLesModificateur.obtenirLesModificateurs();

        //Il y a juste que el.toString() écrit le nom entier avec le package
        constructeur+=" <<create>> "+ PumlDiagram.subStr(grandElement.getSimpleName().toString());
        //TypeElement ede=(TypeElement) e.getEnclosingElement();

        //Parametre
        //mais il n'y a pas encore le nom du paramêtre
         constructeur+=enleverVoidALaFin(petitElement.asType().toString())+"\n";

         //Il faut encore améliorer le constructeur

        //---------------------------
        return constructeur;
    }

    //Une autre méthode maison qui enlève void à la fin (car dans petitElement.asType(), cela met le type et le type d'un constructeur est void)
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
}
