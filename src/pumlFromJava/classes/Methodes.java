package pumlFromJava.classes;

import pumlFromJava.FirstDoclet;
import pumlFromJava.PumlDiagram;

import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
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
        //L'élément ici est la classe ou l'interface
        this.element=element;
    }

    public String obtenirLesMethodesDeLaClasse()
    {
        String texte="";

        //Je parcours chaque élément de la classe ou de l'interface
        for(Element methode:element.getEnclosedElements())
        {
            //Si c'est une méthode
            if(methode.getKind()== ElementKind.METHOD)
            {
                //Je récupere les modificateurs de la méthode
                Modificateurs recupModif = new Modificateurs(methode);
                texte += recupModif.obtenirLesModificateurs();

                //J'ouvre une parenthèse pour les paramêtre
                texte += methode.getSimpleName()+"(";
                int i=0;

                //Je récupère le nom des paramêtre et leurs types
                ExecutableElement ee=(ExecutableElement)methode;
                List<? extends  VariableElement> lst=ee.getParameters();
                for(VariableElement ve:lst)
                {
                     if(i!=0)
                         texte+=",";

                     if(i==lst.size()-1)
                     {
                         texte += lst.get(i) + ":";
                         texte += PumlDiagram.subStrParametres(ve.asType().toString());
                     }
                     else
                     {
                         texte += lst.get(i) + ":";
                         texte += PumlDiagram.subStrParametres(ve.asType().toString());
                     }

                     i++;
                 }

                //----Pour le type de retour----
                //Si c'est un VOID
                if((((ExecutableElement) methode).getReturnType().getKind() == TypeKind.VOID))
                {
                    texte+=")";
                }
                //Sinon, pour les autres types de retour
                else
                {
                    //Si c'est un INT
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
                 }

                //-----Partie Override----
                //On teste si la méthode est override
                List<? extends AnnotationMirror> anno = methode.getAnnotationMirrors();
                for(AnnotationMirror an : anno)
                {
                     //si la methode est override on ajoute redefine au texte de la methode

                     //petit probeleme avec interface et heritage tout ca tu connais
                     String classeBigBoss="";
                     TypeElement typeElement = (TypeElement) methode.getEnclosingElement();
                     TypeMirror heritageSilYA = typeElement.getSuperclass();

                     //S'il y a un Override
                     if(an.toString().equals("@java.lang.Override"))
                     {

                         //   if(typeElement.getAnnotation(methode.))
                         //       classeBigBoss=typeElement.getSuperclass().toString();
                         //if(!typeElement.getInterfaces().isEmpty())
                        //    classeBigBoss=typeElement.getInterfaces().get(0).toString();

                         //TypeElement testTE = (TypeElement) element;
                         //System.out.println(classeBigBoss);

                         //Petit problème car getSuperClasse renvoie la classe hériter (et ce n'est pas forcement la meme chose pour les methodes)
                         texte += "{redefine::"/*+classeBigBoss*/+PumlDiagram.subStr( typeElement.getSuperclass().toString())+"."+PumlDiagram.subStr(methode.getSimpleName().toString()+"}");
                     }
                 }
                //pour override
                //get annotation
                //GetAnnoation.getInterfaces().size()<0

                 texte+='\n';
            }
        }

        return texte;
    }

    //Une méthode qui ajoute l'écriture d'une liste en UML
    public static String parameterList(String texte)
    {
        return texte += "[*]";
    }
}

