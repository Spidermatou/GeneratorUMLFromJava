package pumlFromJava.liens;

import pumlFromJava.PumlDiagram;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;

public class Interfaces
{
    private Element element;

    public Interfaces(Element element)
    {
        //Elément global (le package)
        this.element=element;
    }

    public ArrayList<String> obtenirLesImplements()
    {
        //Une liste dans laquelle on met tous les liens entre les interfaces
        ArrayList<String>interfaces=new ArrayList<String>();

        //Pour chaque élément (ici chaque classe/énumération/interface du package (car j'ai l'élément global (le package) que je lui ait donné dans le constructeur))
        for(Element e:element.getEnclosedElements())
        {
            //Je reparcours chaque élément dans cette classe/interface/énumération
            //Si je ne fais pas ce 2ème foreach, il y a une erreur de conversion
            for (Element el : e.getEnclosedElements())
            {
                TypeElement typeElement=(TypeElement) el.getEnclosingElement();

                //Il y a dans l'interface TypeElement, une méthode getInterfaces() qui renvoie l'interface implémentée par cette classe ou héritée par cette interface
                for(TypeMirror tm:typeElement.getInterfaces())
                {
                    //Si c'est un implement
                    if(e.getKind().isInterface())
                    {
                        //On vérifie qu'on ne l'a pas déjà dans la liste
                        if (!interfaces.contains(PumlDiagram.subStr(tm.toString()) + " <|-- " + e.getSimpleName()))
                            interfaces.add((PumlDiagram.subStr(tm.toString()) + " <|-- " + e.getSimpleName()));
                    }
                    //Sinon, c'est alors un extend
                    else
                    {
                        //Ici aussi, on vérifie qu'on ne l'a pas déjà dans la liste
                        if (!interfaces.contains(PumlDiagram.subStr(tm.toString()) + " <|... " + e.getSimpleName()))
                            interfaces.add((PumlDiagram.subStr(tm.toString()) + " <|... " + e.getSimpleName()));
                    }
                }
            }
        }
        return interfaces;
    }
}
