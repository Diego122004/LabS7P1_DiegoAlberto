package labs7p1_diegoalberto;

import java.util.Random;
import java.util.Scanner;

public class LabS7P1_DiegoAlberto {

    static Scanner scanner = new Scanner(System.in);
    static char[][] laberinto = {
        {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
        {'#', 'C', '#', ' ', ' ', ' ', '#', '#', ' ', '#'},
        {'#', ' ', '#', ' ', '#', '#', '#', ' ', ' ', '&'},
        {'#', ' ', ' ', ' ', ' ', ' ', '#', ' ', '#', '#'},
        {'#', ' ', ' ', ' ', '#', ' ', '#', ' ', ' ', '#'},
        {'#', ' ', ' ', ' ', '#', ' ', '#', ' ', ' ', '#'},
        {'#', ' ', '#', ' ', '#', ' ', '#', '#', ' ', '#'},
        {'#', ' ', '#', ' ', '#', ' ', ' ', ' ', ' ', '#'},
        {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#'}
    };

    // Obtener las dimensiones del laberinto
    static int filas = laberinto.length;
    static int columnas = laberinto[0].length;

    // Encontrar la posición inicial del jugador (carácter 'C')
    static int filaJugador = -1;
    static int columnaJugador = -1;

    public static void main(String[] args) {

        int opci = 0;
        System.out.println("1.D&D");
        System.out.println("2.Laberinto");
        System.out.println("3.Salir");
        opci = scanner.nextInt();
        while (opci != 3) {
            switch (opci) {
                case 1: {
                    char[][] tablero = new char[10][10];
                    int profundidad = 1;
                    int vida = 0;
                    int energia = 0;
                    int cantidadDragones = 0;

                    // Menú de selección de héroe
                    System.out.println("Selecciona el tipo de héroe:");
                    System.out.println("1. Caballero");
                    System.out.println("2. Mago");
                    int opcion = scanner.nextInt();

                    switch (opcion) {
                        case 1:
                            vida = 250;
                            energia = 50;
                            break;
                        case 2:
                            vida = 150;
                            energia = 230;
                            break;
                        default:
                            System.out.println("Opción inválida. Seleccionando Caballero por defecto.");
                            vida = 250;
                            energia = 50;
                            break;
                    }

                    // Fase de juego
                    while (true) {
                        System.out.println("\nPresiona Enter para avanzar en el calabozo.");
                        scanner.nextLine(); // Consumir el salto de línea pendiente

                        // Fase de avanzar en el calabozo
                        System.out.println("\n--- Fase de Avance ---");
                        System.out.println("Profundidad del calabozo: " + profundidad);
                        int dado = lanzarDado(16);
                        System.out.println("Resultado del dado: " + dado);

                        if (dado % 2 == 0) {
                            System.out.println("No hay dragones en esta casilla.");
                        } else {
                            cantidadDragones = dado / 2;
                            System.out.println("Te encuentras con " + cantidadDragones + " dragones.");

                            // Fase de pelea
                            System.out.println("\n--- Fase de Pelea ---");
                            boolean huye = false;
                            for (int i = 0; i < cantidadDragones; i++) {
                                System.out.println("Presiona Enter para luchar contra el dragón #" + (i + 1));
                                scanner.nextLine(); // Consumir el salto de línea pendiente

                                int probabilidad = lanzarDado(100);
                                System.out.println("Dragón #" + (i + 1) + ": Probabilidad de ganar la pelea: " + probabilidad);

                                if (probabilidad > 50) {
                                    System.out.println("¡Ganaste la pelea contra el dragón!");
                                    energia -= 5;
                                } else {
                                    System.out.println("¡Perdiste la pelea contra el dragón!");
                                    int puntosVidaPerdidos = 25 * cantidadDragones;
                                    vida -= puntosVidaPerdidos;
                                    huye = true;
                                    break; // Sale del bucle si pierde una pelea
                                }
                            }

                            if (huye) {
                                System.out.println("Has huido de los dragones. Pierdes " + (25 * cantidadDragones) + " puntos de vida.");
                            }
                        }

                        // Actualización de vida y energía
                        System.out.println("\n--- Actualización de Estadísticas ---");
                        System.out.println("Vida restante: " + vida);
                        System.out.println("Energía restante: " + energia);

                        // Verificar condiciones de fin de juego
                        if (vida <= 0) {
                            System.out.println("Has perdido toda tu vida. ¡Juego terminado!");
                            break;
                        }

                        if (profundidad == 10) {
                            System.out.println("¡Has completado el calabozo! ¡Juego terminado!");
                            break;
                        }

                        // Actualizar y mostrar el tablero
                        actualizarCalabozo(tablero, profundidad);
                        mostrarCalabozo(tablero);

                        // Avanzar a la siguiente casilla
                        profundidad++;
                    }
                }
                break;
                case 2: {
                    encontrarPosicionInicialJugador();

                    // Bucle principal del juego
                    while (true) {
                        imprimirLaberinto();
                        System.out.print("Ingresa una dirección (w/a/s/d): ");
                        String movimiento = scanner.nextLine();

                        // Actualizar la posición del jugador en función del movimiento ingresado
                        if (movimiento.equals("w")) {
                            if (moverJugador(filaJugador - 1, columnaJugador)) {
                                filaJugador--;
                            }
                        } else if (movimiento.equals("a")) {
                            if (moverJugador(filaJugador, columnaJugador - 1)) {
                                columnaJugador--;
                            }
                        } else if (movimiento.equals("s")) {
                            if (moverJugador(filaJugador + 1, columnaJugador)) {
                                filaJugador++;
                            }
                        } else if (movimiento.equals("d")) {
                            if (moverJugador(filaJugador, columnaJugador + 1)) {
                                columnaJugador++;
                            }
                        } else {
                            System.out.println("Movimiento inválido. Ingresa w/a/s/d.");
                        }

                        // Verificar si el jugador ha alcanzado la salida
                        if (laberinto[filaJugador][columnaJugador] == '#') {
                            System.out.println("Hay una pared");
                            break;
                        } else if (laberinto[filaJugador][columnaJugador] == '&') {
                            System.out.println("¡Felicidades! Has encontrado la salida. Juego terminado.");
                            break;
                        }
                    }
                }
                break;
                case 3: {
                    return; // Salir del programa
                }
                default:
                    System.out.println("Opción inválida. Por favor, selecciona una opción válida.");
                    break;
            }

            // Mostrar el menú nuevamente
            System.out.println("\n1.D&D");
            System.out.println("2.Laberinto");
            System.out.println("3.Salir");
            opci = scanner.nextInt();
        }
    }

    public static int lanzarDado(int caras) {
        Random random = new Random();
        return random.nextInt(caras) + 1;
    }

    public static void actualizarCalabozo(char[][] tablero, int profundidad) {
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[i].length; j++) {
                if (i == profundidad - 1) {
                    tablero[i][j] = '*'; // Casilla actual
                } else {
                    tablero[i][j] = '-'; // Otras casillas
                }
            }
        }
    }

    public static void mostrarCalabozo(char[][] tablero) {
        System.out.println("--- Calabozo ---");
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[i].length; j++) {
                System.out.print(tablero[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Encontrar la posición inicial del jugador (carácter 'C')
    public static void encontrarPosicionInicialJugador() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (laberinto[i][j] == 'C') {
                    filaJugador = i;
                    columnaJugador = j;
                    return;
                }
            }
        }
    }

    // Imprimir el laberinto
    public static void imprimirLaberinto() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                System.out.print(laberinto[i][j]);
            }
            System.out.println();
        }
    }

    // Mover al jugador
    public static boolean moverJugador(int fila, int columna) {
        // Verificar si la nueva posición está dentro de los límites del laberinto
        if (fila < 0 || fila >= filas || columna < 0 || columna >= columnas) {
            return false;
        }

        // Verificar si la nueva posición es una pared
        if (laberinto[fila][columna] == '#') {
            return false;
        }

        // Mover al jugador a la nueva posición
        laberinto[filaJugador][columnaJugador] = ' ';
        laberinto[fila][columna] = 'C';
        return true;
    }
}
