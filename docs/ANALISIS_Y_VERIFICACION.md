# Análisis de las fuentes y verificación

## Alcance de las fuentes

Se leyó el texto completo de los dos PDF entregados y todos los archivos Java, XML, Gradle, properties y ProGuard del proyecto del profesor, junto con su `.gitignore`. Se comprobó visualmente la página 2 de la pauta, que contiene el esquema y los puntajes. Se conservaron los scripts y JAR del wrapper del profesor. No se reutilizó ni se incluyó su archivo de conexión Firebase.

La evaluación es la fuente de requisitos académicos; la guía y el ZIP sirven como referencia de implementación. La solicitud del usuario especifica el paquete, las versiones, la validación de edad, los nombres de archivos y el formato de entrega. Las indicaciones de envío presentes en la pauta se documentan como pasos de entrega: no se publicaron archivos, no se enviaron correos ni se accedió al Aula Virtual.

## Evaluación práctica: 3 páginas

- Página 1: actividad individual, sumativa, 100 minutos, ponderación 35%, total 60 puntos; integra Firestore, CRUD y transferencia de IDs entre Activities. El contexto requiere cambios visibles sin reiniciar y confirmación antes de borrar.
- Página 2: nombre HuellitasApp; colección obligatoria `mascotas`, documentos con ID automático y seis campos. Especie y estado tienen valores definidos; edad es entera y vacunado booleano. Distribuye 8 + 12 + 12 + 8 + 10 + 6 + 4 puntos. Exige RecyclerView, CardView, Adapter propio, snapshot listener, ID por Intent, update y delete con diálogo.
- Página 3: orden de las tareas, base Firestore en modo de prueba y entrega del repositorio tanto por correo como en Aula Virtual.

## Guía Firebase: 10 páginas

1. Presenta el temario, agenda telefónica y paquete de ejemplo.
2. Explica colecciones/documentos y registro de la aplicación; comienza Gradle Groovy con AGP 8.9.1 y Google Services 4.4.2.
3. Define SDK 35/24, Java 11, BoM 33.7.0, permiso INTERNET, creación de Firestore y reglas de laboratorio; inicia el modelo.
4. Completa modelo, constructor vacío, getters/setters y `@Exclude`; introduce operaciones asíncronas y `add()`.
5. Compara lectura puntual y tiempo real; muestra `get()` y comienza snapshot listener.
6. Muestra alta/baja del listener y constante `TIEMPO_REAL`; propone ListView con ArrayAdapter para la agenda.
7. Convierte snapshots a objetos, conserva IDs y ejemplifica filtros; compara edición con `set()` y `update()`.
8. Explica la diferencia entre ambas escrituras, precarga del formulario, borrado con confirmación y registro de Activities; pasa un objeto Serializable.
9. Refresca detalle, enumera errores de configuración/tipos/permisos e inicia resumen de operaciones.
10. Completa la tabla de comandos y recomienda la secuencia de implementación.

Los filtros son un extra de la guía y no se incorporaron. Las reglas abiertas permanentes del ejemplo no se copiaron: el README dirige al modo de prueba temporal que exige la pauta.

## Proyecto del profesor

