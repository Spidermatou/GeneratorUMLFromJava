package pumlFromJava;

import java.util.spi.ToolProvider;

public class Java2Puml
{
    public static void main(String[] args)
    {
        ToolProvider toolProvider = ToolProvider.findFirst("javadoc").get();
        System.out.println(toolProvider.name());

        /*
        javadoc -private -sourcepath <src> -doclet pumlFromJava.FirstDoclet -docletpath out/production/<projet>
          <package> ... <fichiers>
        */

        /*
        Commande corrigé:
        javadoc -private -d test -sourcepath src -doclet pumlFromJava.FirstDoclet -docletpath out/production/ProjetP21 pumlFromJava
        */

        /*
        Commande pour western:
        javadoc -private -sourcepath /home/mat/P21Projet -doclet pumlFromJava.FirstDoclet -docletpath /home/mat/P21Projet/out/production/P21Projet western
         */

        //javadoc -private -sourcepath src -doclet pumlFromJava.FirstDoclet -docletpath out/production/p-21-projet-renaud-matteo-gillig-matteo-tp-4 western
        //ou avec un chemin absolu pour ne pas etre obliger d'etre dans le dossier
        //javadoc -private -sourcepath /home/matteo.renaud/Documents/Université/1ère_Année/SAE/SAE_P21/P21_Projet/p-21-projet-renaud-matteo-gillig-matteo-tp-4/src -doclet pumlFromJava.FirstDoclet -docletpath /home/matteo.renaud/Documents/Université/1ère_Année/SAE/SAE_P21/P21_Projet/p-21-projet-renaud-matteo-gillig-matteo-tp-4/out/production/p-21-projet-renaud-matteo-gillig-matteo-tp-4 western

        //Un tableau créer avec les arguments
        String[] argument = {"-private", "-sourcepath", "src", "-doclet", "pumlFromJava.FirstDoclet", "-docletpath", "out/production/p-21-projet-renaud-matteo-gillig-matteo-tp-4", "western"};
        toolProvider.run(System.out, System.err, argument);
    }
}
