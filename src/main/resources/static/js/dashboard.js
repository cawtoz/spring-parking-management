// Configuración para el gráfico de entradas y salidas por día
const entradasChart = new Chart(document.getElementById('entradas-chart'), {
    type: 'line',
    data: {
        labels: ['Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado', 'Domingo'],
        datasets: [{
            label: 'Entradas',
            data: [45, 39, 42, 49, 52, 38, 35],
            borderColor: '#28a745',
            backgroundColor: 'rgba(40, 167, 69, 0.1)',
            tension: 0.4,
            fill: true,
            borderWidth: 2,
            pointRadius: 4,
            pointHoverRadius: 6
        }, {
            label: 'Salidas',
            data: [38, 42, 40, 46, 48, 40, 32],
            borderColor: '#dc3545',
            backgroundColor: 'rgba(220, 53, 69, 0.1)',
            tension: 0.4,
            fill: true,
            borderWidth: 2,
            pointRadius: 4,
            pointHoverRadius: 6
        }]
    },
    options: {
        responsive: true,
        plugins: {
            title: {
                display: true,
                text: 'Flujo de Vehículos por Día',
                font: {
                    size: 16,
                    weight: 'bold'
                },
                padding: 20
            },
            legend: {
                position: 'top',
                labels: {
                    usePointStyle: true,
                    padding: 20,
                    font: {
                        size: 12
                    }
                }
            },
            tooltip: {
                mode: 'index',
                intersect: false,
                backgroundColor: 'rgba(255, 255, 255, 0.9)',
                titleColor: '#000',
                bodyColor: '#666',
                borderColor: '#ddd',
                borderWidth: 1
            }
        },
        scales: {
            y: {
                beginAtZero: true,
                title: {
                    display: true,
                    text: 'Cantidad de Vehículos',
                    font: {
                        size: 12,
                        weight: 'bold'
                    }
                },
                grid: {
                    drawBorder: false,
                    color: 'rgba(0, 0, 0, 0.05)'
                }
            },
            x: {
                grid: {
                    drawBorder: false,
                    color: 'rgba(0, 0, 0, 0.05)'
                }
            }
        },
        interaction: {
            intersect: false,
            mode: 'index'
        }
    }
});

// Configuración para el gráfico de ocupación por hora
const ocupacionChart = new Chart(document.getElementById('salidas-chart'), {
    type: 'bar',
    data: {
        labels: ['6:00', '8:00', '10:00', '12:00', '14:00', '16:00', '18:00', '20:00', '22:00'],
        datasets: [{
            label: 'Porcentaje de Ocupación',
            data: [20, 45, 75, 85, 80, 90, 85, 60, 30],
            backgroundColor: 'rgba(4, 6, 119, 0.6)',
            borderColor: 'rgba(4, 6, 119, 1)',
            borderWidth: 1,
            borderRadius: 4,
            hoverBackgroundColor: 'rgba(4, 6, 119, 0.8)'
        }]
    },
    options: {
        responsive: true,
        plugins: {
            title: {
                display: true,
                text: 'Porcentaje de Ocupación por Hora',
                font: {
                    size: 16,
                    weight: 'bold'
                },
                padding: 20
            },
            legend: {
                display: false
            },
            tooltip: {
                backgroundColor: 'rgba(255, 255, 255, 0.9)',
                titleColor: '#000',
                bodyColor: '#666',
                borderColor: '#ddd',
                borderWidth: 1,
                callbacks: {
                    label: function(context) {
                        return `Ocupación: ${context.raw}%`;
                    }
                }
            }
        },
        scales: {
            y: {
                beginAtZero: true,
                max: 100,
                title: {
                    display: true,
                    text: '% de Ocupación',
                    font: {
                        size: 12,
                        weight: 'bold'
                    }
                },
                grid: {
                    drawBorder: false,
                    color: 'rgba(0, 0, 0, 0.05)'
                },
                ticks: {
                    callback: function(value) {
                        return value + '%';
                    }
                }
            },
            x: {
                grid: {
                    display: false
                }
            }
        }
    }
});

// Manejador para los botones del selector de período
document.querySelectorAll('.period-selector button').forEach(button => {
    button.addEventListener('click', function() {
        // Remover clase active de todos los botones
        document.querySelectorAll('.period-selector button').forEach(btn => {
            btn.classList.remove('active');
        });
        // Agregar clase active al botón clickeado
        this.classList.add('active');

        // Simulación de actualización de datos
        actualizarDatos(this.textContent.trim());
    });
});

// Función para actualizar datos según el período seleccionado
function actualizarDatos(periodo) {
    // Simulación de diferentes datos para cada período
    let nuevosDatasets;

    switch(periodo) {
        case '30 días':
            nuevosDatasets = {
                entradas: [38, 42, 45, 48, 52, 49, 47],
                salidas: [35, 40, 42, 45, 48, 46, 44],
                ocupacion: [25, 50, 80, 90, 85, 95, 80, 65, 35]
            };
            break;
        case 'Este mes':
            nuevosDatasets = {
                entradas: [42, 45, 48, 51, 54, 50, 48],
                salidas: [40, 43, 45, 48, 50, 47, 45],
                ocupacion: [30, 55, 85, 95, 90, 100, 85, 70, 40]
            };
            break;
        default: // 7 días
            nuevosDatasets = {
                entradas: [45, 39, 42, 49, 52, 38, 35],
                salidas: [38, 42, 40, 46, 48, 40, 32],
                ocupacion: [20, 45, 75, 85, 80, 90, 85, 60, 30]
            };
    }

    // Actualizar datos de los gráficos
    entradasChart.data.datasets[0].data = nuevosDatasets.entradas;
    entradasChart.data.datasets[1].data = nuevosDatasets.salidas;
    ocupacionChart.data.datasets[0].data = nuevosDatasets.ocupacion;

    // Animar la actualización
    entradasChart.update('active');
    ocupacionChart.update('active');

    // Actualizar estadísticas
    actualizarEstadisticas(nuevosDatasets);
}

// Función para actualizar las estadísticas mostradas
function actualizarEstadisticas(datos) {
    // Calcular promedios
    const promedioEntradas = datos.entradas.reduce((a, b) => a + b, 0) / datos.entradas.length;
    const promedioSalidas = datos.salidas.reduce((a, b) => a + b, 0) / datos.salidas.length;
    const promedioOcupacion = datos.ocupacion.reduce((a, b) => a + b, 0) / datos.ocupacion.length;

    // Actualizar valores en el DOM si existen los elementos
    const elementosEstadisticas = {
        'promedio-entradas': Math.round(promedioEntradas),
        'promedio-salidas': Math.round(promedioSalidas),
        'promedio-ocupacion': Math.round(promedioOcupacion) + '%'
    };

    for (let [id, valor] of Object.entries(elementosEstadisticas)) {
        const elemento = document.getElementById(id);
        if (elemento) {
            elemento.textContent = valor;
        }
    }
}