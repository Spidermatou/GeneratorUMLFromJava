package pumlFromJava;

import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Reporter;

import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
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

    //Je prévoie un constructeur public et sans arguments
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
    public String getName() {
        return getClass().getSimpleName();
    }


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }
    @Override
    public boolean run(DocletEnvironment environment) {
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

        creation(element);
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


        //Jsp, je teste
        PumlDiagram pumlDiagram=new PumlDiagram();

        Set<? extends Option> options=pumlDiagram.getSupportedOptions();
        for(Option o:options)
        {
            List<String>liste=new ArrayList<String>();
            liste=o.getNames();

            if(liste.get(0) =="-d")
            {
                tiretD=liste.get(0);
                chemin=o.getParameters();
            }

            if(liste.get(0)=="-out")
            {
                out=liste.get(0);
                extension=o.getParameters();
            }
        }

        //La tableau d'arguement
        argument=new String[] {"-private","-sourcepath", "src", "-doclet",
                "pumlFromJava.PumlDiagram", "-docletpath", "out/production/p-21-projet-renaud-matteo-gillig-matteo-tp-4", "western"
                ,tiretD,chemin,out ,extension};

        toolProvider.run(System.out, System.err, argument);
    }
    //--------------------------
    //Ce sont des essais
    public static String[]argument;
    static String out="";
    static String tiretD="";
    static String chemin="";
    static String extension="";
    public String getOutParametre()
    {
        return this.extension;
    }
    public String getTiretDParametre()
    {
        return this.chemin;
    }

    //---Pour les options----
    @Override
    public Set<? extends Option> getSupportedOptions()
    {

        Set <Doclet.Option>options=new HashSet<Doclet.Option>();

        //L'option -d
        Doclet.Option directory=new Option()
        {
            private String nomOption = "-d";
            private String description = "Le chemin vers le fichier.";
            private String parametre="./Diagramme";

            @Override
            public int getArgumentCount() {
                return 1;
            }

            @Override
            public String getDescription() {
                return this.description;
            }

            @Override
            public Kind getKind() {
                return Kind.STANDARD;
            }

            @Override
            public List<String> getNames() {
                ArrayList<String> liste = new ArrayList<>();
            liste.add(nomOption);

                return liste;
            }

            @Override
            public String getParameters() {
                return this.parametre;
            }

            @Override
            public boolean process(String s, List<String> list) {
                return true;
            }
        };
        //L'option -out
        Doclet.Option out=new Option() {
            private String nomOption = "-out";
            private String description = "L'extension du fichier.";
            private String parametree=".puml";
            @Override
            public int getArgumentCount() {
                return 1;
            }

            @Override
            public String getDescription() {
                return this.description;
            }

            @Override
            public Kind getKind() {
                return Kind.STANDARD;
            }

            @Override
            public List<String> getNames() {
                ArrayList<String> liste = new ArrayList<>();
                liste.add("-out");

                return liste;
            }

            @Override
            public String getParameters() {
                return this.parametree;
            }

            @Override
            public boolean process(String s, List<String> list) {
                return true;
            }
        };
        options.add(directory);
        options.add(out);

        // This doclet does not support any options.
        return options;
    }

    public void creation(Element element)
    {
        String cheminVers=getTiretDParametre();
        System.out.println(cheminVers);
        cheminVers+=getOutParametre();
        System.out.println(cheminVers);

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
            myWriter.write("@startuml\n");
            myWriter.write("skinparam classAttributeIconSize 0\n" +
                                "skinparam classFontStyle Bold\n" +
                                "skinparam style strictuml\n" +
                                "hide empty members\n\n");

            //Nom du package
            myWriter.write("package "+element.getSimpleName().toString()+"\n{\n");
            myWriter.write("'jsp si c'est comme ca qu'on fait mais ca fait un fichier stylé\n");

            //Chaque élément
            for(Element e:element.getEnclosedElements())
            {
                //Le type et le nom
                myWriter.write(e.getKind()+" "+e.toString()+"\n{\n}\n");
            }
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
}
