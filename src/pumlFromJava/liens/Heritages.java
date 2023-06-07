package pumlFromJava.liens;

import pumlFromJava.PumlDiagram;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;

public class Heritages
{
    private Element element;

    public Heritages(Element element)
    {
        //L'élément global (le package)
        this.element=element;
    }

    public ArrayList<String> obtenirLesHeritages()
    {
        ArrayList<String> heritages=new ArrayList<>();

        for(Element e:element.getEnclosedElements())
        {
            for (Element el : e.getEnclosedElements())
            {
                TypeElement typeElement = (TypeElement) el.getEnclosingElement();

                //getSuperClass() est une méthode qui renvoie la super-classe de cet élément
                //Si cette classe n'a pas de classe mère, le type NoType avec NONE est renvoyé
                TypeMirror heritageSilYA = typeElement.getSuperclass();

                //Donc s'il y a un héritage et que ce n'est pas une énumération
                if (!heritageSilYA.toString().equals("none") && e.getKind() != ElementKind.ENUM)
                {
                    //Et que l'héritage n'est pas la classe Object
                    if (!heritageSilYA.toString().equals("java.lang.Object"))
                    {
                        //Que cet héritage n'est pas présent dans la liste
                        if (!heritages.contains(PumlDiagram.subStr(heritageSilYA.toString()) + " <|--- " + PumlDiagram.subStr(e.getSimpleName().toString())))
                            heritages.add(PumlDiagram.subStr(heritageSilYA.toString()) + " <|--- " + PumlDiagram.subStr(e.getSimpleName().toString()));
                    }
                }
            }
        }

        return heritages;
    }
}
