# Agregue reglas de ProGuard específicas del proyecto aquí.
# Por defecto, los indicadores en este archivo se anexan a las banderas especificadas
# in /Users/estimote/Library/Android/sdk/tools/proguard/proguard-android.txt
# Puede editar la ruta de inclusión y el orden cambiando los archivos proguard
# directiva en build.gradle.
#
# Para más detalles, ver
# http://developer.android.com/guide/developing/tools/proguard.html

# Agregue cualquier opción de guardado específica del proyecto aquí:

# Si su proyecto usa WebView con JS, elimine el comentario de la siguiente
# y especifique el nombre de clase totalmente calificado para la interfaz de JavaScript
# clase:
# -keepclassmembers class fqcn.of.javascript.interface.for.webview {
# público *;
#}

# Descomente esto para conservar la información del número de línea para
# depuración de los rastros de la pila.
# -keepattributes SourceFile, LineNumberTable

# Si conserva la información del número de línea, elimine el comentario de esta
# ocultar el nombre original del archivo fuente.
# -renamesourcefileattribute SourceFile
