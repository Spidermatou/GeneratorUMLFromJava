package pumlFromJava.classes;

import pumlFromJava.PumlDiagram;

import javax.lang.model.element.*;
import javax.lang.model.util.ElementFilter;
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
        //---------------------------
        String constructeur="";

        //Je récupère les modificateurs
        Modificateurs recupLesModificateur=new Modificateurs(petitElement);
        constructeur+=recupLesModificateur.obtenirLesModificateurs();

        //Il y a juste que el.toString() écrit le nom entier avec le package
        constructeur+=" <<create>> "+ PumlDiagram.subStr(grandElement.getSimpleName().toString());
        //TypeElement ede=(TypeElement) e.getEnclosingElement();

        //Parametre
        //mais il n'y a pas encore le nom du
        List<String> fin = subStrParametre( petitElement.asType().toString());


         constructeur+=enleverVoidALaFin( )+"\n";



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

    public static List<String> subStrParametre(String nom)
    {
        String retour = "";
        List<String> listePara = new ArrayList<>();
        int position=0;
        int position2 = -5;
        for(int i=0;i<nom.length();i++)
        {
            if(nom.charAt(i) == ',')
            {
                String retour2 = "";
                String retour2inverse = "";
                int j = position2;
                while(nom.charAt(j) != '.')
                {
                    retour2 += nom.charAt(j);
                    j--;
                }

            /*for(int k = retour2.length(); k == 0; k--)
            {
                retour2inverse += retour2.charAt(k);
            }*/
                int k = retour2.length()-1;
                while(k >= 0)
                {
                    retour2inverse += retour2.charAt(k);
                    k--;
                }
                listePara.add(retour2inverse);
            }
            if(nom.charAt(i)=='.')
            {
                position=i;
            }
        }


        if(position==0) {
            retour = nom;
        }
        else  {
            listePara.add(nom.substring(position + 1));
        }


        //Dans certains cas, les noms sont entre < > (dans des listes, init, etc ...), et comme on garde tout après le dernier point, ce caractère peut rester
        //On va alors simplement l'enlever s'il y a ce caractère
        if(listePara.get(listePara.size()-1).charAt(listePara.get(listePara.size()-1).length()-1)=='>') {
            String nouvMot;
            nouvMot = listePara.get(listePara.size() - 1).substring(0, listePara.get(listePara.size() - 1).length() - 1);

            listePara.remove(listePara.get(listePara.size()-1));
            listePara.add(nouvMot);
        }
        return listePara;
    }
}
