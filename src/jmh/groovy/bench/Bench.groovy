package bench

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State

import java.util.regex.Pattern

@State(Scope.Benchmark)
class Bench {

    @Benchmark
    @TypeChecked(TypeCheckingMode.SKIP)
    void original_method() {
        def categories = [path: [[id: "MLA1540", name: "Servicios"], [id: "MLA113657", name: "Oficios"],[id: "MLA43972", name: "Plomero y Gasista"]]]

        def isRent = categories?.path?.find { element ->
            element.name?.toLowerCase() =~ /renta|alquil|alug|tempora|arriend/
        }

        assert true
    }

    @Benchmark
    @CompileStatic
    @TypeChecked(TypeCheckingMode.SKIP)
    void using_static_pattern() {
        def categories = [path: [[id: "MLA1540", name: "Servicios"], [id: "MLA113657", name: "Oficios"],[id: "MLA43972", name: "Plomero y Gasista"]]]
        Pattern pattern = ~ /renta|alquil|alug|tempora|arriend/
        def isRent = categories?.path?.find { element ->
            pattern.matcher(element.name?.toLowerCase()).matches()
        }

        assert true
    }

    @Benchmark
    @CompileStatic
    @TypeChecked(TypeCheckingMode.SKIP)
    void using_join_and_regexp() {
        def categories = [path: [[id: "MLA1540", name: "Servicios"], [id: "MLA113657", name: "Oficios"],[id: "MLA43972", name: "Plomero y Gasista"]]]
        def pattern = ~ /renta|alquil|alug|tempora|arriend/
        def list = categories.path*.name*.toLowerCase()
        def isRent = list.join(' ').find {
            pattern.matcher(it as String).matches()
        }

        assert true
    }

    @Benchmark
    @TypeChecked(TypeCheckingMode.SKIP)
    void using_join_with_tokenize_and_contains() {
        def categories = [path: [[id: "MLA1540", name: "Servicios"], [id: "MLA113657", name: "Oficios"],[id: "MLA43972", name: "Plomero y Gasista"]]]
        def list = categories.path*.name*.toLowerCase()
        def isRent = list.join(' ').tokenize().find {
            it.startsWith("renta") || it.startsWith("alquil") || it.startsWith("alug") || it.startsWith("tempora") || it.startsWith("arriend")
        }

        assert true
    }

    @Benchmark
    @TypeChecked(TypeCheckingMode.SKIP)
    void using_find_and_contains() {
        def categories = [path: [[id: "MLA1540", name: "Servicios"], [id: "MLA113657", name: "Oficios"],[id: "MLA43972", name: "Plomero y Gasista"]]]
        def isRent = categories?.path?.find {
            String element = it.name?.toLowerCase()
            element.startsWith("renta") || element.startsWith("alquil") || element.startsWith("alug") || element.startsWith("tempora") || element.startsWith("arriend")
        }

        assert true
    }

    @Benchmark
    @CompileStatic
    @TypeChecked(TypeCheckingMode.SKIP)
    void using_inject_and_regexp() {
        def pattern = ~ /renta|alquil|alug|tempora|arriend/
        def categories = [path: [[id: "MLA1540", name: "Servicios"], [id: "MLA113657", name: "Oficios"],[id: "MLA43972", name: "Plomero y Gasista"]]]
        String allCategs = categories?.path?.inject("", { value, element ->
            value + element.name + "|"
        })

        pattern.matcher(allCategs.toLowerCase()).matches()
        assert true
    }
}