| Archivo o grupo | Hallazgo y adaptación |
|---|---|
| `Contacto.java` | Implementa Serializable; tiene ID con `@Exclude` y campos NOMBRE/TELEFONO/CORREO con `@PropertyName`. Mascota conserva la estructura sencilla pero usa seis propiedades en minúsculas y no implementa Serializable. |
| `MainActivity.java` | Formulario y ListView en la misma pantalla, ArrayAdapter y dos listas; `TIEMPO_REAL=false` por defecto. Se separó Agregar, se reemplazó la lista por RecyclerView con MascotaAdapter y se dejó una sola modalidad de lectura: snapshot listener. |
| `DetalleActivity.java` | Recibe el objeto completo, muestra datos, refresca en onStart, tiene editar y AlertDialog para eliminar. VerActivity recibe solo el ID y consulta `document(id).get()`. Conserva el diálogo y Toast. |
| `EditarActivity.java` | Recibe Serializable, valida con TextUtils y escribe con set. La adaptación recibe ID, consulta datos actuales y usa update con los seis campos. |
| Tres layouts originales | LinearLayout vertical, padding de 16dp, EditText/TextView/Button. Se conserva ese enfoque con ScrollView en formularios/detalle y las incorporaciones exigidas de Spinner, CheckBox, RecyclerView y CardView. |
| `AndroidManifest.xml` | INTERNET y tres Activities con Main como launcher. Se conservó INTERNET y se registraron cuatro Activities con Listar como launcher. |
| `build.gradle` y `app/build.gradle` | Versiones iguales a las indicadas por el usuario, Java 11 y SDK 35/24. Se conservaron, retirando únicamente ConstraintLayout sin uso y agregando RecyclerView/CardView. |
| `settings.gradle` | Repositorios Google, Maven Central y plugin portal; se conservan, cambiando rootProject.name. |
| `gradle.properties` | AndroidX, UTF-8, memoria 2048 MB y R no transitivo: conservados. |
| Wrapper | Gradle 8.13 con scripts y JAR: conservado. |
| Tema e icono | El ejemplo utiliza tema Material con barra de acción y un icono de contacto. Se utiliza tema Material sencillo sin barra y títulos propios; no se reutilizó el icono de persona. |
| ProGuard | Archivo básico y minificación desactivada: conservados. |
| `.gitignore` | Se amplió para excluir configuración Firebase y claves de firma, además de archivos locales y generados. |

## Revisión estática efectuada

Se ejecutaron 35 comprobaciones automáticas sobre los archivos entregados. El registro íntegro está en `CONTROLES_ESTATICOS.txt`.

- XML bien formado y sin IDs duplicados en cada layout.
- Seis clases Java con nombres y paquete esperados.
- Referencias `R.id` presentes en el layout que utiliza cada clase.
- Cuatro Activities declaradas; ListarActivity como launcher.
- Colección `mascotas` usada en todas las operaciones.
- Clave compartida de Intent; ausencia de Serializable.
- Modelo con seis propiedades, ID excluido y nombres coherentes con update.
- Presencia de add, get, update, delete, AlertDialog y ciclo de vida del listener revisada también al leer el código.
- JAR del wrapper íntegro y archivos de construcción presentes.
- Ningún `google-services.json` incluido.

Estos controles verifican estructura y coherencia; no sustituyen javac, Android Lint, compilación, renderizado en Android ni pruebas de integración.

## Intento de compilación

Entorno encontrado: Android SDK Platform 35 y un JDK 21 instalado. Se usó este JDK en lugar del JDK 25 integrado en la instalación de Android Studio. Se aisló la caché Gradle en la carpeta de trabajo, sin cambiar la configuración global del equipo.

Comando ejecutado desde HuellitasApp:

```text
gradlew.bat :app:assembleDebug --no-daemon --console=plain
```

Resultado real: código de salida 1 al intentar descargar la distribución del wrapper.

```text
Downloading https://services.gradle.org/distributions/gradle-8.13-bin.zip
Exception in thread "main" java.net.UnknownHostException: services.gradle.org
```

No se alcanzaron las tareas de compilación. No se generó APK ni se ejecutó la app. Tampoco se realizó una prueba con Firebase, porque no se dispone de la configuración del usuario. Aunque la descarga se complete en otro entorno, se debe incorporar el JSON propio para ejecutar Google Services.

## Comprobaciones que faltan

Completar los casilleros del README después de sincronizar y compilar con la configuración propia. En especial: altas con tipos correctos, edición que conserva ID, confirmación/cancelación de borrado, actualización desde otro cliente, errores de acceso, y visualización con teclado y barras del sistema en API 24/35. Solo entonces se podrá afirmar que el CRUD fue probado en ejecución.

Los 60 puntos son la cobertura objetivo de la pauta, no una calificación certificada. Crear/conectar la cuenta Firebase, generar commits reales, publicar GitHub y entregar el enlace siguen siendo acciones que debe completar el usuario con su cuenta.
