package pumlFromJava.classes;

import pumlFromJava.PumlDiagram;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;

public class Attributs
{
    private final Element element;

    public Attributs(Element element)
    {
        //Attention, ici l'élément n'est pas l'élément global (ce n'est pas le package, ni les classes mais les éléments des classes (donc attributs, méthodes, ...))
        this.element=element;
    }

    public ArrayList<String> obtenirLesAttributs()
    {
        ArrayList<String>attributs=new ArrayList<>();

        //TypeMirror est une interface qui : représente un type en Java.
        //Cela inclut les types primitifs, les types déclarés (classe et interface), tableaux, variables et le type null.
        //Cela inclut aussi les Wildcard arguments, la signature et type de retour des méthodes et les pseudos correspondances aux package, modules et void.
        TypeMirror typeMirror = element.asType();

        //TypeKind est une énumération d'un type de TypeMirror.
        TypeKind typeKind = typeMirror.getKind();

        //Dans l'interface TypeKind, il y a une méthode isPrimitive() qui permet de savoir si le type est primitif
        //Je teste aussi pour savoir si cet élément est une constante dans l'énumération
        //Comme String est une classe, je vérifie aussi qu'on prenne juste cette classe qu'on met aussi en attribut dans les diagrammes
        if (typeKind.isPrimitive()||element.getKind() == ElementKind.ENUM_CONSTANT||(PumlDiagram.subStr(element.asType().toString()).equals("String")&&element.getKind()!=ElementKind.METHOD))
        {
            //Si c'est une constante d'énumération (je ne prends pas les modificateurs)
            if(element.getKind() != ElementKind.ENUM_CONSTANT)
            {
                //Pour récupérer les modificateurs de l'élément
                Modificateurs recupModificateur = new Modificateurs(element);
                attributs.add(recupModificateur.obtenirLesModificateurs());
            }

            //Petit cas spécial où on met dans les attributs les String
            if(PumlDiagram.subStr(element.asType().toString()).equals("String"))
            {
                attributs.add(element.getSimpleName().toString()+":"+PumlDiagram.subStr(element.asType().toString()));
            }
            else
            {
                //Puis j'écris le nom de l'élément
                attributs.add(element.getSimpleName().toString());

                //Si ce n'est pas une constante d'une énumération
                if (element.getKind() != ElementKind.ENUM_CONSTANT)
                {
                    //Je récupère le type de l'élément en string
                    String type = element.asType().toString();

                    //Je regarde si c'est un int, alors j'écris Integer en UML
                    if (element.asType().getKind() == TypeKind.INT)
                        type = "Integer";

                    attributs.add(":" + type + " ");
                }
            }
            attributs.add("\n");
       }
        return attributs;
    }
}
