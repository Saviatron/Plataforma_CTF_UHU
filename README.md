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


## Getting Started

Las carpetas "ctf_monitor" y "ctf_servidor" corresponden a los 2 proyectos desarrollados en Eclipse Mars. 
- El proyecto "ctf_servidor" corresponde al agente servidor que administrará las partidas.
- El proyecto "ctf_monitor" corresponde al agente monitor, que se conectará al servidor y visualizará la partida.

### Prerequisites

La carpeta "lib" contiene todas las dependencias de los proyectos. 

```
- JADE: JAVA Agent DEvelopment Framework is an open source platform for peer-to-peer agent based applications.
Link: http://jade.tilab.com/

- Processing: Processing is a flexible software sketchbook and a language for learning how to code within the context of the visual arts.
» Free to download and open source
Link: https://processing.org/

- Minim: Minim is an audio library that uses the JavaSound API, a bit of Tritonus, and Javazoom’s MP3SPI to provide an easy to use audio library for people developing in the Processing environment.
Minim is licensed under the GNU Lesser General Public License (LGPL), a copy of which is included with the distribution.
Link: http://code.compartmental.net/minim/

```

### Installing

A step by step series of examples that tell you how to get a development env running

Say what the step will be

```
Give the example
```

And repeat

```
until finished
```

End with an example of getting some data out of the system or using it for a little demo

## Running the tests

Explain how to run the automated tests for this system

### Break down into end to end tests

Explain what these tests test and why

```
Give an example
```

### And coding style tests

Explain what these tests test and why

```
Give an example
```

## Deployment

Add additional notes about how to deploy this on a live system

## Built With

* [Dropwizard](http://www.dropwizard.io/1.0.2/docs/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [ROME](https://rometools.github.io/rome/) - Used to generate RSS Feeds

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors

* **Billie Thompson** - *Initial work* - [PurpleBooth](https://github.com/PurpleBooth)

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to anyone whose code was used
* Inspiration
* etc

