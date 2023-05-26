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
            //Si je suis au dernier élément de la liste
            if(i==fin.size()-1)
            {
                //J'écris en premier le nom du paramêtre
                constructeur += ve + ":";
                //Puis, son type
                //(Mais pour le dernier parametre, je vais retire ")void" à la fin)
                constructeur += enleverVoidParentheseALaFin(fin.get(i));
            }
            //Sinon
            else
            {
                //J'écris en premier le nom du paramêtre et son type
                constructeur += ve + ":";
                constructeur += fin.get(i);
            }
            i++;
        }

        //Je termine avec une parenthèse
        constructeur+=")\n";

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

    //Une méthode pour enlever ")void" à la fin
    //C'est comme la méthode enleverVoidALaFin, sauf que j'enlève en plus une parenthèse
    public String enleverVoidParentheseALaFin(String texte)
    {
        try
        {
            return texte.substring(0,texte.length()-5);
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
        for(int i=0;i<nom.length();i++)
        {
            if(nom.charAt(i) == ',')
            {
                String retour2 = "";
                String retour2inverse = "";
                int j = i;
                while(nom.charAt(j) != '.')
                {
                    retour2 += nom.charAt(j);
                    j--;
                }

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


        if(position==0)
        {
            retour = nom;
        }
        else
        {
            listePara.add(nom.substring(position + 1));
        }


        //Dans certains cas, les noms sont entre < > (dans des listes, init, etc ...), et comme on garde tout après le dernier point, ce caractère peut rester
        //On va alors simplement l'enlever s'il y a ce caractère
        /*
        if(listePara.get(listePara.size()-1).charAt(listePara.get(listePara.size()-1).length()-1)=='>')
        {
            String nouvMot;
            nouvMot = listePara.get(listePara.size() - 1).substring(0, listePara.get(listePara.size() - 1).length() - 1);

            listePara.remove(listePara.get(listePara.size()-1));
            listePara.add(nouvMot);
        }*/
        return listePara;
    }
}
