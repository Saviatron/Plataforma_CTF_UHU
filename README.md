# Plataforma CTF - UHU

El proyecto a desarrollar se puede encajar, principalmente, dentro del proyecto docente del área de de Ciencias de la Computación e Inteligencia Artificial. 

Éste va a consistir en el desarrollo de una plataforma multiagente donde poner en práctica todas las ideas y algoritmos desarrollados durante los estudios de Grado en Ingeniería Informática. 

En concreto, para la asignatura de Inteligencia Artificial, los alumnos lo usarán para experimentar con los principales algoritmos de búsqueda estudiados. 

Para la asignatura Sistemas Inteligentes, los alumnos podrán desarrollar por completo un agente software, o varios, para poner en práctica los conocimientos aprendidos en la asignatura. 

Sobre la plataforma, se desarrollará en Java. 
Simulará el juego "Capture the Flag". 
El servidor será un agente sofware JADE. 
Recibirá jugadores y simulará las partidas. 
También se desarrollará un agente gráfico, para ver la partida. 
La comunicación entre agentes se basará en la biblioteca JADE. 
La parte gráfica se desarrollará mediante la biblioteca PROCESSING.


Objetivos: 
- Estudio y desarrollo de la plataforma base y arquitectura del sistema. 
- Integración con entorno gráfico. 
- Desarrollo de esquemas de programación. 
- Elaboración de algunas prácticas "tipo". 
- Redactar documentación del funcionamiento del sistema. 
- Implantación de la plataforma en la web.

## Blog
Para estar al tanto de la evolución del proyecto, puede vistar la página:
http://opendatalab.uhu.es/index.php/Captura_la_bandera

## Instructions
Archivo con las instrucciones de la plataforma: [Instrucciones.pdf](Instrucciones.pdf)

## Demo Video
Youtube: [https://youtu.be/K_xbm8FObIE](Vídeo ejecución)

## Getting Started

Las carpetas "ctf_monitor" y "ctf_servidor" corresponden a los 2 proyectos desarrollados en Eclipse Mars. 
- El proyecto "ctf_servidor" corresponde al agente servidor que administrará las partidas.
- El proyecto "ctf_monitor" corresponde al agente monitor, que se conectará al servidor y visualizará la partida.

### Prerequisites

La carpeta "lib" contiene todas las dependencias de los proyectos. 

- JRE: JavaSE-1.8

- JADE: JAVA Agent DEvelopment Framework is an open source platform for peer-to-peer agent based applications.

Link: http://jade.tilab.com/

- Processing: Processing is a flexible software sketchbook and a language for learning how to code within the 
context of the visual arts.
» Free to download and open source

Link: https://processing.org/

- Minim: Minim is an audio library that uses the JavaSound API, a bit of Tritonus, and Javazoom’s MP3SPI to 
provide an easy to use audio library for people developing in the Processing environment.
Minim is licensed under the GNU Lesser General Public License (LGPL), a copy of which is included with the 
distribution.

Link: http://code.compartmental.net/minim/



### Installing

Para instalar este programa en su equipo, simplemente descargue los proyectos de Eclipse e importelos al WorkSpace deseado.
Luego, podrá exportarlo como jar para ejecutarlo.

## Running

Excepcionalmente, se ha subido la carpeta "CTF_2019_v5.0", la cuál contiene todos los archivos necesarios para la ejecución de la plataforma.
Para ello:
1. Configuramos el servidor mediante su archivo .cfg
2. Configuramos el monitor mediante su archivo .cfg
3. Ejecutamos el archivo Servidor.bat
4. Ejecutamos el archivo Monitor.bat
5. Si todo ha ido bien, veremos la partida. Solo queda lanzar los jugadores. Para ello, ejecutamos el .bat deseado de los jugadores.


## Versioning

Version 5.0

## Authors

* **Javier Martín Moreno** 

## License

This project is licensed under the GNU General Public License (GPL) version 3 - see the [LICENSE.md](LICENSE.md) file for details

