# HuellitasApp

Prototipo de la Evaluación Final de la Unidad III de Programación Android, IP Santo Tomás. Administra mascotas con Java, XML y Cloud Firestore. Incluye agregar, listar en tiempo real, ver, editar y eliminar con confirmación.

## Abrir y conectar Firebase

1. Extrae el ZIP. En Android Studio selecciona **Open** y abre la carpeta `HuellitasApp`, donde está `settings.gradle`. No crees otro proyecto ni abras solamente `app`.
2. Usa un JDK 17 o 21 para ejecutar Gradle: Settings > Build, Execution, Deployment > Build Tools > Gradle > Gradle JDK. El código de la app conserva Java 11. El JDK que ejecuta Gradle y el nivel del lenguaje de la app son configuraciones diferentes. No uses JDK 25 con este wrapper.
3. En SDK Manager instala Android SDK Platform 35 y las herramientas de compilación que solicite Android Studio. Necesitas Internet para descargar Gradle y dependencias la primera vez.
4. Entra a [Firebase Console](https://console.firebase.google.com/) con tu cuenta y crea tu proyecto.
5. Registra una aplicación Android con el paquete exacto **`com.example.huellitasapp`**. Para este CRUD de Firestore no se necesita registrar SHA-1.
6. Descarga tu `google-services.json`. Cópialo en **`HuellitasApp/app/google-services.json`**, al lado de `app/build.gradle`. Si el navegador añadió `(1)` al nombre, renómbralo. No uses el archivo del profesor.
7. En Firebase abre Firestore Database y crea la base **Cloud Firestore**, edición Standard, base predeterminada `(default)`, en **modo de prueba**, como pide la evaluación. Selecciona la ubicación disponible apropiada para tu proyecto. No crees Realtime Database.
8. Conserva las reglas temporales que genera el modo de prueba y revisa su fecha de vencimiento. Ese modo permite acceso sin autenticación durante el laboratorio; no es una configuración para una aplicación pública. No se añadió inicio de sesión porque no lo exige la pauta.
9. Sincroniza Gradle mediante **Sync Project with Gradle Files**. No es necesario crear manualmente `mascotas`: Firestore la crea al guardar la primera mascota.
10. Ejecuta la app en un emulador o teléfono Android 7.0/API 24 o superior con Internet. Agrega una mascota y verifica su documento en la consola.

El ZIP no contiene ningún `google-services.json`, credenciales ni enlaces a una cuenta ajena. Es necesario completar esta conexión antes de compilar y ejecutar la app normalmente. El plugin Google Services está habilitado y no se omite cuando falta el JSON.

Referencias: [configuración Android de Firebase](https://firebase.google.com/docs/android/setup), [inicio de Firestore](https://firebase.google.com/docs/firestore/quickstart).

## Configuración conservada

| Elemento | Versión |
|---|---|
| Package y applicationId | `com.example.huellitasapp` |
| Compile / target / minimum SDK | 35 / 35 / 24 |
| Lenguaje Java de la aplicación | 11 |
| Android Gradle Plugin | 8.9.1 |
| Gradle Wrapper | 8.13, incluido con su JAR y scripts |
| Google Services | 4.4.2 |
| Firebase BoM | 33.7.0 |
| AppCompat / Material | 1.7.0 / 1.12.0 |
| AndroidX Activity | 1.9.3 |
| RecyclerView / CardView | 1.3.2 / 1.0.0 |

Se mantuvieron las versiones del profesor y su wrapper. Se agregaron RecyclerView y CardView para cumplir la pauta. Se retiró ConstraintLayout 2.2.0 porque las pantallas usan LinearLayout y ScrollView. No se añadió arquitectura, autenticación, fotografías, búsquedas ni filtros. Los Spinners toman sus opciones directamente desde XML; el Adapter propio corresponde al RecyclerView.

AGP 8.9 requiere al menos Gradle 8.11.1 y JDK 17; el wrapper 8.13 del profesor se conserva. [Tabla oficial de compatibilidad](https://developer.android.com/build/releases/agp-8-9-0-release-notes).

## Datos y navegación

Colección: **`mascotas`**. Firestore genera el ID con `add(mascota)`.

| Campo | Tipo | Valor de ejemplo |
|---|---|---|
| `nombre` | String | Canela |
| `especie` | String | Perro; opciones: Perro, Gato, Otro |
| `edad` | Number entero | 3 |
| `vacunado` | Boolean | true |
| `estado` | String | Disponible; opciones: Disponible, En proceso, Adoptado |
| `descripcion` | String | Sociable y tranquila |

`id` existe en Java y tiene `@Exclude`; no se guarda dentro del documento. La edad se maneja con `int` y se valida entre 0 y 2147483647. Se rechazan campos vacíos, valores negativos, decimales y números fuera de ese rango. Nombre y descripción se recortan con `trim()`. La casilla sin marcar significa `false`, un valor válido. Especie y estado siempre tienen una opción válida; sus valores iniciales son Perro y Disponible.

Flujo: Listar → Agregar → Listar; Listar → Ver → Editar → Listar; Listar → Ver → confirmar eliminación → Listar. Se usa la misma clave `id_mascota` en ambos Intents y se transmite únicamente el ID. El botón Atrás del sistema permite cancelar la navegación.

Después de agregar, editar o eliminar se abre el listado con `CLEAR_TOP | SINGLE_TOP`: se retiran las pantallas intermedias para que Atrás no vuelva a un detalle eliminado o desactualizado. Al volver al listado, `onStart()` vuelve a activar la escucha.

## Archivos y explicación para la defensa

Los códigos completos están dentro de `app/src/main/`. No hay fragmentos pendientes de implementar.

| Archivo | Función y qué explicar |
|---|---|
| `java/com/example/huellitasapp/Mascota.java` | Modelo con seis campos de datos, ID, constructor vacío, constructor con datos, getters y setters. Firestore usa el constructor vacío al convertir documentos. `@Exclude` evita guardar el ID como campo. |
| `MascotaAdapter.java`, en el mismo paquete | Crea cada tarjeta, asocia sus TextView con una Mascota y abre VerActivity al tocarla. El ViewHolder conserva las referencias de cada fila para reutilizarlas. |
| `ListarActivity.java` | Pantalla principal. Crea el RecyclerView con LinearLayoutManager y una `List<Mascota>` respaldada por `ArrayList`. `addSnapshotListener()` recibe cambios, reconstruye la lista y notifica al Adapter. `onStop()` llama a `remove()` y deja la referencia en null. |
| `AgregarActivity.java` | Lee y valida el formulario, crea una Mascota y llama a `collection("mascotas").add(mascota)`. Deshabilita Guardar mientras espera y muestra éxito o error con Toast. |
| `VerActivity.java` | Recibe `getStringExtra()`, consulta `document(id).get()` y muestra los seis campos. Solicita confirmación en AlertDialog y elimina con `document(id).delete()`. |
| `EditarActivity.java` | Recibe el ID y carga datos desde Firestore. Guarda los seis campos con `update()`, sin crear un documento sustituto. Solo habilita Guardar después de cargar. |
| `res/layout/activity_listar.xml` | Título, botón Agregar, mensaje de estado y RecyclerView. |
| `res/layout/item_mascota.xml` | CardView con nombre, especie, edad y estado de cada mascota. |
| `res/layout/activity_agregar.xml` | Formulario con EditText, Spinner, CheckBox y Button. ScrollView permite desplazarse en pantallas pequeñas. |
| `res/layout/activity_ver.xml` | TextView con los seis datos, botón Editar y botón Eliminar. |
| `res/layout/activity_editar.xml` | Formulario de edición; mantiene IDs equivalentes a los del formulario de alta, pero en otro layout. |
| `res/values/arrays.xml` | Valores exactos permitidos para especie y estado. |
| `res/values/themes.xml` | Tema Material sencillo con AppCompat, sin barra de acción; cada pantalla tiene título propio. |
| `AndroidManifest.xml` | Permiso INTERNET y registro de cuatro Activities. Solo ListarActivity es launcher y exportada. |
| `app/build.gradle` | Paquete, SDK, Java 11, plugins y dependencias. |
| `build.gradle` de raíz | Versiones de los plugins Android y Google Services. |
| `settings.gradle` | Nombre HuellitasApp, módulo app y repositorios Google/Maven Central. |
| `gradle.properties` | AndroidX, memoria y codificación UTF-8 para Gradle. |
| `gradle/wrapper/*`, `gradlew`, `gradlew.bat` | Permiten usar la versión Gradle del proyecto sin instalar Gradle globalmente. |
| `app/proguard-rules.pro` | Archivo de reglas conservado; minificación desactivada como en el ejemplo. |
| `.gitignore` | Excluye cachés, builds, configuración local, JSON de Firebase y claves de firma. |

Las raíces XML usan `fitsSystemWindows` para respetar los márgenes del sistema. Hay que verificar visualmente las pantallas en API 35, donde se aplica edge-to-edge por el target 35. [Referencia Android](https://developer.android.com/develop/ui/views/layout/edge-to-edge).

Preguntas frecuentes para explicar el código:

- **¿Por qué enviar el ID?** Identifica el documento que debe consultarse y evita pasar una copia completa de los datos entre pantallas.
- **¿Por qué `update()`?** Cambia los campos indicados del documento existente. A diferencia de `set()`, falla si el documento ya no existe.
- **¿Por qué listeners de éxito y error?** Firestore trabaja de manera asíncrona: el resultado llega después de llamar al método.
- **¿Qué ocurre con otra persona usando la app?** Los cambios confirmados en Firestore llegan al listener del listado mientras está visible y conectado.
- **¿Qué ocurre sin conexión?** Firestore puede mostrar caché y mantener escrituras pendientes. La app no anuncia éxito antes del callback de confirmación. Para la demostración usa conexión estable y espera el Toast antes de continuar.
- **¿Se modificó la forma de programar del profesor?** Se mantuvieron Activities, findViewById, métodos privados por operación y listeners con lambdas. Las diferencias necesarias se detallan en `docs/ANALISIS_Y_VERIFICACION.md`.

## Generar el APK

Primero conecta Firebase y sincroniza. Selecciona la variante `debug` y utiliza la opción para generar APK(s) del menú Build de Android Studio, o ejecuta en la terminal de la raíz:

```powershell
.\gradlew.bat :app:assembleDebug
```

En Linux/macOS: `chmod +x gradlew` y `./gradlew :app:assembleDebug`.

El APK generado estará en `app/build/outputs/apk/debug/app-debug.apk`. Es un APK de depuración para el laboratorio, no una publicación en Play Store. Puedes ejecutar la app directamente con Run en un dispositivo API 24 o superior. Para un APK de release firmado, usa Generate Signed Bundle / APK y tu propia clave de firma; no subas la clave al repositorio.

## GitHub y commits reales

El ZIP es una entrega de código, no incluye un historial Git inventado ni un repositorio remoto creado. Si ya existe un repositorio de tu trabajo, integra los archivos allí y conserva su historial.

Si empiezas desde esta entrega completa, registra honestamente la base recibida y realiza nuevos commits cuando efectivamente revises, pruebes o cambies el proyecto. No atribuyas a días anteriores etapas que no registraste. Si desarrollas paso a paso, haz un commit al terminar cada etapa real: configuración/modelo, alta validada, listado en tiempo real, detalle por ID, edición/eliminación y documentación de pruebas. No basta con ejecutar varios commits sobre archivos sin cambios.

Para comenzar un repositorio nuevo desde la carpeta HuellitasApp:

```powershell
git init
git branch -M main
git status
git add .
git diff --cached --stat
git commit -m "Incorporar base del CRUD HuellitasApp para revisión"
```

Git puede pedir configurar `user.name` y `user.email`: usa tu identidad real. Tras conectar tu cuenta y probar cada operación, anota los resultados reales en la lista de comprobación, revisa `git diff`, agrega los archivos efectivamente modificados y crea un commit descriptivo. Ejemplo, solo después de realizar y documentar las pruebas: `git commit -m "Documentar pruebas del CRUD con mi proyecto Firebase"`.

En GitHub crea un repositorio vacío `HuellitasApp`, sin README inicial porque ya está incluido. Copia su URL HTTPS. Sustituye `TU_USUARIO` en estos comandos:

```powershell
git remote add origin https://github.com/TU_USUARIO/HuellitasApp.git
git push -u origin main
git log --oneline
```

Autentícate mediante el mecanismo de GitHub disponible en tu equipo. Abre el repositorio en el navegador y comprueba los archivos y commits. Si es privado, concede acceso al profesor. El enlace que debes entregar es `https://github.com/TU_USUARIO/HuellitasApp` con tu usuario real. Envíalo a **poyaneder@santotomas.cl** y adjúntalo en el **Aula Virtual**. No se ha enviado ningún correo ni publicado el proyecto desde esta entrega.

`google-services.json` está excluido por defecto. El clonador debe conectar su propio Firebase. La pauta exige que tu copia operativa tenga el JSON; esta exclusión no lo elimina de tu equipo. Si el profesor necesita evaluar tu conexión, coordina la entrega de tu configuración por el medio indicado por él.

Referencia: [subir código local a GitHub](https://docs.github.com/en/migrations/importing-source-code/using-the-command-line-to-import-source-code/adding-locally-hosted-code-to-github?platform=windows).

## Lista de comprobación de los 60 puntos

La implementación cubre los requisitos de código. Los casilleros quedan abiertos para registrar pruebas reales; no equivalen a una nota obtenida.

| Criterio | Puntos | Evidencia incluida | Verificación pendiente |
|---|---:|---|---|
| Configurar Firebase y modelo | 8 | Plugins, Firestore, permiso, Mascota con constructor y `@Exclude` | Tu proyecto Firebase, tu JSON, sincronizar y conectar |
| Agregar | 12 | Seis campos, validación, `add()`, Toast éxito/error | Crear y verificar tipos y auto-ID en consola |
| Listar | 12 | RecyclerView, CardView, Adapter propio, snapshot listener | Cambios desde otra sesión sin reiniciar |
| Ver detalle | 8 | Intent solo con ID, getStringExtra y `document(id).get()` | Ver todos los campos del documento seleccionado |
| Editar | 10 | Precarga desde Firestore, validación y `update()` | Modificar los seis campos y volver al listado |
| Eliminar | 6 | AlertDialog, delete por ID y regreso al listado | Cancelar y confirmar; revisar consola |
| Control de versiones | 4 | .gitignore e instrucciones | Commits reales, push y entrega del enlace |
| **Total de la pauta** | **60** | **Código preparado para estos criterios** | **Evaluación y pruebas pendientes** |

- [ ] Compilar con mi JSON y ejecutar en dispositivo/emulador.
- [ ] Crear Canela, Perro, 3, vacunada, Disponible, descripción no vacía; verificar exactamente seis campos y ningún campo `id` en Firestore.
- [ ] Intentar guardar con nombre, edad o descripción vacíos: no debe crear documento.
- [ ] Probar edad 0, negativa, decimal, texto y un número demasiado grande, incluyendo pegado desde portapapeles: aceptar solo enteros dentro del rango.
- [ ] Probar las tres especies, los tres estados y vacunado true/false.
- [ ] Abrir Ver y comparar los seis datos con la consola.
- [ ] Editar los seis campos, confirmar que el ID no cambió y que la app vuelve al listado.
- [ ] Cambiar un documento desde Firebase Console u otro dispositivo mientras el listado está visible: la tarjeta se actualiza sola.
- [ ] Salir del listado y volver varias veces: la escucha se vuelve a registrar sin duplicar filas.
- [ ] Cancelar Eliminar: conservar el documento. Confirmar: eliminar y volver al listado.
- [ ] Borrar desde consola una mascota antes de abrir su detalle: mostrar que ya no existe.
- [ ] Verificar Toast ante errores de permisos en un proyecto de laboratorio; restaurar luego las reglas de prueba apropiadas.
- [ ] Revisar pantallas con teclado, desplazamiento, modo oscuro, API 24 y API 35; comprobar que botones y textos no quedan bajo las barras del sistema.
- [ ] Revisar GitHub, commits y acceso del profesor; enviar enlace por correo y Aula Virtual.

## Estado de validación de esta entrega

Se realizó lectura de la evaluación completa (3 páginas), guía completa (10 páginas) y código fuente del ZIP del profesor. Se revisaron nombres, XML, Activities, IDs, campos, Intents y archivos del wrapper mediante controles estáticos; consulta el informe en `docs/ANALISIS_Y_VERIFICACION.md`.

Se intentó `:app:assembleDebug` con JDK 21 y SDK Android 35 disponibles. El proceso se detuvo al descargar Gradle 8.13 por `java.net.UnknownHostException: services.gradle.org`. No llegó a compilar el código. Además, falta tu JSON real, que es necesario para el procesamiento de Google Services. **No se declara compilación exitosa, APK generado ni pruebas de funcionamiento realizadas contra Firebase.**

## Errores comunes

| Error | Acción |
|---|---|
| `google-services.json is missing` | Coloca tu JSON real en `app/`. |
| `No matching client found for package name` | Registra `com.example.huellitasapp` y vuelve a descargar su JSON. |
| `PERMISSION_DENIED` | Revisa reglas de la base predeterminada y vencimiento del modo de prueba. |
| No descarga Gradle/dependencias | Comprueba Internet, proxy, DNS y que Gradle no esté en modo offline. |
| Error del JDK al sincronizar | Selecciona JDK 17 o 21 en la configuración Gradle. Mantén Java 11 en `compileOptions`. |
| Lista vacía | Verifica el proyecto indicado por tu JSON, base `(default)` y colección `mascotas`. |
| Error al convertir datos | Los documentos creados manualmente deben respetar nombres y tipos: edad entera, vacunado booleano. |
| Guardar tarda sin Toast | Comprueba la conexión; el callback de éxito espera confirmación de la escritura. |
