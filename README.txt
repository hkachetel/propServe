Service permettant de gérer les properties des serveurs en temps réel

Les properties seront mutualisées par instance de tomcat (donc par environnement)

Ce service ce compose de trois élément :
 1. le moteur qui permet de gérer les properties, chargement, accès et modification ..
 2. un objet simplifiant les manipulation du moteur et offrant une methode abstraite pour préciser le fichier properties.
 3. Une servlet qui permet de visualiser/modifier les properties a la volée.
 
 Changelog 

V 1.0.0 30/03/2011
- Création du projet