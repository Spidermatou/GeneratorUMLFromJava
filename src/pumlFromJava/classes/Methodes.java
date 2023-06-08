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
                    //Pour les liste (je regarde le dernier caractère (si c'est un '>'))
                    else if (((ExecutableElement) methode).getReturnType().toString().charAt(((ExecutableElement) methode).getReturnType().toString().length()-1)=='>')
                    {
                        texte += "):" + parameterList( PumlDiagram.subStrParametres( ((ExecutableElement) methode).getReturnType().toString() )) ;
                    }

                    //Tout le reste
                    else
                    {
                        texte += "):" + PumlDiagram.subStrParametres(((ExecutableElement) methode).getReturnType().toString()) ;
                    }
                 }

                //---------Partie Override--------
                //N'est pas parfait et peut avoir des erreurs
                //On teste si la méthode est override
                List<? extends AnnotationMirror> anno = methode.getAnnotationMirrors();
                for(AnnotationMirror an : anno)
                {
                     //Si la methode est override on ajoute redefine au texte de la methode

                     String classeBigBoss="";
                     TypeElement typeElement = (TypeElement) methode.getEnclosingElement();
                     TypeMirror heritageSilYA = typeElement.getSuperclass();
                     ExecutableElement exectue=(ExecutableElement) methode;

                     //S'il y a un Override
                     if(an.toString().equals("@java.lang.Override"))
                     {
                         //Si la classe n'a pas d'interface implémenter
                         if(typeElement.getInterfaces().isEmpty())
                         {
                             //Alors elle a forcément que 1 seule classe hériter
                             //(une seule est possible en java, car il n'y a que des héritages simples)
                             classeBigBoss=PumlDiagram.subStr(heritageSilYA.toString());
                         }
                         //Sinon donc elle peut implementer des interfaces ou avoir une classe mère
                         else if (!typeElement.getInterfaces().isEmpty())
                         {
                             //System.out.println(((DeclaredType)typeElement.getInterfaces().get(0)).asElement());

                             //Mais il y a une problème, car il ne prendra forcément que soit une classe mère ou soit une interface pour tous les Override
                             //Or, les méthodes peuvent redéfinir des méthodes de classes et/ou d'interfaces
                             //Si elle n'a pas de classe mère
                             if(((TypeElement) methode.getEnclosingElement()).getSuperclass().toString().equals("none"))
                                 classeBigBoss=PumlDiagram.subStr(typeElement.getInterfaces().get(0).toString());
                             //Sinon, elle a forcément une seule classe mère
                             else
                                 classeBigBoss=PumlDiagram.subStr(heritageSilYA.toString());
                         }

                         //Petit problème car getSuperClasse renvoie la classe hériter (et ce n'est pas forcement la meme chose pour les methodes (elle peuvent redefines soit une méthode d'interface ou de classe mère)
                         texte += "{redefine::"+classeBigBoss/*+PumlDiagram.subStr( typeElement.getSuperclass().toString())*/+"."+PumlDiagram.subStr(methode.getSimpleName().toString()+"}");
                     }
                 }
                //---------------------------------------

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

