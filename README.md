# Feedback6
 
link al repositorio: https://github.com/mbp4/Feedback6.git

## Descripción 

Para este proyecto se nos pedía modificar el proyecto anterior del gestor de novelas y usuarios haciendo uso de firebase añadiendo un mapa para mostrar lugares de interés a los usuarios. 

## Desarrollo

Para esto se ha reutilizado el programa anterior para tener la lógica inicial, es decir, el inicio de sesión, registro y añadir las novelas nuevas y poder visualizarlas. 

Para añadir el mapa se ha cambiado el botón "Acerca De" que anteriormente nos llevaba a una activity con el autor de la aplicación y asi nos lleva directamente al mapa para que el usuario pueda utilizarlo. 

Por otra parte las novelas tienen un nuevo botón que te lleva a la ubicación marcada en el mapa anteriormente, y podrá visualizarla mediante una chincheta donde se marca el lugar. 

## Cambios importantes

Para poder mostrar el mapa se ha hecho uso de la Api de Google Maps, para esto hemos obtenido una API KEY y se ha introducido en el manifest, además de introducir en el gradle una nueva implementación para que el programa pueda ser posible. 
