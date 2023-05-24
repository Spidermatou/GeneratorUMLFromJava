package pumlFromJava;


import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Reporter;
import pumlFromJava.classes.Attributs;
import pumlFromJava.classes.Constructeurs;
import pumlFromJava.liens.Associations;
import pumlFromJava.liens.Heritages;
import pumlFromJava.liens.Interfaces;

import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.*;
import java.util.spi.ToolProvider;

//La classe implémente bien le Doclet pour avoir les méthodes et le fonctionnement
public class PumlDiagram implements Doclet
{

    //Je mets un reporter et un locale
    private Reporter reporter;
    private Locale locale;

    //Je prévois un constructeur public et sans arguments
    public PumlDiagram()
    {
        //Vide
    }

    @Override
    public void init(Locale locale, Reporter reporter)
    {
        //Je met le Reporter et le Locale
        this.reporter=reporter;
        this.locale=locale;
    }

    @Override
    public String getName()
    {
        return getClass().getSimpleName();
    }


    @Override
    public SourceVersion getSupportedSourceVersion()
    {
        return SourceVersion.latest();
    }
    @Override
    public boolean run(DocletEnvironment environment)
    {
        System.out.println(this.getName());
        System.out.println(environment.getSpecifiedElements());
        System.out.println(environment.getIncludedElements());

        for (Element element : environment.getSpecifiedElements())
        {
            dumpElement(element);
        }
        return true;
    }

    private void dumpElement(Element element)
    {
        System.out.print("---- ");
        System.out.println("element: " + element);
        System.out.println("kind: " + element.getKind());
        System.out.println("simpleName: " + element.getSimpleName());
        System.out.println("enclosingElement: " + element.getEnclosingElement());
        System.out.println("enclosedElement: " + element.getEnclosedElements());
        System.out.println("modifiers: " + element.getModifiers());
        System.out.println();

        //La méthode qui va créer le diagramme
        creation(element);
    }
    @Override
    public Set<? extends Option> getSupportedOptions() {
        // This doclet does not support any options.
        return Collections.emptySet();
    }

    /*
    Reporters
    The preceding examples just write directly to System.out.
    Using a Reporter, you can generate messages that are associated with an element or a position in a documentation comment.
    The javadoc tool will try to identify the appropriate source file and line within the source file and will include that in the message displayed to the user.
    The following example uses the reporter to report the kind of the elements specified on the command line.
     */

    public static void main(String[] args)
    {
        ToolProvider toolProvider = ToolProvider.findFirst("javadoc").get();
        System.out.println(toolProvider.name());

        //Le tableau d'arguments
        String []argument=new String[] {"-private","-sourcepath", "src", "-doclet",
                "pumlFromJava.PumlDiagram", "-docletpath", "out/production/p-21-projet-renaud-matteo-gillig-matteo-tp-4", "western"};
        //Jsp pk mais l'option -d donne l'erreur : javadoc: error - invalid flag: -d
        //Donc je choisis moi-même le chemin dans la méthode de création
        toolProvider.run(System.out, System.err, argument);

    }

    public void creation(Element element)
    {
        //Je choisis moi-même le chemin vers le fichier
        String cheminVers="DiagrammeGenere.puml";

        //Créer ficher
        try
        {
            File fichier = new File(cheminVers);
            if (fichier.createNewFile())
            {
                System.out.println("Fichier créer : " + fichier.getName());
            }
            else
            {
                System.out.println("Le fichier existe dèjà.");
            }
        }
        catch (IOException e)
        {
            System.out.println("Erreur de création.");
            e.printStackTrace();
        }

        //Ecriture
        try
        {
            FileWriter myWriter = new FileWriter(cheminVers);

            //L'en-tête du fichier

            //Par contre, comme on écrit directement le nom du package, pour l'instant notre Doclet ne marche que si on lui fournit un et un seul package.
            //Nom du package

            //Pour récupérer les liens pour les héritages, associations et interfaces
            Heritages recupHeritage=new Heritages(element);
            Associations recupAssociations=new Associations(element);
            Interfaces recupInterfaces=new Interfaces(element);
            ArrayList<String> heritages=recupHeritage.obtenirLesHeritages();
            ArrayList<String> associations=recupAssociations.obtenirLesAssociations();
            ArrayList<String> interfaces=recupInterfaces.obtenirLesImplements();

            //Methode Ecrire pour écrire le code du diagramme dans le fichier uml
            Ecrire(element, myWriter);

            //Ecriture des liens pour les interfaces, héritages et associations
            for(String s:interfaces)
                myWriter.write("\n"+s+"\n");

            for(String s:heritages)
                myWriter.write("\n"+s+"\n");

            for(String s:associations)
                myWriter.write("\n"+s+"\n");

            //Fin du fichier
            myWriter.write("}\n@enduml");
            myWriter.close();

            System.out.println("Fin de l'écriture.");
        }
        catch (IOException e)
        {
            System.out.println("Erreur d'écriture.");
            e.printStackTrace();
        }
    }

