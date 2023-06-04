import React, { useRef, useEffect } from 'react';
import { Chart as ChartJS, registerables } from 'chart.js';
import 'chartjs-adapter-date-fns';
import '../Css/Chart.css';

ChartJS.register(...registerables);

function Chart( {rate}) {
    const chartRef = useRef(null);
    const myChart = useRef(null);
    const time = rate.map((item, index) => `${index}s`);

    console.log(time);  
    console.log(rate); 
    useEffect(() => {
        // Prepare the data
        const data = {
            labels: time, 
            datasets: [{
                label: 'Score',
                data: rate, 
                fill: false,
                borderColor: '#f386fd', 
                tension: 0.1
            }]
        };

        const chartInstance = new ChartJS(chartRef.current, {
            type: 'line',
            data: data,
            options: {
                
                scales: {
                    x: {
                        type: 'category',
                        title: {
                            display: true,
                            text: 'Time (seconds)'
                        },
                        ticks: {
                            stepSize: 1, 
                        },
                    },
                    y: {
                        title: {
                            display: true,
                            text: 'Score'
                        },
                        min: 0,
                        max: 100, 
                    }
                }
            }
        });

        myChart.current = chartInstance;

        return () => {
            myChart.current.destroy();
        };
    }, [time, rate]); 

    return (
        <div className='chWrapper'>
            <canvas ref={chartRef} className='chContents'/>
        </div>
    );
}

function Chart2({rate}) {
    const chartRef = useRef(null);
    const myChart = useRef(null);
    const part = ["왼쪽 손목", "왼쪽 팔꿈치", "왼쪽 어깨", "왼쪽 엉덩이", "왼쪽 무릎", "왼쪽 발목", "오른쪽 손목", "오른쪽 팔꿈치", "오른쪽 어깨", "오른쪽 엉덩이", "오른쪽 무릎", "오른쪽 발목", "코"];

    const colors = [
        '#F7C8E0', '#DFFFD8', '#B4E4FF', '#95BDFF', '#FFD4B2', 
        '#FFF6BD', '#CEEDC7', '#86C8BC', '#B8E8FC', '#B1AFFF',
        '#AAC4FF', '#D2DAFF', '#B1B2FF'
    ];

    console.log(rate); 
    useEffect(() => {
        // Prepare the data
        const data = {
            labels: part, 
            datasets: [{
                label: 'Score',
                data: rate, 
                fill: false,
                borderColor: '#f386fd', 
                backgroundColor: colors,
            }]
        };

        const chartInstance = new ChartJS(chartRef.current, {
            type: 'bar',
            data: data,
            options: {
                
                scales: {
                    x: {
                        type: 'category',
                        title: {
                            display: true,
                            text: 'Part'
                        },
                        ticks: {
                            stepSize: 1, // Set step size to 1 seconds
                        },
                    },
                    y: {
                        title: {
                            display: true,
                            text: 'Score'
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
    }, [part, rate]); 

    return (
        <div className='chWrapper'>
            <canvas ref={chartRef} className='chContents'/>
        </div>
    );
}

export {Chart, Chart2};