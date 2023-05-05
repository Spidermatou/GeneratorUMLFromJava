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
    commande pour western:
    javadoc -private -sourcepath /home/mat/P21Projet -doclet pumlFromJava.FirstDoclet -docletpath /home/mat/P21Projet/out/production/P21Projet western

 */
        toolProvider.run(System.out, System.err, args);
    }
}
