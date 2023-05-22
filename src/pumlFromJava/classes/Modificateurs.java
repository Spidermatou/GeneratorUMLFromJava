package pumlFromJava.classes;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

public class Modificateurs
{
    private final Element element;

    public Modificateurs(Element element)
    {
        //Attention, ici l'élément n'est pas l'élément global (ce n'est pas le package, ni les classes, mais les éléments des classes (donc attributs, méthodes, ...))
        //Comme dans la classe Attributs (même si on pourrait avoir des classes privées)
        this.element=element;
    }

    public String obtenirLesModificateurs()
    {
        String modificateurs="";

        //Modifier est une énumération qui énumère les modificateurs d'un élément (ex : private, public, protected, ...)
        //J'utilise la méthode getModifiers() de l'interface Element qui renvoie une liste des modificateurs de cet élément
        for (Modifier mo : element.getModifiers())
        {
            //En fonction du modificateur, j'écris +, - ou #
            if (mo == Modifier.PUBLIC)
                modificateurs+="+ ";
            else if (mo == Modifier.PRIVATE)
                modificateurs+="- ";
            else if (mo == Modifier.PROTECTED)
                modificateurs+="# ";

            //Je peux aussi savoir si c'est static ou final
            if (mo == Modifier.STATIC)
                modificateurs+="{static} ";
            if (mo == Modifier.FINAL)
                modificateurs+="{ReadOnly} ";
        }
        return modificateurs;
    }
}
