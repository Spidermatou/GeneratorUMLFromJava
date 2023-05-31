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
            if(lst.size()>0) {


                if (i != 0)
                    constructeur += ",";

                //Si je suis au dernier élément de la liste
                if (i == fin.size() - 1) {
                    //J'écris en premier le nom du paramêtre
                    constructeur += ve + ":";
                    //Puis, son type
                    //(Mais pour le dernier parametre, je vais retire ")void" à la fin)
                    constructeur += fin.get(i);
                    //System.out.println(ve +" et "+enleverVoidParentheseALaFin(fin.get(i)));


                }
                //Sinon
                else {
                    //J'écris en premier le nom du paramêtre et son type
                    constructeur += ve + ":";
                    constructeur += fin.get(i);
                    //System.out.println(ve +" et "+fin.get(i));

                }

                i++;
            }
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

    public static String enleverParentheseDebutEtFinEtVoid(String s)
    {
        try
        {
            s=s.substring(1,s.length()-1);
            s=s.substring(0,s.length()-4);
            return s;
        }
        catch(IndexOutOfBoundsException e)
        {
            return s;
        }
    }

    //OK CA MARCHE ENFFFFIIIINNNN !!!
    //VICTOIRE CAMARADE !!!!!
    //par contre el margoulin mamamia pizza baguette croissant pain au chocolat ou bonne chocolatine du sud de la france
    public static List<String> subStrParametre(String nom)
    {
        nom=enleverParentheseDebutEtFinEtVoid(nom);
        System.out.println(nom);
        String retour = "";
        List<String> listePara = new ArrayList<>();

        int nbPara=0;
        int nbAjout=0;

        if(!nom.equals(""))
            nbPara=1;

        for(int i=0;i<nom.length();i++)
            if(nom.charAt(i)==',')
                nbPara++;

        boolean fini=false;
        boolean passe=false;
        while(nbPara!=nbAjout)
        {
            if(nbPara==1)
            {
                retour=PumlDiagram.subStr(nom);
                listePara.add(retour);
                nbAjout++;
                passe=true;
            }

            fini=false;
            int i=0;
            if(!passe)
            {


            while (!fini) {
                if (i == nom.length()) {
                    retour = PumlDiagram.subStr(nom);
                    listePara.add(retour);
                    nbAjout++;
                    fini = true;
                    System.out.println(retour);
                } else if (nom.charAt(i) == ',') {
                    retour = nom.substring(0, i);
                    retour = PumlDiagram.subStr(retour);
                    nom = nom.substring(i + 1, nom.length());
                    listePara.add(retour);
                    nbAjout++;
                    fini = true;
                    System.out.println(retour);
                }

                i++;
            }
            }
        }

        System.out.println(listePara);
        return listePara;
    }
}
