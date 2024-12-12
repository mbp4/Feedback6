# Feedback6
 
link al repositorio: https://github.com/mbp4/Feedback6.git

## Descripción 

Para este proyecto se nos pedía modificar el proyecto anterior del gestor de novelas y usuarios haciendo uso de firebase añadiendo un mapa para mostrar lugares de interés a los usuarios. 

## Desarrollo

Para esto se ha reutilizado el programa anterior para tener la lógica inicial, es decir, el inicio de sesión, registro y añadir las novelas nuevas y poder visualizarlas. 

Para añadir el mapa se ha cambiado el botón "Acerca De" que anteriormente nos llevaba a una activity con el autor de la aplicación y asi nos lleva directamente al mapa para que el usuario pueda utilizarlo. 

Por otra parte las novelas tienen un nuevo botón que te lleva a la ubicación marcada en el mapa anteriormente, y podrá visualizarla mediante una chincheta donde se marca el lugar. 

## Cambios importantes

Para poder mostrar el mapa se ha hecho uso de Open Street Map, para esto utilizamos una dependencia y utilizamos los imports de esta implementación para que el mapa sea posible, con esto al añadir ubicaciones se coge mediante longitud y latitud la ubicación que se ha introducido por pantalla, ademas de tener un botón que te lleva a un mapa que muestra la ubicación de Madrid por defecto.

## Firebase

Al añadir una ciudad se han cambiado los atributos en Firebase: 

<img width="1153" alt="Captura de pantalla 2024-12-13 a las 0 15 06" src="https://github.com/user-attachments/assets/ca9c14c6-272a-45f5-ac50-52c9cbf8cd32" />

Donde el atributo pais sera el lugar que el usuario quiera introducir como punto de interes. 
