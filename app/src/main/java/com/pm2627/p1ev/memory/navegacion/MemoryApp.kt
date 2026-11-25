package com.pm2627.p1ev.memory.navegacion

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.pm2627.p1ev.memory.datos.NivelesRepositorio
import com.pm2627.p1ev.memory.model.Nivel
import com.pm2627.p1ev.memory.ui.inicio.InicioScreen
import com.pm2627.p1ev.memory.ui.resultado.ResultadoScreen
import com.pm2627.p1ev.memory.ui.tablero.TableroScreen
import com.pm2627.p1ev.memory.ui.tablero.TableroViewModel

/**
 * LA NAVEGACIÓN de la app con Navigation 3.
 *
 * - La pila de pantallas (back stack, apuntes 3B.1 §1) es una lista de claves. La última es la visible.
 * - rememberNavBackStack la guarda, así sobrevive al giro del móvil.
 * - Decoradores: el de estado guardable (siempre el primero) y el de ViewModel, que da
 *   a cada entrada de la pila su propio ViewModel (3B.1 §8). Así hay UN ViewModel por partida.
 *
 * Botón atrás:
 *   Tablero   -> vuelve a Inicio (se abandona la partida).
 *   Resultado -> vuelve a Inicio (el tablero ya se quitó de la pila al ganar).
 *   Inicio    -> sale de la app (la pila se queda vacía y Android cierra la Activity).
 */
@Composable
fun MemoryApp(modifier: Modifier = Modifier) {
    val niveles = NivelesRepositorio.nivelesPorDefecto
    val pila = rememberNavBackStack(Inicio)

    fun nivelPorId(id: String): Nivel = niveles.firstOrNull { it.id == id } ?: niveles.first()

    NavDisplay(
        backStack = pila,
        modifier = modifier,
        onBack = { pila.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<Inicio> {
                InicioScreen(
                    niveles = niveles,
                    onNivelElegido = { nivel -> pila.add(Tablero(nivelId = nivel.id)) },
                )
            }
            entry<Tablero> { clave ->
                val nivel = nivelPorId(clave.nivelId)
                // viewModel { } busca antes de crear: lo crea con parámetros la primera vez y luego lo reutiliza
                val viewModel = viewModel { TableroViewModel(nivel) }
                TableroScreen(
                    nivel = nivel,
                    viewModel = viewModel,
                    onVolver = { pila.removeLastOrNull() },
                    onPartidaGanada = { movimientos ->
                        // Sustituyo el tablero por el resultado: atrás desde Resultado lleva a Inicio
                        pila.removeLastOrNull()
                        pila.add(Resultado(nivel.id, movimientos))
                    },
                )
            }
            entry<Resultado> { clave ->
                ResultadoScreen(
                    nivel = nivelPorId(clave.nivelId),
                    movimientos = clave.movimientos,
                    onJugarOtraVez = {
                        pila.removeLastOrNull()
                        pila.add(Tablero(clave.nivelId))
                    },
                    onElegirNivel = { volverAInicio(pila) },
                )
            }
        },
    )
}

/** Deja en la pila solo la pantalla de inicio. */
private fun volverAInicio(pila: NavBackStack<NavKey>) {
    while (pila.size > 1) pila.removeLastOrNull()
}