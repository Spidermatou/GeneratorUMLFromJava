package pumlFromJava;


import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Reporter;
import pumlFromJava.classes.Attributs;
import pumlFromJava.classes.Constructeurs;
import pumlFromJava.classes.Methodes;
import pumlFromJava.liens.Associations;
import pumlFromJava.liens.Heritages;
import pumlFromJava.liens.Interfaces;
import pumlFromJava.typeDiagramme.DCA;
import pumlFromJava.typeDiagramme.DCC;

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
        veutDCC=true;
        veutDCA=false;
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

    /*
    @Override
    public Set<? extends Option> getSupportedOptions() {
        // This doclet does not support any options.
        return Collections.emptySet();
    }*/

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
        String []argument2=new String[] {"-private","-sourcepath", "src", "-doclet",
                "pumlFromJava.PumlDiagram", "-docletpath", "out/production/p-21-projet-renaud-matteo-gillig-matteo-tp-4", "western","--dca"};


        //veutDCA=voirSiVeutDCA(args);

        //javadoc -private -sourcepath src -doclet pumlFromJava.PumlDiagram -docletpath out/production/P21Projet western --dca

        //+ option -d
        //Donc je choisis moi-même le chemin dans la méthode de création
        toolProvider.run(System.out, System.err, args);

    }

    private static boolean veutDCC;
    private static boolean veutDCA;

    public void creation(Element element)
    {
        if(veutDCC)
        {
            //Je choisis moi-même le chemin vers le fichier
            String cheminVersDCC = "DCCGenere.puml";

            //Créer ficher
            try
            {
                File fichier = new File(cheminVersDCC);
                if (fichier.createNewFile())
                {
                    System.out.println("Fichier créer : " + fichier.getName());
                }
                else
                {
                    System.out.println("Le fichier existe dèjà.");
                }
            }
            catch (IOException e) {
                System.out.println("Erreur de création.");
                e.printStackTrace();
            }
            //Ecriture
            try
            {
                FileWriter myWriter = new FileWriter(cheminVersDCC);

                DCC dcc = new DCC(element, myWriter);
                dcc.creerDCC();

                myWriter.close();

                System.out.println("Fin de l'écriture.");
            }
            catch (IOException e) {
                System.out.println("Erreur d'écriture.");
                e.printStackTrace();
            }
        }

        if(veutDCA)
        {
            //Je choisis moi-même le chemin vers le fichier
            String cheminVersDCA = "DCAGenere.puml";
            //Créer ficher
            try
            {
                File fichier = new File(cheminVersDCA);
                if (fichier.createNewFile())
                {
                    System.out.println("Fichier créer : " + fichier.getName());
                }
                else
                {
                    System.out.println("Le fichier existe dèjà.");
                }
            }
            catch (IOException e) {
                System.out.println("Erreur de création.");
                e.printStackTrace();
            }

            //Ecriture
            try
            {
                FileWriter myWriter = new FileWriter(cheminVersDCA);

                DCA dca = new DCA(element, myWriter);
                dca.creerDCA();

                myWriter.close();

                System.out.println("Fin de l'écriture.");
            }
            catch (IOException e)
            {
                System.out.println("Erreur d'écriture.");
                e.printStackTrace();
            }
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

    public static boolean voirSiVeutDCA(List<String> argument)
    {
        for(int i=0;i<argument.size();i++)
        {
            System.out.println(argument.get(i));

            if(argument.get(i).toLowerCase().contains("--dca"))
            {
                return true;
            }
        }
        return false;
    }

    //---Pour les options---

    @Override
    public Set<? extends Option> getSupportedOptions() {
        Option[] options = {
                new Option() {
                    private final List<String> someOption = Arrays.asList(
                            "--dca"
                    );

                    @Override
                    public int getArgumentCount() {
                        return 0;
                    }

                    @Override
                    public String getDescription() {
                        return "Pour le DCA";
                    }

                    @Override
                    public Option.Kind getKind() {
                        return Option.Kind.STANDARD;
                    }

                    @Override
                    public List<String> getNames() {
                        return someOption;
                    }

                    @Override
                    public String getParameters() {
                        return "--dca";
                    }

                    @Override
                    public boolean process(String opt, List<String> arguments)
                    {
                        veutDCA=true;
                        return true;
                    }
                }
        };
        return new HashSet<>(Arrays.asList(options));
    }
}
