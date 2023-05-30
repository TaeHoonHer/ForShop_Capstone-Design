import React, { useState, useEffect } from 'react';
import '../Css/ResultVideo.css';

function ResultVideo () {
    const [score, setScore] = useState(0);
    const targetScore = 100; 
    const scoreImg = [
        {src: '/img/res1.gif', alt: '75 ~ 100', level: 4},
        {src: '/img/res2.gif', alt: '50 ~ 75', level: 3},
        {src: '/img/res3.gif', alt: '25 ~ 50', level: 2},
        {src: '/img/res4.gif', alt: '0 ~ 25', level: 1},
    ];

    useEffect(() => {
        const interval = setInterval(() => {
            setScore((prevScore) => prevScore + 10 > targetScore ? targetScore : prevScore + 1);
        }, 100);

        // Cleanup function to clear the interval when the component is unmounted
        return () => clearInterval(interval);
    }, []); // Empty dependency array means this effect runs once when the component is mounted

    let displayImg = scoreImg.find(image => {
        return score <= image.level * 25 && score > (image.level - 1) * 25;
    });

    if (!displayImg) {
        displayImg = scoreImg[scoreImg.length - 1];
    }

    return (
        <div className='RSVideoWrapper'>
            <div className='RSVideoContainer'>
                <div className='RSVideoTitle'>
                    <p>Result of Analysis</p>
                </div>
                <div className='RSVideoBox'>
                    <div className='RSVideo'>
                        <video src ='/img/v1.mp4' loop muted autoPlay className='rsvideo'/>
                    </div>
                    <div className='RSVideo'>
                        <video src ='/img/v2.mp4' loop muted autoPlay className='rsvideo'/>
                    </div>
                </div>
                <div className='RSVideoPoint'>
                    <img src = {displayImg.src} alt={displayImg.alt}/>
                    <p>총점 : &nbsp;&nbsp;&nbsp;{score} score</p>
                </div>
            </div>
        </div>
    )
};

export default ResultVideo;
