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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.spi.ToolProvider;

//La classe implémente bien le Doclet pour avoir les méthodes et le fonctionnement
public class PumlDiagram implements Doclet
{
    //3 bouléens pour savoir si l'utilisateur a mis l'extension --dca ou --d ou --out
    private boolean veutDCA;
    private boolean aMisExtensionTiretD;
    private boolean aChoisisUnNomPourLeFichier;
    //2 chaînes de caractères qui stockeront le paramêtre de l'extension --d ou --out (si elles sont dans la commande)
    private String cheminDeLExtensionTiretD;
    private String nomDuFichier;

    Reporter reporter;
    Locale locale;

    //Je prévois un constructeur public et sans arguments
    public PumlDiagram()
    {
        veutDCA=false;
        aMisExtensionTiretD=false;
        aChoisisUnNomPourLeFichier=false;
        cheminDeLExtensionTiretD="";
        nomDuFichier="";
    }

    //Le main
    public static void main(String[] args)
    {
        ToolProvider toolProvider = ToolProvider.findFirst("javadoc").get();
        System.out.println(toolProvider.name());

        //Les tableaux d'arguments (pour exécuter dans l'IDE)
        String []argument=new String[] {"-private","-sourcepath", "src", "-doclet",
                "pumlFromJava.PumlDiagram", "-docletpath", "out/production/p-21-projet-renaud-matteo-gillig-matteo-tp-4", "western"};
        String []argument2=new String[] {"-private","-sourcepath", "src", "-doclet",
                "pumlFromJava.PumlDiagram", "-docletpath", "out/production/p-21-projet-renaud-matteo-gillig-matteo-tp-4", "western","--dca"};

        //Les commandes (pour exécuter dans le terminal)
        //javadoc -private -sourcepath src -doclet pumlFromJava.PumlDiagram -docletpath out/production/P21Projet western --dca
        //javadoc -private -sourcepath src -doclet pumlFromJava.PumlDiagram -docletpath out/production/P21Projet western --dca --d repertoire_destination --out mes_diagrammes
        //javadoc -private -sourcepath src -doclet pumlFromJava.PumlDiagram -docletpath out/production/P21Projet package_test --dca

        toolProvider.run(System.out, System.err, args);
    }