    //Méthode maison pour enlever le nom des package avant
    //Car quand on écrit le nom (avec la méthode getSimpleName()), on obtient par exemple java.western.Boisson
    //Donc ce qu'on va faire, c'est que l'on va juste garder la dernière partie (après le dernier point)
    public static String subStr(String nom)
    {
        String retour;
        int position=0;
        for(int i=0;i<nom.length();i++)
        {
            if(nom.charAt(i)=='.')
            {
                position=i;
            }
        }

        if(position==0)
            retour=nom;
        else
            retour=nom.substring(position+1);

        //Dans certains cas, les noms sont entre < > (dans des listes, init, etc ...), et comme on garde tout après le dernier point, ce caractère peut rester
        //On va alors simplement l'enlever s'il y a ce caractère
        if(retour.charAt(retour.length()-1)=='>')
            retour=retour.substring(0,retour.length()-1);

        return retour;
    }

    public void Ecrire(Element element, FileWriter myWriter)
    {
        try
        {
            myWriter.write("@startuml\n");
            myWriter.write("""
                    skinparam classAttributeIconSize 0
                    skinparam classFontStyle Bold
                    skinparam style strictuml
                    hide empty members

                    """);

            //Par contre, comme on écrit directement le nom du package, pour l'instant notre Doclet ne marche que si on lui fournit un et un seul package.
            //Nom du package
            myWriter.write("package "+element.getSimpleName().toString()+"\n{\n");



            //Chaque élément présent dans le package
            for(Element e:element.getEnclosedElements()) {
                //Le type et le nom
                myWriter.write(e.getKind() + " " + e.getSimpleName());

                //Si c'est une énumération ou une interface
                if (e.getKind() == ElementKind.INTERFACE) {
                    //J'ajoute le stéréotype <<interface>>
                    myWriter.write("<<interface>>\n{\n");
                } else if (e.getKind() == ElementKind.ENUM) {
                    //Ou <<énumération>>
                    myWriter.write("<<énumération>>\n{\n");
                } else {
                    myWriter.write("\n{\n");
                }

                //Pour chaque élément dans cet élément (donc chaque élément dans la classe/énumération ou interface)
                for (Element el : e.getEnclosedElements()) {
                    //Je récupère les attributs
                    Attributs recupAttribut = new Attributs(el);
                    ArrayList<String> attributs = recupAttribut.obtenirLesAttributs();

                    //J'écris
                    for (String s : attributs)
                        myWriter.write(s);

                    //----Partie suivante----
                    //Le constructeur
                    //On fait bien attention, si c'est un constructeur et qu'on n'est pas dans une énumération
                    if (el.getKind() == ElementKind.CONSTRUCTOR && e.getKind() != ElementKind.ENUM) {
                        //Alors, on peut récupérer notre constructeur
                        Constructeurs recupConstructeurs = new Constructeurs(e, el);
                        myWriter.write(recupConstructeurs.obtenirLeOuLesConstructeurs());
                    }
                }
                myWriter.write("\n}\n");
            }
        }
        catch (IOException e)
        {
            System.out.println("Erreur d'écriture.");
            e.printStackTrace();
        }
    }
}
