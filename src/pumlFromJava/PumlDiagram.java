package pumlFromJava;

import com.sun.source.tree.Tree;
import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Reporter;

import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.Types;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
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

        //La tableau d'arguement
        String []argument=new String[] {"-private","-sourcepath", "src", "-doclet",
                "pumlFromJava.PumlDiagram", "-docletpath", "out/production/p-21-projet-renaud-matteo-gillig-matteo-tp-4", "western"};
        //Jsp pk mais l'option -d donne l'erreur : javadoc: error - invalid flag: -d
        //Donc je choisis moi meme le chemin dans la methode de creation
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
            myWriter.write("@startuml\n");
            myWriter.write("skinparam classAttributeIconSize 0\n" +
                                "skinparam classFontStyle Bold\n" +
                                "skinparam style strictuml\n" +
                                "hide empty members\n\n");

            //Nom du package
            myWriter.write("package "+element.getSimpleName().toString()+"\n{\n");

            ArrayList<String> interfaces=new ArrayList<>();
            ArrayList<String> heritages=new ArrayList<>();
            ArrayList<String> associations=new ArrayList<>();

            //Chaque élément présent dans le package
            for(Element e:element.getEnclosedElements())
            {
                //Le type et le nom
                myWriter.write(e.getKind()+" "+e.getSimpleName());

                //Si c'est une énumeration ou une interface
                if(e.getKind()== ElementKind.INTERFACE)
                {
                    //J'ajoute le stéréotype <<interface>>
                    myWriter.write("<<interface>>\n{\n");
                }
                else if (e.getKind()==ElementKind.ENUM)
                {
                    //Ou <<énumération>>
                    myWriter.write("<<énumération>>\n{\n");
                }
                else
                {
                    myWriter.write("\n{\n");
                }

                //Pour chaque élements dans cet élement (donc chaque éléments dans la classe/énumération ou interface)
                for(Element el:e.getEnclosedElements())
                {
                    //TypeMirror est une interface qui : représente un type en Java.
                    //Les Types inclus les types primitifs, les types déclarer (classe et interface), tableau, variables et le type null.
                    //Cela inclus aussi les Wildcard arguments, la signature et type de retour des méthodes et les pseudos correspondance aux package, modules et void.
                    TypeMirror typeMirror = el.asType();

                    //TypeKind est une énumération d'un type de TypeMirror.
                    TypeKind typeKind = typeMirror.getKind();

                    //Dans l'interface TypeKind, il y a une méthode isPrimitive() qui permet de savoir si le type est primitif
                    //Je teste aussi pour savoir si cet élément est une constante dans l'énumération
                    if (typeKind.isPrimitive()||el.getKind() == ElementKind.ENUM_CONSTANT||(subStr(el.asType().toString()).equals("String")&&el.getKind()!=ElementKind.METHOD))
                    {
                        //Modifier est une énumération qui énumère les modificateur d'un élément (ex : private, public, protected, ...)
                        //J'utilise la méthode getModifiers() de l'interface Element qui renvoie une liste des modificateurs de cet élément
                        for (Modifier mo : el.getModifiers())
                        {
                            //En fonction du modificateur, j'écris +, - ou #
                            if (mo == Modifier.PUBLIC)
                                myWriter.write("+ ");
                            else if (mo == Modifier.PRIVATE)
                                myWriter.write("- ");
                            else if (mo == Modifier.PROTECTED)
                                myWriter.write("# ");

                            //Je peux aussi savoir si c'est static ou final
                            if (mo == Modifier.STATIC)
                                myWriter.write("{static} ");
                            if (mo == Modifier.FINAL)
                                myWriter.write("{ReadOnly} ");
                        }

                        if(subStr(el.asType().toString()).equals("String"))
                        {
                            myWriter.write(el.getSimpleName().toString()+":"+subStr(el.asType().toString()));
                        }
                        else
                        {

                        //Puis j'écris le nom de l'élément
                        myWriter.write(el.getSimpleName().toString());

                        if(el.getKind() != ElementKind.ENUM_CONSTANT) {
                            //Je récupère le type de l'élément en string
                            String type = el.asType().toString();

                            //Je regarde si c'est un int, alors j'écris Integer en UML
                            if (el.asType().getKind() == TypeKind.INT)
                                type = "Integer";


                            myWriter.write(":" + type + " ");
                        }}
                        myWriter.write("\n");
                    }

                    //----Partie Association-----


                    TypeElement typeElement=(TypeElement) el.getEnclosingElement();

                    for(TypeMirror tm:typeElement.getInterfaces())
                    {

                        if(e.getKind().isInterface())
                        {
                            if (!interfaces.contains(subStr(tm.toString()) + " <|-- " + e.getSimpleName()))
                                interfaces.add((subStr(tm.toString()) + " <|-- " + e.getSimpleName()));
                        }
                        else
                        {

                            if (!interfaces.contains(subStr(tm.toString()) + " <|... " + e.getSimpleName()))
                                interfaces.add((subStr(tm.toString()) + " <|... " + e.getSimpleName()));
                        }
                    }

                    TypeMirror heritageSilYA=typeElement.getSuperclass();

                    if(heritageSilYA.toString()!="none"&&e.getKind()!= ElementKind.ENUM)
                    {
                        if(!heritageSilYA.toString().equals("java.lang.Object"))
                        {


                            if (!heritages.contains(subStr(heritageSilYA.toString()) + " <|--- " + subStr(e.getSimpleName().toString())))
                                heritages.add(subStr(heritageSilYA.toString()) + " <|--- " + subStr(e.getSimpleName().toString()));
                        }
                    }

                    //System.out.println(subStr("western.Cowboy"));
                    //System.out.println(heritageSilYA.toString());

                    //----Partie use-----



                        if (!typeKind.isPrimitive()&&el.getKind()!=ElementKind.METHOD)
                        {
                            if(el.getKind()!=ElementKind.CONSTRUCTOR&&el.getKind()!=ElementKind.ENUM_CONSTANT)
                            {
                                if(!subStr(el.asType().toString()).equals("String"))
                                {

                                    if (!associations.contains(e.getSimpleName() + " --- " + subStr(el.asType().toString()) + " : " + subStr(el.getSimpleName().toString())))

                                    associations.add(e.getSimpleName() + " --- " + subStr(el.asType().toString()) + " : " + subStr(el.getSimpleName().toString()));
                                }
                            }
                        }





                    //----Partie suivante----
                    //Le constructeur
                    //Si c'est un constructeur et que je ne suis pas dans une énumération
                    /*
                    if (el.getKind() == ElementKind.CONSTRUCTOR&&e.getKind()!=ElementKind.ENUM) {
                        //Pour chaque modificateur
                        for (Modifier mo : el.getModifiers()) {
                            if (mo == Modifier.PUBLIC)
                                myWriter.write("+ ");
                            else if (mo == Modifier.PRIVATE)
                                myWriter.write("- ");
                        }
                        //Il y a juste que el.toString() écrit le nom entier avec le package
                        myWriter.write(" <<create>> "+subStr(e.getSimpleName().toString()));

                        //TypeElement ede=(TypeElement) e.getEnclosingElement();

                        //parametre
                        //mais il n'y a pas encore le nom
                        myWriter.write(el.asType().toString()+"\n");
                    }*/
                    //---------------------------
                }

                //Fin de la classe/interface/énumération
                myWriter.write("\n}\n");

            }



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

    public static String subStr(String nom)
    {
        String retour="";
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
            retour=nom.substring(position+1,nom.length());


        if(retour.charAt(retour.length()-1) == '>')
        {
            retour = retour.substring(0, retour.length()-1);
        }

        if(retour.charAt(retour.length()-1)=='>')
            retour=retour.substring(0,retour.length()-1);


        return retour;
    }


}
