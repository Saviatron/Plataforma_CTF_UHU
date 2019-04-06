# Plataforma CTF - UHU

El proyecto a desarrollar se puede encajar, principalmente, dentro del proyecto docente del área de ***Ciencias de la Computación e Inteligencia Artificial***. 

Éste va a consistir en el desarrollo de una <u>plataforma multiagente</u> donde poner en práctica todas las ideas y algoritmos desarrollados durante los estudios de Grado en Ingeniería Informática. 

En concreto, para la asignatura de Inteligencia Artificial, los alumnos lo usarán para experimentar con los principales ***algoritmos de búsqueda*** estudiados. 

Para la asignatura Sistemas Inteligentes, los alumnos podrán desarrollar por completo un ***agente software***, o varios, para poner en práctica los conocimientos aprendidos en la asignatura. 

**Sobre la plataforma:**

- Se desarrollará en Java. 
- Simulará el juego "*Capture the Flag*". 
- El servidor será un agente sofware *JADE*. 
- Recibirá jugadores y simulará las partidas. 
- También se desarrollará un agente gráfico, para ver la partida. 
- La comunicación entre agentes se basará en la biblioteca JADE. 
- La parte gráfica se desarrollará mediante la biblioteca *PROCESSING*.

**Objetivos:** 

- Estudio y desarrollo de la plataforma base y arquitectura del sistema. ✅
- Integración con entorno gráfico. ✅
- Desarrollo de esquemas de programación. ✅
- Elaboración de algunas prácticas "tipo". ✅
- Redactar documentación del funcionamiento del sistema. ✅
- Implantación de la plataforma en la web. ❌

**Esquema básico:**

![](C:\Users\JAVIER\Desktop\TFG\GitRepository\Plataforma_CTF_UHU\Files\Esquema_CTF.png)
![](<https://github.com/Saviatron/Plataforma_CTF_UHU/blob/master/Files/Esquema_CTF.png>)



## Blog
Para estar al tanto de la evolución del proyecto, puede vistar la página:
http://opendatalab.uhu.es/index.php/Captura_la_bandera

## Instructions
Archivo con las instrucciones de la plataforma: [Instrucciones.pdf](Files/Instrucciones.pdf)

## Demo Video

[![](http://img.youtube.com/vi/K_xbm8FObIE/0.jpg)](http://www.youtube.com/watch?v=K_xbm8FObIE "Youtube")

## Getting Started

Las carpetas "ctf_monitor" y "ctf_servidor" corresponden a los 2 proyectos desarrollados en Eclipse Mars. 
- El proyecto "ctf_servidor" corresponde al agente servidor que administrará las partidas.
- El proyecto "ctf_monitor" corresponde al agente monitor, que se conectará al servidor y visualizará la partida.

### Prerequisites

La carpeta "lib" contiene todas las dependencias de los proyectos. 

- JRE: JavaSE-1.8



- JADE: JAVA Agent DEvelopment Framework is an open source platform for peer-to-peer agent based applications.

  ​	Link: http://jade.tilab.com/

  

- Processing: Processing is a flexible software sketchbook and a language for learning how to code within the 
  context of the visual arts.
  » Free to download and open source

  ​	Link: https://processing.org/

  

- Minim: Minim is an audio library that uses the JavaSound API, a bit of Tritonus, and Javazoom’s MP3SPI to 
  provide an easy to use audio library for people developing in the Processing environment.
  Minim is licensed under the GNU Lesser General Public License (LGPL), a copy of which is included with the 
  distribution.

  ​	Link: http://code.compartmental.net/minim/

  



### Installing

Para instalar este programa en su equipo, simplemente descargue los proyectos de Eclipse e importelos al WorkSpace deseado.
Luego, podrá exportarlo como jar para ejecutarlo.

## Running

Excepcionalmente, se ha subido la carpeta "CTF_2019_v6.1", la cuál contiene todos los archivos necesarios para la ejecución de la plataforma.
Para ello:

1. Configuramos el servidor mediante su archivo .cfg
2. Configuramos el monitor mediante su archivo .cfg
3. Ejecutamos el archivo Servidor.bat
4. Ejecutamos el archivo Monitor.bat
5. Si todo ha ido bien, veremos la partida. Solo queda lanzar los jugadores. Para ello, ejecutamos el .bat deseado de los jugadores.

## Version

Version 6.1

Estado de desarrollo: **`98%`**

- La plataforma es totalmente funcional, aunque siempre estará la opción de aumentar su desarrollo añadiendo nuevas funcionalidades.

## Last Changes

- Añadida funcionalidad de tablero parcial:
![](C:\Users\JAVIER\Desktop\TFG\GitRepository\Plataforma_CTF_UHU\Files\Tablero_Parcial.gif)
![](<https://github.com/Saviatron/Plataforma_CTF_UHU/blob/master/Files/Tablero_Parcial.gif>)

## Accessibility

Uno de los pilares del software libre es que cualquier persona pueda disponer del software y utilizarlo como le parezca adecuado. Por tanto la accesibilidad es un principio que va muy implícito en las cuatro libertades del software libre que ha definido Richard M. Stallman.
Posiblemente, lo primero que se nos venga a la mente cuando hablamos de accesibilidad es la ceguera. Pero la accesibilidad se preocupa también de personas con discapacidades motoras, auditivas, etc o individuos con discapacidades intelectuales y culturales.
Gracias a los monitores individuales e independientes que complementan esta plataforma, cada usuario podrá seguir a su equipo de jugadores. Cualquier persona puede diseñar su equipo y lanzarlos a competir desde su pc.

## Free Software Community

Aunque en un principio el desarrollo fue pensado para la integración de las prácticas de varias asignaturas impartidas en el Grado de Ingeniería Informática de la Universidad de Huelva, se anima a todas las entidades educativas, y todo público interesado en el tema, de acuerdo con la política del Software Libre a usar esta plataforma. Con ella se podrán estudiar y poner en práctica la metodología de los Sistemas Multi-Agentes (MAS), que tanta importancia tienen en la actualidad.

## Author

* Este proyecto ha sido desarrollado por: **Javier Martín Moreno** 
* Tutor de TFG: **Gonzalo Antonio Aranda Corral**

## Contact

- Si tiene alguna sugerencia, quiere reportar algún error, o necesita resolver alguna duda respecto a la plataforma:

  [javimm97@gmail.com](mailto:javimm97@gmail.com)

## License

This project is licensed under the GNU General Public License (GPL) version 3 - see the [LICENSE.md](LICENSE.md) file for details