    @Override
    public void init(Locale locale, Reporter reporter)
    {
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
    Reporters
    The preceding examples just write directly to System.out.
    Using a Reporter, you can generate messages that are associated with an element or a position in a documentation comment.
    The javadoc tool will try to identify the appropriate source file and line within the source file and will include that in the message displayed to the user.
    The following example uses the reporter to report the kind of the elements specified on the command line.
     */


    //La méthode de création
    public void creation(Element element)
    {
        //Les chemins vers les 2 fichiers
        String cheminVersDCA ;
        String cheminVersDCC ;

        //S'il a choisi l'option --d et --out
        if(aMisExtensionTiretD&&aChoisisUnNomPourLeFichier)
        {
            cheminVersDCC=cheminDeLExtensionTiretD+"/"+nomDuFichier+"_DCC.puml";
            cheminVersDCA=cheminDeLExtensionTiretD+"/"+nomDuFichier+"_DCA.puml";
        }
        //Sinon, s'il n'a que choisi l'option --d
        else if(aMisExtensionTiretD)
        {
            cheminVersDCC=cheminDeLExtensionTiretD+"/"+"DCCGenere.puml";
            cheminVersDCA=cheminDeLExtensionTiretD+"/"+"DCAGenere.puml";
        }
        //Sinon, s'il n'a que choisi l'option --out
        else if(aChoisisUnNomPourLeFichier)
        {
            cheminVersDCC=nomDuFichier+"_DCC.puml";
            cheminVersDCA=nomDuFichier+"_DCA.puml";
        }
        //Sinon, si aucune option
        else
        {
            cheminVersDCC="DCCGenere.puml";
            cheminVersDCA="DCAGenere.puml";
        }


        //S'il a mis l'option --d (je vais créer le répertoire)
        if(aMisExtensionTiretD)
        {
            try
            {
                Path dirPath = Paths.get(cheminDeLExtensionTiretD);

                //Si le répertoire n'existe pas
                if (!Files.exists(dirPath))
                {
                    //Je créé le répertoire
                    Files.createDirectory(dirPath);

                    //Je regarde si maintenant ce répertoire existe et affiche un message
                    if(Files.exists(dirPath))
                        System.out.println("Le dossier a été créer : "+dirPath.getFileName());
                }
                else
                {
                    System.out.println("Le dossier existe déjà.");
                }
            }
            catch (IOException e)
            {
                System.out.println("Erreur : Impossible de créer le dossier.");
                e.printStackTrace();
            }
        }

        //---------------------Création du DCC et DCA---------------------

        //Le DCC
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
        catch (IOException e)
        {
            System.out.println("Erreur de création.");
            e.printStackTrace();
        }

        //Ecriture
        try
        {
            FileWriter myWriter = new FileWriter(cheminVersDCC);

            //Je crée un objet DCC en lui donnant en paramêtre le package et l'objet d'écriture
            DCC dcc = new DCC(element, myWriter);
            dcc.creerDCC();

            myWriter.close();
            System.out.println("Fin de l'écriture.");
        }
        catch (IOException e)
        {
            System.out.println("Erreur d'écriture.");
            e.printStackTrace();
        }


        //Le DCA
        //S'il veut le DCA (avec l'option --dca)
        if(veutDCA)
        {
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
            catch (IOException e)
            {
                System.out.println("Erreur de création.");
                e.printStackTrace();
            }

            //Ecriture
            try
            {
                FileWriter myWriter = new FileWriter(cheminVersDCA);

                //Comme pour le DCC , je crée un objet DCA en lui donnant en paramêtre le package et l'objet d'écriture
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
    //Car quand on écrit le nom (avec exemple, la méthode getSimpleName()), on obtient par exemple java.western.Boisson
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
        if(retour.substring(retour.length()-1).equals(">"))
            retour=retour.substring(0,retour.length()-1);

        return retour;
    }

    //Une autre méthode maison qui s'assure qu'il n'y a pas de < > [ ] à la fin d'une chaîne de caractères
    public static String subStrLiens(String nom)
    {
        String retour;

        if(!nom.equals(""))
        {
            //Comme la méthode subStr
            int position = 0;
            for (int i = 0; i < nom.length(); i++)
            {
                if (nom.charAt(i) == '.')
                {
                    position = i;
                }
            }

            if (position == 0)
                retour = nom;
            else
                retour = nom.substring(position + 1);

            //On va ici enlever tous les caractères indésirables à la fin
            boolean erreur_out_of_range = false;
            while (!erreur_out_of_range)
            {
                try
                {
                    if (retour.charAt(retour.length()-1) == '>'|| retour.charAt(retour.length()-1) == '<' || retour.charAt(retour.length()-1) == ']' || retour.charAt(retour.length()-1)=='[')
                    {
                        retour = retour.substring(0, retour.length() - 1);
                    }
                    else
                    {
                        erreur_out_of_range=true;
                    }
                }
                catch (IndexOutOfBoundsException e)
                {
                     erreur_out_of_range = true;
                }
             }
        }
        else
            retour="";

        return retour;
    }

    //La méthode pour les types de paramêtres et de retour qui va faire :
    // - si on a une ou 0 liste (on va enlever le nom des packages avant et rajouter [*])
    // - sinon (si on a plus de 1 liste), on va tout garder (car on ne sait pas comment écrire en UML quand on a plusieurs listes)
    public static String subStrParametres(String nom)
    {
        String retour="";

        int nbFleche=0;

        for(int i=0;i<nom.length();i++)
        {
            if(nom.charAt(i)=='>')
                nbFleche++;
        }

        //Si on a moins que 2 listes
        if(nbFleche<2)
        {
            int position = 0;
            for (int i = 0; i < nom.length(); i++) {
                if (nom.charAt(i) == '.') {
                    position = i;
                }
            }

            if (position == 0)
                retour = nom;
            else
                retour = nom.substring(position + 1);

            //Dans certains cas, les noms se terminent par < > [ ] (dans des listes, init, etc ...), et comme on garde tout après le dernier point, ce caractère peut rester
            //On va alors simplement l'enlever s'il y a ce caractère
            if(retour.substring(retour.length()-1).equals(">"))
                retour=retour.substring(0,retour.length()-1);
        }
        //Sinon, on garde tous et on enlève juste le nom de certains packages
        else
        {
            //On va enlever les java.util avant
            retour=nom;
            //Technique pour enlever le nom de certains packages
            //(Pour que ce soit juste plus lisible)
            //C'est très subjectif (car je choisis les packages qu'on a le plus)
            retour=retour.replace("java.","");
            retour=retour.replace("util.","");
            retour=retour.replace("Map.","");
            retour=retour.replace("lang.","");
        }

        return retour;
    }


    //------Pour les options-----
    //On a rajouté 3 options en plus pour la commande :
    // --d : le répetoire où il veut mettre les diagrammes
    // --out : le nom des diagrammes
    // --dca : s'il veut le DCA avec

    @Override
    public Set<? extends Option> getSupportedOptions()
    {
        Option[] options = {
               //1ère option --d
                new Option() {
                    private final List<String> someOption = Arrays.asList(
                            "--d"
                    );

                    @Override
                    public int getArgumentCount() {
                        return 1;
                    }

                    @Override
                    public String getDescription() {
                        return "Chemin vers le fichier pour le DCC";
                    }

                    @Override
                    public Option.Kind getKind() {
                        return Kind.STANDARD;
                    }

                    @Override
                    public List<String> getNames() {
                        return someOption;
                    }

                    @Override
                    public String getParameters() {
                        return "--d";
                    }

                    @Override
                    public boolean process(String opt, List<String> arguments)
                    {
                        aMisExtensionTiretD=true;
                        cheminDeLExtensionTiretD=arguments.get(0);
                        return true;
                    }
                },
                //2ème option --out
                new Option() {
                    private final List<String> someOption = Arrays.asList(
                            "--out"
                    );

                    @Override
                    public int getArgumentCount() {
                        return 1;
                    }

                    @Override
                    public String getDescription() {
                        return "Mise en place du nom du fichier pour le DCC (Pas besoin de spécifier l'extension .puml)";
                    }

                    @Override
                    public Option.Kind getKind() {
                        return Kind.STANDARD;
                    }

                    @Override
                    public List<String> getNames() {
                        return someOption;
                    }

                    @Override
                    public String getParameters() {
                        return "--out";
                    }

                    @Override
                    public boolean process(String opt, List<String> arguments)
                    {
                        if(!arguments.get(0).equals(""))
                        {
                            aChoisisUnNomPourLeFichier = true;
                            nomDuFichier = arguments.get(0);

                            if(nomDuFichier.substring(nomDuFichier.length()-5).equals(".puml"))
                            {
                                nomDuFichier=nomDuFichier.substring(0,nomDuFichier.length()-5);
                            }

                            return true;
                        }
                        return false;
                    }
                },
                //3ème option --dca
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
