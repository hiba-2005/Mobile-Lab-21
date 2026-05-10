# LAB 21 — Capteurs Embarqués Android
 
## Description
 
Application Android développée en **Java** permettant d'exploiter les capteurs embarqués d'un smartphone.
 
L'application permet :
- d'afficher les capteurs disponibles,
- de lire les données en temps réel,
- d'afficher des graphes,
- de détecter les mouvements,
- de créer une boussole numérique,
- de compter les pas,
- de reconnaître une activité simple.
---
 
## Fonctionnalités
 
### Liste des capteurs
 
Affichage des informations techniques :
 
| Champ | Description |
|---|---|
| ID | Identifiant unique du capteur |
| Nom | Nom du capteur |
| Fabricant | Fabricant du capteur |
| Version | Version du capteur |
| Type | Type de capteur |
| Résolution | Résolution du capteur |
| Consommation énergétique | Puissance consommée |
| Maximum Range | Plage maximale de mesure |
| Min Delay | Délai minimum entre deux lectures |
 
### Capteurs supportés
 
- Température
- Humidité
- Proximité
- Champ magnétique
- Accéléromètre
- Gravité
- Gyroscope
- Compteur de pas
- Boussole numérique
### Reconnaissance d'activité
 
Détection simple :
- Téléphone stable
- Marche
- Saut
- Mouvement
---
 
## Architecture du projet
 
```text
com.example.sensors
│
├── MainActivity.java
│
├── fragments
│   ├── SensorsListFragment.java
│   ├── SensorGraphFragment.java
│   ├── MotionSensorFragment.java
│   ├── StepCounterFragment.java
│   ├── CompassFragment.java
│   └── ActivityRecognitionFragment.java
│
├── utils
│   └── SensorFormatter.java
│
└── views
    └── LineChartView.java
```
 
---
 
## Technologies utilisées
 
| Technologie | Rôle |
|---|---|
| Android Studio | IDE de développement |
| Java | Langage de programmation |
| Android Sensor API | Accès aux capteurs |
| SensorManager | Gestion des capteurs |
| SensorEventListener | Écoute des événements capteurs |
| Navigation Drawer | Navigation principale |
| CardView | Composants d'interface |
 
---
 
## Configuration
 
### Dépendance Gradle
 
Ajouter dans `build.gradle` :
 
```gradle
implementation 'androidx.cardview:cardview:1.0.0'
```
 
### Permission Android
 
Ajouter dans `AndroidManifest.xml` :
 
```xml
<uses-permission android:name="android.permission.ACTIVITY_RECOGNITION"/>
```
 
---
 
## Interface utilisateur
 
L'application possède :
- un **Navigation Drawer** moderne,
- des cartes **CardView**,
- des **graphes dynamiques**,
- une interface moderne style **dashboard**.
---
 
## Fonctionnement
 
### Température / Humidité / Proximité
 
Affichage :
- valeur actuelle,
- graphe temps réel.
### Accéléromètre / Gravité / Gyroscope
 
Affichage :
- axes X, Y, Z,
- norme du mouvement,
- graphe dynamique.
### Boussole
 
Utilisation :
- accéléromètre,
- magnétomètre.
Affichage :
- angle en degrés,
- direction (Nord, Sud, Est, Ouest).
### Compteur de pas
 
Affichage :
- pas depuis démarrage,
- pas de la session.
### Reconnaissance d'activité
 
Détection basée sur l'accéléromètre :
- marche,
- saut,
- stabilité,
- mouvement.
---
 
## Résultats obtenus
 
- ✅ Lecture des capteurs en temps réel
- ✅ Affichage graphique moderne
- ✅ Détection du mouvement
- ✅ Navigation moderne
- ✅ Interface professionnelle
- ✅ Application pédagogique complète

  

https://github.com/user-attachments/assets/1537915e-34c2-4d00-b2a7-d03959c5f47e


---
 
## Auteur
 Nom: Ouirouane Hiba
> Projet réalisé dans le cadre du cours : **Programmation Mobile — Android avec Java**
