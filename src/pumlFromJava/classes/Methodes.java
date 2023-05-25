package pumlFromJava.classes;

import pumlFromJava.PumlDiagram;

import javax.lang.model.element.*;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.util.List;

public class Methodes
{
    Element element;

    public Methodes(Element element)
    {
        this.element=element;
    }

    public String obtenirLesMethodesDeLaClasse()
    {
        String texte="";

        for(Element methode:element.getEnclosedElements())
        {
            if(methode.getKind()== ElementKind.METHOD)
            {
                Modificateurs recupModif = new Modificateurs(methode);
                texte += recupModif.obtenirLesModificateurs();

                texte += methode.getSimpleName()+"(";
                int i=0;

                 ExecutableElement ee=(ExecutableElement)methode;
                 List<? extends  VariableElement> lst=ee.getParameters();
                 for(VariableElement ve:lst)
                 {
                     if(i!=0)
                         texte+=",";

                     if(i==lst.size()-1)
                     {
                         texte += lst.get(i) + ":";
                         texte += PumlDiagram.subStr(ve.asType().toString());
                     }
                     else
                     {
                         texte += lst.get(i) + ":";
                         texte += PumlDiagram.subStr(ve.asType().toString());
                     }

                     i++;
                 }

                 if(regardeSiVoidALaFin(((ExecutableElement) methode).getReturnType().toString()) )
                    texte+=")\n";

                 else
                 {
                     if (((ExecutableElement) methode).getReturnType().toString().charAt(((ExecutableElement) methode).getReturnType().toString().length()-1)=='>')
                     {
                         //Il faut s'occuper de cette partie car c'est les listes
                         texte += "):" + parameterList( PumlDiagram.subStr( ((ExecutableElement) methode).getReturnType().toString() )) + "\n";
                     }
                     else
                     {
                         texte += "):" + PumlDiagram.subStr(((ExecutableElement) methode).getReturnType().toString()) + "\n";
                     }
                 }
            }
        }

        return texte;
    }

    public boolean regardeSiVoidALaFin(String txt)
    {
        boolean res=false;
        if(txt.length()>=4)
        {
            //System.out.println(txt.substring(txt.length()-4));

            if (txt.substring(txt.length() - 4).equals("void"))
            {
                res = true;
            }
        }

        return res;
    }

    public static String parameterList(String texte)
    {
        return texte += "[*]";
    }
}

