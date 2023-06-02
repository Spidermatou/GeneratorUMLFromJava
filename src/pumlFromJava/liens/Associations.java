package pumlFromJava.liens;

import pumlFromJava.PumlDiagram;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;

public class Associations
{
    private Element element;
     public Associations(Element element)
     {
         //Ici, on a l'élément global (le package)
         this.element=element;
     }

     public ArrayList<String> obtenirLesAssociations()
     {
         ArrayList<String> associations = new ArrayList<String>();

         for (Element e : element.getEnclosedElements())
         {
             for (Element el : e.getEnclosedElements())
             {
                 //TypeMirror est une interface qui : représente un type en Java.
                 //Cela inclut les types primitifs, les types déclarés (classe et interface), tableaux, variables et le type null.
                 //Cela inclut aussi les Wildcard arguments, la signature et type de retour des méthodes et les pseudos correspondances aux packages, modules et void.
                 TypeMirror typeMirror = el.asType();

                 //TypeKind est une énumération d'un type de TypeMirror.
                 TypeKind typeKind = typeMirror.getKind();

                 //Si ce n'est pas un type primitif ou une méthode
                 if (!typeKind.isPrimitive() && el.getKind() != ElementKind.METHOD)
                 {
                     //Que ce n'est pas non plus un constructeur ou une constante d'énumération
                     if (el.getKind() != ElementKind.CONSTRUCTOR && el.getKind() != ElementKind.ENUM_CONSTANT)
                     {
                         //Et que ce n'est pas la classe String
                         if (!PumlDiagram.subStr(el.asType().toString()).equals("String"))
                         {
                             /*
                             //+multiplicité
                             //if(el.asType().getKind()==TypeKind.ARRAY)
                             String cardi="";
                             if(!el.asType().getKind().isPrimitive())
                             {
                                 cardi="\"*\"";
                             }*/

                             //Et enfin que cette association n'est pas déjà présente dans la liste
                             if (!associations.contains(e.getSimpleName() + " --- " + PumlDiagram.subStr(el.asType().toString()) + " : " + PumlDiagram.subStr(el.getSimpleName().toString())))
                                 associations.add(e.getSimpleName() +" --- " + PumlDiagram.subStr(el.asType().toString()) + " : " + PumlDiagram.subStr(el.getSimpleName().toString()));
                         }
                     }
                 }
             }


         }
         return associations;
     }
}
