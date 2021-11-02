package audio.modules.io;

/**
 * Ports können als In-/ und Outputs in etwaigen Geräten vorkommen.
 * Dabei sind die mit \"Input\" benannten Ports nur über eine Methode setztbar und nicht zurückgebbar,
 * während die "Outputs" nur via getter erreichbar sind, nicht allerdings selbst setztbar.
 * Das kammt daher, das ein Output bei der Erstellung eines Geräts deklariert wird
 * und danach nicht mehr geändert werden soll.
 * Ein Input jedoch muss, nach der Erstellung des Geräts, erst gesetzt werden,
 * um überhaupt ein Signal an das Gerät zu senden und kann daher/muss gesetzt werden.
 * Einen Input zurückzugeben wäre an sich möglich und auch nicht gänzlich unlogisch,
 * kann allerdings auch an der Quelle erneit vergeben werden, weswegen der Komplexität wegen darauf verzichtet wird.
 */

public interface Port {

    Port NULL = () -> 0;
    Port ONE = () -> 1;

    double out();

}
