package tp.export;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation personnalisée @Output.
 * - Retention RUNTIME  → lisible par réflexion à l'exécution.
 * - Target TYPE        → applicable sur les classes.
 * La valeur est le chemin du fichier d'export.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Output {
    String value() default "out.txt";
}
