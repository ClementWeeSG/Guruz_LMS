# Demo Library Management System (Reporting System) For IS202 Module

This application is done for the IS202 Module on Data Management at Singapore Management University. it consists of two parts:

* A REST Backend done in PHP 5.4
* A Frontend done in HTML 5 and Javascript

## Frontend Module

The Frontend module is coded in [Scala](http://scala-lang.org), and transpiled to Javascript using the [ScalaJS](https://www.scala-js.org/) compiler. It is built on top of the [Udash Web Framework](http://guide.udash.io).

Its consists of the following source files:

* All the files in [*frontend/src/main/scala*](frontend/src/main/scala)
* [*frontend/src/main/assets/index.html*](frontend/src/main/assets/index.html)
*  [*frontend/src/main/assets/styles/guruz-sidebar.css*](frontend/src/main/assets/styles/guruz-sidebar.css)

### UI Features

* Collapsible sidebar navigation with icons.
* Auto-updating tables based on inputs
* Auto-changing URLs based on inputs
* Self-validating text inputs (For Qn 6 only)

### Functionality

Functionality was based on the Assignment requirements:

* For Qn 5 - Auto-populated dropdown of Card ID which auto-populates a table showing Member Details and Transaction Details based on the selected value. Where replacements are NULL, the screen shows a user-friendly "(None)" for replacements in the Member Details Panel.

* For Qn 6 - A pair of self-validating text-inputs for a data range that auto-updates a table showing the books with the highest lend and renew rates. The validation prevents the table from throwing an error.

* For Qn 7 - Auto-populated dropdown of Item Type that auto-updates a table showing the various details of items in a series.

* For Qn 8 - The function is a tracking list for use in a School Visits Program, showing the list of schools not visited by libraries and the items not yet brought by a library on a school visit.

## Backend Module

The backend module is coded in [PHP 5](htttp://www.php.net). It makes use of the native built mysql1 extension and the [Flight HTTP](http://flightphp.com) micro-framework.

The custom code consists of the following files in the `src/main/assets/api` directory:
* index.php - contains the main application code, including *all* SQL queries.
* init.php - contains some helper functions, mapped using [Flight::map](http://flightphp.com/learn/#extending).

For more info on Flight, please consult the [library documentation](http://flightphp.com/learn).

***NOTE:: The backend module in the backend folder is not part of this project; it is code included in the base project adapted from.***

## Precompiled Package

This repository provides a precompiled version of the package. This is the folder [guruz_lms](guruz_lms).

To deploy, simply copy this entire folder to the Apache Server webroot folder. On Windows WAMP, the default will be `C:\Wamp64\www`.

If you have deployed to another subfolder and/or renamed the folder upon deployment, you need to open [`/index.html`](guruz_lms/index.html) and edit line 9.

By default, line 9 says

```javascript
var lms = {base: "/guruz_lms"};
```
Change "guruz_lms" to your correct path, relative to the webroot directory.

For example, if you place the folder in the Apache webroot directory, but rename it to `php`, then your line 9 should read as follows:

```javascript
var lms = {base: "/php"};
```

## Building and Configuring

The build tool for this project is [sbt](http://www.scala-sbt.org), which is 
set up with a [plugin](http://www.scala-js.org/doc/sbt-plugin.html) 
to enable compilation and packaging of Scala.js web applications. 

For deployment, this project provides additional SBT tasks: `deploy` and `prepareBinaries`.

The `deploy` relies on the SBT setting `apacheServer` to determine where to deploy your folder in, usually a subfolder of the Apache webroot folder.

`prepareBinaries` builds a set of files into your code folder, using the default site name, `guruz_lms`.
