import React, { useRef, useEffect } from 'react';
import { TimeChart as ChartJS, registerables } from 'chart.js';
import 'chartjs-adapter-date-fns';
import '../Css/Chart.css';

ChartJS.register(...registerables);

function TimeChart({ time, rate }) {
    const chartRef = useRef(null);
    const myChart = useRef(null);

    useEffect(() => {
        // Prepare the data
        const data = {
            labels: time, // Use time prop for labels
            datasets: [{
                label: 'Accuracy',
                data: rate, // Use rate prop for data
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
    }, [time, rate]); // Add time and rate to the dependency array to update the chart when these props change

    return (
        <div className='chWrapper'>
            <canvas ref={chartRef} className='chContents'/>
        </div>
    );
}

export default TimeChart;