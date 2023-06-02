package pumlFromJava.classes;

import pumlFromJava.FirstDoclet;
import pumlFromJava.PumlDiagram;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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



                //----Pour le type de retour----
                //Si c'est un Void
                if((((ExecutableElement) methode).getReturnType().getKind() == TypeKind.VOID))
                {
                    texte+=")";
                }
                //Sinon, pour les autres types de retour
                else
                {
                    //INT
                    if(((ExecutableElement) methode).getReturnType().getKind() == TypeKind.INT)
                    {
                        texte+="):Integer";
                    }
                    //Pour les liste
                    else if (((ExecutableElement) methode).getReturnType().toString().charAt(((ExecutableElement) methode).getReturnType().toString().length()-1)=='>')
                    {
                        texte += "):" + parameterList( PumlDiagram.subStr( ((ExecutableElement) methode).getReturnType().toString() )) ;
                    }
                    //Tout le reste
                    else
                    {
                        texte += "):" + PumlDiagram.subStr(((ExecutableElement) methode).getReturnType().toString()) ;
                    }


                     //En fait on peut savoir le type avec l'énumération TypeKind
                     //c mieux que des susbstring et equal un peu tkt
                     //if ((((ExecutableElement) methode).getReturnType().getKind() == TypeKind.BOOLEAN))
                         //System.out.println(((ExecutableElement) methode).getReturnType().getKind());
                 }

                 //On teste si la méthode est override
                 List<? extends AnnotationMirror> anno = methode.getAnnotationMirrors();
                 Boolean test = false;
                 for(AnnotationMirror an : anno)
                 {
                     //si la methode est override on ajoute redefine au texte de la methode

                     //petit probeleme avec interface et heritage tout ca tu connais
                     /*String classeBigBoss;
                     TypeElement typeElement = (TypeElement) methode.getEnclosingElement();
                     TypeMirror heritageSilYA = typeElement.getSuperclass();
                     classeBigBoss=PumlDiagram.subStr(heritageSilYA.toString());*/
                     if(an.toString().equals("@java.lang.Override"))
                     {
                         TypeElement testTE = (TypeElement) element;
                         texte += "{redefine::"/*+classeBigBoss*/+PumlDiagram.subStr( testTE.getSuperclass().toString())+"."+PumlDiagram.subStr(methode.getSimpleName().toString()+"}");
                         test = true;
                     }
                 }


                 texte+='\n';
            }

        }

        return texte;
    }

    public boolean regardeSiVoidALaFin(String txt)
    {
        boolean res=false;
        if(txt.length()>=4)
        {
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

