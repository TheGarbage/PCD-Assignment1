# PCD-Assignment2
		
	
				
L’assignment consiste nell'affrontare il problema illustrato nel primo assignment utilizzando le tecniche di programmazione asincrona viste nel corso. In particolare:

1) Approccio asincrono a Task – Framework Executors
2) Approccio basato Virtual Threads
3) Approccio asincrono ad eventi (event-loop) – Framework Vert.x o equivalenti   	
4) Approccio basato su programmazione reattiva – Framework RxJava o equivalenti

Per ogni approccio, si richiede che la soluzione sia organizzata in modo da incapsulare la funzionalità di analisi dei sorgenti in una libreria,  che implementi un'interfaccia SourceAnalyser includa due metodi asincroni (in pseudocodice):
* getReport(Directory d)
   * metodo asincrono che deve restituire un report che include i risultati richiesti dall'analisi (gli N sorgenti con il numero maggiore di linee di codice e la distribuzione complessiva – come indicato nell'assignment 01)
* analyzeSources(Directory d)	
   * metodo asincrono che, a differenza del precedente, permetta di effettuare un'analisi incrementale, con produzione incrementale  dei risultati e interrompibile

La progettazione di dettaglio della signature dei metodi – in particolare il tipo di ritorno dei metodi – dipende dall'approccio scelto, ovvero deve sfruttare il più possibile le astrazioni/caratteristiche dell'approccio usato.  

Per ogni approccio, considerare due casi:

* Programma senza GUI, in cui si usa il metodo getReport per computare e visualizzare in standard output il report
* Programma con GUI, in cui si usa il metodo analyzeSources per ottenere incrementalmente i risultati e poter fermare il processo



NOTE 


* Per ogni approccio, concepire soluzioni che sfruttino il più possibile le caratteristiche paradigmatiche dell'approccio stesso, a livello di progettazione e a livello di implementazione.
* Il linguaggio di programmazione di base suggerito è il linguaggio Java, tuttavia per ogni approccio è possibile usare linguaggi di programmazione diversi.


 
LA CONSEGNA


* La consegna consiste in una cartella “Assignment-01” compressa (formato zip)  do sottoporre sul sito del corso, contenente:
* directory src con i sorgenti del programma
* directory doc che contenga una breve relazione in PDF (report.pdf). La relazione deve includere:
   * Descrizione della strategia risolutiva e dell’architettura proposta, sempre focalizzando l’attenzione su aspetti relativi alla concorrenza.
   * Descrizione del comportamento del sistema (ad un certo livello di astrazione)
   * Prove di performance



---------------------------------------------------------------------------------------------------------------------------------------------------------


# PCD-Assignment1
           
           
                                
L’assignment è articolato in due punti:


1. Realizzare un programma concorrente che, data una directory D presente sul file system locale contenente un insieme di sorgenti in Java (considerando anche qualsiasi  sottodirectory, ricorsivamente), provveda a determinare e visualizzare in standard output:
   * gli N sorgenti con il numero maggiore di linee di codice 
   * La distribuzione complessiva relativa a quanti sorgenti hanno un numero di linee di codice che ricade in un certo intervallo, considerando un certo numero di intervalli NI  e un numero massimo MAXL  di linee di codice per delimitare l'estremo sinistro dell'ultimo intervallo.
      *  Esempio: se NI = 5 e MAXL è 1000, allora il primo intervallo è [0,249],il secondo è  [250,499], il terzo è  [500,749], il quarto è [750,999], l'ultimo è [1000,infinito]. La distribuzione determina quanti sorgenti ci sono per ogni intervallo


D, NI e MAXL si presuppone siano parametri del programma, passati da linea di comando.  Nota: In questo primo punto non c'è GUI.


2.  Estendere il programma al punto precedente includendo una GUI con:
* Input box per specificare i parametri
* pulsanti start/stop per avviare/fermare l'elaborazione
* una view ove visualizzare interattivamente l’output aggiornato, mediante 2 frame: 
   * un frame relativo ai file con il maggior numero di linee di codice 
   * un frame relativo alla distribuzione


Per il primo punto, si richiede di eseguire prove di performance, verificando il numero ottimale di thread da utilizzare e la scalabilità della soluzione adottata.


Per entrambi i punti, si richiede di:
* di eseguire verifiche di correttezza di un modello della soluzione adottata,  mediante TLA+
* di eseguire verifiche di correttezza dell'implementazione (semplificata) mediante JPF



NOTE / SPECIFICHE / REQUISITI 

* Adottare un approccio basato su programmazione multi-threaded, adottando, da un lato, principi e metodi di progettazione utili per favorire modularità, incapsulamento e proprietà relative, dall’altro una soluzione che massimizzi le performance e reattività. 
* Come meccanismi di coordinazione prediligere la definizione e uso di monitor  rispetto ad altre soluzioni di più basso livello.
 


LA CONSEGNA
 
 
La consegna consiste in una cartella “Assignment-01” compressa (formato zip)  do sottoporre sul sito del corso, contenente:
* directory src con i sorgenti del programma
* directory doc che contenga una breve relazione in PDF (report.pdf). La relazione deve includere:
   * Analisi del problema, focalizzando in particolare gli aspetti relativi alla concorrenza.
   * Descrizione della strategia risolutiva e dell’architettura proposta, sempre focalizzando l’attenzione su aspetti relativi alla concorrenza.
   * Descrizione del comportamento del sistema (ad un certo livello di astrazione) utilizzando Reti di Petri. 
   * Prove di performance e considerazioni relative.
   * Identificazione di proprietà di correttezza e verifica
