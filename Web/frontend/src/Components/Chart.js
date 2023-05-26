import React, { useRef, useEffect } from 'react';
import { Chart as ChartJS, registerables } from 'chart.js';
import 'chartjs-adapter-date-fns';
import '../Css/Chart.css';

ChartJS.register(...registerables);

function Chart() {
    const chartRef = useRef(null);
    const myChart = useRef(null);

    useEffect(() => {
        // Prepare the data
        const data = {
            labels: Array.from({ length: 12 }, (_, i) => (i + 1) * 5), // Create an array [5, 10, 15, ..., 60]
            datasets: [{
                label: 'Accuracy',
                data: Array.from({ length: 12 }, () => Math.floor(Math.random() * 101)), // Create an array of 60 random values
                fill: false,
                borderColor: '#f386fd', // Change line color
                tension: 0.1
            }]
        };

        const chartInstance = new ChartJS(chartRef.current, {
            type: 'line',
            data: data,
            options: {
                animation: {
                    onComplete: () => {},
                    onProgress: (animation) => {
                        let { currentStep, numSteps } = animation;
                        chartInstance.data.datasets[0].data = data.datasets[0].data.slice(0, Math.ceil(currentStep / numSteps * data.labels.length));
                        chartInstance.update('none');
                    }
                },
                scales: {
                    x: {
                        type: 'linear',
                        title: {
                            display: true,
                            text: 'Time (seconds)'
                        },
                        ticks: {
                            stepSize: 5, // Set step size to 5 seconds
                        },
                    },
                    y: {
                        title: {
                            display: true,
                            text: 'Accuracy (%)'
                        },
                        min: 0,
                        max: 100, // Set maximum value to 100
                    }
                }
            }
        });

        myChart.current = chartInstance;

        return () => {
            myChart.current.destroy();
        };
    }, []);

    return (
        <div className='chartWrapper'>
            <canvas ref={chartRef} className='chartContents'/>
        </div>
    );
}

export default Chart;